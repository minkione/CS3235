package com.masterlock.ble.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.service.scan.BackgroundScanService;
import com.masterlock.ble.app.service.scan.FirmwareUpdateService;

public class BootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (MasterLockApp.get().isSignedIn()) {
            Intent intent2 = new Intent(context, BackgroundScanService.class);
            Intent intent3 = new Intent(context, FirmwareUpdateService.class);
            if (VERSION.SDK_INT >= 26) {
                context.startForegroundService(intent2);
                context.startForegroundService(intent3);
                return;
            }
            context.startService(intent2);
            context.startService(intent3);
        }
    }
}
