package com.simone.cfts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class StringActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_string);
    }

    public void openWorkout(View view) {
        EditText workoutString = findViewById(R.id.workout_string_name);
        String content = workoutString.getText().toString();

        if(!content.isEmpty()) {
            StringFormatter stringFormatter = new StringFormatter();
            stringFormatter.setWorkout(content);

            if(stringFormatter.getWorkout()!=null) {
                Intent intent = new Intent(StringActivity.this, DetailActivity.class);
                String title = stringFormatter.getWorkout().getTitle();
                String wod = stringFormatter.getWorkout().getWod();

                Workout work = stringFormatter.getWorkout();

                intent.putExtra("EXTRA_TITLE", title);
                intent.putExtra("EXTRA_WOD", wod);
                intent.putExtra("EXTRA_WORK_OR_ADD", 1);
                intent.putExtra("EXTRA_WORKOUT", work);
                startActivity(intent);
            } else {
                workoutString.setText(null);
                workoutString.setHint("Enter a valid string format");
            }
        }
    }
}