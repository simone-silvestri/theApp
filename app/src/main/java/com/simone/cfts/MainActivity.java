package com.simone.cfts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Random;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.simone.cfts.MESSAGE";
    private static final int CREATE_FILE = 1;
    private static final int OPEN_REQUEST_CODE = 41;
    private static final int WRITE_REQUEST_CODE = 101;

    TextView titlepage,subtitlepage,btexercise;
    ImageView picstatistic;
    Animation imgpage, bttone, btttwo, bttthree, ltr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load animation
        imgpage = AnimationUtils.loadAnimation(this, R.anim.imgpage);
        bttone = AnimationUtils.loadAnimation(this, R.anim.bttone);
        btttwo = AnimationUtils.loadAnimation(this, R.anim.btttwo);
        bttthree = AnimationUtils.loadAnimation(this, R.anim.bttthree);
        ltr = AnimationUtils.loadAnimation(this, R.anim.ltr);

        //import font
        titlepage = (TextView) findViewById(R.id.titlepage);
        subtitlepage = (TextView) findViewById(R.id.subtitlepage);
        btexercise = (TextView) findViewById(R.id.btexercise);
        picstatistic = (ImageView) findViewById(R.id.picstatistics);

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

        final ImageButton pubtnwrite = findViewById(R.id.buttonwrite);
        pubtnwrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showSingleActionPopup(arg0, "Write database to file?", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        writeDataBase(v);
                        Toast.makeText(MainActivity.this, "written database to file", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        final ImageButton pubtnread = findViewById(R.id.buttonread);
        pubtnread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showSingleActionPopup(arg0, null, "READ", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        readDataBase(v);
                        Toast.makeText(MainActivity.this, "read database from file", Toast.LENGTH_SHORT).show();
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

    private void showSingleActionPopup(View anchor, String message, String buttonText,
                                       final View.OnClickListener action) {
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View puView = layoutInflater.inflate(R.layout.popup_read_write, null);
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

        Button btn = puView.findViewById(R.id.button_write);
        if (buttonText != null) btn.setText(buttonText);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action != null) action.onClick(v);
                puWindow.dismiss();
            }
        });
    }

    public void writeDataBase(View view) {
        // when you create document, you need to add Intent.ACTION_CREATE_DOCUMENT
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        // filter to only show openable items.
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Create a file with the requested Mime type
        intent.setType("text/plain");
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    public void readDataBase(View view) {
        // when you create document, you need to add Intent.ACTION_CREATE_DOCUMENT
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        // filter to only show openable items.
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Create a file with the requested Mime type
        intent.setType("text/plain");
        startActivityForResult(intent, OPEN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WRITE_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (data != null
                            && data.getData() != null) {
                        writeInFile(data.getData());
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        } else if (requestCode == OPEN_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                if (data != null
                        && data.getData() != null) {
                    readFromFile(data.getData());
                }
                break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }
    }

    private void readFromFile(@NonNull Uri uri) {
        StringFormatter stringFormatter = new StringFormatter();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String toRead = reader.readLine();
            stringFormatter.setWodList(toRead);
            ArrayList<Workout> wodList = stringFormatter.getWodList();
            DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);
            for (int i=0; i<wodList.size(); i++) {
                int id = (int) dbhandler.addOrUpdateWorkout(wodList.get(i));
                wodList.get(i).setID(id);
                dbhandler.removeExercises(wodList.get(i));
                ArrayList<Exercise> exeList = wodList.get(i).getExercises();
                for (int j=0; j<exeList.size(); j++) {
                    dbhandler.addExerciseInWorkout(exeList.get(j),wodList.get(i));
                    ExerciseDetail exedet = new ExerciseDetail(exeList.get(j).getName(),1);
                    dbhandler.addOrUpdateExercise(exedet);
                }
            }

            StringFormatter sf2 = new StringFormatter();
            sf2.parseCaloriesAndGoal(toRead);
            for (StringFormatter.CalorieRow row : sf2.getCalorieRows()) {
                dbhandler.setMealKcal(row.date, row.meal, row.kcal);
            }
            if (sf2.getGoal() != null) {
                android.content.SharedPreferences prefs =
                        android.preference.PreferenceManager.getDefaultSharedPreferences(this);
                prefs.edit().putInt(HealthActivity.PREF_GOAL, sf2.getGoal()).apply();
            }
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading database from file", e);
        } finally {
            try {
                if (reader != null) reader.close();
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                Log.e("MainActivity", "Error closing streams", e);
            }
        }
    }

    private void writeInFile(@NonNull Uri uri) {
        OutputStream outputStream = null;
        BufferedWriter bw = null;
        try {
            StringBuilder toWrite = new StringBuilder();
            DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);
            ArrayList<Workout> wodList = dbhandler.loadDatabase();

            StringFormatter stringFormatter = new StringFormatter();
            for (int i = 0; i < wodList.size(); i++) {
                stringFormatter.setContent(wodList.get(i));
                toWrite.append(stringFormatter.getContent());
            }

            StringFormatter sf2 = new StringFormatter();
            java.util.List<String> dates = new java.util.ArrayList<>();
            java.util.List<int[]> rows = dbhandler.dumpCaloriesAsTriples(dates);
            for (int i = 0; i < rows.size(); i++) {
                toWrite.append(sf2.appendCalorieRow(dates.get(i), rows.get(i)[0], rows.get(i)[1]));
            }
            android.content.SharedPreferences prefs =
                    android.preference.PreferenceManager.getDefaultSharedPreferences(this);
            int goal = prefs.getInt(HealthActivity.PREF_GOAL, HealthActivity.DEFAULT_GOAL);
            toWrite.append(sf2.appendGoalRow(goal));

            outputStream = getContentResolver().openOutputStream(uri);
            bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            bw.write(toWrite.toString());
            bw.flush();
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing database to file", e);
        } finally {
            try {
                if (bw != null) bw.close();
                if (outputStream != null) outputStream.close();
            } catch (IOException e) {
                Log.e("MainActivity", "Error closing streams", e);
            }
        }
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

        for (int i=0; i<wodList.size(); i++) {
            ArrayList<Exercise> exeList = new ArrayList<>();
            int id = (int) dbhandler.addOrUpdateWorkout(wodList.get(i));
            wodList.get(i).setID(id);
            dbhandler.removeExercises(wodList.get(i));
            exeList = wodList.get(i).getExercises();
            for (int j=0; j<exeList.size(); j++) {
                dbhandler.addExerciseInWorkout(exeList.get(j),wodList.get(i));
            }
        }
    }
}