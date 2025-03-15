package com.simone.cfts;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ExerciseActivity extends AppCompatActivity {

    private TextView title, difficulty, description;
    private TextView txtdifficulty, txtdescription, btnendpage, difftext;
    private EditText editdescription;
    private ExerciseDetail exe;
    private LinearLayout difflayout;
    private ImageView easy,intermediate,advanced,initdiff;
    private Button btnaddorupdate;
    private View dividerView;
    private int currentDiff, addorupdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        title = findViewById(R.id.title_tv);
        difficulty = findViewById(R.id.text_diff);
        description = findViewById(R.id.Description);

        initdiff = findViewById(R.id.imginitialdiff);

        txtdifficulty = findViewById(R.id.textdifficulty); txtdifficulty.setVisibility(View.INVISIBLE);
        txtdescription = findViewById(R.id.textdescription);txtdescription.setVisibility(View.INVISIBLE);
        editdescription = findViewById(R.id.editdescription);editdescription.setVisibility(View.INVISIBLE);

        easy = findViewById(R.id.imgeasy);
        intermediate=findViewById(R.id.imgintermediate);
        advanced=findViewById(R.id.imgadvanced);

        difflayout = findViewById(R.id.linearlayout); difflayout.setVisibility(View.INVISIBLE);
        difftext = findViewById(R.id.difftext); difftext.setVisibility(View.INVISIBLE);
        dividerView = findViewById(R.id.dividerview); dividerView.setVisibility(View.INVISIBLE);
        btnendpage = findViewById(R.id.btnendofpage); btnendpage.setVisibility(View.INVISIBLE);

        btnaddorupdate = findViewById(R.id.addorupdatebutton);

        Bundle extra = getIntent().getExtras();
        String name = "";
        String buttontxt = "Update";
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);

        addorupdate = 0;

        if (extra != null) {
            name = extra.getString("EXTRA_NAME");

            exe = dbhandler.loadOneExercise(name);
            if (exe.getName()==null) {
                exe = new ExerciseDetail(name, -1);
                exe.setDescription("Sorry no exercise with that name, if you want you can add details for it below");
                exe.setMuscle("Not found");
                buttontxt = "Add details";
                addorupdate = 1;
            }
        }

        btnaddorupdate.setText(buttontxt);
        title.setText(exe.getName());

        if (exe.getDifficulty() == 1) {
            difficulty.setText("Easy");
            difficulty.setTextColor(getResources().getColor(R.color.beginner));
            initdiff.setImageResource(R.drawable.easy);
        } else if (exe.getDifficulty() == 2) {
            difficulty.setText("Intermediate");
            difficulty.setTextColor(getResources().getColor(R.color.skilled));
            initdiff.setImageResource(R.drawable.intermediate);
        } else if (exe.getDifficulty() == -1) {
            difficulty.setText("Not found");
            initdiff.setVisibility(View.GONE);
            difficulty.setTextColor(getResources().getColor(R.color.gray));
        } else {
            difficulty.setText("Advanced");
            difficulty.setTextColor(getResources().getColor(R.color.spartan));
            initdiff.setImageResource(R.drawable.advanced);
        }
        description.setText(exe.getDescription());
    }

    public void openViews(View view) {
        txtdifficulty.setVisibility(View.VISIBLE);
        txtdescription.setVisibility(View.VISIBLE);
        difftext.setVisibility(View.VISIBLE);
        editdescription.setVisibility(View.VISIBLE);
        dividerView.setVisibility(View.VISIBLE);
        btnendpage.setVisibility(View.VISIBLE);
        difflayout.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);

        editdescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        TextView olddiff = findViewById(R.id.olddiff);
        olddiff.setText("Old difficuty:");
        if(exe.getDifficulty()==-1) {
            btnendpage.setText("Add exercise");
        } else {
            btnendpage.setText("Update exercise");
        }
        currentDiff = 1;
    }

    public void setCurrentDifficulty(View v) {
        LinearLayout r = (LinearLayout) v.getParent();
        int idx = r.indexOfChild(v);
        TextView diffname = (TextView) findViewById(R.id.difftext);
        currentDiff = idx + 1;
        LinearLayout.LayoutParams small = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.small_exe), getResources().getDimensionPixelSize(R.dimen.small_exe),1.0f);
        LinearLayout.LayoutParams large = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.large_exe), getResources().getDimensionPixelSize(R.dimen.large_exe),1.0f);
        small.gravity= Gravity.CENTER;
        large.gravity= Gravity.CENTER;
        easy.setLayoutParams(small);
        intermediate.setLayoutParams(small);
        advanced.setLayoutParams(small);
        v.setLayoutParams(large);

        if(currentDiff==1) {
            diffname.setText("Easy");
            diffname.setTextColor(getResources().getColor(R.color.beginner));
        } else if (currentDiff==2) {
            diffname.setText("Intermediate");
            diffname.setTextColor(getResources().getColor(R.color.skilled));
        } else {
            diffname.setText("Advanced");
            diffname.setTextColor(getResources().getColor(R.color.spartan));
        }

    }

    public void addExerciseToDatabase(View v) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);

        if (currentDiff != -1) {
            exe.setDifficulty(currentDiff);
            exe.setMuscle("");
            if (!editdescription.getText().toString().isEmpty()) {
                exe.setDescription(editdescription.getText().toString());
                int exeID = (int) dbhandler.addOrUpdateExercise(exe);
                if (addorupdate == 0) {
                    btnendpage.setText("Updated!");
                } else {
                    btnendpage.setText("Added!");
                    addorupdate = 0;
                }
            }
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}