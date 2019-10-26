package com.masterlock.ble.app.bus;

import flow.Screen;

public class ManageLockEvent {
    private Screen screenTo;

    public ManageLockEvent() {
    }

    public ManageLockEvent(Screen screen) {
        this.screenTo = screen;
    }

    public Screen getScreenTo() {
        return this.screenTo;
    }
}
