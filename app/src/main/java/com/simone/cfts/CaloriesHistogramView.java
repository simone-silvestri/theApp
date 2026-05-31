package com.simone.cfts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CaloriesHistogramView extends View {

    private int[] meals = new int[]{0, 0, 0, 0};
    private int goal = 2000;
    private final int[] barColors = new int[]{
            0xFFF8C291,
            0xFFE58E26,
            0xFF6678D1,
            0xFFEB2F06,
            0xFFC0C0C0
    };
    private final String[] labels = new String[]{"B", "L", "D", "E", "T"};

    private final Paint barPaint   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint axisPaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint valuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint goalPaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint goalLabel  = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CaloriesHistogramView(Context c) { super(c); init(); }
    public CaloriesHistogramView(Context c, AttributeSet a) { super(c, a); init(); }
    public CaloriesHistogramView(Context c, AttributeSet a, int s) { super(c, a, s); init(); }

    private void init() {
        labelPaint.setColor(Color.WHITE);
        labelPaint.setTextAlign(Paint.Align.CENTER);
        labelPaint.setTextSize(sp(14));

        valuePaint.setColor(Color.WHITE);
        valuePaint.setTextAlign(Paint.Align.CENTER);
        valuePaint.setTextSize(sp(12));
        valuePaint.setAlpha(220);

        axisPaint.setColor(Color.WHITE);
        axisPaint.setAlpha(120);
        axisPaint.setStrokeWidth(dp(1));
        axisPaint.setTextAlign(Paint.Align.RIGHT);
        axisPaint.setTextSize(sp(10));

        goalPaint.setColor(Color.WHITE);
        goalPaint.setAlpha(128);
        goalPaint.setStyle(Paint.Style.STROKE);
        goalPaint.setStrokeWidth(dp(1.5f));
        goalPaint.setPathEffect(new DashPathEffect(new float[]{dp(6), dp(4)}, 0));

        goalLabel.setColor(Color.WHITE);
        goalLabel.setAlpha(160);
        goalLabel.setTextSize(sp(10));
        goalLabel.setTextAlign(Paint.Align.RIGHT);
    }

    public void setData(int[] perMealKcal, int goalKcal) {
        if (perMealKcal != null && perMealKcal.length == 4) this.meals = perMealKcal;
        this.goal = Math.max(1, goalKcal);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth();
        int h = getHeight();
        int padL = (int) dp(40);
        int padR = (int) dp(36);
        int padT = (int) dp(20);
        int padB = (int) dp(36);
        int plotW = w - padL - padR;
        int plotH = h - padT - padB;
        if (plotW <= 0 || plotH <= 0) return;

        int total = meals[0] + meals[1] + meals[2] + meals[3];
        int[] values = new int[]{meals[0], meals[1], meals[2], meals[3], total};

        int maxVal = goal;
        for (int v : values) if (v > maxVal) maxVal = v;
        float yMax = maxVal * 1.15f;

        int[] yTicks = new int[]{0, goal / 2, goal};
        for (int yv : yTicks) {
            float y = padT + plotH - (yv / yMax) * plotH;
            canvas.drawText(String.valueOf(yv), padL - dp(6), y + sp(3), axisPaint);
        }

        float yGoal = padT + plotH - (goal / yMax) * plotH;
        canvas.drawLine(padL, yGoal, padL + plotW, yGoal, goalPaint);
        canvas.drawText(getContext().getString(R.string.goal_axis_label),
                padL + plotW + dp(28), yGoal + sp(3), goalLabel);

        int n = values.length;
        float barW = plotW / (n * 2f);
        for (int i = 0; i < n; i++) {
            float cx = padL + plotW * (i * 2 + 1) / (n * 2f);
            float barH = (values[i] / yMax) * plotH;
            barPaint.setColor(barColors[i]);
            float left = cx - barW / 2f;
            float right = cx + barW / 2f;
            float top = padT + plotH - barH;
            float bottom = padT + plotH;
            canvas.drawRoundRect(left, top, right, bottom, dp(4), dp(4), barPaint);

            if (values[i] > 0) {
                canvas.drawText(String.valueOf(values[i]), cx, top - dp(4), valuePaint);
            }

            canvas.drawText(labels[i], cx, padT + plotH + dp(20), labelPaint);
        }
    }

    private float dp(float v) { return v * getResources().getDisplayMetrics().density; }
    private float sp(float v) { return v * getResources().getDisplayMetrics().scaledDensity; }
}
