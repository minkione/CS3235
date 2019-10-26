package com.masterlock.ble.app.bus;

public class UploadQueueSizeEvent {
    public final int size;

    public UploadQueueSizeEvent(int i) {
        this.size = i;
    }
}
