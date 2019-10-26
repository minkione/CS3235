package com.akta.android.util.gesture;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class VerticalFlingGesture extends SimpleOnGestureListener {
    private static final int SWIPE_THRESHOLD_VELOCITY = 300;
    private int threshold;

    public VerticalFlingGesture() {
        this.threshold = 300;
    }

    public VerticalFlingGesture(int i) {
        if (i > 0) {
            this.threshold = i;
        }
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if ((f2 >= ((float) (-this.threshold)) || f2 >= 0.0f) && Math.abs(f2) <= ((float) this.threshold)) {
            return false;
        }
        return true;
    }
}
