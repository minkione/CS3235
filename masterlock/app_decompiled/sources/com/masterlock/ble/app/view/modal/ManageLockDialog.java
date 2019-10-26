package com.masterlock.ble.app.view.modal;

import android.content.Context;
import android.support.p003v7.widget.CardView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.akta.android.p004ui.floatinglabeledittext.FloatingLabelEditText;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.util.ViewUtil;
import p007fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class ManageLockDialog extends CardView {
    @InjectView(2131296495)
    Button forgotPasscodeButton;
    @InjectView(2131296587)
    SmoothProgressBar loadingProgressBar;
    @InjectView(2131296631)
    Button negativeButton;
    @InjectView(2131296648)
    FloatingLabelEditText passcodeFloatingLabelEditText;
    @InjectView(2131296655)
    Button positiveButton;

    public ManageLockDialog(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        inflate(getContext(), C1075R.layout.password_verify_dialog, this);
        ButterKnife.inject((View) this);
        this.passcodeFloatingLabelEditText.getEditText().addTextChangedListener(ViewUtil.createHideErrorTextWatcher(this.passcodeFloatingLabelEditText));
        this.passcodeFloatingLabelEditText.getEditText().setOnEditorActionListener(new OnEditorActionListener() {
            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return ManageLockDialog.lambda$init$0(ManageLockDialog.this, textView, i, keyEvent);
            }
        });
    }

    public static /* synthetic */ boolean lambda$init$0(ManageLockDialog manageLockDialog, TextView textView, int i, KeyEvent keyEvent) {
        if (keyEvent != null || i == 6) {
            manageLockDialog.positiveButton.callOnClick();
        }
        return false;
    }

    public String getPasscode() {
        return this.passcodeFloatingLabelEditText.getText();
    }

    public void setPositiveButtonClickListener(OnClickListener onClickListener) {
        this.positiveButton.setOnClickListener(onClickListener);
    }

    public void setNegativeButtonClickListener(OnClickListener onClickListener) {
        this.negativeButton.setOnClickListener(onClickListener);
    }

    public void setForgotPasscodeButtonClickListener(OnClickListener onClickListener) {
        this.forgotPasscodeButton.setOnClickListener(onClickListener);
    }

    public void showPasscodeError() {
        this.passcodeFloatingLabelEditText.getEditText().setText("");
        this.passcodeFloatingLabelEditText.showError(getResources().getString(C1075R.string.invalid_password_error), -1, -1);
    }

    public void showLoading(boolean z) {
        if (z) {
            this.loadingProgressBar.setVisibility(0);
            this.loadingProgressBar.progressiveStart();
            return;
        }
        this.loadingProgressBar.progressiveStop();
    }
}
