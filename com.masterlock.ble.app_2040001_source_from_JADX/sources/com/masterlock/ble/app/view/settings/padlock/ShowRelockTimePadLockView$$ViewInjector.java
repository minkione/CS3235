package com.masterlock.ble.app.view.settings.padlock;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class ShowRelockTimePadLockView$$ViewInjector {
    public static void inject(Finder finder, final ShowRelockTimePadLockView showRelockTimePadLockView, Object obj) {
        showRelockTimePadLockView.mRelockTime = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_relock_time, "field 'mRelockTime'");
        showRelockTimePadLockView.buttonBar = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.button_bar, "field 'buttonBar'");
        finder.findRequiredView(obj, C1075R.C1077id.btn_change_relock_time, "method 'changeRelockTime'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                showRelockTimePadLockView.changeRelockTime();
            }
        });
    }

    public static void reset(ShowRelockTimePadLockView showRelockTimePadLockView) {
        showRelockTimePadLockView.mRelockTime = null;
        showRelockTimePadLockView.buttonBar = null;
    }
}
