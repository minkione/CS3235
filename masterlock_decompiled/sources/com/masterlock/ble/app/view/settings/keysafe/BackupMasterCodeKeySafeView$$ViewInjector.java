package com.masterlock.ble.app.view.settings.keysafe;

import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class BackupMasterCodeKeySafeView$$ViewInjector {
    public static void inject(Finder finder, BackupMasterCodeKeySafeView backupMasterCodeKeySafeView, Object obj) {
        backupMasterCodeKeySafeView.mLockNameBanner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_name_banner, "field 'mLockNameBanner'");
        backupMasterCodeKeySafeView.textDeviceId = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id_banner, "field 'textDeviceId'");
        backupMasterCodeKeySafeView.backupMasterCodeContainer = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.backup_code_container, "field 'backupMasterCodeContainer'");
        backupMasterCodeKeySafeView.mBackupCodeInstructions = (TextView) finder.findRequiredView(obj, C1075R.C1077id.backup_code_instructions, "field 'mBackupCodeInstructions'");
    }

    public static void reset(BackupMasterCodeKeySafeView backupMasterCodeKeySafeView) {
        backupMasterCodeKeySafeView.mLockNameBanner = null;
        backupMasterCodeKeySafeView.textDeviceId = null;
        backupMasterCodeKeySafeView.backupMasterCodeContainer = null;
        backupMasterCodeKeySafeView.mBackupCodeInstructions = null;
    }
}
