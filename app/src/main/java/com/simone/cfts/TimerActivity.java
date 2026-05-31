package com.simone.cfts;

import android.media.AudioManager;
import android.media.ToneGenerator;

public class TimerActivity extends BaseWorkoutActivity {

    private boolean pauseOrNot;
    private ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 200);

    @Override protected int getLayoutId() { return R.layout.activity_timer; }
    @Override protected int getRootViewId() { return R.id.timerview; }
    @Override protected long getInitialTimeMillis() { return 10000; }
    @Override protected int getInitialExerciseIndex() { return -1; }

    @Override
    protected void initExtraViews() {
        // No extra views for timer activity
    }

    @Override
    protected void onExercisesReady() {
        pauseOrNot = true;
        setPhase(Phase.READY);
        if (!exercises.isEmpty()) {
            setExerciseHero(exercises.get(0).getName());
            setNextHint(exercises.size() > 1 ? exercises.get(1).getName() : "");
            startStop();
        }
    }

    @Override
    protected void onDestroy() {
        if (toneGenerator != null) {
            toneGenerator.release();
        }
        super.onDestroy();
    }

    @Override
    protected void onTimerFinish() {
        if (currentExercise < exercises.size() - 1) {
            if (pauseOrNot) {
                currentExercise += 1;
                timeLeftInMilliseconds = exercises.get(currentExercise).getTimeInSeconds() * 1000;
                setPhase(Phase.WORK);
                setExerciseHero(exercises.get(currentExercise).getName());
                setNextHint(currentExercise + 1 < exercises.size()
                        ? exercises.get(currentExercise + 1).getName() : "");
                pauseOrNot = false;
            } else {
                timeLeftInMilliseconds = exercises.get(currentExercise).getPauseInSeconds() * 1000;
                setPhase(Phase.PAUSE);
                setExerciseHero(exercises.get(currentExercise + 1).getName());
                setNextHint(currentExercise + 2 < exercises.size()
                        ? exercises.get(currentExercise + 2).getName() : "");
                pauseOrNot = true;
            }
            timerRunning = false;
            startStop();
        } else {
            if (currentSet == numberOfSets) {
                setPhase(Phase.DONE);
                setExerciseHero("Well done");
                setNextHint("");
                countdownText.setText("Ole!");
                markWorkoutComplete();
            } else {
                currentSet += 1;
                currentExercise = -1;
                pauseOrNot = true;
                timeLeftInMilliseconds = work.getSetPause() * 1000;
                setPhase(Phase.BREAK);
                setExerciseHero(exercises.get(0).getName());
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
            if (pauseOrNot) {
                if (seconds == 7) {
                    if (currentExercise == -1) {
                        speak(" first exercise, " + exercises.get(0).getName());
                    } else if (currentExercise < exercises.size() - 1) {
                        speak(" next exercise, " + exercises.get(currentExercise + 1).getName());
                    }
                }
            }
            if (seconds == 3) {
                speak("three");
            } else if (seconds == 2) {
                speak("two");
            } else if (seconds == 1) {
                speak("one");
            } else if (!pauseOrNot && seconds == exercises.get(currentExercise).getTimeInSeconds() / 2) {
                speak("Half way there");
            } else if (!pauseOrNot && seconds == 10) {
                speak("ten seconds left");
            }
            if (seconds == 0) {
                if (pauseOrNot) {
                    countdownText.setText("GO");
                    if (currentExercise == exercises.size() - 1) {
                        speak("Last round");
                    } else {
                        speak("Work!!");
                    }
                } else {
                    if (currentExercise == exercises.size() - 1) {
                        if (currentSet < numberOfSets) {
                            countdownText.setText("BREAK");
                            speak("Break");
                        } else {
                            countdownText.setText("0");
                            speak("Finished, well done!");
                        }
                    } else {
                        countdownText.setText("REST");
                        speak("Rest");
                    }
                }
            } else {
                countdownText.setText("" + seconds);
            }
        }
    }
}
