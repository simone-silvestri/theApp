package com.simone.cfts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Entry point for opening a .cfts file from outside the app. Reads the URI,
 * parses the single-workout payload via {@link StringFormatter}, then launches
 * {@link DetailActivity} in import mode (EXTRA_WORK_OR_ADD = 2) which reuses
 * the existing workout-detail UI with a DISCARD / IMPORT WORKOUT footer.
 */
public class ImportWorkoutActivity extends AppCompatActivity {
    private static final String TAG = "ImportWorkout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri data = getIntent() != null ? getIntent().getData() : null;
        if (data == null) {
            fail("No file to import.");
            return;
        }

        String content = readUri(data);
        if (content == null || content.isEmpty()) {
            fail("Could not read this file.");
            return;
        }

        StringFormatter sf = new StringFormatter();
        sf.setWorkout(content);
        Workout incoming = sf.getWorkout();
        if (incoming == null) {
            fail("This file is not a Corona Fitness workout.");
            return;
        }

        Intent open = new Intent(this, DetailActivity.class);
        open.putExtra("EXTRA_TITLE", incoming.getTitle());
        open.putExtra("EXTRA_WOD", incoming.getWod());
        open.putExtra("EXTRA_WORKOUT", incoming);
        open.putExtra("EXTRA_WORK_OR_ADD", 2);
        startActivity(open);
        finish();
    }

    private void fail(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        finish();
    }

    private String readUri(Uri uri) {
        StringBuilder sb = new StringBuilder();
        InputStream is = null;
        BufferedReader r = null;
        try {
            is = getContentResolver().openInputStream(uri);
            if (is == null) return null;
            r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;
            while ((line = r.readLine()) != null) sb.append(line);
        } catch (Exception e) {
            Log.e(TAG, "readUri failed", e);
            return null;
        } finally {
            try { if (r != null) r.close(); } catch (Exception ignored) {}
            try { if (is != null) is.close(); } catch (Exception ignored) {}
        }
        return sb.toString();
    }
}
