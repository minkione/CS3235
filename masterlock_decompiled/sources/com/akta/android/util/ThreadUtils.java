package com.akta.android.util;

import android.os.Looper;

public class ThreadUtils {
    public static void errorOnUIThread() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("This method should not be called on the main thread");
        }
    }
}
