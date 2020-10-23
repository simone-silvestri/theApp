package com.example.firsttry;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DataHelper {

    public static ArrayList<Workout> loadWorkout(Context context) {
        ArrayList<Workout> worklist = new ArrayList<>();
        String json = "";
        try {
            InputStream is = context.getAssets().open("girlsBench.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }   catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray jsonArray = obj.getJSONArray("girlsBenchmark");

            for (int i = 0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Workout work = new Workout();
                work.setTitle(jsonObject.getString("title"));
                work.setWod(jsonObject.getString("wod"));
                worklist.add(work);
             }
        }   catch (JSONException e) {
        e.printStackTrace();
        }

        return worklist;

    }

}
