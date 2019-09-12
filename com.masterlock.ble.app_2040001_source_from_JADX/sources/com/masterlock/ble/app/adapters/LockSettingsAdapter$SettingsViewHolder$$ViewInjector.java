package com.masterlock.ble.app.adapters;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.adapters.LockSettingsAdapter.SettingsViewHolder;

public class LockSettingsAdapter$SettingsViewHolder$$ViewInjector {
    public static void inject(Finder finder, final SettingsViewHolder settingsViewHolder, Object obj) {
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.txt_title, "field 'mTextView' and method 'onClick'");
        settingsViewHolder.mTextView = (TextView) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                settingsViewHolder.onClick(view);
            }
        });
    }

    public static void reset(SettingsViewHolder settingsViewHolder) {
        settingsViewHolder.mTextView = null;
    }
}
