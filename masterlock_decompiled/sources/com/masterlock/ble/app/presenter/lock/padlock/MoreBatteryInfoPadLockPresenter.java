package com.masterlock.ble.app.presenter.lock.padlock;

import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.view.lock.padlock.MoreBatteryInfoPadLockView;
import com.masterlock.core.Lock;

public class MoreBatteryInfoPadLockPresenter extends AuthenticatedPresenter<Lock, MoreBatteryInfoPadLockView> {
    public MoreBatteryInfoPadLockPresenter(MoreBatteryInfoPadLockView moreBatteryInfoPadLockView) {
        super(moreBatteryInfoPadLockView);
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
