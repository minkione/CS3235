package com.masterlock.ble.app.util;

import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.bus.PasscodeTimeoutEvent;
import com.squareup.otto.Bus;
import javax.inject.Inject;

public class PasscodeTimeoutTask implements Runnable {
    public static final long PASSCODE_TIMEOUT = 15;
    @Inject
    Bus mEventBus;
    private long startTime = System.currentTimeMillis();

    public PasscodeTimeoutTask() {
        MasterLockApp.get().inject(this);
    }

    public void run() {
        MasterLockSharedPreferences.getInstance().putCanManageLock(false);
        this.mEventBus.post(new PasscodeTimeoutEvent());
    }
}
