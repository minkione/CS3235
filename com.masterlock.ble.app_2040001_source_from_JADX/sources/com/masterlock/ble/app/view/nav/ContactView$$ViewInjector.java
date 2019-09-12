package com.masterlock.ble.app.view.nav;

import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class ContactView$$ViewInjector {
    public static void inject(Finder finder, ContactView contactView, Object obj) {
        contactView.mContactMasterlockWebView = (MasterLockWebView) finder.findRequiredView(obj, C1075R.C1077id.contact_masterlock_webview, "field 'mContactMasterlockWebView'");
    }

    public static void reset(ContactView contactView) {
        contactView.mContactMasterlockWebView = null;
    }
}
