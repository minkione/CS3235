package com.masterlock.ble.app.view.lock.keysafe;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class BatteryDetailKeySafeView$$ViewInjector {
    public static void inject(Finder finder, final BatteryDetailKeySafeView batteryDetailKeySafeView, Object obj) {
        batteryDetailKeySafeView.mWhereIsBattery = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_where_is_battery_desc, "field 'mWhereIsBattery'");
        batteryDetailKeySafeView.mHowToChange = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_how_to_change_battery_desc, "field 'mHowToChange'");
        finder.findRequiredView(obj, C1075R.C1077id.btn_more_battery_info, "method 'showBatteryInfoWebView'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                batteryDetailKeySafeView.showBatteryInfoWebView();
            }
        });
    }

    public static void reset(BatteryDetailKeySafeView batteryDetailKeySafeView) {
        batteryDetailKeySafeView.mWhereIsBattery = null;
        batteryDetailKeySafeView.mHowToChange = null;
    }
}
