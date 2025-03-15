package com.example.firsttry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.firsttry.MESSAGE";
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

        TextToSpeech ttobj=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
            }
        }, "com.google.android.tts");
        ttobj.setLanguage(Locale.US);

        final ImageButton pubtn = (ImageButton) findViewById(R.id.buttonreset);
        pubtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                View puView = layoutInflater.inflate(R.layout.popup_are_you_sure, null);
                puView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.popup_show));

                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                final PopupWindow puWindow = new PopupWindow(puView,height,width,true);
                puWindow.showAtLocation(arg0, Gravity.CENTER, 0, 0);
                puWindow.setAnimationStyle(R.style.Animation);

                Button btnYes = (Button) puView.findViewById(R.id.button_yes);
                btnYes.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resetDatabase(v);
                        puWindow.dismiss();
                        Toast.makeText(MainActivity.this, "Database erased", Toast.LENGTH_SHORT).show();
                    }
                });
                Button btnNo = (Button) puView.findViewById(R.id.button_no);
                btnNo.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        puWindow.dismiss();
                    }
                });
                puWindow.showAsDropDown(pubtn, 0, 0);
            }
        });

        final ImageButton pubtnload = (ImageButton) findViewById(R.id.btnloaddatabase);
        pubtnload.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                View puView = layoutInflater.inflate(R.layout.popup_are_you_sure, null);
                puView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.popup_show));

                TextView text = (TextView) puView.findViewById(R.id.text_id);
                text.setText("Load the original corona workout routines?");

                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                final PopupWindow puWindow = new PopupWindow(puView,height,width,true);
                puWindow.showAtLocation(arg0, Gravity.CENTER, 0, 0);
                puWindow.setAnimationStyle(R.style.Animation);

                Button btnYes = (Button) puView.findViewById(R.id.button_yes);
                btnYes.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        populateDatabase(v);
                        puWindow.dismiss();
                        Toast.makeText(MainActivity.this, "Original database loaded", Toast.LENGTH_SHORT).show();
                    }
                });
                Button btnNo = (Button) puView.findViewById(R.id.button_no);
                btnNo.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        puWindow.dismiss();
                    }
                });
                puWindow.showAsDropDown(pubtnload, 0, 0);
            }
        });

        final ImageButton pubtnadd = (ImageButton) findViewById(R.id.buttonaddworkout);
        pubtnadd.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                View puView = layoutInflater.inflate(R.layout.popup_are_you_sure, null);
                puView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.popup_show));

                TextView text = (TextView) puView.findViewById(R.id.text_id);
                text.setText("Add workout to list");

                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                final PopupWindow puWindow = new PopupWindow(puView,height,width,true);
                puWindow.showAtLocation(arg0, Gravity.CENTER, 0, 0);
                puWindow.setAnimationStyle(R.style.Animation);

                Button btnYes = (Button) puView.findViewById(R.id.button_yes);
                btnYes.setText("new");
                btnYes.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEditor(v);
                        puWindow.dismiss();
                    }
                });
                Button btnNo = (Button) puView.findViewById(R.id.button_no);
                btnNo.setText("copy");
                btnNo.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openStringEditor(v);
                        puWindow.dismiss();
                    }
                });
                puWindow.showAsDropDown(pubtnadd, 0, 0);
            }
        });

        final ImageButton pubtnwrite = (ImageButton) findViewById(R.id.buttonwrite);
        pubtnwrite.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                View puView = layoutInflater.inflate(R.layout.popup_read_write, null);
                puView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.popup_show));

                TextView text = (TextView) puView.findViewById(R.id.text_id);
                text.setText("Write database to file?");

                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                final PopupWindow puWindow = new PopupWindow(puView,height,width,true);
                puWindow.showAtLocation(arg0, Gravity.CENTER, 0, 0);
                puWindow.setAnimationStyle(R.style.Animation);

                Button btnWrite = (Button) puView.findViewById(R.id.button_write);
                btnWrite.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        writeDataBase(v);
                        puWindow.dismiss();
                        Toast.makeText(MainActivity.this, "written database to file", Toast.LENGTH_SHORT).show();
                    }
                });
                puWindow.showAsDropDown(pubtnload, 0, 0);
            }
        });

        final ImageButton pubtnread = (ImageButton) findViewById(R.id.buttonread);
        pubtnread.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                View puView = layoutInflater.inflate(R.layout.popup_read_write, null);
                puView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.popup_show));

                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                final PopupWindow puWindow = new PopupWindow(puView,height,width,true);
                puWindow.showAtLocation(arg0, Gravity.CENTER, 0, 0);
                puWindow.setAnimationStyle(R.style.Animation);

                Button btnRead = (Button) puView.findViewById(R.id.button_write);
                btnRead.setText("READ");
                btnRead.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        readDataBase(v);
                        puWindow.dismiss();
                        Toast.makeText(MainActivity.this, "read database from file", Toast.LENGTH_SHORT).show();
                    }
                });
                puWindow.showAsDropDown(pubtnload, 0, 0);
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
        OutputStream outputStream;
        StringFormatter stringFormatter = new StringFormatter();
        try {
            String toRead;
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            toRead = reader.readLine();
            stringFormatter.setWodList(toRead);
            inputStream.close();
            ArrayList<Workout> wodList = stringFormatter.getWodList();
            DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);
            for (int i=0; i<wodList.size(); i++) {
                ArrayList<Exercise> exeList = new ArrayList<>();
                int id = (int) dbhandler.addOrUpdateWorkout(wodList.get(i));
                wodList.get(i).setID(id);
                dbhandler.removeExercises(wodList.get(i));
                exeList = wodList.get(i).getExercises();
                for (int j=0; j<exeList.size(); j++) {
                    dbhandler.addExerciseInWorkout(exeList.get(j),wodList.get(i));
                    ExerciseDetail exedet = new ExerciseDetail(exeList.get(j).getName(),1);
                    dbhandler.addOrUpdateExercise(exedet);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeInFile(@NonNull Uri uri) {
        OutputStream outputStream;
        try {
            String toWrite = "";
            DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);
            ArrayList<Workout> wodList = dbhandler.loadDatabase();

            StringFormatter stringFormatter = new StringFormatter();
            for (int i = 0; i < wodList.size(); i++) {
                stringFormatter.setContent(wodList.get(i));
                toWrite += stringFormatter.getContent();
            }
            outputStream = getContentResolver().openOutputStream(uri);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            bw.write(toWrite);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
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

        DefaultWorkouts defaultWorkouts = new DefaultWorkouts();

        ArrayList<Workout> wodList = defaultWorkouts.getWodList();

        for (int i=0; i<wodList.size(); i++) {
            ArrayList<Exercise> exeList = new ArrayList<>();
            int id = (int) dbhandler.addOrUpdateWorkout(wodList.get(i));
            wodList.get(i).setID(id);
            dbhandler.removeExercises(wodList.get(i));
            exeList = wodList.get(i).getExercises();
            for (int j=0; j<exeList.size(); j++) {
                dbhandler.addExerciseInWorkout(exeList.get(j),wodList.get(i));
                ExerciseDetail exedet = new ExerciseDetail(exeList.get(j).getName(),1);
                dbhandler.addOrUpdateExercise(exedet);
            }
        }

    }
}