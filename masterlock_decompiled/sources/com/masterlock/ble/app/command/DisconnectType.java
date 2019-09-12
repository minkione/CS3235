package com.masterlock.ble.app.command;

public enum DisconnectType {
    DEFAULT(0),
    PREVENT_ADVERTISING(1),
    CLEAR_KEYPAD(2);
    
    private final byte unlockPayload;

    private DisconnectType(byte b) {
        this.unlockPayload = b;
    }

    public byte getUnlockPayload() {
        return this.unlockPayload;
    }
}
