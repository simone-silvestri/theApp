package com.example.firsttry;

import java.util.ArrayList;

public class StringFormatter {
    private String content;
    private Workout workout;
    public StringFormatter() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(Workout work) {
        String str = "CFLSPASS|";
        str += work.getTitle() + "|";
        str += work.getType() + "|";
        str += work.getDifficulty() + "|";
        str += work.getNumberOfSets() + "|";
        str += work.getSetPause() + "|";
        str += work.getTotalTime() + "|";
        for (int i = 0; i < work.getExercises().size(); i++) {
            str += work.getExercises().get(i).getName() + "|";
            str += work.getExercises().get(i).getTimeInSeconds() + "|";
            str += work.getExercises().get(i).getPauseInSeconds() + "|";
            str += work.getExercises().get(i).getReps() + "|";
        }
        str += "END";
        this.content = str;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(String content) {
        // in string thus giving you the index of where it is in the string
        // Now iend can be -1, if lets say the string had no "." at all in it i.e. no "." is found.
        //So check and account for it.
        Workout work = new Workout();
        ArrayList<Exercise> exeList = new ArrayList<>();
        int iend = content.indexOf("|"); //this finds the first occurrence of "."
        if (iend != -1) {
            String psswd = content.substring(0, iend);
            content = content.substring(iend + 1);
            if (new String("CFLSPASS").equals(psswd)) {

                iend = content.indexOf("|"); //this finds the first occurrence of "."
                if (iend != -1) {
                    work.setTitle(content.substring(0, iend));
                    content = content.substring(iend + 1);
                }
                iend = content.indexOf("|"); //this finds the first occurrence of "."
                if (iend != -1) {
                    work.setType(content.substring(0, iend));
                    content = content.substring(iend + 1);
                }
                iend = content.indexOf("|"); //this finds the first occurrence of "."
                if (iend != -1) {
                    work.setDifficulty(Integer.parseInt(content.substring(0, iend)));
                    content = content.substring(iend + 1);
                }
                iend = content.indexOf("|"); //this finds the first occurrence of "."
                if (iend != -1) {
                    work.setNumberOfSets(Integer.parseInt(content.substring(0, iend)));
                    content = content.substring(iend + 1);
                }
                iend = content.indexOf("|"); //this finds the first occurrence of "."
                if (iend != -1) {
                    work.setSetPause(Integer.parseInt(content.substring(0, iend)));
                    content = content.substring(iend + 1);
                }
                iend = content.indexOf("|");
                if (iend != -1) {
                    work.setTotalTime(Integer.parseInt(content.substring(0, iend)));
                    content = content.substring(iend + 1);
                }
                iend = content.indexOf("|");
                if (iend != -1) {
                    do {
                        Exercise exe = new Exercise();
                        exe.setName(content.substring(0, iend));
                        content = content.substring(iend + 1);

                        iend = content.indexOf("|");
                        exe.setTimeInSeconds(Integer.parseInt(content.substring(0, iend)));
                        content = content.substring(iend + 1);

                        iend = content.indexOf("|");
                        exe.setPauseInSeconds(Integer.parseInt(content.substring(0, iend)));
                        content = content.substring(iend + 1);

                        iend = content.indexOf("|");
                        exe.setReps(Integer.parseInt(content.substring(0, iend)));
                        content = content.substring(iend + 1);

                        exeList.add(exe);
                        iend = content.indexOf("|");
                    } while (iend >= 0);
                }
                work.setExercises(exeList);
                this.workout = work;
            } else {
                this.workout = null;
            }
        } else {
            this.workout = null;
        }
    }
}
