package com.masterlock.ble.app.view.signin;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class SignInView$$ViewInjector {
    public static void inject(Finder finder, final SignInView signInView, Object obj) {
        signInView.userNameEditText = (EditText) finder.findRequiredView(obj, C1075R.C1077id.username_edit_text, "field 'userNameEditText'");
        signInView.passwordEditText = (EditText) finder.findRequiredView(obj, C1075R.C1077id.password_edit_text, "field 'passwordEditText'");
        signInView.forgotUsernamePasscodeTextView = (TextView) finder.findRequiredView(obj, C1075R.C1077id.forgot, "field 'forgotUsernamePasscodeTextView'");
        signInView.termsOfServiceTextView = (TextView) finder.findRequiredView(obj, C1075R.C1077id.tos, "field 'termsOfServiceTextView'");
        signInView.mPrivacyPolicy = (TextView) finder.findRequiredView(obj, C1075R.C1077id.privacy_policy, "field 'mPrivacyPolicy'");
        signInView.signInButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.sign_in_button, "field 'signInButton'");
        finder.findRequiredView(obj, C1075R.C1077id.sign_up_button, "method 'signUp'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                signInView.signUp();
            }
        });
    }

    public static void reset(SignInView signInView) {
        signInView.userNameEditText = null;
        signInView.passwordEditText = null;
        signInView.forgotUsernamePasscodeTextView = null;
        signInView.termsOfServiceTextView = null;
        signInView.mPrivacyPolicy = null;
        signInView.signInButton = null;
    }
}
