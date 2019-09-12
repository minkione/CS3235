package com.masterlock.ble.app.bus;

import com.masterlock.core.Lock;

public class FirmwareUpdateBeginEvent {
    Lock lock;
    boolean startUpdate;

    public FirmwareUpdateBeginEvent(boolean z, Lock lock2) {
        this.startUpdate = z;
        this.lock = lock2;
    }

    public FirmwareUpdateBeginEvent(boolean z) {
        this.startUpdate = z;
    }

    public boolean isStartUpdate() {
        return this.startUpdate;
    }

    public Lock getLock() {
        return this.lock;
    }
}
