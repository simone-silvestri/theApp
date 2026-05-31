package com.simone.cfts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ImageView btn1, btn2, btn3, btn4, btn5;
    private SearchView workoutSearch;

    private int deletePosition;
    private ListView lv;
    private ArrayList<Workout> wodList;
    private int currentDiff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        ImageButton buttonBack = findViewById(R.id.btnBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);

        Animation bttthree = AnimationUtils.loadAnimation(this, R.anim.bttfour);

        workoutSearch = findViewById(R.id.workout_search);

        int id = workoutSearch.getContext().getResources().getIdentifier("android:id/search_button", null, null);
        ImageView searchIcon = workoutSearch.findViewById(id);
        searchIcon.setImageResource(R.drawable.ic_baseline_search_24);

        id = workoutSearch.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = workoutSearch.findViewById(id);

        textView.setTextColor(getResources().getColor(R.color.gray));
        textView.setHintTextColor(getResources().getColor(R.color.gray));

        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);
        wodList = dbhandler.loadDatabase();
        Collections.sort(wodList);

        lv = findViewById(R.id.lv);
        refreshList();
        lv.startAnimation(bttthree);
        lv.setOnItemClickListener(this);

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

    private List<RowDataComplete> buildRowData() {
        List<RowDataComplete> rowData = new ArrayList<>();
        for (int i = 0; i < wodList.size(); i++) {
            RowDataComplete data = new RowDataComplete();
            Workout w = wodList.get(i);
            data.setTitle(w.getTitle());

            String type;
            if ("TIME".equals(w.getType())) {
                type = "H.I.I.T. workout";
                data.setImageType(R.drawable.time);
            } else if ("REPS".equals(w.getType())) {
                type = "Reps workout";
                data.setImageType(R.drawable.reps);
            } else {
                type = "Reps in time";
                data.setImageType(R.drawable.reptime);
            }

            int minutes = w.getTotalTime() / 60;
            int seconds = w.getTotalTime() % 60;
            String timeLeftText;
            if (minutes > 0) {
                timeLeftText = "" + minutes + "'";
                if (seconds < 10) timeLeftText += "0";
                timeLeftText += seconds + "\"";
            } else {
                timeLeftText = "" + seconds + "\"";
            }
            data.setSubtitle("Time: " + timeLeftText);
            data.setType(type);
            data.setImageId(DifficultyHelper.getIconResource(w.getDifficulty()));
            rowData.add(data);
        }
        return rowData;
    }

    private void refreshList() {
        final List<RowDataComplete> rowData = buildRowData();
        final MyAdapterComplete adapter = new MyAdapterComplete(this, rowData);
        lv.setAdapter(adapter);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                deletePosition = position;
                showWorkoutActionPopup(view, rowData, adapter);
                return true;
            }
        });
    }

    private void showWorkoutActionPopup(View anchor, final List<RowDataComplete> rowData, final MyAdapterComplete adapter) {
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View puView = layoutInflater.inflate(R.layout.popup_are_you_sure, null);
        puView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.popup_show));

        TextView text = puView.findViewById(R.id.text_id);
        text.setText("What do you want to do?");

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow puWindow = new PopupWindow(puView, height, width, true);
        puWindow.showAtLocation(anchor, Gravity.CENTER, 0, 0);
        puWindow.setAnimationStyle(R.style.Animation);

        TextView btnYes = puView.findViewById(R.id.button_yes);
        btnYes.setText("DELETE");
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbhandler = DatabaseHelper.getInstance(WorkoutActivity.this);
                Workout work = wodList.get(deletePosition);
                int delId = work.getID();
                dbhandler.deleteWorkout(work);
                SyncManager.get(getApplicationContext()).notifyWorkoutDelete(delId);
                rowData.remove(deletePosition);
                adapter.notifyDataSetChanged();
                Toast.makeText(WorkoutActivity.this, "Workout Deleted", Toast.LENGTH_SHORT).show();
                puWindow.dismiss();
            }
        });
        TextView btnNo = puView.findViewById(R.id.button_no);
        btnNo.setText("MODIFY");
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Workout work = wodList.get(deletePosition);
                Intent intent = new Intent(WorkoutActivity.this, ModifyWorkoutActivity.class);
                intent.putExtra("EXTRA_WORKOUT", work);
                startActivity(intent);
                puWindow.dismiss();
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
        ArrayList<Workout> wdtemp = dbhandler.loadDatabase();
        wodList.clear();

        if (!wdtemp.isEmpty()) {
            for (int i = 0; i < wdtemp.size(); i++) {
                if (containsIgnoreCase(wdtemp.get(i).getTitle(), query)) {
                    wodList.add(wdtemp.get(i));
                }
            }
        }

        refreshList();
    }

    public void filterWorkout(View view) {
        btn1.setImageResource(R.drawable.star_white);
        btn2.setImageResource(R.drawable.star_white);
        btn3.setImageResource(R.drawable.star_white);
        btn4.setImageResource(R.drawable.star_white);
        btn5.setImageResource(R.drawable.star_white);

        int id = view.getId();
        int idx;
        if      (id == R.id.btn1) idx = 1;
        else if (id == R.id.btn2) idx = 2;
        else if (id == R.id.btn3) idx = 3;
        else if (id == R.id.btn4) idx = 4;
        else if (id == R.id.btn5) idx = 5;
        else                      idx = 6; // btnall

        currentDiff = idx;
        if (currentDiff == 1) {
            btn1.setImageResource(R.drawable.star_beginner);
        } else if (currentDiff == 2) {
            btn1.setImageResource(R.drawable.star_average);
            btn2.setImageResource(R.drawable.star_average);
        } else if (currentDiff == 3) {
            btn1.setImageResource(R.drawable.star_skilled);
            btn2.setImageResource(R.drawable.star_skilled);
            btn3.setImageResource(R.drawable.star_skilled);
        } else if (currentDiff == 4) {
            btn1.setImageResource(R.drawable.star_expert);
            btn2.setImageResource(R.drawable.star_expert);
            btn3.setImageResource(R.drawable.star_expert);
            btn4.setImageResource(R.drawable.star_expert);
        } else if (currentDiff == 5) {
            btn1.setImageResource(R.drawable.star_spartan);
            btn2.setImageResource(R.drawable.star_spartan);
            btn3.setImageResource(R.drawable.star_spartan);
            btn4.setImageResource(R.drawable.star_spartan);
            btn5.setImageResource(R.drawable.star_spartan);
        } else {
            currentDiff = -1;
        }

        DatabaseHelper dbhandler = DatabaseHelper.getInstance(this);
        if (currentDiff == -1) {
            wodList = dbhandler.loadDatabase();
            Collections.sort(wodList);
        } else {
            wodList = dbhandler.loadDatabaseDiff(currentDiff);
        }

        refreshList();
    }

    public static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true;

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }
}
