package com.masterlock.ble.app.util;

import android.content.Context;
import android.os.Build;
import java.io.File;

public class VerifyDeviceUtil {
    private static String PLAY_STORE_APP_ID = "com.android.vending";

    public static boolean verifyInstaller(Context context) {
        String installerPackageName = context.getPackageManager().getInstallerPackageName(context.getPackageName());
        return installerPackageName != null && installerPackageName.startsWith(PLAY_STORE_APP_ID);
    }

    public static boolean isRooted() {
        String str = Build.TAGS;
        boolean z = true;
        if (str != null && str.contains("test-keys")) {
            return true;
        }
        try {
            if (new File("/system/app/Superuser.apk").exists()) {
                return true;
            }
        } catch (Exception unused) {
        }
        if (!canExecuteCommand("/system/xbin/which su") || !canExecuteCommand("/system/bin/which su") || !canExecuteCommand("which su")) {
            z = false;
        }
        return z;
    }

    private static boolean canExecuteCommand(String str) {
        try {
            Runtime.getRuntime().exec(str);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }
}
