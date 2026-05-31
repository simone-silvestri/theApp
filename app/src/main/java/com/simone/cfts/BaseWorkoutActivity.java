package com.simone.cfts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public abstract class BaseWorkoutActivity extends AppCompatActivity {

    private static final String TAG = "BaseWorkoutActivity";

    protected enum Phase { READY, WORK, PAUSE, BREAK, DONE }

    private static final int COLOR_READY = 0xFFFFFFFF;
    private static final int COLOR_WORK  = 0xFFE58E26;
    private static final int COLOR_PAUSE = 0xFF7A8FB0;
    private static final int COLOR_BREAK = 0xFF7A8FB0;
    private static final int COLOR_DONE  = 0xFF71B62C;

    protected TextView command, nextExercise, exerciseLabel;
    protected View nextRow;
    protected TimerRingView timerRing;
    protected Workout work;
    protected TextView countdownText, setnumber;
    protected Button countdownButton;
    protected long timeLeftInMilliseconds;
    protected long phaseTotalMs;
    protected boolean timerRunning;
    protected CountDownTimer countdownTimer;
    protected int currentExercise, numberOfSets, currentSet;
    protected ArrayList<Exercise> exercises;
    protected TextToSpeech ttobj;
    protected Phase currentPhase = Phase.READY;

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
        exerciseLabel = findViewById(R.id.exerciseLabel);
        timerRing = findViewById(R.id.timerRing);
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            work = (Workout) extra.getSerializable("EXTRA_WORKOUT");
        }

        countdownText = findViewById(R.id.countdownText);
        countdownButton = findViewById(R.id.countdownButton);
        setnumber = findViewById(R.id.setnumber);
        nextExercise = findViewById(R.id.nextexercise);
        nextRow = findViewById(R.id.nextRow);

        if (exerciseLabel != null) exerciseLabel.setText(work.getTitle());

        exercises = work.getExercises();
        numberOfSets = work.getNumberOfSets();
        currentSet = 1;
        setnumber.setText("SET " + currentSet + " OF " + numberOfSets);

        ImageButton close = findViewById(R.id.workoutClose);
        if (close != null) {
            close.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { confirmAbort(); }
            });
        }

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
        phaseTotalMs = timeLeftInMilliseconds;
        // Show the initial value immediately. CountDownTimer's first onTick fires AFTER the
        // first interval, so without this you'd see "9" with a full ring instead of "10".
        if (countdownText != null) countdownText.setText(formatTime(timeLeftInMilliseconds));
        if (timerRing != null) timerRing.setProgress(1f);
        countdownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilliseconds = l;
                updateTimer();
                if (timerRing != null && phaseTotalMs > 0) {
                    // Quantize progress to whole seconds so the ring drains in equal arcs:
                    // one arc per displayed second, and 0 arcs the moment the display hits 0.
                    int secondsLeft  = (int) (l / 1000);
                    int totalSeconds = (int) (phaseTotalMs / 1000);
                    float p = totalSeconds > 0 ? (float) secondsLeft / totalSeconds : 0f;
                    timerRing.setProgress(p);
                }
            }

            @Override
            public void onFinish() {
                if (timerRing != null) timerRing.setProgress(0f);
                onTimerFinish();
            }
        }.start();
        countdownButton.setText("TAP TO PAUSE");
        timerRunning = true;
    }

    public void stopTimer() {
        countdownTimer.cancel();
        countdownButton.setText("TAP TO START");
        countdownText.setText("paused");
        timerRunning = false;
    }

    /** Update phase + apply phase color to the phase label, countdown text, and ring. */
    protected void setPhase(Phase p) {
        currentPhase = p;
        int color;
        String label;
        switch (p) {
            case WORK:  color = COLOR_WORK;  label = "WORK";  break;
            case PAUSE: color = COLOR_PAUSE; label = "PAUSE"; break;
            case BREAK: color = COLOR_BREAK; label = "BREAK"; break;
            case DONE:  color = COLOR_DONE;  label = "DONE";  break;
            case READY:
            default:    color = COLOR_READY; label = "READY"; break;
        }
        if (command != null) {
            command.setText(label);
            command.setTextColor(color);
        }
        if (countdownText != null) countdownText.setTextColor(color);
        if (timerRing != null) timerRing.setColor(color);
    }

    protected void setExerciseHero(String name) {
        if (exerciseLabel != null) exerciseLabel.setText(name == null ? "" : name);
    }

    protected void setNextHint(String name) {
        if (nextExercise == null) return;
        boolean empty = name == null || name.isEmpty();
        if (nextRow != null) nextRow.setVisibility(empty ? View.GONE : View.VISIBLE);
        nextExercise.setText(empty ? "" : name);
    }

    /** Cream popup to confirm abort. Mirrors the popup_are_you_sure family. */
    private void confirmAbort() {
        View content = LayoutInflater.from(this).inflate(R.layout.popup_are_you_sure, null);
        TextView text = content.findViewById(R.id.text_id);
        if (text != null) text.setText("Abort workout?");

        final PopupWindow pw = new PopupWindow(content,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(Color.TRANSPARENT));
        pw.setAnimationStyle(0);
        pw.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);

        TextView yes = content.findViewById(R.id.button_yes);
        TextView no  = content.findViewById(R.id.button_no);
        if (yes != null) {
            yes.setText("ABORT");
            yes.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    pw.dismiss();
                    stopCountDownTimer();
                    finish();
                }
            });
        }
        if (no != null) {
            no.setText("STAY");
            no.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { pw.dismiss(); }
            });
        }
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
