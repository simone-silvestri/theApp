package com.simone.cfts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class TimerRingView extends FrameLayout {

    private final Paint ringPaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float progress = 1.0f; // 1 = full ring (start), 0 = empty (finish)

    public TimerRingView(Context c) { super(c); init(); }
    public TimerRingView(Context c, AttributeSet a) { super(c, a); init(); }
    public TimerRingView(Context c, AttributeSet a, int s) { super(c, a, s); init(); }

    private void init() {
        setWillNotDraw(false);

        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeWidth(dp(2f));
        ringPaint.setStrokeCap(Paint.Cap.ROUND);
        ringPaint.setColor(0xFFFFFFFF);

        trackPaint.setStyle(Paint.Style.STROKE);
        trackPaint.setStrokeWidth(dp(2f));
        trackPaint.setColor(Color.WHITE);
        trackPaint.setAlpha(40);
    }

    public void setColor(int color) {
        ringPaint.setColor(color);
        invalidate();
    }

    public void setProgress(float p) {
        progress = Math.max(0f, Math.min(1f, p));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        int size = Math.min(w, h);
        if (size <= 0) return;

        float stroke = dp(2f);
        float pad = stroke / 2f + dp(4f);
        float left   = (w - size) / 2f + pad;
        float top    = (h - size) / 2f + pad;
        float right  = left + size - 2 * pad;
        float bottom = top  + size - 2 * pad;
        RectF arc = new RectF(left, top, right, bottom);

        canvas.drawArc(arc, 0f, 360f, false, trackPaint);
        if (progress > 0f) {
            canvas.drawArc(arc, -90f, 360f * progress, false, ringPaint);
        }
    }

    private float dp(float v) { return v * getResources().getDisplayMetrics().density; }
}
