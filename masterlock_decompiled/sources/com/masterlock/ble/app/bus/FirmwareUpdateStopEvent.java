package com.masterlock.ble.app.bus;

public class FirmwareUpdateStopEvent {
    private boolean stop;

    public FirmwareUpdateStopEvent(boolean z) {
        this.stop = z;
    }

    public boolean isStop() {
        return this.stop;
    }
}
