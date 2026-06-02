package com.simone.cfts;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Running weight chart: faint daily dots plus a bold weekly-median line.
 * Horizontally pannable; the vertical scale auto-fits the visible window.
 */
public class WeightChartView extends View {

    private static final int COLOR_DOT    = 0x667A8FB0; // ~40% alpha muted blue
    private static final int COLOR_MEDIAN = 0xFFE58E26; // orange
    private static final int COLOR_GRID   = 0x1AFFFFFF; // 10% white
    private static final int COLOR_LABEL  = 0xFF7A8FB0;
    private static final int COLOR_RING   = 0xFFE58E26;

    private static final long MILLIS_PER_DAY = 24L * 60L * 60L * 1000L;

    private final SimpleDateFormat iso   = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private final SimpleDateFormat month = new SimpleDateFormat("MMM", Locale.US);

    private final Paint dotPaint    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint medianLine  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint medianDot   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint gridPaint   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint ringPaint   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final TextPaint labelPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private final TextPaint emptyPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private List<DatabaseHelper.WeightRow> rows = new ArrayList<>();
    private List<WeightStats.WeekMedian> medians = new ArrayList<>();
    private String selectedIso;

    private long baseMillis;   // midnight of earliest weigh-in
    private float dpPerDay;
    private float scrollX;      // px; 0 = earliest at left edge
    private boolean scrollInit; // snap to most recent once we know our width

    private float reveal = 1f;  // first-draw fade-in multiplier
    private boolean revealed;
    private ValueAnimator revealAnim;

    private final GestureDetector gestures;

