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
        command.setText("Get Ready!");
        if (!exercises.isEmpty()) {
            nextExercise.setText("Next: " + exercises.get(0).getName());
            nextRepNumber.setText("X " + exercises.get(0).getReps());
            startStop();
        }
    }

    @Override
    protected void onTimerFinish() {
        if (currentExercise < exercises.size() - 1) {
            currentExercise += 1;
            timeLeftInMilliseconds = exercises.get(currentExercise).getTimeInSeconds() * 1000;
            command.setText((currentExercise + 1) + "/" + exercises.size() + " - " + "Work!");
            nextExercise.setText(exercises.get(currentExercise).getName());
            nextRepNumber.setText("X " + exercises.get(currentExercise).getReps());
            timerRunning = false;
            startStop();
        } else {
            if (currentSet == numberOfSets) {
                command.setText("Finished!!");
                nextExercise.setText("Well Done");
                countdownText.setText("Ole!");
                markWorkoutComplete();
            } else {
                currentSet += 1;
                currentExercise = -1;
                timeLeftInMilliseconds = work.getSetPause() * 1000;
                command.setText("Break");
                nextExercise.setText("Next: " + exercises.get(currentExercise + 1).getName());
                nextRepNumber.setText("X " + exercises.get(currentExercise + 1).getReps());
                setnumber.setText("set " + currentSet + " of " + numberOfSets);
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
                    countdownText.setText("GO!!");
                    speak("Go!");
                }
            } else {
                countdownText.setText("" + seconds);
            }
        }
    }
}
