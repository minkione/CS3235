package com.masterlock.ble.app.view.nav.settings;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class VerifyNewEmailView$$ViewInjector {
    public static void inject(Finder finder, final VerifyNewEmailView verifyNewEmailView, Object obj) {
        verifyNewEmailView.mNewEmail = (TextView) finder.findRequiredView(obj, C1075R.C1077id.new_email_address, "field 'mNewEmail'");
        verifyNewEmailView.mVerificationCodeInput = (EditText) finder.findRequiredView(obj, C1075R.C1077id.email_verification_code, "field 'mVerificationCodeInput'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.new_email_continue_btn, "field 'mContinueBtn' and method 'sendEmail'");
        verifyNewEmailView.mContinueBtn = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                verifyNewEmailView.sendEmail();
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.new_email_resend_btn, "method 'resendEmail'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                verifyNewEmailView.resendEmail();
            }
        });
    }

    public static void reset(VerifyNewEmailView verifyNewEmailView) {
        verifyNewEmailView.mNewEmail = null;
        verifyNewEmailView.mVerificationCodeInput = null;
        verifyNewEmailView.mContinueBtn = null;
    }
}
