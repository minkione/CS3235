package com.masterlock.ble.app.gamma;

import android.os.Build.VERSION;

public class VersionUtils {
    public static boolean isAtLeastL() {
        return VERSION.SDK_INT >= 21;
    }
}
