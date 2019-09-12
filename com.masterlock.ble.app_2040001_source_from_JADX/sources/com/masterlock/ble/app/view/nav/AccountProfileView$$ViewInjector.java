package com.masterlock.ble.app.view.nav;

import android.support.p003v7.widget.RecyclerView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class AccountProfileView$$ViewInjector {
    public static void inject(Finder finder, AccountProfileView accountProfileView, Object obj) {
        accountProfileView.mAccountProfileSettingsList = (RecyclerView) finder.findRequiredView(obj, C1075R.C1077id.account_profile_settings_list, "field 'mAccountProfileSettingsList'");
    }

    public static void reset(AccountProfileView accountProfileView) {
        accountProfileView.mAccountProfileSettingsList = null;
    }
}
