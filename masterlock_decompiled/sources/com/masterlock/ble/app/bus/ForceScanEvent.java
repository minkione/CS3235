package com.masterlock.ble.app.bus;

import com.masterlock.core.Lock;
import java.util.List;

public class ForceScanEvent {
    private Lock lock;
    private List<Lock> locks;

    public ForceScanEvent(Lock lock2) {
        this.lock = lock2;
    }

    public ForceScanEvent(List list) {
        this.locks = list;
    }

    public Lock getLock() {
        return this.lock;
    }

    public List getLocks() {
        return this.locks;
    }
}
