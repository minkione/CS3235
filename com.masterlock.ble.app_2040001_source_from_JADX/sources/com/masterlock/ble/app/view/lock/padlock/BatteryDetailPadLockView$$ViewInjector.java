package com.masterlock.ble.app.view.lock.padlock;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class BatteryDetailPadLockView$$ViewInjector {
    public static void inject(Finder finder, final BatteryDetailPadLockView batteryDetailPadLockView, Object obj) {
        batteryDetailPadLockView.mWhereIsBattery = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_where_is_battery_desc, "field 'mWhereIsBattery'");
        batteryDetailPadLockView.mHowToChange = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_how_to_change_battery_desc, "field 'mHowToChange'");
        finder.findRequiredView(obj, C1075R.C1077id.btn_more_battery_info, "method 'showBatteryInfoWebView'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                batteryDetailPadLockView.showBatteryInfoWebView();
            }
        });
    }

    public static void reset(BatteryDetailPadLockView batteryDetailPadLockView) {
        batteryDetailPadLockView.mWhereIsBattery = null;
        batteryDetailPadLockView.mHowToChange = null;
    }
}
