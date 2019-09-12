package com.masterlock.ble.app.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class LandingScrollView extends ScrollView {
    private boolean isScrollable = true;

    public boolean isScrollable() {
        return this.isScrollable;
    }

    public void setIsScrollable(boolean z) {
        this.isScrollable = z;
    }

    public LandingScrollView(Context context) {
        super(context);
    }

    public LandingScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public LandingScrollView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.isScrollable && super.onTouchEvent(motionEvent);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.isScrollable && super.onInterceptTouchEvent(motionEvent);
    }
}
