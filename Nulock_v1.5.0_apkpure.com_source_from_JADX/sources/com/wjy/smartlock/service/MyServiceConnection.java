package com.wjy.smartlock.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import com.wjy.smartlock.service.SmartLockService.MyBinder;

public class MyServiceConnection {
    private static MyServiceConnection mInstance = new MyServiceConnection();
    private final String TAG = "MyServiceConnection";
    private boolean isBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            Log.i("MyServiceConnection", "onServiceDisconnected-->");
            MyServiceConnection.this.mSmartLockService = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("MyServiceConnection", "onServiceConnected-->");
            MyServiceConnection.this.mSmartLockService = ((MyBinder) service).getService();
            MyServiceConnection.this.mSmartLockService.setBindDeviceListActivity(true);
        }
    };
    /* access modifiers changed from: private */
    public SmartLockService mSmartLockService = null;

    public static MyServiceConnection getInstance() {
        return mInstance;
    }

    public boolean bindService(Context context) {
        this.isBound = context.bindService(new Intent(context, SmartLockService.class), this.mConnection, 1);
        return this.isBound;
    }

    public void unbindService(Context context) {
        if (this.isBound) {
            context.unbindService(this.mConnection);
        }
        this.isBound = false;
        if (this.mSmartLockService != null) {
            this.mSmartLockService.setBindDeviceListActivity(false);
        }
    }

    public void startService(Context context) {
        context.startService(new Intent(context, SmartLockService.class));
    }

    public void stopService(Context context) {
        context.stopService(new Intent(context, SmartLockService.class));
    }

    public SmartLockService getService() {
        return this.mSmartLockService;
    }
}
