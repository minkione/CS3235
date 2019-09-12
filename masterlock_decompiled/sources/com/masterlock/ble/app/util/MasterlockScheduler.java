package com.masterlock.ble.app.util;

import android.os.Looper;
import p009rx.Scheduler;
import p009rx.android.schedulers.AndroidSchedulers;
import p009rx.schedulers.Schedulers;

public class MasterlockScheduler implements IScheduler {
    public Scheduler background() {
        return backgroundIfNeeded();
    }

    public Scheduler main() {
        return AndroidSchedulers.mainThread();
    }

    private Scheduler backgroundIfNeeded() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            return Schedulers.m220io();
        }
        return Schedulers.immediate();
    }
}
