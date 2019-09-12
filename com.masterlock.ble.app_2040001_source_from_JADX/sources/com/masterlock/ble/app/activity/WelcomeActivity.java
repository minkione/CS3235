package com.masterlock.ble.app.activity;

import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.bus.WelcomeActivityBackButtonPressed;
import com.masterlock.ble.app.bus.WelcomeActivityFinish;
import com.masterlock.ble.app.screens.WelcomeScreens.Walkthrough;
import com.squareup.otto.Subscribe;

public class WelcomeActivity extends FlowActivity {
    public int getContentView() {
        return C1075R.layout.no_action_bar_activity;
    }

    public void showNoConnectionDialog() {
    }

    public Object defaultScreen() {
        return new Walkthrough();
    }

    public void onBackPressed() {
        this.mEventBus.post(new WelcomeActivityBackButtonPressed());
    }

    @Subscribe
    public void finish(WelcomeActivityFinish welcomeActivityFinish) {
        finish();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mEventBus.register(this);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mEventBus.unregister(this);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }
}
