package com.simone.cfts;

import java.io.Serializable;

public class ExerciseDetail implements Serializable {

    private int ID;
    private String name;
    private int difficulty;
    private String description;
    private String muscle;

    public ExerciseDetail(String name, int difficulty) {
        this.name = name;
        this.difficulty = difficulty;
    }

    public ExerciseDetail() {
    }

    public String getMuscle() {
        return muscle;
    }

    public void setMuscle(String muscle) {
        this.muscle = muscle;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
