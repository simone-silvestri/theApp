package com.simone.cfts;

import java.util.ArrayList;

public class DefaultWorkouts {
    private ArrayList<Workout> wodList;

    public ArrayList<Workout> getWodList() {
        return wodList;
    }

    public DefaultWorkouts() {
        this.wodList = new ArrayList<>();
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

    public void addEntryLevel() {

        Exercise e1,e2,e3,e4,e5,e6,e7,e8,e9,e10;
        ArrayList<Exercise> eList = new ArrayList<>();

        ExerciseDetail exe = new ExerciseDetail();

        e1 = new Exercise("Shoulder taps",10,40,20);
        exe.setName(e1.getName()); exe.setDifficulty(2); exe.setMuscle("Chest");
        exe.setDescription("Set yourself in a full arm position. While keeping the body as still as possible touch your right shoulder with your left hand." +
                " Then touch your left shoulder with your right hand. Repeat");
        e2 = new Exercise("Bicycle crunches",10,40,20);
        exe.setName(e2.getName()); exe.setDifficulty(2); exe.setMuscle("Abs");
        exe.setDescription("Laying on your back do a cycling motion with your legs. While cycling try to touch your knee with the opposite elbow.");
        e3 = new Exercise("Burpees",10,40,20);
        exe.setName(e3.getName()); exe.setDifficulty(3); exe.setMuscle("Cardio");
        exe.setDescription("A pushup, followed by a jump! Easier variation: just stand up without jumping.");
        e4 = new Exercise("Crunches",10,40,20);
        exe.setName(e4.getName()); exe.setDifficulty(2); exe.setMuscle("Abs");
        exe.setDescription("Classical abdominal exercise.");
        e5 = new Exercise("Dips",10,40,20);
        exe.setName(e5.getName()); exe.setDifficulty(2); exe.setMuscle("Arms");
        exe.setDescription("Classical abdominal exercise.");
        e6 = new Exercise("Mountain climbers",10,40,20);
        exe.setName(e6.getName()); exe.setDifficulty(1); exe.setMuscle("Cardio");
        exe.setDescription("Classical abdominal exercise.");
        e7 = new Exercise("Hip thrusts",10,40,20);
        exe.setName(e7.getName()); exe.setDifficulty(1); exe.setMuscle("Glutes");
        exe.setDescription("Classical abdominal exercise.");
        e8 = new Exercise("Side plank left",10,40,20);
        exe.setName(e8.getName()); exe.setDifficulty(2); exe.setMuscle("Abs");
        exe.setDescription("Classical abdominal exercise.");
        e9 = new Exercise("Side plank right",10,40,20);
        exe.setName(e9.getName()); exe.setDifficulty(2); exe.setMuscle("Abs");
        exe.setDescription("Classical abdominal exercise.");

        eList.add(e1); eList.add(e2); eList.add(e3); eList.add(e4); eList.add(e5);
        eList.add(e6); eList.add(e7); eList.add(e8); eList.add(e9);

        Workout wd = new Workout();
        wd.setDifficulty(1); wd.setNumberOfSets(3); wd.setSetPause(180);
        wd.setExercises(eList); wd.setTotalTime(); wd.setType("TIME");
        wd.setWod("your favourite description"); wd.setTitle("Entry Level");

        this.wodList.add(wd);
    }

    public void addNoEffort() {

        Exercise e1,e2,e3,e4,e5,e6,e7,e8,e9,e10;
        ArrayList<Exercise> eList = new ArrayList<>();

        e1 = new Exercise("Lunges",10,40,20);
        e2 = new Exercise("Crunches",10,40,20);
        e3 = new Exercise("Squats",10,40,20);
        e4 = new Exercise("Lower abs",10,40,20);
        e5 = new Exercise("Hip thrusts",10,40,20);
        e6 = new Exercise("Inchworms",10,40,10);
        e7 = new Exercise("Hyperextensions",10,40,20);
        e8 = new Exercise("Plank rotation",10,40,20);
        e9 = new Exercise("Side squat",10,40,20);
        e10= new Exercise("Plank ups",10,40,20);

        eList.add(e1); eList.add(e2); eList.add(e3); eList.add(e4); eList.add(e5);
        eList.add(e6); eList.add(e7); eList.add(e8); eList.add(e9); eList.add(e10);

        Workout wd = new Workout();
        wd.setDifficulty(1); wd.setNumberOfSets(3); wd.setSetPause(180);
        wd.setExercises(eList); wd.setTotalTime(); wd.setType("TIME");
        wd.setWod("your favourite description"); wd.setTitle("No Effort");

        this.wodList.add(wd);
    }

    public void addFirstAttempt() {

        Exercise e1,e2,e3,e4,e5,e6,e7,e8,e9,e10;
        ArrayList<Exercise> eList = new ArrayList<>();

        e1 = new Exercise("Burpees (no pushup)",10,40,20);
        e2 = new Exercise("Crunches",10,40,20);
        e3 = new Exercise("Pushups",10,40,20);
        e4 = new Exercise("Lower abs",10,40,20);
        e5 = new Exercise("Hindu pushups",10,40,20);
        e6 = new Exercise("Scissors",10,40,10);
        e7 = new Exercise("Pike pushups",10,40,20);
        e8 = new Exercise("Lunges",10,40,20);
        e9 = new Exercise("Hyperextensions",10,40,20);
        e10= new Exercise("Plank",10,40,20);

        eList.add(e1); eList.add(e2); eList.add(e3); eList.add(e4); eList.add(e5);
        eList.add(e6); eList.add(e7); eList.add(e8); eList.add(e9); eList.add(e10);

        Workout wd = new Workout();
        wd.setDifficulty(3); wd.setNumberOfSets(4); wd.setSetPause(120);
        wd.setExercises(eList); wd.setTotalTime(); wd.setType("TIME");
        wd.setWod("your favourite description"); wd.setTitle("First attempt");

        this.wodList.add(wd);
    }

    public void addWholeBody() {

        Exercise e1,e2,e3,e4,e5,e6,e7,e8,e9,e10;
        ArrayList<Exercise> eList = new ArrayList<>();

        e1 = new Exercise("Jump squats",10,45,15);
        e2 = new Exercise("Crunches",10,45,15);
        e3 = new Exercise("Knee pushups",10,45,15);
        e4 = new Exercise("Plank",10,45,15);
        e5 = new Exercise("Mountain climbers",10,45,15);
        e6 = new Exercise("Lunges",10,45,15);
        e7 = new Exercise("Sit ups",10,45,15);
        e8 = new Exercise("Lower abs",10,45,15);
        e9 = new Exercise("Pike pushups",10,45,15);
        e10= new Exercise("Squats",10,45,14);

        eList.add(e1); eList.add(e2); eList.add(e3); eList.add(e4); eList.add(e5);
        eList.add(e6); eList.add(e7); eList.add(e8); eList.add(e9); eList.add(e10);

        Workout wd = new Workout();
        wd.setDifficulty(2); wd.setNumberOfSets(4); wd.setSetPause(90);
        wd.setExercises(eList); wd.setTotalTime(); wd.setType("TIME");
        wd.setWod("your favourite description"); wd.setTitle("Whole body");

        this.wodList.add(wd);
    }

    public void addMondayDetox() {

        Exercise e1,e2,e3,e4,e5,e6,e7,e8,e9,e10;
        ArrayList<Exercise> eList = new ArrayList<>();

        e1 = new Exercise("Burpees",10,50,10);
        e2 = new Exercise("Crunches",10,50,10);
        e3 = new Exercise("Pushups",10,50,10);
        e4 = new Exercise("Lower abs",10,50,10);
        e5 = new Exercise("Diamond pushups",10,50,10);
        e6 = new Exercise("Scissors",10,50,10);
        e7 = new Exercise("Pike pushups",10,50,10);
        e8 = new Exercise("Jump lunges",10,50,10);
        e9 = new Exercise("Side plank",10,50,10);
        e10= new Exercise("Hindu pushups",10,50,10);

        eList.add(e1); eList.add(e2); eList.add(e3); eList.add(e4); eList.add(e5);
        eList.add(e6); eList.add(e7); eList.add(e8); eList.add(e9); eList.add(e10);

        Workout wd = new Workout();
        wd.setDifficulty(4); wd.setNumberOfSets(4); wd.setSetPause(120);
        wd.setExercises(eList); wd.setTotalTime(); wd.setType("TIME");
        wd.setWod("your favourite description"); wd.setTitle("Monday Detox");

        this.wodList.add(wd);
    }

    public void addMondayDetoxAli() {

        Exercise e1,e2,e3,e4,e5,e6,e7,e8,e9,e10;
        ArrayList<Exercise> eList = new ArrayList<>();

        e1 = new Exercise("Burpees",10,50,10);
        e2 = new Exercise("Crunches",10,50,10);
        e3 = new Exercise("Pushups",10,50,10);
        e4 = new Exercise("Lower abs",10,50,10);
        e5 = new Exercise("Donkey kicks",10,50,10);
        e6 = new Exercise("Scissors",10,50,10);
        e7 = new Exercise("Lateral donkey",10,50,10);
        e8 = new Exercise("Lunges",10,50,10);
        e9 = new Exercise("Side plank",10,50,10);
        e10= new Exercise("Jump squats",10,50,10);

        eList.add(e1); eList.add(e2); eList.add(e3); eList.add(e4); eList.add(e5);
        eList.add(e6); eList.add(e7); eList.add(e8); eList.add(e9); eList.add(e10);

        Workout wd = new Workout();
        wd.setDifficulty(3); wd.setNumberOfSets(4); wd.setSetPause(120);
        wd.setExercises(eList); wd.setTotalTime(); wd.setType("TIME");
        wd.setWod("your favourite description"); wd.setTitle("Ali's Monday Detox");

        this.wodList.add(wd);
    }

    public void addCardio() {

        Exercise e1,e2,e3,e4,e5,e6,e7,e8,e9,e10,e11,e12;
        ArrayList<Exercise> eList = new ArrayList<>();

        e1 = new Exercise("Burpees",10,40,20);
        e2 = new Exercise("Seated tucks",10,40,20);
        e3 = new Exercise("Jumping sit outs",10,40,20);
        e4 = new Exercise("Butterfly squats (explosive)",10,40,20);
        e5 = new Exercise("Knee tap pushups",10,40,20);
        e6 = new Exercise("Ankle taps",10,40,20);
        e7 = new Exercise("Super skater jumps",10,40,20);
        e8 = new Exercise("Shoulder taps",10,40,20);
        e9 = new Exercise("Plank switches",10,40,20);
        e10= new Exercise("Tuck jumps",10,40,20);
        e11= new Exercise("Lower abs",10,40,20);
        e12= new Exercise("Jumping jacks",10,40,20);

        eList.add(e1); eList.add(e2); eList.add(e3); eList.add(e4); eList.add(e5);
        eList.add(e6); eList.add(e7); eList.add(e8); eList.add(e9); eList.add(e10);
        eList.add(e11); eList.add(e12);

        Workout wd = new Workout();
        wd.setDifficulty(2); wd.setNumberOfSets(4); wd.setSetPause(60);
        wd.setExercises(eList); wd.setTotalTime(); wd.setType("TIME");
        wd.setWod("your favourite description"); wd.setTitle("Cardio");

        this.wodList.add(wd);
    }

    public void addBalanced() {

        Exercise e1,e2,e3,e4,e5,e6,e7,e8,e9,e10,e11,e12;
        ArrayList<Exercise> eList = new ArrayList<>();

        e1 = new Exercise("Jumping jacks",40,50,0);
        e2 = new Exercise("Squats",10,50,0);
        e3 = new Exercise("Russian twists",20,50,0);
        e4 = new Exercise("High knees",45,50,0);
        e5 = new Exercise("Sit ups",15,50,0);
        e6 = new Exercise("Knee pushups",15,50,0);
        e7 = new Exercise("Lower abs",20,50,0);
        e8 = new Exercise("Jump lunges",20,50,0);
        e9 = new Exercise("Hip thrusts",15,50,0);
        e10= new Exercise("Burpees (no pushups)",10,50,0);
        e11= new Exercise("Scissors",40,50,0);
        e12= new Exercise("Jump squats",10,50,0);

        eList.add(e1); eList.add(e2); eList.add(e3); eList.add(e4); eList.add(e5);
        eList.add(e6); eList.add(e7); eList.add(e8); eList.add(e9); eList.add(e10);
        eList.add(e11); eList.add(e12);

        Workout wd = new Workout();
        wd.setDifficulty(1); wd.setNumberOfSets(4); wd.setSetPause(120);
        wd.setExercises(eList); wd.setTotalTime(); wd.setType("REPSINTIME");
        wd.setWod("your favourite description"); wd.setTitle("Balanced beginners");

        this.wodList.add(wd);
    }

    public void addPushups() {

        Exercise e1,e2,e3,e4,e5,e6,e7,e8,e9,e10,e11,e12;
        ArrayList<Exercise> eList = new ArrayList<>();

        e1 = new Exercise("Burpees",12,50,0);
        e2 = new Exercise("Staggered pushups",16,50,0);
        e3 = new Exercise("Pushup rotation",12,50,0);
        e4 = new Exercise("Hindu pushup",16,50,0);
        e5 = new Exercise("One leg pushups",12,50,0);
        e6 = new Exercise("Diamond pushups",16,50,0);
        e7 = new Exercise("Spiderman pushups",16,50,0);
        e8 = new Exercise("Decline pushups",12,50,0);
        e9 = new Exercise("Archer pushups",16,50,0);
        e10= new Exercise("One arm pushups",12,50,0);
        e11= new Exercise("Explosive pushups",10,50,0);

        eList.add(e1); eList.add(e2); eList.add(e3); eList.add(e4); eList.add(e5);
        eList.add(e6); eList.add(e7); eList.add(e8); eList.add(e9); eList.add(e10);
        eList.add(e11); eList.add(e1);

        Workout wd = new Workout();
        wd.setDifficulty(5); wd.setNumberOfSets(4); wd.setSetPause(120);
        wd.setExercises(eList); wd.setTotalTime(); wd.setType("REPSINTIME");
        wd.setWod("your favourite description"); wd.setTitle("Pushups!!");

        this.wodList.add(wd);
    }

    public void addLegDay() {

        Exercise e1,e2,e3,e4,e5,e6,e7,e8,e9,e10;
        ArrayList<Exercise> eList = new ArrayList<>();

        e1 = new Exercise("Burpees",10,50,10);
        e2 = new Exercise("Side plank leg up",10,50,10);
        e3 = new Exercise("Single leg deadlifts",10,50,10);
        e4 = new Exercise("Bicycle crunches",10,50,10);
        e5 = new Exercise("Bottom up lunges",10,50,10);
        e6 = new Exercise("Mountain climbers",10,50,10);
        e7 = new Exercise("Pushups",10,50,10);
        e8 = new Exercise("Single leg chair stands",10,50,10);
        e9 = new Exercise("One legged plank",10,50,10);
        e10= new Exercise("Jump squats",10,50,10);

        eList.add(e1); eList.add(e2); eList.add(e3); eList.add(e4); eList.add(e5);
        eList.add(e6); eList.add(e7); eList.add(e8); eList.add(e9); eList.add(e10);

        Workout wd = new Workout();
        wd.setDifficulty(3); wd.setNumberOfSets(4); wd.setSetPause(120);
        wd.setExercises(eList); wd.setTotalTime(); wd.setType("TIME");
        wd.setWod("your favourite description"); wd.setTitle("Leg day");

        this.wodList.add(wd);
    }

    public void addGreekTraining() {

        Exercise e1,e2,e3,e4,e5,e6,e7,e8,e9,e10;
        ArrayList<Exercise> eList = new ArrayList<>();

        e1 = new Exercise("Pushups",40,0,0);
        e2 = new Exercise("Jump squats",40,0,0);
        e3 = new Exercise("Burpees",40,0,0);
        e4 = new Exercise("Sit ups",40,0,0);
        e5 = new Exercise("Jump lunges",40,0,0);
        e6 = new Exercise("Plank",1,0,0);
        e7 = new Exercise("Pushups on fist",40,0,0);
        e8 = new Exercise("Burpees",40,0,0);
        e9 = new Exercise("Lower abs",40,0,0);

        eList.add(e1); eList.add(e2); eList.add(e3); eList.add(e4); eList.add(e5);
        eList.add(e6); eList.add(e7); eList.add(e8); eList.add(e9); eList.add(e1); eList.add(e6);

        Workout wd = new Workout();
        wd.setDifficulty(5); wd.setNumberOfSets(2); wd.setSetPause(0);
        wd.setExercises(eList); wd.setTotalTime(40*60); wd.setType("REPS");
        wd.setWod("your favourite description"); wd.setTitle("Greek training");

        this.wodList.add(wd);
    }

    public void addDoubleProg1() {

        Exercise e1,e2,e3,e4,e5,e6,e7,e8,e9,e10,e11,e12,e13,e14,e15,e16,e17,e18,e19,e20;
        ArrayList<Exercise> eList = new ArrayList<>();

        e1 = new Exercise("Side burpees left",10,50,10);
        e2 = new Exercise("Run in place",10,50,10);
        e3 = new Exercise("Side burpees right",10,50,10);
        e4 = new Exercise("Isometric lower abs",10,50,10);
        e5 = new Exercise("Australian pullups",10,50,10);
        e6 = new Exercise("Pushup rotation",10,50,10);
        e7 = new Exercise("One legged squat",10,50,10);
        e8 = new Exercise("Sphinx pushups",10,50,10);
        e9 = new Exercise("Lower abs",10,50,10);
        e10= new Exercise("Isometric pushups",10,50,120);

        e11= new Exercise("Burpes",10,50,10);
        e12= new Exercise("Australian pullups",10,50,10);
        e13= new Exercise("Russian twists",10,50,10);
        e14= new Exercise("Handstand pushups",10,50,10);
        e15= new Exercise("Plank ups",10,50,10);
        e16= new Exercise("Jump lunges",10,50,10);
        e17= new Exercise("Archer pushups",10,50,10);
        e18= new Exercise("Scissors",10,50,10);
        e19= new Exercise("Decline pushups",10,50,10);
        e20= new Exercise("Dips",10,50,120);

        eList.add(e1); eList.add(e2); eList.add(e3); eList.add(e4); eList.add(e5);
        eList.add(e6); eList.add(e7); eList.add(e8); eList.add(e9); eList.add(e10);

        eList.add(e11); eList.add(e12); eList.add(e13); eList.add(e14); eList.add(e15);
        eList.add(e16); eList.add(e17); eList.add(e18); eList.add(e19); eList.add(e20);

        Workout wd = new Workout();
        wd.setDifficulty(4); wd.setNumberOfSets(2); wd.setSetPause(120);
        wd.setExercises(eList); wd.setTotalTime(); wd.setType("TIME");
        wd.setWod("your favourite description"); wd.setTitle("Double program 1");

        this.wodList.add(wd);
    }

    public void addDoubleProg2() {

        Exercise e1,e2,e3,e4,e5,e6,e7,e8,e9,e10,e11,e12,e13,e14,e15,e16,e17,e18,e19,e20;
        ArrayList<Exercise> eList = new ArrayList<>();

        e1 = new Exercise("Side burpees left",10,50,10);
        e2 = new Exercise("Run in place",10,50,10);
        e3 = new Exercise("Side burpees right",10,50,10);
        e4 = new Exercise("Bicycle crunches",10,50,10);
        e5 = new Exercise("Hindu pushups",10,50,10);
        e6 = new Exercise("Shoulder taps",10,50,10);
        e7 = new Exercise("Windshield wipers",10,50,10);
        e8 = new Exercise("Jump squats",10,50,10);
        e9 = new Exercise("Pushups",10,50,10);
        e10= new Exercise("Isometric lower abs",10,50,120);

        e11= new Exercise("Burpes",10,50,10);
        e12= new Exercise("Butterfly crunches",10,50,10);
        e13= new Exercise("Jump crunches",10,50,10);
        e14= new Exercise("Half burpees",10,50,10);
        e15= new Exercise("Lower abs",10,50,10);
        e16= new Exercise("Pushups toe touch",10,50,10);
        e17= new Exercise("Punch sit ups",10,50,10);
        e18= new Exercise("Power tucks",10,50,10);
        e19= new Exercise("Pike pushups",10,50,10);
        e20= new Exercise("Mountain climbers",10,50,120);

        eList.add(e1); eList.add(e2); eList.add(e3); eList.add(e4); eList.add(e5);
        eList.add(e6); eList.add(e7); eList.add(e8); eList.add(e9); eList.add(e10);

        eList.add(e11); eList.add(e12); eList.add(e13); eList.add(e14); eList.add(e15);
        eList.add(e16); eList.add(e17); eList.add(e18); eList.add(e19); eList.add(e20);

        Workout wd = new Workout();
        wd.setDifficulty(4); wd.setNumberOfSets(2); wd.setSetPause(120);
        wd.setExercises(eList); wd.setTotalTime(); wd.setType("TIME");
        wd.setWod("your favourite description"); wd.setTitle("Double program 2");

        this.wodList.add(wd);
    }

    public void addPlankPushups() {

        Exercise e1,e2,e3,e4,e5,e6,e7,e8,e9,e10,e11,e12,e13,e14,e15,e16,e17,e18,e19,e20;
        ArrayList<Exercise> eList = new ArrayList<>();

        e1 = new Exercise("Arm leg plank",10,20,10);
        e2 = new Exercise("Knee pushups",10,20,10);
        e3 = new Exercise("Pushups full rotation",10,20,10);
        e4 = new Exercise("Plank ups",10,20,10);
        e5 = new Exercise("Tiger band pushups",10,20,10);
        e6 = new Exercise("Dips toe touch",10,20,10);
        e7 = new Exercise("Side kicks",10,20,10);
        e8 = new Exercise("Sphinx pushups",10,20,10);
        e9 = new Exercise("Clap pushups",10,20,10);
        e10= new Exercise("Dips",10,20,10);

        e11= new Exercise("plank leg lifts",10,20,10);
        e12= new Exercise("Plank stars",10,20,10);
        e13= new Exercise("Shoulder taps",10,20,10);
        e14= new Exercise("Diamond pushups",10,20,10);
        e15= new Exercise("Pushups rotation",10,20,10);
        e16= new Exercise("Pushups toe touch",10,20,10);
        e17= new Exercise("One leg pushups",10,20,10);
        e18= new Exercise("One leg dips",10,20,10);
        e19= new Exercise("Plank",10,20,10);
        e20= new Exercise("Pushups half squat",10,20,10);

        eList.add(e1); eList.add(e2); eList.add(e3); eList.add(e4); eList.add(e5);
        eList.add(e6); eList.add(e7); eList.add(e8); eList.add(e9); eList.add(e10);

        eList.add(e11); eList.add(e12); eList.add(e13); eList.add(e14); eList.add(e15);
        eList.add(e16); eList.add(e17); eList.add(e18); eList.add(e19); eList.add(e20);

        Workout wd = new Workout();
        wd.setDifficulty(3); wd.setNumberOfSets(4); wd.setSetPause(120);
        wd.setExercises(eList); wd.setTotalTime(); wd.setType("TIME");
        wd.setWod("your favourite description"); wd.setTitle("Abs-Chest HIIT");

        this.wodList.add(wd);
    }

    public void addAbsChillax() {

        Exercise e1,e2,e3,e4,e5,e6,e7,e8,e9,e10,e11,e12,e13,e14,e15,e16,e17,e18,e19,e20;
        ArrayList<Exercise> eList = new ArrayList<>();

        e1 = new Exercise("Plank",10,20,10);
        e2 = new Exercise("Russian twists",10,20,10);
        e3 = new Exercise("Single leg crunches",10,20,10);
        e4 = new Exercise("Plank kicks",10,20,10);
        e5 = new Exercise("Jackknife twists",10,20,10);
        e6 = new Exercise("3 way cruch pulses",10,20,10);
        e7 = new Exercise("Side plank",10,20,10);
        e8 = new Exercise("Low abs twists",10,20,10);
        e9 = new Exercise("Low abs bent knees",10,20,10);
        e10= new Exercise("Side plank leg circle",10,20,10);

        e11= new Exercise("Seated leg raises",10,20,10);
        e12= new Exercise("Circle crunches",10,20,10);
        e13= new Exercise("Plank buzzsaw",10,20,10);
        e14= new Exercise("Russian twists",10,20,10);
        e15= new Exercise("Jackknives",10,20,10);
        e16= new Exercise("Full arm side plank leg up",10,20,10);
        e17= new Exercise("Lateral abs",10,20,10);
        e18= new Exercise("Scissor drops",10,20,10);
        e19= new Exercise("Plank jacks",10,20,10);
        e20= new Exercise("Butterfly crunches",10,20,10);

        eList.add(e1); eList.add(e2); eList.add(e3); eList.add(e4); eList.add(e5);
        eList.add(e6); eList.add(e7); eList.add(e8); eList.add(e9); eList.add(e10);

        eList.add(e11); eList.add(e12); eList.add(e13); eList.add(e14); eList.add(e15);
        eList.add(e16); eList.add(e17); eList.add(e18); eList.add(e19); eList.add(e20);

        Workout wd = new Workout();
        wd.setDifficulty(2); wd.setNumberOfSets(4); wd.setSetPause(120);
        wd.setExercises(eList); wd.setTotalTime(); wd.setType("TIME");
        wd.setWod("your favourite description"); wd.setTitle("Abs Chillax");

        this.wodList.add(wd);
    }

    public void addJordanYeoh() {

        Exercise e1,e2,e3,e4,e5,e6,e1b,e2b,e3b,e4b,e5b,e6b,e6b2;
        ArrayList<Exercise> eList = new ArrayList<>();

        e1 = new Exercise("Pushups toe touch",10,30,2);
        e2 = new Exercise("Running jacks",10,30,2);
        e3 = new Exercise("Mountain climbers",10,30,2);
        e4 = new Exercise("Butt kicks",10,30,2);
        e5 = new Exercise("Burpees",10,30,2);
        e6 = new Exercise("Jump Lunges",10,30,2);

        e1b= new Exercise("Pushups toe touch",10,30,30);
        e2b= new Exercise("Running jacks",10,30,30);
        e3b= new Exercise("Mountain climbers",10,30,30);
        e4b= new Exercise("Butt kicks",10,30,30);
        e5b= new Exercise("Burpees",10,30,30);
        e6b= new Exercise("Jump Lunges",10,30,30);
        e6b2= new Exercise("Jump Lunges",10,30,120);

        eList.add(e1b);
        eList.add(e1); eList.add(e2b);
        eList.add(e1); eList.add(e2); eList.add(e3b);
        eList.add(e1); eList.add(e2); eList.add(e3); eList.add(e4b);
        eList.add(e1); eList.add(e2); eList.add(e3); eList.add(e4); eList.add(e5b);
        eList.add(e1); eList.add(e2); eList.add(e3); eList.add(e4); eList.add(e5); eList.add(e6b2);

        eList.add(e6b);
        eList.add(e6); eList.add(e5b);
        eList.add(e6); eList.add(e5); eList.add(e4b);
        eList.add(e6); eList.add(e5); eList.add(e4); eList.add(e3b);
        eList.add(e6); eList.add(e5); eList.add(e4); eList.add(e3); eList.add(e2b);
        eList.add(e6); eList.add(e5); eList.add(e4); eList.add(e3); eList.add(e2); eList.add(e1b);

        Workout wd = new Workout();
        wd.setDifficulty(5); wd.setNumberOfSets(1); wd.setSetPause(0);
        wd.setExercises(eList); wd.setTotalTime(); wd.setType("TIME");
        wd.setWod("your favourite description"); wd.setTitle("Jordan Yeoh HIIT");

        this.wodList.add(wd);
    }

    public void addMorningPushups() {

        Exercise e1,e2,e3,e4,e5,e6,e7,e8,e9,e10,e11,e12;
        ArrayList<Exercise> eList = new ArrayList<>();

        e1 = new Exercise("Burpees",15,60,0);
        e2 = new Exercise("Diamond pushups",16,60,0);
        e3 = new Exercise("Spiderman pushups",16,60,0);
        e4 = new Exercise("One leg pushups",15,60,0);
        e5 = new Exercise("Staggered pushups",16,60,0);
        e6 = new Exercise("Archer pushups",16,60,0);
        e7 = new Exercise("Hindu pushup",16,60,0);

        eList.add(e1); eList.add(e2); eList.add(e3); eList.add(e4); eList.add(e5);
        eList.add(e6); eList.add(e7);

        Workout wd = new Workout();
        wd.setDifficulty(4); wd.setNumberOfSets(3); wd.setSetPause(120);
        wd.setExercises(eList); wd.setTotalTime(); wd.setType("REPSINTIME");
        wd.setWod("your favourite description"); wd.setTitle("Morning Pushups");

        this.wodList.add(wd);
    }

    public void addWarmupPreTabata() {

        Exercise e1,e2,e3,e4,e5,e6,e7,e8,e9,e10,e11,e12,e13,e14,e15,e16,e17,e18,e19,e20;
        ArrayList<Exercise> eList = new ArrayList<>();

        e1 = new Exercise("Squats",10,20,10);
        e2 = new Exercise("Pushups",10,20,10);
        e3 = new Exercise("Crunches",10,20,10);
        e4 = new Exercise("Squat taps",10,20,10);
        e5 = new Exercise("Jump jacks",10,20,10);
        e6 = new Exercise("Pushups",10,20,10);
        e7 = new Exercise("Hips thrusts",10,20,10);
        e8 = new Exercise("Leg raises",10,20,10);
        e9 = new Exercise("High plank",10,20,10);
        e10= new Exercise("Run in place",10,20,120);

        e11= new Exercise("Squat run",10,20,10);
        e12= new Exercise("Pushups",10,20,10);
        e13= new Exercise("Butterfly crunches",10,20,10);
        e14= new Exercise("Lunges",10,20,10);
        e15= new Exercise("Jump jacks",10,20,10);
        e16= new Exercise("Pushups",10,20,10);
        e17= new Exercise("Knee to elbow",10,20,10);
        e18= new Exercise("Step in",10,20,10);
        e19= new Exercise("Knee up run",10,20,10);
        e20= new Exercise("Low plank",10,20,10);

        eList.add(e1); eList.add(e2); eList.add(e3); eList.add(e4); eList.add(e5);
        eList.add(e6); eList.add(e7); eList.add(e8); eList.add(e9); eList.add(e10);

        eList.add(e11); eList.add(e12); eList.add(e13); eList.add(e14); eList.add(e15);
        eList.add(e16); eList.add(e17); eList.add(e18); eList.add(e19); eList.add(e20);

        Workout wd = new Workout();
        wd.setDifficulty(1); wd.setNumberOfSets(1); wd.setSetPause(0);
        wd.setExercises(eList); wd.setTotalTime(); wd.setType("TIME");
        wd.setWod("your favourite description"); wd.setTitle("Warmup pre Tabata");

        this.wodList.add(wd);
    }

    public void addPureTabata() {

        Exercise e1,e2;
        ArrayList<Exercise> eList = new ArrayList<>();

        e1 = new Exercise("Burpees",10,20,10);
        e2 = new Exercise("Jump rope",10,20,10);

        eList.add(e1); eList.add(e2); eList.add(e1); eList.add(e2);
        eList.add(e1); eList.add(e2); eList.add(e1); eList.add(e2);

        Workout wd = new Workout();
        wd.setDifficulty(3); wd.setNumberOfSets(2); wd.setSetPause(30);
        wd.setExercises(eList); wd.setTotalTime(); wd.setType("TIME");
        wd.setWod("your favourite description"); wd.setTitle("Pure Tabata");

        this.wodList.add(wd);
    }

    public void addFrasFridayClassic() {

        Exercise e1,e2,e3,e4,e5,e6,e7;
        ArrayList<Exercise> eList = new ArrayList<>();

        e1 = new Exercise("Jump Squats",10,40,0);
        e2 = new Exercise("Shoulder taps",20,40,0);
        e3 = new Exercise("Lower abs",20,50,0);
        e4 = new Exercise("Knee up run",35,40,0);
        e5 = new Exercise("Sit ups",20,50,0);
        e6 = new Exercise("Knee pushups",20,50,0);
        e7 = new Exercise("Plank",1,50,0);

        eList.add(e1); eList.add(e2); eList.add(e3); eList.add(e4); eList.add(e5);
        eList.add(e6); eList.add(e7);

        Workout wd = new Workout();
        wd.setDifficulty(3); wd.setNumberOfSets(7); wd.setSetPause(60);
        wd.setExercises(eList); wd.setTotalTime(); wd.setType("REPSINTIME");
        wd.setWod("your favourite description"); wd.setTitle("Fra's Friday Classic");

        this.wodList.add(wd);
    }

    public void addFridayClassic() {

        Exercise e1,e2,e3,e4,e5,e6,e7;
        ArrayList<Exercise> eList = new ArrayList<>();

        e1 = new Exercise("Jump Squats",15,40,0);
        e2 = new Exercise("Decline pushups",20,40,0);
        e3 = new Exercise("Lower abs",20,50,0);
        e4 = new Exercise("Dips",20,40,0);
        e5 = new Exercise("Sit ups",20,50,0);
        e6 = new Exercise("Pushups",25,50,0);
        e7 = new Exercise("Advanced plank",1,50,0);

        eList.add(e1); eList.add(e2); eList.add(e3); eList.add(e4); eList.add(e5);
        eList.add(e6); eList.add(e7);

        Workout wd = new Workout();
        wd.setDifficulty(4); wd.setNumberOfSets(7); wd.setSetPause(60);
        wd.setExercises(eList); wd.setTotalTime(); wd.setType("REPSINTIME");
        wd.setWod("your favourite description"); wd.setTitle("Friday Classic");

        this.wodList.add(wd);
    }

    public void addWholeBodyIntermediate() {

        Exercise e1,e2,e3,e4,e5,e6,e7;
        ArrayList<Exercise> eList = new ArrayList<>();

        e1 = new Exercise("Diamond pushups",15,50,0);
        e2 = new Exercise("Jump lunges",28,50,0);
        e3 = new Exercise("Pushups",20,50,0);
        e4 = new Exercise("Lower abs",20,50,0);
        e5 = new Exercise("Burpees",15,50,0);

        eList.add(e1); eList.add(e2); eList.add(e3); eList.add(e4); eList.add(e5);

        Workout wd = new Workout();
        wd.setDifficulty(3); wd.setNumberOfSets(6); wd.setSetPause(60);
        wd.setExercises(eList); wd.setTotalTime(); wd.setType("REPSINTIME");
        wd.setWod("your favourite description"); wd.setTitle("Whole body intense");

        this.wodList.add(wd);
    }

    public void addBurpeesMattanza() {

        Exercise e1;
        ArrayList<Exercise> eList = new ArrayList<>();

        e1 = new Exercise("Burpees",1,4,0);

        for(int i=0;i<100;i++) {
            eList.add(e1); }

        Workout wd = new Workout();
        wd.setDifficulty(5); wd.setNumberOfSets(4); wd.setSetPause(240);
        wd.setExercises(eList); wd.setTotalTime(); wd.setType("REPSINTIME");
        wd.setWod("Only 400 burpees, enjoy! :)"); wd.setTitle("Burpees Mattanza");

        this.wodList.add(wd);
    }

    public void addPureHIIT() {

        Exercise e1,e2,e3,e4,e5,e6;
        ArrayList<Exercise> eList = new ArrayList<>();

        e1 = new Exercise("High knees",10,20,2);
        e2 = new Exercise("Mountain climbers",10,20,2);
        e3 = new Exercise("Spiderman",10,20,2);
        e4 = new Exercise("Pushups",10,20,2);
        e5 = new Exercise("Jump squats",10,20,2);
        e6 = new Exercise("Jump jacks",10,20,2);


        eList.add(e1); eList.add(e2); eList.add(e1);
        eList.add(e3); eList.add(e4); eList.add(e3);
        eList.add(e5); eList.add(e6); eList.add(e5);

        Workout wd = new Workout();
        wd.setDifficulty(4); wd.setNumberOfSets(7); wd.setSetPause(60);
        wd.setExercises(eList); wd.setTotalTime(); wd.setType("TIME");
        wd.setWod("your favourite description"); wd.setTitle("Pure HIIT");

        this.wodList.add(wd);
    }
}