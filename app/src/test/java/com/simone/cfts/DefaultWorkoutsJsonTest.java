package com.simone.cfts;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class DefaultWorkoutsJsonTest {

    private JSONObject root;

    @Before
    public void loadJson() throws Exception {
        String path = "src/main/assets/default_workouts.json";
        String json = new String(Files.readAllBytes(Paths.get(path)));
        root = new JSONObject(json);
    }

    @Test
    public void hasWorkoutsArray() throws Exception {
        assertTrue(root.has("workouts"));
        JSONArray workouts = root.getJSONArray("workouts");
        assertEquals(24, workouts.length());
    }

    @Test
    public void hasExerciseDetailsArray() throws Exception {
        assertTrue(root.has("exerciseDetails"));
        JSONArray details = root.getJSONArray("exerciseDetails");
        assertEquals(109, details.length());
    }

    @Test
    public void allWorkoutsHaveRequiredFields() throws Exception {
        JSONArray workouts = root.getJSONArray("workouts");
        for (int i = 0; i < workouts.length(); i++) {
            JSONObject w = workouts.getJSONObject(i);
            String title = w.getString("title");

            assertTrue(title + " missing type", w.has("type"));
            assertTrue(title + " missing difficulty", w.has("difficulty"));
            assertTrue(title + " missing sets", w.has("sets"));
            assertTrue(title + " missing setPause", w.has("setPause"));
            assertTrue(title + " missing description", w.has("description"));
            assertTrue(title + " missing exercises", w.has("exercises"));

            String type = w.getString("type");
            assertTrue(title + " invalid type: " + type,
                    type.equals("TIME") || type.equals("REPS") || type.equals("REPSINTIME"));

            assertTrue(title + " difficulty out of range",
                    w.getInt("difficulty") >= 1 && w.getInt("difficulty") <= 5);
            assertTrue(title + " sets must be positive",
                    w.getInt("sets") >= 1);
            assertTrue(title + " setPause must be non-negative",
                    w.getInt("setPause") >= 0);
            assertTrue(title + " must have at least one exercise",
                    w.getJSONArray("exercises").length() > 0);
        }
    }

    @Test
    public void allExercisesHaveRequiredFields() throws Exception {
        JSONArray workouts = root.getJSONArray("workouts");
        for (int i = 0; i < workouts.length(); i++) {
            JSONObject w = workouts.getJSONObject(i);
            String workoutTitle = w.getString("title");
            JSONArray exercises = w.getJSONArray("exercises");

            for (int j = 0; j < exercises.length(); j++) {
                JSONObject exe = exercises.getJSONObject(j);
                String context = workoutTitle + " exercise #" + j;

                assertTrue(context + " missing name", exe.has("name"));
                assertTrue(context + " missing reps", exe.has("reps"));
                assertTrue(context + " missing workTime", exe.has("workTime"));
                assertTrue(context + " missing restTime", exe.has("restTime"));

                assertFalse(context + " name is empty",
                        exe.getString("name").isEmpty());
                assertTrue(context + " reps must be non-negative",
                        exe.getInt("reps") >= 0);
                assertTrue(context + " workTime must be non-negative",
                        exe.getInt("workTime") >= 0);
                assertTrue(context + " restTime must be non-negative",
                        exe.getInt("restTime") >= 0);
            }
        }
    }

    @Test
    public void allExerciseDetailsHaveRequiredFields() throws Exception {
        JSONArray details = root.getJSONArray("exerciseDetails");
        Set<String> names = new HashSet<>();

        for (int i = 0; i < details.length(); i++) {
            JSONObject d = details.getJSONObject(i);
            String name = d.getString("name");

            assertTrue("Duplicate exercise detail: " + name, names.add(name));
            assertTrue(name + " missing difficulty", d.has("difficulty"));
            assertTrue(name + " missing muscle", d.has("muscle"));
            assertTrue(name + " missing description", d.has("description"));

            assertFalse(name + " muscle is empty", d.getString("muscle").isEmpty());
            assertFalse(name + " description is empty", d.getString("description").isEmpty());
        }
    }

    @Test
    public void workoutTitlesAreUnique() throws Exception {
        JSONArray workouts = root.getJSONArray("workouts");
        Set<String> titles = new HashSet<>();

        for (int i = 0; i < workouts.length(); i++) {
            String title = workouts.getJSONObject(i).getString("title");
            assertTrue("Duplicate workout title: " + title, titles.add(title));
        }
    }

    @Test
    public void greekTrainingHasExplicitTotalTime() throws Exception {
        JSONArray workouts = root.getJSONArray("workouts");
        for (int i = 0; i < workouts.length(); i++) {
            JSONObject w = workouts.getJSONObject(i);
            if (w.getString("title").equals("Greek Training")) {
                assertFalse("Greek Training should have totalTime set",
                        w.isNull("totalTime"));
                assertEquals(2400, w.getInt("totalTime"));
                return;
            }
        }
        fail("Greek Training workout not found");
    }

    @Test
    public void burpeesMattanzaHas100Exercises() throws Exception {
        JSONArray workouts = root.getJSONArray("workouts");
        for (int i = 0; i < workouts.length(); i++) {
            JSONObject w = workouts.getJSONObject(i);
            if (w.getString("title").equals("Burpees Mattanza")) {
                assertEquals(100, w.getJSONArray("exercises").length());
                return;
            }
        }
        fail("Burpees Mattanza workout not found");
    }

    @Test
    public void jordanYeohHas42Exercises() throws Exception {
        JSONArray workouts = root.getJSONArray("workouts");
        for (int i = 0; i < workouts.length(); i++) {
            JSONObject w = workouts.getJSONObject(i);
            if (w.getString("title").equals("Jordan Yeoh HIIT")) {
                assertEquals(42, w.getJSONArray("exercises").length());
                return;
            }
        }
        fail("Jordan Yeoh HIIT workout not found");
    }
}
