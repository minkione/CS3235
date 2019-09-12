package com.masterlock.ble.app.util;

import android.content.Context;
import android.widget.Toast;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.core.Lock;

public class LockRestoreUtil {
    private static LockRestoreUtil instance;

    public static LockRestoreUtil getInstance() {
        if (instance == null) {
            instance = new LockRestoreUtil();
        }
        return instance;
    }

    public void setLockWithPendingRestore(Lock lock) {
        FileUtil.getInstance().saveRestoreConfigFile(lock.getKmsId(), lock.getLockMode(), lock.getRelockTimeInSeconds(), lock.getPrimaryCode());
    }

    public boolean isLockPendingRestore(Lock lock) {
        return FileUtil.getInstance().existsRestoreConfigFile(lock.getKmsId());
    }

    public void removeLockPendingRestore(Lock lock) {
        if (FileUtil.getInstance().existsRestoreConfigFile(lock.getKmsId())) {
            Context applicationContext = MasterLockApp.get().getApplicationContext();
            Toast.makeText(applicationContext, applicationContext.getResources().getString(C1075R.string.firmware_update_restoring_title), 0).show();
            FileUtil.getInstance().deleteRestoreConfigFile(lock.getKmsId());
        }
    }
}
