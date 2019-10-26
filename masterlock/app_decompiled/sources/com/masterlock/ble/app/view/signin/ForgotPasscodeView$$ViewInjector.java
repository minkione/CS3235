package com.masterlock.ble.app.view.signin;

import android.widget.Button;
import android.widget.EditText;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class ForgotPasscodeView$$ViewInjector {
    public static void inject(Finder finder, ForgotPasscodeView forgotPasscodeView, Object obj) {
        forgotPasscodeView.emailEditText = (EditText) finder.findRequiredView(obj, C1075R.C1077id.email_edit_text, "field 'emailEditText'");
        forgotPasscodeView.resetPasswordButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.reset_password_button, "field 'resetPasswordButton'");
    }

    public static void reset(ForgotPasscodeView forgotPasscodeView) {
        forgotPasscodeView.emailEditText = null;
        forgotPasscodeView.resetPasswordButton = null;
    }
}
