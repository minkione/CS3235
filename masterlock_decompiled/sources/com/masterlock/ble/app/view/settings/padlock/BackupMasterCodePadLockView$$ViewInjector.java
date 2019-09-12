package com.masterlock.ble.app.view.settings.padlock;

import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class BackupMasterCodePadLockView$$ViewInjector {
    public static void inject(Finder finder, BackupMasterCodePadLockView backupMasterCodePadLockView, Object obj) {
        backupMasterCodePadLockView.mLockNameBanner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_name_banner, "field 'mLockNameBanner'");
        backupMasterCodePadLockView.textDeviceId = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id_banner, "field 'textDeviceId'");
    }

    public static void reset(BackupMasterCodePadLockView backupMasterCodePadLockView) {
        backupMasterCodePadLockView.mLockNameBanner = null;
        backupMasterCodePadLockView.textDeviceId = null;
    }
}
