package com.masterlock.ble.app.view.settings.padlock;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class ResetKeysPadLockView$$ViewInjector {
    public static void inject(Finder finder, final ResetKeysPadLockView resetKeysPadLockView, Object obj) {
        resetKeysPadLockView.mLockNameBanner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_name_banner, "field 'mLockNameBanner'");
        resetKeysPadLockView.textDeviceId = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id_banner, "field 'textDeviceId'");
        finder.findRequiredView(obj, C1075R.C1077id.btn_reset_keys, "method 'onSaveClicked'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                resetKeysPadLockView.onSaveClicked();
            }
        });
    }

    public static void reset(ResetKeysPadLockView resetKeysPadLockView) {
        resetKeysPadLockView.mLockNameBanner = null;
        resetKeysPadLockView.textDeviceId = null;
    }
}
