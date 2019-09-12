package org.zff.ble.communication;

public class SmartLockInterface {

    public interface LeInputStreamListener {
        void onRead();
    }

    public interface LeVerifyCallback {
        void onFailed(Peripheral peripheral);

        void onSuccess(Peripheral peripheral);
    }

    public interface SmartLockEventListener {
        void onVerifyFailed(Peripheral peripheral);

        void onVerifySuccess(Peripheral peripheral);
    }
}
