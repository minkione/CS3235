package com.masterlock.ble.app.view.nav.settings;

import android.content.Context;
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
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.nav.settings.VerifyNewEmailPresenter;
import com.masterlock.ble.app.util.TextWatcherContinueBtn;
import com.masterlock.ble.app.util.ViewUtil;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.square.flow.appflow.AppFlow;
import com.square.flow.screenswitcher.HandlesBack;

public class VerifyNewEmailView extends LinearLayout implements HandlesBack, IAuthenticatedView {
    @InjectView(2131296634)
    Button mContinueBtn;
    @InjectView(2131296633)
    TextView mNewEmail;
    @InjectView(2131296452)
    EditText mVerificationCodeInput;
    private VerifyNewEmailPresenter mVerifyNewEmailPresenter;

    public VerifyNewEmailView(Context context) {
        super(context);
    }

    public VerifyNewEmailView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public VerifyNewEmailView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mVerifyNewEmailPresenter = new VerifyNewEmailPresenter(this);
            this.mVerifyNewEmailPresenter.start();
            EditText editText = this.mVerificationCodeInput;
            editText.addTextChangedListener(new TextWatcherContinueBtn(this.mContinueBtn, editText));
        }
    }

    public void setCurrentEmail(String str) {
        this.mNewEmail.setText(str);
    }

    public void displayMessage(String str) {
        Toast.makeText(getContext(), str, 0).show();
    }

    public void displayMessage(int i) {
        Toast.makeText(getContext(), i, 0).show();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296634})
    public void sendEmail() {
        ViewUtil.hideKeyboard(getContext(), this.mVerificationCodeInput.getWindowToken());
        this.mVerifyNewEmailPresenter.verifyEmailCode(this.mVerificationCodeInput.getText().toString());
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296635})
    public void resendEmail() {
        this.mVerifyNewEmailPresenter.resendEmail();
    }

    public boolean onBackPressed() {
        this.mVerifyNewEmailPresenter.clearNewEmail();
        AppFlow.get(getContext()).goBack();
        return true;
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }
}
