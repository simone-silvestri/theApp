package com.simone.cfts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    // Database Info
    private static final String DATABASE_NAME = "workexeDatabase.db";
    private static final int DATABASE_VERSION = 3;

    // Table Names
    private static final String TABLE_WORK = "Workouts";
    private static final String TABLE_EXE  = "Exercises";
    private static final String TABLE_REL  = "WorkoutExercises";
    private static final String TABLE_CAL  = "Calendar";

    // Calories Table
    private static final String TABLE_CALORIES = "Calories";
    private static final String KEY_CAL_DATE = "date";
    private static final String KEY_CAL_MEAL = "meal";
    private static final String KEY_CAL_KCAL = "kcal";

    // Meal indices
    public static final int MEAL_BREAKFAST = 0;
    public static final int MEAL_LUNCH     = 1;
    public static final int MEAL_DINNER    = 2;
    public static final int MEAL_EXTRA     = 3;

    // Calendar Table
    private static final String KEY_CAL_ID      = "id";
    private static final String KEY_CAL_DAY     = "day";
    private static final String KEY_CAL_WORK_ID = "workId";

    // Exercise Table
    private static final String KEY_EXE_ID   = "id";
    private static final String KEY_EXE_NAME = "exerciseName";
    private static final String KEY_EXE_DIFF = "exerciseDifficulty";
    private static final String KEY_EXE_DESC = "exerciseDescription";
    private static final String KEY_EXE_MUSC = "exerciseMuscle";

    // Relation Table
    private static final String KEY_REL_ID = "id";
    private static final String KEY_REL_WORK_ID = "relWorkoutId";
    private static final String KEY_REL_EXE_NAME = "relExerciseName";
    private static final String KEY_REL_TIME = "relExerciseTime";
    private static final String KEY_REL_PAUSE = "relExercisePause";
    private static final String KEY_REL_REPS = "relExerciseReps";

    // Workout table columns
    private static final String KEY_WORK_ID = "id";
    private static final String KEY_WORK_NAME = "workoutName";
    private static final String KEY_WORK_TYPE = "workoutType";
    private static final String KEY_WORK_WOD = "workoutWod";
    private static final String KEY_WORK_DIFF = "workoutDiff";
    private static final String KEY_WORK_TIME = "workoutTime";
    private static final String KEY_WORK_SET = "workoutSet";
    private static final String KEY_WORK_PAUSE = "workoutPause";

    private static DatabaseHelper sInstance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
