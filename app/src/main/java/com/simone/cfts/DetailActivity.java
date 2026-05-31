package com.simone.cfts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private TextView title;
    private int workoradd;
    private TextView btnexercise;
    private TextView textime, texttype, description, textsets, textpause;
    private ListView lvexe;
    private ImageView diffIcon, typeIcon;
    private Adapter adapter;
    private Workout work;
    private ArrayList<Exercise> exer;
    private Animation bttone, bttfour, btttwo, ltr;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        bttone = AnimationUtils.loadAnimation(this, R.anim.bttone);
        btttwo = AnimationUtils.loadAnimation(this, R.anim.btttwo);
        bttfour = AnimationUtils.loadAnimation(this, R.anim.bttfour);
        ltr = AnimationUtils.loadAnimation(this, R.anim.ltr);
        btnexercise = findViewById(R.id.btexercise);

        title = findViewById(R.id.title_tv);
        diffIcon = findViewById(R.id.difficultyicon);
        typeIcon = findViewById(R.id.type);
        description = findViewById(R.id.Description);
        texttype = findViewById(R.id.text_type);
        textime = findViewById(R.id.text_time);
        textsets = findViewById(R.id.text_sets);
        textpause = findViewById(R.id.text_pause);

        work = new Workout();
        work = (Workout) getIntent().getSerializableExtra("EXTRA_WORKOUT");

        exer = new ArrayList<>();
        exer = work.getExercises();

        Bundle extra = getIntent().getExtras();
        workoradd = getIntent().getIntExtra("EXTRA_WORK_OR_ADD",0);
        if(workoradd==1) {
            btnexercise.setText("ADD TO LIBRARY");
            ImageView sendbutton = findViewById(R.id.sendbutton);
            sendbutton.setVisibility(View.INVISIBLE);
        } else if (workoradd==2) {
            btnexercise.setVisibility(View.GONE);
            View importRow = findViewById(R.id.importButtonRow);
            importRow.setVisibility(View.VISIBLE);
            ImageView sendbutton = findViewById(R.id.sendbutton);
            sendbutton.setVisibility(View.INVISIBLE);
            findViewById(R.id.importDiscard).setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { finish(); }
            });
            findViewById(R.id.importConfirm).setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { confirmImport(); }
            });
        }


        title.setText(work.getTitle());
        if (new String("TIME").equals(work.getType())) {
            texttype.setText(R.string.type_1);
            description.setText(R.string.type_1_desc_2);
            type = 1;
            typeIcon.setImageResource(R.drawable.time);
        } else if (new String("REPS").equals(work.getType())) {
            texttype.setText(R.string.type_2);
            description.setText(R.string.type_2_desc_2);
            type = 2;
            typeIcon.setImageResource(R.drawable.reps);
        } else {
            texttype.setText(R.string.type_3);
            description.setText(R.string.type_3_desc_2);
            type = 3;
            typeIcon.setImageResource(R.drawable.reptime);
        }

        textime.setText(String.valueOf(exer.size()));
        textsets.setText(String.valueOf(work.getNumberOfSets()));

        String timeLeftText;
        int minutes = (int) work.getTotalTime() / 60;
        int seconds = (int) work.getTotalTime() % 60;
        if (minutes > 0) {
            timeLeftText = "" + minutes + "'";
            if (seconds < 10) timeLeftText += "0";
            timeLeftText += seconds + "\"";
        } else {
            timeLeftText = "" + seconds + "\"";
        }
        textime.setText(timeLeftText);

        if (type == 2) {
            timeLeftText = "-";
        } else {
            minutes = (int) work.getSetPause() / 60;
            seconds = (int) work.getSetPause() % 60;
            if (minutes > 0) {
                timeLeftText = "" + minutes + "'";
                if (seconds < 10) timeLeftText += "0";
                timeLeftText += seconds + "\"";
            } else {
                timeLeftText = "" + seconds + "\"";
            }
        }
        textpause.setText(timeLeftText);


        diffIcon.setImageResource(DifficultyHelper.getIconResource(work.getDifficulty()));

        List<RowDataDetail> rowData;
        rowData = new ArrayList<RowDataDetail>();
        if (exer.isEmpty()) {
            RowDataDetail data = new RowDataDetail();
            data.setTitle("No Exercises, something is wrong");
            data.setTime("");
            data.setPause("");
            rowData.add(data);
        } else {
            for (int i = 0; i < exer.size(); i++) {
                RowDataDetail data = new RowDataDetail();
                data.setTitle(exer.get(i).getName());
                //Do different stuff based on the workout type
                if (new String("TIME").equals(work.getType())) {
                    data.setTime("");
                    data.setPause(String.valueOf(exer.get(i).getTimeInSeconds() + "\", " + exer.get(i).getPauseInSeconds()) + "\"");
                } else if (new String("REPS").equals(work.getType())) {
                    data.setTime(String.valueOf("X" + exer.get(i).getReps()));
                    data.setPause("");
                } else {
                    data.setTime(String.valueOf("X" + exer.get(i).getReps()));
                    data.setPause(String.valueOf("in " + exer.get(i).getTimeInSeconds()) + "\"");
                }
                rowData.add(data);
            }
        }
        lvexe = findViewById(R.id.lvexe);

        MyAdapterDetail adapter = new MyAdapterDetail(this, rowData);
        lvexe.setAdapter(adapter);

        title.startAnimation(bttone);
        lvexe.startAnimation(bttfour);
        lvexe.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        Intent intent = new Intent(DetailActivity.this, ExerciseActivity.class);
        String exeName = exer.get(pos).getName();

        intent.putExtra("EXTRA_NAME", exeName);
        startActivity(intent);
    }

    public void closeDetail(View view) { finish(); }

    public void openTimer(View view) {
        if(workoradd==0) {
            if (!work.getExercises().isEmpty()) {
                if (new String("TIME").equals(work.getType())) {
                    Intent intent = new Intent(this, TimerActivity.class);
                    intent.putExtra("EXTRA_WORKOUT", work);
                    startActivity(intent);
                } else if (new String("REPS").equals(work.getType())) {
                    Intent intent = new Intent(this, RepsActivity.class);
                    intent.putExtra("EXTRA_WORKOUT", work);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, RepsInTimeActivity.class);
                    intent.putExtra("EXTRA_WORKOUT", work);
                    startActivity(intent);
                }
            } else {
                btnexercise.setText("NO EXERCISES IN WORKOUT");
            }
        } else {
            DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);

            ArrayList<Exercise> exeList = new ArrayList<>();
            int id = dbhandler.loadWorkoutId(work.getTitle());
            if(id==-1) {
                id = (int) dbhandler.addOrUpdateWorkout(work);
                work.setID(id);
                dbhandler.removeExercises(work);
                exeList = work.getExercises();
                for (int j = 0; j < exeList.size(); j++) {
                    dbhandler.addExerciseInWorkout(exeList.get(j), work);
                }
                SyncManager.get(getApplicationContext()).notifyWorkoutUpsert(work);
                btnexercise.setText("ADDED");
            } else {
                btnexercise.setText("ALREADY IN LIBRARY");
            }
        }
    }

    private void confirmImport() {
        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);
        int id = (int) dbhandler.addOrUpdateWorkout(work);
        work.setID(id);
        dbhandler.removeExercises(work);
        for (Exercise e : work.getExercises()) dbhandler.addExerciseInWorkout(e, work);
        SyncManager.get(getApplicationContext()).notifyWorkoutUpsert(work);
        Toast.makeText(this, "Workout imported", Toast.LENGTH_SHORT).show();
        Intent open = new Intent(this, WorkoutActivity.class);
        open.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(open);
        finish();
    }

    public void sendWorkout(View view) {
        StringFormatter stringFormatter = new StringFormatter();
        stringFormatter.setContent(work);

        try {
            java.io.File shareDir = new java.io.File(getCacheDir(), "share");
            if (!shareDir.exists()) shareDir.mkdirs();

            String safeTitle = work.getTitle() == null
                    ? "workout"
                    : work.getTitle().replaceAll("[^A-Za-z0-9 _\\-]", "_");
            java.io.File outFile = new java.io.File(shareDir, safeTitle + ".cfts");

            java.io.FileOutputStream fos = new java.io.FileOutputStream(outFile);
            fos.write(stringFormatter.getContent().getBytes(java.nio.charset.StandardCharsets.UTF_8));
            fos.close();

            android.net.Uri uri = androidx.core.content.FileProvider.getUriForFile(
                    this, "com.simone.cfts.fileprovider", outFile);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/octet-stream");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share workout"));
        } catch (Exception e) {
            android.util.Log.e("DetailActivity", "Failed to share workout", e);
            android.widget.Toast.makeText(this, "Could not share workout",
                    android.widget.Toast.LENGTH_SHORT).show();
        }
    }

}