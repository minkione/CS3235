package com.masterlock.ble.app.view.modal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.akta.android.p004ui.floatinglabeledittext.FloatingLabelEditText;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.util.FloatingLabelEditTextValidator;
import com.masterlock.ble.app.util.ViewUtil;
import java.util.List;
import p007fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class ForgotPasscodeDialog extends SimpleDialog {
    @InjectView(2131296649)
    FloatingLabelEditText forgotPasscodeEditText;
    @InjectView(2131296587)
    SmoothProgressBar loadingProgressBar;

    public ForgotPasscodeDialog(Context context) {
        this(context, null);
    }

    public ForgotPasscodeDialog(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: 0000 */
    public void init() {
        inflate(getContext(), C1075R.layout.forgot_passcode_dialog, this);
        ButterKnife.inject((View) this);
        this.forgotPasscodeEditText.getEditText().addTextChangedListener(ViewUtil.createHideErrorTextWatcher(this.forgotPasscodeEditText));
        this.forgotPasscodeEditText.getEditText().setOnEditorActionListener(new OnEditorActionListener() {
            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return ForgotPasscodeDialog.lambda$init$0(ForgotPasscodeDialog.this, textView, i, keyEvent);
            }
        });
    }

    public static /* synthetic */ boolean lambda$init$0(ForgotPasscodeDialog forgotPasscodeDialog, TextView textView, int i, KeyEvent keyEvent) {
        if (keyEvent != null || i == 6) {
            forgotPasscodeDialog.positiveButton.callOnClick();
        }
        return false;
    }

    public void showError(String str) {
        Toast.makeText(getContext(), str, 0).show();
    }

    public void toastSuccess() {
        Toast.makeText(getContext(), getResources().getString(C1075R.string.forgot_password_email_sent_description), 1).show();
    }

    public boolean isValid() {
        List<FloatingLabelEditText> isEmpty = FloatingLabelEditTextValidator.isEmpty(this.forgotPasscodeEditText);
        if (isEmpty.isEmpty()) {
            return true;
        }
        for (FloatingLabelEditText showError : isEmpty) {
            showError.showError(getResources().getString(C1075R.string.empty_email_validation));
        }
        return false;
    }

    public void showLoading(boolean z) {
        if (z) {
            this.loadingProgressBar.setVisibility(0);
            this.loadingProgressBar.progressiveStart();
            return;
        }
        this.loadingProgressBar.progressiveStop();
    }

    public String getEmailAddress() {
        return this.forgotPasscodeEditText.getText();
    }
}