//    @Override
//    public void onConfigure(SQLiteDatabase db) {
//        super.onConfigure(db);
//        db.setForeignKeyConstraintsEnabled(true);
//    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EXE_TABLE = " CREATE TABLE " + TABLE_EXE + "("
                + KEY_EXE_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_EXE_NAME + " TEXT, "
                + KEY_EXE_DIFF   + " INTEGER, "
                + KEY_EXE_DESC + " TEXT, "
                + KEY_EXE_MUSC + " TEXT)";
        String CREATE_REL_TABLE = " CREATE TABLE " + TABLE_REL + "("
                + KEY_REL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_REL_WORK_ID + " INTEGER REFERENCES " + TABLE_WORK + ", "
                + KEY_REL_EXE_NAME + " TEXT REFERENCES " + TABLE_EXE + ", "
                + KEY_REL_TIME + " INTEGER, "
                + KEY_REL_PAUSE + " INTEGER, "
                + KEY_REL_REPS + " INTEGER)";
        String CREATE_WORK_TABLE = " CREATE TABLE " + TABLE_WORK + "("
                + KEY_WORK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_WORK_NAME + " TEXT, "
                + KEY_WORK_WOD + " TEXT, "
                + KEY_WORK_TYPE + " TEXT, "
                + KEY_WORK_DIFF + " INTEGER, "
                + KEY_WORK_TIME + " INTEGER, "
                + KEY_WORK_SET + " INTEGER, "
                + KEY_WORK_PAUSE + " INTEGER)";
        String CREATE_CAL_TABLE = " CREATE TABLE " + TABLE_CAL + "("
                + KEY_CAL_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_CAL_DAY   + " TEXT, "
                + KEY_CAL_WORK_ID + " INTEGER)";
        db.execSQL(CREATE_EXE_TABLE);
        db.execSQL(CREATE_REL_TABLE);
        db.execSQL(CREATE_WORK_TABLE);
        db.execSQL(CREATE_CAL_TABLE);

        String CREATE_CALORIES_TABLE = " CREATE TABLE " + TABLE_CALORIES + "("
                + KEY_CAL_DATE + " TEXT NOT NULL, "
                + KEY_CAL_MEAL + " INTEGER NOT NULL, "
                + KEY_CAL_KCAL + " INTEGER NOT NULL, "
                + "PRIMARY KEY (" + KEY_CAL_DATE + ", " + KEY_CAL_MEAL + "))";
        db.execSQL(CREATE_CALORIES_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(" CREATE TABLE IF NOT EXISTS " + TABLE_CALORIES + "("
                    + KEY_CAL_DATE + " TEXT NOT NULL, "
                    + KEY_CAL_MEAL + " INTEGER NOT NULL, "
                    + KEY_CAL_KCAL + " INTEGER NOT NULL, "
                    + "PRIMARY KEY (" + KEY_CAL_DATE + ", " + KEY_CAL_MEAL + "))");
        }
        if (oldVersion < 3) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAL);
            db.execSQL(" CREATE TABLE " + TABLE_CAL + "("
                    + KEY_CAL_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_CAL_DAY   + " TEXT, "
                    + KEY_CAL_WORK_ID + " INTEGER)");
        }
    }

    // Insert or update a workout in the database
    // Since SQLite doesn't support "upsert" we need to fall back on an attempt to UPDATE (in case the
    // user already exists) optionally followed by an INSERT (in case the user does not already exist).
    // Unfortunately, there is a bug with the insertOnConflict method
    // (https://code.google.com/p/android/issues/detail?id=13045) so we need to fall back to the more
    // verbose option of querying for the user's primary key if we did an update.

    // this is needed because the value _could_ be smaller than 0 (not really in practice)
    public int getPositiveColumnIndex(Cursor cursor, String string) {
        int n1 = cursor.getColumnIndex(string);
        if (n1 < 0) { n1 = 0; }
        return n1;
    }

    public History loadDate(String date) {
        History dateWod = new History();

        String CAL_SELECT_QUERY = "SELECT * FROM " + TABLE_CAL + " WHERE " + KEY_CAL_DAY + " = ?";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(CAL_SELECT_QUERY, new String[]{date});
        ArrayList<Integer> wod = new ArrayList<Integer>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    dateWod.setDate(cursor.getString(getPositiveColumnIndex(cursor, KEY_CAL_DAY)));
                    wod.add(cursor.getInt(getPositiveColumnIndex(cursor, KEY_CAL_WORK_ID)));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        dateWod.setWod(wod);
        return dateWod;
    }

    public long addWorkoutOnDate(String isoDate, int workoutId) {
        SQLiteDatabase db = getWritableDatabase();
        long calendarId = -1;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_CAL_DAY, isoDate);
            values.put(KEY_CAL_WORK_ID, workoutId);
            calendarId = db.insert(TABLE_CAL, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while writing calendar row");
        } finally {
            db.endTransaction();
        }
        return calendarId;
    }

    public void removeCalendarEntry(int rowId) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_CAL, KEY_CAL_ID + " = ?", new String[]{String.valueOf(rowId)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error removing calendar entry");
        } finally {
            db.endTransaction();
        }
    }

    /** Returns day-of-month -> list of (calendar row id, workout id) for the given month (1-based monthIndex). */
    public java.util.Map<Integer, java.util.List<int[]>> loadMonth(int year, int monthIndex) {
        java.util.Map<Integer, java.util.List<int[]>> out = new java.util.HashMap<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            String prefix = String.format(java.util.Locale.US, "%04d-%02d-", year, monthIndex);
            cursor = db.rawQuery(
                    "SELECT " + KEY_CAL_ID + ", " + KEY_CAL_DAY + ", " + KEY_CAL_WORK_ID
                            + " FROM " + TABLE_CAL
                            + " WHERE " + KEY_CAL_DAY + " LIKE ?",
                    new String[]{prefix + "%"});
            while (cursor.moveToNext()) {
                int rowId = cursor.getInt(0);
                String day = cursor.getString(1);
                int workId = cursor.getInt(2);
                int dayOfMonth;
                try {
                    dayOfMonth = Integer.parseInt(day.substring(8, 10));
                } catch (Exception e) {
                    continue;
                }
                java.util.List<int[]> list = out.get(dayOfMonth);
                if (list == null) {
                    list = new java.util.ArrayList<>();
                    out.put(dayOfMonth, list);
                }
                list.add(new int[]{rowId, workId});
            }
        } catch (Exception e) {
            Log.d(TAG, "Error loading month");
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return out;
    }

    public int monthlyCount(int year, int monthIndex) {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            String prefix = String.format(java.util.Locale.US, "%04d-%02d-", year, monthIndex);
            cursor = db.rawQuery(
                    "SELECT COUNT(*) FROM " + TABLE_CAL
                            + " WHERE " + KEY_CAL_DAY + " LIKE ?",
                    new String[]{prefix + "%"});
            if (cursor.moveToFirst()) count = cursor.getInt(0);
        } catch (Exception e) {
            Log.d(TAG, "Error counting month");
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return count;
    }

    private Workout workoutFromCursor(Cursor cursor) {
        Workout work = new Workout();
        work.setID(cursor.getInt(getPositiveColumnIndex(cursor, KEY_WORK_ID)));
        work.setTitle(cursor.getString(getPositiveColumnIndex(cursor, KEY_WORK_NAME)));
        work.setWod(cursor.getString(getPositiveColumnIndex(cursor, KEY_WORK_WOD)));
        work.setType(cursor.getString(getPositiveColumnIndex(cursor, KEY_WORK_TYPE)));
        work.setTotalTime(cursor.getInt(getPositiveColumnIndex(cursor, KEY_WORK_TIME)));
        work.setDifficulty(cursor.getInt(getPositiveColumnIndex(cursor, KEY_WORK_DIFF)));
        work.setNumberOfSets(cursor.getInt(getPositiveColumnIndex(cursor, KEY_WORK_SET)));
        work.setSetPause(cursor.getInt(getPositiveColumnIndex(cursor, KEY_WORK_PAUSE)));
        return work;
    }

    private ArrayList<Workout> loadWorkouts(String whereClause, String[] whereArgs) {
        ArrayList<Workout> wdList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_WORK;
        if (whereClause != null) {
            query += " WHERE " + whereClause;
        }
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, whereArgs);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Workout work = workoutFromCursor(cursor);
                    ArrayList<Exercise> exeList = loadExercisesForWorkout(db, work.getID());
                    work.setExercises(exeList);
                    wdList.add(work);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return wdList;
    }

    public ArrayList<Workout> loadDatabaseDiff(int difficulty) {
        return loadWorkouts(KEY_WORK_DIFF + " = ?", new String[]{String.valueOf(difficulty)});
    }

    public ArrayList<Workout> loadDatabase() {
        return loadWorkouts(null, null);
    }

    private ArrayList<Exercise> loadExercisesForWorkout(SQLiteDatabase db, int workoutId) {
        ArrayList<Exercise> exeList = new ArrayList<>();
        String REL_SELECT_QUERY = "SELECT * FROM " + TABLE_REL + " WHERE " + KEY_REL_WORK_ID + " = ?";
        Cursor cursor = db.rawQuery(REL_SELECT_QUERY, new String[]{String.valueOf(workoutId)});
        try {
            if (cursor.moveToFirst()) {
                do {
                    Exercise exe = new Exercise();
                    exe.setWorkoutId(cursor.getInt(getPositiveColumnIndex(cursor, KEY_REL_WORK_ID)));
                    exe.setName(cursor.getString(getPositiveColumnIndex(cursor, KEY_REL_EXE_NAME)));
                    exe.setPauseInSeconds(cursor.getInt(getPositiveColumnIndex(cursor, KEY_REL_PAUSE)));
                    exe.setTimeInSeconds(cursor.getInt(getPositiveColumnIndex(cursor, KEY_REL_TIME)));
                    exe.setReps(cursor.getInt(getPositiveColumnIndex(cursor, KEY_REL_REPS)));
                    exeList.add(exe);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while loading exercises for workout");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return exeList;
    }

    public void removeExercises(Workout work) {
        ArrayList<Exercise> exeList = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            long workoutId = addOrUpdateWorkout(work);
            String query = "SELECT * FROM " + TABLE_REL + " WHERE " + KEY_REL_WORK_ID + " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(workoutId)});
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getInt(getPositiveColumnIndex(cursor, KEY_REL_ID));
                    db.delete(TABLE_REL, KEY_REL_ID + " = ?", new String[]{String.valueOf(id)});
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Exercise> loadExercises() {
        ArrayList<Exercise> exeList = new ArrayList<>();
        String REL_SELECT_QUERY = "SELECT * FROM " + TABLE_REL;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(REL_SELECT_QUERY, null);
            if (cursor.moveToFirst()) {
                do {
                    Exercise exe = new Exercise();
                    exe.setWorkoutId(cursor.getInt(getPositiveColumnIndex(cursor, KEY_REL_WORK_ID)));
                    exe.setName(cursor.getString(getPositiveColumnIndex(cursor, KEY_REL_EXE_NAME)));
                    exe.setPauseInSeconds(cursor.getInt(getPositiveColumnIndex(cursor, KEY_REL_PAUSE)));
                    exe.setTimeInSeconds(cursor.getInt(getPositiveColumnIndex(cursor, KEY_REL_TIME)));
                    exe.setReps(cursor.getInt(getPositiveColumnIndex(cursor, KEY_REL_REPS)));
                    exeList.add(exe);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while loading exercise list");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return exeList;
    }

    public int loadExerciseId(String name) {
        int exeId = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            String workoutSelectQuery = "SELECT " + KEY_EXE_ID + " FROM " + TABLE_EXE
                    + " WHERE " + KEY_EXE_NAME + " = ?";
            cursor = db.rawQuery(workoutSelectQuery, new String[]{String.valueOf(name)});
            if (cursor.moveToFirst()) {
                exeId = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to load exercise ID");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return exeId;
    }

    public ExerciseDetail loadOneExercise(String name) {
        ExerciseDetail exe = new ExerciseDetail();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            String workoutSelectQuery = "SELECT * FROM " + TABLE_EXE
                    + " WHERE " + KEY_EXE_NAME + " = ?";
            cursor = db.rawQuery(workoutSelectQuery, new String[]{name});
            if (cursor.moveToFirst()) {
                exe.setName(cursor.getString(getPositiveColumnIndex(cursor, KEY_EXE_NAME)));
                exe.setDifficulty(cursor.getInt(getPositiveColumnIndex(cursor, KEY_EXE_DIFF)));
                exe.setDescription(cursor.getString(getPositiveColumnIndex(cursor, KEY_EXE_DESC)));
                exe.setMuscle(cursor.getString(getPositiveColumnIndex(cursor, KEY_EXE_MUSC)));
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to load exercise");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return exe;
    }

    public ArrayList<ExerciseDetail> loadAllExercises() {
        ArrayList<ExerciseDetail> exeList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            String workoutSelectQuery = "SELECT * FROM " + TABLE_EXE;
            cursor = db.rawQuery(workoutSelectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    ExerciseDetail exe = new ExerciseDetail();
                    exe.setName(cursor.getString(getPositiveColumnIndex(cursor, KEY_EXE_NAME)));
                    exe.setDifficulty(cursor.getInt(getPositiveColumnIndex(cursor, KEY_EXE_DIFF)));
                    exe.setID(cursor.getInt(getPositiveColumnIndex(cursor, KEY_EXE_ID)));
                    exe.setDescription(cursor.getString(getPositiveColumnIndex(cursor, KEY_EXE_DESC)));
                    exe.setMuscle(cursor.getString(getPositiveColumnIndex(cursor, KEY_EXE_MUSC)));
                    exeList.add(exe);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to load all exercises");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return exeList;
    }

    public int loadWorkoutId(String name) {
        int workoutId = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            String workoutSelectQuery = "SELECT " + KEY_WORK_ID + " FROM " + TABLE_WORK
                    + " WHERE " + KEY_WORK_NAME + " = ?";
            cursor = db.rawQuery(workoutSelectQuery, new String[]{String.valueOf(name)});
            if (cursor.moveToFirst()) {
                workoutId = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to load workout ID");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return workoutId;
    }

    public Workout loadWorkoutFromId(int wrkId) {
        Workout work = new Workout();
        String WRK_SELECT_QUERY = "SELECT * FROM " + TABLE_WORK + " WHERE " + KEY_WORK_ID + " = ?";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(WRK_SELECT_QUERY, new String[]{String.valueOf(wrkId)});
        try {
            if (cursor.moveToFirst()) {
                work = workoutFromCursor(cursor);
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return work;
    }

    public void addExerciseInWorkout(Exercise exercise, Workout work) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            long workoutId = addOrUpdateWorkout(work);
            ContentValues values = new ContentValues();
            values.put(KEY_REL_WORK_ID, workoutId);
            values.put(KEY_REL_EXE_NAME, exercise.getName());
            values.put(KEY_REL_TIME, exercise.getTimeInSeconds());
            values.put(KEY_REL_PAUSE, exercise.getPauseInSeconds());
            values.put(KEY_REL_REPS, exercise.getReps());
            db.insertOrThrow(TABLE_REL, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
    }

    public long addOneExercise(ExerciseDetail exe) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long exerciseId = -1;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_EXE_NAME, exe.getName());
            values.put(KEY_EXE_DIFF, exe.getDifficulty());
            values.put(KEY_EXE_DESC, exe.getDescription());
            values.put(KEY_EXE_MUSC, exe.getMuscle());
            // First try to update the workout in case the workout already exists in the database
            // This assumes workoutNames are unique

            exerciseId = db.insert(TABLE_EXE, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return exerciseId;
    }

    public long addOrUpdateExercise(ExerciseDetail exe) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long exerciseId = -1;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_EXE_NAME, exe.getName());
            values.put(KEY_EXE_DIFF, exe.getDifficulty());
            values.put(KEY_EXE_DESC, exe.getDescription());
            values.put(KEY_EXE_MUSC, exe.getMuscle());
            // First try to update the workout in case the workout already exists in the database
            // This assumes workoutNames are unique
            int rows = db.update(TABLE_EXE, values, KEY_EXE_NAME + "= ?", new String[]{exe.getName()});
            // Check if update succeeded
            if (rows == 1) {
                String workoutSelectQuery = "SELECT " + KEY_EXE_ID + " FROM " + TABLE_EXE
                        + " WHERE " + KEY_EXE_NAME + " = ?";
                Cursor cursor = db.rawQuery(workoutSelectQuery, new String[]{String.valueOf(exe.getName())});
                try {
                    if (cursor.moveToFirst()) {
                        exerciseId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                exerciseId = db.insertOrThrow(TABLE_EXE, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return exerciseId;
    }

    public long addOrUpdateWorkout(Workout work) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long workoutId = -1;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_WORK_NAME, work.getTitle());
            values.put(KEY_WORK_WOD, work.getWod());
            values.put(KEY_WORK_TYPE, work.getType());
            values.put(KEY_WORK_DIFF, work.getDifficulty());
            values.put(KEY_WORK_TIME, work.getTotalTime());
            values.put(KEY_WORK_SET, work.getNumberOfSets());
            values.put(KEY_WORK_PAUSE, work.getSetPause());
            // First try to update the workout in case the workout already exists in the database
            // This assumes workoutNames are unique
            int rows = db.update(TABLE_WORK, values, KEY_WORK_NAME + "= ?", new String[]{work.getTitle()});
            // Check if update succeeded
            if (rows == 1) {
                String workoutSelectQuery = "SELECT " + KEY_WORK_ID + " FROM " + TABLE_WORK
                        + " WHERE " + KEY_WORK_NAME + " = ?";
                Cursor cursor = db.rawQuery(workoutSelectQuery, new String[]{String.valueOf(work.getTitle())});
                try {
                    if (cursor.moveToFirst()) {
                        workoutId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                workoutId = db.insertOrThrow(TABLE_WORK, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return workoutId;
    }

    // Delete everything
    public void deleteDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_REL,  null, null);
            db.delete(TABLE_EXE,  null, null);
            db.delete(TABLE_WORK, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteAllExercises() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_REL, null, null);
            db.delete(TABLE_EXE, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteExercisesTables() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_EXE, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteCalendarTables() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_CAL, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }

    public int deleteWorkout(Workout work) {
        int workoutId = -1;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String workoutSelectQuery = "SELECT " + KEY_WORK_ID + " FROM " + TABLE_WORK
                    + " WHERE " + KEY_WORK_NAME + " = ?";
            Cursor cursor = db.rawQuery(workoutSelectQuery, new String[]{String.valueOf(work.getTitle())});
            if (cursor.moveToFirst()) {
                workoutId = cursor.getInt(0);
                db.delete(TABLE_REL, KEY_REL_WORK_ID + "= ?", new String[]{String.valueOf(workoutId)});
            }
            cursor.close();
            db.delete(TABLE_WORK, KEY_WORK_ID + "= ?", new String[]{String.valueOf(workoutId)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error when trying to delete one workout");
        } finally {
            db.endTransaction();
        }
        return workoutId;
    }

    public static final long MILLIS_PER_DAY = 86400000L;

    public int DateToDays (Date date){
        //  convert a date to an integer and back again
        long currentTime=date.getTime();
        currentTime=currentTime/MILLIS_PER_DAY;
        return (int) currentTime;
    }

    public void setMealKcal(String date, int meal, int kcal) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_CAL_DATE, date);
            values.put(KEY_CAL_MEAL, meal);
            values.put(KEY_CAL_KCAL, kcal);
            db.insertWithOnConflict(TABLE_CALORIES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error writing meal kcal");
        } finally {
            db.endTransaction();
        }
    }

    public void clearMealKcal(String date, int meal) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_CALORIES,
                    KEY_CAL_DATE + " = ? AND " + KEY_CAL_MEAL + " = ?",
                    new String[]{date, String.valueOf(meal)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error clearing meal kcal");
        } finally {
            db.endTransaction();
        }
    }

    public int[] getDayKcal(String date) {
        int[] result = new int[4];
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "SELECT " + KEY_CAL_MEAL + ", " + KEY_CAL_KCAL
                            + " FROM " + TABLE_CALORIES
                            + " WHERE " + KEY_CAL_DATE + " = ?",
                    new String[]{date});
            while (cursor.moveToNext()) {
                int meal = cursor.getInt(0);
                int kcal = cursor.getInt(1);
                if (meal >= 0 && meal < 4) {
                    result[meal] = kcal;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Error reading day kcal");
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return result;
    }

    /** Returns 7 daily totals, one per ISO weekday starting at {@code mondayDate}. */
    public int[] getWeekKcal(String mondayDate) {
        int[] totals = new int[7];
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "SELECT " + KEY_CAL_DATE + ", SUM(" + KEY_CAL_KCAL + ") "
                            + "FROM " + TABLE_CALORIES + " "
                            + "WHERE " + KEY_CAL_DATE + " >= ? AND " + KEY_CAL_DATE + " <= date(?, '+6 days') "
                            + "GROUP BY " + KEY_CAL_DATE,
                    new String[]{mondayDate, mondayDate});
            while (cursor.moveToNext()) {
                String d = cursor.getString(0);
                int total = cursor.getInt(1);
                int dayIndex = daysSinceMonday(mondayDate, d);
                if (dayIndex >= 0 && dayIndex < 7) {
                    totals[dayIndex] = total;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Error reading week kcal");
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return totals;
    }

    private static int daysSinceMonday(String mondayIso, String otherIso) {
        try {
            java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US);
            long a = fmt.parse(mondayIso).getTime();
            long b = fmt.parse(otherIso).getTime();
            return (int) ((b - a) / MILLIS_PER_DAY);
        } catch (Exception e) {
            return -1;
        }
    }

}
