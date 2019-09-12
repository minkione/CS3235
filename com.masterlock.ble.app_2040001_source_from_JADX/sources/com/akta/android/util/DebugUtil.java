package com.akta.android.util;

import android.content.pm.ApplicationInfo;
import android.os.Build.VERSION;
import android.os.StrictMode;
import android.util.Log;

public class DebugUtil {
    public static final String TAG = LogUtil.makeLogTag(TAG);

    public static void strictMode(ApplicationInfo applicationInfo) {
        int i = applicationInfo.flags & 2;
        applicationInfo.flags = i;
        if (!(i != 0)) {
            return;
        }
        if (VERSION.SDK_INT >= 9) {
            StrictMode.enableDefaults();
        } else if (LogUtil.DEBUG) {
            Log.e(TAG, "Your device does not support strict mode");
        }
    }
}
