package com.akta.android.util;

import android.content.Context;
import android.os.Build.VERSION;

public class CameraUtils {
    public static boolean hasCamera(Context context) {
        return VERSION.SDK_INT >= 5 && context.getPackageManager().hasSystemFeature("android.hardware.camera");
    }
}
