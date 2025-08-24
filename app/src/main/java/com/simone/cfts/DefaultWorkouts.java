package com.simone.cfts;

import android.view.View;

import java.util.ArrayList;

public class DefaultWorkouts {
    private ArrayList<Workout> wodList;

    public ArrayList<Workout> getWodList() {
        return this.wodList;
    }

    public DefaultWorkouts(View view) {
        this.wodList = new ArrayList<>();
        addEntryLevel(view);
        addNoEffort(view);
        addFirstAttempt(view);
        addWholeBody(view);
        addMondayDetox(view);
        addMondayDetoxAli(view);
        addCardio(view);
        addPushups(view);
        addLegDay(view);
        addGreekTraining(view);
        addDoubleProg1(view);
        addDoubleProg2(view);
        addPlankPushups(view);
        addAbsChillax(view);
        addJordanYeoh(view);
        addMorningPushups(view);
        addWarmupPreTabata(view);
        addPureTabata(view);
        addFridayClassic(view);
        addFrasFridayClassic(view);
        addWholeBodyIntermediate(view);
        addBurpeesMattanza(view);
        addBalanced(view);
        addPureHIIT(view);
    }

    // Helper method
    private Exercise createExercise(DatabaseHelper db, String name, int reps, int workTime, int restTime,
                                    int difficulty, String muscle, String description) {
        Exercise e = new Exercise(name, reps, workTime, restTime);
        ExerciseDetail exe = new ExerciseDetail();
        exe.setName(name);
        exe.setDifficulty(difficulty);
        exe.setMuscle(muscle);
        exe.setDescription(description);
        db.addOrUpdateExercise(exe);
        return e;
    }

    public void addEntryLevel(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());

        ArrayList<Exercise> eList = new ArrayList<>();

        // Add exercises to DB and eList
        eList.add(createExercise(dbhandler, "Shoulder taps", 10, 40, 20, 2, "Chest",
                "Start in a plank position, then alternate tapping each shoulder with the opposite hand while keeping your core stable."));
        eList.add(createExercise(dbhandler, "Bicycle crunches", 10, 40, 20, 2, "Abs",
                "Lie on your back, bring opposite elbow and knee together while extending the other leg in a pedaling motion."));
        eList.add(createExercise(dbhandler, "Burpees", 10, 40, 20, 3, "Cardio",
                "From standing, squat down, kick your feet back into a plank, do a push-up, return to squat, and jump explosively upward."));
        eList.add(createExercise(dbhandler, "Crunches", 10, 40, 20, 2, "Abs",
                "Lie on your back, bend knees, lift your upper body toward your knees using your abs, then return slowly."));
        eList.add(createExercise(dbhandler, "Dips", 10, 40, 20, 2, "Arms",
                "Place hands on a bench behind you, extend legs, and lower your body by bending elbows, then push back up."));
        eList.add(createExercise(dbhandler, "Mountain climbers", 10, 40, 20, 1, "Cardio",
                "Start in plank position, alternate driving knees toward your chest as fast as possible while keeping your core tight."));
        eList.add(createExercise(dbhandler, "Hip thrusts", 10, 40, 20, 1, "Glutes",
                "Sit on the ground with your back against a bench, feet flat, and lift hips upward by squeezing your glutes."));
        eList.add(createExercise(dbhandler, "Side plank left", 10, 40, 20, 2, "Abs",
                "Lie on your left side, prop up on your forearm, lift hips to form a straight line, hold position."));
        eList.add(createExercise(dbhandler, "Side plank right", 10, 40, 20, 2, "Abs",
                "Lie on your right side, prop up on your forearm, lift hips to form a straight line, hold position."));

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

    public void addNoEffort(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());

        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(createExercise(dbhandler, "Lunges", 10, 40, 20, 1, "Legs",
                "Step forward with one leg, lower your hips until both knees are bent at about 90 degrees, then push back to start."));
        eList.add(createExercise(dbhandler, "Crunches", 10, 40, 20, 1, "Abs",
                "Lie on your back, bend knees, lift your upper body toward your knees using your abs, then return slowly."));
        eList.add(createExercise(dbhandler, "Squats", 10, 40, 20, 1, "Legs",
                "Stand with feet shoulder-width apart, lower hips back and down as if sitting in a chair, then return to standing."));
        eList.add(createExercise(dbhandler, "Lower abs", 10, 40, 20, 1, "Abs",
                "Lie flat on your back, lift your legs toward the ceiling, then slowly lower them without touching the ground."));
        eList.add(createExercise(dbhandler, "Hip thrusts", 10, 40, 20, 1, "Glutes",
                "Sit on the ground with your back against a bench, feet flat, and lift hips upward by squeezing your glutes."));
        eList.add(createExercise(dbhandler, "Inchworms", 10, 40, 10, 1, "Full Body",
                "Stand tall, hinge at the hips to touch the floor, walk your hands forward into a plank, then walk them back and stand up."));
        eList.add(createExercise(dbhandler, "Hyperextensions", 10, 40, 20, 1, "Lower Back",
                "Lie face down, lift your chest and legs off the ground by contracting your lower back, then slowly return."));
        eList.add(createExercise(dbhandler, "Plank rotation", 10, 40, 20, 1, "Core",
                "Start in a plank position, rotate your torso and extend one arm toward the ceiling, then return and repeat on the other side."));
        eList.add(createExercise(dbhandler, "Side squat", 10, 40, 20, 1, "Legs",
                "Take a wide stance, shift your weight to one side as you bend the knee and keep the other leg straight, then switch sides."));
        eList.add(createExercise(dbhandler, "Plank ups", 10, 40, 20, 1, "Arms & Core",
                "Start in a forearm plank, push up into a high plank one arm at a time, then return to forearms, alternating sides."));

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

