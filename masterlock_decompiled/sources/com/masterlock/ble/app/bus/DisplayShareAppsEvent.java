package com.masterlock.ble.app.bus;

import android.content.Intent;

public class DisplayShareAppsEvent {
    private Intent intent;

    public DisplayShareAppsEvent(Intent intent2) {
        this.intent = intent2;
    }

    public Intent getIntent() {
        return this.intent;
    }
}
