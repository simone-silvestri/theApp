package com.simone.cfts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public abstract class BaseWorkoutActivity extends AppCompatActivity {

    private static final String TAG = "BaseWorkoutActivity";

    protected TextView command, nextExercise;
    protected Workout work;
    protected TextView countdownText, setnumber;
    protected Button countdownButton;
    protected long timeLeftInMilliseconds;
    protected boolean timerRunning;
    protected CountDownTimer countdownTimer;
    protected int currentExercise, numberOfSets, currentSet;
    protected ArrayList<Exercise> exercises;
    protected TextToSpeech ttobj;

    protected abstract int getLayoutId();
    protected abstract int getRootViewId();
    protected abstract long getInitialTimeMillis();
    protected abstract int getInitialExerciseIndex();
    protected abstract void onTimerFinish();
    protected abstract void updateTimer();
    protected abstract void initExtraViews();
    protected abstract void onExercisesReady();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        View currentView = findViewById(getRootViewId());
        currentView.setKeepScreenOn(true);

        command = findViewById(R.id.command);
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            work = (Workout) extra.getSerializable("EXTRA_WORKOUT");
        }

        command.setText(work.getTitle());
        countdownText = findViewById(R.id.countdownText);
        countdownButton = findViewById(R.id.countdownButton);
        setnumber = findViewById(R.id.setnumber);
        nextExercise = findViewById(R.id.nextexercise);

        exercises = work.getExercises();
        numberOfSets = work.getNumberOfSets();
        currentSet = 1;
        setnumber.setText("set " + currentSet + " of " + numberOfSets);

        initExtraViews();

        initTextToSpeech();

        countdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStop();
            }
        });

        currentExercise = getInitialExerciseIndex();
        timeLeftInMilliseconds = getInitialTimeMillis();
        timerRunning = false;

        onExercisesReady();
    }

    protected void initTextToSpeech() {
        ttobj = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = ttobj.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Intent installIntent = new Intent();
                        installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                        startActivity(installIntent);
                        Log.e(TAG, "This Language is not supported");
                    } else {
                        speak("Starting");
                    }
                } else {
                    Log.e(TAG, "Initialization Failed!");
                }
            }
        }, "com.google.android.tts");
    }

    protected void speak(String text) {
        if (ttobj != null) {
            ttobj.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void stopCountDownTimer() {
        if (countdownTimer != null) {
            countdownTimer.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        stopCountDownTimer();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (countdownTimer != null) {
            countdownTimer.cancel();
        }
        if (ttobj != null) {
            ttobj.stop();
            ttobj.shutdown();
        }
        super.onDestroy();
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
                onTimerFinish();
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

    protected String formatTime(long millis) {
        int minutes = (int) millis / 60000;
        int seconds = (int) (millis % 60000) / 1000;
        if (minutes > 0) {
            String text = "" + minutes + ":";
            if (seconds < 10) text += "0";
            text += seconds;
            return text;
        }
        return "" + seconds;
    }

    protected int getSeconds() {
        return (int) (timeLeftInMilliseconds % 60000) / 1000;
    }

    protected int getMinutes() {
        return (int) timeLeftInMilliseconds / 60000;
    }

    protected void markWorkoutComplete() {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);
        int workoutId = dbhandler.loadWorkoutId(work.getTitle());
        String today = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
                .format(new java.util.Date());
        long rowId = dbhandler.addWorkoutOnDate(today, workoutId);
        SyncManager.get(getApplicationContext()).notifyCalendarAdd(rowId, today, workoutId);
    }
}
