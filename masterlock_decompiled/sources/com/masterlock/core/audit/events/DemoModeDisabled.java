package com.masterlock.core.audit.events;

import com.masterlock.core.bluetooth.util.LittleEndian;
import java.util.Date;

public class DemoModeDisabled extends IEvent {
    public static final AuditEventId mAuditEventId = AuditEventId.DEMO_MODE_DISABLED;
    private int mTime;

    public DemoModeDisabled(byte[] bArr) {
        this.mTime = LittleEndian.getInt(bArr);
    }

    public int getTime() {
        return this.mTime;
    }

    public String getEventValue() {
        return String.valueOf(this.mTime);
    }

    public EventCode getEventCode() {
        return EventCode.DEMO_MODE_DISABLED;
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
        sb.append("\n");
        return sb.toString();
    }
}
