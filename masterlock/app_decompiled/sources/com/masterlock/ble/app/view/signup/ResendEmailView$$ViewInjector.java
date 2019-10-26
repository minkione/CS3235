package com.masterlock.ble.app.view.signup;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class ResendEmailView$$ViewInjector {
    public static void inject(Finder finder, final ResendEmailView resendEmailView, Object obj) {
        resendEmailView.emailSentTo = (TextView) finder.findRequiredView(obj, C1075R.C1077id.email_sent_to, "field 'emailSentTo'");
        resendEmailView.emailVerificationCode = (EditText) finder.findRequiredView(obj, C1075R.C1077id.email_verification_code, "field 'emailVerificationCode'");
        finder.findRequiredView(obj, C1075R.C1077id.resend_email_button, "method 'resendEmail'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                resendEmailView.resendEmail();
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.verify_email_code, "method 'verifyEmailCode'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                resendEmailView.verifyEmailCode();
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.sign_in_button, "method 'signIn'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                resendEmailView.signIn();
            }
        });
    }

    public static void reset(ResendEmailView resendEmailView) {
        resendEmailView.emailSentTo = null;
        resendEmailView.emailVerificationCode = null;
    }
}
