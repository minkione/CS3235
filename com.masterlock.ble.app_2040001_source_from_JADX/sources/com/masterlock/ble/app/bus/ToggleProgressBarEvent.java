package com.masterlock.ble.app.bus;

public class ToggleProgressBarEvent {
    public static final boolean HIDE = false;
    public static final boolean SHOW = true;
    private boolean shouldShow;

    public ToggleProgressBarEvent() {
        this.shouldShow = false;
    }

    public ToggleProgressBarEvent(boolean z) {
        this.shouldShow = z;
    }

    public boolean shouldShow() {
        return this.shouldShow;
    }

    public void setShouldShow(boolean z) {
        this.shouldShow = z;
    }
}
