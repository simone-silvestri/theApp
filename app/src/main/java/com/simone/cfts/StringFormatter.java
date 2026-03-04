package com.simone.cfts;

import android.util.Log;

import java.util.ArrayList;

public class StringFormatter {
    private static final String TAG = "StringFormatter";
    private static final String PASSWORD = "CFLSPASS";

    private String content;
    private Workout workout;
    private ArrayList<Workout> wodList;

    public StringFormatter() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(Workout work) {
        StringBuilder sb = new StringBuilder();
        sb.append(PASSWORD).append("|");
        sb.append(work.getTitle()).append("|");
        sb.append(work.getType()).append("|");
        sb.append(work.getDifficulty()).append("|");
        sb.append(work.getNumberOfSets()).append("|");
        sb.append(work.getSetPause()).append("|");
        sb.append(work.getTotalTime()).append("|");
        for (int i = 0; i < work.getExercises().size(); i++) {
            Exercise exe = work.getExercises().get(i);
            sb.append(exe.getName()).append("|");
            sb.append(exe.getTimeInSeconds()).append("|");
            sb.append(exe.getPauseInSeconds()).append("|");
            sb.append(exe.getReps()).append("|");
        }
        sb.append("END");
        this.content = sb.toString();
    }

    public Workout getWorkout() {
        return workout;
    }

    private Workout parseWorkout(String[] tokens, int startIndex) {
        if (startIndex + 6 >= tokens.length) {
            return null;
        }
        try {
            Workout work = new Workout();
            int i = startIndex;
            work.setTitle(tokens[i++]);
            work.setType(tokens[i++]);
            work.setDifficulty(Integer.parseInt(tokens[i++]));
            work.setNumberOfSets(Integer.parseInt(tokens[i++]));
            work.setSetPause(Integer.parseInt(tokens[i++]));
            work.setTotalTime(Integer.parseInt(tokens[i++]));

            ArrayList<Exercise> exeList = new ArrayList<>();
            while (i + 3 < tokens.length) {
                String name = tokens[i];
                if (name.equals("END") || name.equals(PASSWORD)) {
                    break;
                }
                Exercise exe = new Exercise();
                exe.setName(tokens[i++]);
                exe.setTimeInSeconds(Integer.parseInt(tokens[i++]));
                exe.setPauseInSeconds(Integer.parseInt(tokens[i++]));
                exe.setReps(Integer.parseInt(tokens[i++]));
                exeList.add(exe);
            }
            work.setExercises(exeList);
            return work;
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parsing workout data", e);
            return null;
        }
    }

    public void setWorkout(String content) {
        this.workout = null;
        try {
            content = content.replace("END" + PASSWORD, "END|" + PASSWORD);
            String[] tokens = content.split("\\|");
            if (tokens.length < 2 || !PASSWORD.equals(tokens[0])) {
                return;
            }
            this.workout = parseWorkout(tokens, 1);
        } catch (Exception e) {
            Log.e(TAG, "Error parsing workout string", e);
        }
    }

    public void setWodList(String content) {
        wodList = new ArrayList<>();
        try {
            // Handle concatenated format where END runs into CFLSPASS with no separator
            content = content.replace("END" + PASSWORD, "END|" + PASSWORD);
            String[] tokens = content.split("\\|");
            int i = 0;
            while (i < tokens.length) {
                if (!PASSWORD.equals(tokens[i])) {
                    i++;
                    continue;
                }
                i++; // skip password
                if (i + 6 >= tokens.length) {
                    break;
                }

                Workout work = new Workout();
                work.setTitle(tokens[i++]);
                work.setType(tokens[i++]);
                work.setDifficulty(Integer.parseInt(tokens[i++]));
                work.setNumberOfSets(Integer.parseInt(tokens[i++]));
                work.setSetPause(Integer.parseInt(tokens[i++]));
                work.setTotalTime(Integer.parseInt(tokens[i++]));

                ArrayList<Exercise> exeList = new ArrayList<>();
                while (i + 3 < tokens.length) {
                    String name = tokens[i];
                    if (name.equals("END") || name.equals(PASSWORD)) {
                        break;
                    }
                    Exercise exe = new Exercise();
                    exe.setName(tokens[i++]);
                    exe.setTimeInSeconds(Integer.parseInt(tokens[i++]));
                    exe.setPauseInSeconds(Integer.parseInt(tokens[i++]));
                    exe.setReps(Integer.parseInt(tokens[i++]));
                    exeList.add(exe);
                }
                work.setExercises(exeList);
                wodList.add(work);

                // Skip "END" marker if present
                if (i < tokens.length && tokens[i].equals("END")) {
                    i++;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing workout list string", e);
        }
    }

    public ArrayList<Workout> getWodList() {
        return wodList;
    }
}
