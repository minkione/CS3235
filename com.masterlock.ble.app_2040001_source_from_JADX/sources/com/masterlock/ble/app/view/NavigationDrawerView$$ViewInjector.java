package com.masterlock.ble.app.view;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class NavigationDrawerView$$ViewInjector {
    public static void inject(Finder finder, final NavigationDrawerView navigationDrawerView, Object obj) {
        navigationDrawerView.mAppVersion = (TextView) finder.findRequiredView(obj, C1075R.C1077id.app_version, "field 'mAppVersion'");
        finder.findRequiredView(obj, C1075R.C1077id.txt_settings, "method 'goToSettings'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                navigationDrawerView.goToSettings();
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.txt_log_out, "method 'confirmLogOut'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                navigationDrawerView.confirmLogOut();
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.txt_help, "method 'goToHelp'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                navigationDrawerView.goToHelp();
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.txt_terms_of_service, "method 'goToTermsOfService'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                navigationDrawerView.goToTermsOfService();
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.txt_privacy_policy, "method 'goToPrivacyPolicy'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                navigationDrawerView.goToPrivacyPolicy();
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.txt_contact_master_lock, "method 'goToContactMasterlock'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                navigationDrawerView.goToContactMasterlock();
            }
        });
    }

    public static void reset(NavigationDrawerView navigationDrawerView) {
        navigationDrawerView.mAppVersion = null;
    }
}
