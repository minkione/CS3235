package com.masterlock.ble.app.view.lock;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class LocksFirmwareUpdateSteps$$ViewInjector {
    public static void inject(Finder finder, final LocksFirmwareUpdateSteps locksFirmwareUpdateSteps, Object obj) {
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.locks_firmware_update_steps_button, "field 'mContinue' and method 'continueWithFirmwareUpdate'");
        locksFirmwareUpdateSteps.mContinue = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                locksFirmwareUpdateSteps.continueWithFirmwareUpdate();
            }
        });
    }

    public static void reset(LocksFirmwareUpdateSteps locksFirmwareUpdateSteps) {
        locksFirmwareUpdateSteps.mContinue = null;
    }
}
