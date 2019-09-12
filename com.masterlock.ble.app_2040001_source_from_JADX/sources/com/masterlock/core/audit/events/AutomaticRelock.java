package com.masterlock.core.audit.events;

import com.masterlock.core.bluetooth.util.LittleEndian;
import java.util.Date;

public class AutomaticRelock extends IEvent {
    public static final AuditEventId mAuditEventId = AuditEventId.AUTOMATIC_RELOCK;
    private int mTime;

    public String getEventValue() {
        return null;
    }

    public AutomaticRelock(byte[] bArr) {
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
        return isPadlock() ? EventCode.RELOCK_AUTOMATIC : EventCode.RELOCK_AUTOMATIC_DOOR;
    }

    public int getKMSDeviceKeyAlias() {
        return NO_USER_ID;
    }

    public Date getCreatedOn() {
        if (this.mTime != INVALID_VALUE) {
            return new Date(((long) this.mTime) * 1000);
        }
        return null;
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
