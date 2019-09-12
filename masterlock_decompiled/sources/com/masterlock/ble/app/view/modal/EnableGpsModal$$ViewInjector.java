package com.masterlock.ble.app.view.modal;

import android.widget.Button;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class EnableGpsModal$$ViewInjector {
    public static void inject(Finder finder, EnableGpsModal enableGpsModal, Object obj) {
        enableGpsModal.mCancel = (Button) finder.findRequiredView(obj, C1075R.C1077id.negative_button, "field 'mCancel'");
        enableGpsModal.mEnableGps = (Button) finder.findRequiredView(obj, C1075R.C1077id.positive_button, "field 'mEnableGps'");
    }

    public static void reset(EnableGpsModal enableGpsModal) {
        enableGpsModal.mCancel = null;
        enableGpsModal.mEnableGps = null;
    }
}
