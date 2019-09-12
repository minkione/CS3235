package com.masterlock.ble.app.bus;

import com.masterlock.core.Lock;
import com.masterlock.core.LockConfigAction;

public class ConfigDeviceFoundEvent {
    boolean isReconfiguringLock = false;
    Lock lock;
    LockConfigAction lockConfigAction;

    public ConfigDeviceFoundEvent(Lock lock2) {
        this.lock = lock2;
    }

    public ConfigDeviceFoundEvent(Lock lock2, boolean z, LockConfigAction lockConfigAction2) {
        this.lock = lock2;
        this.isReconfiguringLock = z;
        this.lockConfigAction = lockConfigAction2;
    }

    public Lock getLock() {
        return this.lock;
    }

    public boolean isReconfiguringLock() {
        return this.isReconfiguringLock;
    }

    public LockConfigAction getLockConfigAction() {
        return this.lockConfigAction;
    }
}
