package com.masterlock.ble.app.gamma;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.support.p000v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import com.masterlock.ble.app.C1075R;

public class ExpandableArrow extends View {
    private static final String LOG_TAG = "ExpandableArrow";
    private int arrowColor = 0;
    private int arrowHeight = 20;
    private int arrowWidth = 10;
    private int bodyWidth = 3;
    private int height;
    private Context mContext;
    private Paint mPaint;
    private Path path;
    private int width;

    public ExpandableArrow(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        setAttributes(attributeSet);
    }

    private void setAttributes(AttributeSet attributeSet) {
        this.mPaint = new Paint();
        this.mPaint.setStyle(Style.FILL_AND_STROKE);
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(attributeSet, C1075R.styleable.ExpandableArrow, 0, 0);
        try {
            this.arrowColor = obtainStyledAttributes.getColor(0, ViewCompat.MEASURED_STATE_MASK);
        } catch (Exception unused) {
            obtainStyledAttributes.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        MeasureSpec.getMode(i);
        MeasureSpec.getMode(i2);
        int size = MeasureSpec.getSize(i);
        int size2 = MeasureSpec.getSize(i2);
        this.width = size;
        this.height = size2;
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Measured: width: ");
        sb.append(this.width);
        sb.append(", height: ");
        sb.append(this.height);
        Log.d(str, sb.toString());
        setMeasuredDimension(this.width, this.height);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        allocatePath();
        this.mPaint.setColor(this.arrowColor);
        canvas.drawPath(this.path, this.mPaint);
    }

    private void allocatePath() {
        if (this.path == null) {
            Point point = new Point((this.width / 2) - this.bodyWidth, 0);
            Point point2 = new Point((this.width / 2) + this.bodyWidth, 0);
            Point point3 = new Point((this.width / 2) + this.bodyWidth, this.height - this.arrowHeight);
            Point point4 = new Point((this.width / 2) + this.arrowWidth, this.height - this.arrowHeight);
            Point point5 = new Point(this.width / 2, this.height);
            Point point6 = new Point((this.width / 2) - this.arrowWidth, this.height - this.arrowHeight);
            Point point7 = new Point((this.width / 2) - this.bodyWidth, this.height - this.arrowHeight);
            this.path = new Path();
            this.path.moveTo((float) point.x, (float) point.y);
            this.path.lineTo((float) point2.x, (float) point2.y);
            this.path.lineTo((float) point3.x, (float) point3.y);
            this.path.lineTo((float) point4.x, (float) point4.y);
            this.path.lineTo((float) point5.x, (float) point5.y);
            this.path.lineTo((float) point6.x, (float) point6.y);
            this.path.lineTo((float) point7.x, (float) point7.y);
        }
    }
}
