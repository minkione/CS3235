package com.masterlock.ble.app.view.modal;

import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class PasswordTipsModal$$ViewInjector {
    public static void inject(Finder finder, PasswordTipsModal passwordTipsModal, Object obj) {
        passwordTipsModal.passwordTipsContainer = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.ll_password_tips_container, "field 'passwordTipsContainer'");
        passwordTipsModal.positiveButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.positive_button, "field 'positiveButton'");
    }

    public static void reset(PasswordTipsModal passwordTipsModal) {
        passwordTipsModal.passwordTipsContainer = null;
        passwordTipsModal.positiveButton = null;
    }
}
