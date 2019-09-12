package com.akta.android.util;

import android.util.Log;

public class LogUtil {
    public static boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final int MAX_LOG_TAG_LENGTH = 20;
    public static final String TAG = makeLogTag(LogUtil.class);
    public static boolean VERBOSE = Log.isLoggable(TAG, 2);

    public static String makeLogTag(String str) {
        return str.length() > 20 ? str.substring(0, 19) : str;
    }

    public static String makeLogTag(Class cls) {
        return makeLogTag(cls.getSimpleName());
    }
}
