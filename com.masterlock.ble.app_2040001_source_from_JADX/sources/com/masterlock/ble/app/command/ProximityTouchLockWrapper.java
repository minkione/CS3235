package com.masterlock.ble.app.command;

import com.masterlock.core.Lock;

public class ProximityTouchLockWrapper {
    private Lock lock;
    private String macAddress;
    private Runnable runnable;

    public ProximityTouchLockWrapper(Lock lock2, String str, Runnable runnable2) {
        this.lock = lock2;
        this.macAddress = str;
        this.runnable = runnable2;
    }

    public Lock getLock() {
        return this.lock;
    }

    public void setLock(Lock lock2) {
        this.lock = lock2;
    }

    public Runnable getRunnable() {
        return this.runnable;
    }

    public void setRunnable(Runnable runnable2) {
        this.runnable = runnable2;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public void setMacAddress(String str) {
        this.macAddress = str;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProximitySwipeLockWrapper{lock=");
        sb.append(this.lock);
        sb.append(", macAddress='");
        sb.append(this.macAddress);
        sb.append('\'');
        sb.append(", runnable=");
        sb.append(this.runnable);
        sb.append('}');
        return sb.toString();
    }
}
