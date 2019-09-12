package com.masterlock.ble.app.bus;

public class ChangeSoftKeyboardBehaviorEvent {
    private int softKeyboardBehavior;

    public ChangeSoftKeyboardBehaviorEvent(int i) {
        this.softKeyboardBehavior = i;
    }

    public int getSoftKeyboardBehavior() {
        return this.softKeyboardBehavior;
    }
}
