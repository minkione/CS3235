package com.masterlock.ble.app.view.signup;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class ResendSmsView$$ViewInjector {
    public static void inject(Finder finder, final ResendSmsView resendSmsView, Object obj) {
        resendSmsView.smsSentTo = (TextView) finder.findRequiredView(obj, C1075R.C1077id.sms_sent_to, "field 'smsSentTo'");
        resendSmsView.smsVerificationCode = (TextView) finder.findRequiredView(obj, C1075R.C1077id.sms_verification_code, "field 'smsVerificationCode'");
        finder.findRequiredView(obj, C1075R.C1077id.skip_sms_verification, "method 'skipVerification'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                resendSmsView.skipVerification();
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.sign_in_button, "method 'signIn'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                resendSmsView.signIn();
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.resend_sms_button, "method 'resendEmail'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                resendSmsView.resendEmail();
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.continue_button, "method 'verifySmsCode'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                resendSmsView.verifySmsCode();
            }
        });
    }

    public static void reset(ResendSmsView resendSmsView) {
        resendSmsView.smsSentTo = null;
        resendSmsView.smsVerificationCode = null;
    }
}
