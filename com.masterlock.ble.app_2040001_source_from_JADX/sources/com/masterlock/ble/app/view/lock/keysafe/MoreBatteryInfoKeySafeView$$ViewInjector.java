package com.masterlock.ble.app.view.lock.keysafe;

import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.view.nav.MasterLockWebView;

public class MoreBatteryInfoKeySafeView$$ViewInjector {
    public static void inject(Finder finder, MoreBatteryInfoKeySafeView moreBatteryInfoKeySafeView, Object obj) {
        moreBatteryInfoKeySafeView.mHelpWebView = (MasterLockWebView) finder.findRequiredView(obj, C1075R.C1077id.more_battery_info, "field 'mHelpWebView'");
    }

    public static void reset(MoreBatteryInfoKeySafeView moreBatteryInfoKeySafeView) {
        moreBatteryInfoKeySafeView.mHelpWebView = null;
    }
}
