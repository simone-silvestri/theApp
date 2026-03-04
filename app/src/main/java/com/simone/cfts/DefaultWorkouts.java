package com.simone.cfts;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DefaultWorkouts {
    private static final String TAG = "DefaultWorkouts";

    private ArrayList<Workout> wodList;
    private ArrayList<ExerciseDetail> exeDetails;

    public ArrayList<ExerciseDetail> getExerciseDetails() {
        return this.exeDetails;
    }

    public ArrayList<Workout> getWodList() {
        return this.wodList;
    }

    public DefaultWorkouts(Context context) {
        this.exeDetails = new ArrayList<>();
        this.wodList = new ArrayList<>();

        try {
            String json = loadJsonFromAsset(context, "default_workouts.json");
            JSONObject root = new JSONObject(json);
            parseWorkouts(root.getJSONArray("workouts"));
            parseExerciseDetails(context, root.getJSONArray("exerciseDetails"));
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing default workouts JSON", e);
        } catch (IOException e) {
            Log.e(TAG, "Error reading default workouts asset", e);
        }
    }

    private String loadJsonFromAsset(Context context, String filename) throws IOException {
        InputStream is = context.getAssets().open(filename);
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        is.close();
        return new String(buffer, StandardCharsets.UTF_8);
    }

    private void parseWorkouts(JSONArray workoutsArray) throws JSONException {
        for (int i = 0; i < workoutsArray.length(); i++) {
            JSONObject wo = workoutsArray.getJSONObject(i);

            ArrayList<Exercise> exercises = new ArrayList<>();
            JSONArray exeArray = wo.getJSONArray("exercises");
            for (int j = 0; j < exeArray.length(); j++) {
                JSONObject exeObj = exeArray.getJSONObject(j);
                exercises.add(new Exercise(
                        exeObj.getString("name"),
                        exeObj.getInt("reps"),
                        exeObj.getInt("workTime"),
                        exeObj.getInt("restTime")
                ));
            }

            Workout wd = new Workout();
            wd.setTitle(wo.getString("title"));
            wd.setType(wo.getString("type"));
            wd.setDifficulty(wo.getInt("difficulty"));
            wd.setNumberOfSets(wo.getInt("sets"));
            wd.setSetPause(wo.getInt("setPause"));
            wd.setExercises(exercises);
            wd.setWod(wo.getString("description"));

            if (wo.has("totalTime") && !wo.isNull("totalTime")) {
                wd.setTotalTime(wo.getInt("totalTime"));
            } else {
                wd.setTotalTime();
            }

            this.wodList.add(wd);
        }
    }

    private void parseExerciseDetails(Context context, JSONArray detailsArray) throws JSONException {
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        for (int i = 0; i < detailsArray.length(); i++) {
            JSONObject obj = detailsArray.getJSONObject(i);
            ExerciseDetail exe = new ExerciseDetail();
            exe.setName(obj.getString("name"));
            exe.setDifficulty(obj.getInt("difficulty"));
            exe.setMuscle(obj.getString("muscle"));
            exe.setDescription(obj.getString("description"));
            db.addOrUpdateExercise(exe);
            this.exeDetails.add(exe);
        }
    }
}
