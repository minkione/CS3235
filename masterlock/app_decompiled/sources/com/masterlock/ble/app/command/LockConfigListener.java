package com.masterlock.ble.app.command;

import com.masterlock.ble.app.bus.ConfigDeviceFoundEvent;
import com.masterlock.ble.app.bus.DeviceConfigSuccessEvent;
import com.masterlock.ble.app.bus.DeviceTimeoutEvent;
import com.masterlock.ble.app.bus.FirmwareCommandSuccessEvent;
import com.masterlock.ble.app.bus.FirmwareUpdateSuccessEvent;
import com.masterlock.ble.app.bus.ResetKeyEvent;

public interface LockConfigListener {
    void onConfigError(DeviceTimeoutEvent deviceTimeoutEvent);

    void onConfigSuccess(DeviceConfigSuccessEvent deviceConfigSuccessEvent);

    void onFirmwareCommandSuccess(FirmwareCommandSuccessEvent firmwareCommandSuccessEvent);

    void onFirmwareUpdateSuccess(FirmwareUpdateSuccessEvent firmwareUpdateSuccessEvent);

    void onKeyReset(ResetKeyEvent resetKeyEvent);

    void onLockFound(ConfigDeviceFoundEvent configDeviceFoundEvent);
}
