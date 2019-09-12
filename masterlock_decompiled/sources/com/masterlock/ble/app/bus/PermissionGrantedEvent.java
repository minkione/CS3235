package com.masterlock.ble.app.bus;

public class PermissionGrantedEvent {
    private boolean isGranted;
    private String permission;

    public PermissionGrantedEvent(String str, boolean z) {
        this.permission = str;
        this.isGranted = z;
    }

    public boolean isGranted() {
        return this.isGranted;
    }

    public String getPermission() {
        return this.permission;
    }
}
