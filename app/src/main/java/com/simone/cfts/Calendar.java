package com.simone.cfts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Calendar extends AppCompatActivity {

    static final String PREF_MONTHLY_GOAL = "calendar.monthly_goal";
    static final int DEFAULT_MONTHLY_GOAL = 22;

    private static final int[] DIFF_COLORS = new int[]{
            0xFF71B62C, 0xFF4EB795, 0xFFD3D91A, 0xFFE5842B, 0xFFE20814
    };
    private static final String[] DIFF_LABELS = new String[]{
            "Beginner", "Average", "Skilled", "Expert", "Spartan"
    };

    private final SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private final SimpleDateFormat monthOfYear = new SimpleDateFormat("MMMM yyyy", Locale.US);
    private final SimpleDateFormat fullDay = new SimpleDateFormat("EEEE, MMMM d", Locale.US);

    private DatabaseHelper db;
    private SharedPreferences prefs;

    private CalendarMonthView monthGrid;
    private TextView monthLabel, selectedDayHeader, monthlyCount;
    private LinearLayout cardsContainer;

    private int year, monthIndex; // monthIndex 1-based
    private int selectedDay;
    private int todayYear, todayMonth, todayDay;

    private Map<Integer, List<int[]>> monthData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        db = DatabaseHelper.getInstance(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        monthGrid         = findViewById(R.id.monthGrid);
        monthLabel        = findViewById(R.id.monthLabel);
        selectedDayHeader = findViewById(R.id.selectedDayHeader);
        monthlyCount      = findViewById(R.id.monthlyCount);
        cardsContainer    = findViewById(R.id.cardsContainer);

        java.util.Calendar today = java.util.Calendar.getInstance();
        todayYear  = today.get(java.util.Calendar.YEAR);
        todayMonth = today.get(java.util.Calendar.MONTH) + 1;
        todayDay   = today.get(java.util.Calendar.DAY_OF_MONTH);

        year = todayYear;
        monthIndex = todayMonth;
        selectedDay = todayDay;

        monthGrid.setOnDayTapListener(new CalendarMonthView.OnDayTapListener() {
            @Override public void onDayTap(int day) {
                selectedDay = day;
                refreshDetails();
            }
        });

        findViewById(R.id.addWorkoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { openPicker(); }
        });

        refresh();
    }

    public void closeCalendar(View v) { finish(); }

    public void openPrevMonth(View v) { shiftMonth(-1); }
    public void openNextMonth(View v) { shiftMonth(+1); }

    private void shiftMonth(int delta) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(year, monthIndex - 1, 1);
        c.add(java.util.Calendar.MONTH, delta);
        year = c.get(java.util.Calendar.YEAR);
        monthIndex = c.get(java.util.Calendar.MONTH) + 1;
        int dim = c.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
        if (selectedDay > dim) selectedDay = dim;
        refresh();
    }

    public void openGoalSettings(View view) {
        View content = LayoutInflater.from(this).inflate(R.layout.popup_goal, null);
        TextView title  = content.findViewById(R.id.goalTitle);
        final EditText field = content.findViewById(R.id.goalField);
        TextView cancel = content.findViewById(R.id.goalCancel);
        TextView ok     = content.findViewById(R.id.goalOk);
        title.setText(R.string.monthly_workout_goal);
        int current = prefs.getInt(PREF_MONTHLY_GOAL, DEFAULT_MONTHLY_GOAL);
        field.setText(String.valueOf(current));
        final PopupWindow pw = showPopup(content);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { hideKeyboard(field); pw.dismiss(); }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                hideKeyboard(field);
                int newGoal = parseOr(field.getText().toString(), DEFAULT_MONTHLY_GOAL);
                prefs.edit().putInt(PREF_MONTHLY_GOAL, newGoal).apply();
                pw.dismiss();
                refreshDetails();
            }
        });
    }

    private void openPicker() {
        final ArrayList<Workout> workouts = db.loadDatabase();
        View content = LayoutInflater.from(this).inflate(R.layout.popup_workout_picker, null);
        TextView title  = content.findViewById(R.id.pickerTitle);
        EditText search = content.findViewById(R.id.pickerSearch);
        TextView cancel = content.findViewById(R.id.pickerCancel);
        final LinearLayout list = content.findViewById(R.id.pickerList);

        title.setText(getString(R.string.add_to_day_format, fullDay.format(currentDayDate())));

        final PopupWindow pw = showPopup(content);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { hideKeyboard(content); pw.dismiss(); }
        });

        if (workouts.isEmpty()) {
            search.setVisibility(View.GONE);
            TextView empty = new TextView(this);
            empty.setText(R.string.picker_empty);
            empty.setTextColor(0xFF606060);
            empty.setTextSize(14);
            empty.setPadding((int) dp(8), (int) dp(16), (int) dp(8), (int) dp(16));
            list.addView(empty);
            return;
        }

        renderPickerList(list, workouts, "", pw);

        search.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(android.text.Editable s) {
                renderPickerList(list, workouts, s.toString(), pw);
            }
        });
    }

    private void renderPickerList(LinearLayout list, ArrayList<Workout> workouts,
                                  String query, final PopupWindow pw) {
        list.removeAllViews();
        String q = query == null ? "" : query.trim().toLowerCase(Locale.US);
        for (final Workout w : workouts) {
            if (!q.isEmpty() && !w.getTitle().toLowerCase(Locale.US).contains(q)) continue;
            View row = buildPickerRow(w);
            row.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    hideKeyboard(v);
                    db.addWorkoutOnDate(currentIsoDate(), w.getID());
                    pw.dismiss();
                    refresh();
                }
            });
            list.addView(row);
        }
    }

    private View buildPickerRow(Workout w) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding((int) dp(6), (int) dp(10), (int) dp(6), (int) dp(10));

        TextView name = new TextView(this);
        name.setText(w.getTitle());
        name.setTextColor(0xFF0D122C);
        name.setTextSize(16);
        LinearLayout.LayoutParams nlp = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        row.addView(name, nlp);

        TextView stars = new TextView(this);
        stars.setText(diffStars(w.getDifficulty()));
        stars.setTextColor(diffColor(w.getDifficulty()));
        stars.setTextSize(14);
        LinearLayout.LayoutParams slp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        slp.setMarginStart((int) dp(8));
        row.addView(stars, slp);

        return row;
    }

    private void refresh() {
        monthLabel.setText(monthOfYear.format(currentMonthFirstDate()));
        monthData = db.loadMonth(year, monthIndex);

        int[] maxDiffPerDay = new int[32];
        for (Map.Entry<Integer, List<int[]>> e : monthData.entrySet()) {
            int day = e.getKey();
            int max = 0;
            for (int[] pair : e.getValue()) {
                Workout w = db.loadWorkoutFromId(pair[1]);
                if (w.getDifficulty() > max) max = w.getDifficulty();
            }
            if (day >= 1 && day < 32) maxDiffPerDay[day] = max;
        }
        monthGrid.setMonth(year, monthIndex, selectedDay, maxDiffPerDay,
                todayYear, todayMonth, todayDay);

        refreshDetails();
    }

    private void refreshDetails() {
        selectedDayHeader.setText(fullDay.format(currentDayDate()));

        cardsContainer.removeAllViews();
        List<int[]> rows = monthData != null ? monthData.get(selectedDay) : null;
        if (rows != null) {
            for (int[] pair : rows) {
                final int rowId = pair[0];
                final int workoutId = pair[1];
                Workout w = db.loadWorkoutFromId(workoutId);
                cardsContainer.addView(buildCard(w, rowId));
            }
        }

        int goal = prefs.getInt(PREF_MONTHLY_GOAL, DEFAULT_MONTHLY_GOAL);
        int total = db.monthlyCount(year, monthIndex);
        monthlyCount.setText(getString(R.string.monthly_count_format, total, goal));
    }

    private View buildCard(final Workout w, final int calendarRowId) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundColor(0xFFFFFFFF);
        card.setPadding((int) dp(14), (int) dp(12), (int) dp(14), (int) dp(12));
        LinearLayout.LayoutParams clp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        clp.setMargins(0, 0, 0, (int) dp(8));
        card.setLayoutParams(clp);

        LinearLayout topRow = new LinearLayout(this);
        topRow.setOrientation(LinearLayout.HORIZONTAL);
        topRow.setGravity(Gravity.CENTER_VERTICAL);

        TextView title = new TextView(this);
        title.setText(w.getTitle());
        title.setTextColor(0xFF0D122C);
        title.setTextSize(18);
        LinearLayout.LayoutParams tlp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        topRow.addView(title, tlp);

        TextView stars = new TextView(this);
        stars.setText(diffStars(w.getDifficulty()));
        stars.setTextColor(diffColor(w.getDifficulty()));
        stars.setTextSize(15);
        LinearLayout.LayoutParams slp = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        slp.setMarginStart((int) dp(8));
        topRow.addView(stars, slp);

        TextView remove = new TextView(this);
        remove.setText("✕");
        remove.setTextColor(0xFF707070);
        remove.setTextSize(18);
        remove.setPadding((int) dp(10), (int) dp(2), (int) dp(2), (int) dp(2));
        remove.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { confirmRemove(w, calendarRowId); }
        });
        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        topRow.addView(remove, rlp);

        card.addView(topRow);

        TextView subtitle = new TextView(this);
        String typeStr = TextUtils.isEmpty(w.getType()) ? "" : w.getType();
        int totalSec = w.getTotalTime();
        String timeStr = totalSec > 0 ? (totalSec / 60) + " min" : "";
        String mid = (typeStr.isEmpty() || timeStr.isEmpty()) ? (typeStr + timeStr) : (typeStr + " · " + timeStr);
        subtitle.setText(mid);
        subtitle.setTextColor(0xFF707070);
        subtitle.setTextSize(13);
        LinearLayout.LayoutParams sublp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        sublp.topMargin = (int) dp(2);
        card.addView(subtitle, sublp);

        card.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { openWorkoutDetail(w); }
        });
        return card;
    }

    private static String diffStars(int diff) {
        if (diff < 1 || diff > 5) return "";
        StringBuilder sb = new StringBuilder(diff);
        for (int i = 0; i < diff; i++) sb.append('★');
        return sb.toString();
    }

    private void openWorkoutDetail(Workout w) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("EXTRA_TITLE", w.getTitle());
        intent.putExtra("EXTRA_WOD", w.getWod());
        intent.putExtra("EXTRA_WORKOUT", w);
        startActivity(intent);
    }

    private void confirmRemove(final Workout w, final int calendarRowId) {
        View content = LayoutInflater.from(this).inflate(R.layout.popup_are_you_sure, null);
        TextView text = content.findViewById(R.id.text_id);
        text.setText(getString(R.string.remove_workout_confirm, w.getTitle()));

        final PopupWindow pw = new PopupWindow(content,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new ColorDrawable(0));
        pw.setAnimationStyle(0);
        pw.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);

        Button yes = content.findViewById(R.id.button_yes);
        Button no  = content.findViewById(R.id.button_no);
        yes.setText("Yes");
        no.setText("No");
        yes.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                db.removeCalendarEntry(calendarRowId);
                pw.dismiss();
                refresh();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { pw.dismiss(); }
        });
    }

    private PopupWindow showPopup(View content) {
        PopupWindow pw = new PopupWindow(content,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new ColorDrawable(0));
        pw.setAnimationStyle(0);
        pw.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
        return pw;
    }

    private void hideKeyboard(View v) {
        android.view.inputmethod.InputMethodManager imm =
                (android.view.inputmethod.InputMethodManager) getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
        if (imm != null && v != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private java.util.Date currentMonthFirstDate() {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(year, monthIndex - 1, 1);
        return c.getTime();
    }

    private java.util.Date currentDayDate() {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(year, monthIndex - 1, Math.max(1, Math.min(selectedDay,
                c.getActualMaximum(java.util.Calendar.DAY_OF_MONTH))));
        return c.getTime();
    }

    private String currentIsoDate() {
        return iso.format(currentDayDate());
    }

    private static int parseOr(String s, int fallback) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return fallback; }
    }

    private static String diffLabel(int diff) {
        if (diff >= 1 && diff <= 5) return DIFF_LABELS[diff - 1];
        return "";
    }

    private static int diffColor(int diff) {
        if (diff >= 1 && diff <= 5) return DIFF_COLORS[diff - 1];
        return 0xFF808080;
    }

    private float dp(float v) { return v * getResources().getDisplayMetrics().density; }
}
