package com.masterlock.ble.app.view.nav.settings;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class ChangeEmailView$$ViewInjector {
    public static void inject(Finder finder, final ChangeEmailView changeEmailView, Object obj) {
        changeEmailView.mCurrentEmail = (TextView) finder.findRequiredView(obj, C1075R.C1077id.current_email_address, "field 'mCurrentEmail'");
        changeEmailView.mEmailInput = (EditText) finder.findRequiredView(obj, C1075R.C1077id.email_edit_text, "field 'mEmailInput'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.change_email_continue_btn, "field 'mContinueBtn' and method 'sendEmail'");
        changeEmailView.mContinueBtn = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                changeEmailView.sendEmail();
            }
        });
    }

    public static void reset(ChangeEmailView changeEmailView) {
        changeEmailView.mCurrentEmail = null;
        changeEmailView.mEmailInput = null;
        changeEmailView.mContinueBtn = null;
    }
}
