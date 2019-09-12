package com.masterlock.core.audit.events;

import com.masterlock.core.bluetooth.util.LittleEndian;
import java.util.Date;

public class WirelessUnlock extends IEvent {
    public static final int DATA_LENGTH_WITH_UNLOCK_TIME = 9;
    public static final int UNLOCK_TIME_LOCATION = 8;
    public static final int USER_ID_LOCATION = 4;
    public static final AuditEventId mAuditEventId = AuditEventId.WIRELESS_UNLOCK;
    private int mTime;
    private int mUnlockTime;
    private int mUserId;

    public WirelessUnlock(byte[] bArr) {
        this.mTime = LittleEndian.getInt(bArr);
        this.mUserId = LittleEndian.getInt(bArr, 4);
        if (bArr.length == 9) {
            this.mUnlockTime = bArr[8];
        } else {
            this.mUnlockTime = INVALID_VALUE;
        }
    }

    public int getTime() {
        return this.mTime;
    }

    public int getUserId() {
        return this.mUserId;
    }

    public int getUnlockTime() {
        return this.mUnlockTime;
    }

    public String getEventValue() {
        return String.valueOf(this.mUnlockTime);
    }

    public EventCode getEventCode() {
        return isPadlock() ? EventCode.UNLOCK_WIRELESS : EventCode.UNLOCK_WIRELESS_DOOR;
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
        sb.append("\nUnlock Time: ");
        sb.append(this.mUnlockTime);
        return sb.toString();
    }
}
