package com.masterlock.ble.app.view.nav;

import android.support.p003v7.widget.RecyclerView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class AccountSettingsView$$ViewInjector {
    public static void inject(Finder finder, AccountSettingsView accountSettingsView, Object obj) {
        accountSettingsView.mAccountSettingsList = (RecyclerView) finder.findRequiredView(obj, C1075R.C1077id.account_settings_list, "field 'mAccountSettingsList'");
    }

    public static void reset(AccountSettingsView accountSettingsView) {
        accountSettingsView.mAccountSettingsList = null;
    }
}
