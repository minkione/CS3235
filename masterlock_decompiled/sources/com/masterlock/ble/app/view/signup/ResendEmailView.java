package com.masterlock.ble.app.view.signup;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.signup.ResendEmailPresenter;

public class ResendEmailView extends LinearLayout {
    @InjectView(2131296451)
    TextView emailSentTo;
    @InjectView(2131296452)
    EditText emailVerificationCode;
    private ResendEmailPresenter mPresenter;

    public ResendEmailView(Context context) {
        this(context, null);
    }

    public ResendEmailView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mPresenter = new ResendEmailPresenter(this);
            this.mPresenter.start();
            this.emailSentTo.setText(this.mPresenter.getUserEmail());
            this.emailVerificationCode.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void afterTextChanged(Editable editable) {
                    ResendEmailView.this.enableSave(editable.length() > 0);
                }
            });
            enableSave(false);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mPresenter.finish();
    }

    public void displayError(String str) {
        Toast.makeText(getContext(), str, 1).show();
    }

    public void displayError(ApiError apiError) {
        Toast.makeText(getContext(), apiError.getMessage(), 1).show();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296693})
    public void resendEmail() {
        this.mPresenter.resendEmail();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296907})
    public void verifyEmailCode() {
        this.mPresenter.confirmAccountCreationPhoneVerificationCode(this.emailVerificationCode.getText().toString());
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296757})
    public void signIn() {
        this.mPresenter.signIn();
    }

    /* access modifiers changed from: private */
    public void enableSave(boolean z) {
        Resources resources;
        int i;
        Button button = (Button) ButterKnife.findById((View) this, (int) C1075R.C1077id.verify_email_code);
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
