package com.masterlock.ble.app.view.modal;

import android.widget.Button;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class BleLocationPermissionModal$$ViewInjector {
    public static void inject(Finder finder, BleLocationPermissionModal bleLocationPermissionModal, Object obj) {
        bleLocationPermissionModal.mCancel = (Button) finder.findRequiredView(obj, C1075R.C1077id.negative_button, "field 'mCancel'");
        bleLocationPermissionModal.mEnableBleLocation = (Button) finder.findRequiredView(obj, C1075R.C1077id.positive_button, "field 'mEnableBleLocation'");
    }

    public static void reset(BleLocationPermissionModal bleLocationPermissionModal) {
        bleLocationPermissionModal.mCancel = null;
        bleLocationPermissionModal.mEnableBleLocation = null;
    }
}
