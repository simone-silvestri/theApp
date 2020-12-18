package com.example.firsttry;

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

public class RepsActivity extends AppCompatActivity {

    private TextView command, nextExercise;
    private Workout work;
    private TextView countdownText, setnumber;
    private TextView nextRepNumber;
    private Button countdownButton, doneButton;
    private long timeLeftInMilliseconds;
    private boolean timerRunning, finished;
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
        setContentView(R.layout.activity_reps);

        View currentView = findViewById(R.id.repsview);
        currentView.setKeepScreenOn(true);

        command = (TextView) findViewById(R.id.command);
        Bundle extra = getIntent().getExtras();
        if(extra !=null) { work = (Workout) extra.getSerializable("EXTRA_WORKOUT"); }

        command.setText(work.getTitle());
        countdownText = findViewById(R.id.countdownText);
        countdownButton = findViewById(R.id.countdownButton);
        setnumber = findViewById(R.id.setnumber);
        nextRepNumber = findViewById(R.id.numberofreps);
        doneButton = findViewById(R.id.doneButton);

        exercises = work.getExercises();
        currentExercise = 0;

        nextExercise = (TextView) findViewById(R.id.nextexercise);
        numberOfSets = work.getNumberOfSets();
        currentSet = 1;

        setnumber.setText("set " + currentSet + " of " + numberOfSets);
        command.setText((currentExercise + 1) + "/" + exercises.size() + " - " + "Work!");

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

        finished = false;
        timerRunning = false;
        timeLeftInMilliseconds = work.getTotalTime()*1000;

        countdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStop();
            }
        });
        if(!exercises.isEmpty()) {
            nextExercise.setText(exercises.get(0).getName());
            nextRepNumber.setText("X " + exercises.get(0).getReps());
            startStop();
        }
    }

    public void switchExercise(View view) {
        if (finished) {
            //do nothing
            doneButton.setText("finished!");
        } else {
            if (currentExercise < exercises.size() - 1) {
                currentExercise += 1;
                nextExercise.setText(exercises.get(currentExercise).getName());
                nextRepNumber.setText("X " + exercises.get(currentExercise).getReps());
                command.setText((currentExercise + 1) + "/" + exercises.size() + " - " + "Work!");
            } else {
                if (currentSet < numberOfSets) {
                    currentSet += 1;
                    currentExercise = 0;
                    nextExercise.setText(exercises.get(currentExercise).getName());
                    nextRepNumber.setText("X " + exercises.get(currentExercise).getReps());
                    command.setText((currentExercise + 1) + "/" + exercises.size() + " - " + "Work!");
                    setnumber.setText("set " + currentSet + " of " + numberOfSets);
                } else {
                    nextRepNumber.setText("");
                    command.setText("Finished!!");
                    finished = true;
                    if (timerRunning) {
                        finishTimer();
                        long timeReq = work.getTotalTime() * 1000 - timeLeftInMilliseconds;
                        int minutes = (int) timeReq / 60000;
                        int seconds = (int) (timeReq % 60000) / 1000;
                        String timeLeftText;
                        timeLeftText = "" + minutes;
                        timeLeftText += ":";
                        if (seconds < 10) timeLeftText += "0";
                        timeLeftText += seconds;

                        nextExercise.setText("Time required: " + timeLeftText);
                    } else {
                        nextExercise.setText("Time required: more than " + work.getTotalTime() + ":00");
                    }
                }
            }
        }
    }


    public void startStop() {
        if(finished) {
            countdownButton.setText("Finished!");
        } else {
            if (timerRunning) {
                stopTimer();
            } else {
                startTimer();
            }
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

    public void finishTimer() {
        countdownTimer.cancel();
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
            if (seconds == 0) {
                countdownText.setText("Time Finished!");
            } else {
                timeLeftText = "" + seconds;
                countdownText.setText(timeLeftText);
            }
        }
    }
}