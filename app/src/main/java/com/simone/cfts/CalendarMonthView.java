package com.simone.cfts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import java.util.Calendar;

public class CalendarMonthView extends View {

    public interface OnDayTapListener { void onDayTap(int day); }

    private static final int[] DIFF_COLORS = new int[]{
            0xFF71B62C,
            0xFF4EB795,
            0xFFD3D91A,
            0xFFE5842B,
            0xFFE20814
    };

    private static final int ORANGE_ACCENT = 0xFFE58E26;
    private static final int WEEKDAY_GRAY  = 0xFF7A8FB0;

    private final String[] weekdays = new String[]{"M", "T", "W", "T", "F", "S", "S"};

    private int year, monthIndex;
    private int selectedDay = -1;
    private int todayDay = -1;
    private int todayMonth = -1;
    private int todayYear = -1;
    private int[] maxDiffPerDay = new int[32];

    private int firstWeekdayIndex;
    private int daysInMonth;
    private int prevMonthTrailingStart;
    private int prevMonthDays;
    private int nextMonthLeadingDays;

    private OnDayTapListener listener;

    private final Paint diffLine    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint dayText     = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint fadedText   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint weekdayHdr  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint todayDot    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint selectedBox = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CalendarMonthView(Context c) { super(c); init(); }
    public CalendarMonthView(Context c, AttributeSet a) { super(c, a); init(); }
    public CalendarMonthView(Context c, AttributeSet a, int s) { super(c, a, s); init(); }

    private void init() {
        setClickable(true);
        setFocusable(true);

        Typeface mlight = null, mmedium = null;
        try {
            mlight  = ResourcesCompat.getFont(getContext(), R.font.mlight);
            mmedium = ResourcesCompat.getFont(getContext(), R.font.mmedium);
        } catch (Exception ignored) {}

        // Day number: italic-skewed mlight white
        dayText.setColor(Color.WHITE);
        dayText.setTextAlign(Paint.Align.CENTER);
        dayText.setTextSize(sp(14));
        if (mlight != null) dayText.setTypeface(mlight);
        dayText.setTextSkewX(-0.18f);

        // Faded text for prev/next month days
        fadedText.setColor(Color.WHITE);
        fadedText.setAlpha(70);
        fadedText.setTextAlign(Paint.Align.CENTER);
        fadedText.setTextSize(sp(14));
        if (mlight != null) fadedText.setTypeface(mlight);
        fadedText.setTextSkewX(-0.18f);

        // Weekday header: small letterspaced caps
        weekdayHdr.setColor(WEEKDAY_GRAY);
        weekdayHdr.setTextAlign(Paint.Align.CENTER);
        weekdayHdr.setTextSize(sp(11));
        if (mmedium != null) weekdayHdr.setTypeface(mmedium);
        weekdayHdr.setLetterSpacing(0.32f);

        // Today: small orange filled dot above the day number
        todayDot.setColor(ORANGE_ACCENT);
        todayDot.setStyle(Paint.Style.FILL);

        // Selected: thin orange rectangle stroke around the cell
        selectedBox.setColor(ORANGE_ACCENT);
        selectedBox.setStyle(Paint.Style.STROKE);
        selectedBox.setStrokeWidth(dp(1.5f));

        // Difficulty: thin colored underline beneath the day number
        diffLine.setStyle(Paint.Style.FILL);
    }

    public void setMonth(int year, int monthIndex /* 1-based */, int selectedDay,
                         int[] maxDiffPerDay, int todayYear, int todayMonth, int todayDay) {
        this.year = year;
        this.monthIndex = monthIndex;
        this.selectedDay = selectedDay;
        this.maxDiffPerDay = maxDiffPerDay != null ? maxDiffPerDay : new int[32];
        this.todayYear = todayYear;
        this.todayMonth = todayMonth;
        this.todayDay = todayDay;

        Calendar c = Calendar.getInstance();
        c.set(year, monthIndex - 1, 1);
        int dow = c.get(Calendar.DAY_OF_WEEK);
        firstWeekdayIndex = (dow == Calendar.SUNDAY) ? 6 : (dow - Calendar.MONDAY);
        daysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        Calendar prev = (Calendar) c.clone();
        prev.add(Calendar.MONTH, -1);
        prevMonthDays = prev.getActualMaximum(Calendar.DAY_OF_MONTH);
        prevMonthTrailingStart = prevMonthDays - firstWeekdayIndex + 1;

        int used = firstWeekdayIndex + daysInMonth;
        nextMonthLeadingDays = (42 - used);

        invalidate();
    }

