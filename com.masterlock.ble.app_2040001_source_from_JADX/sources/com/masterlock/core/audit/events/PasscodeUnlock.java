package com.masterlock.core.audit.events;

import com.masterlock.core.bluetooth.util.LittleEndian;
import java.util.Date;

public class PasscodeUnlock extends IEvent {
    public static final int DATA_LENGTH_DEFAULT_WITH_TIME = 6;
    public static final int DATA_LENGTH_SECONDARY_WITH_TIME = 7;
    public static final int DATA_LENGTH_TEMPORARY_WITH_TIME = 10;
    public static final int INVALID_VALUE = -1;
    public static final int PASSCODE_COUNTER_LOCATION = 5;
    public static final int PASSCODE_TYPE_LOCATION = 4;
    public static final int SECONDARY_PASSCODE_INDEX_LOCATION = 6;
    public static final int UNLOCK_TIME_LOCATION_DEFAULT = 5;
    public static final int UNLOCK_TIME_LOCATION_FOR_SECONDARY = 5;
    public static final int UNLOCK_TIME_LOCATION_FOR_TEMPORARY = 9;
    public static final AuditEventId mAuditEventId = AuditEventId.PASSCODE_UNLOCK;
    private int mPasscodeCounter;
    private PasscodeType mPasscodeType;
    private int mSecondaryPasscodeIndex;
    private int mTime;
    private int mUnlockTime;

    public PasscodeUnlock(byte[] bArr) {
        this.mTime = LittleEndian.getInt(bArr);
        this.mPasscodeType = PasscodeType.valueOf(bArr[4]);
        byte b = -1;
        if (this.mPasscodeType.equals(PasscodeType.TEMPORARY)) {
            this.mPasscodeCounter = LittleEndian.getInt(bArr, 5);
            if (bArr.length == 10) {
                b = bArr[9];
            }
            this.mUnlockTime = b;
        } else if (!this.mPasscodeType.equals(PasscodeType.SECONDARY)) {
            if (bArr.length == 6) {
                b = bArr[5];
            }
            this.mUnlockTime = b;
        } else if (bArr.length != 7) {
            this.mUnlockTime = -1;
            this.mSecondaryPasscodeIndex = bArr[5];
        } else {
            this.mUnlockTime = bArr[5];
            this.mSecondaryPasscodeIndex = bArr[6];
        }
    }

    public int getTime() {
        return this.mTime;
    }

    public PasscodeType getPasscodeType() {
        return this.mPasscodeType;
    }

    public int getPasscodeCounter() {
        return this.mPasscodeCounter;
    }

    public int getUnlockTime() {
        return this.mUnlockTime;
    }

    public int getSecondaryPasscodeIndex() {
        return this.mSecondaryPasscodeIndex;
    }

    public String getEventValue() {
        StringBuilder sb = new StringBuilder();
        if (this.mPasscodeType == PasscodeType.SECONDARY) {
            sb.append(String.valueOf(this.mSecondaryPasscodeIndex + 1));
            sb.append("|");
        }
        int i = this.mPasscodeCounter;
        if (i == -1 || this.mUnlockTime == -1) {
            int i2 = this.mPasscodeCounter;
            if (i2 != -1) {
                sb.append(i2);
            } else {
                int i3 = this.mUnlockTime;
                if (i3 != -1) {
                    sb.append(i3);
                }
            }
        } else {
            sb.append(i);
            sb.append(";");
            sb.append(this.mUnlockTime);
        }
        return sb.toString();
    }

    public EventCode getEventCode() {
        switch (this.mPasscodeType) {
            case MASTER:
                return isPadlock() ? EventCode.UNLOCK_MASTERCODE : EventCode.UNLOCK_DOOR_MASTERCODE;
            case TEMPORARY:
                return isPadlock() ? EventCode.UNLOCK_SERVICECODE : EventCode.UNLOCK_DOOR_SERVICECODE;
            case SECONDARY:
                return EventCode.UNLOCK_SECONDARY_PASSCODE;
            default:
                return isPadlock() ? EventCode.UNLOCK_PRIMARYCODE : EventCode.UNLOCK_DOOR_PRIMARYCODE;
        }
    }

    public int getKMSDeviceKeyAlias() {
        return NO_USER_ID;
    }

    public Date getCreatedOn() {
        return new Date(((long) this.mTime) * 1000);
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("Event ID: ");
        sb.append(mAuditEventId.name());
        sb.append("\nTime: ");
        sb.append(this.mTime);
        sb.append("\nPasscode Type: ");
        sb.append(this.mPasscodeType.name());
        sb.append("\nPasscode Counter: ");
        sb.append(this.mPasscodeCounter);
        sb.append("\n");
        if (this.mPasscodeType == PasscodeType.SECONDARY) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("SecondaryPasscodeIndex: ");
            sb2.append(this.mSecondaryPasscodeIndex);
            str = sb2.toString();
        } else {
            str = "";
        }
        sb.append(str);
        return sb.toString();
    }
}
