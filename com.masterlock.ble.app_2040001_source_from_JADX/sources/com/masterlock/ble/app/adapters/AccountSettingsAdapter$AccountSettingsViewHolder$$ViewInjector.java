package com.masterlock.ble.app.adapters;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.adapters.AccountSettingsAdapter.AccountSettingsViewHolder;

public class AccountSettingsAdapter$AccountSettingsViewHolder$$ViewInjector {
    public static void inject(Finder finder, final AccountSettingsViewHolder accountSettingsViewHolder, Object obj) {
        accountSettingsViewHolder.mTextView = (TextView) finder.findRequiredView(obj, C1075R.C1077id.account_settings_title, "field 'mTextView'");
        finder.findRequiredView(obj, C1075R.C1077id.account_settings_item, "method 'onClick'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                accountSettingsViewHolder.onClick(view);
            }
        });
    }

    public static void reset(AccountSettingsViewHolder accountSettingsViewHolder) {
        accountSettingsViewHolder.mTextView = null;
    }
}
