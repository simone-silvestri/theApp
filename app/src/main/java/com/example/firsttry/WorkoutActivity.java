package com.example.firsttry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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

    private int deletePosition;
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
        final List<RowData> rowData;
        rowData = new ArrayList<RowData>();
        for (int i = 0; i < wodList.size(); i++) {
            RowData data = new RowData();
            String str = wodList.get(i).getTitle();
            data.setTitle(str);
            String type;
            if (new String("TIME").equals(wodList.get(i).getType())) {
                type = "TIME";
            } else if (new String("REPS").equals(wodList.get(i).getType())) {
                type = "REPS";
            } else {
                type = "REPTIME";
            }
            str = "Total time: " + wodList.get(i).getTotalTime() / 60 + " mins;   Type: " + type;
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

        final MyAdapter adapter = new MyAdapter(this, rowData);
        lv.setAdapter(adapter);
        lv.startAnimation(bttthree);
        lv.setOnItemClickListener(this);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                deletePosition = position;
                View puView = layoutInflater.inflate(R.layout.popup_are_you_sure, null);
                puView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.popup_show));

                final TextView text = (TextView) puView.findViewById(R.id.text_id);
                text.setText("Delete the workout from database?");

                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                final PopupWindow puWindow = new PopupWindow(puView, height, width, true);
                puWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                puWindow.setAnimationStyle(R.style.Animation);

                Button btnYes = (Button) puView.findViewById(R.id.button_yes);
                btnYes.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseHelper dbhandler = DatabaseHelper.getInstance(WorkoutActivity.this);
                        Workout work = wodList.get(deletePosition);
                        int id = dbhandler.deleteWorkout(work);
                        rowData.remove(deletePosition);
                        adapter.notifyDataSetChanged();
                          // TODO Auto-generated method stub
                        Toast.makeText(WorkoutActivity.this, "Workout Deleted", Toast.LENGTH_SHORT).show();
                        puWindow.dismiss();
                    }
                });
                Button btnNo = (Button) puView.findViewById(R.id.button_no);
                btnNo.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        puWindow.dismiss();
                    }
                });
//                puWindow.showAsDropDown(pubtnadd, 0, 0);
                return true;
            }
        });


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

        final List<RowData> rowData;
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

        final MyAdapter adapter = new MyAdapter(this, rowData);
        lv.setAdapter(adapter);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                deletePosition = position;
                View puView = layoutInflater.inflate(R.layout.popup_are_you_sure, null);
                puView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.popup_show));

                TextView text = (TextView) puView.findViewById(R.id.text_id);
                text.setText("Delete the workout from database?");

                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                final PopupWindow puWindow = new PopupWindow(puView, height, width, true);
                puWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                puWindow.setAnimationStyle(R.style.Animation);

                Button btnYes = (Button) puView.findViewById(R.id.button_yes);
                btnYes.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseHelper dbhandler = DatabaseHelper.getInstance(WorkoutActivity.this);
                        Workout work = wodList.get(deletePosition);
                        dbhandler.deleteWorkout(work);
                        rowData.remove(deletePosition);
                        adapter.notifyDataSetChanged();
                        // TODO Auto-generated method stub
                        Toast.makeText(WorkoutActivity.this, "Workout Deleted", Toast.LENGTH_SHORT).show();
                        puWindow.dismiss();
                    }
                });
                Button btnNo = (Button) puView.findViewById(R.id.button_no);
                btnNo.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        puWindow.dismiss();
                    }
                });
//                puWindow.showAsDropDown(pubtnadd, 0, 0);
                return true;
            }
        });
    }
}