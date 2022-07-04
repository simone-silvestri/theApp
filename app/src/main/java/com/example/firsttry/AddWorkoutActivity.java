package com.example.firsttry;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
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

public class AddWorkoutActivity extends AppCompatActivity {

    private ArrayList<String> arrayList;
    private Dialog dialog;

    private ArrayList<EditText> exework, exepause;
    private ArrayList<TextView> exename, worksec, pausesec, additional;
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
    private TextView generalworksec, generalpausesec, generaladditional;
    private EditText generalexework, generalexepause;

    private ImageButton buttonBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout2);

        buttonBack = findViewById(R.id.btnBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        exename = new ArrayList<>();
        exework = new ArrayList<>();
        exepause = new ArrayList<>();
        worksec = new ArrayList<>();
        pausesec = new ArrayList<>();
        additional = new ArrayList<>();
        layoutlist = new ArrayList<>();
        btndelete = new ArrayList<>();
        linear = findViewById(R.id.exercise_list_layout);

        textname = findViewById(R.id.titlename);
        textset = findViewById(R.id.editsets);
        textpause = findViewById(R.id.editpause);
        btnexercise = findViewById(R.id.btexercise);
        pauseOrTotalTime = findViewById(R.id.pauseOrTotalTime);

        generalpausesec   = findViewById(R.id.txt_general_pause);
        generalworksec    = findViewById(R.id.txt_general_work);
        generalexepause   = findViewById(R.id.btn_add_general_pause);
        generalexework    = findViewById(R.id.btn_add_general_work);
        generaladditional = findViewById(R.id.txt_general_additional);

        generalexepause.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (!exepause.isEmpty()) {
                    for (int i = 0; i < exepause.size(); i++) {
                        exepause.get(i).setText(s);
                    }
                }
            }
        });

        generalexework.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (!exework.isEmpty()) {
                    for (int i = 0; i < exework.size(); i++) {
                        exework.get(i).setText(s);
                    }
                }
            }
        });

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

        setCurrentDiff(beginner);
        setCurrentType(time);

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
                    exer.setText("H.I.I.T. workout");
                    img.setImageResource(R.drawable.time);
                } else if (new String("REPS").equals(currentType)) {
                    text.setText(R.string.type_2_desc);
                    exer.setText("Reps based");
                    img.setImageResource(R.drawable.reps);
                } else {
                    text.setText(R.string.type_3_desc);
                    exer.setText("Reps in time");
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
        TextView diffname = (TextView) findViewById(R.id.diffname);
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

        if(currentDiff==1) {
            diffname.setText("Beginner");
            diffname.setTextColor(getResources().getColor(R.color.beginner));
        } else if (currentDiff==2) {
            diffname.setText("Average");
            diffname.setTextColor(getResources().getColor(R.color.average));
        } else if (currentDiff==3) {
            diffname.setText("Skilled");
            diffname.setTextColor(getResources().getColor(R.color.skilled));
        } else if (currentDiff==4) {
            diffname.setText("Expert");
            diffname.setTextColor(getResources().getColor(R.color.expert));
        } else {
            diffname.setText("Spartan");
            diffname.setTextColor(getResources().getColor(R.color.spartan));
        }

    }

    public void setCurrentType(View v) {
        LinearLayout r = (LinearLayout) v.getParent();
        int idx = r.indexOfChild(v);
        TextView type = (TextView) findViewById(R.id.typename);
        if(idx == 0) {
            currentType = "TIME";
            if (!worksec.isEmpty()) {
                for (int i = 0; i < worksec.size(); i++) {
                    worksec.get(i).setText("\" ,");
                }
            }
            if (!exework.isEmpty()) {
                for (int i = 0; i < exework.size(); i++) {
                    exework.get(i).setHint("work");
                }
            }
            if (!pausesec.isEmpty()) {
                for (int i = 0; i < pausesec.size(); i++) {
                    pausesec.get(i).setText("\"");
                }
            }
            if (!exepause.isEmpty()) {
                for (int i = 0; i < exepause.size(); i++) {
                    exepause.get(i).setHint("pause");
                }
            }
            if (!additional.isEmpty()) {
                for (int i = 0; i < additional.size(); i++) {
                    additional.get(i).setText("");
                }
            }
            // set general hints
            generalworksec.setText("\" ,");
            generalpausesec.setText("\"");
            generalexework.setHint("work");
            generalexepause.setHint("pause");
            generaladditional.setText("");
            // set set info
            pauseOrTotalTime.setText("Pause:");
            textpause.setHint("seconds");
            type.setText("H.I.I.T. workout");
        } else if (idx == 1) {
            currentType = "REPS";
            if (!worksec.isEmpty()) {
                for (int i = 0; i < worksec.size(); i++) {
                    worksec.get(i).setText("X");
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
                    pausesec.get(i).setText("");
                }
            }
            if (!exepause.isEmpty()) {
                for (int i = 0; i < exepause.size(); i++) {
                    exepause.get(i).setHint("reps");
                }
            }
            if (!additional.isEmpty()) {
                for (int i = 0; i < additional.size(); i++) {
                    additional.get(i).setText("");
                }
            }
            // set general hints
            generalworksec.setText("X");
            generalpausesec.setText("");
            generalexework.setHint("");
            generalexework.setText(null);
            generalexepause.setHint("reps");
            generaladditional.setText("");
            // set set info
            pauseOrTotalTime.setText("Total time:");
            textpause.setHint("minutes");
            type.setText("Reps workout");
        } else {
            currentType = "REPTIME";
            if (!worksec.isEmpty()) {
                for (int i = 0; i < worksec.size(); i++) {
                    worksec.get(i).setText(" in");
                }
            }
            if (!exework.isEmpty()) {
                for (int i = 0; i < exework.size(); i++) {
                    exework.get(i).setHint("reps");
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
            if (!additional.isEmpty()) {
                for (int i = 0; i < additional.size(); i++) {
                    additional.get(i).setText("X");
                }
            }
            // set general hints
            generalworksec.setText(" in");
            generalpausesec.setText("\"");
            generalexework.setHint("reps");
            generalexepause.setHint("sec");
            generaladditional.setText("X");
            // set set info
            pauseOrTotalTime.setText("Pause:");
            textpause.setHint("seconds");
            type.setText("Reps in time");
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
            additional.remove(idx);
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
        exename.add((TextView) layout.findViewById(R.id.text_search));
        arrayList = new ArrayList<>();

        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);
        ArrayList<ExerciseDetail> exeList = dbhandler.loadAllExercises();

        for (int i=0; i<exeList.size(); i++) {
            arrayList.add(exeList.get(i).getName());
        }

        exename.get(exename.size()-1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout r = (RelativeLayout) view.getParent();
                final int idx = ((ViewGroup) r.getParent()).indexOfChild(r);

                dialog = new Dialog(AddWorkoutActivity.this);
                dialog.setContentView(R.layout.dialog_searchable_spinner);

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;

                dialog.getWindow().setLayout((int) ((float)width*1.0/5.0*4.0),(int) ((float)height*1.0/5.0*4.0));

                dialog.show();
                dialog.setCanceledOnTouchOutside(true);

                final EditText editText = dialog.findViewById(R.id.edit_search_exercise);
                ListView listView = dialog.findViewById(R.id.list_search_exercise);

                final ArrayAdapter<String> adapter = new ArrayAdapter<>(AddWorkoutActivity.this,
                        android.R.layout.simple_list_item_1,arrayList);

                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        exename.get(idx).setText(adapter.getItem(i));
                        exename.get(idx).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        dialog.dismiss();
                    }
                });

                ImageButton btnadd = dialog.findViewById(R.id.btnadd);
                btnadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        exename.get(idx).setText(editText.getText().toString());
                        exename.get(idx).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        dialog.dismiss();
                    }
                });
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
        additional.add((TextView) layout.findViewById(R.id.txtadditional));
        pausesec.add((TextView) layout.findViewById(R.id.txtpause));
        if (new String("REPS").equals(currentType)) {
            exepause.get(exepause.size() - 1).setHint("reps");
            pausesec.get(pausesec.size() - 1).setText("");
            exework.get(exework.size() - 1).setHint("");
            worksec.get(worksec.size() - 1).setText("X");
            additional.get(additional.size()-1).setText("");
        } else if (new String("REPTIME").equals(currentType)) {
            exepause.get(exepause.size() - 1).setHint("sec");
            exework.get(exework.size() - 1).setHint("reps");
            pausesec.get(pausesec.size() - 1).setText("\"");
            worksec.get(pausesec.size() - 1).setText(" in");
            additional.get(additional.size()-1).setText("X");
        } else {
            exepause.get(exepause.size() - 1).setHint("pause");
            exework.get(exework.size() - 1).setHint("work");
            worksec.get(pausesec.size() - 1).setText("\" ,");
            pausesec.get(pausesec.size() - 1).setText("\"");
            additional.get(additional.size()-1).setText("");
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
                                    reps = Integer.parseInt(exework.get(i).getText().toString());
                                    time = Integer.parseInt(exepause.get(i).getText().toString());
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
                            if (workoutToBeAdded.getTotalTime() == 0) {
                                textname.setHint("Total time is smaller than zero");
                                textname.setText(null);
                            } else {
                                int id = (int) dbhandler.addOrUpdateWorkout(workoutToBeAdded);
                                workoutToBeAdded.setID(id);

                                for (int i = 0; i < exercises.size(); i++) {
                                    dbhandler.addExerciseInWorkout(exercises.get(i), workoutToBeAdded);
                                    ExerciseDetail exe = dbhandler.loadOneExercise(exercises.get(i).getName());
                                    if (exe.getName()==null) {
                                        exe.setName(exercises.get(i).getName());
                                        exe.setDifficulty(-1);
                                        exe.setDescription("Sorry no exercise with that name, if you want you can add details for it below");
                                        exe.setMuscle("Not found");
                                        dbhandler.addOrUpdateExercise(exe);
                                    }
                                }
                            }
                            btnexercise.setText("Added!");
                        }
                    }
                }
            }
        }
    }

}