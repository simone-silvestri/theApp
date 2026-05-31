package com.simone.cfts;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RepsActivity extends BaseWorkoutActivity {

    private TextView nextRepNumber;
    private Button doneButton;
    private boolean finished;

    @Override protected int getLayoutId() { return R.layout.activity_reps; }
    @Override protected int getRootViewId() { return R.id.repsview; }
    @Override protected long getInitialTimeMillis() { return work.getTotalTime() * 1000; }
    @Override protected int getInitialExerciseIndex() { return 0; }

    @Override
    protected void initExtraViews() {
        nextRepNumber = findViewById(R.id.numberofreps);
        doneButton = findViewById(R.id.doneButton);
        finished = false;
    }

    @Override
    protected void onExercisesReady() {
        setPhase(Phase.WORK);
        if (!exercises.isEmpty()) {
            setExerciseHero(exercises.get(0).getName());
            nextRepNumber.setText("× " + exercises.get(0).getReps() + " reps");
            setNextHint(exercises.size() > 1 ? exercises.get(1).getName() : "");
            startStop();
        }
    }

    @Override
    public void startStop() {
        if (finished) {
            countdownButton.setText("FINISHED");
        } else {
            super.startStop();
        }
    }

    public void switchExercise(View view) {
        if (finished) {
            doneButton.setText("FINISHED");
        } else {
            if (currentExercise < exercises.size() - 1) {
                currentExercise += 1;
                setExerciseHero(exercises.get(currentExercise).getName());
                nextRepNumber.setText("× " + exercises.get(currentExercise).getReps() + " reps");
                setNextHint(currentExercise + 1 < exercises.size()
                        ? exercises.get(currentExercise + 1).getName() : "");
                setPhase(Phase.WORK);
            } else {
                if (currentSet < numberOfSets) {
                    currentSet += 1;
                    currentExercise = 0;
                    setExerciseHero(exercises.get(currentExercise).getName());
                    nextRepNumber.setText("× " + exercises.get(currentExercise).getReps() + " reps");
                    setNextHint(exercises.size() > 1 ? exercises.get(1).getName() : "");
                    setnumber.setText("SET " + currentSet + " OF " + numberOfSets);
                    setPhase(Phase.WORK);
                } else {
                    nextRepNumber.setText("");
                    setPhase(Phase.DONE);
                    setExerciseHero("Well done");
                    setNextHint("");
                    markWorkoutComplete();
                    finished = true;
                    if (timerRunning) {
                        countdownTimer.cancel();
                        timerRunning = false;
                        long timeReq = work.getTotalTime() * 1000 - timeLeftInMilliseconds;
                        int minutes = (int) timeReq / 60000;
                        int seconds = (int) (timeReq % 60000) / 1000;
                        String timeLeftText = "" + minutes + ":";
                        if (seconds < 10) timeLeftText += "0";
                        timeLeftText += seconds;
                        countdownText.setText(timeLeftText);
                    } else {
                        countdownText.setText("> " + work.getTotalTime() + ":00");
                    }
                }
            }
        }
    }

    @Override
    protected void onTimerFinish() {
        // Reps timer just counts down total time — no action on finish
    }

    @Override
    protected void updateTimer() {
        int minutes = getMinutes();
        int seconds = getSeconds();

        if (minutes > 0) {
            countdownText.setText(formatTime(timeLeftInMilliseconds));
        } else {
            if (seconds == 0) {
                countdownText.setText("TIME UP");
            } else {
                countdownText.setText("" + seconds);
            }
        }
    }
}
