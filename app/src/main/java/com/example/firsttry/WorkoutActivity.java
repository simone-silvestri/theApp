package com.example.firsttry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class WorkoutActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private TextView titlepage, subtitlepage, btexercise;
    private Animation bttone, btttwo, bttthree, ltr;
    private Button btn1, btn2, btn3, btn4, btn5, btnall;
    private Button btn20min, btn40min, btn60min, btn80min, btnallmin;
    private View bgprogress;

    private ListView lv;
    private ArrayList<Workout> wodList;
    private Adapter adapter;
    private int currentTime, currentDiff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btnall = (Button) findViewById(R.id.btnall);

//        btn20min = (Button) findViewById(R.id.btn20min);
//        btn40min = (Button) findViewById(R.id.btn40min);
//        btn60min = (Button) findViewById(R.id.btn60min);
//        btn80min = (Button) findViewById(R.id.btn80min);
//        btnallmin = (Button) findViewById(R.id.btnallmin);

        bttone = AnimationUtils.loadAnimation(this, R.anim.bttone);
        btttwo = AnimationUtils.loadAnimation(this, R.anim.btttwo);
        bttthree = AnimationUtils.loadAnimation(this, R.anim.bttfour);
        ltr = AnimationUtils.loadAnimation(this, R.anim.ltr);

        //import font
//        titlepage = (TextView) findViewById(R.id.titlepage);
//        subtitlepage = (TextView) findViewById(R.id.subtitlepage);
//        btexercise = (TextView) findViewById(R.id.btexercise);
//        bgprogress = (View) findViewById(R.id.bgprogress);

