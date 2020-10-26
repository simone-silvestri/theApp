package com.example.firsttry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class AddWorkoutActivity extends AppCompatActivity {

    private ArrayList<EditText> exename,exework,exepause;
    private ArrayList<TextView> worksec,pausesec;
    private EditText textname, textset, textpause;
    private Workout work;
    private int currentDiff;
    private String currentType;
    private TextView btnexercise;
    private Spinner dropdownType, dropdownDifficulty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        exename = new ArrayList<>();
        exework = new ArrayList<>();
        exepause = new ArrayList<>();
        worksec = new ArrayList<>();
        pausesec = new ArrayList<>();

        textname = findViewById(R.id.text_name);
        textset = findViewById(R.id.text_sets);
        textpause = findViewById(R.id.text_pause);
        btnexercise = findViewById(R.id.btexercise);

        currentDiff = 1;
        currentType = "TIME";

        dropdownType = findViewById(R.id.spinnertype);
        String[] items = new String[]{"TIME", "REPS", "REPTIME"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdownType.setAdapter(adapter);
        dropdownType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {
                    case 0:
                        currentType = "TIME";
                        if (!worksec.isEmpty()) {
                            for (int i = 0; i < worksec.size(); i++) {
                                worksec.get(i).setText("Work:");
                            }
                        }
                        if (!exework.isEmpty()) {
                            for (int i = 0; i < exework.size(); i++) {
                                exework.get(i).setHint("sec");
                            }
                        }
                        if (!pausesec.isEmpty()) {
                            for (int i = 0; i < pausesec.size(); i++) {
                                pausesec.get(i).setText("Pause:");
                            }
                        }
                        if (!exepause.isEmpty()) {
                            for (int i = 0; i < exepause.size(); i++) {
                                exepause.get(i).setHint("sec");
                            }
                        }
                        break;
                    case 1:
                        currentType = "REPS";
                        if (!worksec.isEmpty()) {
                            for (int i = 0; i < worksec.size(); i++) {
                                worksec.get(i).setText("");
                            }
                        }
                        if (!exework.isEmpty()) {
                            for (int i = 0; i < exework.size(); i++) {
                                exework.get(i).setText(null);
                                exework.get(i).setHint("");
                            }
                        }
                        if (!pausesec.isEmpty()) {
                            for (int i = 0; i < pausesec.size(); i++) {
                                pausesec.get(i).setText("Reps:");
                            }
                        }
                        if (!exepause.isEmpty()) {
                            for (int i = 0; i < exepause.size(); i++) {
                                exepause.get(i).setHint("num");
                            }
                        }
                        break;
                    case 2:
                        currentType = "REPTIME";
                        if (!worksec.isEmpty()) {
                            for (int i = 0; i < worksec.size(); i++) {
                                worksec.get(i).setText("Work");
                            }
                        }
                        if (!exework.isEmpty()) {
                            for (int i = 0; i < exework.size(); i++) {
                                exework.get(i).setHint("sec");
                            }
                        }
                        if (!pausesec.isEmpty()) {
                            for (int i = 0; i < pausesec.size(); i++) {
                                pausesec.get(i).setText("Reps:");
                            }
                        }
                        if (!exepause.isEmpty()) {
                            for (int i = 0; i < exepause.size(); i++) {
                                exepause.get(i).setHint("num");
                            }
                        }
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        dropdownDifficulty = findViewById(R.id.spinnerdifficulty);
        items = new String[]{"Beginner", "Intermediate", "Skilled","Expert","Spartan"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdownDifficulty.setAdapter(adapter);
        dropdownDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                currentDiff = position+1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }

    public void addExerciseToList(View v) {
        LinearLayout linear =  findViewById(R.id.exercise_list_layout);

        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.add_exercise_list, null, false);

        exename.add((EditText) layout.findViewById(R.id.txtname));
        exepause.add((EditText) layout.findViewById(R.id.btnaddpause));
        exework.add((EditText) layout.findViewById(R.id.btnaddwork));
        worksec.add((TextView) layout.findViewById(R.id.txttime));
        pausesec.add((TextView) layout.findViewById(R.id.txtpause));
        if (new String("REPS").equals(currentType)) {
            exepause.get(exepause.size()-1).setHint("num");
            pausesec.get(pausesec.size()-1).setText("Reps:");
            exework.get(exework.size()-1).setHint("");
            worksec.get(worksec.size()-1).setText("");
        } else if (new String("REPTIME").equals(currentType)) {
            exepause.get(exepause.size()-1).setHint("num");
            pausesec.get(pausesec.size()-1).setText("Reps:");
        }
        linear.addView(layout);
    }

    public void createWorkout(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);
        Workout workoutToBeAdded = new Workout();
        ArrayList<Exercise> exercises = new ArrayList<>();

        workoutToBeAdded.setTitle(textname.getText().toString());
        if(workoutToBeAdded.getTitle()==null) {
            textname.setText("Insert Title!");
        } else {
            int check = dbhandler.loadWorkoutId(workoutToBeAdded.getTitle());
            if (check != -1) {
                textname.setText("Title already existent, choose other");
            } else {
                if (exename.isEmpty()) {
                        //Have to say something about it
                } else {
                    workoutToBeAdded.setType(currentType);
                    workoutToBeAdded.setDifficulty(currentDiff);
                    workoutToBeAdded.setSetPause(Integer.parseInt(textpause.getText().toString()));
                    workoutToBeAdded.setNumberOfSets(Integer.parseInt(textset.getText().toString()));
                    workoutToBeAdded.setWod("");
                    // Finally add and update the workout, by adding a workout and by adding the exercises
                    for(int i=0; i<exework.size(); i++) {
                        String nm = exename.get(i).getText().toString();
                        int time,pause,reps;
                        if (new String("TIME").equals(currentType)) {
                            time = Integer.parseInt(exework.get(i).getText().toString());
                            pause = Integer.parseInt(exepause.get(i).getText().toString());
                            reps = 0;
                        } else if (new String("REPS").equals(currentType)) {
                            time = 0;
                            reps = Integer.parseInt(exepause.get(i).getText().toString());
                            pause = 0;
                        } else {
                            time = Integer.parseInt(exework.get(i).getText().toString());
                            reps = Integer.parseInt(exepause.get(i).getText().toString());
                            pause = 0;
                        }
                        Exercise exe = new Exercise(nm,reps,time,pause);
                        exe.setWorkoutName(workoutToBeAdded.getTitle());
                        exercises.add(exe);
                    }
                    workoutToBeAdded.setExercises(exercises);
                    workoutToBeAdded.setTotalTime();
                    int id = (int) dbhandler.addOrUpdateWorkout(workoutToBeAdded);
                    workoutToBeAdded.setID(id);
                    for (int i = 0; i < exercises.size(); i++) {
                        dbhandler.addExerciseInWorkout(exercises.get(i), workoutToBeAdded);
                    }
                    btnexercise.setText("Added!");
                }
            }
        }
    }
}