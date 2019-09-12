package com.masterlock.ble.app.view.signup;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class SignUpView$$ViewInjector {
    public static void inject(Finder finder, final SignUpView signUpView, Object obj) {
        signUpView.emailAddressInput = (EditText) finder.findRequiredView(obj, C1075R.C1077id.email_address, "field 'emailAddressInput'");
        signUpView.countryCode = (TextView) finder.findRequiredView(obj, C1075R.C1077id.country_code, "field 'countryCode'");
        signUpView.phoneNumberInput = (EditText) finder.findRequiredView(obj, C1075R.C1077id.phone_number, "field 'phoneNumberInput'");
        signUpView.termsOfServiceTextView = (TextView) finder.findRequiredView(obj, C1075R.C1077id.tos, "field 'termsOfServiceTextView'");
        signUpView.mPrivacyPolicy = (TextView) finder.findRequiredView(obj, C1075R.C1077id.privacy_policy, "field 'mPrivacyPolicy'");
        finder.findRequiredView(obj, C1075R.C1077id.change_country_code, "method 'openModal'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                signUpView.openModal();
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.continue_button, "method 'sendEmail'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                signUpView.sendEmail();
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.sign_in_button, "method 'navigateToSignIn'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                signUpView.navigateToSignIn();
            }
        });
    }

    public static void reset(SignUpView signUpView) {
        signUpView.emailAddressInput = null;
        signUpView.countryCode = null;
        signUpView.phoneNumberInput = null;
        signUpView.termsOfServiceTextView = null;
        signUpView.mPrivacyPolicy = null;
    }
}
