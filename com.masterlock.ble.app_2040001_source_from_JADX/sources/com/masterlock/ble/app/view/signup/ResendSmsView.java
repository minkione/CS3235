package com.masterlock.ble.app.view.signup;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.signup.ResendSmsPresenter;

public class ResendSmsView extends LinearLayout {
    private ResendSmsPresenter mPresenter;
    @InjectView(2131296764)
    TextView smsSentTo;
    @InjectView(2131296765)
    TextView smsVerificationCode;

    public ResendSmsView(Context context) {
        this(context, null);
    }

    public ResendSmsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mPresenter = new ResendSmsPresenter(this);
            this.mPresenter.start();
            this.smsVerificationCode.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void afterTextChanged(Editable editable) {
                    ResendSmsView.this.enableSave(editable.length() > 0);
                }
            });
            this.smsSentTo.setText(this.mPresenter.getPhoneNumber());
            if (this.mPresenter.getSkipCount() >= 3) {
                redrawToSkipValidation();
            }
            enableSave(false);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mPresenter.finish();
    }

    public void redrawToSkipValidation() {
        ((Button) ButterKnife.findById((View) this, (int) C1075R.C1077id.resend_sms_button)).setText(getResources().getString(C1075R.string.sms_skip_verification));
        ButterKnife.findById((View) this, (int) C1075R.C1077id.skip_sms_verification).setVisibility(8);
    }

    public void displayError(ApiError apiError) {
        Toast.makeText(getContext(), apiError.getMessage(), 1).show();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296761})
    public void skipVerification() {
        this.mPresenter.skipSmsVerification();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296757})
    public void signIn() {
        this.mPresenter.signIn();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296694})
    public void resendEmail() {
        if (this.mPresenter.getSkipCount() < 3) {
            this.mPresenter.resendSms();
        } else {
            this.mPresenter.skipSmsVerification();
        }
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296393})
    public void verifySmsCode() {
        this.mPresenter.confirmAccountCreationPhoneVerificationCode(this.smsVerificationCode.getText().toString());
    }

    /* access modifiers changed from: private */
    public void enableSave(boolean z) {
        Resources resources;
        int i;
        Button button = (Button) ButterKnife.findById((View) this, (int) C1075R.C1077id.continue_button);
        button.setEnabled(z);
        if (z) {
            resources = getResources();
            i = C1075R.color.text_color_primary_inverse;
        } else {
            resources = getResources();
            i = C1075R.color.continue_button_disabled;
        }
        button.setTextColor(resources.getColor(i));
    }
}
