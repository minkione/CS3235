package com.masterlock.ble.app.presenter.welcome;

import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.bus.WelcomeActivityBackButtonPressed;
import com.masterlock.ble.app.bus.WelcomeActivityFinish;
import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.view.welcome.WelcomeWalkthrough;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import javax.inject.Inject;

public class WelcomeWalkthroughPresenter extends Presenter<Void, WelcomeWalkthrough> {
    @Inject
    Bus mEventBus;

    public WelcomeWalkthroughPresenter(WelcomeWalkthrough welcomeWalkthrough) {
        super(welcomeWalkthrough);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        this.mEventBus.register(this);
    }

    public void finish() {
        super.finish();
        this.mEventBus.unregister(this);
    }

    @Subscribe
    public void onBackPressed(WelcomeActivityBackButtonPressed welcomeActivityBackButtonPressed) {
        ((WelcomeWalkthrough) this.view).onBackPressed();
    }

    public void finishActivity() {
        this.mEventBus.post(new WelcomeActivityFinish());
    }
}
