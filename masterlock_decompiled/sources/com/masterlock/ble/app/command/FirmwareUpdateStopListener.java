package com.masterlock.ble.app.command;

import com.masterlock.ble.app.bus.FirmwareUpdateStopEvent;

public interface FirmwareUpdateStopListener {
    void onFirmwareUpdateStopEvent(FirmwareUpdateStopEvent firmwareUpdateStopEvent);
}
