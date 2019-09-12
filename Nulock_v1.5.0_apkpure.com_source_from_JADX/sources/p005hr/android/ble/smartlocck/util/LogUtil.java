package p005hr.android.ble.smartlocck.util;

import android.util.Log;

/* renamed from: hr.android.ble.smartlocck.util.LogUtil */
public class LogUtil {
    private static final String TAG = "hurui";
    public static final boolean debug = true;

    /* renamed from: v */
    public static void m9v(String tag, String msg) {
        Log.v(tag, msg);
    }

    /* renamed from: v */
    public static void m8v(String msg) {
        Log.v(TAG, msg);
    }

    /* renamed from: d */
    public static void m3d(String tag, String msg) {
        Log.d(tag, msg);
    }

    /* renamed from: d */
    public static void m2d(String msg) {
        Log.d(TAG, msg);
    }

    /* renamed from: i */
    public static void m7i(String tag, String msg) {
        Log.i(tag, msg);
    }

    /* renamed from: i */
    public static void m6i(String msg) {
        Log.i(TAG, msg);
    }

    /* renamed from: w */
    public static void m11w(String tag, String msg) {
        Log.w(tag, msg);
    }

    /* renamed from: w */
    public static void m10w(String msg) {
        Log.w(TAG, msg);
    }

    /* renamed from: e */
    public static void m5e(String tag, String msg) {
        Log.e(tag, msg);
    }

    /* renamed from: e */
    public static void m4e(String msg) {
        Log.e(TAG, msg);
    }
}
