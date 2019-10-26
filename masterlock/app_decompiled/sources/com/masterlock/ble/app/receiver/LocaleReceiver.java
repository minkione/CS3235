package com.masterlock.ble.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.masterlock.api.module.ApiModule;

public class LocaleReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        ApiModule.getDeviceLocale("2.4.0.1");
    }
}
