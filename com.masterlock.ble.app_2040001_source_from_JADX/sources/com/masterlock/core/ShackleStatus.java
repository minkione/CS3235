package com.masterlock.core;

public enum ShackleStatus {
    UNKNOWN(0),
    LOCKED(1),
    UNLOCKED(2),
    LOCK_FOUND(101),
    OPENED(103),
    UNREACHABLE(104),
    UPDATE_MODE(105),
    UNLOCKING(-1),
    NO_COMMUNICATION(-2);
    
    private final int value;

    private ShackleStatus(int i) {
        this.value = i;
    }

    public int getValue() {
        return this.value;
    }

    public static ShackleStatus fromKey(int i) {
        ShackleStatus[] values;
        for (ShackleStatus shackleStatus : values()) {
            if (shackleStatus.getValue() == i) {
                return shackleStatus;
            }
        }
        return UNKNOWN;
    }
}
