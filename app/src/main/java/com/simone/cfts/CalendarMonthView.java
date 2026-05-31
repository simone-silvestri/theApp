package com.simone.cfts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

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

    private final Paint cellFill   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint dayText    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint fadedText  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint weekdayHdr = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint todayRing  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint selectedRing = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CalendarMonthView(Context c) { super(c); init(); }
    public CalendarMonthView(Context c, AttributeSet a) { super(c, a); init(); }
    public CalendarMonthView(Context c, AttributeSet a, int s) { super(c, a, s); init(); }

    private void init() {
        setClickable(true);
        setFocusable(true);

        dayText.setColor(Color.WHITE);
        dayText.setTextAlign(Paint.Align.CENTER);
        dayText.setTextSize(sp(14));

        fadedText.setColor(Color.WHITE);
        fadedText.setAlpha(80);
        fadedText.setTextAlign(Paint.Align.CENTER);
        fadedText.setTextSize(sp(14));

        weekdayHdr.setColor(Color.WHITE);
        weekdayHdr.setAlpha(160);
        weekdayHdr.setTextAlign(Paint.Align.CENTER);
        weekdayHdr.setTextSize(sp(12));

        todayRing.setColor(Color.WHITE);
        todayRing.setAlpha(180);
        todayRing.setStyle(Paint.Style.STROKE);
        todayRing.setStrokeWidth(dp(1.5f));

        selectedRing.setColor(Color.WHITE);
        selectedRing.setStyle(Paint.Style.STROKE);
        selectedRing.setStrokeWidth(dp(2.5f));
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
            int headerH = (int) (dp(8) + sp(14));
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
        int headerH = (int) (dp(8) + sp(14));
        float cellW = w / 7f;
        float cellH = (h - headerH) / 6f;

        for (int i = 0; i < 7; i++) {
            float cx = (i + 0.5f) * cellW;
            canvas.drawText(weekdays[i], cx, sp(14), weekdayHdr);
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
                float pillRadius = Math.min(cellW, cellH) * 0.40f;

                if (!otherMonth) {
                    int diff = maxDiffPerDay[day];
                    if (diff >= 1 && diff <= 5) {
                        cellFill.setColor(DIFF_COLORS[diff - 1]);
                        canvas.drawRoundRect(cx - pillRadius, cy - pillRadius,
                                cx + pillRadius, cy + pillRadius,
                                dp(8), dp(8), cellFill);
                    }
                }

                boolean isToday = !otherMonth
                        && year == todayYear && monthIndex == todayMonth && day == todayDay;
                if (isToday) {
                    canvas.drawRoundRect(cx - pillRadius, cy - pillRadius,
                            cx + pillRadius, cy + pillRadius,
                            dp(8), dp(8), todayRing);
                }

                boolean isSelected = !otherMonth && day == selectedDay;
                if (isSelected) {
                    float sr = pillRadius + dp(2);
                    canvas.drawRoundRect(cx - sr, cy - sr,
                            cx + sr, cy + sr,
                            dp(10), dp(10), selectedRing);
                }

                Paint p = otherMonth ? fadedText : dayText;
                canvas.drawText(String.valueOf(day), cx, cy + sp(5), p);
            }
        }
    }

    private float dp(float v) { return v * getResources().getDisplayMetrics().density; }
    private float sp(float v) { return v * getResources().getDisplayMetrics().scaledDensity; }
}
