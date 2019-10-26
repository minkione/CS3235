package com.masterlock.ble.app.command;

import com.masterlock.ble.app.bus.FirmwareUpdateBrickedEvent;

public interface LockBrickedListener {
    void onFirmwareUpdateBrickedEvent(FirmwareUpdateBrickedEvent firmwareUpdateBrickedEvent);
}
