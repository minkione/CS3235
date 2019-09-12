package com.masterlock.ble.app.bus;

import com.masterlock.core.Lock;

public class FirmwareCommandSuccessEvent {
    boolean isFirstCommand;
    boolean isLastCommand;
    Lock lock;

    public FirmwareCommandSuccessEvent(Lock lock2) {
        this.lock = lock2;
    }

    public FirmwareCommandSuccessEvent(Lock lock2, boolean z) {
        this.lock = lock2;
        this.isFirstCommand = z;
    }

    public FirmwareCommandSuccessEvent(Lock lock2, boolean z, boolean z2) {
        this.lock = lock2;
        this.isFirstCommand = z;
        this.isLastCommand = z2;
    }

    public Lock getLock() {
        return this.lock;
    }

    public boolean isFirstCommand() {
        return this.isFirstCommand;
    }

    public boolean isLastCommand() {
        return this.isLastCommand;
    }
}
