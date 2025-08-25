package com.simone.cfts;

import android.content.Context;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DefaultWorkouts {
    private ArrayList<Workout> wodList;
    private ArrayList<ExerciseDetail> exeDetails;

    public ArrayList<ExerciseDetail> getExerciseDetails() {
        return this.exeDetails;
    }

    public ArrayList<Workout> getWodList() {
        return this.wodList;
    }

    public void DefaultExercises(View view) {
        this.exeDetails = new ArrayList<>();
        addExerciseList(view);
    }

    public DefaultWorkouts(View view) {
        this.wodList = new ArrayList<>();

        // Load default exercises
        DefaultExercises(view);

        addEntryLevel();
        addNoEffort();
        addFirstAttempt();
        addWholeBody();
        addMondayDetox();
        addMondayDetoxAli();
        addCardio();
        addPushups();
        addLegDay();
        addGreekTraining();
        addDoubleProg1();
        addDoubleProg2();
        addPlankPushups();
        addAbsChillax();
        addJordanYeoh();
        addMorningPushups();
        addWarmupPreTabata();
        addPureTabata();
        addFridayClassic();
        addFrasFridayClassic();
        addWholeBodyIntermediate();
        addBurpeesMattanza();
        addBalanced();
        addPureHIIT();
    }

    private ArrayList<Exercise> exerciseListFromName(ArrayList<String> names, int reps, int workTime, int restTime) {
        ArrayList<Exercise> exercises = new ArrayList<>();

        for (String name : names) {
            exercises.add(new Exercise(name, reps, workTime, restTime));
        }
        return exercises;
    }

    public void addEntryLevel() {
        ArrayList<Exercise> eList = exerciseListFromName(
                new ArrayList<>(Arrays.asList("Shoulder taps", "Bicycle crunches", "Burpees", "Crunches", "Dips",
                        "Mountain climbers", "Hip thrusts", "Side plank left", "Side plank right")),
                10, 40, 20
        );

        Workout wd = new Workout();
        wd.setDifficulty(1);
        wd.setNumberOfSets(3);
        wd.setSetPause(180);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("TIME");
        wd.setWod("This beginner-friendly workout focuses on full-body strength and core stability using simple bodyweight movements.");
        wd.setTitle("Entry Level");

        this.wodList.add(wd);
    }

    public void addNoEffort() {
        ArrayList<Exercise> eList = exerciseListFromName(
                new ArrayList<>(Arrays.asList("Lunges", "Crunches", "Squats", "Lower abs", "Hip thrusts", "Inchworms",
                "Hyperextensions", "Plank rotation", "Side squat", "Plank ups")),
                10, 40, 20
        );

        Workout wd = new Workout();
        wd.setDifficulty(1);
        wd.setNumberOfSets(3);
        wd.setSetPause(180);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("TIME");
        wd.setWod("A light full-body workout designed for minimal effort, focusing on mobility and core stability.");
        wd.setTitle("No Effort");

        this.wodList.add(wd);
    }

    public void addFirstAttempt() {
        ArrayList<Exercise> eList = exerciseListFromName(
                new ArrayList<>(Arrays.asList("Burpees (no pushup)", "Crunches", "Pushups", "Lower abs", "Hindu pushups", "Scissors",
                "Pike pushups", "Lunges", "Hyperextensions", "Plank")),
                10, 40, 20
        );

        Workout wd = new Workout();
        wd.setDifficulty(2);
        wd.setNumberOfSets(4);
        wd.setSetPause(120);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("TIME");
        wd.setWod("A challenging full-body workout to test your endurance and strength with a mix of cardio, core, and push variations.");
        wd.setTitle("First Attempt");

        this.wodList.add(wd);
    }

    public void addWholeBody() {
        ArrayList<Exercise> eList = exerciseListFromName(
                new ArrayList<>(Arrays.asList("Jump squats", "Crunches", "Knee pushups", "Plank", "Mountain climbers",
                        "Lunges", "Sit ups", "Lower abs", "Pike pushups", "Squats")),
                10, 45, 15
        );

        Workout wd = new Workout();
        wd.setDifficulty(2);
        wd.setNumberOfSets(4);
        wd.setSetPause(90);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("TIME");
        wd.setWod("A full-body workout designed to improve strength and endurance using dynamic bodyweight movements.");
        wd.setTitle("Whole Body");

        this.wodList.add(wd);
    }

    public void addMondayDetox() {
        ArrayList<Exercise> eList = exerciseListFromName(
                new ArrayList<>(Arrays.asList("Burpees", "Crunches", "Pushups", "Lower abs", "Diamond pushups", "Scissors",
                        "Pike pushups", "Jump lunges", "Side plank", "Hindu pushups")),
                10, 50, 10
        );

        Workout wd = new Workout();
        wd.setDifficulty(4);
        wd.setNumberOfSets(4);
        wd.setSetPause(120);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("TIME");
        wd.setWod("An intense full-body detox workout focusing on strength, endurance, and mobility with challenging push-up variations.");
        wd.setTitle("Monday Detox");

        this.wodList.add(wd);
    }

    public void addMondayDetoxAli() {
        ArrayList<Exercise> eList = exerciseListFromName(
                new ArrayList<>(Arrays.asList("Burpees", "Crunches", "Pushups", "Lower abs", "Donkey kicks", "Scissors",
                        "Pike pushups", "Lateral donkey", "Side plank", "Jump squats")),
                10, 50, 10
        );

        Workout wd = new Workout();
        wd.setDifficulty(3);
        wd.setNumberOfSets(4);
        wd.setSetPause(120);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("TIME");
        wd.setWod("A dynamic full-body workout focusing on strength, core stability, and lower-body power for an energizing start to the week.");
        wd.setTitle("Ali's Monday Detox");

        this.wodList.add(wd);
    }

    public void addCardio() {
        ArrayList<Exercise> eList = exerciseListFromName(
                new ArrayList<>(Arrays.asList("Burpees", "Seated tucks", "Jumping sit outs", "Butterfly squats (explosive)", "Knee tap pushups", "Ankle taps",
                "Super skater jumps", "Shoulder taps", "Plank switches", "Tuck jumps", "Lower abs", "Jumping jacks")),
                10, 40, 20
        );

        Workout wd = new Workout();
        wd.setDifficulty(2);
        wd.setNumberOfSets(4);
        wd.setSetPause(60);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("TIME");
        wd.setWod("A fast-paced cardio session to boost endurance and burn calories with dynamic full-body movements.");
        wd.setTitle("Cardio");

        this.wodList.add(wd);
    }

    public void addBalanced() {
        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(new Exercise("Jumping jacks", 40, 50, 0));
        eList.add(new Exercise("Squats", 10, 50, 0));
        eList.add(new Exercise("Russian twists", 20, 50, 0));
        eList.add(new Exercise("High knees", 45, 50, 0));
        eList.add(new Exercise("Sit ups", 15, 50, 0));
        eList.add(new Exercise("Knee pushups", 15, 50, 0));
        eList.add(new Exercise("Lower abs", 20, 50, 0));
        eList.add(new Exercise("Jump lunges", 20, 50, 0));
        eList.add(new Exercise("Hip thrusts", 15, 50, 0));
        eList.add(new Exercise("Burpees (no pushups)", 10, 50, 0));
        eList.add(new Exercise("Scissors", 40, 50, 0));
        eList.add(new Exercise("Jump squats", 10, 50, 0));

        Workout wd = new Workout();
        wd.setDifficulty(1);
        wd.setNumberOfSets(4);
        wd.setSetPause(120);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("REPSINTIME");
        wd.setWod("A balanced full-body beginner-friendly routine combining cardio, core, and strength for overall fitness.");
        wd.setTitle("Balanced Beginners");

        this.wodList.add(wd);
    }

    public void addPushups() {
        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(new Exercise("Burpees", 12, 50, 0));
        eList.add(new Exercise("Staggered pushups", 16, 50, 0));
        eList.add(new Exercise("Pushup rotation", 12, 50, 0));
        eList.add(new Exercise("Hindu pushups", 16, 50, 0));
        eList.add(new Exercise("One leg pushups", 12, 50, 0));
        eList.add(new Exercise("Diamond pushups", 16, 50, 0));
        eList.add(new Exercise("Spiderman pushups", 16, 50, 0));
        eList.add(new Exercise("Decline pushups", 12, 50, 0));
        eList.add(new Exercise("Archer pushups", 16, 50, 0));
        eList.add(new Exercise("One arm pushups", 12, 50, 0));
        eList.add(new Exercise("Explosive pushups", 10, 50, 0));

        Workout wd = new Workout();
        wd.setDifficulty(5);
        wd.setNumberOfSets(4);
        wd.setSetPause(120);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("REPSINTIME");
        wd.setWod("An advanced pushup challenge to build strength, endurance, and explosive power across chest, arms, and core.");
        wd.setTitle("Pushup Power");

        this.wodList.add(wd);
    }

    public void addLegDay() {
        ArrayList<Exercise> eList = exerciseListFromName(
                new ArrayList<>(Arrays.asList("Burpees", "Side plank leg up", "Single leg deadlifts", "Bicycle crunches", "Bottom up lunges", "Mountain climbers",
                "Pushups", "Single leg chair stands", "One legged plank", "Jump squats")),
                10, 50, 10
        );

        Workout wd = new Workout();
        wd.setDifficulty(4);
        wd.setNumberOfSets(4);
        wd.setSetPause(120);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("TIME");
        wd.setWod("A leg-focused workout targeting strength, balance, and explosive power for lower-body conditioning.");
        wd.setTitle("Leg Day");

        this.wodList.add(wd);
    }

    public void addGreekTraining() {
        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(new Exercise("Pushups", 40, 0, 0));
        eList.add(new Exercise("Jump squats", 40, 0, 0));
        eList.add(new Exercise("Burpees", 40, 0, 0));
        eList.add(new Exercise("Sit ups", 40, 0, 0));
        eList.add(new Exercise("Jump lunges", 40, 0, 0));
        eList.add(new Exercise("Plank", 1, 0, 0));
        eList.add(new Exercise("Pushups on fist", 40, 0, 0));
        eList.add(new Exercise("Burpees", 40, 0, 0));
        eList.add(new Exercise("Lower abs", 40, 0, 0));
        eList.add(new Exercise("Pushups", 40, 0, 0));
        eList.add(new Exercise("Plank", 1, 0, 0));

        Workout wd = new Workout();
        wd.setDifficulty(5);
        wd.setNumberOfSets(2);
        wd.setSetPause(0);
        wd.setExercises(eList);
        wd.setTotalTime(40 * 60); // 40 minutes
        wd.setType("REPS");
        wd.setWod("An extremely challenging full-body routine combining strength, cardio, and core endurance for maximal intensity.");
        wd.setTitle("Greek Training");

        this.wodList.add(wd);
    }

    public void addDoubleProg1() {
        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(new Exercise("Side burpees left", 10, 50, 10));
        eList.add(new Exercise("Run in place", 10, 50, 10));
        eList.add(new Exercise("Side burpees right", 10, 50, 10));
        eList.add(new Exercise("Isometric lower abs", 10, 50, 10));
        eList.add(new Exercise("Australian pullups", 10, 50, 10));
        eList.add(new Exercise("Pushup rotation", 10, 50, 10));
        eList.add(new Exercise("One legged squat", 10, 50, 10));
        eList.add(new Exercise("Sphinx pushups", 10, 50, 10));
        eList.add(new Exercise("Lower abs", 10, 50, 10));
        eList.add(new Exercise("Isometric pushups", 10, 50, 120));

        eList.add(new Exercise("Burpees", 10, 50, 10));
        eList.add(new Exercise("Australian pullups", 10, 50, 10));
        eList.add(new Exercise("Russian twists", 10, 50, 10));
        eList.add(new Exercise("Handstand pushups", 10, 50, 10));
        eList.add(new Exercise("Plank ups", 10, 50, 10));
        eList.add(new Exercise("Jump lunges", 10, 50, 10));
        eList.add(new Exercise("Archer pushups", 10, 50, 10));
        eList.add(new Exercise("Scissors", 10, 50, 10));
        eList.add(new Exercise("Decline pushups", 10, 50, 10));
        eList.add(new Exercise("Dips", 10, 50, 120));

        Workout wd = new Workout();
        wd.setDifficulty(4);
        wd.setNumberOfSets(2);
        wd.setSetPause(120);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("TIME");
        wd.setWod("A high-intensity, full-body program combining strength, cardio, core, and advanced push/pull movements.");
        wd.setTitle("Double Program 1");

        this.wodList.add(wd);
    }

    public void addDoubleProg2() {
        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(new Exercise("Side burpees left", 10, 50, 10));
        eList.add(new Exercise("Run in place", 10, 50, 10));
        eList.add(new Exercise("Side burpees right", 10, 50, 10));
        eList.add(new Exercise("Bicycle crunches", 10, 50, 10));
        eList.add(new Exercise("Hindu pushups", 10, 50, 10));
        eList.add(new Exercise("Shoulder taps", 10, 50, 10));
        eList.add(new Exercise("Windshield wipers", 10, 50, 10));
        eList.add(new Exercise("Jump squats", 10, 50, 10));
        eList.add(new Exercise("Pushups", 10, 50, 10));
        eList.add(new Exercise("Isometric lower abs", 10, 50, 120));

        eList.add(new Exercise("Burpees", 10, 50, 10));
        eList.add(new Exercise("Butterfly crunches", 10, 50, 10));
        eList.add(new Exercise("Jump crunches", 10, 50, 10));
        eList.add(new Exercise("Half burpees", 10, 50, 10));
        eList.add(new Exercise("Lower abs", 10, 50, 10));
        eList.add(new Exercise("Pushups toe touch", 10, 50, 10));
        eList.add(new Exercise("Punch sit ups", 10, 50, 10));
        eList.add(new Exercise("Power tucks", 10, 50, 10));
        eList.add(new Exercise("Pike pushups", 10, 50, 10));
        eList.add(new Exercise("Mountain climbers", 10, 50, 120));

        Workout wd = new Workout();
        wd.setDifficulty(4);
        wd.setNumberOfSets(2);
        wd.setSetPause(120);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("TIME");
        wd.setWod("A high-intensity, full-body program combining cardio, core, upper and lower body strength exercises.");
        wd.setTitle("Double Program 2");

        this.wodList.add(wd);
    }

    public void addPlankPushups() {
        ArrayList<Exercise> eList = exerciseListFromName(
                new ArrayList<>(Arrays.asList("Arm leg plank", "Knee pushups", "Pushups full rotation", "Plank ups", "Tiger band pushups", "Dips toe touch",
                "Side kicks", "Sphinx pushups", "Clap pushups", "Dips", "Plank leg lifts", "Plank stars", "Shoulder taps", "Diamond pushups", "Pushups rotation", 
                "Pushups toe touch", "One leg pushups", "One leg dips", "Plank", "Pushups half squat")),
                10, 20, 10
        );

        Workout wd = new Workout();
        wd.setDifficulty(3);
        wd.setNumberOfSets(4);
        wd.setSetPause(120);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("TIME");
        wd.setWod("High-intensity HIIT combining plank variations and pushups for abs, chest, and arms.");
        wd.setTitle("Abs-Chest HIIT");

        this.wodList.add(wd);
    }


    public void addAbsChillax() {
        ArrayList<Exercise> eList = exerciseListFromName(
                new ArrayList<>(Arrays.asList("Plank", "Russian twists", "Single leg crunches", "Plank kicks", "Jackknife twists", "3 way crunch pulses", "Side plank",
                "Low abs twists", "Low abs bent knees", "Side plank leg circle", "Seated leg raises", "Circle crunches", "Plank buzzsaw", "Russian twists", "Jackknives",
                "Full arm side plank leg up", "Lateral abs", "Scissor drops", "Plank jacks", "Butterfly crunches")),
                10, 20, 10
        );

        Workout wd = new Workout();
        wd.setDifficulty(2);
        wd.setNumberOfSets(4);
        wd.setSetPause(120);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("TIME");
        wd.setWod("A relaxing yet effective ab-focused HIIT, combining core, obliques, and lower abs exercises.");
        wd.setTitle("Abs Chillax");

        this.wodList.add(wd);
    }

    public void addJordanYeoh() {
        ArrayList<Exercise> eList = new ArrayList<>();

        // First set of exercises (short duration)
        Exercise pushupsToeTouchShort = new Exercise("Pushups toe touch", 10, 30, 2);
        Exercise runningJacksShort = new Exercise("Running jacks", 10, 30, 2);
        Exercise mountainClimbersShort = new Exercise("Mountain climbers", 10, 30, 2);
        Exercise buttKicksShort = new Exercise("Butt kicks", 10, 30, 2);
        Exercise burpeesShort = new Exercise("Burpees", 10, 30, 2);
        Exercise jumpLungesShort = new Exercise("Jump lunges", 10, 30, 2);

        // Second set of exercises (longer duration)
        Exercise pushupsToeTouchLong = new Exercise("Pushups toe touch", 10, 30, 30);
        Exercise runningJacksLong = new Exercise("Running jacks", 10, 30, 30);
        Exercise mountainClimbersLong = new Exercise("Mountain climbers", 10, 30, 30);
        Exercise buttKicksLong = new Exercise("Butt kicks", 10, 30, 30);
        Exercise burpeesLong = new Exercise("Burpees", 10, 30, 30);
        Exercise jumpLungesLong = new Exercise("Jump lunges", 10, 30, 120);

        // Add exercises in the original complex pattern
        eList.add(pushupsToeTouchLong);
        eList.add(pushupsToeTouchShort); eList.add(runningJacksLong);
        eList.add(pushupsToeTouchShort); eList.add(runningJacksShort); eList.add(mountainClimbersLong);
        eList.add(pushupsToeTouchShort); eList.add(runningJacksShort); eList.add(mountainClimbersShort); eList.add(buttKicksLong);
        eList.add(pushupsToeTouchShort); eList.add(runningJacksShort); eList.add(mountainClimbersShort); eList.add(buttKicksShort); eList.add(burpeesLong);
        eList.add(pushupsToeTouchShort); eList.add(runningJacksShort); eList.add(mountainClimbersShort); eList.add(buttKicksShort); eList.add(burpeesShort); eList.add(jumpLungesLong);

        eList.add(jumpLungesLong);
        eList.add(jumpLungesShort); eList.add(burpeesLong);
        eList.add(jumpLungesShort); eList.add(burpeesShort); eList.add(buttKicksLong);
        eList.add(jumpLungesShort); eList.add(burpeesShort); eList.add(buttKicksShort); eList.add(mountainClimbersLong);
        eList.add(jumpLungesShort); eList.add(burpeesShort); eList.add(buttKicksShort); eList.add(mountainClimbersShort); eList.add(runningJacksLong);
        eList.add(jumpLungesShort); eList.add(burpeesShort); eList.add(buttKicksShort); eList.add(mountainClimbersShort); eList.add(runningJacksShort); eList.add(pushupsToeTouchLong);

        Workout wd = new Workout();
        wd.setDifficulty(5);
        wd.setNumberOfSets(1);
        wd.setSetPause(0);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("TIME");
        wd.setWod("High-intensity, full-body HIIT combining cardio, plyometrics, and core exercises inspired by Jordan Yeoh training.");
        wd.setTitle("Jordan Yeoh HIIT");

        this.wodList.add(wd);
    }

    public void addMorningPushups() {
        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(new Exercise("Burpees", 15, 60, 0));
        eList.add(new Exercise("Diamond pushups", 16, 60, 0));
        eList.add(new Exercise("Spiderman pushups", 16, 60, 0));
        eList.add(new Exercise("One leg pushups", 15, 60, 0));
        eList.add(new Exercise("Staggered pushups", 16, 60, 0));
        eList.add(new Exercise("Archer pushups", 16, 60, 0));
        eList.add(new Exercise("Hindu pushups", 16, 60, 0));

        Workout wd = new Workout();
        wd.setDifficulty(4);
        wd.setNumberOfSets(3);
        wd.setSetPause(120);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("REPSINTIME");
        wd.setWod("A morning pushup routine targeting chest, arms, shoulders, and core to kickstart your day.");
        wd.setTitle("Morning Pushups");

        this.wodList.add(wd);
    }

    public void addWarmupPreTabata() {
        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(new Exercise("Squats", 10, 20, 10));
        eList.add(new Exercise("Pushups", 10, 20, 10));
        eList.add(new Exercise("Crunches", 10, 20, 10));
        eList.add(new Exercise("Squat taps", 10, 20, 10));
        eList.add(new Exercise("Jump jacks", 10, 20, 10));
        eList.add(new Exercise("Pushups", 10, 20, 10));
        eList.add(new Exercise("Hip thrusts", 10, 20, 10));
        eList.add(new Exercise("Leg raises", 10, 20, 10));
        eList.add(new Exercise("High plank", 10, 20, 10));
        eList.add(new Exercise("Run in place", 10, 20, 120));

        eList.add(new Exercise("Squat run", 10, 20, 10));
        eList.add(new Exercise("Pushups", 10, 20, 10));
        eList.add(new Exercise("Butterfly crunches", 10, 20, 10));
        eList.add(new Exercise("Lunges", 10, 20, 10));
        eList.add(new Exercise("Jump jacks", 10, 20, 10));
        eList.add(new Exercise("Pushups", 10, 20, 10));
        eList.add(new Exercise("Knee to elbow", 10, 20, 10));
        eList.add(new Exercise("Step in", 10, 20, 10));
        eList.add(new Exercise("Knee up run", 10, 20, 10));
        eList.add(new Exercise("Low plank", 10, 20, 10));

        Workout wd = new Workout();
        wd.setDifficulty(1);
        wd.setNumberOfSets(1);
        wd.setSetPause(0);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("TIME");
        wd.setWod("A dynamic full-body warmup to get your muscles ready and heart rate up before Tabata training.");
        wd.setTitle("Warmup pre Tabata");

        this.wodList.add(wd);
    }

    public void addPureTabata() {
        ArrayList<Exercise> eList = new ArrayList<>();

        Exercise burpees = new Exercise("Burpees", 10, 20, 10);
        Exercise jumpRope = new Exercise("Jump rope", 10, 20, 10);

        // Add exercises in Tabata pattern (8 rounds alternating)
        for (int i = 0; i < 4; i++) {
            eList.add(burpees);
            eList.add(jumpRope);
        }

        Workout wd = new Workout();
        wd.setDifficulty(3);
        wd.setNumberOfSets(2);
        wd.setSetPause(30);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("TIME");
        wd.setWod("An intense Tabata workout alternating burpees and jump rope to maximize cardiovascular and full-body endurance.");
        wd.setTitle("Pure Tabata");

        this.wodList.add(wd);
    }

    public void addFrasFridayClassic() {
        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(new Exercise("Jump Squats", 10, 40, 0));
        eList.add(new Exercise("Shoulder taps", 20, 40, 0));
        eList.add(new Exercise("Lower abs", 20, 50, 0));
        eList.add(new Exercise("Knee up run", 35, 40, 0));
        eList.add(new Exercise("Sit ups", 20, 50, 0));
        eList.add(new Exercise("Knee pushups", 20, 50, 0));
        eList.add(new Exercise("Plank", 1, 50, 0));

        Workout wd = new Workout();
        wd.setDifficulty(3);
        wd.setNumberOfSets(7);
        wd.setSetPause(60);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("REPSINTIME");
        wd.setWod("A classic Friday routine combining cardio, core, and strength exercises for a full-body challenge.");
        wd.setTitle("Fra's Friday Classic");

        this.wodList.add(wd);
    }

    public void addFridayClassic() {
        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(new Exercise("Jump Squats", 15, 40, 0));
        eList.add(new Exercise("Decline pushups", 20, 40, 0));
        eList.add(new Exercise("Lower abs", 20, 50, 0));
        eList.add(new Exercise("Dips", 20, 40, 0));
        eList.add(new Exercise("Sit ups", 20, 50, 0));
        eList.add(new Exercise("Pushups", 25, 50, 0));
        eList.add(new Exercise("Advanced plank", 1, 50, 0));

        Workout wd = new Workout();
        wd.setDifficulty(4);
        wd.setNumberOfSets(7);
        wd.setSetPause(60);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("REPSINTIME");
        wd.setWod("A high-intensity Friday routine combining strength, core, and explosive leg exercises for a full-body challenge.");
        wd.setTitle("Friday Classic");

        this.wodList.add(wd);
    }

    public void addWholeBodyIntermediate() {
        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(new Exercise("Diamond pushups", 15, 50, 0));
        eList.add(new Exercise("Jump lunges", 28, 50, 0));
        eList.add(new Exercise("Pushups", 20, 50, 0));
        eList.add(new Exercise("Lower abs", 20, 50, 0));
        eList.add(new Exercise("Burpees", 15, 50, 0));

        Workout wd = new Workout();
        wd.setDifficulty(3);
        wd.setNumberOfSets(6);
        wd.setSetPause(60);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("REPSINTIME");
        wd.setWod("An intermediate full-body routine combining explosive leg, chest, and core exercises to boost strength and endurance.");
        wd.setTitle("Whole Body Intense");

        this.wodList.add(wd);
    }

    public void addBurpeesMattanza() {
        ArrayList<Exercise> eList = new ArrayList<>();

        Exercise burpees = new Exercise("Burpees", 1, 4, 0);

        // Add the burpees 100 times to reach the 400-rep challenge
        for (int i = 0; i < 100; i++) {
            eList.add(burpees);
        }

        Workout wd = new Workout();
        wd.setDifficulty(5);
        wd.setNumberOfSets(4);
        wd.setSetPause(240);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("REPSINTIME");
        wd.setWod("A brutal 400-burpee challenge to test your full-body strength and endurance.");
        wd.setTitle("Burpees Mattanza");

        this.wodList.add(wd);
    }

    public void addPureHIIT() {
        ArrayList<Exercise> eList = exerciseListFromName(
                new ArrayList<>(Arrays.asList("High knees", "Mountain climbers", "Spiderman", "Pushups", "Jump squats", "Jump jacks")),
                10, 20, 10
        );

        // Sequence for the HIIT rounds
        eList.add(eList.get(0)); // High knees
        eList.add(eList.get(1)); // Mountain climbers
        eList.add(eList.get(0)); // High knees
        eList.add(eList.get(2)); // Spiderman
        eList.add(eList.get(3)); // Pushups
        eList.add(eList.get(2)); // Spiderman
        eList.add(eList.get(4)); // Jump squats
        eList.add(eList.get(5)); // Jump jacks
        eList.add(eList.get(4)); // Jump squats

        Workout wd = new Workout();
        wd.setDifficulty(4);
        wd.setNumberOfSets(7);
        wd.setSetPause(60);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("TIME");
        wd.setWod("A full-body high-intensity interval training session to boost cardio, strength, and endurance.");
        wd.setTitle("Pure HIIT");

        this.wodList.add(wd);
    }

    // EXERCISES!!!
        private ExerciseDetail addExerciseDetail(DatabaseHelper db, String name, int difficulty,
                                           String muscle, String description) {
        ExerciseDetail exe = new ExerciseDetail();
        exe.setName(name);
        exe.setDifficulty(difficulty);
        exe.setMuscle(muscle);
        exe.setDescription(description);
        db.addOrUpdateExercise(exe);
        return exe;
    }

    public void addExerciseList(View view) {
        DatabaseHelper db = DatabaseHelper.getInstance(view.getContext());

        // CORE & ABS EXERCISES
        this.exeDetails.add(addExerciseDetail(db, "Plank", 1, "Core",
                "Hold forearm plank position keeping body in a straight line."));
        this.exeDetails.add(addExerciseDetail(db, "Advanced plank", 2, "Core & Shoulders",
                "Hold a plank with variations such as arm/leg lifts for increased intensity."));
        this.exeDetails.add(addExerciseDetail(db, "One legged plank", 1, "Core & Balance",
                "Hold a plank position while lifting one leg off the ground, maintaining a straight line from head to heels."));
        this.exeDetails.add(addExerciseDetail(db, "Side plank", 2, "Core",
                "Lie on your side, prop up on one forearm, lift hips to form a straight line from head to feet, and hold."));
        this.exeDetails.add(addExerciseDetail(db, "Side plank left", 1, "Abs",
                "Lie on your left side, prop up on your forearm, lift hips to form a straight line, hold position."));
        this.exeDetails.add(addExerciseDetail(db, "Side plank right", 1, "Abs",
                "Lie on your right side, prop up on your forearm, lift hips to form a straight line, hold position."));
        this.exeDetails.add(addExerciseDetail(db, "Side plank leg up", 1, "Core & Glutes",
                "Hold a side plank position and lift the top leg upward, keeping your core engaged throughout."));
        this.exeDetails.add(addExerciseDetail(db, "Plank rotation", 1, "Core",
                "Start in a plank position, rotate your torso and extend one arm toward the ceiling, then return and repeat on the other side."));
        this.exeDetails.add(addExerciseDetail(db, "Plank ups", 2, "Core & Arms",
                "Move from forearm plank to high plank and back, alternating one arm at a time."));
        this.exeDetails.add(addExerciseDetail(db, "Plank switches", 2, "Core",
                "Alternate between high plank (hands) and low plank (forearms) positions by moving one arm at a time."));
        this.exeDetails.add(addExerciseDetail(db, "Plank leg lifts", 1, "Core & Glutes",
                "In plank, lift legs alternately keeping hips stable."));
        this.exeDetails.add(addExerciseDetail(db, "Plank stars", 2, "Core & Shoulders",
                "From plank, lift opposite arm and leg outwards forming a star shape."));
        this.exeDetails.add(addExerciseDetail(db, "Arm leg plank", 2, "Core & Shoulders",
                "In plank position, lift opposite arm and leg simultaneously and hold briefly."));
        this.exeDetails.add(addExerciseDetail(db, "Crunches", 1, "Abs",
                "Lie on your back, bend knees, lift your upper body toward your knees using your abs, then return slowly."));
        this.exeDetails.add(addExerciseDetail(db, "Bicycle crunches", 2, "Abs",
                "Lie on your back, lift shoulders off the ground, and alternate touching elbows to opposite knees in a pedaling motion."));
        this.exeDetails.add(addExerciseDetail(db, "Butterfly crunches", 2, "Abs",
                "Lie on back, bring soles together, perform crunches touching feet or ankles."));
        this.exeDetails.add(addExerciseDetail(db, "Jump crunches", 3, "Abs & Cardio",
                "Crunch up explosively while simultaneously jumping your feet off the floor."));
        this.exeDetails.add(addExerciseDetail(db, "Sit ups", 2, "Abs",
                "Lie on your back, bend knees, and lift your upper body all the way up to a sitting position, then lower slowly."));
        this.exeDetails.add(addExerciseDetail(db, "Punch sit ups", 2, "Abs & Arms",
                "Perform a sit up and punch forward at the top of the motion."));
        this.exeDetails.add(addExerciseDetail(db, "Lower abs", 1, "Abs",
                "Lie flat on your back, lift your legs toward the ceiling, then slowly lower them without touching the ground."));
        this.exeDetails.add(addExerciseDetail(db, "Isometric lower abs", 3, "Abs",
                "Hold legs slightly above the floor, keeping lower abs engaged."));
        this.exeDetails.add(addExerciseDetail(db, "Scissors", 2, "Abs",
                "Lie on your back, lift legs slightly, and alternate crossing them over each other in a scissor motion."));
        this.exeDetails.add(addExerciseDetail(db, "Seated tucks", 1, "Abs",
                "Sit on the floor, lean back slightly, pull your knees toward your chest, then extend your legs outward without touching the floor."));
        this.exeDetails.add(addExerciseDetail(db, "Ankle taps", 1, "Abs",
                "Lie on your back with knees bent, lift your shoulders slightly, and tap each ankle by bending sideways."));
        this.exeDetails.add(addExerciseDetail(db, "Russian twists", 2, "Core & Abs",
                "Sit with feet off the floor, twist torso side to side while holding hands together or a weight."));
        this.exeDetails.add(addExerciseDetail(db, "Windshield wipers", 1, "Core",
                "Lie on your back, lift legs, and rotate them side to side like wipers."));
        this.exeDetails.add(addExerciseDetail(db, "Power tucks", 3, "Abs & Cardio",
                "Jump and tuck knees to chest, landing softly before repeating."));
        this.exeDetails.add(addExerciseDetail(db, "Single leg crunches", 2, "Abs",
                "Perform crunches while keeping one leg raised and alternating sides."));
        this.exeDetails.add(addExerciseDetail(db, "Plank kicks", 1, "Core",
                "From plank, lift legs alternately in a kicking motion."));

        // CHEST & ARMS EXERCISES
        this.exeDetails.add(addExerciseDetail(db, "Pushups", 2, "Chest & Arms",
                "Start in high plank, lower your chest toward the floor, and push back up while keeping your core tight."));
        this.exeDetails.add(addExerciseDetail(db, "Knee pushups", 1, "Chest & Arms",
                "Perform pushups on knees with proper form, lowering chest to floor."));
        this.exeDetails.add(addExerciseDetail(db, "Diamond pushups", 3, "Chest & Triceps",
                "Form a diamond with hands and perform pushups targeting triceps."));
        this.exeDetails.add(addExerciseDetail(db, "Decline pushups", 2, "Chest & Shoulders",
                "Elevate your feet and perform pushups to target upper chest and shoulders."));
        this.exeDetails.add(addExerciseDetail(db, "Hindu pushups", 3, "Shoulders & Chest",
                "Start in a downward dog position, swoop your chest low and forward while bending elbows, then return to the starting position."));
        this.exeDetails.add(addExerciseDetail(db, "Staggered pushups", 3, "Chest & Arms",
                "Place one hand slightly forward and one back, perform pushups alternating sides for balance."));
        this.exeDetails.add(addExerciseDetail(db, "Pushup rotation", 3, "Chest & Core",
                "Do a pushup, then rotate your torso and raise one arm towards the ceiling for a side plank position."));
        this.exeDetails.add(addExerciseDetail(db, "Pushups rotation", 2, "Chest & Core",
                "Do a pushup and rotate into side plank, alternating sides."));
        this.exeDetails.add(addExerciseDetail(db, "One leg pushups", 3, "Chest & Core",
                "Perform pushups while keeping one leg lifted for extra core and balance challenge."));
        this.exeDetails.add(addExerciseDetail(db, "Spiderman pushups", 3, "Chest & Core",
                "As you lower in a pushup, bring one knee toward your elbow. Alternate sides each rep."));
        this.exeDetails.add(addExerciseDetail(db, "Archer pushups", 3, "Chest & Arms",
                "Keep one arm extended while lowering toward the opposite arm, mimicking an archer's draw motion."));
        this.exeDetails.add(addExerciseDetail(db, "One arm pushups", 3, "Chest & Core",
                "Perform pushups with one arm while keeping your core engaged and feet spread for stability."));
        this.exeDetails.add(addExerciseDetail(db, "Explosive pushups", 3, "Chest & Power",
                "Perform pushups explosively so your hands leave the floor. Land softly and repeat."));
        this.exeDetails.add(addExerciseDetail(db, "Pushups on fist", 3, "Chest & Arms",
                "Perform pushups on your fists for added wrist stability and forearm strength."));
        this.exeDetails.add(addExerciseDetail(db, "Knee tap pushups", 2, "Chest & Core",
                "Perform a push-up, then tap your knee with the opposite hand before starting the next rep."));
        this.exeDetails.add(addExerciseDetail(db, "Pushups toe touch", 2, "Chest & Core",
                "Perform pushups and touch one hand to opposite foot at the top."));
        this.exeDetails.add(addExerciseDetail(db, "Pushups full rotation", 2, "Chest & Core",
                "Do a pushup and rotate your body into a side plank before returning."));
        this.exeDetails.add(addExerciseDetail(db, "Tiger band pushups", 3, "Chest & Arms",
                "Perform pushups with resistance band around upper back for extra tension."));
        this.exeDetails.add(addExerciseDetail(db, "Clap pushups", 3, "Chest & Arms",
                "Perform explosive pushups and clap hands at the top."));
        this.exeDetails.add(addExerciseDetail(db, "Sphinx pushups", 3, "Triceps & Chest",
                "From forearm plank, lower and push up keeping elbows tucked."));
        this.exeDetails.add(addExerciseDetail(db, "Isometric pushups", 3, "Chest & Core",
                "Hold the low position of a pushup as long as possible, keeping your body straight."));
        this.exeDetails.add(addExerciseDetail(db, "Pushups half squat", 1, "Chest & Legs",
                "Do a pushup, then return to standing with a half squat before next repetition."));
        this.exeDetails.add(addExerciseDetail(db, "Shoulder taps", 2, "Chest & Core",
                "In plank position, tap each shoulder alternately keeping core tight."));

        // SHOULDERS & ARMS
        this.exeDetails.add(addExerciseDetail(db, "Pike pushups", 3, "Shoulders",
                "Start in a pike position with hips up, bend elbows to lower your head toward the ground, then push back up."));
        this.exeDetails.add(addExerciseDetail(db, "Handstand pushups", 3, "Shoulders & Arms",
                "Perform pushups while in a handstand position against a wall for support."));
        this.exeDetails.add(addExerciseDetail(db, "Dips", 1, "Triceps & Chest",
                "Lower your body using parallel bars or a bench and push back up, keeping elbows close to your body."));
        this.exeDetails.add(addExerciseDetail(db, "One leg dips", 2, "Triceps & Chest",
                "Perform dips while extending one leg forward or backward."));
        this.exeDetails.add(addExerciseDetail(db, "Dips toe touch", 1, "Triceps & Chest",
                "Do dips and at the top reach one hand toward your opposite toe."));
        this.exeDetails.add(addExerciseDetail(db, "Jumping sit outs", 2, "Core & Shoulders",
                "Start in a bear crawl position, lift one hand and opposite leg, rotate your body and kick the leg through, then return and repeat."));

        // BACK & ARMS
        this.exeDetails.add(addExerciseDetail(db, "Australian pullups", 2, "Back & Arms",
                "Hang under a bar, pull your chest toward the bar, then lower slowly while keeping your body straight."));
        this.exeDetails.add(addExerciseDetail(db, "Hyperextensions", 1, "Lower Back",
                "Lie face down, lift your chest and legs off the ground by contracting your lower back, then slowly return."));

        // LEGS EXERCISES
        this.exeDetails.add(addExerciseDetail(db, "Squats", 1, "Legs",
                "Stand with feet shoulder-width apart, lower hips back and down as if sitting in a chair, then return to standing."));
        this.exeDetails.add(addExerciseDetail(db, "Jump squats", 3, "Legs & Cardio",
                "Perform a squat, then jump explosively, landing softly and returning to squat position."));
        this.exeDetails.add(addExerciseDetail(db, "One legged squat", 3, "Legs & Balance",
                "Perform a squat on one leg while keeping the other leg extended forward, maintaining balance."));
        this.exeDetails.add(addExerciseDetail(db, "Side squat", 1, "Legs",
                "Take a wide stance, shift your weight to one side as you bend the knee and keep the other leg straight, then switch sides."));
        this.exeDetails.add(addExerciseDetail(db, "Butterfly squats (explosive)", 2, "Legs",
                "Start in a squat position, jump explosively and tap your feet together mid-air, then land softly back into the squat."));
        this.exeDetails.add(addExerciseDetail(db, "Lunges", 1, "Legs",
                "Step forward with one leg, lower your hips until both knees are bent at about 90 degrees, then push back to start."));
        this.exeDetails.add(addExerciseDetail(db, "Jump lunges", 3, "Legs & Cardio",
                "Lunge forward, jump explosively, switch legs mid-air, and land softly."));
        this.exeDetails.add(addExerciseDetail(db, "Bottom up lunges", 3, "Legs",
                "Step forward into a lunge, then drive back to standing, focusing on pushing from the heel and engaging glutes."));
        this.exeDetails.add(addExerciseDetail(db, "Single leg deadlifts", 1, "Legs & Glutes",
                "Stand on one leg, hinge at the hips keeping back straight, and lower your torso toward the floor, then return upright."));
        this.exeDetails.add(addExerciseDetail(db, "Single leg chair stands", 1, "Legs & Balance",
                "Sit on a chair, extend one leg, then stand up using only the other leg, keeping balance controlled."));
        this.exeDetails.add(addExerciseDetail(db, "Tuck jumps", 3, "Legs & Cardio",
                "Jump explosively while pulling your knees toward your chest, land softly, and immediately go into the next jump."));
        this.exeDetails.add(addExerciseDetail(db, "Super skater jumps", 2, "Legs & Cardio",
                "Jump laterally from side to side, landing on one leg with the other leg sweeping behind for balance."));
        this.exeDetails.add(addExerciseDetail(db, "Side kicks", 1, "Core & Legs",
                "From plank or standing, perform controlled side kicks."));

        // GLUTES EXERCISES
        this.exeDetails.add(addExerciseDetail(db, "Hip thrusts", 1, "Glutes",
                "Sit on the ground with your back against a bench, feet flat, and lift hips upward by squeezing your glutes."));
        this.exeDetails.add(addExerciseDetail(db, "Donkey kicks", 1, "Glutes",
                "Start on all fours, lift one leg upward while keeping your knee bent at 90 degrees, then lower and repeat."));
        this.exeDetails.add(addExerciseDetail(db, "Lateral donkey", 2, "Glutes",
                "Start on all fours, lift one leg to the side while keeping your knee bent, then lower back down and repeat."));

        // CARDIO & FULL BODY EXERCISES
        this.exeDetails.add(addExerciseDetail(db, "Burpees", 3, "Full Body & Cardio",
                "From standing, squat down, kick your feet back into a plank, return to squat, and jump explosively upward."));
        this.exeDetails.add(addExerciseDetail(db, "Burpees (no pushup)", 2, "Cardio",
                "From standing, squat down, kick your feet back into a plank, quickly return to squat, and jump explosively upward."));
        this.exeDetails.add(addExerciseDetail(db, "Half burpees", 2, "Full Body & Cardio",
                "Perform burpees without the pushup, finishing with a jump."));
        this.exeDetails.add(addExerciseDetail(db, "Side burpees left", 3, "Full Body & Cardio",
                "Perform a burpee, then jump laterally to the left before repeating."));
        this.exeDetails.add(addExerciseDetail(db, "Side burpees right", 3, "Full Body & Cardio",
                "Perform a burpee, then jump laterally to the right before repeating."));
        this.exeDetails.add(addExerciseDetail(db, "Mountain climbers", 2, "Cardio & Core",
                "Start in plank position, alternate driving knees toward your chest quickly while keeping your core tight."));
        this.exeDetails.add(addExerciseDetail(db, "Jumping jacks", 1, "Cardio",
                "Jump with your legs spreading outward and arms going overhead, then return to the starting position quickly."));
        this.exeDetails.add(addExerciseDetail(db, "Run in place", 1, "Cardio",
                "Run on the spot, lifting knees high and pumping your arms."));
        this.exeDetails.add(addExerciseDetail(db, "Knee up run", 1, "Cardio & Legs",
                "Run in place bringing knees up towards chest to increase heart rate."));
        this.exeDetails.add(addExerciseDetail(db, "Jump rope", 1, "Cardio & Legs",
                "Jump rope continuously, keeping a steady pace and engaging your calves and core."));
        this.exeDetails.add(addExerciseDetail(db, "Inchworms", 1, "Full Body",
                "Stand tall, hinge at the hips to touch the floor, walk your hands forward into a plank, then walk them back and stand up."));

        // ADDITIONAL CORE & ABS EXERCISES (New additions)
        this.exeDetails.add(addExerciseDetail(db, "Jackknife twists", 3, "Abs & Obliques",
                "From lying position, lift legs and torso, twisting to touch toes or sides."));
        this.exeDetails.add(addExerciseDetail(db, "3 way crunch pulses", 2, "Abs",
                "Pulse your torso in three directions to engage all areas of the abs."));
        this.exeDetails.add(addExerciseDetail(db, "Low abs twists", 2, "Lower Abs & Obliques",
                "Lie on back and twist lower body side to side while legs lifted."));
        this.exeDetails.add(addExerciseDetail(db, "Low abs bent knees", 2, "Lower Abs",
                "Raise bent knees toward chest while lying on back, lowering slowly."));
        this.exeDetails.add(addExerciseDetail(db, "Side plank leg circle", 2, "Obliques & Glutes",
                "Hold side plank and move top leg in controlled circles."));
        this.exeDetails.add(addExerciseDetail(db, "Seated leg raises", 2, "Lower Abs",
                "Sit with legs extended and lift them together while leaning back slightly."));
        this.exeDetails.add(addExerciseDetail(db, "Circle crunches", 2, "Abs",
                "Perform crunches moving your torso in a circular motion."));
        this.exeDetails.add(addExerciseDetail(db, "Plank buzzsaw", 3, "Core",
                "From plank, rotate hips side to side rapidly while keeping torso stable."));
        this.exeDetails.add(addExerciseDetail(db, "Jackknives", 3, "Abs",
                "Lift legs and torso simultaneously to meet in a jackknife position."));
        this.exeDetails.add(addExerciseDetail(db, "Full arm side plank leg up", 3, "Obliques & Glutes",
                "In side plank, lift top leg and extend top arm overhead."));
        this.exeDetails.add(addExerciseDetail(db, "Lateral abs", 2, "Obliques",
                "Perform side crunches or twists to target lateral abdominal muscles."));
        this.exeDetails.add(addExerciseDetail(db, "Scissor drops", 2, "Lower Abs",
                "Alternate lowering legs in a scissor motion without touching the floor."));
        this.exeDetails.add(addExerciseDetail(db, "Plank jacks", 3, "Core & Cardio",
                "Jump legs in and out while holding a plank position."));

        // ADDITIONAL EXERCISES (New additions - formatted and organized)
        
        // Additional Core & Abs
        this.exeDetails.add(addExerciseDetail(db, "High plank", 2, "Core & Arms",
                "Hold plank position with hands under shoulders and body straight."));
        this.exeDetails.add(addExerciseDetail(db, "Low plank", 2, "Core & Arms",
                "Hold a forearm plank keeping body straight and engaged."));
        this.exeDetails.add(addExerciseDetail(db, "Knee to elbow", 2, "Core & Obliques",
                "From plank, bring knee to opposite elbow alternately."));
        this.exeDetails.add(addExerciseDetail(db, "Leg raises", 2, "Lower Abs",
                "Lift legs straight up from the ground while lying on your back."));

        // Additional Legs
        this.exeDetails.add(addExerciseDetail(db, "Squat taps", 1, "Legs & Cardio",
                "Perform a squat and tap one foot to the side, alternating sides."));
        this.exeDetails.add(addExerciseDetail(db, "Squat run", 1, "Legs & Cardio",
                "Alternate between squats and short running steps in place."));
        this.exeDetails.add(addExerciseDetail(db, "Step in", 1, "Legs & Cardio",
                "Step one foot forward and back repeatedly as a light warmup."));

        // ADDITIONAL EXERCISES (More new additions)
        
        // Additional Cardio
        this.exeDetails.add(addExerciseDetail(db, "High knees", 3, "Legs & Cardio",
                "Run in place while lifting knees as high as possible, pumping arms."));
        
        // Additional Core & Chest
        this.exeDetails.add(addExerciseDetail(db, "Spiderman", 3, "Core & Chest",
                "In a pushup position, bring knee toward elbow alternating sides."));

    }
}