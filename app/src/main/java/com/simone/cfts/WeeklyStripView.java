package com.simone.cfts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class WeeklyStripView extends View {

    public interface OnDayTapListener { void onDayTap(int dayIndex); }

    private int[] values = new int[7];
    private String[] dateNumbers = new String[7];
    private int selected = 0;
    private int goal = 2000;
    private OnDayTapListener listener;

    private final String[] day = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

    private final Paint barPaint    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint outlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint dayPaint    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint datePaint   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint valuePaint  = new Paint(Paint.ANTI_ALIAS_FLAG);

    public WeeklyStripView(Context c) { super(c); init(); }
    public WeeklyStripView(Context c, AttributeSet a) { super(c, a); init(); }
    public WeeklyStripView(Context c, AttributeSet a, int s) { super(c, a, s); init(); }

    private void init() {
        setClickable(true);
        setFocusable(true);

        barPaint.setColor(0xFFF8C291);

        outlinePaint.setColor(Color.WHITE);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(dp(1.5f));

        dayPaint.setColor(Color.WHITE);
        dayPaint.setAlpha(200);
        dayPaint.setTextSize(sp(12));
        dayPaint.setTextAlign(Paint.Align.CENTER);

        datePaint.setColor(Color.WHITE);
        datePaint.setAlpha(140);
        datePaint.setTextSize(sp(10));
        datePaint.setTextAlign(Paint.Align.CENTER);

        valuePaint.setColor(Color.WHITE);
        valuePaint.setAlpha(160);
        valuePaint.setTextSize(sp(10));
        valuePaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setData(int[] perDayKcal, String[] dayDateNumbers, int selectedIndex, int goalKcal) {
        if (perDayKcal != null && perDayKcal.length == 7) this.values = perDayKcal;
        if (dayDateNumbers != null && dayDateNumbers.length == 7) this.dateNumbers = dayDateNumbers;
        if (selectedIndex >= 0 && selectedIndex < 7) this.selected = selectedIndex;
        this.goal = Math.max(1, goalKcal);
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
        int padT = (int) dp(6);
        int padB = (int) dp(22);

        float dayY  = padT + sp(11);
        float dateY = dayY + sp(12);

        int barTop = (int) (dateY + dp(4));
        int barBottom = h - padB;
        int barAreaH = Math.max(1, barBottom - barTop);

        for (int i = 0; i < 7; i++) {
            float cx = (i + 0.5f) * (w / 7f);
            float barW = (w / 7f) * 0.45f;

            canvas.drawText(day[i], cx, dayY, dayPaint);

            String dateText = dateNumbers[i] != null ? dateNumbers[i] : "";
            canvas.drawText(dateText, cx, dateY, datePaint);

            float left = cx - barW / 2f;
            float right = cx + barW / 2f;
            float ratio = Math.min(1.0f, values[i] / (float) goal);
            float fillH = ratio * barAreaH;

            if (values[i] <= 0) {
                canvas.drawText("—", cx, barBottom - barAreaH / 2f, dayPaint);
            } else {
                canvas.drawRoundRect(left, barBottom - fillH, right, barBottom, dp(3), dp(3), barPaint);
            }

            if (i == selected) {
                canvas.drawRoundRect(left - dp(2), barTop - dp(2),
                        right + dp(2), barBottom + dp(2),
                        dp(5), dp(5), outlinePaint);
            }

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
