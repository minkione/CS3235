package com.masterlock.ble.app.view.signin;

import android.widget.Button;
import android.widget.EditText;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class ForgotUsernameView$$ViewInjector {
    public static void inject(Finder finder, ForgotUsernameView forgotUsernameView, Object obj) {
        forgotUsernameView.emailEditText = (EditText) finder.findRequiredView(obj, C1075R.C1077id.email_edit_text, "field 'emailEditText'");
        forgotUsernameView.retrieveUsernameButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.retrieve_username_button, "field 'retrieveUsernameButton'");
    }

    public static void reset(ForgotUsernameView forgotUsernameView) {
        forgotUsernameView.emailEditText = null;
        forgotUsernameView.retrieveUsernameButton = null;
    }
}
