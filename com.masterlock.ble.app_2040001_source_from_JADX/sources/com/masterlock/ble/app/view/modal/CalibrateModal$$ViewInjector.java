package com.masterlock.ble.app.view.modal;

import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class CalibrateModal$$ViewInjector {
    public static void inject(Finder finder, CalibrateModal calibrateModal, Object obj) {
        calibrateModal.mCalibrateLock = (Button) finder.findRequiredView(obj, C1075R.C1077id.bt_calibrate_lock, "field 'mCalibrateLock'");
        calibrateModal.mSkipForNow = (TextView) finder.findRequiredView(obj, C1075R.C1077id.tv_skip_for_now, "field 'mSkipForNow'");
    }

    public static void reset(CalibrateModal calibrateModal) {
        calibrateModal.mCalibrateLock = null;
        calibrateModal.mSkipForNow = null;
    }
}
