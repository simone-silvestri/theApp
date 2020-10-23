package com.example.firsttry;

import java.io.Serializable;
import java.util.ArrayList;

public class Workout implements Serializable, Comparable<Workout> {

    private String title;
    private String wod;
    private ArrayList<Exercise> exercises;
    private String type;
    private int difficulty;
    private int totalTime;
    private int numberOfSets;
    private int setPause;
    private int ID;

    @Override
    public int compareTo(Workout w) {
        if(this.difficulty > w.difficulty){
            return 1;
        } else {
            return -1;
        }
    }

    public Workout() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getNumberOfSets() {
        return numberOfSets;
    }

    public void setNumberOfSets(int numberOfSets) {
        this.numberOfSets = numberOfSets;
    }

    public int getSetPause() {
        return setPause;
    }

    public void setSetPause(int setPause) {
        this.setPause = setPause;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public void setTotalTime() {
        double time = 0;
        for (int i=0; i<this.exercises.size()-1; i++) {
            time += (double) (this.exercises.get(i).getTimeInSeconds() + this.exercises.get(i).getPauseInSeconds());
        }
        time += (double) this.exercises.get(this.exercises.size()-1).getTimeInSeconds();
        time = time*this.numberOfSets;
        time += (this.numberOfSets-1)*this.setPause;
        this.totalTime = (int) (time);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    public void addExerciseAtEnd(Exercise exercise) {
        ArrayList<Exercise> ex = this.getExercises();
        ex.add(exercise);
        this.exercises = ex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWod() {
        return wod;
    }

    public void setWod(String wod) {
        this.wod = wod;
    }
}