    public void setOnDayTapListener(OnDayTapListener l) { this.listener = l; }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            int w = getWidth();
            int h = getHeight();
            int headerH = (int) (dp(10) + sp(11));
            float cellW = w / 7f;
            float cellH = (h - headerH) / 6f;
            float x = ev.getX();
            float y = ev.getY() - headerH;
            if (y < 0) return super.onTouchEvent(ev);
            int col = Math.max(0, Math.min(6, (int) (x / cellW)));
            int row = Math.max(0, Math.min(5, (int) (y / cellH)));
            int cellIdx = row * 7 + col;
            int day = cellIdx - firstWeekdayIndex + 1;
            if (day >= 1 && day <= daysInMonth) {
                selectedDay = day;
                invalidate();
                if (listener != null) listener.onDayTap(day);
                performClick();
                return true;
            }
        }
        return super.onTouchEvent(ev);
    }

    @Override public boolean performClick() { super.performClick(); return true; }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth();
        int h = getHeight();
        int headerH = (int) (dp(10) + sp(11));
        float cellW = w / 7f;
        float cellH = (h - headerH) / 6f;

        // Weekday header
        for (int i = 0; i < 7; i++) {
            float cx = (i + 0.5f) * cellW;
            canvas.drawText(weekdays[i], cx, sp(11) + dp(2), weekdayHdr);
        }

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                int cellIdx = row * 7 + col;
                int day;
                boolean otherMonth;
                if (cellIdx < firstWeekdayIndex) {
                    day = prevMonthTrailingStart + cellIdx;
                    otherMonth = true;
                } else if (cellIdx >= firstWeekdayIndex + daysInMonth) {
                    day = cellIdx - firstWeekdayIndex - daysInMonth + 1;
                    otherMonth = true;
                } else {
                    day = cellIdx - firstWeekdayIndex + 1;
                    otherMonth = false;
                }

                float cx = (col + 0.5f) * cellW;
                float cy = headerH + (row + 0.5f) * cellH;
                float cellLeft   = col * cellW;
                float cellRight  = (col + 1) * cellW;
                float cellTop    = headerH + row * cellH;
                float cellBottom = headerH + (row + 1) * cellH;

                boolean isToday = !otherMonth
                        && year == todayYear && monthIndex == todayMonth && day == todayDay;
                boolean isSelected = !otherMonth && day == selectedDay;
                int diff = !otherMonth ? maxDiffPerDay[day] : 0;

                // Selected cell outline (drawn first so other marks sit inside)
                if (isSelected) {
                    float inset = dp(4);
                    canvas.drawRoundRect(
                            cellLeft + inset, cellTop + inset,
                            cellRight - inset, cellBottom - inset,
                            dp(3), dp(3), selectedBox);
                }

                // Today dot above the day number
                if (isToday) {
                    float dotY = cy - sp(9) - dp(3);
                    canvas.drawCircle(cx, dotY, dp(2.2f), todayDot);
                }

                // Day number (text baseline at cy + ~sp(5) keeps glyph centered)
                Paint p = otherMonth ? fadedText : dayText;
                canvas.drawText(String.valueOf(day), cx, cy + sp(5), p);

                // Difficulty underline beneath the day number
                if (diff >= 1 && diff <= 5) {
                    diffLine.setColor(DIFF_COLORS[diff - 1]);
                    float lineY = cy + sp(5) + dp(3);
                    float lineHalfW = dp(7);
                    canvas.drawRect(cx - lineHalfW, lineY,
                            cx + lineHalfW, lineY + dp(1.5f), diffLine);
                }
            }
        }
    }

    private float dp(float v) { return v * getResources().getDisplayMetrics().density; }
    private float sp(float v) { return v * getResources().getDisplayMetrics().scaledDensity; }
}
