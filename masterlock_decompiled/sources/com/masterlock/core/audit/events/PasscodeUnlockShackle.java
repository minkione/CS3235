package com.masterlock.core.audit.events;

import com.masterlock.core.bluetooth.util.LittleEndian;
import java.util.Date;

public class PasscodeUnlockShackle extends IEvent {
    public static final int DATA_LENGTH_WITH_UNLOCK_TIME = 6;
    public static final int INVALID_VALUE = -1;
    public static final int PASSCODE_TYPE_LOCATION = 4;
    public static final int UNLOCK_TIME_LOCATION = 5;
    public static final AuditEventId mAuditEventId = AuditEventId.PASSCODE_UNLOCK_SHACKLE;
    private PasscodeType mPasscodeType;
    private int mTime;
    private int mUnlockTime;

    public PasscodeUnlockShackle(byte[] bArr) {
        this.mTime = LittleEndian.getInt(bArr);
        this.mPasscodeType = PasscodeType.valueOf(bArr[4]);
        this.mUnlockTime = bArr.length == 6 ? bArr[5] : -1;
    }

    public int getTime() {
        return this.mTime;
    }

    public PasscodeType getPasscodeType() {
        return this.mPasscodeType;
    }

    public int getUnlockTime() {
        return this.mUnlockTime;
    }

    public String getEventValue() {
        StringBuilder sb = new StringBuilder();
        int i = this.mUnlockTime;
        if (i != -1) {
            sb.append(i);
        }
        return sb.toString();
    }

    public EventCode getEventCode() {
        return this.mPasscodeType == PasscodeType.MASTER ? EventCode.UNLOCK_SHACKLE_MASTERCODE : EventCode.UNLOCK_SHACKLE_PRIMARYCODE;
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
        sb.append("\nPasscode Type: ");
        sb.append(this.mPasscodeType.name());
        sb.append("\n");
        return sb.toString();
    }
}
