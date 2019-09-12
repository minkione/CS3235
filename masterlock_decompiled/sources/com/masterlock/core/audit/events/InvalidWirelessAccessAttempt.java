package com.masterlock.core.audit.events;

import com.masterlock.core.bluetooth.util.LittleEndian;
import java.util.Date;

public class InvalidWirelessAccessAttempt extends IEvent {
    public static final int ARRAY_WITH_USER_ID_SIZE = 9;
    public static final int TYPE_ARRAY_LOCATION = 4;
    public static final int USER_ARRAY_LOCATION = 5;
    public static final AuditEventId mAuditEventId = AuditEventId.INVALID_WIRELESS_ATTEMPT;
    private int mTime;
    private InvalidWirelessAccessAttemptType mType;
    private int mUserId;

    public String getEventValue() {
        return null;
    }

    public InvalidWirelessAccessAttempt(byte[] bArr) {
        this.mTime = LittleEndian.getInt(bArr);
        this.mType = InvalidWirelessAccessAttemptType.valueOf(bArr[4]);
        if (bArr.length == 9) {
            this.mUserId = LittleEndian.getInt(bArr, 5);
        }
    }

    public int getTime() {
        return this.mTime;
    }

    public InvalidWirelessAccessAttemptType getType() {
        return this.mType;
    }

    public String getTypeName() {
        return this.mType.name();
    }

    public EventCode getEventCode() {
        switch (this.mType) {
            case INVALID_SESSION_TIME:
                return EventCode.INVALID_WIRELESS_SESSION_TIME;
            case REPLAY:
                return EventCode.INVALID_WIRELESS_REPLAY;
            case UNAUTHENTICATED_CMD:
                return EventCode.INVALID_WIRELESS_UNAUTHENTICATED_CMD;
            case INVALID_AUTHENTICATED_CMD:
                return EventCode.INVALID_WIRELESS_AUTHENTICATED_CMD;
            case NOT_PERMITTED:
                return EventCode.INVALID_WIRELESS_NOT_PERMITTED;
            case NOT_SCHEDULED:
                return EventCode.INVALID_WIRELESS_NOT_SHEDULED;
            default:
                return EventCode.INVALID_WIRELESS_UNAUTHENTICATED_USER;
        }
    }

    public int getKMSDeviceKeyAlias() {
        return this.mUserId;
    }

    public Date getCreatedOn() {
        return new Date(((long) this.mTime) * 1000);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Event ID: ");
        sb.append(mAuditEventId.name());
        sb.append("\nTime: ");
        sb.append(this.mTime);
        sb.append("\nEvent Type: ");
        sb.append(this.mType.name());
        return sb.toString();
    }
}
