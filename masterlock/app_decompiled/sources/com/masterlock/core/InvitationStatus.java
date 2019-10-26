package com.masterlock.core;

public enum InvitationStatus {
    UNKNOWN(0),
    ACTIVE(1),
    PENDING(5);
    
    private final int value;

    private InvitationStatus(int i) {
        this.value = i;
    }

    public int getValue() {
        return this.value;
    }

    public static InvitationStatus fromKey(int i) {
        InvitationStatus[] values;
        for (InvitationStatus invitationStatus : values()) {
            if (invitationStatus.getValue() == i) {
                return invitationStatus;
            }
        }
        return UNKNOWN;
    }
}
