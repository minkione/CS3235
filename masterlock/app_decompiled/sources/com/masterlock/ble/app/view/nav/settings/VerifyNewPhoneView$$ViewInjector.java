package com.masterlock.ble.app.view.nav.settings;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class VerifyNewPhoneView$$ViewInjector {
    public static void inject(Finder finder, final VerifyNewPhoneView verifyNewPhoneView, Object obj) {
        verifyNewPhoneView.tVNewPhone = (TextView) finder.findRequiredView(obj, C1075R.C1077id.tv_new_phone, "field 'tVNewPhone'");
        verifyNewPhoneView.eTVerificationCode = (EditText) finder.findRequiredView(obj, C1075R.C1077id.et_verification_code, "field 'eTVerificationCode'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.bt_continue, "field 'bTContinue' and method 'sendEmail'");
        verifyNewPhoneView.bTContinue = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                verifyNewPhoneView.sendEmail();
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.bt_resend_sms, "method 'resendEmail'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                verifyNewPhoneView.resendEmail();
            }
        });
    }

    public static void reset(VerifyNewPhoneView verifyNewPhoneView) {
        verifyNewPhoneView.tVNewPhone = null;
        verifyNewPhoneView.eTVerificationCode = null;
        verifyNewPhoneView.bTContinue = null;
    }
}
