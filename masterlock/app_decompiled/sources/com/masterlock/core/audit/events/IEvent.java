package com.masterlock.core.audit.events;

import java.util.Date;

public abstract class IEvent {
    static int INVALID_VALUE = -1;
    static int NO_USER_ID = -1;
    private boolean isPadlock;
    private String userFullName;

    /* access modifiers changed from: 0000 */
    public abstract Date getCreatedOn();

    /* access modifiers changed from: 0000 */
    public abstract EventCode getEventCode();

    /* access modifiers changed from: 0000 */
    public abstract String getEventValue();

    /* access modifiers changed from: 0000 */
    public abstract int getKMSDeviceKeyAlias();

    public abstract String toString();

    public void setIsPadLock(boolean z) {
        this.isPadlock = z;
    }

    public boolean isPadlock() {
        return this.isPadlock;
    }

    public void setUserFullName(String str) {
        this.userFullName = str;
    }

    public String getUserFullName() {
        return this.userFullName;
    }
}
