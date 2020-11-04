package com.example.firsttry;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class ModifyWorkoutActivity extends AppCompatActivity {

    private ArrayList<EditText> exename, exework, exepause;
    private ArrayList<TextView> worksec, pausesec;
    private ArrayList<RelativeLayout> layoutlist;
    private ArrayList<ImageButton> btndelete;
    private EditText textset, textpause;
    private TextView textname;
    private Workout work;
    private int currentDiff;
    private String currentType;
    private TextView btnexercise, pauseOrTotalTime;
    private Spinner dropdownType, dropdownDifficulty;
    private LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_workout);

        exename = new ArrayList<>();
        exework = new ArrayList<>();
        exepause = new ArrayList<>();
        worksec = new ArrayList<>();
        pausesec = new ArrayList<>();
        layoutlist = new ArrayList<>();
        btndelete = new ArrayList<>();
        linear = findViewById(R.id.exercise_list_layout);

        textname = findViewById(R.id.text_name);
        textset = findViewById(R.id.text_sets);
        textpause = findViewById(R.id.text_pause);
        btnexercise = findViewById(R.id.btexercise);
        pauseOrTotalTime = findViewById(R.id.pauseOrTotalTime);

        currentDiff = 1;
        currentType = "TIME";

        work = new Workout();
        work = (Workout) getIntent().getSerializableExtra("EXTRA_WORKOUT");

        textname.setText(work.getTitle());
        textset.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        textpause.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
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
                        pauseOrTotalTime.setText("Pause:");
                        textpause.setHint("seconds");
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
                        pauseOrTotalTime.setText("Time:");
                        textpause.setHint("minutes");
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
                        pauseOrTotalTime.setText("Pause:");
                        textpause.setHint("seconds");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        dropdownDifficulty = findViewById(R.id.spinnerdifficulty);
        items = new String[]{"Beginner", "Average", "Skilled", "Expert", "Spartan"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdownDifficulty.setAdapter(adapter);
        dropdownDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                currentDiff = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        //choose here all the parameters
        if (new String("TIME").equals(work.getType())) {
            dropdownType.setSelection(0);
            textpause.setText(Integer.toString(work.getSetPause()));
        } else if (new String("REPS").equals(work.getType())) {
            dropdownType.setSelection(1);
            textpause.setText(Integer.toString(work.getTotalTime()/60));
        } else {
            dropdownType.setSelection(2);
            textpause.setText(Integer.toString(work.getSetPause()));
        }
        dropdownDifficulty.setSelection(work.getDifficulty() - 1);
        textset.setText(Integer.toString(work.getNumberOfSets()));

        for (int i = 0; i < work.getExercises().size(); i++) {
            addExerciseToList((View) textname);
            exename.get(i).setText(work.getExercises().get(i).getName());
            if (new String("TIME").equals(work.getType())) {
                exework.get(i).setText(Integer.toString(work.getExercises().get(i).getTimeInSeconds()));
                exepause.get(i).setText(Integer.toString(work.getExercises().get(i).getPauseInSeconds()));
            } else if (new String("REPS").equals(work.getType())) {
                exepause.get(i).setText(Integer.toString(work.getExercises().get(i).getReps()));
            } else {
                exework.get(i).setText(Integer.toString(work.getExercises().get(i).getTimeInSeconds()));
                exepause.get(i).setText(Integer.toString(work.getExercises().get(i).getReps()));
            }
        }

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void deleteExerciseFromList(View v) {
        RelativeLayout r = (RelativeLayout) v.getParent();
        int idx = ((ViewGroup) r.getParent()).indexOfChild(r);

        ArrayList<String> nm = new ArrayList<>();
        ArrayList<String> wk = new ArrayList<>();
        ArrayList<String> ps = new ArrayList<>();
        for (int i = 0; i < exename.size(); i++) {
            nm.add(exename.get(i).getText().toString());
            wk.add(exework.get(i).getText().toString());
            ps.add(exepause.get(i).getText().toString());
        }

        if (idx != -1) {
            exename.remove(idx);
            exepause.remove(idx);
            exework.remove(idx);
            worksec.remove(idx);
            pausesec.remove(idx);
            btndelete.remove(idx);
            layoutlist.remove(idx);
            nm.remove(idx);
            wk.remove(idx);
            ps.remove(idx);
        }

        linear.removeView((View) v.getParent());

        for (int i = 0; i < exename.size(); i++) {
            exename.get(i).setText(nm.get(i));
            exework.get(i).setText(wk.get(i));
            exepause.get(i).setText(ps.get(i));
        }


    }


    public void addExerciseToList(View v) {

        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.add_exercise_list, null, false);

        layoutlist.add(layout);
        btndelete.add((ImageButton) layout.findViewById(R.id.btndeleteexercise));
        exename.add((EditText) layout.findViewById(R.id.txtname));
        exename.get(exename.size() - 1).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        exepause.add((EditText) layout.findViewById(R.id.btnaddpause));
        exepause.get(exepause.size() - 1).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        exework.add((EditText) layout.findViewById(R.id.btnaddwork));
        exework.get(exework.size() - 1).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        worksec.add((TextView) layout.findViewById(R.id.txttime));
        pausesec.add((TextView) layout.findViewById(R.id.txtpause));
        if (new String("REPS").equals(currentType)) {
            exepause.get(exepause.size() - 1).setHint("num");
            pausesec.get(pausesec.size() - 1).setText("Reps:");
            exework.get(exework.size() - 1).setHint("");
            worksec.get(worksec.size() - 1).setText("");
        } else if (new String("REPTIME").equals(currentType)) {
            exepause.get(exepause.size() - 1).setHint("num");
            pausesec.get(pausesec.size() - 1).setText("Reps:");
        }

        btnexercise.setText("Update Library");
        linear.addView(layout);
    }

    public void createWorkout(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);
        Workout workoutToBeAdded = new Workout();
        ArrayList<Exercise> exercises = new ArrayList<>();

        workoutToBeAdded.setTitle(textname.getText().toString());
        if (workoutToBeAdded.getTitle().isEmpty()) {
            textname.setHint("Insert Title!");
        } else {
            if (exename.isEmpty()) {
                btnexercise.setText("Add exercises first");
            } else {
                if (textset.getText().toString().isEmpty()) {
                    textset.setHint("Fill sets");
                    textset.setText(null);
                } else {
                    if (textpause.getText().toString().isEmpty()) {
                        textpause.setHint("Fill field");
                        textpause.setText(null);
                    } else {
                        workoutToBeAdded.setType(currentType);
                        workoutToBeAdded.setDifficulty(currentDiff);
                        workoutToBeAdded.setWod("");
                        // Finally add and update the workout, by adding a workout and by adding the exercises
                        for (int i = 0; i < exework.size(); i++) {
                            String nm = exename.get(i).getText().toString();
                            int time, pause, reps;
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
                            Exercise exe = new Exercise(nm, reps, time, pause);
                            exe.setWorkoutName(workoutToBeAdded.getTitle());
                            exercises.add(exe);
                        }
                        workoutToBeAdded.setExercises(exercises);
                        if (new String("REPS").equals(workoutToBeAdded.getType())) {
                            workoutToBeAdded.setSetPause(0);
                            workoutToBeAdded.setNumberOfSets(Integer.parseInt(textset.getText().toString()));
                            workoutToBeAdded.setTotalTime(Integer.parseInt(textpause.getText().toString()) * 60);
                        } else {
                            workoutToBeAdded.setSetPause(Integer.parseInt(textpause.getText().toString()));
                            workoutToBeAdded.setNumberOfSets(Integer.parseInt(textset.getText().toString()));
                            workoutToBeAdded.setTotalTime();
                        }
                        int id = (int) dbhandler.addOrUpdateWorkout(workoutToBeAdded);
                        workoutToBeAdded.setID(id);
                        dbhandler.removeExercises(workoutToBeAdded);
                        for (int i = 0; i < exercises.size(); i++) {
                            dbhandler.addExerciseInWorkout(exercises.get(i), workoutToBeAdded);
                        }
                        btnexercise.setText("Updated!");
                    }
                }
            }
        }
    }
}