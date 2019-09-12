package com.masterlock.ble.app.provider.builder;

import android.database.Cursor;
import com.masterlock.core.GuestInterface;
import com.masterlock.core.GuestPermissions;
import com.masterlock.core.ScheduleType;

public class PermissionsBuilder {
    public static GuestPermissions buildGuestPermissions(Cursor cursor) {
        GuestPermissions guestPermissions = new GuestPermissions();
        guestPermissions.setStartAtDate(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.PERMISSION_START_AT_DATE)));
        guestPermissions.setExpiresAtDate(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.PERMISSION_EXPIRES_AT_DATE)));
        boolean z = false;
        guestPermissions.setMonday(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.PERMISSION_MONDAY)) == 1);
        guestPermissions.setTuesday(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.PERMISSION_TUESDAY)) == 1);
        guestPermissions.setWednesday(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.PERMISSION_WEDNESDAY)) == 1);
        guestPermissions.setThursday(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.PERMISSION_THURSDAY)) == 1);
        guestPermissions.setFriday(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.PERMISSION_FRIDAY)) == 1);
        guestPermissions.setSaturday(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.PERMISSION_SATURDAY)) == 1);
        guestPermissions.setSunday(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.PERMISSION_SUNDAY)) == 1);
        guestPermissions.setViewLastKnownLocationPermission(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.PERMISSION_VIEW_LAST_KNOWN_LOCATION_PERMISSION)) == 1);
        guestPermissions.setViewTemporaryCodePermission(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.PERMISSION_VIEW_TEMPORARY_PERMISSION)) == 1);
        if (cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.PERMISSION_OPEN_SHACKLE_PERMISSION)) == 1) {
            z = true;
        }
        guestPermissions.setOpenShacklePermission(z);
        guestPermissions.setScheduleType(ScheduleType.fromKey(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.PERMISSION_SCHEDULE_TYPE))));
        guestPermissions.setGuestInterface(GuestInterface.fromKey(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.PERMISSION_VIEW_MODE))));
        return guestPermissions;
    }
}
