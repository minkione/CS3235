package com.masterlock.ble.app.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.masterlock.ble.app.C1075R;

public class BatteryIndicator extends View {
    private RectF batteryNipRect;
    private RectF batteryRect;
    private RectF innerRect;
    private float mBatteryHeight;
    private float mBatteryInnerHeight;
    private float mBatteryInnerWidth;
    private float mBatteryNipHeight;
    private float mBatteryNipWidth;
    private float mBatteryWidth;
    private int mColor;
    private float mCornerRadius;
    private int mNormalColor;
    private float mStrokeWidth;
    private int mTextColor;
    private Paint paint = new Paint();
    private Paint textPaint = new Paint();

    public BatteryIndicator(Context context) {
        super(context, null);
    }

    public BatteryIndicator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C1075R.styleable.BatteryIndicator);
        try {
            this.mBatteryWidth = obtainStyledAttributes.getDimension(9, getResources().getDimension(C1075R.dimen.battery_width));
            this.mBatteryHeight = obtainStyledAttributes.getDimension(2, getResources().getDimension(C1075R.dimen.battery_height));
            this.mBatteryNipWidth = obtainStyledAttributes.getDimension(6, getResources().getDimension(C1075R.dimen.battery_nip_width));
            this.mBatteryNipHeight = obtainStyledAttributes.getDimension(5, getResources().getDimension(C1075R.dimen.battery_nip_height));
            this.mBatteryInnerWidth = obtainStyledAttributes.getDimension(4, getResources().getDimension(C1075R.dimen.battery_inner_full_width));
            this.mBatteryInnerHeight = obtainStyledAttributes.getDimension(3, getResources().getDimension(C1075R.dimen.battery_inner_height));
            this.mStrokeWidth = obtainStyledAttributes.getDimension(7, getResources().getDimension(C1075R.dimen.battery_stroke_width));
            this.mCornerRadius = obtainStyledAttributes.getDimension(1, getResources().getDimension(C1075R.dimen.battery_corner_radius));
            this.mNormalColor = obtainStyledAttributes.getColor(0, getResources().getColor(C1075R.color.medium_grey));
            this.mTextColor = obtainStyledAttributes.getColor(8, getResources().getColor(C1075R.color.white));
        } catch (Exception unused) {
            obtainStyledAttributes.recycle();
        }
        this.mColor = this.mNormalColor;
        float f = this.mStrokeWidth / 2.0f;
        this.batteryNipRect = new RectF(0.0f, 0.0f, this.mBatteryNipWidth, this.mBatteryNipHeight);
        this.batteryNipRect.offset(f, f);
        this.batteryRect = new RectF(0.0f, 0.0f, this.mBatteryWidth, this.mBatteryHeight);
        this.batteryRect.offset(f, f);
        this.innerRect = new RectF(0.0f, 0.0f, this.mBatteryInnerWidth, this.mBatteryInnerHeight);
        this.innerRect.offset(f, f);
        this.batteryNipRect.offset(0.0f, (this.batteryRect.height() / 2.0f) - (this.batteryNipRect.height() / 2.0f));
        this.batteryRect.offset(this.batteryNipRect.width(), 0.0f);
        float width = ((this.batteryRect.width() / 2.0f) - (this.innerRect.width() / 2.0f)) + this.batteryNipRect.width();
        this.innerRect.offset(width, (this.batteryRect.height() / 2.0f) - (this.innerRect.height() / 2.0f));
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        this.paint.setColor(this.mColor);
        this.paint.setStrokeWidth(this.mStrokeWidth);
        this.paint.setStyle(Style.STROKE);
        RectF rectF = this.batteryRect;
        float f = this.mCornerRadius;
        canvas.drawRoundRect(rectF, f, f, this.paint);
        RectF rectF2 = this.batteryNipRect;
        float f2 = this.mCornerRadius;
        canvas.drawRoundRect(rectF2, f2, f2, this.paint);
        this.paint.setStyle(Style.FILL);
        RectF rectF3 = this.innerRect;
        float f3 = this.mCornerRadius;
        canvas.drawRoundRect(rectF3, f3, f3, this.paint);
        this.textPaint.setColor(this.mTextColor);
        this.textPaint.setTextSize(this.innerRect.height());
        canvas.drawText("i", (float) (getWidth() / 2), this.innerRect.height(), this.textPaint);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setMeasuredDimension(Float.valueOf(this.batteryRect.width() + this.batteryNipRect.width() + this.mStrokeWidth).intValue(), Float.valueOf(this.batteryRect.height() + this.mStrokeWidth).intValue());
    }

    public void setBatteryPercentage(int i) {
        float f = ((float) i) / 100.0f;
        Resources resources = getResources();
        if (i <= resources.getInteger(C1075R.integer.low_battery_percentage)) {
            this.mColor = resources.getColor(C1075R.color.low_battery);
            this.mTextColor = this.mColor;
            f = 0.0f;
        } else {
            this.mColor = this.mNormalColor;
        }
        RectF rectF = this.innerRect;
        rectF.left = rectF.right - (resources.getDimension(C1075R.dimen.battery_inner_full_width) * f);
        invalidate();
    }

    public void setColor(int i) {
        this.mNormalColor = i;
        this.mColor = i;
        invalidate();
    }

    public void setTextColor(int i) {
        this.mTextColor = i;
    }
}
