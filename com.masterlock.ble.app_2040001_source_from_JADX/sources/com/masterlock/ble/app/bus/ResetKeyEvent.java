package com.masterlock.ble.app.bus;

import com.masterlock.ble.app.command.ResetKeysWrapper;

public class ResetKeyEvent {
    private final ResetKeysWrapper resetKeysWrapper;

    public ResetKeyEvent(ResetKeysWrapper resetKeysWrapper2) {
        this.resetKeysWrapper = resetKeysWrapper2;
    }

    public ResetKeysWrapper getResetKeysWrapper() {
        return this.resetKeysWrapper;
    }
}
