package com.masterlock.ble.app.view.signin;

import android.content.Context;
import android.support.p000v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.login.ForgotPasscodePresenter;
import com.masterlock.ble.app.util.FloatingLabelEditTextValidator;
import com.masterlock.ble.app.util.ViewUtil;
import java.util.List;

public class ForgotPasscodeView extends LinearLayout {
    @InjectView(2131296450)
    EditText emailEditText;
    /* access modifiers changed from: private */
    public ForgotPasscodePresenter forgotPasscodePresenter;
    @InjectView(2131296695)
    Button resetPasswordButton;
    private OnClickListener resetPasswordButtonOnClickListener;

    public ForgotPasscodeView(Context context) {
        this(context, null);
    }

    public ForgotPasscodeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.resetPasswordButtonOnClickListener = new OnClickListener() {
            public void onClick(View view) {
                ViewUtil.hideKeyboard(ForgotPasscodeView.this.getContext(), ForgotPasscodeView.this.emailEditText.getWindowToken());
                List<EditText> isEmpty = FloatingLabelEditTextValidator.isEmpty(ForgotPasscodeView.this.emailEditText);
                if (isEmpty.isEmpty()) {
                    ForgotPasscodeView.this.forgotPasscodePresenter.forgotPasscode(ForgotPasscodeView.this.emailEditText.getText().toString());
                    return;
                }
                for (EditText editText : isEmpty) {
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.forgotPasscodePresenter = new ForgotPasscodePresenter(this);
            this.forgotPasscodePresenter.start();
            setEditTextTextWatchers();
            toggleResetPasswordButton();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.forgotPasscodePresenter.finish();
    }

    public void displayError(ApiError apiError) {
        Toast.makeText(getContext(), apiError.getMessage(), 1).show();
    }

    public void toast() {
        Toast.makeText(getContext(), getResources().getString(C1075R.string.forgot_password_email_sent_description), 1).show();
    }

    /* access modifiers changed from: private */
    public void toggleResetPasswordButton() {
        if (FloatingLabelEditTextValidator.isEmpty(this.emailEditText).isEmpty()) {
            this.resetPasswordButton.setTextColor(-1);
            this.resetPasswordButton.setOnClickListener(this.resetPasswordButtonOnClickListener);
            return;
        }
        this.resetPasswordButton.setTextColor(ContextCompat.getColor(getContext(), C1075R.color.half_white));
        this.resetPasswordButton.setOnClickListener(null);
    }

    private void setEditTextTextWatchers() {
        this.emailEditText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                ForgotPasscodeView.this.toggleResetPasswordButton();
            }
        });
    }
}
