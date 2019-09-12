package com.masterlock.ble.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.tape.ConfirmTaskService;
import com.masterlock.ble.app.tape.UploadTaskService;

public class NetworkReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) MasterLockApp.get().getBaseContext().getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (MasterLockApp.get().isSignedIn()) {
                context.startService(new Intent(context, UploadTaskService.class));
            }
            context.startService(new Intent(context, ConfirmTaskService.class));
        }
    }
}
