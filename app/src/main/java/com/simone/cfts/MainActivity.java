package com.simone.cfts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.simone.cfts.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SyncManager sync = SyncManager.get(getApplicationContext());
        if (sync.isSignedIn()) sync.onSignedIn(this);

        final ImageButton pubtn = findViewById(R.id.buttonreset);
        pubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showConfirmPopup(arg0, null, null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resetDatabase(v);
                        Toast.makeText(MainActivity.this, "Database erased", Toast.LENGTH_SHORT).show();
                    }
                }, null, null);
            }
        });

        final ImageButton pubtnadd = findViewById(R.id.buttonaddworkout);
        pubtnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showThreeActionPopup(arg0, "Add workout to list",
                        "new", new View.OnClickListener() {
                            @Override public void onClick(View v) { openEditor(v); }
                        },
                        "copy", new View.OnClickListener() {
                            @Override public void onClick(View v) { openStringEditor(v); }
                        },
                        "OG WODs", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                populateDatabase(v);
                                Toast.makeText(MainActivity.this, "Original database loaded", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    private void showConfirmPopup(View anchor, String message,
                                  String yesText, final View.OnClickListener yesAction,
                                  String noText, final View.OnClickListener noAction) {
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View puView = layoutInflater.inflate(R.layout.popup_are_you_sure, null);
        puView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.popup_show));

        if (message != null) {
            TextView text = puView.findViewById(R.id.text_id);
            text.setText(message);
        }

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow puWindow = new PopupWindow(puView, height, width, true);
        puWindow.showAtLocation(anchor, Gravity.CENTER, 0, 0);
        puWindow.setAnimationStyle(R.style.Animation);

        Button btnYes = puView.findViewById(R.id.button_yes);
        if (yesText != null) btnYes.setText(yesText);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesAction != null) yesAction.onClick(v);
                puWindow.dismiss();
            }
        });

        Button btnNo = puView.findViewById(R.id.button_no);
        if (noText != null) btnNo.setText(noText);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noAction != null) noAction.onClick(v);
                puWindow.dismiss();
            }
        });
    }

    private void showThreeActionPopup(View anchor, String message,
                                      String oneText, final View.OnClickListener oneAction,
                                      String twoText, final View.OnClickListener twoAction,
                                      String threeText, final View.OnClickListener threeAction) {
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View puView = layoutInflater.inflate(R.layout.popup_three_actions, null);
        puView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.popup_show));

        if (message != null) {
            TextView text = puView.findViewById(R.id.text_id);
            text.setText(message);
        }

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow puWindow = new PopupWindow(puView, height, width, true);
        puWindow.showAtLocation(anchor, Gravity.CENTER, 0, 0);
        puWindow.setAnimationStyle(R.style.Animation);

        wireThreeButton(puView, R.id.button_one,   oneText,   oneAction,   puWindow);
        wireThreeButton(puView, R.id.button_two,   twoText,   twoAction,   puWindow);
        wireThreeButton(puView, R.id.button_three, threeText, threeAction, puWindow);
    }

    private void wireThreeButton(View puView, int id, String label,
                                 final View.OnClickListener action, final PopupWindow pw) {
        Button btn = puView.findViewById(id);
        if (label != null) btn.setText(label);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action != null) action.onClick(v);
                pw.dismiss();
            }
        });
    }

    public void openStringEditor(View view) {
        Intent intent = new Intent(this, StringActivity.class);
        startActivity(intent);
    }

    public void openCalendar(View view) {
        Intent intent = new Intent(this, Calendar.class);
        startActivity(intent);
    }

    public void openHealth(View view) {
        Intent intent = new Intent(this, HealthActivity.class);
        startActivity(intent);
    }

    public void openSignIn(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    public void openLibrary(View view) {
        Intent intent = new Intent(this, WorkoutActivity.class);
        startActivity(intent);
    }

    public void openRandom(View view) {
        ArrayList<Workout> wodList = new ArrayList<>();
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);
        wodList = dbhandler.loadDatabase();
        if(!wodList.isEmpty()) {
            Random rand = new Random();

            // Obtain a number between [0 - wodList.size()].
            int randomNum = rand.nextInt(wodList.size());
            String title = wodList.get(randomNum).getTitle();
            String wod = wodList.get(randomNum).getWod();

            Workout work = wodList.get(randomNum);
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("EXTRA_TITLE", title);
            intent.putExtra("EXTRA_WOD", wod);
            intent.putExtra("EXTRA_WORKOUT", work);
            startActivity(intent);
        }
    }

    public void openEditor(View view) {
        Intent intent = new Intent(this, AddWorkoutActivity.class);
        startActivity(intent);
    }

    public void resetDatabase(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);
        dbhandler.deleteDatabase();
    }

    public void populateDatabase(View view) {

        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);

        DefaultWorkouts defaultWorkouts = new DefaultWorkouts(this);

        ArrayList<Workout> wodList = defaultWorkouts.getWodList();

        SyncManager sync = SyncManager.get(getApplicationContext());
        for (ExerciseDetail exe : defaultWorkouts.getExerciseDetails()) {
            sync.notifyExerciseCatalogUpsert(exe);
        }
        for (int i=0; i<wodList.size(); i++) {
            ArrayList<Exercise> exeList = new ArrayList<>();
            int id = (int) dbhandler.addOrUpdateWorkout(wodList.get(i));
            wodList.get(i).setID(id);
            dbhandler.removeExercises(wodList.get(i));
            exeList = wodList.get(i).getExercises();
            for (int j=0; j<exeList.size(); j++) {
                dbhandler.addExerciseInWorkout(exeList.get(j), wodList.get(i));
                ExerciseDetail exe = dbhandler.loadOneExercise(exeList.get(j).getName());
                if (exe.getName() == null) {
                    exe.setName(exeList.get(j).getName());
                    exe.setDifficulty(-1);
                    exe.setDescription("Sorry no exercise with that name, if you want you can add details for it below");
                    exe.setMuscle("Not found");
                    dbhandler.addOrUpdateExercise(exe);
                    sync.notifyExerciseCatalogUpsert(exe);
                }
            }
            sync.notifyWorkoutUpsert(wodList.get(i));
        }
    }
}