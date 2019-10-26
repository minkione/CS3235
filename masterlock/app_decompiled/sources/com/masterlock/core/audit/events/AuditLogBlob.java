package com.masterlock.core.audit.events;

import com.masterlock.core.EventSource;
import com.masterlock.core.KmsLogEntry;
import com.masterlock.core.KmsLogEntry.Builder;
import com.masterlock.core.Lock;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class AuditLogBlob {
    public static final int INVALID = -1;
    public static final int SIZE_OF_ID_AND_LENGTH_FIELDS = 2;
    private String fullUserName;
    private int mEntryArrayIndex = 0;
    private final byte[] mEventEntries;
    private int mEventIndex = 0;

    public AuditLogBlob(ByteBuffer byteBuffer, String str) {
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        this.mEventIndex = byteBuffer.getInt();
        this.mEventEntries = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.capacity());
        this.fullUserName = str;
    }

    public int getEventIndex() {
        return this.mEventIndex;
    }

    public byte[] getEventEntries() {
        return this.mEventEntries;
    }

    public byte getEventId() {
        return this.mEventEntries[this.mEntryArrayIndex];
    }

    public byte getEventLength() {
        int i = this.mEntryArrayIndex + 1;
        byte[] bArr = this.mEventEntries;
        if (i >= bArr.length) {
            return -1;
        }
        return bArr[i];
    }

    public boolean moveToNextEvent() {
        int bytesToSkipAhead = bytesToSkipAhead();
        int i = this.mEntryArrayIndex + bytesToSkipAhead;
        this.mEventIndex++;
        this.mEntryArrayIndex = i;
        if (bytesToSkipAhead == -1 || this.mEntryArrayIndex > this.mEventEntries.length) {
            return false;
        }
        return true;
    }

    private int bytesToSkipAhead() {
        if (getEventLength() == -1) {
            return -1;
        }
        return getEventLength() + 2;
    }

    public byte[] getEventData() {
        int i = this.mEntryArrayIndex;
        return Arrays.copyOfRange(this.mEventEntries, i + 2, i + bytesToSkipAhead());
    }

    public IEvent getEvent() {
        if (this.mEntryArrayIndex >= this.mEventEntries.length) {
            return null;
        }
        switch (AuditEventId.valueOf(getEventId())) {
            case FIRMWARE_UPDATE_INITIATED:
                return new FirmwareUpdateInitiated(getEventData());
            case FIRMWARE_UPDATE_COMPLETED:
                return new FirmwareUpdateCompleted(getEventData());
            case TIME_STOPPED:
                return new TimeStopped(getEventData());
            case TIME_WRITTEN:
                return new TimeWritten(getEventData());
            case INVALID_WIRELESS_ATTEMPT:
                return new InvalidWirelessAccessAttempt(getEventData());
            case INVALID_PASSCODE:
                return new InvalidPasscode(getEventData());
            case WIRELESS_UNLOCK:
                return new WirelessUnlock(getEventData());
            case PASSCODE_UNLOCK:
                return new PasscodeUnlock(getEventData());
            case OPENED:
                return new Opened(getEventData());
            case CLOSED:
                return new Closed(getEventData());
            case AUTOMATIC_RELOCK:
                return new AutomaticRelock(getEventData());
            case WIRELESS_UNLOCK_SHACKLE:
                return new WirelessUnlockShackle(getEventData());
            case PASSCODE_UNLOCK_SHACKLE:
                return new PasscodeUnlockShackle(getEventData());
            case OPENED_SHACKLE:
                return new OpenedShackle(getEventData());
            case CLOSED_SHACKLE:
                return new ClosedShackle(getEventData());
            case AUTOMATIC_RELOCK_SHACKLE:
                return new AutomaticRelockShackle(getEventData());
            case DEMO_MODE_ENABLED:
                return new DemoModeEnabled(getEventData());
            case DEMO_MODE_DISABLED:
                return new DemoModeDisabled(getEventData());
            default:
                return null;
        }
    }

    public KmsLogEntry generateKmsLogEntry(Lock lock) {
        Builder builder = new Builder();
        IEvent event = getEvent();
        if (event == null) {
            return null;
        }
        event.setIsPadLock(lock.isPadLock());
        event.setUserFullName(this.fullUserName);
        builder.kmsDeviceId(lock.getKmsId()).eventIndex((long) getEventIndex()).firmwareCounter(Integer.valueOf(lock.getFirmwareCounter())).eventCode(event.getEventCode()).eventSource(EventSource.DEVICE).eventValue(event.getEventValue()).createdOn(event.getCreatedOn());
        if (event.getKMSDeviceKeyAlias() != IEvent.NO_USER_ID) {
            builder.kmsDeviceKeyAlias(Integer.valueOf(event.getKMSDeviceKeyAlias()));
        }
        return builder.build();
    }
}
