package com.masterlock.ble.app.bus;

import com.masterlock.core.Lock;

public class ConfigAppliedEvent {
    Lock lock;

    public ConfigAppliedEvent(Lock lock2) {
        this.lock = lock2;
    }

    public Lock getLock() {
        return this.lock;
    }
}
