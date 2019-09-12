package com.masterlock.core;

import com.google.common.base.Ascii;

public enum AvailableCommandType {
    UNKNOWN((String) 0),
    CMD_DISCONNECT((String) 0),
    CMD_KEEP_ALIVE((String) 1),
    CMD_WRITE_AUTHENTICATED_MEMORY((String) 2),
    CMD_READ_MEMORY((String) 3),
    CMD_WRITE_MEMORY((String) 4),
    CMD_READ_STATE_PRIMARY((String) 5),
    CMD_READ_BATTERY_LEVEL((String) 6),
    CMD_READ_TEMPERATURE((String) 7),
    CMD_UNLOCK_PRIMARY((String) 8),
    CMD_READ_TIME((String) 9),
    CMD_WRITE_TIME((String) 10),
    CMD_NUDGE_TIME((String) Ascii.f94VT),
    CMD_READ_AUDIT_TRIAL((String) 12),
    CMD_READ_AUDIT_TRAIL_EVENT_INDEX((String) 13),
    CMD_READ_STATE_SECONDARY((String) Ascii.f91SO),
    CMD_UNLOCK_SECONDARY((String) Ascii.f90SI),
    CMD_CLEAR_INDIVIDUAL_SECONDARY_PASSCODE_BY_INDEX((String) Ascii.DLE),
    CMD_CLEAR_ALL_SECONDARY_PASSCODES((String) 17),
    CMD_ENABLE_DEMO_MODE((String) 18),
    CMD_DISABLE_DEMO_MODE((String) 19);
    
    private final byte mValue;

    private AvailableCommandType(int i) {
        this.mValue = (byte) i;
    }

    private AvailableCommandType(byte b) {
        this.mValue = b;
    }

    public byte getValue() {
        return this.mValue;
    }

    public static AvailableCommandType fromKey(byte b) {
        AvailableCommandType[] values;
        for (AvailableCommandType availableCommandType : values()) {
            if (availableCommandType.getValue() == b) {
                return availableCommandType;
            }
        }
        return UNKNOWN;
    }
}
