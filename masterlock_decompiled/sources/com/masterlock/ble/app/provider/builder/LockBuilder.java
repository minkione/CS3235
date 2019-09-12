package com.masterlock.ble.app.provider.builder;

import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentValues;
import android.database.Cursor;
import com.masterlock.ble.app.provider.MasterlockContract.Locks;
import com.masterlock.ble.app.provider.MasterlockContract.SyncColumns;
import com.masterlock.core.AccessType;
import com.masterlock.core.Lock;
import com.masterlock.core.Lock.LockType;
import com.masterlock.core.LockMode;
import com.masterlock.core.LockStatus;
import com.masterlock.core.ShackleStatus;
import java.util.ArrayList;

public class LockBuilder {
    private LockBuilder() {
    }

    public static ContentValues buildContentValues(Lock lock) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SyncColumns.UPDATED, Long.valueOf(System.currentTimeMillis()));
        contentValues.put("lock_id", lock.getLockId());
        contentValues.put(LocksColumns.LOCK_NAME, lock.getName());
        contentValues.put(LocksColumns.LOCK_NOTES, lock.getNotes());
        contentValues.put(LocksColumns.LOCK_LABEL, lock.getLabel());
        contentValues.put(LocksColumns.IS_FAVORITE, Integer.valueOf(lock.isFavorite() ? 1 : 0));
        contentValues.put(LocksColumns.USER_TYPE, Integer.valueOf(lock.getAccessType().getValue()));
        contentValues.put("created_on", lock.getCreatedOn());
        contentValues.put(LocksColumns.MODIFIED_ON, lock.getModifiedOn());
        contentValues.put(LocksColumns.MODEL_ID, lock.getModelId());
        contentValues.put(LocksColumns.MODEL_INTERFACE_ID, Integer.valueOf(lock.getModelInterfaceId()));
        contentValues.put(LocksColumns.MODEL_NUMBER, lock.getModelNumber());
        contentValues.put(LocksColumns.MODEL_SKU, lock.getModelSku());
        contentValues.put(LocksColumns.MODEL_NAME, lock.getModelName());
        contentValues.put(LocksColumns.MODEL_DESCRIPTION, lock.getModelDescription());
        contentValues.put(LocksColumns.KMS_ID, lock.getKmsId());
        contentValues.put(LocksColumns.FIRMWARE_VERSION, Integer.valueOf(lock.getFirmwareVersion()));
        contentValues.put("firmware_counter", Integer.valueOf(lock.getFirmwareCounter()));
        contentValues.put(LocksColumns.PRIMARY_CODE, lock.getPrimaryCode());
        contentValues.put(LocksColumns.SECONDARY_CODE_1, lock.getSecondaryCode1() != null ? lock.getSecondaryCode1() : "");
        contentValues.put(LocksColumns.SECONDARY_CODE_2, lock.getSecondaryCode2() != null ? lock.getSecondaryCode2() : "");
        contentValues.put(LocksColumns.SECONDARY_CODE_3, lock.getSecondaryCode3() != null ? lock.getSecondaryCode3() : "");
        contentValues.put(LocksColumns.SECONDARY_CODE_4, lock.getSecondaryCode4() != null ? lock.getSecondaryCode4() : "");
        contentValues.put(LocksColumns.SECONDARY_CODE_5, lock.getSecondaryCode5() != null ? lock.getSecondaryCode5() : "");
        contentValues.put(LocksColumns.BATTERY_LEVEL, Integer.valueOf(lock.getRemainingBatteryPercentage()));
        contentValues.put(LocksColumns.TEMPERATURE, Integer.valueOf(lock.getTemperature()));
        contentValues.put(LocksColumns.TX_POWER, Integer.valueOf(lock.getTxPower()));
        contentValues.put(LocksColumns.TX_INTERVAL, Integer.valueOf(lock.getTxInterval()));
        contentValues.put(LocksColumns.LOCK_MODE, Integer.valueOf(lock.getLockMode().getValue()));
        contentValues.put(LocksColumns.RELOCK_TIME, Integer.valueOf(lock.getRelockTimeInSeconds()));
        contentValues.put(LocksColumns.LOCK_STATUS, Integer.valueOf(lock.getLockStatus().getValue()));
        contentValues.put(LocksColumns.SHACKLE_STATUS, Integer.valueOf(lock.getShackleStatus().getValue()));
        contentValues.put(LocksColumns.LOCKER_MODE, Integer.valueOf(lock.isLockerMode() ? 1 : 0));
        contentValues.put(LocksColumns.TIMEZONE, lock.getTimezone());
        contentValues.put(LocksColumns.SERVICE_CODE, lock.getServiceCode());
        contentValues.put(LocksColumns.SERVICE_CODE_EXPIRATION, lock.getServiceCodeExpiresOn());
        contentValues.put(LocksColumns.LAST_LOG_REFERENCE_ID, Long.valueOf(lock.getLastLogReferenceId()));
        contentValues.put(LocksColumns.TOUCH_MODE_CONFIGURATION, lock.getTouchModeConfiguration());
        contentValues.put(LocksColumns.PROXIMITY_SWIPE_MODE_CONFIGURATION, lock.getProximitySwipeModeConfiguration());
        contentValues.put(LocksColumns.KMS_CREATED_ON, lock.getKmsCreatedOn());
        contentValues.put(LocksColumns.KMS_MODIFIED_ON, lock.getKmsModifiedOn());
        contentValues.put(LocksColumns.PUBLIC_CONFIG_COUNTER, Integer.valueOf(lock.getPublicConfigCounter()));
        contentValues.put(LocksColumns.PRIMARY_PASSCODE_COUNTER, Long.valueOf(lock.getPrimaryCodeCounter()));
        contentValues.put(LocksColumns.MEASUREMENT_COUNTER, Integer.valueOf(lock.getMeasurementCounter()));
        contentValues.put(LocksColumns.SECONDARY_PASSCODE_COUNTER, Long.valueOf(lock.getSecondaryCodeCounter()));
        contentValues.put(LocksColumns.LAST_UNLOCKED, Long.valueOf(lock.getLastUnlocked()));
        contentValues.put(LocksColumns.LAST_UNLOCKED_SHACKLE, Long.valueOf(lock.getLastUnlockedShackle()));
        contentValues.put(LocksColumns.LATITUDE, lock.getLatitude());
        contentValues.put(LocksColumns.LONGITUDE, lock.getLongitude());
        contentValues.put(LocksColumns.PERMISSION_START_AT_DATE, lock.getPermissions().getStartAtDate());
        contentValues.put(LocksColumns.PERMISSION_EXPIRES_AT_DATE, lock.getPermissions().getExpiresAtDate());
        contentValues.put(LocksColumns.PERMISSION_MONDAY, Integer.valueOf(lock.getPermissions().isMonday() ? 1 : 0));
        contentValues.put(LocksColumns.PERMISSION_TUESDAY, Integer.valueOf(lock.getPermissions().isTuesday() ? 1 : 0));
        contentValues.put(LocksColumns.PERMISSION_WEDNESDAY, Integer.valueOf(lock.getPermissions().isWednesday() ? 1 : 0));
        contentValues.put(LocksColumns.PERMISSION_THURSDAY, Integer.valueOf(lock.getPermissions().isThursday() ? 1 : 0));
        contentValues.put(LocksColumns.PERMISSION_FRIDAY, Integer.valueOf(lock.getPermissions().isFriday() ? 1 : 0));
        contentValues.put(LocksColumns.PERMISSION_SATURDAY, Integer.valueOf(lock.getPermissions().isSaturday() ? 1 : 0));
        contentValues.put(LocksColumns.PERMISSION_SUNDAY, Integer.valueOf(lock.getPermissions().isSunday() ? 1 : 0));
        contentValues.put(LocksColumns.PERMISSION_VIEW_LAST_KNOWN_LOCATION_PERMISSION, Integer.valueOf(lock.getPermissions().isViewLastKnownLocationPermission() ? 1 : 0));
        contentValues.put(LocksColumns.PERMISSION_VIEW_TEMPORARY_PERMISSION, Integer.valueOf(lock.getPermissions().isViewTemporaryCodePermission() ? 1 : 0));
        contentValues.put(LocksColumns.PERMISSION_OPEN_SHACKLE_PERMISSION, Integer.valueOf(lock.getPermissions().isOpenShacklePermission() ? 1 : 0));
        contentValues.put(LocksColumns.PERMISSION_SCHEDULE_TYPE, Integer.valueOf(lock.getPermissions().getScheduleType().getValue()));
        contentValues.put(LocksColumns.PERMISSION_VIEW_MODE, Integer.valueOf(lock.getPermissions().getGuestInterface().getValue()));
        contentValues.put(LocksColumns.MEMORY_MAP_VERSION, Integer.valueOf(lock.getMemoryMapVersion()));
        String str = LocksColumns.SECTION_NUMBER;
        int i = (lock.isMechanicalLock() || lock.isDialSpeedLock() || lock.isBiometricPadLock()) ? 3 : lock.getAccessType() == AccessType.OWNER ? 1 : 2;
        contentValues.put(str, Integer.valueOf(i));
        return contentValues;
    }

    public static ContentProviderOperation buildContentProviderOperation(Lock lock, long j) {
        Builder withValue = ContentProviderOperation.newInsert(Locks.CONTENT_URI).withValue(SyncColumns.UPDATED, Long.valueOf(j)).withValue("lock_id", lock.getLockId()).withValue(LocksColumns.LOCK_NAME, lock.getName()).withValue(LocksColumns.LOCK_NOTES, lock.getNotes()).withValue(LocksColumns.LOCK_LABEL, lock.getLabel()).withValue(LocksColumns.IS_FAVORITE, Integer.valueOf(lock.isFavorite() ? 1 : 0)).withValue(LocksColumns.USER_TYPE, Integer.valueOf(lock.getAccessType().getValue())).withValue("created_on", lock.getCreatedOn()).withValue(LocksColumns.MODIFIED_ON, lock.getModifiedOn()).withValue(LocksColumns.MODEL_ID, lock.getModelId()).withValue(LocksColumns.MODEL_INTERFACE_ID, Integer.valueOf(lock.getModelInterfaceId())).withValue(LocksColumns.MODEL_NUMBER, lock.getModelNumber()).withValue(LocksColumns.MODEL_SKU, lock.getModelSku()).withValue(LocksColumns.MODEL_NAME, lock.getModelName()).withValue(LocksColumns.MODEL_DESCRIPTION, lock.getModelDescription()).withValue(LocksColumns.KMS_ID, lock.getKmsId()).withValue(LocksColumns.FIRMWARE_VERSION, Integer.valueOf(lock.getFirmwareVersion())).withValue("firmware_counter", Integer.valueOf(lock.getFirmwareCounter())).withValue(LocksColumns.PRIMARY_CODE, lock.getPrimaryCode()).withValue(LocksColumns.SECONDARY_CODE_1, lock.getSecondaryCode1() != null ? lock.getSecondaryCode1() : "").withValue(LocksColumns.SECONDARY_CODE_2, lock.getSecondaryCode2() != null ? lock.getSecondaryCode2() : "").withValue(LocksColumns.SECONDARY_CODE_3, lock.getSecondaryCode3() != null ? lock.getSecondaryCode3() : "").withValue(LocksColumns.SECONDARY_CODE_4, lock.getSecondaryCode4() != null ? lock.getSecondaryCode4() : "").withValue(LocksColumns.SECONDARY_CODE_5, lock.getSecondaryCode5() != null ? lock.getSecondaryCode5() : "").withValue(LocksColumns.BATTERY_LEVEL, Integer.valueOf(lock.getRemainingBatteryPercentage())).withValue(LocksColumns.TEMPERATURE, Integer.valueOf(lock.getTemperature())).withValue(LocksColumns.TX_POWER, Integer.valueOf(lock.getTxPower())).withValue(LocksColumns.TX_INTERVAL, Integer.valueOf(lock.getTxInterval())).withValue(LocksColumns.LOCK_MODE, Integer.valueOf(lock.getLockMode().getValue())).withValue(LocksColumns.RELOCK_TIME, Integer.valueOf(lock.getRelockTimeInSeconds())).withValue(LocksColumns.LOCK_STATUS, Integer.valueOf(lock.getLockStatus().getValue())).withValue(LocksColumns.SHACKLE_STATUS, Integer.valueOf(lock.getShackleStatus().getValue())).withValue(LocksColumns.LOCKER_MODE, Integer.valueOf(lock.isLockerMode() ? 1 : 0)).withValue(LocksColumns.TIMEZONE, lock.getTimezone()).withValue(LocksColumns.SERVICE_CODE, lock.getServiceCode()).withValue(LocksColumns.SERVICE_CODE_EXPIRATION, lock.getServiceCodeExpiresOn()).withValue(LocksColumns.LAST_LOG_REFERENCE_ID, Long.valueOf(lock.getLastLogReferenceId())).withValue(LocksColumns.TOUCH_MODE_CONFIGURATION, lock.getTouchModeConfiguration()).withValue(LocksColumns.PROXIMITY_SWIPE_MODE_CONFIGURATION, lock.getProximitySwipeModeConfiguration()).withValue(LocksColumns.KMS_CREATED_ON, lock.getKmsCreatedOn()).withValue(LocksColumns.KMS_MODIFIED_ON, lock.getKmsModifiedOn()).withValue(LocksColumns.PUBLIC_CONFIG_COUNTER, Integer.valueOf(lock.getPublicConfigCounter())).withValue(LocksColumns.PRIMARY_PASSCODE_COUNTER, Long.valueOf(lock.getPrimaryCodeCounter())).withValue(LocksColumns.MEASUREMENT_COUNTER, Integer.valueOf(lock.getMeasurementCounter())).withValue(LocksColumns.SECONDARY_PASSCODE_COUNTER, Long.valueOf(lock.getSecondaryCodeCounter())).withValue(LocksColumns.LAST_UNLOCKED, Long.valueOf(lock.getLastUnlocked())).withValue(LocksColumns.LAST_UNLOCKED_SHACKLE, Long.valueOf(lock.getLastUnlockedShackle())).withValue(LocksColumns.LATITUDE, lock.getLatitude()).withValue(LocksColumns.LONGITUDE, lock.getLongitude()).withValue(LocksColumns.PERMISSION_START_AT_DATE, lock.getPermissions().getStartAtDate()).withValue(LocksColumns.PERMISSION_EXPIRES_AT_DATE, lock.getPermissions().getExpiresAtDate()).withValue(LocksColumns.PERMISSION_MONDAY, Integer.valueOf(lock.getPermissions().isMonday() ? 1 : 0)).withValue(LocksColumns.PERMISSION_TUESDAY, Integer.valueOf(lock.getPermissions().isTuesday() ? 1 : 0)).withValue(LocksColumns.PERMISSION_WEDNESDAY, Integer.valueOf(lock.getPermissions().isWednesday() ? 1 : 0)).withValue(LocksColumns.PERMISSION_THURSDAY, Integer.valueOf(lock.getPermissions().isThursday() ? 1 : 0)).withValue(LocksColumns.PERMISSION_FRIDAY, Integer.valueOf(lock.getPermissions().isFriday() ? 1 : 0)).withValue(LocksColumns.PERMISSION_SATURDAY, Integer.valueOf(lock.getPermissions().isSaturday() ? 1 : 0)).withValue(LocksColumns.PERMISSION_SUNDAY, Integer.valueOf(lock.getPermissions().isSunday() ? 1 : 0)).withValue(LocksColumns.PERMISSION_VIEW_LAST_KNOWN_LOCATION_PERMISSION, Integer.valueOf(lock.getPermissions().isViewLastKnownLocationPermission() ? 1 : 0)).withValue(LocksColumns.PERMISSION_VIEW_TEMPORARY_PERMISSION, Integer.valueOf(lock.getPermissions().isViewTemporaryCodePermission() ? 1 : 0)).withValue(LocksColumns.PERMISSION_OPEN_SHACKLE_PERMISSION, Integer.valueOf(lock.getPermissions().isOpenShacklePermission() ? 1 : 0)).withValue(LocksColumns.PERMISSION_SCHEDULE_TYPE, Integer.valueOf(lock.getPermissions().getScheduleType().getValue())).withValue(LocksColumns.PERMISSION_VIEW_MODE, Integer.valueOf(lock.getPermissions().getGuestInterface().getValue())).withValue(LocksColumns.MEMORY_MAP_VERSION, Integer.valueOf(lock.getMemoryMapVersion()));
        String str = LocksColumns.SECTION_NUMBER;
        int i = (lock.isDialSpeedLock() || lock.isBiometricPadLock() || lock.isMechanicalLock()) ? 3 : lock.getAccessType() == AccessType.OWNER ? 1 : 2;
        return withValue.withValue(str, Integer.valueOf(i)).build();
    }

    public static ArrayList<Lock> buildLocks(Cursor cursor) {
        ArrayList<Lock> arrayList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            arrayList.add(buildLock(cursor));
            cursor.moveToNext();
        }
        return arrayList;
    }

    public static Lock buildLock(Cursor cursor) {
        Lock lock = new Lock(cursor.getString(cursor.getColumnIndexOrThrow("lock_id")));
        lock.setMacAddress(DeviceInfoBuilder.buildMacAddress(cursor));
        lock.setRssiThreshold(DeviceInfoBuilder.buildRssiThreshold(cursor));
        lock.setLastUpdated(cursor.getLong(cursor.getColumnIndexOrThrow(SyncColumns.UPDATED)));
        lock.setName(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.LOCK_NAME)));
        lock.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.LOCK_NOTES)));
        boolean z = false;
        lock.setFavorite(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.IS_FAVORITE)) == 1);
        lock.setLabel(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.LOCK_LABEL)));
        lock.setAccessType(AccessType.fromKey(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.USER_TYPE))));
        lock.setCreatedOn(cursor.getString(cursor.getColumnIndexOrThrow("created_on")));
        lock.setModifiedOn(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.MODIFIED_ON)));
        lock.setModelId(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.MODEL_ID)));
        lock.setModelInterfaceId(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.MODEL_INTERFACE_ID)));
        lock.setModelNumber(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.MODEL_NUMBER)));
        lock.setModelSku(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.MODEL_SKU)));
        lock.setModelName(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.MODEL_NAME)));
        lock.setModelDescription(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.MODEL_DESCRIPTION)));
        lock.setKmsId(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.KMS_ID)));
        lock.setFirmwareVersion(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.FIRMWARE_VERSION)));
        lock.setFirmwareCounter(cursor.getInt(cursor.getColumnIndexOrThrow("firmware_counter")));
        lock.setPrimaryCode(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.PRIMARY_CODE)));
        lock.setSecondaryCode1(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.SECONDARY_CODE_1)));
        lock.setSecondaryCode2(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.SECONDARY_CODE_2)));
        lock.setSecondaryCode3(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.SECONDARY_CODE_3)));
        lock.setSecondaryCode4(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.SECONDARY_CODE_4)));
        lock.setSecondaryCode5(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.SECONDARY_CODE_5)));
        lock.setRemainingBatteryPercentage(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.BATTERY_LEVEL)));
        lock.setTemperature(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.TEMPERATURE)));
        lock.setTxPower(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.TX_POWER)));
        lock.setTxInterval(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.TX_INTERVAL)));
        lock.setLockMode(LockMode.fromKey(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.LOCK_MODE))));
        lock.setRelockTimeInSeconds(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.RELOCK_TIME)));
        lock.setLockStatus(LockStatus.fromKey(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.LOCK_STATUS))));
        lock.setShackleStatus(ShackleStatus.fromKey(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.SHACKLE_STATUS))));
        if (cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.LOCKER_MODE)) == 1) {
            z = true;
        }
        lock.setLockerMode(z);
        lock.setTimezone(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.TIMEZONE)));
        lock.setServiceCode(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.SERVICE_CODE)));
        lock.setServiceCodeExpiresOn(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.SERVICE_CODE_EXPIRATION)));
        lock.setLastLogReferenceId((long) cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.LAST_LOG_REFERENCE_ID)));
        lock.setTouchModeConfiguration(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.TOUCH_MODE_CONFIGURATION)));
        lock.setProximitySwipeModeConfiguration(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.PROXIMITY_SWIPE_MODE_CONFIGURATION)));
        lock.setKmsCreatedOn(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.KMS_CREATED_ON)));
        lock.setKmsModifiedOn(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.KMS_MODIFIED_ON)));
        lock.setKmsDeviceKey(KmsDeviceKeyBuilder.buildKmsDeviceKey(cursor));
        lock.setPublicConfigCounter(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.PUBLIC_CONFIG_COUNTER)));
        lock.setPrimaryCodeCounter((long) cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.PRIMARY_PASSCODE_COUNTER)));
        lock.setMeasurementCounter(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.MEASUREMENT_COUNTER)));
        lock.setSecondaryCodeCounter((long) cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.SECONDARY_PASSCODE_COUNTER)));
        lock.setLastUnlocked(cursor.getLong(cursor.getColumnIndexOrThrow(LocksColumns.LAST_UNLOCKED)));
        lock.setLastUnlockedShackle(cursor.getLong(cursor.getColumnIndexOrThrow(LocksColumns.LAST_UNLOCKED_SHACKLE)));
        lock.setLatitude(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.LATITUDE)));
        lock.setLongitude(cursor.getString(cursor.getColumnIndexOrThrow(LocksColumns.LONGITUDE)));
        lock.setLockType(LockType.fromKey(lock.modelNumber));
        lock.setPermissions(PermissionsBuilder.buildGuestPermissions(cursor));
        lock.setMemoryMapVersion(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.MEMORY_MAP_VERSION)));
        lock.setSectionNumber(cursor.getInt(cursor.getColumnIndexOrThrow(LocksColumns.SECTION_NUMBER)));
        return lock;
    }
}
