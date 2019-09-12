package com.masterlock.ble.app.bus;

import com.masterlock.core.comparator.LockComparator.SortId;

public class SortByEvent {
    SortId sortId;

    public SortByEvent(SortId sortId2) {
        this.sortId = sortId2;
    }

    public SortId getSortId() {
        return this.sortId;
    }
}
