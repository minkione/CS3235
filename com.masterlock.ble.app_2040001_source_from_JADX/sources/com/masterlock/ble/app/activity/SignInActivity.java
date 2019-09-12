package com.masterlock.ble.app.activity;

import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.screens.SignInScreens.SignIn;

public class SignInActivity extends FlowActivity {
    public int getContentView() {
        return C1075R.layout.no_action_bar_activity;
    }

    public Object defaultScreen() {
        return new SignIn();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }
}
