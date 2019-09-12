package com.masterlock.ble.app.bus;

import com.masterlock.core.Lock;

public class StartContactPickerEvent {
    public final Lock model;

    public StartContactPickerEvent(Lock lock) {
        this.model = lock;
    }
}
