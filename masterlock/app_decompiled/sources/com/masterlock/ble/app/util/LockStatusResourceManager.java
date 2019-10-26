package com.masterlock.ble.app.util;

import android.support.annotation.DrawableRes;
import com.masterlock.ble.app.C1075R;
import com.masterlock.core.Lock;
import com.masterlock.core.Lock.LockType;
import com.masterlock.core.LockStatus;

public enum LockStatusResourceManager {
    UNKNOWN(LockStatus.UNKNOWN, C1075R.C1076drawable.ic_locklist_unreachable, C1075R.C1076drawable.ic_locklist_5440_unreachable, C1075R.C1076drawable.ic_locklist_5441_unreachable),
    LOCKED(LockStatus.LOCKED, C1075R.C1076drawable.ic_locklist_lock, C1075R.C1076drawable.ic_locklist_5440_found, C1075R.C1076drawable.ic_locklist_5441_found),
    UNLOCKED(LockStatus.UNLOCKED, C1075R.C1076drawable.ic_locklist_unlocked, C1075R.C1076drawable.ic_locklist_5440_unlocked, C1075R.C1076drawable.ic_locklist_5441_unlocked),
    LOCK_FOUND(LockStatus.LOCK_FOUND, C1075R.C1076drawable.ic_locklist_lock_filled, C1075R.C1076drawable.ic_locklist_5440_filled, C1075R.C1076drawable.ic_locklist_5441_filled),
    OPENED(LockStatus.OPENED, C1075R.C1076drawable.ic_locklist_open, C1075R.C1076drawable.ic_locklist_5440_open, C1075R.C1076drawable.ic_locklist_5441_open),
    UNREACHABLE(LockStatus.UNREACHABLE, C1075R.C1076drawable.ic_locklist_unreachable, C1075R.C1076drawable.ic_locklist_5440_unreachable, C1075R.C1076drawable.ic_locklist_5441_unreachable),
    UPDATE_MODE(LockStatus.UPDATE_MODE, C1075R.C1076drawable.ic_lock_installing, C1075R.C1076drawable.ic_locklist_5440_installing, C1075R.C1076drawable.ic_locklist_5441_installing),
    UNLOCKING(LockStatus.UNLOCKING, C1075R.C1076drawable.ic_locklist_unlocked, C1075R.C1076drawable.ic_locklist_5440_unlocked, C1075R.C1076drawable.ic_locklist_5441_unlocked),
    NO_COMMUNICATION(LockStatus.NO_COMMUNICATION, C1075R.C1076drawable.ic_locklist_lockermode, C1075R.C1076drawable.ic_locklist_keysafe_comunication_disabled, C1075R.C1076drawable.ic_locklist_keysafe_comunication_disabled);
    
    private final int lockListKeySafeShackledResId;
    private final int lockListKeySafeWallMountResId;
    private final int lockListPadlockResId;
    private LockStatus lockStatus;

    private LockStatusResourceManager(LockStatus lockStatus2, int i, int i2, int i3) {
        this.lockStatus = lockStatus2;
        this.lockListPadlockResId = i;
        this.lockListKeySafeShackledResId = i2;
        this.lockListKeySafeWallMountResId = i3;
    }

    @DrawableRes
    public static int getResIdForLockStatus(Lock lock) {
        return getResId(lock, lock.getLockStatus());
    }

    public static int getResIdForLockStatus(Lock lock, LockStatus lockStatus2) {
        return getResId(lock, lockStatus2);
    }

    @DrawableRes
    private static int getResId(Lock lock, LockStatus lockStatus2) {
        LockStatusResourceManager[] values;
        LockType lockType = lock.getLockType();
        int i = -1;
        for (LockStatusResourceManager lockStatusResourceManager : values()) {
            if (lockStatus2 == lockStatusResourceManager.lockStatus) {
                switch (lockType) {
                    case BIOMETRIC_PADLOCK_INTERIORS:
                    case BIOMETRIC_PADLOCK_EXTERIORS:
                    case PADLOCK_INTERIORS:
                    case PADLOCK_EXTERIORS:
                        i = lockStatusResourceManager.lockListPadlockResId;
                        break;
                    case KEY_SAFE_SHACKLED:
                        i = lockStatusResourceManager.lockListKeySafeShackledResId;
                        break;
                    case KEY_SAFE_WALL_MOUNT:
                        i = lockStatusResourceManager.lockListKeySafeWallMountResId;
                        break;
                    case MECHANICAL_GENERIC:
                        i = C1075R.C1076drawable.generic_lock_icon;
                        break;
                    case DIAL_SPEED:
                        i = C1075R.C1076drawable.dial_speed_icon;
                        break;
                }
            }
        }
        return i;
    }
}
