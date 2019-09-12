package com.masterlock.ble.app.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.content.ContextCompat;

public class PermissionUtil {
    public static final int REQUEST_CONTACTS_PERMISSION = 11;
    public static final int REQUEST_LOCATION_PERMISSION = 10;

    public enum PermissionType {
        LOCATION("android.permission.ACCESS_FINE_LOCATION"),
        CONTACTS("android.permission.READ_CONTACTS"),
        NOTIFICATIONS("");
        
        String mValue;

        private PermissionType(String str) {
            this.mValue = str;
        }

        public String getValue() {
            return this.mValue;
        }

        public static String[] getAllPermissionValues() {
            String[] strArr = new String[values().length];
            for (int i = 0; i < strArr.length; i++) {
                strArr[i] = values()[i].getValue();
            }
            return strArr;
        }
    }

    public void requestPermission(Activity activity, PermissionType permissionType) {
        int i;
        switch (permissionType) {
            case LOCATION:
                i = 10;
                break;
            case CONTACTS:
                i = 11;
                break;
            default:
                throw new UnsupportedOperationException("Not allowed to request this permission");
        }
        ActivityCompat.requestPermissions(activity, new String[]{permissionType.getValue()}, i);
    }

    public boolean isPermissionGranted(Activity activity, String str) {
        return ContextCompat.checkSelfPermission(activity, str) == 0;
    }

    public void goToApplicationSettingsScreen(Activity activity) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        activity.startActivity(intent);
    }
}
