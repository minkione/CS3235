package com.masterlock.ble.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received sms broadcast");
    }
}
