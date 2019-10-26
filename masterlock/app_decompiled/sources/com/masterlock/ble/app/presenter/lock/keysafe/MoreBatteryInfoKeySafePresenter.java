package com.masterlock.ble.app.presenter.lock.keysafe;

import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.view.lock.keysafe.MoreBatteryInfoKeySafeView;
import com.masterlock.core.Lock;

public class MoreBatteryInfoKeySafePresenter extends AuthenticatedPresenter<Lock, MoreBatteryInfoKeySafeView> {
    public MoreBatteryInfoKeySafePresenter(MoreBatteryInfoKeySafeView moreBatteryInfoKeySafeView) {
        super(moreBatteryInfoKeySafeView);
    }

    public void start() {
        super.start();
        startProgress();
    }

    public void finish() {
        super.finish();
        stopProgress();
    }

    public void startProgress() {
        this.mEventBus.post(new ToggleProgressBarEvent(true));
    }

    public void stopProgress() {
        this.mEventBus.post(new ToggleProgressBarEvent(false));
    }
}
