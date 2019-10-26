package com.masterlock.core.audit.events;

import com.masterlock.core.bluetooth.util.LittleEndian;
import java.util.Date;

public class FirmwareUpdateInitiated extends IEvent {
    public static final int FIRMWARE_VERSION_LOCATION = 8;
    public static final int USER_ID_LOCATION = 4;
    public static final AuditEventId mAuditEventId = AuditEventId.FIRMWARE_UPDATE_INITIATED;
    private int mFirmwareVersion;
    private int mTime;
    private int mUserId;

    public FirmwareUpdateInitiated(byte[] bArr) {
        this.mTime = LittleEndian.getInt(bArr);
        this.mUserId = LittleEndian.getInt(bArr, 4);
        this.mFirmwareVersion = LittleEndian.getInt(bArr, 8);
    }

    public int getUserId() {
        return this.mUserId;
    }

    public int getFirmwareVersion() {
        return this.mFirmwareVersion;
    }

    public int getTime() {
        return this.mTime;
    }

    public String getEventValue() {
        return String.valueOf(this.mFirmwareVersion);
    }

    public EventCode getEventCode() {
        return EventCode.FIRMWARE_UPDATE_INITIATED;
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
        sb.append("\nUser ID: ");
        sb.append(this.mUserId);
        sb.append("\nFirmwareVersion: ");
        sb.append(this.mFirmwareVersion);
        return sb.toString();
    }
}
