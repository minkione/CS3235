package com.masterlock.ble.app.adapters;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.adapters.AccountProfileAdapter.AccountProfileViewHolder;

public class AccountProfileAdapter$AccountProfileViewHolder$$ViewInjector {
    public static void inject(Finder finder, final AccountProfileViewHolder accountProfileViewHolder, Object obj) {
        accountProfileViewHolder.mTitleText = (TextView) finder.findRequiredView(obj, C1075R.C1077id.account_profile_title, "field 'mTitleText'");
        accountProfileViewHolder.mValueText = (TextView) finder.findRequiredView(obj, C1075R.C1077id.account_profile_value, "field 'mValueText'");
        accountProfileViewHolder.mPhoneCountryInfo = (TextView) finder.findRequiredView(obj, C1075R.C1077id.account_profile_phone_country_info, "field 'mPhoneCountryInfo'");
        finder.findRequiredView(obj, C1075R.C1077id.account_profile_item, "method 'onClick'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                accountProfileViewHolder.onClick(view);
            }
        });
    }

    public static void reset(AccountProfileViewHolder accountProfileViewHolder) {
        accountProfileViewHolder.mTitleText = null;
        accountProfileViewHolder.mValueText = null;
        accountProfileViewHolder.mPhoneCountryInfo = null;
    }
}
