package com.masterlock.core;

public enum SecondaryCodeIndex {
    SECONDARY_PASSCODE_1(0),
    SECONDARY_PASSCODE_2(1),
    SECONDARY_PASSCODE_3(2),
    SECONDARY_PASSCODE_4(3),
    SECONDARY_PASSCODE_5(4);
    
    private int mValue;

    private SecondaryCodeIndex(int i) {
        this.mValue = i;
    }

    public int getValue() {
        return this.mValue;
    }

    public byte getByte() {
        return (byte) this.mValue;
    }

    public static SecondaryCodeIndex fromValue(int i) {
        SecondaryCodeIndex[] values;
        for (SecondaryCodeIndex secondaryCodeIndex : values()) {
            if (secondaryCodeIndex.getValue() == i) {
                return secondaryCodeIndex;
            }
        }
        return null;
    }
}
