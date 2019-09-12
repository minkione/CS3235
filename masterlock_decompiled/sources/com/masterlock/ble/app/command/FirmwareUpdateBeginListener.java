package com.masterlock.ble.app.command;

import com.masterlock.ble.app.bus.FirmwareUpdateBeginEvent;

public interface FirmwareUpdateBeginListener {
    void onFirmwareUpdateBeginEvent(FirmwareUpdateBeginEvent firmwareUpdateBeginEvent);
}
