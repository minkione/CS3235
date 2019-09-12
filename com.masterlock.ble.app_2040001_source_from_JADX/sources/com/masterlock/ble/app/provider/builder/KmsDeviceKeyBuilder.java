package com.masterlock.ble.app.provider.builder;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.database.Cursor;
import com.masterlock.ble.app.provider.MasterlockContract.Keys;
import com.masterlock.ble.app.provider.MasterlockContract.SyncColumns;
import com.masterlock.core.AccessType;
import com.masterlock.core.KmsDeviceKey;
import com.masterlock.core.KmsDeviceKey.Builder;
import java.util.ArrayList;

public class KmsDeviceKeyBuilder {
    private KmsDeviceKeyBuilder() {
    }

    public static ContentValues buildContentValues(KmsDeviceKey kmsDeviceKey) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SyncColumns.UPDATED, Long.valueOf(System.currentTimeMillis()));
        contentValues.put(KeysColumns.KEY_ID, kmsDeviceKey.getId());
        contentValues.put("kms_device_id", kmsDeviceKey.getKmsDeviceId());
        contentValues.put(KeysColumns.DEVICE_ID, kmsDeviceKey.getDeviceId());
        contentValues.put(KeysColumns.KEY_VALUE, kmsDeviceKey.getValue());
        contentValues.put(KeysColumns.PROFILE_VALUE, kmsDeviceKey.getProfile());
        contentValues.put(KeysColumns.KEY_USER_TYPE, Integer.valueOf(kmsDeviceKey.getUserType().getValue()));
        contentValues.put(KeysColumns.USER_ID, kmsDeviceKey.getUserId());
        contentValues.put("alias", Integer.valueOf(kmsDeviceKey.getAlias()));
        contentValues.put(KeysColumns.PRODUCT_INVITATION_ID, kmsDeviceKey.getProductInvitationId());
        contentValues.put(KeysColumns.KEY_EXPIRES_ON, kmsDeviceKey.getExpiresOn());
        contentValues.put(KeysColumns.KEY_CREATED_ON, kmsDeviceKey.getCreatedOn());
        contentValues.put(KeysColumns.KEY_MODIFIED_ON, kmsDeviceKey.getModifiedOn());
        return contentValues;
    }

    public static ContentProviderOperation buildContentProviderOperation(KmsDeviceKey kmsDeviceKey, long j) {
        return ContentProviderOperation.newInsert(Keys.CONTENT_URI).withValue(SyncColumns.UPDATED, Long.valueOf(j)).withValue(KeysColumns.KEY_ID, kmsDeviceKey.getId()).withValue("kms_device_id", kmsDeviceKey.getKmsDeviceId()).withValue(KeysColumns.DEVICE_ID, kmsDeviceKey.getDeviceId()).withValue(KeysColumns.KEY_VALUE, kmsDeviceKey.getValue()).withValue(KeysColumns.PROFILE_VALUE, kmsDeviceKey.getProfile()).withValue(KeysColumns.KEY_USER_TYPE, Integer.valueOf(kmsDeviceKey.getUserType().getValue())).withValue(KeysColumns.USER_ID, kmsDeviceKey.getUserId()).withValue("alias", Integer.valueOf(kmsDeviceKey.getAlias())).withValue(KeysColumns.PRODUCT_INVITATION_ID, kmsDeviceKey.getProductInvitationId()).withValue(KeysColumns.KEY_EXPIRES_ON, kmsDeviceKey.getExpiresOn()).withValue(KeysColumns.KEY_CREATED_ON, kmsDeviceKey.getCreatedOn()).withValue(KeysColumns.KEY_MODIFIED_ON, kmsDeviceKey.getModifiedOn()).build();
    }

    public static ArrayList<KmsDeviceKey> buildKmsDeviceKeys(Cursor cursor) {
        ArrayList<KmsDeviceKey> arrayList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            arrayList.add(buildKmsDeviceKey(cursor));
            cursor.moveToNext();
        }
        return arrayList;
    }

    public static KmsDeviceKey buildKmsDeviceKey(Cursor cursor) {
        return new Builder().mo20008id(cursor.getString(cursor.getColumnIndexOrThrow(KeysColumns.KEY_ID))).kmsDeviceId(cursor.getString(cursor.getColumnIndexOrThrow("kms_device_id"))).deviceId(cursor.getString(cursor.getColumnIndexOrThrow(KeysColumns.DEVICE_ID))).value(cursor.getString(cursor.getColumnIndexOrThrow(KeysColumns.KEY_VALUE))).profile(cursor.getString(cursor.getColumnIndexOrThrow(KeysColumns.PROFILE_VALUE))).userType(AccessType.fromKey(cursor.getInt(cursor.getColumnIndexOrThrow(KeysColumns.KEY_USER_TYPE)))).alias(cursor.getInt(cursor.getColumnIndexOrThrow("alias"))).userId(cursor.getString(cursor.getColumnIndexOrThrow(KeysColumns.KEY_USER_TYPE))).alias(cursor.getInt(cursor.getColumnIndexOrThrow("alias"))).productInvitationId(cursor.getString(cursor.getColumnIndexOrThrow(KeysColumns.PRODUCT_INVITATION_ID))).expiresOn(cursor.getString(cursor.getColumnIndexOrThrow(KeysColumns.KEY_EXPIRES_ON))).createdOn(cursor.getString(cursor.getColumnIndexOrThrow(KeysColumns.KEY_CREATED_ON))).modifiedOn(cursor.getString(cursor.getColumnIndexOrThrow(KeysColumns.KEY_MODIFIED_ON))).build();
    }
}