    public void addFirstAttempt(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());

        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(createExercise(dbhandler, "Burpees (no pushup)", 10, 40, 20, 2, "Cardio",
                "From standing, squat down, kick your feet back into a plank, quickly return to squat, and jump explosively upward."));
        eList.add(createExercise(dbhandler, "Crunches", 10, 40, 20, 1, "Abs",
                "Lie on your back, bend knees, lift your upper body toward your knees using your abs, then return slowly."));
        eList.add(createExercise(dbhandler, "Pushups", 10, 40, 20, 2, "Chest",
                "Start in a high plank, lower your body until your chest nearly touches the ground, then push back up."));
        eList.add(createExercise(dbhandler, "Lower abs", 10, 40, 20, 1, "Abs",
                "Lie flat on your back, lift your legs toward the ceiling, then slowly lower them without touching the ground."));
        eList.add(createExercise(dbhandler, "Hindu pushups", 10, 40, 20, 3, "Shoulders & Chest",
                "Start in a downward dog position, swoop your chest low and forward while bending elbows, then return to the starting position."));
        eList.add(createExercise(dbhandler, "Scissors", 10, 40, 10, 1, "Abs",
                "Lie on your back, lift your legs slightly off the ground, and alternate crossing them over each other in a scissor motion."));
        eList.add(createExercise(dbhandler, "Pike pushups", 10, 40, 20, 3, "Shoulders",
                "Start in a pike position with hips up, bend elbows to lower your head toward the ground, then push back up."));
        eList.add(createExercise(dbhandler, "Lunges", 10, 40, 20, 1, "Legs",
                "Step forward with one leg, lower your hips until both knees are bent at about 90 degrees, then push back to start."));
        eList.add(createExercise(dbhandler, "Hyperextensions", 10, 40, 20, 1, "Lower Back",
                "Lie face down, lift your chest and legs off the ground by contracting your lower back, then slowly return."));
        eList.add(createExercise(dbhandler, "Plank", 10, 40, 20, 1, "Core",
                "Hold a forearm plank position with your body in a straight line, engaging your core and glutes."));

        Workout wd = new Workout();
        wd.setDifficulty(3);
        wd.setNumberOfSets(4);
        wd.setSetPause(120);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("TIME");
        wd.setWod("A challenging full-body workout to test your endurance and strength with a mix of cardio, core, and push variations.");
        wd.setTitle("First Attempt");

