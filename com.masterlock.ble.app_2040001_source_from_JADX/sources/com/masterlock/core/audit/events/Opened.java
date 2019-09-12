package com.masterlock.core.audit.events;

import com.masterlock.core.bluetooth.util.LittleEndian;
import java.util.Date;

public class Opened extends IEvent {
    public static final AuditEventId mAuditEventId = AuditEventId.OPENED;
    private int mTime;

    public String getEventValue() {
        return null;
    }

    public Opened(byte[] bArr) {
        if (bArr.length > 0) {
            this.mTime = LittleEndian.getInt(bArr);
        } else {
            this.mTime = INVALID_VALUE;
        }
    }

    public int getTime() {
        return this.mTime;
    }

    public EventCode getEventCode() {
        return isPadlock() ? EventCode.LOCK_OPENED : EventCode.DOOR_OPENED;
    }

    public int getKMSDeviceKeyAlias() {
        return NO_USER_ID;
    }

    public Date getCreatedOn() {
        if (this.mTime == INVALID_VALUE) {
            return null;
        }
        return new Date(((long) this.mTime) * 1000);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Event ID: ");
        sb.append(mAuditEventId.name());
        sb.append("\nTime: ");
        sb.append(this.mTime);
        sb.append("\n");
        return sb.toString();
    }
}
