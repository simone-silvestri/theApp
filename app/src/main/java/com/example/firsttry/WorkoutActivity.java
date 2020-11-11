package com.example.firsttry;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class WorkoutActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private TextView titlepage, subtitlepage, btexercise;
    private Animation bttone, btttwo, bttthree, ltr;
    private ImageView btn1, btn2, btn3, btn4, btn5, btnall;
    private SearchView workoutSearch;

    private int deletePosition;
    private ListView lv;
    private ArrayList<Workout> wodList;
    private int currentTime, currentDiff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        btn1 = (ImageView) findViewById(R.id.btn1);
        btn2 = (ImageView) findViewById(R.id.btn2);
        btn3 = (ImageView) findViewById(R.id.btn3);
        btn4 = (ImageView) findViewById(R.id.btn4);
        btn5 = (ImageView) findViewById(R.id.btn5);
        btnall = (ImageView) findViewById(R.id.btnall);

        bttone = AnimationUtils.loadAnimation(this, R.anim.bttone);
        btttwo = AnimationUtils.loadAnimation(this, R.anim.btttwo);
        bttthree = AnimationUtils.loadAnimation(this, R.anim.bttfour);
        ltr = AnimationUtils.loadAnimation(this, R.anim.ltr);

        workoutSearch = findViewById(R.id.workout_search);
        int id = workoutSearch.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) workoutSearch.findViewById(id);

        textView.setTextColor(getResources().getColor(R.color.gray));
        textView.setHintTextColor(getResources().getColor(R.color.gray));

        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);
        wodList = dbhandler.loadDatabase();

        Collections.sort(wodList);
        final List<RowDataComplete> rowData;
        rowData = new ArrayList<RowDataComplete>();
        for (int i = 0; i < wodList.size(); i++) {
            RowDataComplete data = new RowDataComplete();
            String str = wodList.get(i).getTitle();
            data.setTitle(str);
            String type;
            if (new String("TIME").equals(wodList.get(i).getType())) {
                type = "H.I.I.T. workout";
                data.setImageType(R.drawable.time);
            } else if (new String("REPS").equals(wodList.get(i).getType())) {
                type = "Reps workout";
                data.setImageType(R.drawable.reps);
            } else {
                type = "Reps in time";
                data.setImageType(R.drawable.reptime);
            }
            String timeLeftText;
            int minutes = (int) wodList.get(i).getTotalTime() / 60;
            int seconds = (int) wodList.get(i).getTotalTime() % 60;
            if (minutes > 0) {
                timeLeftText = "" + minutes + "'";
                if (seconds < 10) timeLeftText += "0";
                timeLeftText += seconds + "\"";
            } else {
                timeLeftText = "" + seconds + "\"";
            }
            str = "Time: " + timeLeftText;
            data.setSubtitle(str);
            data.setType(type);
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

        final MyAdapterComplete adapter = new MyAdapterComplete(this, rowData);
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
                text.setText("What do you want to do?");

                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                final PopupWindow puWindow = new PopupWindow(puView, height, width, true);
                puWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                puWindow.setAnimationStyle(R.style.Animation);

                Button btnYes = (Button) puView.findViewById(R.id.button_yes);
                btnYes.setText("Delete");
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
                btnNo.setText("Modify");
                btnNo.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Workout work = wodList.get(deletePosition);
                        Intent intent = new Intent(WorkoutActivity.this, ModifyWorkoutActivity.class);
                        intent.putExtra("EXTRA_WORKOUT", work);
                        startActivity(intent);
                        puWindow.dismiss();
                    }
                });
                return true;
            }
        });


        currentTime = -1;
        currentDiff = -1;

        workoutSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                workoutSearch.post(new Runnable() {
                    @Override
                    public void run() {
                        workoutSearch.clearFocus();
                    }
                });

                filterByName(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

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
        intent.putExtra("EXTRA_WORK_OR_ADD", 0);

        startActivity(intent);
    }

    public void filterByName(String query) {

        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);

        ArrayList<Workout> wdtemp = new ArrayList<>();
        wdtemp = dbhandler.loadDatabase();
        wodList.clear();

        if (!wdtemp.isEmpty()) {
            for (int i = 0; i < wdtemp.size(); i++) {
                if (containsIgnoreCase(wdtemp.get(i).getTitle(),query)) {
                    wodList.add(wdtemp.get(i));
                }
            }
        }


        final List<RowDataComplete> rowData;
        rowData = new ArrayList<RowDataComplete>();
        for (int i = 0; i < wodList.size(); i++) {
            RowDataComplete data = new RowDataComplete();
            String str = wodList.get(i).getTitle();
            data.setTitle(str);
            String type;
            if (new String("TIME").equals(wodList.get(i).getType())) {
                type = "H.I.I.T. workout";
                data.setImageType(R.drawable.time);
            } else if (new String("REPS").equals(wodList.get(i).getType())) {
                type = "Reps workout";
                data.setImageType(R.drawable.reps);
            } else {
                type = "Reps in time";
                data.setImageType(R.drawable.reptime);
            }
            String timeLeftText;
            int minutes = (int) wodList.get(i).getTotalTime() / 60;
            int seconds = (int) wodList.get(i).getTotalTime() % 60;
            if (minutes > 0) {
                timeLeftText = "" + minutes + "'";
                if (seconds < 10) timeLeftText += "0";
                timeLeftText += seconds + "\"";
            } else {
                timeLeftText = "" + seconds + "\"";
            }
            str = "Time: " + timeLeftText;
            data.setSubtitle(str);
            data.setType(type);
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

        final MyAdapterComplete adapter = new MyAdapterComplete(this, rowData);
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
                text.setText("What do you want to do?");

                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                final PopupWindow puWindow = new PopupWindow(puView, height, width, true);
                puWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                puWindow.setAnimationStyle(R.style.Animation);

                Button btnYes = (Button) puView.findViewById(R.id.button_yes);
                btnYes.setText("Delete");
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
                btnNo.setText("Modify");
                btnNo.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Workout work = wodList.get(deletePosition);
                        Intent intent = new Intent(WorkoutActivity.this, ModifyWorkoutActivity.class);
                        intent.putExtra("EXTRA_WORKOUT", work);
                        startActivity(intent);
                        puWindow.dismiss();
                    }
                });
                return true;
            }
        });
    }


    public void filterWorkout(View view) {
        btn1.setImageResource(R.drawable.star_white);
        btn2.setImageResource(R.drawable.star_white);
        btn3.setImageResource(R.drawable.star_white);
        btn4.setImageResource(R.drawable.star_white);
        btn5.setImageResource(R.drawable.star_white);

        LinearLayout r = (LinearLayout) view.getParent();
        int idx = r.indexOfChild(view);

        currentDiff = idx;
        if(currentDiff==1) {
            btn1.setImageResource(R.drawable.star_beginner);
        } else if (currentDiff==2) {
            btn1.setImageResource(R.drawable.star_average);
            btn2.setImageResource(R.drawable.star_average);
        } else if (currentDiff==3) {
            btn1.setImageResource(R.drawable.star_skilled);
            btn2.setImageResource(R.drawable.star_skilled);
            btn3.setImageResource(R.drawable.star_skilled);
        } else if (currentDiff==4) {
            btn1.setImageResource(R.drawable.star_expert);
            btn2.setImageResource(R.drawable.star_expert);
            btn3.setImageResource(R.drawable.star_expert);
            btn4.setImageResource(R.drawable.star_expert);
        } else if (currentDiff==5) {
            btn1.setImageResource(R.drawable.star_spartan);
            btn2.setImageResource(R.drawable.star_spartan);
            btn3.setImageResource(R.drawable.star_spartan);
            btn4.setImageResource(R.drawable.star_spartan);
            btn5.setImageResource(R.drawable.star_spartan);
        } else {
            currentDiff = -1;
        }

        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);

        if (currentDiff==-1) {
            wodList = dbhandler.loadDatabase();
            Collections.sort(wodList);
        } else {
            wodList = dbhandler.loadDatabaseDiff(currentDiff);
        }

        final List<RowDataComplete> rowData;
        rowData = new ArrayList<RowDataComplete>();
        for (int i = 0; i < wodList.size(); i++) {
            RowDataComplete data = new RowDataComplete();
            String str = wodList.get(i).getTitle();
            data.setTitle(str);
            String type;
            if (new String("TIME").equals(wodList.get(i).getType())) {
                type = "H.I.I.T. workout";
                data.setImageType(R.drawable.time);
            } else if (new String("REPS").equals(wodList.get(i).getType())) {
                type = "Reps workout";
                data.setImageType(R.drawable.reps);
            } else {
                type = "Reps in time";
                data.setImageType(R.drawable.reptime);
            }
            String timeLeftText;
            int minutes = (int) wodList.get(i).getTotalTime() / 60;
            int seconds = (int) wodList.get(i).getTotalTime() % 60;
            if (minutes > 0) {
                timeLeftText = "" + minutes + "'";
                if (seconds < 10) timeLeftText += "0";
                timeLeftText += seconds + "\"";
            } else {
                timeLeftText = "" + seconds + "\"";
            }
            str = "Time: " + timeLeftText;
            data.setSubtitle(str);
            data.setType(type);
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

        final MyAdapterComplete adapter = new MyAdapterComplete(this, rowData);
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
                text.setText("What do you want to do?");

                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                final PopupWindow puWindow = new PopupWindow(puView, height, width, true);
                puWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                puWindow.setAnimationStyle(R.style.Animation);

                Button btnYes = (Button) puView.findViewById(R.id.button_yes);
                btnYes.setText("Delete");
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
                btnNo.setText("Modify");
                btnNo.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Workout work = wodList.get(deletePosition);
                        Intent intent = new Intent(WorkoutActivity.this, ModifyWorkoutActivity.class);
                        intent.putExtra("EXTRA_WORKOUT", work);
                        startActivity(intent);
                        puWindow.dismiss();
                    }
                });
                return true;
            }
        });
    }

    public static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }


}