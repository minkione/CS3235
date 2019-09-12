package com.masterlock.ble.app.view.settings.keysafe;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.view.settings.UnlockModeCard;

public class UnlockModeKeySafeListView$$ViewInjector {
    public static void inject(Finder finder, final UnlockModeKeySafeListView unlockModeKeySafeListView, Object obj) {
        unlockModeKeySafeListView.mProximitySwipeModeCard = (UnlockModeCard) finder.findRequiredView(obj, C1075R.C1077id.proximity_swipe_mode_card, "field 'mProximitySwipeModeCard'");
        unlockModeKeySafeListView.mTouchModeCard = (UnlockModeCard) finder.findRequiredView(obj, C1075R.C1077id.touch_mode_card, "field 'mTouchModeCard'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.btn_save_unlock_mode, "field 'mSaveButton' and method 'onSave'");
        unlockModeKeySafeListView.mSaveButton = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                unlockModeKeySafeListView.onSave();
            }
        });
        unlockModeKeySafeListView.mLockNameBanner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_name_banner, "field 'mLockNameBanner'");
        unlockModeKeySafeListView.txtDeviceId = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id_banner, "field 'txtDeviceId'");
    }

    public static void reset(UnlockModeKeySafeListView unlockModeKeySafeListView) {
        unlockModeKeySafeListView.mProximitySwipeModeCard = null;
        unlockModeKeySafeListView.mTouchModeCard = null;
        unlockModeKeySafeListView.mSaveButton = null;
        unlockModeKeySafeListView.mLockNameBanner = null;
        unlockModeKeySafeListView.txtDeviceId = null;
    }
}
