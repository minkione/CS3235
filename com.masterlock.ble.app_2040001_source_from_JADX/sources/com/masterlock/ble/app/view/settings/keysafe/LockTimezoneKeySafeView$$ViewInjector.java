package com.masterlock.ble.app.view.settings.keysafe;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class LockTimezoneKeySafeView$$ViewInjector {
    public static void inject(Finder finder, final LockTimezoneKeySafeView lockTimezoneKeySafeView, Object obj) {
        lockTimezoneKeySafeView.mTimezoneSpinner = (Spinner) finder.findRequiredView(obj, C1075R.C1077id.lock_timezones, "field 'mTimezoneSpinner'");
        lockTimezoneKeySafeView.mLockNameBanner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_name_banner, "field 'mLockNameBanner'");
        lockTimezoneKeySafeView.textDeviceId = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id_banner, "field 'textDeviceId'");
        finder.findRequiredView(obj, C1075R.C1077id.btn_save_name, "method 'onSaveClicked'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                lockTimezoneKeySafeView.onSaveClicked();
            }
        });
    }

    public static void reset(LockTimezoneKeySafeView lockTimezoneKeySafeView) {
        lockTimezoneKeySafeView.mTimezoneSpinner = null;
        lockTimezoneKeySafeView.mLockNameBanner = null;
        lockTimezoneKeySafeView.textDeviceId = null;
    }
}
