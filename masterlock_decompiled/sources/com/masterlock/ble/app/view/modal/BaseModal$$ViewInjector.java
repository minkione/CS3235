package com.masterlock.ble.app.view.modal;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class BaseModal$$ViewInjector {
    public static void inject(Finder finder, BaseModal baseModal, Object obj) {
        baseModal.mTitle = (TextView) finder.findRequiredView(obj, C1075R.C1077id.title, "field 'mTitle'");
        baseModal.mBody = (TextView) finder.findRequiredView(obj, C1075R.C1077id.body, "field 'mBody'");
        baseModal.mBodyContainer = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.body_container, "field 'mBodyContainer'");
        baseModal.positiveButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.positive_button, "field 'positiveButton'");
        baseModal.negativeButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.negative_button, "field 'negativeButton'");
    }

    public static void reset(BaseModal baseModal) {
        baseModal.mTitle = null;
        baseModal.mBody = null;
        baseModal.mBodyContainer = null;
        baseModal.positiveButton = null;
        baseModal.negativeButton = null;
    }
}
