package com.example.firsttry;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.media.AudioManager;
import android.media.ToneGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class TimerActivity extends AppCompatActivity {

    private TextView command, nextExercise;
    private Workout work;
    private TextView countdownText, setnumber;
    private Button countdownButton;
    private long timeLeftInMilliseconds;
    private boolean timerRunning, pauseOrNot;
    private CountDownTimer countdownTimer;
    public void stopCountDownTimer() {
        countdownTimer.cancel();
    }

    @Override
    public void onBackPressed() {
        if(timerRunning) {
            stopCountDownTimer();
        }
        super.onBackPressed();
    }
    private int currentExercise, numberOfSets, currentSet;
    private ArrayList<Exercise> exercises;
    private ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 200);
    private TextToSpeech ttobj;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        View currentView = findViewById(R.id.timerview);
        currentView.setKeepScreenOn(true);

        command = (TextView) findViewById(R.id.command);
        Bundle extra = getIntent().getExtras();
        if(extra !=null) { work = (Workout) extra.getSerializable("EXTRA_WORKOUT"); }

        command.setText(work.getTitle());
        countdownText = findViewById(R.id.countdownText);
        countdownButton = findViewById(R.id.countdownButton);
        setnumber = findViewById(R.id.setnumber);

        exercises = work.getExercises();
        currentExercise = -1;
        pauseOrNot = true;

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
                        installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
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
        timerRunning = false;
        timeLeftInMilliseconds = 10000;

        if(!exercises.isEmpty()) {
            nextExercise.setText("Next: "+ exercises.get(currentExercise+1).getName());
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
                    if(pauseOrNot) {
                        currentExercise += 1;
                        timeLeftInMilliseconds = exercises.get(currentExercise).getTimeInSeconds()*1000;
                        command.setText((currentExercise+1)+"/"+exercises.size()+" - "+"Work!");
                        nextExercise.setText(exercises.get(currentExercise).getName());
                        pauseOrNot = false;
                    } else {
                        timeLeftInMilliseconds = exercises.get(currentExercise).getPauseInSeconds()*1000;
                        command.setText((currentExercise+1)+"/"+exercises.size()+" - "+"Pause");
                        nextExercise.setText("Next: "+ exercises.get(currentExercise+1).getName());
                        pauseOrNot = true;
                    }
                    timerRunning = false;
                    startStop();
                } else {
                    if(currentSet==numberOfSets) {
                        command.setText("Finished!!");
                        nextExercise.setText("Well Done");
                        countdownText.setText("Ole!");
                    } else {
                        currentSet += 1;
                        currentExercise = -1;
                        pauseOrNot = true;
                        timeLeftInMilliseconds = work.getSetPause()*1000;
                        command.setText("Break");
                        nextExercise.setText("Next: "+ exercises.get(currentExercise+1).getName());
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
            if(pauseOrNot){
                if (seconds == 7) {
                    if(currentExercise==-1) {
                        ttobj.speak(" first exercise, " + exercises.get(0).getName(), TextToSpeech.QUEUE_FLUSH, null);
                    }  else if (currentExercise>-1 && currentExercise<exercises.size()-1) {
                        ttobj.speak(" next exercise, " + exercises.get(currentExercise + 1).getName(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }
            if(seconds==3) {
                ttobj.speak("three", TextToSpeech.QUEUE_FLUSH, null);
            } else if(seconds==2) {
                ttobj.speak("two", TextToSpeech.QUEUE_FLUSH, null);
            } else if(seconds==1) {
                ttobj.speak("one", TextToSpeech.QUEUE_FLUSH, null);
            } else if(!pauseOrNot && seconds==exercises.get(currentExercise).getTimeInSeconds()/2) {
                ttobj.speak("Half way there", TextToSpeech.QUEUE_FLUSH, null);
            } else if(!pauseOrNot && seconds==10) {
                ttobj.speak("ten seconds left", TextToSpeech.QUEUE_FLUSH, null);
            }
            if (seconds == 0) {
                if (pauseOrNot) {
                    countdownText.setText("GO!!");
                    if(currentExercise==exercises.size()-1) {
                        ttobj.speak("Last round", TextToSpeech.QUEUE_FLUSH, null);
                    } else {
                    ttobj.speak("Work!!", TextToSpeech.QUEUE_FLUSH, null);
                    }
                } else {
                    if (currentExercise == exercises.size() - 1) {
                        if(currentSet<numberOfSets) {
                            countdownText.setText("BREAK");
                            ttobj.speak("Break", TextToSpeech.QUEUE_FLUSH, null);
                        } else {
                            countdownText.setText("0");
                            ttobj.speak("Finished, well done!", TextToSpeech.QUEUE_FLUSH, null);
                        }
                    } else {
                        countdownText.setText("REST");
                        ttobj.speak("Rest", TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            } else {
                timeLeftText = "" + seconds;
                countdownText.setText(timeLeftText);
            }
        }
    }
}