package com.masterlock.ble.app.provider.builder;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.database.Cursor;
import com.masterlock.ble.app.provider.MasterlockContract.Calibration;
import com.masterlock.core.CalibrationInfo;
import com.masterlock.core.CalibrationInfo.Builder;
import java.util.ArrayList;

public class CalibrationInfoBuilder {
    private CalibrationInfoBuilder() {
    }

    public static ContentValues buildContentValues(CalibrationInfo calibrationInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", calibrationInfo.getId());
        contentValues.put("kms_device_id", calibrationInfo.getKmsDeviceId());
        contentValues.put(CalibrationColumns.CALIBRATION_SKIP, Boolean.valueOf(calibrationInfo.hasSkipped()));
        contentValues.put("value", Integer.valueOf(calibrationInfo.getValue()));
        return contentValues;
    }

    public static ContentProviderOperation buildContentProviderOperation(CalibrationInfo calibrationInfo) {
        return ContentProviderOperation.newInsert(Calibration.CONTENT_URI).withValue("id", calibrationInfo.getId()).withValue("kms_device_id", calibrationInfo.getKmsDeviceId()).withValue(CalibrationColumns.CALIBRATION_SKIP, Boolean.valueOf(calibrationInfo.hasSkipped())).withValue("value", Integer.valueOf(calibrationInfo.getValue())).build();
    }

    public static ArrayList<CalibrationInfo> buildCalibrationInfoList(Cursor cursor) {
        ArrayList<CalibrationInfo> arrayList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            arrayList.add(buildCalibrationInfo(cursor));
            cursor.moveToNext();
        }
        return arrayList;
    }

    public static CalibrationInfo buildCalibrationInfo(Cursor cursor) {
        Builder kmsDeviceId = new Builder().setId(cursor.getString(cursor.getColumnIndexOrThrow("id"))).setKmsDeviceId(cursor.getString(cursor.getColumnIndexOrThrow("kms_device_id")));
        boolean z = true;
        if (cursor.getInt(cursor.getColumnIndexOrThrow(CalibrationColumns.CALIBRATION_SKIP)) != 1) {
            z = false;
        }
        return kmsDeviceId.setHasSkipped(z).setValue(buildCalibrationValue(cursor).intValue()).build();
    }

    private static Integer buildCalibrationValue(Cursor cursor) {
        String string = cursor.getString(cursor.getColumnIndexOrThrow("value"));
        if (string == null) {
            string = "0";
        }
        return Integer.valueOf(Integer.parseInt(string));
    }
}
