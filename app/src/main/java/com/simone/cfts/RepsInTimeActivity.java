package com.simone.cfts;

import android.widget.TextView;

public class RepsInTimeActivity extends BaseWorkoutActivity {

    private TextView nextRepNumber;

    @Override protected int getLayoutId() { return R.layout.activity_reps_in_time; }
    @Override protected int getRootViewId() { return R.id.repsintimeview; }
    @Override protected long getInitialTimeMillis() { return 10000; }
    @Override protected int getInitialExerciseIndex() { return -1; }

    @Override
    protected void initExtraViews() {
        nextRepNumber = findViewById(R.id.numberofreps);
    }

    @Override
    protected void onExercisesReady() {
        setPhase(Phase.READY);
        if (!exercises.isEmpty()) {
            setExerciseHero(exercises.get(0).getName());
            nextRepNumber.setText("× " + exercises.get(0).getReps() + " reps");
            setNextHint(exercises.size() > 1 ? exercises.get(1).getName() : "");
            startStop();
        }
    }

    @Override
    protected void onTimerFinish() {
        if (currentExercise < exercises.size() - 1) {
            currentExercise += 1;
            timeLeftInMilliseconds = exercises.get(currentExercise).getTimeInSeconds() * 1000;
            setPhase(Phase.WORK);
            setExerciseHero(exercises.get(currentExercise).getName());
            nextRepNumber.setText("× " + exercises.get(currentExercise).getReps() + " reps");
            setNextHint(currentExercise + 1 < exercises.size()
                    ? exercises.get(currentExercise + 1).getName() : "");
            timerRunning = false;
            startStop();
        } else {
            if (currentSet == numberOfSets) {
                setPhase(Phase.DONE);
                setExerciseHero("Well done");
                setNextHint("");
                nextRepNumber.setText("");
                countdownText.setText("Ole!");
                markWorkoutComplete();
            } else {
                currentSet += 1;
                currentExercise = -1;
                timeLeftInMilliseconds = work.getSetPause() * 1000;
                setPhase(Phase.BREAK);
                setExerciseHero(exercises.get(0).getName());
                nextRepNumber.setText("× " + exercises.get(0).getReps() + " reps");
                setNextHint(exercises.size() > 1 ? exercises.get(1).getName() : "");
                setnumber.setText("SET " + currentSet + " OF " + numberOfSets);
                timerRunning = false;
                startStop();
            }
        }
    }

    @Override
    protected void updateTimer() {
        int minutes = getMinutes();
        int seconds = getSeconds();

        if (minutes > 0) {
            countdownText.setText(formatTime(timeLeftInMilliseconds));
        } else {
            if (seconds == 3) {
                speak("three");
            } else if (seconds == 2) {
                speak("two");
            } else if (seconds == 1) {
                speak("one");
            } else if (seconds == 10) {
                if (currentExercise == -1) {
                    speak(" first exercise, " + exercises.get(0).getName() + "; " + exercises.get(0).getReps() + " reps");
                } else if (currentExercise < exercises.size() - 1) {
                    speak(" next exercise, " + exercises.get(currentExercise + 1).getName() + "; " + exercises.get(currentExercise + 1).getReps() + " reps");
                } else {
                    speak("ten seconds left");
                }
            }
            if (seconds == 0) {
                if (currentExercise == exercises.size() - 1) {
                    if (currentSet < numberOfSets) {
                        countdownText.setText("BREAK");
                        speak("Break");
                    } else {
                        countdownText.setText("0");
                        speak("Finished, well done!");
                    }
                } else {
                    countdownText.setText("GO");
                    speak("Go!");
                }
            } else {
                countdownText.setText("" + seconds);
            }
        }
    }
}