        this.wodList.add(wd);
    }

    public void addWholeBody(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());

        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(createExercise(dbhandler, "Jump squats", 10, 45, 15, 2, "Legs",
                "Perform a regular squat, then explode upward into a jump, land softly, and immediately go into the next squat."));
        eList.add(createExercise(dbhandler, "Crunches", 10, 45, 15, 1, "Abs",
                "Lie on your back, bend knees, lift your upper body toward your knees using your abs, then return slowly."));
        eList.add(createExercise(dbhandler, "Knee pushups", 10, 45, 15, 1, "Chest",
                "Start in a pushup position on your knees, lower your chest toward the floor, then push back up."));
        eList.add(createExercise(dbhandler, "Plank", 10, 45, 15, 1, "Core",
                "Hold a forearm plank position with your body in a straight line, engaging your core and glutes."));
        eList.add(createExercise(dbhandler, "Mountain climbers", 10, 45, 15, 2, "Cardio",
                "Start in plank position, alternate driving knees toward your chest as fast as possible while keeping your core tight."));
        eList.add(createExercise(dbhandler, "Lunges", 10, 45, 15, 1, "Legs",
                "Step forward with one leg, lower your hips until both knees are bent at about 90 degrees, then push back to start."));
        eList.add(createExercise(dbhandler, "Sit ups", 10, 45, 15, 2, "Abs",
                "Lie on your back, bend knees, and lift your upper body all the way up to a sitting position, then lower slowly."));
        eList.add(createExercise(dbhandler, "Lower abs", 10, 45, 15, 1, "Abs",
                "Lie flat on your back, lift your legs toward the ceiling, then slowly lower them without touching the ground."));
        eList.add(createExercise(dbhandler, "Pike pushups", 10, 45, 15, 3, "Shoulders",
                "Start in a pike position with hips up, bend elbows to lower your head toward the ground, then push back up."));
        eList.add(createExercise(dbhandler, "Squats", 10, 45, 14, 1, "Legs",
                "Stand with feet shoulder-width apart, lower hips back and down as if sitting in a chair, then return to standing."));

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

    public void addMondayDetox(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());

        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(createExercise(dbhandler, "Burpees", 10, 50, 10, 3, "Cardio",
                "From standing, squat down, kick your feet back into a plank, do a push-up, return to squat, and jump explosively upward."));
        eList.add(createExercise(dbhandler, "Crunches", 10, 50, 10, 1, "Abs",
                "Lie on your back, bend knees, lift your upper body toward your knees using your abs, then return slowly."));
        eList.add(createExercise(dbhandler, "Pushups", 10, 50, 10, 2, "Chest",
                "Start in a high plank, lower your body until your chest nearly touches the ground, then push back up."));
        eList.add(createExercise(dbhandler, "Lower abs", 10, 50, 10, 1, "Abs",
                "Lie flat on your back, lift your legs toward the ceiling, then slowly lower them without touching the ground."));
        eList.add(createExercise(dbhandler, "Diamond pushups", 10, 50, 10, 3, "Triceps",
                "Start in a push-up position, place your hands close together under your chest forming a diamond shape, lower and push up."));
        eList.add(createExercise(dbhandler, "Scissors", 10, 50, 10, 1, "Abs",
                "Lie on your back, lift your legs slightly off the ground, and alternate crossing them over each other in a scissor motion."));
        eList.add(createExercise(dbhandler, "Pike pushups", 10, 50, 10, 3, "Shoulders",
                "Start in a pike position with hips up, bend elbows to lower your head toward the ground, then push back up."));
        eList.add(createExercise(dbhandler, "Jump lunges", 10, 50, 10, 3, "Legs",
                "Start in a lunge position, jump explosively and switch legs in the air, landing softly in the opposite lunge."));
        eList.add(createExercise(dbhandler, "Side plank", 10, 50, 10, 2, "Core",
                "Lie on your side, prop up on one forearm, lift hips to form a straight line from head to feet, and hold."));
        eList.add(createExercise(dbhandler, "Hindu pushups", 10, 50, 10, 3, "Shoulders & Chest",
                "Start in a downward dog position, swoop your chest low and forward while bending elbows, then return to the starting position."));

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

    public void addMondayDetoxAli(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());

        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(createExercise(dbhandler, "Burpees", 10, 50, 10, 3, "Cardio",
                "From standing, squat down, kick your feet back into a plank, do a push-up, return to squat, and jump explosively upward."));
        eList.add(createExercise(dbhandler, "Crunches", 10, 50, 10, 1, "Abs",
                "Lie on your back, bend knees, lift your upper body toward your knees using your abs, then return slowly."));
        eList.add(createExercise(dbhandler, "Pushups", 10, 50, 10, 2, "Chest",
                "Start in a high plank, lower your body until your chest nearly touches the ground, then push back up."));
        eList.add(createExercise(dbhandler, "Lower abs", 10, 50, 10, 1, "Abs",
                "Lie flat on your back, lift your legs toward the ceiling, then slowly lower them without touching the ground."));
        eList.add(createExercise(dbhandler, "Donkey kicks", 10, 50, 10, 1, "Glutes",
                "Start on all fours, lift one leg upward while keeping your knee bent at 90 degrees, then lower and repeat."));
        eList.add(createExercise(dbhandler, "Scissors", 10, 50, 10, 1, "Abs",
                "Lie on your back, lift your legs slightly off the ground, and alternate crossing them over each other in a scissor motion."));
        eList.add(createExercise(dbhandler, "Lateral donkey", 10, 50, 10, 2, "Glutes",
                "Start on all fours, lift one leg to the side while keeping your knee bent, then lower back down and repeat."));
        eList.add(createExercise(dbhandler, "Lunges", 10, 50, 10, 1, "Legs",
                "Step forward with one leg, lower your hips until both knees are bent at about 90 degrees, then push back to start."));
        eList.add(createExercise(dbhandler, "Side plank", 10, 50, 10, 2, "Core",
                "Lie on your side, prop up on one forearm, lift hips to form a straight line from head to feet, and hold."));
        eList.add(createExercise(dbhandler, "Jump squats", 10, 50, 10, 2, "Legs",
                "Perform a regular squat, then explode upward into a jump, land softly, and immediately go into the next squat."));

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

    public void addCardio(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());

        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(createExercise(dbhandler, "Burpees", 10, 40, 20, 3, "Cardio",
                "From standing, squat down, kick your feet back into a plank, do a push-up, return to squat, and jump explosively upward."));
        eList.add(createExercise(dbhandler, "Seated tucks", 10, 40, 20, 1, "Abs",
                "Sit on the floor, lean back slightly, pull your knees toward your chest, then extend your legs outward without touching the floor."));
        eList.add(createExercise(dbhandler, "Jumping sit outs", 10, 40, 20, 2, "Core & Shoulders",
                "Start in a bear crawl position, lift one hand and opposite leg, rotate your body and kick the leg through, then return and repeat."));
        eList.add(createExercise(dbhandler, "Butterfly squats (explosive)", 10, 40, 20, 2, "Legs",
                "Start in a squat position, jump explosively and tap your feet together mid-air, then land softly back into the squat."));
        eList.add(createExercise(dbhandler, "Knee tap pushups", 10, 40, 20, 2, "Chest & Core",
                "Perform a push-up, then tap your knee with the opposite hand before starting the next rep."));
        eList.add(createExercise(dbhandler, "Ankle taps", 10, 40, 20, 1, "Abs",
                "Lie on your back with knees bent, lift your shoulders slightly, and tap each ankle by bending sideways."));
        eList.add(createExercise(dbhandler, "Super skater jumps", 10, 40, 20, 2, "Legs & Cardio",
                "Jump laterally from side to side, landing on one leg with the other leg sweeping behind for balance."));
        eList.add(createExercise(dbhandler, "Shoulder taps", 10, 40, 20, 2, "Core",
                "Start in a plank position, then alternate tapping each shoulder with the opposite hand while keeping your core stable."));
        eList.add(createExercise(dbhandler, "Plank switches", 10, 40, 20, 2, "Core",
                "Alternate between high plank (hands) and low plank (forearms) positions by moving one arm at a time."));
        eList.add(createExercise(dbhandler, "Tuck jumps", 10, 40, 20, 3, "Legs & Cardio",
                "Jump explosively while pulling your knees toward your chest, land softly, and immediately go into the next jump."));
        eList.add(createExercise(dbhandler, "Lower abs", 10, 40, 20, 1, "Abs",
                "Lie flat on your back, lift your legs toward the ceiling, then slowly lower them without touching the ground."));
        eList.add(createExercise(dbhandler, "Jumping jacks", 10, 40, 20, 1, "Cardio",
                "Jump with your legs spreading outward and arms going overhead, then return to the starting position quickly."));

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

    public void addBalanced(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());

        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(createExercise(dbhandler, "Jumping jacks", 40, 50, 0, 1, "Cardio",
                "Jump with legs and arms spreading outward and then return to starting position. Maintain a quick rhythm."));
        eList.add(createExercise(dbhandler, "Squats", 10, 50, 0, 1, "Legs",
                "Stand with feet shoulder-width apart, lower your hips until thighs are parallel to the floor, then return to standing."));
        eList.add(createExercise(dbhandler, "Russian twists", 20, 50, 0, 2, "Core",
                "Sit with feet off the floor, twist your torso side to side while holding your hands together or a weight."));
        eList.add(createExercise(dbhandler, "High knees", 45, 50, 0, 2, "Cardio",
                "Run in place by lifting your knees as high as possible toward your chest while pumping your arms."));
        eList.add(createExercise(dbhandler, "Sit ups", 15, 50, 0, 1, "Abs",
                "Lie on your back, bend knees, and lift your torso up to your thighs, then return slowly to the ground."));
        eList.add(createExercise(dbhandler, "Knee pushups", 15, 50, 0, 1, "Chest",
                "Start on your knees, place hands on the floor, lower your chest to the ground and push back up."));
        eList.add(createExercise(dbhandler, "Lower abs", 20, 50, 0, 1, "Abs",
                "Lie on your back, lift both legs straight up and slowly lower them without touching the floor."));
        eList.add(createExercise(dbhandler, "Jump lunges", 20, 50, 0, 2, "Legs",
                "Lunge forward, jump explosively, and switch legs in the air. Land softly and continue alternating."));
        eList.add(createExercise(dbhandler, "Hip thrusts", 15, 50, 0, 1, "Glutes",
                "Lie on your back with knees bent, lift your hips upward by squeezing your glutes, then lower slowly."));
        eList.add(createExercise(dbhandler, "Burpees (no pushups)", 10, 50, 0, 2, "Cardio",
                "From standing, squat down, kick your legs back to a plank, return to squat, and jump explosively upward."));
        eList.add(createExercise(dbhandler, "Scissors", 40, 50, 0, 1, "Abs",
                "Lie on your back, alternate lifting legs in a crisscross motion while keeping your core engaged."));
        eList.add(createExercise(dbhandler, "Jump squats", 10, 50, 0, 2, "Legs & Cardio",
                "Perform a squat, then jump explosively and land softly, immediately returning to the squat position."));

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

    public void addPushups(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());
        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(createExercise(dbhandler, "Burpees", 12, 50, 0, 3, "Full Body",
                "From standing, squat down, kick legs back to a plank, return to squat, and jump explosively upward."));
        eList.add(createExercise(dbhandler, "Staggered pushups", 16, 50, 0, 3, "Chest & Arms",
                "Place one hand slightly forward and one back, perform pushups alternating sides for balance."));
        eList.add(createExercise(dbhandler, "Pushup rotation", 12, 50, 0, 3, "Chest & Core",
                "Do a pushup, then rotate your torso and raise one arm towards the ceiling for a side plank position."));
        eList.add(createExercise(dbhandler, "Hindu pushup", 16, 50, 0, 4, "Chest & Shoulders",
                "Start in downward dog, sweep your chest forward and down, then push up into upward dog position."));
        eList.add(createExercise(dbhandler, "One leg pushups", 12, 50, 0, 4, "Chest & Core",
                "Perform pushups while keeping one leg lifted for extra core and balance challenge."));
        eList.add(createExercise(dbhandler, "Diamond pushups", 16, 50, 0, 4, "Triceps",
                "Place your hands under your chest forming a diamond shape, lower and push up slowly."));
        eList.add(createExercise(dbhandler, "Spiderman pushups", 16, 50, 0, 4, "Chest & Core",
                "As you lower in a pushup, bring one knee toward your elbow. Alternate sides each rep."));
        eList.add(createExercise(dbhandler, "Decline pushups", 12, 50, 0, 4, "Upper Chest",
                "Elevate your feet on a bench or platform and perform a pushup to target upper chest and shoulders."));
        eList.add(createExercise(dbhandler, "Archer pushups", 16, 50, 0, 5, "Chest & Arms",
                "Keep one arm extended while lowering toward the opposite arm, mimicking an archer’s draw motion."));
        eList.add(createExercise(dbhandler, "One arm pushups", 12, 50, 0, 5, "Chest & Core",
                "Perform pushups with one arm while keeping your core engaged and feet spread for stability."));
        eList.add(createExercise(dbhandler, "Explosive pushups", 10, 50, 0, 5, "Chest & Power",
                "Perform pushups explosively so your hands leave the floor. Land softly and repeat."));

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

    public void addLegDay(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());
        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(createExercise(dbhandler, "Burpees", 10, 50, 10, 3, "Full Body",
                "From standing, squat down, kick legs back into a plank, return to squat, and jump explosively upward."));
        eList.add(createExercise(dbhandler, "Side plank leg up", 10, 50, 10, 2, "Core & Glutes",
                "Hold a side plank position and lift the top leg upward, keeping your core engaged throughout."));
        eList.add(createExercise(dbhandler, "Single leg deadlifts", 10, 50, 10, 3, "Legs & Glutes",
                "Stand on one leg, hinge at the hips keeping back straight, and lower your torso toward the floor, then return upright."));
        eList.add(createExercise(dbhandler, "Bicycle crunches", 10, 50, 10, 2, "Abs",
                "Lie on your back, lift shoulders off the ground, and alternate touching elbows to opposite knees in a pedaling motion."));
        eList.add(createExercise(dbhandler, "Bottom up lunges", 10, 50, 10, 3, "Legs",
                "Step forward into a lunge, then drive back to standing, focusing on pushing from the heel and engaging glutes."));
        eList.add(createExercise(dbhandler, "Mountain climbers", 10, 50, 10, 2, "Cardio & Core",
                "Start in plank position, alternate driving knees toward your chest quickly while keeping your core tight."));
        eList.add(createExercise(dbhandler, "Pushups", 10, 50, 10, 2, "Chest & Arms",
                "Start in high plank, lower your chest toward the floor, and push back up while keeping your core tight."));
        eList.add(createExercise(dbhandler, "Single leg chair stands", 10, 50, 10, 3, "Legs & Balance",
                "Sit on a chair, extend one leg, then stand up using only the other leg, keeping balance controlled."));
        eList.add(createExercise(dbhandler, "One legged plank", 10, 50, 10, 3, "Core & Balance",
                "Hold a plank position while lifting one leg off the ground, maintaining a straight line from head to heels."));
        eList.add(createExercise(dbhandler, "Jump squats", 10, 50, 10, 3, "Legs & Cardio",
                "Perform a squat, then jump explosively, landing softly and returning to squat position."));

        Workout wd = new Workout();
        wd.setDifficulty(3);
        wd.setNumberOfSets(4);
        wd.setSetPause(120);
        wd.setExercises(eList);
        wd.setTotalTime();
        wd.setType("TIME");
        wd.setWod("A leg-focused workout targeting strength, balance, and explosive power for lower-body conditioning.");
        wd.setTitle("Leg Day");

        this.wodList.add(wd);
    }

    public void addGreekTraining(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());
        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(createExercise(dbhandler, "Pushups", 40, 0, 0, 4, "Chest & Arms",
                "Perform pushups with controlled form, lowering your chest to the ground and pushing back up."));
        eList.add(createExercise(dbhandler, "Jump squats", 40, 0, 0, 4, "Legs & Cardio",
                "Perform a squat, then jump explosively and land softly, immediately returning to squat position."));
        eList.add(createExercise(dbhandler, "Burpees", 40, 0, 0, 5, "Full Body & Cardio",
                "From standing, squat down, kick your feet back into a plank, return to squat, and jump explosively upward."));
        eList.add(createExercise(dbhandler, "Sit ups", 40, 0, 0, 3, "Abs",
                "Lie on your back, bend knees, lift your torso up to your thighs, then return slowly to the ground."));
        eList.add(createExercise(dbhandler, "Jump lunges", 40, 0, 0, 4, "Legs & Cardio",
                "Lunge forward, jump explosively, switch legs in mid-air, and land softly."));
        eList.add(createExercise(dbhandler, "Plank", 1, 0, 0, 2, "Core",
                "Hold a forearm plank position, keeping your body in a straight line and engaging your core."));
        eList.add(createExercise(dbhandler, "Pushups on fist", 40, 0, 0, 5, "Chest & Arms",
                "Perform pushups on your fists for added wrist stability and forearm strength."));
        eList.add(createExercise(dbhandler, "Burpees", 40, 0, 0, 5, "Full Body & Cardio",
                "From standing, squat down, kick your feet back into a plank, return to squat, and jump explosively upward."));
        eList.add(createExercise(dbhandler, "Lower abs", 40, 0, 0, 3, "Abs",
                "Lie on your back, lift your legs toward the ceiling, then slowly lower them without touching the ground."));

        // Add repetitions of key exercises to mimic original structure
        eList.add(createExercise(dbhandler, "Pushups", 40, 0, 0, 4, "Chest & Arms",
                "Perform pushups with controlled form, lowering your chest to the ground and pushing back up."));
        eList.add(createExercise(dbhandler, "Plank", 1, 0, 0, 2, "Core",
                "Hold a forearm plank position, keeping your body in a straight line and engaging your core."));

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

    public void addDoubleProg1(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());
        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(createExercise(dbhandler, "Side burpees left", 10, 50, 10, 4, "Full Body & Cardio",
                "Perform a burpee, then jump laterally to the left before repeating."));
        eList.add(createExercise(dbhandler, "Run in place", 10, 50, 10, 1, "Cardio",
                "Run on the spot, lifting knees high and pumping your arms."));
        eList.add(createExercise(dbhandler, "Side burpees right", 10, 50, 10, 4, "Full Body & Cardio",
                "Perform a burpee, then jump laterally to the right before repeating."));
        eList.add(createExercise(dbhandler, "Isometric lower abs", 10, 50, 10, 2, "Abs",
                "Lie on your back and hold your legs slightly above the floor, keeping your lower abs engaged."));
        eList.add(createExercise(dbhandler, "Australian pullups", 10, 50, 10, 3, "Back & Arms",
                "Hang under a bar, pull your chest toward the bar, then lower slowly while keeping your body straight."));
        eList.add(createExercise(dbhandler, "Pushup rotation", 10, 50, 10, 3, "Chest & Core",
                "Perform a pushup, then rotate your torso and lift one arm toward the ceiling for a side plank."));
        eList.add(createExercise(dbhandler, "One legged squat", 10, 50, 10, 4, "Legs & Balance",
                "Perform a squat on one leg while keeping the other leg extended forward, maintaining balance."));
        eList.add(createExercise(dbhandler, "Sphinx pushups", 10, 50, 10, 4, "Triceps & Chest",
                "From a forearm plank, lower and push up, keeping elbows tucked and core engaged."));
        eList.add(createExercise(dbhandler, "Lower abs", 10, 50, 10, 2, "Abs",
                "Lie on your back, lift legs toward the ceiling, and slowly lower them without touching the ground."));
        eList.add(createExercise(dbhandler, "Isometric pushups", 10, 50, 120, 4, "Chest & Core",
                "Hold the low position of a pushup as long as possible, keeping your body straight."));

        eList.add(createExercise(dbhandler, "Burpees", 10, 50, 10, 4, "Full Body & Cardio",
                "From standing, squat down, kick your feet back into a plank, return to squat, and jump explosively upward."));
        eList.add(createExercise(dbhandler, "Australian pullups", 10, 50, 10, 3, "Back & Arms",
                "Hang under a bar, pull your chest toward the bar, then lower slowly while keeping your body straight."));
        eList.add(createExercise(dbhandler, "Russian twists", 10, 50, 10, 2, "Core",
                "Sit with feet off the floor, twist torso side to side while holding hands together or a weight."));
        eList.add(createExercise(dbhandler, "Handstand pushups", 10, 50, 10, 5, "Shoulders & Arms",
                "Perform pushups while in a handstand position against a wall for support."));
        eList.add(createExercise(dbhandler, "Plank ups", 10, 50, 10, 3, "Core & Arms",
                "Move from forearm plank to high plank and back, alternating one arm at a time."));
        eList.add(createExercise(dbhandler, "Jump lunges", 10, 50, 10, 3, "Legs & Cardio",
                "Lunge forward, jump explosively, switch legs mid-air, and land softly."));
        eList.add(createExercise(dbhandler, "Archer pushups", 10, 50, 10, 4, "Chest & Arms",
                "Keep one arm extended while lowering toward the opposite arm, mimicking an archer’s draw motion."));
        eList.add(createExercise(dbhandler, "Scissors", 10, 50, 10, 2, "Abs",
                "Lie on your back, lift legs slightly, and alternate crossing them over each other in a scissor motion."));
        eList.add(createExercise(dbhandler, "Decline pushups", 10, 50, 10, 4, "Chest & Shoulders",
                "Elevate your feet and perform pushups to target upper chest and shoulders."));
        eList.add(createExercise(dbhandler, "Dips", 10, 50, 120, 4, "Triceps & Chest",
                "Lower your body using parallel bars or a bench and push back up, keeping elbows close to your body."));

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

    public void addDoubleProg2(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());
        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(createExercise(dbhandler, "Side burpees left", 10, 50, 10, 4, "Full Body & Cardio",
                "Perform a burpee, then jump laterally to the left before repeating."));
        eList.add(createExercise(dbhandler, "Run in place", 10, 50, 10, 1, "Cardio",
                "Run on the spot, lifting knees high and pumping your arms."));
        eList.add(createExercise(dbhandler, "Side burpees right", 10, 50, 10, 4, "Full Body & Cardio",
                "Perform a burpee, then jump laterally to the right before repeating."));
        eList.add(createExercise(dbhandler, "Bicycle crunches", 10, 50, 10, 2, "Abs",
                "Lie on your back, alternate touching elbows to opposite knees in a pedaling motion."));
        eList.add(createExercise(dbhandler, "Hindu pushups", 10, 50, 10, 4, "Chest & Shoulders",
                "Start in downward dog, sweep chest forward and down, then push up into upward dog."));
        eList.add(createExercise(dbhandler, "Shoulder taps", 10, 50, 10, 2, "Chest & Core",
                "In plank position, tap each shoulder alternately while keeping your core tight."));
        eList.add(createExercise(dbhandler, "Windshield wipers", 10, 50, 10, 3, "Core",
                "Lie on your back, lift legs, and rotate them side to side like wipers."));
        eList.add(createExercise(dbhandler, "Jump squats", 10, 50, 10, 3, "Legs & Cardio",
                "Perform a squat, then jump explosively and land softly."));
        eList.add(createExercise(dbhandler, "Pushups", 10, 50, 10, 2, "Chest & Arms",
                "Perform standard pushups with proper form."));
        eList.add(createExercise(dbhandler, "Isometric lower abs", 10, 50, 120, 3, "Abs",
                "Hold legs slightly above the floor, keeping lower abs engaged."));

        eList.add(createExercise(dbhandler, "Burpees", 10, 50, 10, 4, "Full Body & Cardio",
                "From standing, squat, kick feet back into plank, return, and jump."));
        eList.add(createExercise(dbhandler, "Butterfly crunches", 10, 50, 10, 2, "Abs",
                "Lie on back, bring soles together, perform crunches touching feet or ankles."));
        eList.add(createExercise(dbhandler, "Jump crunches", 10, 50, 10, 3, "Abs & Cardio",
                "Crunch up explosively while simultaneously jumping your feet off the floor."));
        eList.add(createExercise(dbhandler, "Half burpees", 10, 50, 10, 3, "Full Body & Cardio",
                "Perform burpees without the pushup, finishing with a jump."));
        eList.add(createExercise(dbhandler, "Lower abs", 10, 50, 10, 2, "Abs",
                "Lift legs toward ceiling and lower slowly without touching the floor."));
        eList.add(createExercise(dbhandler, "Pushups toe touch", 10, 50, 10, 3, "Chest & Core",
                "Do a pushup, then touch one hand to the opposite foot while maintaining plank."));
        eList.add(createExercise(dbhandler, "Punch sit ups", 10, 50, 10, 3, "Abs & Arms",
                "Perform a sit up and punch forward at the top of the motion."));
        eList.add(createExercise(dbhandler, "Power tucks", 10, 50, 10, 4, "Abs & Cardio",
                "Jump and tuck knees to chest, landing softly before repeating."));
        eList.add(createExercise(dbhandler, "Pike pushups", 10, 50, 10, 4, "Shoulders & Chest",
                "Form a pike position and perform pushups targeting shoulders."));
        eList.add(createExercise(dbhandler, "Mountain climbers", 10, 50, 120, 2, "Cardio & Core",
                "Drive knees toward chest rapidly from plank position."));

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

    public void addPlankPushups(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());
        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(createExercise(dbhandler, "Arm leg plank", 10, 20, 10, 2, "Core & Shoulders",
                "In plank position, lift opposite arm and leg simultaneously and hold briefly."));
        eList.add(createExercise(dbhandler, "Knee pushups", 10, 20, 10, 1, "Chest & Arms",
                "Perform pushups on knees with proper form, lowering chest to floor."));
        eList.add(createExercise(dbhandler, "Pushups full rotation", 10, 20, 10, 3, "Chest & Core",
                "Do a pushup and rotate your body into a side plank before returning."));
        eList.add(createExercise(dbhandler, "Plank ups", 10, 20, 10, 3, "Core & Arms",
                "Move from forearm plank to high plank and back, alternating arms."));
        eList.add(createExercise(dbhandler, "Tiger band pushups", 10, 20, 10, 4, "Chest & Arms",
                "Perform pushups with resistance band around upper back for extra tension."));
        eList.add(createExercise(dbhandler, "Dips toe touch", 10, 20, 10, 3, "Triceps & Chest",
                "Do dips and at the top reach one hand toward your opposite toe."));
        eList.add(createExercise(dbhandler, "Side kicks", 10, 20, 10, 2, "Core & Legs",
                "From plank or standing, perform controlled side kicks."));
        eList.add(createExercise(dbhandler, "Sphinx pushups", 10, 20, 10, 4, "Triceps & Chest",
                "From forearm plank, lower and push up keeping elbows tucked."));
        eList.add(createExercise(dbhandler, "Clap pushups", 10, 20, 10, 5, "Chest & Arms",
                "Perform explosive pushups and clap hands at the top."));
        eList.add(createExercise(dbhandler, "Dips", 10, 20, 10, 3, "Triceps & Chest",
                "Lower your body on parallel bars or a bench and push back up."));

        eList.add(createExercise(dbhandler, "Plank leg lifts", 10, 20, 10, 2, "Core & Glutes",
                "In plank, lift legs alternately keeping hips stable."));
        eList.add(createExercise(dbhandler, "Plank stars", 10, 20, 10, 3, "Core & Shoulders",
                "From plank, lift opposite arm and leg outwards forming a star shape."));
        eList.add(createExercise(dbhandler, "Shoulder taps", 10, 20, 10, 2, "Chest & Core",
                "In plank position, tap each shoulder alternately keeping core tight."));
        eList.add(createExercise(dbhandler, "Diamond pushups", 10, 20, 10, 3, "Chest & Triceps",
                "Form a diamond with hands and perform pushups targeting triceps."));
        eList.add(createExercise(dbhandler, "Pushups rotation", 10, 20, 10, 3, "Chest & Core",
                "Do a pushup and rotate into side plank, alternating sides."));
        eList.add(createExercise(dbhandler, "Pushups toe touch", 10, 20, 10, 3, "Chest & Core",
                "Perform pushups and touch one hand to opposite foot at the top."));
        eList.add(createExercise(dbhandler, "One leg pushups", 10, 20, 10, 4, "Chest & Core",
                "Perform pushups while lifting one leg off the ground."));
        eList.add(createExercise(dbhandler, "One leg dips", 10, 20, 10, 4, "Triceps & Chest",
                "Perform dips while extending one leg forward or backward."));
        eList.add(createExercise(dbhandler, "Plank", 10, 20, 10, 2, "Core",
                "Hold forearm plank position keeping body in a straight line."));
        eList.add(createExercise(dbhandler, "Pushups half squat", 10, 20, 10, 3, "Chest & Legs",
                "Do a pushup, then return to standing with a half squat before next repetition."));

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


    public void addAbsChillax(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());
        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(createExercise(dbhandler, "Plank", 10, 20, 10, 2, "Core",
                "Hold a forearm plank, keeping body straight and core engaged."));
        eList.add(createExercise(dbhandler, "Russian twists", 10, 20, 10, 2, "Obliques",
                "Sit with feet off the ground and rotate torso side to side."));
        eList.add(createExercise(dbhandler, "Single leg crunches", 10, 20, 10, 2, "Abs",
                "Perform crunches while keeping one leg raised and alternating sides."));
        eList.add(createExercise(dbhandler, "Plank kicks", 10, 20, 10, 2, "Core & Legs",
                "From plank, lift legs alternately in a kicking motion."));
        eList.add(createExercise(dbhandler, "Jackknife twists", 10, 20, 10, 3, "Abs & Obliques",
                "From lying position, lift legs and torso, twisting to touch toes or sides."));
        eList.add(createExercise(dbhandler, "3 way crunch pulses", 10, 20, 10, 2, "Abs",
                "Pulse your torso in three directions to engage all areas of the abs."));
        eList.add(createExercise(dbhandler, "Side plank", 10, 20, 10, 2, "Obliques",
                "Hold a plank on one side, keeping body straight and core tight."));
        eList.add(createExercise(dbhandler, "Low abs twists", 10, 20, 10, 2, "Lower Abs & Obliques",
                "Lie on back and twist lower body side to side while legs lifted."));
        eList.add(createExercise(dbhandler, "Low abs bent knees", 10, 20, 10, 2, "Lower Abs",
                "Raise bent knees toward chest while lying on back, lowering slowly."));
        eList.add(createExercise(dbhandler, "Side plank leg circle", 10, 20, 10, 2, "Obliques & Glutes",
                "Hold side plank and move top leg in controlled circles."));

        eList.add(createExercise(dbhandler, "Seated leg raises", 10, 20, 10, 2, "Lower Abs",
                "Sit with legs extended and lift them together while leaning back slightly."));
        eList.add(createExercise(dbhandler, "Circle crunches", 10, 20, 10, 2, "Abs",
                "Perform crunches moving your torso in a circular motion."));
        eList.add(createExercise(dbhandler, "Plank buzzsaw", 10, 20, 10, 3, "Core",
                "From plank, rotate hips side to side rapidly while keeping torso stable."));
        eList.add(createExercise(dbhandler, "Russian twists", 10, 20, 10, 2, "Obliques",
                "Repeat the side-to-side torso rotation with controlled movement."));
        eList.add(createExercise(dbhandler, "Jackknives", 10, 20, 10, 3, "Abs",
                "Lift legs and torso simultaneously to meet in a jackknife position."));
        eList.add(createExercise(dbhandler, "Full arm side plank leg up", 10, 20, 10, 3, "Obliques & Glutes",
                "In side plank, lift top leg and extend top arm overhead."));
        eList.add(createExercise(dbhandler, "Lateral abs", 10, 20, 10, 2, "Obliques",
                "Perform side crunches or twists to target lateral abdominal muscles."));
        eList.add(createExercise(dbhandler, "Scissor drops", 10, 20, 10, 2, "Lower Abs",
                "Alternate lowering legs in a scissor motion without touching the floor."));
        eList.add(createExercise(dbhandler, "Plank jacks", 10, 20, 10, 3, "Core & Cardio",
                "Jump legs in and out while holding a plank position."));
        eList.add(createExercise(dbhandler, "Butterfly crunches", 10, 20, 10, 2, "Abs & Obliques",
                "Lie on back, soles together, crunch up touching feet or ankles."));

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

    public void addJordanYeoh(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());
        ArrayList<Exercise> eList = new ArrayList<>();

        // First set of exercises (short duration)
        Exercise pushupsToeTouchShort = createExercise(dbhandler, "Pushups toe touch", 10, 30, 2, 3, "Chest & Core",
                "Do a pushup, then touch one hand to the opposite foot at the top.");
        Exercise runningJacksShort = createExercise(dbhandler, "Running jacks", 10, 30, 2, 1, "Cardio",
                "Run in place with arms and legs moving like jumping jacks.");
        Exercise mountainClimbersShort = createExercise(dbhandler, "Mountain climbers", 10, 30, 2, 2, "Core & Cardio",
                "Drive knees toward chest rapidly from plank position.");
        Exercise buttKicksShort = createExercise(dbhandler, "Butt kicks", 10, 30, 2, 1, "Legs & Cardio",
                "Run in place while kicking heels toward your glutes.");
        Exercise burpeesShort = createExercise(dbhandler, "Burpees", 10, 30, 2, 4, "Full Body & Cardio",
                "Squat, kick feet back into plank, return, and jump explosively.");
        Exercise jumpLungesShort = createExercise(dbhandler, "Jump lunges", 10, 30, 2, 4, "Legs & Cardio",
                "Perform alternating lunges with an explosive jump.");

        // Second set of exercises (longer duration)
        Exercise pushupsToeTouchLong = createExercise(dbhandler, "Pushups toe touch", 10, 30, 30, 3, "Chest & Core",
                "Do a pushup, then touch one hand to the opposite foot at the top.");
        Exercise runningJacksLong = createExercise(dbhandler, "Running jacks", 10, 30, 30, 1, "Cardio",
                "Run in place with arms and legs moving like jumping jacks.");
        Exercise mountainClimbersLong = createExercise(dbhandler, "Mountain climbers", 10, 30, 30, 2, "Core & Cardio",
                "Drive knees toward chest rapidly from plank position.");
        Exercise buttKicksLong = createExercise(dbhandler, "Butt kicks", 10, 30, 30, 1, "Legs & Cardio",
                "Run in place while kicking heels toward your glutes.");
        Exercise burpeesLong = createExercise(dbhandler, "Burpees", 10, 30, 30, 4, "Full Body & Cardio",
                "Squat, kick feet back into plank, return, and jump explosively.");
        Exercise jumpLungesLong = createExercise(dbhandler, "Jump lunges", 10, 30, 120, 4, "Legs & Cardio",
                "Perform alternating lunges with an explosive jump.");

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

    public void addMorningPushups(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());
        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(createExercise(dbhandler, "Burpees", 15, 60, 0, 4, "Full Body & Cardio",
                "From standing, drop into a plank, perform a pushup, then jump up explosively."));
        eList.add(createExercise(dbhandler, "Diamond pushups", 16, 60, 0, 4, "Chest & Triceps",
                "Hands form a diamond shape; lower chest to hands and push up."));
        eList.add(createExercise(dbhandler, "Spiderman pushups", 16, 60, 0, 4, "Chest, Core & Arms",
                "Perform a pushup while bringing one knee to the elbow on each repetition."));
        eList.add(createExercise(dbhandler, "One leg pushups", 15, 60, 0, 5, "Chest & Core",
                "Raise one leg while performing a pushup to increase core engagement."));
        eList.add(createExercise(dbhandler, "Staggered pushups", 16, 60, 0, 4, "Chest & Arms",
                "One hand slightly forward of the other, alternate positions with each set."));
        eList.add(createExercise(dbhandler, "Archer pushups", 16, 60, 0, 5, "Chest & Arms",
                "Push up while extending one arm out to the side, alternating arms each rep."));
        eList.add(createExercise(dbhandler, "Hindu pushup", 16, 60, 0, 5, "Chest, Shoulders & Core",
                "Start in downward dog, swoop chest down and forward, finishing in upward dog position."));

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

    public void addWarmupPreTabata(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());
        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(createExercise(dbhandler, "Squats", 10, 20, 10, 1, "Legs & Glutes",
                "Stand with feet shoulder-width apart and lower hips, then return to standing."));
        eList.add(createExercise(dbhandler, "Pushups", 10, 20, 10, 2, "Chest & Arms",
                "Lower chest to the ground and push back up in a controlled motion."));
        eList.add(createExercise(dbhandler, "Crunches", 10, 20, 10, 2, "Abs",
                "Lie on your back and lift upper body toward knees using your core."));
        eList.add(createExercise(dbhandler, "Squat taps", 10, 20, 10, 1, "Legs & Cardio",
                "Perform a squat and tap one foot to the side, alternating sides."));
        eList.add(createExercise(dbhandler, "Jump jacks", 10, 20, 10, 1, "Cardio & Full Body",
                "Jump with arms and legs extending outward and return to start position."));
        eList.add(createExercise(dbhandler, "Pushups", 10, 20, 10, 2, "Chest & Arms",
                "Standard pushup for warming up upper body."));
        eList.add(createExercise(dbhandler, "Hip thrusts", 10, 20, 10, 1, "Glutes & Core",
                "Push hips upward from the ground while lying on your back with knees bent."));
        eList.add(createExercise(dbhandler, "Leg raises", 10, 20, 10, 2, "Lower Abs",
                "Lift legs straight up from the ground while lying on your back."));
        eList.add(createExercise(dbhandler, "High plank", 10, 20, 10, 2, "Core & Arms",
                "Hold plank position with hands under shoulders and body straight."));
        eList.add(createExercise(dbhandler, "Run in place", 10, 20, 120, 1, "Cardio",
                "Jog in place to raise heart rate."));

        eList.add(createExercise(dbhandler, "Squat run", 10, 20, 10, 1, "Legs & Cardio",
                "Alternate between squats and short running steps in place."));
        eList.add(createExercise(dbhandler, "Pushups", 10, 20, 10, 2, "Chest & Arms",
                "Standard pushup."));
        eList.add(createExercise(dbhandler, "Butterfly crunches", 10, 20, 10, 2, "Abs",
                "Lie on back, feet together, and lift chest while touching feet."));
        eList.add(createExercise(dbhandler, "Lunges", 10, 20, 10, 1, "Legs & Glutes",
                "Step forward and lower hips until both knees are at 90 degrees."));
        eList.add(createExercise(dbhandler, "Jump jacks", 10, 20, 10, 1, "Cardio & Full Body",
                "Standard jumping jack for warmup."));
        eList.add(createExercise(dbhandler, "Pushups", 10, 20, 10, 2, "Chest & Arms",
                "Standard pushup."));
        eList.add(createExercise(dbhandler, "Knee to elbow", 10, 20, 10, 2, "Core & Obliques",
                "From plank, bring knee to opposite elbow alternately."));
        eList.add(createExercise(dbhandler, "Step in", 10, 20, 10, 1, "Legs & Cardio",
                "Step one foot forward and back repeatedly as a light warmup."));
        eList.add(createExercise(dbhandler, "Knee up run", 10, 20, 10, 1, "Cardio & Legs",
                "Run in place bringing knees up toward chest."));
        eList.add(createExercise(dbhandler, "Low plank", 10, 20, 10, 2, "Core & Arms",
                "Hold a forearm plank keeping body straight and engaged."));

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

    public void addPureTabata(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());
        ArrayList<Exercise> eList = new ArrayList<>();

        Exercise burpees = createExercise(dbhandler, "Burpees", 10, 20, 10, 4, "Full Body & Cardio",
                "From standing, drop into a plank, perform a pushup, then jump up explosively.");
        Exercise jumpRope = createExercise(dbhandler, "Jump rope", 10, 20, 10, 2, "Cardio & Legs",
                "Jump rope continuously, keeping a steady pace and engaging your calves and core.");

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

    public void addFrasFridayClassic(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());
        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(createExercise(dbhandler, "Jump Squats", 10, 40, 0, 3, "Legs & Glutes",
                "Perform a squat and explode upward into a jump, landing softly and repeating."));
        eList.add(createExercise(dbhandler, "Shoulder taps", 20, 40, 0, 2, "Chest & Core",
                "From plank, tap each shoulder alternately while keeping hips stable."));
        eList.add(createExercise(dbhandler, "Lower abs", 20, 50, 0, 2, "Abs",
                "Perform slow controlled leg raises to engage lower abdominal muscles."));
        eList.add(createExercise(dbhandler, "Knee up run", 35, 40, 0, 1, "Cardio & Legs",
                "Run in place bringing knees up towards chest to increase heart rate."));
        eList.add(createExercise(dbhandler, "Sit ups", 20, 50, 0, 2, "Abs",
                "Lie on your back and lift torso towards knees, keeping core engaged."));
        eList.add(createExercise(dbhandler, "Knee pushups", 20, 50, 0, 2, "Chest & Arms",
                "Perform pushups on knees, maintaining a straight line from head to knees."));
        eList.add(createExercise(dbhandler, "Plank", 1, 50, 0, 2, "Core",
                "Hold a plank position on forearms or hands, keeping the body straight and tight."));

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

    public void addFridayClassic(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());
        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(createExercise(dbhandler, "Jump Squats", 15, 40, 0, 3, "Legs & Glutes",
                "Perform a squat and explode upward into a jump, landing softly and repeating."));
        eList.add(createExercise(dbhandler, "Decline pushups", 20, 40, 0, 4, "Chest & Arms",
                "Perform pushups with feet elevated on a platform to increase chest activation."));
        eList.add(createExercise(dbhandler, "Lower abs", 20, 50, 0, 2, "Abs",
                "Controlled leg raises while lying on your back to target lower abdominal muscles."));
        eList.add(createExercise(dbhandler, "Dips", 20, 40, 0, 3, "Triceps & Chest",
                "Use parallel bars or a bench to lower and raise your body using triceps strength."));
        eList.add(createExercise(dbhandler, "Sit ups", 20, 50, 0, 2, "Abs",
                "Lie on your back and lift torso towards knees, keeping core tight."));
        eList.add(createExercise(dbhandler, "Pushups", 25, 50, 0, 3, "Chest & Arms",
                "Standard pushup with controlled movement."));
        eList.add(createExercise(dbhandler, "Advanced plank", 1, 50, 0, 4, "Core & Shoulders",
                "Hold a plank with variations such as arm/leg lifts for increased intensity."));

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

    public void addWholeBodyIntermediate(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());
        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(createExercise(dbhandler, "Diamond pushups", 15, 50, 0, 4, "Chest & Arms",
                "Hands close together in a diamond shape, lower chest to hands, then push up."));
        eList.add(createExercise(dbhandler, "Jump lunges", 28, 50, 0, 4, "Legs & Glutes",
                "Lunge forward explosively, jumping to switch legs in mid-air."));
        eList.add(createExercise(dbhandler, "Pushups", 20, 50, 0, 3, "Chest & Arms",
                "Standard pushups with controlled form and full range of motion."));
        eList.add(createExercise(dbhandler, "Lower abs", 20, 50, 0, 3, "Abs",
                "Perform controlled leg raises or crunch variations targeting lower abdominal muscles."));
        eList.add(createExercise(dbhandler, "Burpees", 15, 50, 0, 4, "Full Body & Cardio",
                "From standing, drop into a plank, do a pushup, then jump explosively upward."));

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

    public void addBurpeesMattanza(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());
        ArrayList<Exercise> eList = new ArrayList<>();

        Exercise burpees = createExercise(dbhandler, "Burpees", 1, 4, 0, 5, "Full Body & Cardio",
                "Perform a squat, drop into a plank with a pushup, then jump explosively upward.");

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

    public void addPureHIIT(View view) {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(view.getContext());
        ArrayList<Exercise> eList = new ArrayList<>();

        eList.add(createExercise(dbhandler, "High knees", 10, 20, 2, 3, "Legs & Cardio",
                "Run in place while lifting knees as high as possible, pumping arms."));
        eList.add(createExercise(dbhandler, "Mountain climbers", 10, 20, 2, 3, "Core & Cardio",
                "From a plank position, alternate driving knees toward the chest quickly."));
        eList.add(createExercise(dbhandler, "Spiderman", 10, 20, 2, 3, "Core & Chest",
                "In a pushup position, bring knee toward elbow alternating sides."));
        eList.add(createExercise(dbhandler, "Pushups", 10, 20, 2, 3, "Chest & Arms",
                "Standard pushups with controlled motion."));
        eList.add(createExercise(dbhandler, "Jump squats", 10, 20, 2, 4, "Legs & Glutes",
                "Perform a squat and explode upward into a jump, landing softly."));
        eList.add(createExercise(dbhandler, "Jump jacks", 10, 20, 2, 2, "Full Body & Cardio",
                "Jump to a wide stance with arms overhead, then return to start."));

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
}