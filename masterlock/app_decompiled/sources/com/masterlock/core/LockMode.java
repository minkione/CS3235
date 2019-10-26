package com.masterlock.core;

public enum LockMode {
    UNKNOWN(0),
    PROXIMITY(1),
    TOUCH(5),
    PROXIMITYSWIPE(10);
    
    public static final int DISCOVERY_TIME_FOR_PROXIMITY_MODE = 0;
    private final int value;

    public static String getStringValue(int i) {
        return i != 1 ? i != 5 ? i != 10 ? "UNKNOWN" : "SWIPE" : "TOUCH" : "PROXIMITY";
    }

    private LockMode(int i) {
        this.value = i;
    }

    public int getValue() {
        return this.value;
    }

    public static LockMode fromKey(int i) {
        LockMode[] values;
        for (LockMode lockMode : values()) {
            if (lockMode.getValue() == i) {
                return lockMode;
            }
        }
        return UNKNOWN;
    }

    public static LockMode lockModeForLimitedDiscoveryTime(int i) {
        return i == 0 ? PROXIMITYSWIPE : TOUCH;
    }
}
