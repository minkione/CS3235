package com.masterlock.ble.app.util;

import p009rx.Scheduler;

public interface IScheduler {
    Scheduler background();

    Scheduler main();
}
