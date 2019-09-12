package com.masterlock.ble.app.view.settings.padlock;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class LockTimezonePadLockView$$ViewInjector {
    public static void inject(Finder finder, final LockTimezonePadLockView lockTimezonePadLockView, Object obj) {
        lockTimezonePadLockView.mTimezoneSpinner = (Spinner) finder.findRequiredView(obj, C1075R.C1077id.lock_timezones, "field 'mTimezoneSpinner'");
        lockTimezonePadLockView.mLockNameBanner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_name_banner, "field 'mLockNameBanner'");
        lockTimezonePadLockView.textDeviceId = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id_banner, "field 'textDeviceId'");
        finder.findRequiredView(obj, C1075R.C1077id.btn_save_name, "method 'onSaveClicked'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                lockTimezonePadLockView.onSaveClicked();
            }
        });
    }

    public static void reset(LockTimezonePadLockView lockTimezonePadLockView) {
        lockTimezonePadLockView.mTimezoneSpinner = null;
        lockTimezonePadLockView.mLockNameBanner = null;
        lockTimezonePadLockView.textDeviceId = null;
    }
}
