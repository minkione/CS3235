package com.masterlock.ble.app.provider.builder;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.database.Cursor;
import com.masterlock.ble.app.provider.MasterlockContract.DeviceInfo;
import com.masterlock.ble.app.provider.MasterlockContract.SyncColumns;
import com.masterlock.core.DeviceInformation;
import com.masterlock.core.Lock;
import java.util.ArrayList;

public class DeviceInfoBuilder {
    private DeviceInfoBuilder() {
    }

    public static ContentValues buildContentValues(Lock lock) {
        ContentValues buildBaseContentValues = buildBaseContentValues(lock);
        buildBaseContentValues.put(DeviceInfoColumns.MAC_ADDRESS, lock.getMacAddress());
        buildBaseContentValues.put(DeviceInfoColumns.RSSI_THRESHOLD, lock.getRssiThreshold());
        return buildBaseContentValues;
    }

    public static ContentValues buildBaseContentValues(Lock lock) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SyncColumns.UPDATED, Long.valueOf(System.currentTimeMillis()));
        contentValues.put(DeviceInfoColumns.DEVICE_INFO_LOCK_ID, lock.getLockId());
        return contentValues;
    }

    public static ContentProviderOperation buildContentProviderOperation(Lock lock, long j) {
        return ContentProviderOperation.newInsert(DeviceInfo.CONTENT_URI).withValue(SyncColumns.UPDATED, Long.valueOf(System.currentTimeMillis())).withValue(DeviceInfoColumns.DEVICE_INFO_LOCK_ID, lock.getLockId()).withValue(DeviceInfoColumns.MAC_ADDRESS, lock.getMacAddress()).withValue(DeviceInfoColumns.RSSI_THRESHOLD, lock.getRssiThreshold()).build();
    }

    public static ArrayList<DeviceInformation> buildDeviceInfos(Cursor cursor) {
        ArrayList<DeviceInformation> arrayList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            arrayList.add(buildDeviceInfo(cursor));
            cursor.moveToNext();
        }
        return arrayList;
    }

    public static DeviceInformation buildDeviceInfo(Cursor cursor) {
        DeviceInformation deviceInformation = new DeviceInformation();
        deviceInformation.setLockId(cursor.getString(cursor.getColumnIndexOrThrow(DeviceInfoColumns.DEVICE_INFO_LOCK_ID)));
        deviceInformation.setMacAddress(buildMacAddress(cursor));
        deviceInformation.setRssiThreshold(buildRssiThreshold(cursor));
        return deviceInformation;
    }

    public static String buildMacAddress(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(DeviceInfoColumns.MAC_ADDRESS));
    }

    public static Integer buildRssiThreshold(Cursor cursor) {
        String string = cursor.getString(cursor.getColumnIndexOrThrow(DeviceInfoColumns.RSSI_THRESHOLD));
        if (string == null) {
            string = "0";
        }
        return Integer.valueOf(Integer.parseInt(string));
    }
}
