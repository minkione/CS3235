package com.masterlock.core.audit.events;

import com.google.common.base.Ascii;

public enum AuditEventId {
    FIRMWARE_UPDATE_INITIATED(1),
    FIRMWARE_UPDATE_COMPLETED(2),
    TIME_STOPPED(3),
    TIME_WRITTEN(4),
    INVALID_WIRELESS_ATTEMPT(5),
    INVALID_PASSCODE(6),
    WIRELESS_UNLOCK(7),
    PASSCODE_UNLOCK(8),
    OPENED(9),
    CLOSED(10),
    AUTOMATIC_RELOCK(Ascii.f94VT),
    WIRELESS_UNLOCK_SHACKLE(12),
    PASSCODE_UNLOCK_SHACKLE(13),
    OPENED_SHACKLE(Ascii.f91SO),
    CLOSED_SHACKLE(Ascii.f90SI),
    AUTOMATIC_RELOCK_SHACKLE(Ascii.DLE),
    DEMO_MODE_ENABLED(17),
    DEMO_MODE_DISABLED(18);
    
    private final byte mValue;

    private AuditEventId(byte b) {
        this.mValue = b;
    }

    public byte getValue() {
        return this.mValue;
    }

    public static AuditEventId valueOf(byte b) {
        AuditEventId[] values;
        for (AuditEventId auditEventId : values()) {
            if (auditEventId.getValue() == b) {
                return auditEventId;
            }
        }
        throw new IllegalArgumentException("Invalid input byte");
    }
}
