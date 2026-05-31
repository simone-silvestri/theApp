package com.simone.cfts;

import android.util.Log;

import java.util.ArrayList;

public class StringFormatter {
    private static final String TAG = "StringFormatter";
    private static final String PASSWORD = "CFLSPASS";
    private static final String KCAL_MARKER = "CFLSKCAL";
    private static final String GOAL_MARKER = "CFLSGOAL";

    public static class CalorieRow {
        public final String date;
        public final int meal;
        public final int kcal;
        public CalorieRow(String date, int meal, int kcal) {
            this.date = date;
            this.meal = meal;
            this.kcal = kcal;
        }
    }

    private String content;
    private Workout workout;
    private ArrayList<Workout> wodList;
    private ArrayList<CalorieRow> calorieRows = new ArrayList<>();
    private Integer goal;

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
            // Handle concatenated format where END runs into the next record marker
            content = content
                    .replace("END" + PASSWORD, "END|" + PASSWORD)
                    .replace("END" + KCAL_MARKER, "END|" + KCAL_MARKER)
                    .replace("END" + GOAL_MARKER, "END|" + GOAL_MARKER);
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
                    if (name.equals("END") || name.equals(PASSWORD)
                            || name.equals(KCAL_MARKER) || name.equals(GOAL_MARKER)) {
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

    public ArrayList<CalorieRow> getCalorieRows() {
        return calorieRows;
    }

    public Integer getGoal() {
        return goal;
    }

    public String appendCalorieRow(String date, int meal, int kcal) {
        return KCAL_MARKER + "|" + date + "|" + meal + "|" + kcal + "|END";
    }

    public String appendGoalRow(int goalKcal) {
        return GOAL_MARKER + "|" + goalKcal + "|END";
    }

    public void parseCaloriesAndGoal(String content) {
        calorieRows = new ArrayList<>();
        goal = null;
        if (content == null) return;
        try {
            String normalized = content
                    .replace("END" + KCAL_MARKER, "END|" + KCAL_MARKER)
                    .replace("END" + GOAL_MARKER, "END|" + GOAL_MARKER)
                    .replace("END" + PASSWORD, "END|" + PASSWORD);
            String[] tokens = normalized.split("\\|");
            int i = 0;
            while (i < tokens.length) {
                if (KCAL_MARKER.equals(tokens[i]) && i + 3 < tokens.length) {
                    String date = tokens[i + 1];
                    int meal = Integer.parseInt(tokens[i + 2]);
                    int kcal = Integer.parseInt(tokens[i + 3]);
                    calorieRows.add(new CalorieRow(date, meal, kcal));
                    i += 4;
                    if (i < tokens.length && "END".equals(tokens[i])) i++;
                } else if (GOAL_MARKER.equals(tokens[i]) && i + 1 < tokens.length) {
                    goal = Integer.parseInt(tokens[i + 1]);
                    i += 2;
                    if (i < tokens.length && "END".equals(tokens[i])) i++;
                } else {
                    i++;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing calorie/goal records", e);
        }
    }
}
