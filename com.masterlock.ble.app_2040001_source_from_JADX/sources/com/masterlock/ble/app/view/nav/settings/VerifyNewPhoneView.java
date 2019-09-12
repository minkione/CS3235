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
import com.masterlock.ble.app.presenter.nav.settings.VerifyNewPhonePresenter;
import com.masterlock.ble.app.util.TextWatcherContinueBtn;
import com.masterlock.ble.app.util.ViewUtil;
import com.masterlock.ble.app.view.IAuthenticatedView;

public class VerifyNewPhoneView extends LinearLayout implements IAuthenticatedView {
    @InjectView(2131296326)
    Button bTContinue;
    @InjectView(2131296464)
    EditText eTVerificationCode;
    private VerifyNewPhonePresenter mVerifyNewPhonePresenter;
    @InjectView(2131296828)
    TextView tVNewPhone;

    public void showPasscodeExpiredToast() {
    }

    public VerifyNewPhoneView(Context context) {
        super(context);
    }

    public VerifyNewPhoneView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public VerifyNewPhoneView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mVerifyNewPhonePresenter = new VerifyNewPhonePresenter(this);
            this.mVerifyNewPhonePresenter.start();
            EditText editText = this.eTVerificationCode;
            editText.addTextChangedListener(new TextWatcherContinueBtn(this.bTContinue, editText));
        }
    }

    public void setNewPhone(String str) {
        this.tVNewPhone.setText(str);
    }

    public void displayMessage(String str) {
        Toast.makeText(getContext(), str, 0).show();
    }

    public void displayMessage(int i) {
        Toast.makeText(getContext(), i, 0).show();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296326})
    public void sendEmail() {
        ViewUtil.hideKeyboard(getContext(), this.eTVerificationCode.getWindowToken());
        this.mVerifyNewPhonePresenter.verifyMobilePhoneNumber(this.eTVerificationCode.getText().toString());
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296328})
    public void resendEmail() {
        this.mVerifyNewPhonePresenter.returnToPreviousScreen();
    }
}
