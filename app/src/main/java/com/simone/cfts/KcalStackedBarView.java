package com.simone.cfts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class KcalStackedBarView extends View {

    private static final int[] MEAL_COLORS = new int[]{
            0xFFF8C291, // Breakfast
            0xFFE58E26, // Lunch
            0xFF6678D1, // Dinner
            0xFFEB2F06  // Extra
    };

    private int[] meals = new int[]{0, 0, 0, 0};
    private int goal = 2000;

    private final Paint trackPaint    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint segmentPaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint overPaint     = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint goalTickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public KcalStackedBarView(Context c) { super(c); init(); }
    public KcalStackedBarView(Context c, AttributeSet a) { super(c, a); init(); }
    public KcalStackedBarView(Context c, AttributeSet a, int s) { super(c, a, s); init(); }

    private void init() {
        trackPaint.setColor(Color.WHITE);
        trackPaint.setAlpha(30);

        segmentPaint.setStyle(Paint.Style.FILL);

        overPaint.setColor(0xFFEB2F06);
        overPaint.setAlpha(220);

        goalTickPaint.setColor(Color.WHITE);
        goalTickPaint.setStyle(Paint.Style.FILL);
        goalTickPaint.setAlpha(220);
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
        if (w <= 0 || h <= 0) return;

        float padTop = dp(2);
        float padBottom = dp(2);
        float trackTop = padTop;
        float trackBottom = h - padBottom;
        float trackLeft = 0;
        float trackRight = w;
        float radius = dp(3);
        RectF trackRect = new RectF(trackLeft, trackTop, trackRight, trackBottom);

        // Background track
        canvas.drawRoundRect(trackRect, radius, radius, trackPaint);

        int total = meals[0] + meals[1] + meals[2] + meals[3];
        // Scale so that the larger of goal vs total fills the rail.
        // When total <= goal: goal tick is at the right edge, stack ends short of it.
        // When total >  goal: stack fills the rail, goal tick floats left to its proportional position.
        int scaleMax = Math.max(goal, total);
        float pxPerKcal = (trackRight - trackLeft) / (float) scaleMax;

        if (total > 0) {
            canvas.save();
            Path clip = new Path();
            clip.addRoundRect(trackRect, radius, radius, Path.Direction.CW);
            canvas.clipPath(clip);

            float cursor = trackLeft;
            for (int i = 0; i < 4; i++) {
                if (meals[i] <= 0) continue;
                float segW = meals[i] * pxPerKcal;
                segmentPaint.setColor(MEAL_COLORS[i]);
                canvas.drawRect(cursor, trackTop, cursor + segW, trackBottom, segmentPaint);
                cursor += segW;
            }

            canvas.restore();
        }

        // Goal tick: at the proportional goal position relative to scaleMax
        float tickX = trackLeft + goal * pxPerKcal;
        float tickH = (trackBottom - trackTop) + dp(4);
        float tickY = trackTop - dp(2);
        canvas.drawRect(tickX - dp(1.2f), tickY,
                tickX, tickY + tickH, goalTickPaint);
    }

    private float dp(float v) { return v * getResources().getDisplayMetrics().density; }
}
