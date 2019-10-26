package com.masterlock.ble.app.provider.builder;

import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentValues;
import android.database.Cursor;
import com.masterlock.ble.app.provider.MasterlockContract.Logs;
import com.masterlock.ble.app.provider.MasterlockContract.SyncColumns;
import com.masterlock.core.EventSource;
import com.masterlock.core.KmsLogEntry;
import com.masterlock.core.audit.events.EventCode;
import java.util.ArrayList;

public class LogBuilder {
    private LogBuilder() {
    }

    public static ContentValues buildContentValues(KmsLogEntry kmsLogEntry) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SyncColumns.UPDATED, Long.valueOf(System.currentTimeMillis()));
        contentValues.put(LogColumns.LOG_ID, kmsLogEntry.getId());
        contentValues.put("kms_device_id", kmsLogEntry.getKmsDeviceId());
        contentValues.put(LogColumns.KMS_DEVICE_KEY_ALIAS, kmsLogEntry.getKmsDeviceKeyAlias());
        contentValues.put(LogColumns.REFERENCE_ID, Long.valueOf(kmsLogEntry.getEventIndex()));
        contentValues.put("firmware_counter", kmsLogEntry.getFirmwareCounter());
        String str = null;
        contentValues.put(LogColumns.EVENT_CODE, kmsLogEntry.getEventCode() == null ? null : kmsLogEntry.getEventCode().toString());
        String str2 = LogColumns.EVENT_SOURCE;
        if (kmsLogEntry.getEventSource() != null) {
            str = kmsLogEntry.getEventSource().toString();
        }
        contentValues.put(str2, str);
        contentValues.put(LogColumns.EVENT_VALUE, kmsLogEntry.getEventValue());
        contentValues.put("alias", kmsLogEntry.getAlias());
        contentValues.put("message", kmsLogEntry.getMessage());
        contentValues.put("created_on", kmsLogEntry.getCreatedOn());
        return contentValues;
    }

    public static ContentProviderOperation buildContentProviderOperation(KmsLogEntry kmsLogEntry, long j) {
        String str = null;
        Builder withValue = ContentProviderOperation.newInsert(Logs.CONTENT_URI).withValue(SyncColumns.UPDATED, Long.valueOf(j)).withValue(LogColumns.LOG_ID, kmsLogEntry.getId()).withValue("kms_device_id", kmsLogEntry.getKmsDeviceId()).withValue(LogColumns.KMS_DEVICE_KEY_ALIAS, kmsLogEntry.getKmsDeviceKeyAlias()).withValue(LogColumns.REFERENCE_ID, Long.valueOf(kmsLogEntry.getEventIndex())).withValue("firmware_counter", kmsLogEntry.getFirmwareCounter()).withValue(LogColumns.EVENT_CODE, kmsLogEntry.getEventCode() == null ? null : kmsLogEntry.getEventCode().toString());
        String str2 = LogColumns.EVENT_SOURCE;
        if (kmsLogEntry.getEventSource() != null) {
            str = kmsLogEntry.getEventSource().toString();
        }
        return withValue.withValue(str2, str).withValue(LogColumns.EVENT_VALUE, kmsLogEntry.getEventValue()).withValue("alias", kmsLogEntry.getAlias()).withValue("message", kmsLogEntry.getMessage()).withValue("created_on", kmsLogEntry.getCreatedOn()).build();
    }

    public static ArrayList<KmsLogEntry> buildKmsLogEntries(Cursor cursor) {
        ArrayList<KmsLogEntry> arrayList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            arrayList.add(buildKmsLogEntry(cursor));
            cursor.moveToNext();
        }
        return arrayList;
    }

    public static KmsLogEntry buildKmsLogEntry(Cursor cursor) {
        KmsLogEntry kmsLogEntry = new KmsLogEntry();
        String string = cursor.getString(cursor.getColumnIndexOrThrow(LogColumns.EVENT_CODE));
        if (string != null) {
            try {
                kmsLogEntry.setEventCode(EventCode.getEventCode(string));
            } catch (IllegalArgumentException unused) {
            }
        }
        String string2 = cursor.getString(cursor.getColumnIndexOrThrow(LogColumns.EVENT_SOURCE));
        if (string2 != null) {
            try {
                kmsLogEntry.setEventSource(EventSource.valueOf(string2));
            } catch (IllegalArgumentException unused2) {
            }
        }
        kmsLogEntry.setId(cursor.getString(cursor.getColumnIndexOrThrow(LogColumns.LOG_ID)));
        kmsLogEntry.setKmsDeviceId(cursor.getString(cursor.getColumnIndexOrThrow("kms_device_id")));
        kmsLogEntry.setKmsDeviceKeyAlias(Integer.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(LogColumns.KMS_DEVICE_KEY_ALIAS))));
        kmsLogEntry.setEventIndex(Long.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(LogColumns.REFERENCE_ID))));
        kmsLogEntry.setFirmwareCounter(Integer.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("firmware_counter"))));
        kmsLogEntry.setEventValue(cursor.getString(cursor.getColumnIndexOrThrow(LogColumns.EVENT_VALUE)));
        kmsLogEntry.setAlias(cursor.getString(cursor.getColumnIndexOrThrow("alias")));
        kmsLogEntry.setMessage(cursor.getString(cursor.getColumnIndexOrThrow("message")));
        kmsLogEntry.setCreatedOn(cursor.getString(cursor.getColumnIndexOrThrow("created_on")));
        return kmsLogEntry;
    }
}
