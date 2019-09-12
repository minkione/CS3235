package p005hr.android.ble.smartlocck.util;

import android.util.Log;

/* renamed from: hr.android.ble.smartlocck.util.LogUtils */
public class LogUtils {
    static String className;
    static int lineNumber;
    static String methodName;

    private LogUtils() {
    }

    public static boolean isDebuggable() {
        return true;
    }

    private static String createLog(String log) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(methodName);
        buffer.append(":");
        buffer.append(lineNumber);
        buffer.append("]------>");
        buffer.append(log);
        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = "--->" + sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    /* renamed from: i */
    public static void m17i(String message) {
        if (isDebuggable()) {
            getMethodNames(new Throwable().getStackTrace());
            Log.i(className, createLog(message));
        }
    }

    /* renamed from: i */
    public static void m16i(int message) {
        m17i(new StringBuilder(String.valueOf(message)).toString());
    }

    /* renamed from: e */
    public static void m15e(String message) {
        if (isDebuggable()) {
            getMethodNames(new Throwable().getStackTrace());
            Log.e(className, createLog(message));
        }
    }

    /* renamed from: e */
    public static void m14e(int message) {
        m15e(new StringBuilder(String.valueOf(message)).toString());
    }

    /* renamed from: d */
    public static void m13d(String message) {
        if (isDebuggable()) {
            getMethodNames(new Throwable().getStackTrace());
            Log.d(className, createLog(message));
        }
    }

    /* renamed from: d */
    public static void m12d(int message) {
        m13d(new StringBuilder(String.valueOf(message)).toString());
    }

    /* renamed from: v */
    public static void m19v(String message) {
        if (isDebuggable()) {
            getMethodNames(new Throwable().getStackTrace());
            Log.v(className, createLog(message));
        }
    }

    /* renamed from: v */
    public static void m18v(int message) {
        m19v(new StringBuilder(String.valueOf(message)).toString());
    }

    /* renamed from: w */
    public static void m21w(String message) {
        if (isDebuggable()) {
            getMethodNames(new Throwable().getStackTrace());
            Log.w(className, createLog(message));
        }
    }

    /* renamed from: w */
    public static void m20w(int message) {
        m21w(new StringBuilder(String.valueOf(message)).toString());
    }

    public static void wtf(String message) {
        if (isDebuggable()) {
            getMethodNames(new Throwable().getStackTrace());
            Log.wtf(className, createLog(message));
        }
    }

    public static void wtf(int message) {
        wtf(new StringBuilder(String.valueOf(message)).toString());
    }
}
