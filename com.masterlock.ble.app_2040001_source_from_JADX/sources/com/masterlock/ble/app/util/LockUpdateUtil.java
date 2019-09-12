package com.masterlock.ble.app.util;

import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.service.LockService;
import java.util.HashMap;
import javax.inject.Inject;

public class LockUpdateUtil {
    private static LockUpdateUtil instance;
    private HashMap<String, Boolean> locksWithUpdatesAvailable = new HashMap<>();
    @Inject
    LockService mLockService;
    @Inject
    IScheduler mScheduler;

    protected LockUpdateUtil() {
        MasterLockApp.get().inject(this);
    }

    public static LockUpdateUtil getInstance() {
        if (instance == null) {
            instance = new LockUpdateUtil();
        }
        return instance;
    }

    public void setLockWithUpdate(String str, boolean z) {
        this.locksWithUpdatesAvailable.put(str, Boolean.valueOf(z));
    }

    public boolean getUpdateAvailableForLock(String str) {
        if (this.locksWithUpdatesAvailable.size() <= 0 || this.locksWithUpdatesAvailable.get(str) == null) {
            return false;
        }
        return ((Boolean) this.locksWithUpdatesAvailable.get(str)).booleanValue();
    }

    public boolean removeUpdateAvailableForLock(String str) {
        if (this.locksWithUpdatesAvailable.size() <= 0 || this.locksWithUpdatesAvailable.get(str) == null) {
            return true;
        }
        return ((Boolean) this.locksWithUpdatesAvailable.remove(str)).booleanValue();
    }
}
