package com.example.firsttry;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class AddWorkout2 extends AppCompatActivity {

    private ArrayList<EditText> exename, exework, exepause;
    private ArrayList<TextView> worksec, pausesec;
    private ArrayList<RelativeLayout> layoutlist;
    private ArrayList<ImageButton> btndelete;
    private ImageView time, reps, reptime, beginner, average, skilled, expert, spartan;
    private EditText textname, textset, textpause;
    private Workout work;
    private int currentDiff;
    private String currentType;
    private TextView btnexercise, pauseOrTotalTime;
    private Spinner dropdownType, dropdownDifficulty;
    private LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout2);

        exename = new ArrayList<>();
        exework = new ArrayList<>();
        exepause = new ArrayList<>();
        worksec = new ArrayList<>();
        pausesec = new ArrayList<>();
        layoutlist = new ArrayList<>();
        btndelete = new ArrayList<>();
        linear = findViewById(R.id.exercise_list_layout);

        textname = findViewById(R.id.titlename);
        textset = findViewById(R.id.editsets);
        textpause = findViewById(R.id.editpause);
        btnexercise = findViewById(R.id.btexercise);
        pauseOrTotalTime = findViewById(R.id.pauseOrTotalTime);

        currentDiff = 1;
        currentType = "TIME";

        textname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
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

        time = findViewById(R.id.imgtime);
        reps = findViewById(R.id.imgreps);
        reptime = findViewById(R.id.imgreptime);
        beginner = findViewById(R.id.imgbeginner);
        average = findViewById(R.id.imgaverage);
        skilled = findViewById(R.id.imgskilled);
        expert = findViewById(R.id.imgexpert);
        spartan = findViewById(R.id.imgspartan);
        final ImageButton buttoninfo = findViewById(R.id.buttoninfo);
        buttoninfo.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View puView = layoutInflater.inflate(R.layout.info_page, null);
                puView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.popup_show));

                TextView text = (TextView) puView.findViewById(R.id.text_id);
                TextView exer = (TextView) puView.findViewById(R.id.text_exe);
                ImageView img = (ImageView) puView.findViewById(R.id.imgexe);
                if(new String("TIME").equals(currentType)) {
                    text.setText(R.string.type_1_desc);
                    exer.setText("H.I.I.T. type workout");
                    img.setImageResource(R.drawable.time);
                } else if (new String("REPS").equals(currentType)) {
                    text.setText(R.string.type_2_desc);
                    exer.setText("Repetition based workout");
                    img.setImageResource(R.drawable.reps);
                } else {
                    text.setText(R.string.type_3_desc);
                    exer.setText("Repetitions in time");
                    img.setImageResource(R.drawable.reptime);
                }
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                final PopupWindow puWindow = new PopupWindow(puView,height,width,true);
                puWindow.showAtLocation(arg0, Gravity.CENTER, 0, 0);
                puWindow.setAnimationStyle(R.style.Animation);
                puWindow.showAsDropDown(buttoninfo, 0, 0);
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void setCurrentDiff(View v) {
        LinearLayout r = (LinearLayout) v.getParent();
        int idx = r.indexOfChild(v);
        currentDiff = idx + 1;
        LinearLayout.LayoutParams small = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.small_image), getResources().getDimensionPixelSize(R.dimen.small_image),1.0f);
        LinearLayout.LayoutParams large = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.large_image), getResources().getDimensionPixelSize(R.dimen.large_image),1.0f);
        small.gravity= Gravity.CENTER;
        large.gravity= Gravity.CENTER;
        beginner.setLayoutParams(small);
        average.setLayoutParams(small);
        expert.setLayoutParams(small);
        skilled.setLayoutParams(small);
        spartan.setLayoutParams(small);
        v.setLayoutParams(large);
    }

    public void setCurrentType(View v) {
        LinearLayout r = (LinearLayout) v.getParent();
        int idx = r.indexOfChild(v);

        if(idx == 0) {
            currentType = "TIME";
            if (!worksec.isEmpty()) {
                for (int i = 0; i < worksec.size(); i++) {
                    worksec.get(i).setText("\"");
                }
            }
            if (!exework.isEmpty()) {
                for (int i = 0; i < exework.size(); i++) {
                    exework.get(i).setHint("sec");
                }
            }
            if (!pausesec.isEmpty()) {
                for (int i = 0; i < pausesec.size(); i++) {
                    pausesec.get(i).setText("\"");
                }
            }
            if (!exepause.isEmpty()) {
                for (int i = 0; i < exepause.size(); i++) {
                    exepause.get(i).setHint("sec");
                }
            }
            pauseOrTotalTime.setText("Pause:");
            textpause.setHint("seconds");
        } else if (idx == 1) {
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
                    pausesec.get(i).setText("X");
                }
            }
            if (!exepause.isEmpty()) {
                for (int i = 0; i < exepause.size(); i++) {
                    exepause.get(i).setHint("num");
                }
            }
            pauseOrTotalTime.setText("Time:");
            textpause.setHint("minutes");
        } else {
            currentType = "REPTIME";
            if (!worksec.isEmpty()) {
                for (int i = 0; i < worksec.size(); i++) {
                    worksec.get(i).setText("\"");
                }
            }
            if (!exework.isEmpty()) {
                for (int i = 0; i < exework.size(); i++) {
                    exework.get(i).setHint("sec");
                }
            }
            if (!pausesec.isEmpty()) {
                for (int i = 0; i < pausesec.size(); i++) {
                    pausesec.get(i).setText("X");
                }
            }
            if (!exepause.isEmpty()) {
                for (int i = 0; i < exepause.size(); i++) {
                    exepause.get(i).setHint("num");
                }
            }
            pauseOrTotalTime.setText("Pause:");
            textpause.setHint("seconds");
        }
        LinearLayout.LayoutParams small = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.small_image), getResources().getDimensionPixelSize(R.dimen.small_image),1.0f);
        LinearLayout.LayoutParams large = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.large_image), getResources().getDimensionPixelSize(R.dimen.large_image),1.0f);
        small.gravity= Gravity.CENTER;
        large.gravity= Gravity.CENTER;
        time.setLayoutParams(small);
        reps.setLayoutParams(small);
        reptime.setLayoutParams(small);
        v.setLayoutParams(large);
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
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.add_exercise_list_2, null, false);

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
            pausesec.get(pausesec.size() - 1).setText("X");
            exework.get(exework.size() - 1).setHint("");
            worksec.get(worksec.size() - 1).setText("");
        } else if (new String("REPTIME").equals(currentType)) {
            exepause.get(exepause.size() - 1).setHint("num");
            pausesec.get(pausesec.size() - 1).setText("X");
            worksec.get(pausesec.size() - 1).setText("\"");
        } else {
            worksec.get(pausesec.size() - 1).setText("\"");
            pausesec.get(pausesec.size() - 1).setText("\"");
        }

        btnexercise.setText("Add to Library");
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
            int check = dbhandler.loadWorkoutId(workoutToBeAdded.getTitle());
            if (check != -1) {
                textname.setHint("Title already existent, choose other");
                textname.setText(null);
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
                            for (int i = 0; i < exercises.size(); i++) {
                                dbhandler.addExerciseInWorkout(exercises.get(i), workoutToBeAdded);
                            }
                            btnexercise.setText("Added!");
                        }
                    }
                }
            }
        }
    }

}