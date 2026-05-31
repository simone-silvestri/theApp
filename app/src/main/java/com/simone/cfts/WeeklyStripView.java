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

public class WeeklyStripView extends View {

    public interface OnDayTapListener { void onDayTap(int dayIndex); }

    private static final int ORANGE_ACCENT = 0xFFE58E26;
    private static final int WEEKDAY_GRAY  = 0xFF7A8FB0;

    private int[] values = new int[7];
    private String[] dateNumbers = new String[7];
    private int selected = 0;
    private int goal = 2000;
    private int todayIndex = -1;
    private OnDayTapListener listener;

    private final String[] day = new String[]{"M", "T", "W", "T", "F", "S", "S"};

    private final Paint barPaint     = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint outlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint dayPaint     = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint datePaint    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint emptyPaint   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint valuePaint   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint todayDot     = new Paint(Paint.ANTI_ALIAS_FLAG);

    public WeeklyStripView(Context c) { super(c); init(); }
    public WeeklyStripView(Context c, AttributeSet a) { super(c, a); init(); }
    public WeeklyStripView(Context c, AttributeSet a, int s) { super(c, a, s); init(); }

    private void init() {
        setClickable(true);
        setFocusable(true);

        Typeface mlight = null, mmedium = null;
        try {
            mlight  = ResourcesCompat.getFont(getContext(), R.font.mlight);
            mmedium = ResourcesCompat.getFont(getContext(), R.font.mmedium);
        } catch (Exception ignored) {}

        // Bars: white at 70% alpha — calm chart fill
        barPaint.setColor(Color.WHITE);
        barPaint.setAlpha(180);

        // Selected: orange outline
        outlinePaint.setColor(ORANGE_ACCENT);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(dp(1.5f));

        // Day letters: letterspaced caps in muted blue-gray
        dayPaint.setColor(WEEKDAY_GRAY);
        dayPaint.setTextSize(sp(11));
        dayPaint.setTextAlign(Paint.Align.CENTER);
        if (mmedium != null) dayPaint.setTypeface(mmedium);
        dayPaint.setLetterSpacing(0.32f);

        // Date numbers: italic mlight white
        datePaint.setColor(Color.WHITE);
        datePaint.setAlpha(200);
        datePaint.setTextSize(sp(13));
        datePaint.setTextAlign(Paint.Align.CENTER);
        if (mlight != null) datePaint.setTypeface(mlight);
        datePaint.setTextSkewX(-0.18f);

        // Empty placeholder "—": italic mlight at low alpha
        emptyPaint.setColor(Color.WHITE);
        emptyPaint.setAlpha(100);
        emptyPaint.setTextSize(sp(14));
        emptyPaint.setTextAlign(Paint.Align.CENTER);
        if (mlight != null) emptyPaint.setTypeface(mlight);
        emptyPaint.setTextSkewX(-0.18f);

        // Value text below each column
        valuePaint.setColor(WEEKDAY_GRAY);
        valuePaint.setTextSize(sp(10));
        valuePaint.setTextAlign(Paint.Align.CENTER);
        if (mlight != null) valuePaint.setTypeface(mlight);
        valuePaint.setTextSkewX(-0.18f);

        // Today dot
        todayDot.setColor(ORANGE_ACCENT);
        todayDot.setStyle(Paint.Style.FILL);
    }

    public void setData(int[] perDayKcal, String[] dayDateNumbers,
                        int selectedIndex, int goalKcal, int todayIndex) {
        if (perDayKcal != null && perDayKcal.length == 7) this.values = perDayKcal;
        if (dayDateNumbers != null && dayDateNumbers.length == 7) this.dateNumbers = dayDateNumbers;
        if (selectedIndex >= 0 && selectedIndex < 7) this.selected = selectedIndex;
        this.goal = Math.max(1, goalKcal);
        this.todayIndex = todayIndex;
        invalidate();
    }

    public void setOnDayTapListener(OnDayTapListener l) { this.listener = l; }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) ev.getX();
            int colW = getWidth() / 7;
            int idx = Math.max(0, Math.min(6, x / Math.max(1, colW)));
            selected = idx;
            invalidate();
            if (listener != null) listener.onDayTap(idx);
            performClick();
            return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override public boolean performClick() { super.performClick(); return true; }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth();
        int h = getHeight();

        // Header band (today-dot + day letter + date number) at top
        float padT = dp(10);
        float dayLetterY = padT + sp(11);
        float dateY      = dayLetterY + sp(15);

        int barTop = (int) (dateY + dp(8));
        int barBottom = (int) (h - dp(20));
        int barAreaH = Math.max(1, barBottom - barTop);

        for (int i = 0; i < 7; i++) {
            float cx = (i + 0.5f) * (w / 7f);
            float barW = (w / 7f) * 0.45f;
            float left = cx - barW / 2f;
            float right = cx + barW / 2f;

            // Today dot above the day letter
            if (i == todayIndex) {
                canvas.drawCircle(cx, padT - dp(1), dp(2.2f), todayDot);
            }

            // Day letter (letterspaced caps)
            canvas.drawText(day[i], cx, dayLetterY, dayPaint);

            // Date number (italic mlight)
            String dateText = dateNumbers[i] != null ? dateNumbers[i] : "";
            canvas.drawText(dateText, cx, dateY, datePaint);

            // Bar fill / empty placeholder
            float ratio = Math.min(1.0f, values[i] / (float) goal);
            float fillH = ratio * barAreaH;
            if (values[i] <= 0) {
                canvas.drawText("—", cx, barBottom - barAreaH / 2f + sp(5), emptyPaint);
            } else {
                canvas.drawRoundRect(left, barBottom - fillH, right, barBottom,
                        dp(3), dp(3), barPaint);
            }

            // Selected outline (orange, around the bar area)
            if (i == selected) {
                canvas.drawRoundRect(left - dp(3), barTop - dp(3),
                        right + dp(3), barBottom + dp(3),
                        dp(5), dp(5), outlinePaint);
            }

            // Value text beneath the column
            String v = values[i] > 0 ? formatKcal(values[i]) : "—";
            canvas.drawText(v, cx, h - dp(6), valuePaint);
        }
    }

    private String formatKcal(int kcal) {
        if (kcal >= 1000) {
            float k = kcal / 1000f;
            return String.format(java.util.Locale.US, "%.1fk", k);
        }
        return String.valueOf(kcal);
    }

    private float dp(float v) { return v * getResources().getDisplayMetrics().density; }
    private float sp(float v) { return v * getResources().getDisplayMetrics().scaledDensity; }
}
