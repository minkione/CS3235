package com.masterlock.ble.app.view.settings.keysafe;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class ResetKeysKeySafeView$$ViewInjector {
    public static void inject(Finder finder, final ResetKeysKeySafeView resetKeysKeySafeView, Object obj) {
        resetKeysKeySafeView.mLockNameBanner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_name_banner, "field 'mLockNameBanner'");
        resetKeysKeySafeView.textDeviceId = (TextView) finder.findOptionalView(obj, C1075R.C1077id.device_id_banner);
        finder.findRequiredView(obj, C1075R.C1077id.btn_reset_keys, "method 'onSaveClicked'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                resetKeysKeySafeView.onSaveClicked();
            }
        });
    }

    public static void reset(ResetKeysKeySafeView resetKeysKeySafeView) {
        resetKeysKeySafeView.mLockNameBanner = null;
        resetKeysKeySafeView.textDeviceId = null;
    }
}
