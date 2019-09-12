package com.masterlock.ble.app.view.nav;

import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class HelpView$$ViewInjector {
    public static void inject(Finder finder, HelpView helpView, Object obj) {
        helpView.mHelpWebView = (MasterLockWebView) finder.findRequiredView(obj, C1075R.C1077id.help_webview, "field 'mHelpWebView'");
    }

    public static void reset(HelpView helpView) {
        helpView.mHelpWebView = null;
    }
}
