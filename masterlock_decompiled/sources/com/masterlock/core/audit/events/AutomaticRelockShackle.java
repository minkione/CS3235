package com.masterlock.core.audit.events;

import com.masterlock.core.bluetooth.util.LittleEndian;
import java.util.Date;

public class AutomaticRelockShackle extends IEvent {
    public static final AuditEventId mAuditEventId = AuditEventId.AUTOMATIC_RELOCK_SHACKLE;
    private int mTime;

    public String getEventValue() {
        return null;
    }

    public AutomaticRelockShackle(byte[] bArr) {
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
        return EventCode.RELOCK_AUTOMATIC_SHACKLE;
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
