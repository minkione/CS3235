package com.masterlock.ble.app.view.nav;

import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class ProfileView$$ViewInjector {
    public static void inject(Finder finder, ProfileView profileView, Object obj) {
        profileView.mProfileWebView = (MasterLockWebView) finder.findRequiredView(obj, C1075R.C1077id.profile_webview, "field 'mProfileWebView'");
    }

    public static void reset(ProfileView profileView) {
        profileView.mProfileWebView = null;
    }
}
