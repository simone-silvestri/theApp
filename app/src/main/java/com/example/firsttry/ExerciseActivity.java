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
        int id = -1000;


        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);

//        if (extra != null) {
            name = extra.getString("EXTRA_NAME");
            ExerciseDetail exe1 = new ExerciseDetail(name,3);
            exe1.setMuscle("Chest");
            exe1.setDescription("Let's see");
            dbhandler.addOneExercise(exe1);
//
//            id = (int) dbhandler.addOrUpdateExercise(exe1);
//            if (id != -1) {
//                exe = dbhandler.loadOneExercise(id);
//            } else {
//                exe = new ExerciseDetail(name, -1);
//                exe.setDescription("Sorry no exercise with that name, if you want you can go back to the homepage and add it to the exercise library");
//                exe.setMuscle("Not found");
//            }
//        }

        ArrayList<ExerciseDetail> exeList = dbhandler.loadAllExercises();
        if(exeList.isEmpty()) {
            exe = new ExerciseDetail(name, -1);
            exe.setDescription("Sorry no exercise with that name, if you want you can go back to the homepage and add it to the exercise library");
            exe.setMuscle("Not found");
        } else {
            exe = exeList.get(0);
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