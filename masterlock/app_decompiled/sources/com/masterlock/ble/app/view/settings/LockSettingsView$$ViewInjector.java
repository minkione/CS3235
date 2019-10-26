package com.masterlock.ble.app.view.settings;

import android.support.p003v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class LockSettingsView$$ViewInjector {
    public static void inject(Finder finder, final LockSettingsView lockSettingsView, Object obj) {
        lockSettingsView.mLockName = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_lock_name, "field 'mLockName'");
        lockSettingsView.textDeviceId = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id_banner, "field 'textDeviceId'");
        lockSettingsView.mRecycler = (RecyclerView) finder.findRequiredView(obj, C1075R.C1077id.recycler_view, "field 'mRecycler'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.btn_delete, "field 'mDelete' and method 'deleteLock'");
        lockSettingsView.mDelete = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                lockSettingsView.deleteLock();
            }
        });
    }

    public static void reset(LockSettingsView lockSettingsView) {
        lockSettingsView.mLockName = null;
        lockSettingsView.textDeviceId = null;
        lockSettingsView.mRecycler = null;
        lockSettingsView.mDelete = null;
    }
}
