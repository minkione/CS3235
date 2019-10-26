package com.masterlock.ble.app.bus;

public class SharingAppOperationResultEvent {
    private boolean successfullyShared;

    public SharingAppOperationResultEvent(boolean z) {
        this.successfullyShared = z;
    }

    public boolean isSuccessfullyShared() {
        return this.successfullyShared;
    }
}
