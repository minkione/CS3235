package com.masterlock.core;

public enum LockStatus {
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

    private LockStatus(int i) {
        this.value = i;
    }

    public int getValue() {
        return this.value;
    }

    public static LockStatus fromKey(int i) {
        LockStatus[] values;
        for (LockStatus lockStatus : values()) {
            if (lockStatus.getValue() == i) {
                return lockStatus;
            }
        }
        return UNKNOWN;
    }
}
