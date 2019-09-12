package com.masterlock.ble.app.view.lock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.masterlock.ble.app.C1075R;

public class DirectionalPadButton extends View {
    public static final boolean EXACT = true;
    private Paint mButtonPaint;
    private Region mRegion;

    public DirectionalPadButton(Context context) {
        this(context, null);
    }

    public DirectionalPadButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public DirectionalPadButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    private void init() {
        this.mButtonPaint = new Paint(1);
        this.mButtonPaint.setColor(getResources().getColor(C1075R.color.medium_grey));
        this.mButtonPaint.setStyle(Style.FILL);
    }

    private Path getPath() {
        float dimensionPixelSize = (float) getResources().getDimensionPixelSize(C1075R.dimen.outer_radius);
        float dimensionPixelSize2 = (float) getResources().getDimensionPixelSize(C1075R.dimen.inner_radius);
        float width = ((float) getWidth()) / 2.0f;
        float height = (float) getHeight();
        Path path = new Path();
        float f = dimensionPixelSize / 1.3f;
        PointF pointOnCircle = pointOnCircle(45.0f, f, width, height);
        path.moveTo(pointOnCircle.x, height - (pointOnCircle.y - height));
        float f2 = dimensionPixelSize2 / 1.3f;
        PointF pointOnCircle2 = pointOnCircle(45.0f, f2, width, height);
        path.lineTo(pointOnCircle2.x, height - (pointOnCircle2.y - height));
        PointF pointOnCircle3 = pointOnCircle(90.0f, dimensionPixelSize2 / 1.2f, width, height);
        PointF pointOnCircle4 = pointOnCircle(135.0f, f2, width, height);
        path.quadTo(pointOnCircle3.x, height - (pointOnCircle3.y - height), pointOnCircle4.x, height - (pointOnCircle4.y - height));
        PointF pointOnCircle5 = pointOnCircle(135.0f, f, width, height);
        path.lineTo(pointOnCircle5.x, height - (pointOnCircle5.y - height));
        path.quadTo(pointOnCircle(90.0f, dimensionPixelSize, width, 0.0f).x, 0.0f, pointOnCircle.x, height - (pointOnCircle.y - height));
        path.close();
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        this.mRegion = new Region();
        this.mRegion.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        return path;
    }

    private PointF pointOnCircle(float f, float f2, float f3, float f4) {
        PointF pointF = new PointF();
        double d = (double) f;
        pointF.x = f3 + (((float) Math.cos(Math.toRadians(d))) * f2);
        pointF.y = f4 + (f2 * ((float) Math.sin(Math.toRadians(d))));
        return pointF;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(getPath(), this.mButtonPaint);
    }

    public boolean viewTouched(MotionEvent motionEvent) {
        PointF pointF = new PointF();
        pointF.x = motionEvent.getX();
        pointF.y = motionEvent.getY();
        if (motionEvent.getAction() == 0 && isTouchInDrawnBounds(pointF)) {
            this.mButtonPaint.setColor(getResources().getColor(C1075R.color.primary));
            invalidate();
            return true;
        } else if (motionEvent.getAction() != 1) {
            return false;
        } else {
            this.mButtonPaint.setColor(getResources().getColor(C1075R.color.medium_grey));
            invalidate();
            return true;
        }
    }

    private boolean isTouchInDrawnBounds(PointF pointF) {
        return this.mRegion.contains((int) pointF.x, (int) pointF.y);
    }
}
