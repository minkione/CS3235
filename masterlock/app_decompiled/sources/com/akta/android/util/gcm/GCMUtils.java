package com.akta.android.util.gcm;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import com.akta.android.util.LogUtil;

public class GCMUtils {
    private static final String PROPERTY_APP_VERSION = "com.akta.gcm.appVersion";
    public static final String PROPERTY_REG_ID = "com.akta.gcm.registration_id";
    public static final String TAG = TAG;

    public static String getRegistrationId(SharedPreferences sharedPreferences, int i) {
        String string = sharedPreferences.getString(PROPERTY_REG_ID, "");
        if (string != null && "".equals(string) && LogUtil.VERBOSE) {
            Log.v(TAG, "Registration not found.");
        }
        if (sharedPreferences.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE) == i) {
            return string;
        }
        if (LogUtil.VERBOSE) {
            Log.v(TAG, "App version changed.");
        }
        return "";
    }

    public static void storeRegistrationId(SharedPreferences sharedPreferences, int i, String str) {
        if (LogUtil.VERBOSE) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Saving regId on app version ");
            sb.append(i);
            Log.v(str2, sb.toString());
        }
        Editor edit = sharedPreferences.edit();
        edit.putString(PROPERTY_REG_ID, str);
        edit.putInt(PROPERTY_APP_VERSION, i);
        edit.commit();
    }
}
