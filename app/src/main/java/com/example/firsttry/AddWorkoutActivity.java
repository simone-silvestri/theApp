package com.example.firsttry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class AddWorkoutActivity extends AppCompatActivity {

    private EditText texttype, textname, textset, textsetpause, textwod, textdiff;
    private Workout work;
    private ListView lv;
    private ArrayList<Exercise> exercises;
    private int currentDiff;
    private String currentType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        texttype = findViewById(R.id.texttype);
        textname = findViewById(R.id.textname);
        textdiff = findViewById(R.id.textdiff);
        textset  = findViewById(R.id.textset);
        textsetpause = findViewById(R.id.textsetpause);
        textwod = findViewById(R.id.textwod);
        lv = findViewById(R.id.lexercise);

        currentDiff = 1;
        currentType = "TIME";

    }


    public void addExerciseToList() {}

    public void createWorkout() {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);
        Workout workoutToBeAdded = new Workout();

        workoutToBeAdded.setTitle(textname.getText().toString());
        int check = dbhandler.loadWorkoutId(workoutToBeAdded.getTitle());
        if(check<0) {
            textname.setText("Title already existent, choose other");
        } else {
            if(exercises.isEmpty()) {
                //Have to say something about it
            } else {
                workoutToBeAdded.setType(currentType);
                workoutToBeAdded.setDifficulty(currentDiff);
                workoutToBeAdded.setSetPause(Integer.parseInt(textsetpause.getText().toString()));
                workoutToBeAdded.setNumberOfSets(Integer.parseInt(textset.getText().toString()));
                workoutToBeAdded.setWod(textwod.getText().toString());
                // Finally add and update the workout, by adding a workout and by adding the exercises
                int id = (int) dbhandler.addOrUpdateWorkout(workoutToBeAdded);
                workoutToBeAdded.setID(id);
                workoutToBeAdded.setExercises(exercises);
                for (int i=0; i<exercises.size(); i++) {
                    dbhandler.addExerciseInWorkout(exercises.get(i),workoutToBeAdded);
                }
            }
        }
    }
}