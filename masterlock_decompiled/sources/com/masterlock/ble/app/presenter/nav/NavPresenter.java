package com.masterlock.ble.app.presenter.nav;

import android.view.View;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.Presenter;
import com.squareup.otto.Bus;
import javax.inject.Inject;

public class NavPresenter extends Presenter<Void, View> {
    @Inject
    Bus mEventBus;

    public void start() {
    }

    public NavPresenter(View view) {
        super(view);
        MasterLockApp.get().inject(this);
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
