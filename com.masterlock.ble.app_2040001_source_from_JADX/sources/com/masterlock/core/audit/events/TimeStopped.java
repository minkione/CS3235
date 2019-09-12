package com.masterlock.core.audit.events;

import com.masterlock.core.bluetooth.util.LittleEndian;
import java.util.Date;

public class TimeStopped extends IEvent {
    public static final AuditEventId mAuditEventId = AuditEventId.TIME_STOPPED;
    private int mTime;

    public String getEventValue() {
        return null;
    }

    public TimeStopped(byte[] bArr) {
        this.mTime = LittleEndian.getInt(bArr);
    }

    public int getTime() {
        return this.mTime;
    }

    public EventCode getEventCode() {
        return EventCode.TIME_STOPPED;
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
        return sb.toString();
    }
}
