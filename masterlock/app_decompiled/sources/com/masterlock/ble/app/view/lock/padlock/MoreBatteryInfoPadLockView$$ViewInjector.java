package com.masterlock.ble.app.view.lock.padlock;

import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.view.nav.MasterLockWebView;

public class MoreBatteryInfoPadLockView$$ViewInjector {
    public static void inject(Finder finder, MoreBatteryInfoPadLockView moreBatteryInfoPadLockView, Object obj) {
        moreBatteryInfoPadLockView.mHelpWebView = (MasterLockWebView) finder.findRequiredView(obj, C1075R.C1077id.more_battery_info, "field 'mHelpWebView'");
    }

    public static void reset(MoreBatteryInfoPadLockView moreBatteryInfoPadLockView) {
        moreBatteryInfoPadLockView.mHelpWebView = null;
    }
}
