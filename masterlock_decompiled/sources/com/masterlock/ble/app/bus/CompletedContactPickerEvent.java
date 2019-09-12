package com.masterlock.ble.app.bus;

import com.masterlock.core.Guest;

public class CompletedContactPickerEvent {
    public Guest guest;

    public CompletedContactPickerEvent(Guest guest2) {
        this.guest = guest2;
    }
}
