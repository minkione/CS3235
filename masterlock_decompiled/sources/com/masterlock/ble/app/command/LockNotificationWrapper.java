package com.masterlock.ble.app.command;

import android.util.Log;
import com.masterlock.core.Lock;

public class LockNotificationWrapper {
    private Lock lock;
    private String macAddress;
    private int numberErrors = 0;

    public LockNotificationWrapper(Lock lock2, String str) {
        this.lock = lock2;
        this.macAddress = str;
    }

    public Lock getLock() {
        return this.lock;
    }

    public void setLock(Lock lock2) {
        this.lock = lock2;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public void setMacAddress(String str) {
        this.macAddress = str;
    }

    public void setError() {
        this.numberErrors++;
    }

    public boolean isNeedNotification() {
        StringBuilder sb = new StringBuilder();
        sb.append("isNeedNotification: # errors: ");
        sb.append(this.numberErrors);
        Log.i("TAG", sb.toString());
        return this.numberErrors >= 5;
    }
}
