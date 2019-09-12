package com.masterlock.ble.app.service;

import android.content.ContentValues;
import com.masterlock.ble.app.command.ResetKeysWrapper;
import com.masterlock.core.Lock;

public interface LockListener {

    public enum Configuration {
        PRIMARY_CODE,
        SECONDARY_CODE,
        UNLOCK_MODE,
        RELOCK_TIME,
        UPDATE_FIRMWARE,
        LOCATION
    }

    void onConfigAppliedSuccess(Lock lock, Configuration configuration);

    void onConfigChangeApplied(Lock lock);

    void onFirmwareCommandResponse(Lock lock);

    void onKeyReset(ResetKeysWrapper resetKeysWrapper);

    void onLocationUpdated(Lock lock);

    void onLockDisconnect(Lock lock);

    void onLockUpdate(Lock lock);

    void onLockUpdate(Lock lock, ContentValues contentValues);

    void onStatusUpdate(Lock lock);
}
