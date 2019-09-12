package com.masterlock.ble.app.util;

import android.os.Looper;

public class ThreadUtil {
    private ThreadUtil() {
    }

    public static void errorOnUIThread() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("This method should not be called on the main thread");
        }
    }
}
