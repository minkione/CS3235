package com.masterlock.ble.app.view.settings.padlock;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.view.settings.UnlockModeCard;

public class UnlockModePadLockListView$$ViewInjector {
    public static void inject(Finder finder, final UnlockModePadLockListView unlockModePadLockListView, Object obj) {
        unlockModePadLockListView.mProximitySwipeModeCard = (UnlockModeCard) finder.findRequiredView(obj, C1075R.C1077id.proximity_swipe_mode_card, "field 'mProximitySwipeModeCard'");
        unlockModePadLockListView.mTouchModeCard = (UnlockModeCard) finder.findRequiredView(obj, C1075R.C1077id.touch_mode_card, "field 'mTouchModeCard'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.btn_save_unlock_mode, "field 'mSaveButton' and method 'onSave'");
        unlockModePadLockListView.mSaveButton = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                unlockModePadLockListView.onSave();
            }
        });
        unlockModePadLockListView.mLockNameBanner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_name_banner, "field 'mLockNameBanner'");
        unlockModePadLockListView.txtDeviceId = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id_banner, "field 'txtDeviceId'");
    }

    public static void reset(UnlockModePadLockListView unlockModePadLockListView) {
        unlockModePadLockListView.mProximitySwipeModeCard = null;
        unlockModePadLockListView.mTouchModeCard = null;
        unlockModePadLockListView.mSaveButton = null;
        unlockModePadLockListView.mLockNameBanner = null;
        unlockModePadLockListView.txtDeviceId = null;
    }
}
