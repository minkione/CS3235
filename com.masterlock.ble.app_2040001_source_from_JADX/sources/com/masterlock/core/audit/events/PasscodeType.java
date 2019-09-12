package com.masterlock.core.audit.events;

public enum PasscodeType {
    MASTER(0),
    PRIMARY(1),
    TEMPORARY(2),
    SECONDARY(4),
    DEMO_MODE(8);
    
    private final byte mValue;

    private PasscodeType(byte b) {
        this.mValue = b;
    }

    public byte getValue() {
        return this.mValue;
    }

    public static PasscodeType valueOf(byte b) {
        PasscodeType[] values;
        for (PasscodeType passcodeType : values()) {
            if (passcodeType.getValue() == b) {
                return passcodeType;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid input byte: ");
        sb.append(b);
        throw new IllegalArgumentException(sb.toString());
    }
}
