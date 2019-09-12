package com.masterlock.ble.app.bus;

import com.masterlock.core.Lock;

public class DeviceConfigSuccessEvent {
    Lock lock;

    public DeviceConfigSuccessEvent(Lock lock2) {
        this.lock = lock2;
    }

    public Lock getLock() {
        return this.lock;
    }
}
