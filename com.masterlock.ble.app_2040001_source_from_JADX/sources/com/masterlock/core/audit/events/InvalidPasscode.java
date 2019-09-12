package com.masterlock.core.audit.events;

import com.masterlock.core.bluetooth.util.LittleEndian;
import java.util.Date;

public class InvalidPasscode extends IEvent {
    private static final int TYPE_ARRAY_LOCATION = 4;
    public static final AuditEventId mAuditEventId = AuditEventId.INVALID_PASSCODE;
    private int mTime;
    private InvalidPasscodeType mType;

    public String getEventValue() {
        return null;
    }

    public InvalidPasscode(byte[] bArr) {
        this.mTime = LittleEndian.getInt(bArr);
        this.mType = InvalidPasscodeType.valueOf(bArr[4]);
    }

    public InvalidPasscodeType getType() {
        return this.mType;
    }

    public String getTypeName() {
        return this.mType.name();
    }

    public EventCode getEventCode() {
        switch (this.mType) {
            case MASTER_OR_PRIMARY_OR_SECONDARY:
                return EventCode.INVALID_PASSCODE_MASTER;
            case TEMPORARY:
                return EventCode.INVALID_PASSCODE_TEMPORARY;
            default:
                return EventCode.INVALID_PASSCODE_INDETERMINATE;
        }
    }

    public int getKMSDeviceKeyAlias() {
        return NO_USER_ID;
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
