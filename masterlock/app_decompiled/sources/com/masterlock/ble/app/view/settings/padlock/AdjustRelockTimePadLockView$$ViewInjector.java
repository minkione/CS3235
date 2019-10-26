package com.masterlock.ble.app.view.settings.padlock;

import android.view.View;
import android.view.View.OnClickListener;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class AdjustRelockTimePadLockView$$ViewInjector {
    public static void reset(AdjustRelockTimePadLockView adjustRelockTimePadLockView) {
    }

    public static void inject(Finder finder, final AdjustRelockTimePadLockView adjustRelockTimePadLockView, Object obj) {
        finder.findRequiredView(obj, C1075R.C1077id.btn_save_relock_time, "method 'saveRelockTime'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                adjustRelockTimePadLockView.saveRelockTime();
            }
        });
    }
}
