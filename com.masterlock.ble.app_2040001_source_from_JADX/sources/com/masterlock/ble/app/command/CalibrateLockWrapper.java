package com.masterlock.ble.app.command;

import com.masterlock.core.Lock;

public class CalibrateLockWrapper {
    private int count;
    private Lock lock;
    private float mean;

    public CalibrateLockWrapper(Lock lock2) {
        this.lock = lock2;
    }

    public Lock getLock() {
        return this.lock;
    }

    public void setLock(Lock lock2) {
        this.lock = lock2;
    }

    public void addRssiValue(int i) {
        this.count++;
        float f = (float) i;
        float f2 = this.mean;
        this.mean = ((f - f2) / ((float) this.count)) + f2;
    }

    public float getMean() {
        return this.mean;
    }

    public int getCount() {
        return this.count;
    }
}
