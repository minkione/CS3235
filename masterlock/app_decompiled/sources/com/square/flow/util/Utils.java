package com.square.flow.util;

import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;

public final class Utils {

    public interface OnMeasuredCallback {
        void onMeasured(View view, int i, int i2);
    }

    public static void waitForMeasure(final View view, final OnMeasuredCallback onMeasuredCallback) {
        int width = view.getWidth();
        int height = view.getHeight();
        if (width <= 0 || height <= 0) {
            view.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                public boolean onPreDraw() {
                    ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
                    if (viewTreeObserver.isAlive()) {
                        viewTreeObserver.removeOnPreDrawListener(this);
                    }
                    OnMeasuredCallback onMeasuredCallback = onMeasuredCallback;
                    View view = view;
                    onMeasuredCallback.onMeasured(view, view.getWidth(), view.getHeight());
                    return true;
                }
            });
        } else {
            onMeasuredCallback.onMeasured(view, width, height);
        }
    }

    private Utils() {
    }
}
