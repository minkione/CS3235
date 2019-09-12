package com.masterlock.ble.app.view.lock.keysafe;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class UnlockShackleView$$ViewInjector {
    public static void inject(Finder finder, final UnlockShackleView unlockShackleView, Object obj) {
        unlockShackleView.mLockNameBanner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_name_banner, "field 'mLockNameBanner'");
        unlockShackleView.mDeviceIdBanner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id_banner, "field 'mDeviceIdBanner'");
        unlockShackleView.mContentFlipper = (ViewFlipper) finder.findRequiredView(obj, C1075R.C1077id.content_flipper, "field 'mContentFlipper'");
        unlockShackleView.mButtonBar = finder.findRequiredView(obj, C1075R.C1077id.button_bar, "field 'mButtonBar'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.primary_action_button, "field 'mPrimaryActionButton'");
        unlockShackleView.mPrimaryActionButton = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                unlockShackleView.onPrimaryClicked();
            }
        });
    }

    public static void reset(UnlockShackleView unlockShackleView) {
        unlockShackleView.mLockNameBanner = null;
        unlockShackleView.mDeviceIdBanner = null;
        unlockShackleView.mContentFlipper = null;
        unlockShackleView.mButtonBar = null;
        unlockShackleView.mPrimaryActionButton = null;
    }
}
