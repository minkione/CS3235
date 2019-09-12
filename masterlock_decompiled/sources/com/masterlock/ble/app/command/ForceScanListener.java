package com.masterlock.ble.app.command;

import com.masterlock.ble.app.bus.ForceScanEvent;
import com.masterlock.ble.app.bus.ForceStopScanEvent;

public interface ForceScanListener {
    void onForceScanEvent(ForceScanEvent forceScanEvent);

    void onStopScanEvent(ForceStopScanEvent forceStopScanEvent);
}
