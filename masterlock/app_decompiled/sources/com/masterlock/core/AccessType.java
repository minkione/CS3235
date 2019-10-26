package com.masterlock.core;

public enum AccessType {
    UNKNOWN(0),
    OWNER(1),
    CO_OWNER(2),
    GUEST(3);
    
    private final int value;

    private AccessType(int i) {
        this.value = i;
    }

    public int getValue() {
        return this.value;
    }

    public static AccessType fromKey(int i) {
        AccessType[] values;
        for (AccessType accessType : values()) {
            if (accessType.getValue() == i) {
                return accessType;
            }
        }
        return UNKNOWN;
    }
}
