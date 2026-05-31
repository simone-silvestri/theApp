package com.simone.cfts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Single source of cloud truth. Every UI mutation goes through {@link DatabaseHelper}
 * first (local SQLite), then through one of the {@code notify*} methods here, which
 * pushes the change to Firestore. A snapshot listener per collection writes remote
 * changes back into SQLite and broadcasts {@link #ACTION_DATA_CHANGED} so open
 * Activities can refresh.
 *
 * Conflict resolution: last-write-wins by Firestore server {@code updatedAt}.
 */
public class SyncManager {
    private static final String TAG = "SyncManager";

    public static final String ACTION_DATA_CHANGED = "com.simone.cfts.DATA_CHANGED";

    private static final String PREF_MERGE_DONE = "sync.merge_done";
    private static final String PREF_BACKFILL_DONE = "sync.backfill_calendar_calories_v2";

    private static SyncManager instance;

    public static synchronized SyncManager get(Context context) {
        if (instance == null) {
            instance = new SyncManager(context.getApplicationContext());
        }
        return instance;
    }

    private final Context appContext;
    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;
    private final DatabaseHelper db;
    private final SharedPreferences prefs;
    private final boolean firebaseReady;

    private final List<ListenerRegistration> listeners = new ArrayList<>();
    private boolean connected = false;

    private SyncManager(Context appContext) {
        this.appContext = appContext;
        this.db = DatabaseHelper.getInstance(appContext);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(appContext);

        FirebaseAuth a = null;
        FirebaseFirestore f = null;
        boolean ready = false;
        try {
            if (!FirebaseApp.getApps(appContext).isEmpty()) {
                a = FirebaseAuth.getInstance();
                f = FirebaseFirestore.getInstance();
                ready = true;
            } else {
                Log.w(TAG, "Firebase not configured — drop google-services.json into app/ and apply the plugin");
            }
        } catch (Throwable t) {
            Log.e(TAG, "Firebase init failed; cloud sync disabled", t);
        }
        this.auth = a;
        this.firestore = f;
        this.firebaseReady = ready;
    }

    public boolean isFirebaseReady() { return firebaseReady; }

    // ---------- public state ----------

    public boolean isSignedIn() {
        return firebaseReady && auth.getCurrentUser() != null;
    }

    public boolean isConnected() { return connected; }

    public String currentUserEmail() {
        if (!firebaseReady) return null;
        FirebaseUser u = auth.getCurrentUser();
        return u != null ? u.getEmail() : null;
    }

    // ---------- lifecycle ----------

    /** Call after a successful Firebase sign-in. Runs the smart-merge flow and attaches listeners. */
    public void onSignedIn(Activity caller) {
        if (!isSignedIn()) return;
        notifyProfile();
        runSmartMerge(caller);
        backfillIfNeeded();
    }

    /**
     * One-shot upload of local calendar + calories to cloud, for users who signed in before
     * the upload sweeps were implemented. Idempotent (uses INSERT OR REPLACE keyed by id),
     * so re-runs cost only redundant Firestore writes.
     */
    private void backfillIfNeeded() {
        if (prefs.getBoolean(PREF_BACKFILL_DONE, false)) return;
        uploadAllCalendar();
        uploadAllCalories();
        uploadAllExerciseCatalog();
        prefs.edit().putBoolean(PREF_BACKFILL_DONE, true).apply();
    }

    private void notifyProfile() {
        if (!isSignedIn()) return;
        FirebaseUser u = auth.getCurrentUser();
        Map<String, Object> doc = new HashMap<>();
        doc.put("email", u.getEmail());
        doc.put("displayName", u.getDisplayName());
        doc.put("lastSeen", FieldValue.serverTimestamp());
        doc.put("schemaVersion", 1);
        firestore.collection("users").document(u.getUid())
                .set(doc, SetOptions.merge())
                .addOnFailureListener(e -> Log.e(TAG, "profile upsert failed", e));
    }

    public void onSignedOut() {
        detachListeners();
        connected = false;
    }

    // ---------- mutation notifications (called by UI after local write) ----------

    public void notifyWorkoutUpsert(Workout w) {
        if (!isSignedIn()) return;
        String wid = String.valueOf(w.getID());
        DocumentReference workoutDoc = userDoc("workouts").document(wid);
        CollectionReference exes = workoutDoc.collection("exercises");

        // Single atomic batch: delete old exercises, set workout doc, set new exercises.
        // Avoids the race where the listener sees workout doc but empty exercises.
        exes.get().addOnSuccessListener(snap -> {
            WriteBatch batch = firestore.batch();
            for (QueryDocumentSnapshot d : snap) batch.delete(d.getReference());

            Map<String, Object> doc = new HashMap<>();
            doc.put("title", w.getTitle());
            doc.put("type", w.getType());
            doc.put("difficulty", w.getDifficulty());
            doc.put("totalTime", w.getTotalTime());
            doc.put("numberOfSets", w.getNumberOfSets());
            doc.put("setPause", w.getSetPause());
            doc.put("wod", w.getWod());
            doc.put("updatedAt", FieldValue.serverTimestamp());
            batch.set(workoutDoc, doc, SetOptions.merge());

            int idx = 0;
            for (Exercise e : w.getExercises()) {
                Map<String, Object> ed = new HashMap<>();
                ed.put("name", e.getName());
                ed.put("time", e.getTimeInSeconds());
                ed.put("pause", e.getPauseInSeconds());
                ed.put("reps", e.getReps());
                ed.put("updatedAt", FieldValue.serverTimestamp());
                batch.set(exes.document(String.valueOf(idx++)), ed);
            }
            batch.commit().addOnFailureListener(e -> Log.e(TAG, "workout upsert failed", e));
        }).addOnFailureListener(e -> Log.e(TAG, "workout exercises preflight failed", e));
    }

    public void notifyWorkoutDelete(int workoutId) {
        if (!isSignedIn()) return;
        DocumentReference doc = userDoc("workouts").document(String.valueOf(workoutId));
        doc.collection("exercises").get().addOnSuccessListener(snap -> {
            WriteBatch batch = firestore.batch();
            for (QueryDocumentSnapshot d : snap) batch.delete(d.getReference());
            batch.delete(doc);
            batch.commit();
        });
    }

    public void notifyCalendarAdd(long rowId, String isoDate, int workoutId) {
        if (!isSignedIn()) return;
        Map<String, Object> doc = new HashMap<>();
        doc.put("day", isoDate);
        doc.put("workoutId", workoutId);
        doc.put("updatedAt", FieldValue.serverTimestamp());
        userDoc("calendar").document(String.valueOf(rowId))
                .set(doc, SetOptions.merge())
                .addOnFailureListener(e -> Log.e(TAG, "calendar add failed", e));
    }

    public void notifyCalendarRemove(int rowId) {
        if (!isSignedIn()) return;
        userDoc("calendar").document(String.valueOf(rowId))
                .delete()
                .addOnFailureListener(e -> Log.e(TAG, "calendar remove failed", e));
    }

    public void notifyMealKcal(String isoDate, int meal, int kcal) {
        if (!isSignedIn()) return;
        Map<String, Object> doc = new HashMap<>();
        doc.put("date", isoDate);
        doc.put("meal", meal);
        doc.put("kcal", kcal);
        doc.put("updatedAt", FieldValue.serverTimestamp());
        userDoc("calories").document(isoDate + "_" + meal)
                .set(doc, SetOptions.merge())
                .addOnFailureListener(e -> Log.e(TAG, "meal upsert failed", e));
    }

    public void notifyMealClear(String isoDate, int meal) {
        if (!isSignedIn()) return;
        userDoc("calories").document(isoDate + "_" + meal)
                .delete()
                .addOnFailureListener(e -> Log.e(TAG, "meal delete failed", e));
    }

    public void notifyExerciseCatalogUpsert(ExerciseDetail exe) {
        if (!isSignedIn()) return;
        if (exe == null || exe.getName() == null || exe.getName().isEmpty()) return;
        Map<String, Object> doc = new HashMap<>();
        doc.put("name", exe.getName());
        doc.put("difficulty", exe.getDifficulty());
        doc.put("description", exe.getDescription());
        doc.put("muscle", exe.getMuscle());
        doc.put("updatedAt", FieldValue.serverTimestamp());
        userDoc("exerciseCatalog").document(sanitizeDocId(exe.getName()))
                .set(doc, SetOptions.merge())
                .addOnFailureListener(e -> Log.e(TAG, "exercise catalog upsert failed", e));
    }

    private static String sanitizeDocId(String s) {
        return s.replace("/", "_").replace(".", "_");
    }

    public void notifyGoalsChanged(int dailyKcalGoal, int monthlyWorkoutGoal) {
        if (!isSignedIn()) return;
        Map<String, Object> doc = new HashMap<>();
        doc.put("dailyKcalGoal", dailyKcalGoal);
        doc.put("monthlyWorkoutGoal", monthlyWorkoutGoal);
        doc.put("updatedAt", FieldValue.serverTimestamp());
        userDoc("settings").document("main")
                .set(doc, SetOptions.merge())
                .addOnFailureListener(e -> Log.e(TAG, "settings upsert failed", e));
    }

    // ---------- smart merge ----------

    private void runSmartMerge(Activity caller) {
        boolean alreadyDone = prefs.getBoolean(PREF_MERGE_DONE, false);

        userDoc("workouts").get().addOnSuccessListener(cloudWorkouts -> {
            boolean cloudHasData = !cloudWorkouts.isEmpty();
            boolean localHasData = !db.loadDatabase().isEmpty();

            if (alreadyDone || (!cloudHasData && !localHasData)) {
                attachListeners();
                return;
            }
            if (localHasData && !cloudHasData) {
                uploadAllToCloud();
                prefs.edit().putBoolean(PREF_MERGE_DONE, true).apply();
                attachListeners();
                return;
            }
            if (!localHasData && cloudHasData) {
                // Just attach listeners — the workouts listener will pull data into SQLite.
                prefs.edit().putBoolean(PREF_MERGE_DONE, true).apply();
                attachListeners();
                return;
            }
            // Both have data — ask the user.
            showMergeChoice(caller);
        }).addOnFailureListener(e -> {
            Log.e(TAG, "smart merge: cloud probe failed", e);
            attachListeners();
        });
    }

    private void showMergeChoice(Activity caller) {
        View content = LayoutInflater.from(caller).inflate(R.layout.popup_three_actions, null);
        TextView text = content.findViewById(R.id.text_id);
        text.setText("This phone and the cloud both have workouts. Which set do you want to keep?");
        TextView one   = content.findViewById(R.id.button_one);
        TextView two   = content.findViewById(R.id.button_two);
        TextView three = content.findViewById(R.id.button_three);
        one.setText("This phone");
        two.setText("Cloud");
        three.setText("Merge both");

        final PopupWindow pw = new PopupWindow(content,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new ColorDrawable(0));
        pw.setAnimationStyle(0);
        pw.showAtLocation(caller.findViewById(android.R.id.content),
                Gravity.CENTER, 0, 0);

        one.setOnClickListener(v -> { pw.dismiss(); clearCloudThenUpload(); prefs.edit().putBoolean(PREF_MERGE_DONE, true).apply(); attachListeners(); });
        two.setOnClickListener(v -> { pw.dismiss(); clearLocalThenAttach(); prefs.edit().putBoolean(PREF_MERGE_DONE, true).apply(); });
        three.setOnClickListener(v -> { pw.dismiss(); uploadAllToCloud(); prefs.edit().putBoolean(PREF_MERGE_DONE, true).apply(); attachListeners(); });
    }

    private void uploadAllToCloud() {
        ArrayList<Workout> workouts = db.loadDatabase();
        for (Workout w : workouts) notifyWorkoutUpsert(w);
        int dailyGoal   = prefs.getInt(HealthActivity.PREF_GOAL, HealthActivity.DEFAULT_GOAL);
        int monthlyGoal = prefs.getInt(Calendar.PREF_MONTHLY_GOAL, Calendar.DEFAULT_MONTHLY_GOAL);
        notifyGoalsChanged(dailyGoal, monthlyGoal);
        // Calendar + calories handled by the per-entity notifications elsewhere; on first upload
        // we sweep them too.
        uploadAllCalendar();
        uploadAllCalories();
    }

    private void uploadAllCalendar() {
        for (DatabaseHelper.CalendarRow row : db.loadAllCalendarRows()) {
            notifyCalendarAdd(row.rowId, row.day, row.workoutId);
        }
    }

    private void uploadAllCalories() {
        for (DatabaseHelper.CalorieRow row : db.loadAllCalorieRows()) {
            notifyMealKcal(row.date, row.meal, row.kcal);
        }
    }

    private void uploadAllExerciseCatalog() {
        for (ExerciseDetail exe : db.loadAllExercises()) {
            notifyExerciseCatalogUpsert(exe);
        }
    }

    private void clearCloudThenUpload() {
        // Delete all docs under users/{uid}/workouts, calendar, calories, settings — then upload local.
        deleteSubcollection("workouts", () -> {
            deleteSubcollection("calendar", () -> {
                deleteSubcollection("calories", () -> {
                    deleteSubcollection("settings", () -> uploadAllToCloud());
                });
            });
        });
    }

    private void clearLocalThenAttach() {
        // Wipe local SQLite for synced tables; listeners will re-populate from cloud.
        db.deleteDatabase();
        db.deleteCalendarTables();
        // No public API to clear all calories — but on listener fire-back, cloud rows will be set.
        attachListeners();
    }

    private void deleteSubcollection(String name, Runnable done) {
        userDoc(name).get().addOnSuccessListener(snap -> {
            WriteBatch batch = firestore.batch();
            for (QueryDocumentSnapshot d : snap) batch.delete(d.getReference());
            batch.commit().addOnCompleteListener(t -> done.run());
        }).addOnFailureListener(e -> done.run());
    }

    // ---------- listeners ----------

    private void attachListeners() {
        detachListeners();
        if (!isSignedIn()) return;
        connected = true;

        listeners.add(userDoc("workouts").addSnapshotListener((snap, err) -> {
            if (err != null || snap == null) return;
            for (DocumentChange c : snap.getDocumentChanges()) {
                if (c.getType() == DocumentChange.Type.REMOVED) {
                    try {
                        int id = Integer.parseInt(c.getDocument().getId());
                        db.deleteWorkoutById(id);
                    } catch (Exception e) { Log.e(TAG, "delete workout local failed", e); }
                } else {
                    applyRemoteWorkout(c.getDocument());
                }
            }
            broadcastChanged();
        }));
        listeners.add(userDoc("calendar").addSnapshotListener((snap, err) -> {
            if (err != null || snap == null) return;
            for (DocumentChange c : snap.getDocumentChanges()) {
                if (c.getType() == DocumentChange.Type.REMOVED) {
                    try { db.removeCalendarEntry(Integer.parseInt(c.getDocument().getId())); }
                    catch (Exception e) { Log.e(TAG, "delete calendar local failed", e); }
                } else {
                    applyRemoteCalendar(c.getDocument());
                }
            }
            broadcastChanged();
        }));
        listeners.add(userDoc("exerciseCatalog").addSnapshotListener((snap, err) -> {
            if (err != null || snap == null) return;
            for (DocumentChange c : snap.getDocumentChanges()) {
                if (c.getType() == DocumentChange.Type.REMOVED) {
                    String name = c.getDocument().getString("name");
                    if (name != null) {
                        try { db.removeExerciseFromCatalog(name); }
                        catch (Exception e) { Log.e(TAG, "delete catalog local failed", e); }
                    }
                } else {
                    applyRemoteExerciseCatalog(c.getDocument());
                }
            }
            broadcastChanged();
        }));
        listeners.add(userDoc("calories").addSnapshotListener((snap, err) -> {
            if (err != null || snap == null) return;
            for (DocumentChange c : snap.getDocumentChanges()) {
                if (c.getType() == DocumentChange.Type.REMOVED) {
                    String[] parts = c.getDocument().getId().split("_");
                    if (parts.length == 2) {
                        try { db.clearMealKcal(parts[0], Integer.parseInt(parts[1])); }
                        catch (Exception e) { Log.e(TAG, "delete calorie local failed", e); }
                    }
                } else {
                    applyRemoteCalorie(c.getDocument());
                }
            }
            broadcastChanged();
        }));
        listeners.add(userDoc("settings").document("main").addSnapshotListener((snap, err) -> {
            if (err != null || snap == null || !snap.exists()) return;
            applyRemoteSettings(snap);
            broadcastChanged();
        }));
    }

    private void detachListeners() {
        for (ListenerRegistration r : listeners) r.remove();
        listeners.clear();
    }

    private void applyRemoteWorkout(DocumentSnapshot d) {
        try {
            final int cloudId = Integer.parseInt(d.getId());
            Workout w = new Workout();
            w.setID(cloudId);
            w.setTitle(d.getString("title"));
            w.setType(d.getString("type"));
            Long diff  = d.getLong("difficulty");
            Long total = d.getLong("totalTime");
            Long sets  = d.getLong("numberOfSets");
            Long pause = d.getLong("setPause");
            w.setDifficulty(diff != null ? diff.intValue() : 0);
            w.setTotalTime(total != null ? total.intValue() : 0);
            w.setNumberOfSets(sets != null ? sets.intValue() : 0);
            w.setSetPause(pause != null ? pause.intValue() : 0);
            w.setWod(d.getString("wod"));
            db.upsertWorkoutWithId(w);

            // Pull the exercises subcollection so reinstalled clients see them.
            d.getReference().collection("exercises").get().addOnSuccessListener(exSnap -> {
                db.removeExercisesByWorkoutId(cloudId);
                for (DocumentSnapshot ex : exSnap.getDocuments()) {
                    Exercise e = new Exercise();
                    e.setName(ex.getString("name"));
                    Long time = ex.getLong("time");
                    Long pauseSec = ex.getLong("pause");
                    Long reps = ex.getLong("reps");
                    e.setTimeInSeconds(time != null ? time.intValue() : 0);
                    e.setPauseInSeconds(pauseSec != null ? pauseSec.intValue() : 0);
                    e.setReps(reps != null ? reps.intValue() : 0);
                    e.setWorkoutId(cloudId);
                    db.addExerciseInWorkoutById(e, cloudId);
                }
                broadcastChanged();
            });
        } catch (Exception e) {
            Log.e(TAG, "applyRemoteWorkout", e);
        }
    }

    private void applyRemoteExerciseCatalog(DocumentSnapshot d) {
        try {
            String name = d.getString("name");
            if (name == null || name.isEmpty()) return;
            ExerciseDetail exe = new ExerciseDetail();
            exe.setName(name);
            Long diff = d.getLong("difficulty");
            exe.setDifficulty(diff != null ? diff.intValue() : -1);
            exe.setDescription(d.getString("description"));
            exe.setMuscle(d.getString("muscle"));
            db.addOrUpdateExercise(exe);
        } catch (Exception e) {
            Log.e(TAG, "applyRemoteExerciseCatalog", e);
        }
    }

    private void applyRemoteCalendar(DocumentSnapshot d) {
        try {
            int rowId = Integer.parseInt(d.getId());
            String day = d.getString("day");
            Long workoutId = d.getLong("workoutId");
            if (day != null && workoutId != null) {
                db.upsertCalendarEntry(rowId, day, workoutId.intValue());
            }
        } catch (Exception e) {
            Log.e(TAG, "applyRemoteCalendar", e);
        }
    }

    private void applyRemoteCalorie(DocumentSnapshot d) {
        try {
            String date = d.getString("date");
            Long meal = d.getLong("meal");
            Long kcal = d.getLong("kcal");
            if (date != null && meal != null && kcal != null) {
                db.setMealKcal(date, meal.intValue(), kcal.intValue());
            }
        } catch (Exception e) {
            Log.e(TAG, "applyRemoteCalorie", e);
        }
    }

    private void applyRemoteSettings(DocumentSnapshot d) {
        try {
            Long daily   = d.getLong("dailyKcalGoal");
            Long monthly = d.getLong("monthlyWorkoutGoal");
            SharedPreferences.Editor ed = prefs.edit();
            if (daily   != null) ed.putInt(HealthActivity.PREF_GOAL, daily.intValue());
            if (monthly != null) ed.putInt(Calendar.PREF_MONTHLY_GOAL, monthly.intValue());
            ed.apply();
        } catch (Exception e) {
            Log.e(TAG, "applyRemoteSettings", e);
        }
    }

    private void broadcastChanged() {
        appContext.sendBroadcast(new Intent(ACTION_DATA_CHANGED).setPackage(appContext.getPackageName()));
    }

    // ---------- helpers ----------

    private CollectionReference userDoc(String collection) {
        String uid = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "anon";
        return firestore.collection("users").document(uid).collection(collection);
    }
}
