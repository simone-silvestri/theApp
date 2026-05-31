package com.simone.cfts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class StringActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_string);
    }

    public void closeString(View view) { finish(); }

    public void openWorkout(View view) {
        EditText workoutString = findViewById(R.id.workout_string_name);
        String content = workoutString.getText().toString().trim();

        if (!content.isEmpty()) {
            StringFormatter stringFormatter = new StringFormatter();
            stringFormatter.setWorkout(content);

            if (stringFormatter.getWorkout() != null) {
                Intent intent = new Intent(StringActivity.this, DetailActivity.class);
                Workout work = stringFormatter.getWorkout();
                intent.putExtra("EXTRA_TITLE", work.getTitle());
                intent.putExtra("EXTRA_WOD", work.getWod());
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
