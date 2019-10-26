package com.akta.android.p004ui.sliding;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/* renamed from: com.akta.android.ui.sliding.SlidingLayout */
public abstract class SlidingLayout extends LinearLayout {
    private float paneFraction;
    private int screenWidth;
    private float xFraction;

    public abstract void setPaneWidth();

    public SlidingLayout(Context context) {
        super(context);
        shared();
    }

    public SlidingLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        shared();
    }

    private void shared() {
        setPaneWidth();
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.screenWidth = i;
        setX((float) this.screenWidth);
    }

    public float getXFraction() {
        return this.xFraction;
    }

    public void setXFraction(float f) {
        this.xFraction = f;
        int i = this.screenWidth;
        setX(i > 0 ? ((float) i) - (f * ((float) i)) : 0.0f);
    }

    public void setPaneFraction(float f) {
        this.paneFraction = f;
    }
}
