package com.wjy.smartlock;

public class SmartLockEvent {

    public enum EventType {
        CONNECTED,
        DISCONNECT,
        MODIFY_NAME,
        MODIFY_PASSWORD,
        NOTIFY,
        VIBRATE,
        AUTOLOCK,
        SM_LOCK,
        SM_UNLOCK,
        SM_STAY_LOCK,
        BATTERY_CHANGE,
        GET_LOCK_INFO,
        SET_EVENT_FAIL,
        RESET_PASSWORD
    }

    public interface EventTypeValue {
        public static final int AUTO_LOCK_DISABLE = 0;
        public static final int AUTO_LOCK_ENABLE = 1;
        public static final int SM_LOCK = 1;
        public static final int SM_STAY_LOCK = 2;
        public static final int SM_UNLOCK = 0;
        public static final int VIBRATE_DISABLE = 0;
        public static final int VIBRATE_ENABLE = 1;
    }

    public interface OnSmartLockEventListener {
        void onEvent(EventType eventType);
    }

    public interface OnSmartLockPretreatmentEventListener {
        void onPretreatmentEvent(EventType eventType);
    }

    public interface OnSmartLockResumeConnectListener {
        void onResumeConnectVerifyFailed(SmartLock smartLock);

        void onResumeConnectVerifySuccess(SmartLock smartLock);
    }

    public interface OnSmartLockVerifyListener {
        void onVerifyFailed(SmartLock smartLock);

        void onVerifySuccess(SmartLock smartLock);
    }

    public SmartLockEvent(SmartLock sm) {
    }
}
