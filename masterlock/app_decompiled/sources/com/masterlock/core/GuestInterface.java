package com.masterlock.core;

public enum GuestInterface {
    SIMPLE(1),
    ADVANCED(2);
    
    int value;

    private GuestInterface(int i) {
        this.value = i;
    }

    public int getValue() {
        return this.value;
    }

    public static GuestInterface fromKey(int i) {
        GuestInterface[] values;
        for (GuestInterface guestInterface : values()) {
            if (guestInterface.getValue() == i) {
                return guestInterface;
            }
        }
        return SIMPLE;
    }
}
