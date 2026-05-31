package com.simone.cfts;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HealthActivity extends AppCompatActivity {

    static final String PREF_GOAL = "health.daily_goal_kcal";
    static final int DEFAULT_GOAL = 2000;

    private static final int[] MEAL_COLORS = new int[]{
            0xFFF8C291, // Breakfast
            0xFFE58E26, // Lunch
            0xFF6678D1, // Dinner
            0xFFEB2F06  // Extra
    };

    private DatabaseHelper db;
    private SharedPreferences prefs;
    private final SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private final SimpleDateFormat dayNumber = new SimpleDateFormat("d", Locale.US);
    private final SimpleDateFormat weekLabel = new SimpleDateFormat("d MMMM", Locale.US);
    private final SimpleDateFormat dayName   = new SimpleDateFormat("EEEE", Locale.US);

    private String mondayIso;
    private int selectedIndex;
    private WeeklyStripView weeklyStrip;
    private KcalStackedBarView stackedBar;
    private TextView totalKcal;
    private TextView weekOfTextView;
    private TextView weeklyAvg;
    private TextView dayNameLabel;
    private TextView barCaption;
    private LinearLayout mealsContainer;

    private Typeface mlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        db = DatabaseHelper.getInstance(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        mlight = ResourcesCompat.getFont(this, R.font.mlight);

        weeklyStrip    = findViewById(R.id.weeklyStrip);
        stackedBar     = findViewById(R.id.stackedBar);
        totalKcal      = findViewById(R.id.totalKcal);
        weekOfTextView = findViewById(R.id.weekOfLabel);
        weeklyAvg      = findViewById(R.id.weeklyAvg);
        dayNameLabel   = findViewById(R.id.dayNameLabel);
        barCaption     = findViewById(R.id.barCaption);
        mealsContainer = findViewById(R.id.mealsContainer);

        Calendar today = Calendar.getInstance();
        mondayIso = iso.format(mondayOfWeek(today).getTime());
        selectedIndex = dayOfWeekIndex(today);

        weeklyStrip.setOnDayTapListener(new WeeklyStripView.OnDayTapListener() {
            @Override public void onDayTap(int dayIndex) {
                selectedIndex = dayIndex;
                refresh();
            }
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

        int todayIndex = computeTodayIndexInWeek();
        weeklyStrip.setData(week, dateNumbers, selectedIndex, goal, todayIndex);
        stackedBar.setData(day, goal);

        int dayTotal = day[0] + day[1] + day[2] + day[3];

        // Day name (small caps)
        try {
            String name = dayName.format(iso.parse(selectedIsoDate())).toUpperCase(Locale.US);
            dayNameLabel.setText(name);
        } catch (Exception e) {
            dayNameLabel.setText("");
        }

        // Total kcal as Spannable: consumed white, "/ goal kcal" muted
        String consumed = String.format(Locale.US, "%,d", dayTotal);
        String tail     = String.format(Locale.US, " / %,d kcal", goal);
        SpannableStringBuilder sb = new SpannableStringBuilder(consumed).append(tail);
        sb.setSpan(new ForegroundColorSpan(0xFFA3B0CC),
                consumed.length(), consumed.length() + tail.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        totalKcal.setText(sb);

        // Bar caption: "X to go" or "Y over goal"
        if (dayTotal <= 0) {
            barCaption.setText("");
        } else if (dayTotal < goal) {
            barCaption.setText(String.format(Locale.US, "%,d to go", goal - dayTotal));
        } else if (dayTotal == goal) {
            barCaption.setText("right on goal");
        } else {
            barCaption.setText(String.format(Locale.US, "%,d over goal", dayTotal - goal));
        }

        // Week label: "WEEK OF 25 MAY"
        try {
            weekOfTextView.setText("WEEK OF " +
                    weekLabel.format(iso.parse(mondayIso)).toUpperCase(Locale.US));
        } catch (Exception e) {
            weekOfTextView.setText("");
        }

        // Weekly avg
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

        rebuildMealRows(day);
    }

    private void rebuildMealRows(int[] dayKcal) {
        mealsContainer.removeAllViews();
        for (int i = 0; i < 4; i++) {
            mealsContainer.addView(buildMealRow(i, dayKcal[i]));
        }
    }

    private View buildMealRow(final int meal, int kcal) {
        LinearLayout outer = new LinearLayout(this);
        outer.setOrientation(LinearLayout.VERTICAL);

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setClickable(true);
        row.setFocusable(true);
        row.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { openMealEntry(meal); }
        });

        // Colored stripe
        View stripe = new View(this);
        stripe.setBackgroundColor(MEAL_COLORS[meal]);
        row.addView(stripe, new LinearLayout.LayoutParams((int) dp(4), (int) dp(48)));

        // Body: meal name + kicker (when empty)
        LinearLayout body = new LinearLayout(this);
        body.setOrientation(LinearLayout.VERTICAL);
        body.setPadding(0, (int) dp(14), 0, (int) dp(14));
        LinearLayout.LayoutParams blp = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        blp.setMarginStart((int) dp(14));
        row.addView(body, blp);

        TextView name = new TextView(this);
        name.setText(mealLabel(meal));
        name.setTextColor(0xFFFFFFFF);
        name.setTextSize(18);
        if (mlight != null) name.setTypeface(mlight);
        name.setMaxLines(1);
        body.addView(name);

        if (kcal <= 0) {
            TextView kicker = new TextView(this);
            kicker.setText("tap to log");
            kicker.setTextColor(0xFF7A8FB0);
            kicker.setTextSize(12);
            if (mlight != null) kicker.setTypeface(mlight, Typeface.ITALIC);
            else kicker.setTypeface(null, Typeface.ITALIC);
            LinearLayout.LayoutParams klp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            klp.topMargin = (int) dp(1);
            body.addView(kicker, klp);
        }

        // Right-side kcal value
        TextView value = new TextView(this);
        if (kcal > 0) {
            value.setText(String.format(Locale.US, "%,d kcal", kcal));
            value.setTextColor(0xFFFFFFFF);
        } else {
            value.setText("—");
            value.setTextColor(0xFF5A6B85);
        }
        value.setTextSize(18);
        if (mlight != null) value.setTypeface(mlight, Typeface.ITALIC);
        else value.setTypeface(null, Typeface.ITALIC);
        value.setPadding((int) dp(8), 0, 0, 0);
        row.addView(value, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        outer.addView(row);

        // Hairline divider
        View hairline = new View(this);
        hairline.setBackgroundColor(0x33FFFFFF);
        outer.addView(hairline, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1));

        return outer;
    }

    private int computeTodayIndexInWeek() {
        try {
            Calendar today = Calendar.getInstance();
            String todayIsoStr = iso.format(today.getTime());
            Calendar c = Calendar.getInstance();
            c.setTime(iso.parse(mondayIso));
            for (int i = 0; i < 7; i++) {
                if (iso.format(c.getTime()).equals(todayIsoStr)) return i;
                c.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (Exception ignored) {}
        return -1;
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

    private float dp(float v) { return v * getResources().getDisplayMetrics().density; }
}
