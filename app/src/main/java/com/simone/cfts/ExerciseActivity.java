package com.simone.cfts;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class ExerciseActivity extends AppCompatActivity {

    private TextView title, difftext;
    private EditText editdescription, editcategory;
    private ImageView easy, intermediate, advanced;
    private ExerciseDetail exe;
    private int currentDiff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        title = findViewById(R.id.title_tv);
        difftext = findViewById(R.id.difftext);
        editdescription = findViewById(R.id.editdescription);
        editcategory = findViewById(R.id.editcategory);
        easy = findViewById(R.id.imgeasy);
        intermediate = findViewById(R.id.imgintermediate);
        advanced = findViewById(R.id.imgadvanced);

        String name = getIntent().getStringExtra("EXTRA_NAME");
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        exe = db.loadOneExercise(name);
        if (exe.getName() == null) {
            exe = new ExerciseDetail(name, -1);
            exe.setDescription("");
            exe.setMuscle("");
        }

        title.setText(exe.getName());

        String desc = exe.getDescription();
        if (desc != null) editdescription.setText(desc);

        currentDiff = exe.getDifficulty() >= 1 && exe.getDifficulty() <= 3 ? exe.getDifficulty() : 1;
        applyDifficultySelection(currentDiff);

        editdescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) hideKeyboard(v);
            }
        });

        String currentCategory = exe.getMuscle();
        if (currentCategory != null && !currentCategory.isEmpty()
                && !currentCategory.equals("Not found")) {
            editcategory.setText(currentCategory);
        }
    }

    public void closeExercise(View v) { finish(); }

    public void pickCategory(View v) {
        final String[] categories = getResources().getStringArray(R.array.muscle_categories);
        new AlertDialog.Builder(this)
                .setTitle("Choose a category")
                .setItems(categories, (dialog, which) -> editcategory.setText(categories[which]))
                .show();
    }

    public void setCurrentDifficulty(View v) {
        LinearLayout r = (LinearLayout) v.getParent();
        int idx = r.indexOfChild(v);
        currentDiff = idx + 1;
        applyDifficultySelection(currentDiff);
    }

    private void applyDifficultySelection(int diff) {
        int largeDim = getResources().getDimensionPixelSize(R.dimen.large_exe);
        int smallDim = getResources().getDimensionPixelSize(R.dimen.small_exe);
        LinearLayout.LayoutParams small = new LinearLayout.LayoutParams(smallDim, smallDim, 1.0f);
        LinearLayout.LayoutParams large = new LinearLayout.LayoutParams(largeDim, largeDim, 1.0f);
        small.gravity = Gravity.CENTER;
        large.gravity = Gravity.CENTER;

        easy.setLayoutParams(diff == 1 ? large : small);
        intermediate.setLayoutParams(diff == 2 ? large : small);
        advanced.setLayoutParams(diff == 3 ? large : small);

        if (diff == 1) {
            difftext.setText("Easy");
            difftext.setTextColor(getResources().getColor(R.color.beginner));
        } else if (diff == 2) {
            difftext.setText("Intermediate");
            difftext.setTextColor(getResources().getColor(R.color.skilled));
        } else {
            difftext.setText("Advanced");
            difftext.setTextColor(getResources().getColor(R.color.spartan));
        }
    }

    public void addExerciseToDatabase(View v) {
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        exe.setDifficulty(currentDiff);
        exe.setMuscle(editcategory.getText().toString());
        exe.setDescription(editdescription.getText().toString());
        db.addOrUpdateExercise(exe);
        SyncManager.get(getApplicationContext()).notifyExerciseCatalogUpsert(exe);
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        hideKeyboard(v);
        finish();
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
