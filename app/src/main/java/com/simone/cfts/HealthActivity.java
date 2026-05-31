package com.simone.cfts;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HealthActivity extends AppCompatActivity {

    static final String PREF_GOAL = "health.daily_goal_kcal";
    static final int DEFAULT_GOAL = 2000;

    private DatabaseHelper db;
    private SharedPreferences prefs;
    private final SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private final SimpleDateFormat dayNumber = new SimpleDateFormat("d", Locale.US);
    private final SimpleDateFormat weekOfLabel = new SimpleDateFormat("MMM d, yyyy", Locale.US);

    private String mondayIso;
    private int selectedIndex;
    private CaloriesHistogramView histogram;
    private WeeklyStripView weeklyStrip;
    private TextView totalKcal;
    private TextView weekOfTextView;
    private TextView weeklyAvg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        db = DatabaseHelper.getInstance(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        histogram      = findViewById(R.id.histogram);
        weeklyStrip    = findViewById(R.id.weeklyStrip);
        totalKcal      = findViewById(R.id.totalKcal);
        weekOfTextView = findViewById(R.id.weekOfLabel);
        weeklyAvg      = findViewById(R.id.weeklyAvg);

        Calendar today = Calendar.getInstance();
        mondayIso = iso.format(mondayOfWeek(today).getTime());
        selectedIndex = dayOfWeekIndex(today);

        weeklyStrip.setOnDayTapListener(new WeeklyStripView.OnDayTapListener() {
            @Override public void onDayTap(int dayIndex) {
                selectedIndex = dayIndex;
                refresh();
            }
        });

        findViewById(R.id.chipBreakfast).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { openMealEntry(DatabaseHelper.MEAL_BREAKFAST); }
        });
        findViewById(R.id.chipLunch).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { openMealEntry(DatabaseHelper.MEAL_LUNCH); }
        });
        findViewById(R.id.chipDinner).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { openMealEntry(DatabaseHelper.MEAL_DINNER); }
        });
        findViewById(R.id.chipExtra).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { openMealEntry(DatabaseHelper.MEAL_EXTRA); }
        });

        refresh();
    }

    public void closeHealth(View view) { finish(); }

    public void openPrevWeek(View view) {
        mondayIso = shiftWeek(mondayIso, -1);
        refresh();
    }

    public void openNextWeek(View view) {
        mondayIso = shiftWeek(mondayIso, +1);
        refresh();
    }

    public void openGoalSettings(View view) {
        View content = LayoutInflater.from(this).inflate(R.layout.popup_goal, null);
        TextView title  = content.findViewById(R.id.goalTitle);
        final EditText field = content.findViewById(R.id.goalField);
        TextView cancel = content.findViewById(R.id.goalCancel);
        TextView ok     = content.findViewById(R.id.goalOk);
        title.setText(R.string.daily_kcal_goal);
        int currentGoal = prefs.getInt(PREF_GOAL, DEFAULT_GOAL);
        field.setText(String.valueOf(currentGoal));
        final PopupWindow pw = showPopup(content);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                hideKeyboard(field);
                pw.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                hideKeyboard(field);
                int newGoal = parseOr(field.getText().toString(), DEFAULT_GOAL);
                prefs.edit().putInt(PREF_GOAL, newGoal).apply();
                int monthly = prefs.getInt(com.simone.cfts.Calendar.PREF_MONTHLY_GOAL,
                        com.simone.cfts.Calendar.DEFAULT_MONTHLY_GOAL);
                SyncManager.get(getApplicationContext()).notifyGoalsChanged(newGoal, monthly);
                pw.dismiss();
                refresh();
            }
        });
    }

    private void openMealEntry(final int meal) {
        View content = LayoutInflater.from(this).inflate(R.layout.popup_meal_entry, null);
        TextView name = content.findViewById(R.id.popupMealName);
        final EditText field = content.findViewById(R.id.popupKcalField);
        TextView clear = content.findViewById(R.id.popupClear);
        TextView save  = content.findViewById(R.id.popupSave);

        name.setText(mealLabel(meal));
        name.setTextColor(mealColor(meal));

        int[] day = db.getDayKcal(selectedIsoDate());
        if (day[meal] > 0) field.setText(String.valueOf(day[meal]));
        field.requestFocus();

        final PopupWindow pw = showPopup(content);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                hideKeyboard(field);
                String d = selectedIsoDate();
                db.clearMealKcal(d, meal);
                SyncManager.get(getApplicationContext()).notifyMealClear(d, meal);
                pw.dismiss();
                refresh();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                hideKeyboard(field);
                int kcal = parseOr(field.getText().toString(), 0);
                String d = selectedIsoDate();
                if (kcal > 0) {
                    db.setMealKcal(d, meal, kcal);
                    SyncManager.get(getApplicationContext()).notifyMealKcal(d, meal, kcal);
                } else {
                    db.clearMealKcal(d, meal);
                    SyncManager.get(getApplicationContext()).notifyMealClear(d, meal);
                }
                pw.dismiss();
                refresh();
            }
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

    private void refresh() {
        int goal = prefs.getInt(PREF_GOAL, DEFAULT_GOAL);
        int[] day = db.getDayKcal(selectedIsoDate());
        int[] week = db.getWeekKcal(mondayIso);
        String[] dateNumbers = buildDateNumbers(mondayIso);

        histogram.setData(day, goal);
        weeklyStrip.setData(week, dateNumbers, selectedIndex, goal);

        int dayTotal = day[0] + day[1] + day[2] + day[3];
        totalKcal.setText(String.format(Locale.US, "%,d / %,d kcal", dayTotal, goal));

        try {
            weekOfTextView.setText("Week of " + weekOfLabel.format(iso.parse(mondayIso)));
        } catch (Exception e) {
            weekOfTextView.setText("");
        }

        int sum = 0;
        int activeDays = 0;
        for (int v : week) {
            if (v > 0) { sum += v; activeDays++; }
        }
        if (activeDays == 0) {
            weeklyAvg.setText(getString(R.string.week_avg_empty));
        } else {
            weeklyAvg.setText(getString(R.string.week_avg_format, sum / activeDays));
        }
    }

    private String[] buildDateNumbers(String mondayDate) {
        String[] out = new String[7];
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(iso.parse(mondayDate));
            for (int i = 0; i < 7; i++) {
                out[i] = dayNumber.format(c.getTime());
                c.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (Exception e) {
            for (int i = 0; i < 7; i++) out[i] = "";
        }
        return out;
    }

    private String shiftWeek(String mondayDate, int weeks) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(iso.parse(mondayDate));
            c.add(Calendar.DAY_OF_MONTH, weeks * 7);
            return iso.format(c.getTime());
        } catch (Exception e) {
            return mondayDate;
        }
    }

    private String selectedIsoDate() {
        Calendar c = Calendar.getInstance();
        try { c.setTime(iso.parse(mondayIso)); } catch (Exception e) { /* keep today */ }
        c.add(Calendar.DAY_OF_MONTH, selectedIndex);
        return iso.format(c.getTime());
    }

    private static Calendar mondayOfWeek(Calendar c) {
        Calendar m = (Calendar) c.clone();
        int dow = m.get(Calendar.DAY_OF_WEEK);
        int diff = (dow == Calendar.SUNDAY) ? -6 : (Calendar.MONDAY - dow);
        m.add(Calendar.DAY_OF_MONTH, diff);
        return m;
    }

    private static int dayOfWeekIndex(Calendar c) {
        int dow = c.get(Calendar.DAY_OF_WEEK);
        return (dow == Calendar.SUNDAY) ? 6 : (dow - Calendar.MONDAY);
    }

    private static int parseOr(String s, int fallback) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return fallback; }
    }

    private String mealLabel(int meal) {
        switch (meal) {
            case DatabaseHelper.MEAL_BREAKFAST: return getString(R.string.meal_breakfast);
            case DatabaseHelper.MEAL_LUNCH:     return getString(R.string.meal_lunch);
            case DatabaseHelper.MEAL_DINNER:    return getString(R.string.meal_dinner);
            default:                            return getString(R.string.meal_extra);
        }
    }

    private int mealColor(int meal) {
        switch (meal) {
            case DatabaseHelper.MEAL_BREAKFAST: return 0xFFF8C291;
            case DatabaseHelper.MEAL_LUNCH:     return 0xFFE58E26;
            case DatabaseHelper.MEAL_DINNER:    return 0xFF6678D1;
            default:                            return 0xFFEB2F06;
        }
    }
}
