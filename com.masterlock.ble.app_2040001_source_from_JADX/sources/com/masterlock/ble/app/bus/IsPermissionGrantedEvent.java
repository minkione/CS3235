package com.masterlock.ble.app.bus;

public class IsPermissionGrantedEvent {
    private String permission;

    public IsPermissionGrantedEvent(String str) {
        this.permission = str;
    }

    public String getPermission() {
        return this.permission;
    }
}
