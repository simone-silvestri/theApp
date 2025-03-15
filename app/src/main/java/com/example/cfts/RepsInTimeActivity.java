package com.example.cfts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.media.AudioManager;

import java.util.ArrayList;
import java.util.Locale;

public class RepsInTimeActivity extends AppCompatActivity {

    private TextView command, nextExercise;
    private Workout work;
    private TextView countdownText, setnumber;
    private TextView nextRepNumber;
    private Button countdownButton;
    private long timeLeftInMilliseconds;
    private boolean timerRunning;
    private CountDownTimer countdownTimer;
    public void stopCountDownTimer() {
        countdownTimer.cancel();
    }

    @Override
    public void onBackPressed() {
        if(timerRunning) {
            stopCountDownTimer();
        }
        stopCountDownTimer();
        super.onBackPressed();
    }
    private int currentExercise, numberOfSets, currentSet;
    private ArrayList<Exercise> exercises;
    private TextToSpeech ttobj;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reps_in_time);

        View currentView = findViewById(R.id.repsintimeview);
        currentView.setKeepScreenOn(true);

        timerRunning = false;
        timeLeftInMilliseconds = 10000;

        command = (TextView) findViewById(R.id.command);
        Bundle extra = getIntent().getExtras();
        if(extra !=null) { work = (Workout) extra.getSerializable("EXTRA_WORKOUT"); }

        command.setText(work.getTitle());
        countdownText = findViewById(R.id.countdownText);
        countdownButton = findViewById(R.id.countdownButton);
        setnumber = findViewById(R.id.setnumber);
        nextRepNumber = findViewById(R.id.numberofreps);

        exercises = work.getExercises();
        currentExercise = -1;

        command.setText("Get Ready!");

        nextExercise = (TextView) findViewById(R.id.nextexercise);
        numberOfSets = work.getNumberOfSets();
        currentSet = 1;
        setnumber.setText("set " + currentSet + " of " + numberOfSets);

//        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
//        int amStreamMusicMaxVol = am.getStreamMaxVolume(am.STREAM_NOTIFICATION);
//        am.setStreamVolume(am.STREAM_NOTIFICATION, amStreamMusicMaxVol, 0);

        ttobj=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if(status == TextToSpeech.SUCCESS){
                    int result=ttobj.setLanguage(Locale.US);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Intent installIntent = new Intent();
                        installIntent.setAction(
                                TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                        startActivity(installIntent);
                        Log.e("error", "This Language is not supported");
                    }
                    else{
                        ttobj.speak("Starting", TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }

        }, "com.google.android.tts");

        countdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStop();
            }
        });

        if(!exercises.isEmpty()) {
            nextExercise.setText("Next: "+ exercises.get(0).getName());
            nextRepNumber.setText("X " + exercises.get(0).getReps());
            startStop();
        }
    }


    public void startStop() {
        if (timerRunning) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    public void startTimer() {
        countdownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {

            @Override
            public void onTick(long l) {
                timeLeftInMilliseconds = l;
                updateTimer();
            }
            @Override
            public void onFinish() {
                if(currentExercise<exercises.size()-1) {
                    currentExercise += 1;
                    timeLeftInMilliseconds = exercises.get(currentExercise).getTimeInSeconds() * 1000;
                    command.setText((currentExercise + 1) + "/" + exercises.size() + " - " + "Work!");
                    nextExercise.setText(exercises.get(currentExercise).getName());
                    nextRepNumber.setText("X "+exercises.get(currentExercise).getReps());
                    timerRunning = false;
                    startStop();
                } else {
                    if(currentSet==numberOfSets) {
                        command.setText("Finished!!");
                        nextExercise.setText("Well Done");
                        countdownText.setText("Ole!");
                        DatabaseHelper dbhandler = DatabaseHelper.getInstance(RepsInTimeActivity.this);
                        long calendarId = dbhandler.addDateToCalendar(work.getTitle());
                    } else {
                        currentSet += 1;
                        currentExercise = -1;
                        timeLeftInMilliseconds = work.getSetPause()*1000;
                        command.setText("Break");
                        nextExercise.setText("Next: "+ exercises.get(currentExercise+1).getName());
                        nextRepNumber.setText("X "+exercises.get(currentExercise+1).getReps());
                        setnumber.setText("set " + currentSet + " of " + numberOfSets);
                        timerRunning = false;
                        startStop();
                    }
                }
            }
        }.start();
        countdownButton.setText("tap to pause");
        timerRunning = true;
    }
    public void stopTimer() {
        countdownTimer.cancel();
        countdownButton.setText("tap to start");
        countdownText.setText("paused");
        timerRunning = false;
    }

    public void updateTimer() {
        int minutes = (int) timeLeftInMilliseconds/60000;
        int seconds = (int) (timeLeftInMilliseconds % 60000) /1000;

        String timeLeftText;
        if(minutes>0) {
            timeLeftText = "" + minutes;
            timeLeftText += ":";
            if(seconds<10) timeLeftText+= "0";
            timeLeftText += seconds;
            countdownText.setText(timeLeftText);
        } else {
            if (seconds == 3) {
                ttobj.speak("three", TextToSpeech.QUEUE_FLUSH, null);
            } else if(seconds==2) {
                ttobj.speak("two", TextToSpeech.QUEUE_FLUSH, null);
            } else if(seconds==1) {
                ttobj.speak("one", TextToSpeech.QUEUE_FLUSH, null);
            } else if (seconds == 10) {
                if (currentExercise == -1) {
                    ttobj.speak(" first exercise, " + exercises.get(0).getName() + "; " + exercises.get(0).getReps() + " reps", TextToSpeech.QUEUE_FLUSH, null);
                } else if (currentExercise > -1 && currentExercise < exercises.size() - 1) {
                    ttobj.speak(" next exercise, " + exercises.get(currentExercise + 1).getName() + "; " + exercises.get(currentExercise+1).getReps() + " reps", TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    ttobj.speak("ten seconds left", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
            if (seconds == 0) {
                if (currentExercise == exercises.size() - 1) {
                    if (currentSet < numberOfSets) {
                        countdownText.setText("BREAK");
                        ttobj.speak("Break", TextToSpeech.QUEUE_FLUSH, null);
                    } else {
                        countdownText.setText("0");
                        ttobj.speak("Finished, well done!", TextToSpeech.QUEUE_FLUSH, null);
                    }
                } else {
                    countdownText.setText("GO!!");
                    ttobj.speak("Go!", TextToSpeech.QUEUE_FLUSH, null);
                }
            } else {
                timeLeftText = "" + seconds;
                countdownText.setText(timeLeftText);
            }
        }
    }
}