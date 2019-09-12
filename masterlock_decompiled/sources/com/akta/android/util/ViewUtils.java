package com.akta.android.util;

import android.app.Activity;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

public class ViewUtils {
    public static void sendViewToBack(View view) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(view);
            viewGroup.addView(view, 0);
        }
    }

    public static DisplayMetrics getMetrics(Activity activity) {
        Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static float getDisplayWidthInPixels(Activity activity) {
        return (float) getMetrics(activity).widthPixels;
    }

    public static float getDisplayHeightInPixels(Activity activity) {
        return (float) getMetrics(activity).heightPixels;
    }

    public static void setHardwareAccelerated(View view, boolean z) {
        if (VERSION.SDK_INT < 11) {
            return;
        }
        if (z) {
            view.setLayerType(2, null);
        } else {
            view.setLayerType(1, null);
        }
    }

    public static void removeGlobalLayoutListener(View view, OnGlobalLayoutListener onGlobalLayoutListener) {
        if (VERSION.SDK_INT >= 16) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        } else {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(onGlobalLayoutListener);
        }
    }
}
