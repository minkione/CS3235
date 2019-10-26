package com.masterlock.ble.app.presenter.welcome;

import android.os.CountDownTimer;
import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.screens.WelcomeScreens.Walkthrough;
import com.masterlock.ble.app.view.welcome.WelcomeView;
import com.square.flow.appflow.AppFlow;

public class WelcomePresenter extends Presenter<Void, WelcomeView> {
    private CountDownTimer mCountDownTimer;

    public WelcomePresenter(WelcomeView welcomeView) {
        super(welcomeView);
    }

    public void start() {
        C13581 r0 = new CountDownTimer(2600, 2600) {
            public void onTick(long j) {
            }

            public void onFinish() {
                AppFlow.get(((WelcomeView) WelcomePresenter.this.view).getContext()).goTo(new Walkthrough());
            }
        };
        this.mCountDownTimer = r0;
        this.mCountDownTimer.start();
    }

    public void finish() {
        super.finish();
        this.mCountDownTimer.cancel();
    }
}
