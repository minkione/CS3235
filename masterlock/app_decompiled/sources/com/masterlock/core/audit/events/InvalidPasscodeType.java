package com.masterlock.core.audit.events;

public enum InvalidPasscodeType {
    MASTER_OR_PRIMARY_OR_SECONDARY(0),
    TEMPORARY(1),
    INDETERMINATE(2);
    
    private final byte mValue;

    private InvalidPasscodeType(byte b) {
        this.mValue = b;
    }

    public byte getValue() {
        return this.mValue;
    }

    public static InvalidPasscodeType valueOf(byte b) {
        InvalidPasscodeType[] values;
        for (InvalidPasscodeType invalidPasscodeType : values()) {
            if (invalidPasscodeType.getValue() == b) {
                return invalidPasscodeType;
            }
        }
        throw new IllegalArgumentException("Invalid input byte");
    }
}
