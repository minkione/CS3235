package com.masterlock.ble.app.view.signup;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class TermsOfServiceView$$ViewInjector {
    public static void inject(Finder finder, final TermsOfServiceView termsOfServiceView, Object obj) {
        termsOfServiceView.termsOfServiceTextView = (TextView) finder.findRequiredView(obj, C1075R.C1077id.terms_of_service_text_view, "field 'termsOfServiceTextView'");
        termsOfServiceView.buttonBar = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.button_bar, "field 'buttonBar'");
        finder.findRequiredView(obj, C1075R.C1077id.terms_of_service_dismiss_button, "method 'dismissTOS'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                termsOfServiceView.dismissTOS();
            }
        });
    }

    public static void reset(TermsOfServiceView termsOfServiceView) {
        termsOfServiceView.termsOfServiceTextView = null;
        termsOfServiceView.buttonBar = null;
    }
}
