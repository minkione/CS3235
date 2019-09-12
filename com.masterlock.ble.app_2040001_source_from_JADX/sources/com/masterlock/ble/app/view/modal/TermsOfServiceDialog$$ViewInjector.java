package com.masterlock.ble.app.view.modal;

import android.webkit.WebView;
import android.widget.Button;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class TermsOfServiceDialog$$ViewInjector {
    public static void inject(Finder finder, TermsOfServiceDialog termsOfServiceDialog, Object obj) {
        termsOfServiceDialog.mTOSWebView = (WebView) finder.findRequiredView(obj, C1075R.C1077id.tos_webview, "field 'mTOSWebView'");
        termsOfServiceDialog.negativeButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.negative_button, "field 'negativeButton'");
        termsOfServiceDialog.positiveButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.positive_button, "field 'positiveButton'");
    }

    public static void reset(TermsOfServiceDialog termsOfServiceDialog) {
        termsOfServiceDialog.mTOSWebView = null;
        termsOfServiceDialog.negativeButton = null;
        termsOfServiceDialog.positiveButton = null;
    }
}
