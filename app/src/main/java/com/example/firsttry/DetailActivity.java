package com.example.firsttry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private TextView title;
    private TextView btnexercise;
    private TextView textime, texttype, description, textsets, textpause;
    private ListView lvexe;
    private ImageView diffIcon;
    private Adapter adapter;
    private Workout work;
    private ArrayList<Exercise> exer;
    private Animation bttone,bttfour,btttwo,ltr;
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

        title.setText(work.getTitle());
        if(new String("TIME").equals(work.getType())) {
            texttype.setText(R.string.type_1);
            description.setText(R.string.type_1_desc);
            type = 1;
        } else if(new String("REPS").equals(work.getType())) {
            texttype.setText(R.string.type_2);
            description.setText(R.string.type_2_desc);
            type = 2;
        } else {
            texttype.setText(R.string.type_3);
            description.setText(R.string.type_3_desc);
            type = 3;
        }

        textime.setText(String.valueOf(exer.size()));
        textsets.setText(String.valueOf(work.getNumberOfSets()));

        String timeLeftText;
        int minutes = (int) work.getTotalTime() / 60;
        int seconds = (int) work.getTotalTime() % 60;
        if(minutes>0) {
            timeLeftText = "" + minutes +"'";
            if (seconds < 10) timeLeftText += "0";
            timeLeftText += seconds + "\"";
        } else {
            timeLeftText = ""+seconds+ "\"";
        }
        textime.setText(timeLeftText);

        if(type == 2) {
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


        int diff = work.getDifficulty();

        if(diff==1) {
            diffIcon.setImageDrawable(getResources().getDrawable(R.drawable.beginner));
        } else if(diff==2) {
            diffIcon.setImageDrawable(getResources().getDrawable(R.drawable.average));
        } else if(diff==3) {
            diffIcon.setImageDrawable(getResources().getDrawable(R.drawable.skilled));
        } else if(diff==4) {
            diffIcon.setImageDrawable(getResources().getDrawable(R.drawable.expert));
        } else {
            diffIcon.setImageDrawable(getResources().getDrawable(R.drawable.spartan));
        }

        List<RowDataDetail> rowData;
        rowData = new ArrayList<RowDataDetail>();
        if(exer.isEmpty()) {
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
                if(new String("TIME").equals(work.getType())) {
                    data.setTime("");
                    data.setPause(String.valueOf(exer.get(i).getTimeInSeconds() + "\"," + exer.get(i).getPauseInSeconds())+"\"");
                } else if (new String("REPS").equals(work.getType())) {
                    data.setTime(String.valueOf("X" + exer.get(i).getReps()));
                    data.setPause("");
                } else {
                    data.setTime(String.valueOf("X" + exer.get(i).getReps()));
                    data.setPause(String.valueOf("in "+ exer.get(i).getTimeInSeconds())+"\"");
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

    public void openTimer(View view) {
        if(!work.getExercises().isEmpty()) {
            if(new String("TIME").equals(work.getType())) {
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
            btnexercise.setText("no exercises in workout");
        }
    }

}