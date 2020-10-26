package com.example.firsttry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class ExerciseActivity extends AppCompatActivity {

    private TextView title, difficulty, muscle, description;
    private ExerciseDetail exe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        title = findViewById(R.id.title_tv);
        difficulty = findViewById(R.id.text_diff);
        muscle = findViewById(R.id.text_muscle);
        description = findViewById(R.id.Description);


        Bundle extra = getIntent().getExtras();
        String name = "";


        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);

        if (extra != null) {
            name = extra.getString("EXTRA_NAME");

            exe = dbhandler.loadOneExercise(name);
            if (exe.getName()==null) {
                exe = new ExerciseDetail(name, -1);
                exe.setDescription("Sorry no exercise with that name, if you want you can go back to the homepage and add it to the exercise library");
                exe.setMuscle("Not found");
            }
        }

        title.setText(exe.getName());

        muscle.setText(exe.getMuscle());
        if (exe.getDifficulty() == 1) {
            difficulty.setText("Easy");
        } else if (exe.getDifficulty() == 2) {
            difficulty.setText("Intermediate");
        } else if (exe.getDifficulty() == -1) {
            difficulty.setText("Not found");
        } else {
            difficulty.setText("Advanced");
        }
        description.setText(exe.getDescription());

    }
}