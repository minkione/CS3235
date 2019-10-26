package com.akta.android.util.log;

import android.util.Log;

public class LogWrapper {
    public static void LOGE(String str, String str2) {
    }

    public static void LOGE(String str, String str2, Throwable th) {
    }

    public static void LOGI(String str, String str2) {
    }

    public static void LOGI(String str, String str2, Throwable th) {
    }

    public static void LOGV(String str, String str2) {
    }

    public static void LOGV(String str, String str2, Throwable th) {
    }

    public static void LOGW(String str, String str2) {
    }

    public static void LOGW(String str, String str2, Throwable th) {
    }

    private LogWrapper() {
    }

    public static void LOGD(String str, String str2) {
        if (Log.isLoggable(str, 3)) {
            Log.d(str, str2);
        }
    }

    public static void LOGD(String str, String str2, Throwable th) {
        if (Log.isLoggable(str, 3)) {
            Log.d(str, str2, th);
        }
    }
}
