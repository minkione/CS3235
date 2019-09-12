package com.masterlock.core.audit.events;

public enum InvalidWirelessAccessAttemptType {
    UNAUTHENTICATED_USER(0),
    INVALID_SESSION_TIME(1),
    REPLAY(2),
    UNAUTHENTICATED_CMD(3),
    INVALID_AUTHENTICATED_CMD(4),
    NOT_PERMITTED(5),
    NOT_SCHEDULED(6);
    
    private final byte mValue;

    private InvalidWirelessAccessAttemptType(byte b) {
        this.mValue = b;
    }

    public byte getValue() {
        return this.mValue;
    }

    public static InvalidWirelessAccessAttemptType valueOf(byte b) {
        InvalidWirelessAccessAttemptType[] values;
        for (InvalidWirelessAccessAttemptType invalidWirelessAccessAttemptType : values()) {
            if (invalidWirelessAccessAttemptType.getValue() == b) {
                return invalidWirelessAccessAttemptType;
            }
        }
        throw new IllegalArgumentException("Invalid input byte");
    }
}