    public WeightChartView(Context c, AttributeSet a) {
        super(c, a);
        dpPerDay = dp(14);

        dotPaint.setColor(COLOR_DOT);

        medianLine.setColor(COLOR_MEDIAN);
        medianLine.setStyle(Paint.Style.STROKE);
        medianLine.setStrokeWidth(dp(2.5f));
        medianLine.setStrokeJoin(Paint.Join.ROUND);
        medianLine.setStrokeCap(Paint.Cap.ROUND);

        medianDot.setColor(COLOR_MEDIAN);

        gridPaint.setColor(COLOR_GRID);
        gridPaint.setStrokeWidth(1f);

        ringPaint.setColor(COLOR_RING);
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeWidth(dp(2));

        labelPaint.setColor(COLOR_LABEL);
        labelPaint.setTextSize(dp(10));

        emptyPaint.setColor(COLOR_LABEL);
        emptyPaint.setTextSize(dp(13));
        emptyPaint.setTextAlign(Paint.Align.CENTER);

        gestures = new GestureDetector(c, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onDown(MotionEvent e) { return true; }
            @Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
                scrollX = clamp(scrollX + dx, 0f, maxScroll());
                invalidate();
                return true;
            }
        });
    }

    public void setLabelTypeface(Typeface tf) {
        if (tf != null) {
            labelPaint.setTypeface(tf);
            emptyPaint.setTypeface(Typeface.create(tf, Typeface.ITALIC));
        }
    }

    public void setData(List<DatabaseHelper.WeightRow> rows,
                        List<WeightStats.WeekMedian> medians, String selectedIso) {
        this.rows = rows != null ? rows : new ArrayList<DatabaseHelper.WeightRow>();
        this.medians = medians != null ? medians : new ArrayList<WeightStats.WeekMedian>();
        this.selectedIso = selectedIso;
        if (!this.rows.isEmpty()) baseMillis = millisOf(this.rows.get(0).date);
        scrollInit = false; // re-snap to most recent on next draw
        if (!revealed) {
            revealed = true;
            startReveal();
        }
        invalidate();
    }

    private void startReveal() {
        if (revealAnim != null) revealAnim.cancel();
        reveal = 0f;
        revealAnim = ValueAnimator.ofFloat(0f, 1f);
        revealAnim.setDuration(450);
        revealAnim.addUpdateListener(a -> { reveal = (float) a.getAnimatedValue(); invalidate(); });
        revealAnim.start();
    }

    @Override public boolean onTouchEvent(MotionEvent e) {
        int action = e.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            if (getParent() != null) getParent().requestDisallowInterceptTouchEvent(true);
        }
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            if (getParent() != null) getParent().requestDisallowInterceptTouchEvent(false);
        }
        return gestures.onTouchEvent(e) || super.onTouchEvent(e);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();

        if (rows.size() < 2) {
            canvas.drawText("log a few days to see your trend", w / 2f, h / 2f, emptyPaint);
            return;
        }

        float plotLeft = dp(8);
        float plotRight = w - dp(36); // right gutter for kg labels
        float plotTop = dp(14);
        float plotBottom = h - dp(20); // baseline for month labels
        float plotW = plotRight - plotLeft;
        float plotH = plotBottom - plotTop;

        if (!scrollInit) {
            scrollX = maxScroll();
            scrollInit = true;
        } else {
            scrollX = clamp(scrollX, 0f, maxScroll());
        }

        // Build median screen-points (plotted at each week's mid-date = Monday + 3 days).
        List<float[]> medianPts = new ArrayList<>(); // {x, weight}
        for (WeightStats.WeekMedian m : medians) {
            long mid = millisOf(m.mondayIso) + 3L * MILLIS_PER_DAY;
            float x = xForMillis(mid, plotLeft);
            medianPts.add(new float[]{x, m.median});
        }

        // Determine vertical scale from values currently visible.
        float minW = Float.MAX_VALUE, maxW = -Float.MAX_VALUE;
        for (DatabaseHelper.WeightRow r : rows) {
            float x = xForMillis(millisOf(r.date), plotLeft);
            if (x < plotLeft - dp(6) || x > plotRight + dp(6)) continue;
            minW = Math.min(minW, r.weight);
            maxW = Math.max(maxW, r.weight);
        }
        for (float[] p : medianPts) {
            if (p[0] < plotLeft - dp(6) || p[0] > plotRight + dp(6)) continue;
            minW = Math.min(minW, p[1]);
            maxW = Math.max(maxW, p[1]);
        }
        if (minW == Float.MAX_VALUE) { minW = 60f; maxW = 80f; } // nothing visible fallback
        minW -= 0.5f; maxW += 0.5f;
        if (maxW - minW < 1f) { float mid = (minW + maxW) / 2f; minW = mid - 0.5f; maxW = mid + 0.5f; }

        int saveCount = -1;
        if (reveal < 1f) saveCount = canvas.saveLayerAlpha(0, 0, w, h, (int) (255 * reveal));

        // Horizontal gridlines + kg labels (3 lines).
        for (int i = 0; i <= 2; i++) {
            float t = i / 2f;
            float y = plotBottom - t * plotH;
            canvas.drawLine(plotLeft, y, plotRight, y, gridPaint);
            float kg = minW + t * (maxW - minW);
            canvas.drawText(String.format(Locale.US, "%.0f", kg), plotRight + dp(6), y + dp(3.5f), labelPaint);
        }

        // Month boundary ticks across the visible day window.
        int firstDay = (int) Math.floor(scrollX / dpPerDay) - 1;
        int lastDay = (int) Math.ceil((scrollX + plotW) / dpPerDay) + 1;
        Calendar cal = Calendar.getInstance();
        for (int d = Math.max(0, firstDay); d <= lastDay; d++) {
            long ms = baseMillis + (long) d * MILLIS_PER_DAY;
            cal.setTimeInMillis(ms);
            if (cal.get(Calendar.DAY_OF_MONTH) != 1) continue;
            float x = plotLeft + d * dpPerDay - scrollX;
            if (x < plotLeft || x > plotRight) continue;
            canvas.drawLine(x, plotTop, x, plotBottom, gridPaint);
            canvas.drawText(month.format(cal.getTime()).toUpperCase(Locale.US), x + dp(3), plotBottom + dp(13), labelPaint);
        }

        // Daily dots.
        float dotR = dp(2.5f);
        for (DatabaseHelper.WeightRow r : rows) {
            float x = xForMillis(millisOf(r.date), plotLeft);
            if (x < plotLeft - dotR || x > plotRight + dotR) continue;
            float y = yFor(r.weight, minW, maxW, plotTop, plotBottom);
            canvas.drawCircle(x, y, dotR, dotPaint);
        }

        // Weekly-median line + dots.
        float[] prev = null;
        float medR = dp(4);
        for (float[] p : medianPts) {
            float x = p[0];
            float y = yFor(p[1], minW, maxW, plotTop, plotBottom);
            if (prev != null) {
                canvas.drawLine(prev[0], prev[1], x, y, medianLine);
            }
            prev = new float[]{x, y};
        }
        for (float[] p : medianPts) {
            float x = p[0];
            if (x < plotLeft - medR || x > plotRight + medR) continue;
            float y = yFor(p[1], minW, maxW, plotTop, plotBottom);
            canvas.drawCircle(x, y, medR, medianDot);
        }

        // Selected-day ring.
        if (selectedIso != null) {
            for (DatabaseHelper.WeightRow r : rows) {
                if (!selectedIso.equals(r.date)) continue;
                float x = xForMillis(millisOf(r.date), plotLeft);
                if (x < plotLeft || x > plotRight) break;
                float y = yFor(r.weight, minW, maxW, plotTop, plotBottom);
                canvas.drawCircle(x, y, dp(6), ringPaint);
                break;
            }
        }

        if (saveCount != -1) canvas.restoreToCount(saveCount);
    }

    // ---------- helpers ----------

    private float xForMillis(long ms, float plotLeft) {
        long dayIndex = Math.round((double) (ms - baseMillis) / MILLIS_PER_DAY);
        return plotLeft + dayIndex * dpPerDay - scrollX;
    }

    private float yFor(float weight, float minW, float maxW, float top, float bottom) {
        float t = (weight - minW) / (maxW - minW);
        return bottom - t * (bottom - top);
    }

    private float maxScroll() {
        if (rows.size() < 2) return 0f;
        long lastDay = Math.round((double) (millisOf(rows.get(rows.size() - 1).date) - baseMillis) / MILLIS_PER_DAY);
        float contentW = lastDay * dpPerDay;
        float visibleW = getWidth() - dp(8) - dp(36);
        return Math.max(0f, contentW - visibleW);
    }

    private long millisOf(String date) {
        try { return iso.parse(date).getTime(); } catch (Exception e) { return baseMillis; }
    }

    private static float clamp(float v, float lo, float hi) { return Math.max(lo, Math.min(hi, v)); }

    private float dp(float v) { return v * getResources().getDisplayMetrics().density; }
}
