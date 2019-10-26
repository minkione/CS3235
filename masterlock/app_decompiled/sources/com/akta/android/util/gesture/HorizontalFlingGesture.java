package com.akta.android.util.gesture;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class HorizontalFlingGesture extends SimpleOnGestureListener {
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    public float flingVelocityX;
    private int threshold;

    public HorizontalFlingGesture() {
        this.threshold = 200;
    }

    public HorizontalFlingGesture(int i) {
        if (i > 0) {
            this.threshold = i;
        }
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        this.flingVelocityX = 0.0f;
        if (f < ((float) (-this.threshold)) && f < 0.0f) {
            this.flingVelocityX = f;
            return true;
        } else if (Math.abs(f) <= ((float) this.threshold)) {
            return false;
        } else {
            this.flingVelocityX = f;
            return true;
        }
    }
}