//        titlepage.startAnimation(bttone);
//        subtitlepage.startAnimation(bttone);

        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);

        wodList = dbhandler.loadDatabase();

        Collections.sort(wodList);
        List<RowData> rowData;
        rowData = new ArrayList<RowData>();
        for (int i = 0; i < wodList.size(); i++) {
            RowData data = new RowData();
            String str = wodList.get(i).getTitle();
            data.setTitle(str);
            String type;
            if(new String("TIME").equals(wodList.get(i).getType())) {
                type = "TIME";
            } else if(new String("REPS").equals(wodList.get(i).getType())) {
                type = "REPS";
            } else {
                type = "REPTIME";
            }
            str = "Total time: " + wodList.get(i).getTotalTime()/60 + " mins;   Type: " + type;
            data.setSubtitle(str);
            if (wodList.get(i).getDifficulty() == 1) {
                data.setImageId(R.drawable.beginner);
            } else if (wodList.get(i).getDifficulty() == 2) {
                data.setImageId(R.drawable.average);
            } else if (wodList.get(i).getDifficulty() == 3) {
                data.setImageId(R.drawable.skilled);
            } else if (wodList.get(i).getDifficulty() == 4) {
                data.setImageId(R.drawable.expert);
            } else {
                data.setImageId(R.drawable.spartan);
            }
            rowData.add(data);
        }
        lv = findViewById(R.id.lv);

        MyAdapter adapter = new MyAdapter(this, rowData);
        lv.setAdapter(adapter);
        lv.startAnimation(bttthree);
        lv.setOnItemClickListener(this);

        currentTime = -1;
        currentDiff = -1;

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        Intent intent = new Intent(WorkoutActivity.this, DetailActivity.class);
        String title = wodList.get(pos).getTitle();
        String wod = wodList.get(pos).getWod();

        Workout work = wodList.get(pos);

        intent.putExtra("EXTRA_TITLE", title);
        intent.putExtra("EXTRA_WOD", wod);
        intent.putExtra("EXTRA_WORKOUT", work);
        startActivity(intent);
    }

    public void filterWorkout(View view) {
        btn1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        btn2.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        btn3.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        btn4.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        btn5.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        btnall.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        btn1.setTextColor(getResources().getColor(R.color.gray));
        btn2.setTextColor(getResources().getColor(R.color.gray));
        btn3.setTextColor(getResources().getColor(R.color.gray));
        btn4.setTextColor(getResources().getColor(R.color.gray));
        btn5.setTextColor(getResources().getColor(R.color.gray));
        btnall.setTextColor(getResources().getColor(R.color.gray));

        Button bthere = (Button) view;
        bthere.setBackgroundColor(getResources().getColor(R.color.white));
        bthere.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);

        String btntxt = bthere.getText().toString();
        if (new String("ALL").equals(btntxt)) {
            currentDiff = -1;
            wodList = dbhandler.loadDatabase();
            Collections.sort(wodList);
        } else {
            int diff = Integer.parseInt(bthere.getText().toString());
            currentDiff = diff;
            wodList = dbhandler.loadDatabaseDiff(currentDiff);
        }

        List<RowData> rowData;
        rowData = new ArrayList<RowData>();
        for (int i = 0; i < wodList.size(); i++) {
            RowData data = new RowData();
            String str = wodList.get(i).getTitle();
            data.setTitle(str);
            String type;
            if(new String("TIME").equals(wodList.get(i).getType())) {
                type = "TIME";
            } else if(new String("REPS").equals(wodList.get(i).getType())) {
                type = "REPS";
            } else {
                type = "REPTIME";
            }
            str = "Total time: " + wodList.get(i).getTotalTime()/60 + " mins;   Type: " + type;            data.setSubtitle(str);
            if (wodList.get(i).getDifficulty() == 1) {
                data.setImageId(R.drawable.beginner);
            } else if (wodList.get(i).getDifficulty() == 2) {
                data.setImageId(R.drawable.average);
            } else if (wodList.get(i).getDifficulty() == 3) {
                data.setImageId(R.drawable.skilled);
            } else if (wodList.get(i).getDifficulty() == 4) {
                data.setImageId(R.drawable.expert);
            } else {
                data.setImageId(R.drawable.spartan);
            }
            rowData.add(data);
        }
        lv = findViewById(R.id.lv);

        MyAdapter adapter = new MyAdapter(this, rowData);
        lv.setAdapter(adapter);
    }


    public void filterWorkoutOnTime(View view) {
        // REMEMBER I HAVE CHANGED THE TOTAL TIME TO SECONDS INSTEAD OF MINUTES!!
        btn20min.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        btn40min.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        btn60min.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        btn80min.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        btnallmin.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        btn20min.setTextColor(getResources().getColor(R.color.gray));
        btn40min.setTextColor(getResources().getColor(R.color.gray));
        btn60min.setTextColor(getResources().getColor(R.color.gray));
        btn80min.setTextColor(getResources().getColor(R.color.gray));
        btnallmin.setTextColor(getResources().getColor(R.color.gray));

        Button bthere = (Button) view;
        bthere.setBackgroundColor(getResources().getColor(R.color.white));
        bthere.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);
        wodList = dbhandler.loadDatabase();
        if (currentDiff == -1) {
            //nothing to be done
        } else {
            ArrayList<Workout> wtemp = new ArrayList<>();
            for (int i = 0; i < wodList.size(); i++) {
                if (wodList.get(i).getDifficulty() == currentDiff) {
                    wtemp.add(wodList.get(i));
                }
            }
            wodList.clear();
            wodList = wtemp;
        }

        String btntxt = bthere.getText().toString();
        if (new String("ALL").equals(btntxt)) {
            Collections.sort(wodList);
            currentTime = -1;
            //nothing is done
        } else if (new String("60+").equals(btntxt)) {
            ArrayList<Workout> wtemp1 = new ArrayList<>();
            currentTime = 80;
            for (int i = 0; i < wodList.size(); i++) {
                if (wodList.get(i).getTotalTime()/60 > currentTime - 20) {
                    wtemp1.add(wodList.get(i));
                }
            }
            wodList.clear();
            wodList = wtemp1;
            Collections.sort(wodList);
        } else {

            int time = Integer.parseInt(bthere.getText().toString());
            currentTime = time;
            ArrayList<Workout> wtemp1 = new ArrayList<>();
            for (int i = 0; i < wodList.size(); i++) {
                if (wodList.get(i).getTotalTime()/60 > currentTime - 20 && wodList.get(i).getTotalTime()/60 <= currentTime) {
                    wtemp1.add(wodList.get(i));
                }
            }
            wodList.clear();
            wodList = wtemp1;
            Collections.sort(wodList);
        }

        List<RowData> rowData;
        rowData = new ArrayList<RowData>();
        for (int i = 0; i < wodList.size(); i++) {
            RowData data = new RowData();
            String str = wodList.get(i).getTitle();
            data.setTitle(str);
            String type;
            if(new String("TIME").equals(wodList.get(i).getType())) {
                type = "TIME";
            } else if(new String("REPS").equals(wodList.get(i).getType())) {
                type = "REPS";
            } else {
                type = "REPTIME";
            }
            str = "Total time: " + wodList.get(i).getTotalTime() + " mins;   Type: " + type;            data.setSubtitle(str);            data.setSubtitle(str);
            if (wodList.get(i).getDifficulty() == 1) {
                data.setImageId(R.drawable.beginner);
            } else if (wodList.get(i).getDifficulty() == 2) {
                data.setImageId(R.drawable.average);
            } else if (wodList.get(i).getDifficulty() == 3) {
                data.setImageId(R.drawable.skilled);
            } else if (wodList.get(i).getDifficulty() == 4) {
                data.setImageId(R.drawable.expert);
            } else {
                data.setImageId(R.drawable.spartan);
            }
            rowData.add(data);
        }
        lv = findViewById(R.id.lv);

        MyAdapter adapter = new MyAdapter(this, rowData);
        lv.setAdapter(adapter);
    }
}