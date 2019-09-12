package com.masterlock.ble.app.provider.builder;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.database.Cursor;
import com.masterlock.ble.app.provider.MasterlockContract.AvailableSettings;
import com.masterlock.core.AvailableSetting;
import com.masterlock.core.AvailableSetting.Builder;
import com.masterlock.core.AvailableSettingType;
import java.util.ArrayList;

public class AvailableSettingBuilder {
    private AvailableSettingBuilder() {
    }

    public static ContentValues buildContentValues(AvailableSetting availableSetting) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(AvailableSettingsColumns.AVAILABLE_SETTING_UUID, availableSetting.getUuid());
        contentValues.put("kms_device_id", availableSetting.getKmsDeviceId());
        contentValues.put(AvailableSettingsColumns.AVAILABLE_SETTING_ID, Integer.valueOf(availableSetting.getId().getValue()));
        contentValues.put(AvailableSettingsColumns.AVAILABLE_SETTING_ADDRESS, Integer.valueOf(availableSetting.getAddress()));
        contentValues.put(AvailableSettingsColumns.AVAILABLE_SETTING_SIZE, Integer.valueOf(availableSetting.getSize()));
        return contentValues;
    }

    public static ContentProviderOperation buildContentProviderOperation(AvailableSetting availableSetting) {
        return ContentProviderOperation.newInsert(AvailableSettings.CONTENT_URI).withValue(AvailableSettingsColumns.AVAILABLE_SETTING_UUID, availableSetting.getUuid()).withValue("kms_device_id", availableSetting.getKmsDeviceId()).withValue(AvailableSettingsColumns.AVAILABLE_SETTING_ID, Integer.valueOf(availableSetting.getId().getValue())).withValue(AvailableSettingsColumns.AVAILABLE_SETTING_ADDRESS, Integer.valueOf(availableSetting.getAddress())).withValue(AvailableSettingsColumns.AVAILABLE_SETTING_SIZE, Integer.valueOf(availableSetting.getSize())).build();
    }

    public static ArrayList<AvailableSetting> buildAvailableSettings(Cursor cursor) {
        ArrayList<AvailableSetting> arrayList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            arrayList.add(buildAvailableSetting(cursor));
            cursor.moveToNext();
        }
        return arrayList;
    }

    public static AvailableSetting buildAvailableSetting(Cursor cursor) {
        return new Builder().setId(AvailableSettingType.fromKey(cursor.getInt(cursor.getColumnIndexOrThrow(AvailableSettingsColumns.AVAILABLE_SETTING_ID)))).setAddress(cursor.getInt(cursor.getColumnIndexOrThrow(AvailableSettingsColumns.AVAILABLE_SETTING_ADDRESS))).setSize(cursor.getInt(cursor.getColumnIndexOrThrow(AvailableSettingsColumns.AVAILABLE_SETTING_SIZE))).setKmsDeviceId(cursor.getString(cursor.getColumnIndexOrThrow("kms_device_id"))).build();
    }
}
