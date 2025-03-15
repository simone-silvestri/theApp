package com.simone.cfts;

import java.io.Serializable;

public class Exercise implements Serializable {

    private String name;
    private int workoutId;
    private String workoutName;
    private int reps;
    private int timeInSeconds;
    private int pauseInSeconds;

    public Exercise(String name, int reps, int timeInSeconds, int pauseInSeconds) {
        this.name = name;
        this.reps = reps;
        this.timeInSeconds = timeInSeconds;
        this.pauseInSeconds = pauseInSeconds;
    }

    public Exercise() {
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public int getPauseInSeconds() {
        return pauseInSeconds;
    }

    public void setPauseInSeconds(int pauseInSeconds) {
        this.pauseInSeconds = pauseInSeconds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(int timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

}
