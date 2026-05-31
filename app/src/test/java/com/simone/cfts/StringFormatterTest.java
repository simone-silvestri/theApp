package com.simone.cfts;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class StringFormatterTest {

    // --- setContent / getContent ---

    @Test
    public void setContent_producesCorrectFormat() {
        Workout work = makeWorkout("TestWod", "TIME", 2, 3, 60, 600);
        work.setExercises(makeExerciseList(
                new String[]{"Pushups", "Squats"},
                new int[]{10, 15},
                new int[]{40, 50},
                new int[]{20, 10}
        ));

        StringFormatter sf = new StringFormatter();
        sf.setContent(work);
        String content = sf.getContent();

        assertEquals("CFLSPASS|TestWod|TIME|2|3|60|600|Pushups|40|20|10|Squats|50|10|15|END", content);
    }

    @Test
    public void setContent_singleExercise() {
        Workout work = makeWorkout("Solo", "REPS", 1, 1, 0, 100);
        work.setExercises(makeExerciseList(
                new String[]{"Burpees"},
                new int[]{5},
                new int[]{30},
                new int[]{10}
        ));

        StringFormatter sf = new StringFormatter();
        sf.setContent(work);

        assertEquals("CFLSPASS|Solo|REPS|1|1|0|100|Burpees|30|10|5|END", sf.getContent());
    }

    // --- setWorkout (single workout parsing) ---

    @Test
    public void setWorkout_parsesValidString() {
        String input = "CFLSPASS|MyWorkout|TIME|3|4|120|900|Pushups|40|20|10|Squats|50|10|15|END";

        StringFormatter sf = new StringFormatter();
        sf.setWorkout(input);
        Workout w = sf.getWorkout();

        assertNotNull(w);
        assertEquals("MyWorkout", w.getTitle());
        assertEquals("TIME", w.getType());
        assertEquals(3, w.getDifficulty());
        assertEquals(4, w.getNumberOfSets());
        assertEquals(120, w.getSetPause());
        assertEquals(900, w.getTotalTime());

        ArrayList<Exercise> exes = w.getExercises();
        assertEquals(2, exes.size());

        assertEquals("Pushups", exes.get(0).getName());
        assertEquals(40, exes.get(0).getTimeInSeconds());
        assertEquals(20, exes.get(0).getPauseInSeconds());
        assertEquals(10, exes.get(0).getReps());

        assertEquals("Squats", exes.get(1).getName());
        assertEquals(50, exes.get(1).getTimeInSeconds());
        assertEquals(10, exes.get(1).getPauseInSeconds());
        assertEquals(15, exes.get(1).getReps());
    }

    @Test
    public void setWorkout_rejectsWrongPassword() {
        String input = "WRONG|MyWorkout|TIME|3|4|120|900|Pushups|40|20|10|END";

        StringFormatter sf = new StringFormatter();
        sf.setWorkout(input);

        assertNull(sf.getWorkout());
    }

    @Test
    public void setWorkout_rejectsEmptyString() {
        StringFormatter sf = new StringFormatter();
        sf.setWorkout("");
        assertNull(sf.getWorkout());
    }

    @Test
    public void setWorkout_handlesMalformedInput() {
        StringFormatter sf = new StringFormatter();
        sf.setWorkout("CFLSPASS|OnlyTitle");
        assertNull(sf.getWorkout());
    }

    @Test
    public void setWorkout_handlesNonNumericFields() {
        String input = "CFLSPASS|Title|TIME|notanumber|4|120|900|END";

        StringFormatter sf = new StringFormatter();
        sf.setWorkout(input);

        assertNull(sf.getWorkout());
    }

    // --- Round-trip tests: setContent → setWorkout ---

    @Test
    public void roundTrip_singleWorkout() {
        Workout original = makeWorkout("RoundTrip", "REPSINTIME", 4, 5, 90, 1200);
        original.setExercises(makeExerciseList(
                new String[]{"Burpees", "Plank", "Squats"},
                new int[]{12, 1, 20},
                new int[]{50, 60, 45},
                new int[]{10, 0, 15}
        ));

        StringFormatter writer = new StringFormatter();
        writer.setContent(original);

        StringFormatter reader = new StringFormatter();
        reader.setWorkout(writer.getContent());
        Workout parsed = reader.getWorkout();

        assertNotNull(parsed);
        assertEquals(original.getTitle(), parsed.getTitle());
        assertEquals(original.getType(), parsed.getType());
        assertEquals(original.getDifficulty(), parsed.getDifficulty());
        assertEquals(original.getNumberOfSets(), parsed.getNumberOfSets());
        assertEquals(original.getSetPause(), parsed.getSetPause());
        assertEquals(original.getTotalTime(), parsed.getTotalTime());
        assertEquals(original.getExercises().size(), parsed.getExercises().size());

        for (int i = 0; i < original.getExercises().size(); i++) {
            Exercise origEx = original.getExercises().get(i);
            Exercise parsedEx = parsed.getExercises().get(i);
            assertEquals(origEx.getName(), parsedEx.getName());
            assertEquals(origEx.getTimeInSeconds(), parsedEx.getTimeInSeconds());
            assertEquals(origEx.getPauseInSeconds(), parsedEx.getPauseInSeconds());
            assertEquals(origEx.getReps(), parsedEx.getReps());
        }
    }

    // --- Helpers ---

    private Workout makeWorkout(String title, String type, int diff, int sets, int pause, int totalTime) {
        Workout w = new Workout();
        w.setTitle(title);
        w.setType(type);
        w.setDifficulty(diff);
        w.setNumberOfSets(sets);
        w.setSetPause(pause);
        w.setTotalTime(totalTime);
        return w;
    }

    private ArrayList<Exercise> makeExerciseList(String[] names, int[] reps, int[] workTimes, int[] restTimes) {
        ArrayList<Exercise> list = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            list.add(new Exercise(names[i], reps[i], workTimes[i], restTimes[i]));
        }
        return list;
    }
}
