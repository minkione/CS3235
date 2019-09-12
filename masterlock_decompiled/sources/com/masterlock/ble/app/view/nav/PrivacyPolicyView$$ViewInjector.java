package com.masterlock.ble.app.view.nav;

import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class PrivacyPolicyView$$ViewInjector {
    public static void inject(Finder finder, PrivacyPolicyView privacyPolicyView, Object obj) {
        privacyPolicyView.mPrivacyPolicyWebView = (MasterLockWebView) finder.findRequiredView(obj, C1075R.C1077id.privacy_policy_webview, "field 'mPrivacyPolicyWebView'");
    }

    public static void reset(PrivacyPolicyView privacyPolicyView) {
        privacyPolicyView.mPrivacyPolicyWebView = null;
    }
}
