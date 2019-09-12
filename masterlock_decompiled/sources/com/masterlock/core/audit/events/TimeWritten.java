package com.masterlock.core.audit.events;

import com.masterlock.core.bluetooth.util.LittleEndian;
import java.util.Date;

public class TimeWritten extends IEvent {
    public static final int NEW_TIME_LOCATION = 8;
    public static final int USER_ID_LOCATION = 4;
    public static final AuditEventId mAuditEventId = AuditEventId.TIME_WRITTEN;
    private int mNewTime;
    private int mTime;
    private int mUserId;

    public TimeWritten(byte[] bArr) {
        this.mTime = LittleEndian.getInt(bArr);
        this.mUserId = LittleEndian.getInt(bArr, 4);
        this.mNewTime = LittleEndian.getInt(bArr, 8);
    }

    public int getTime() {
        return this.mTime;
    }

    public int getUserId() {
        return this.mUserId;
    }

    public int getNewTime() {
        return this.mNewTime;
    }

    public String getEventValue() {
        return String.valueOf(this.mNewTime);
    }

    public EventCode getEventCode() {
        return EventCode.TIME_WRITTEN;
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
        sb.append("\nNew Time: ");
        sb.append(this.mNewTime);
        sb.append("\n");
        return sb.toString();
    }
}
