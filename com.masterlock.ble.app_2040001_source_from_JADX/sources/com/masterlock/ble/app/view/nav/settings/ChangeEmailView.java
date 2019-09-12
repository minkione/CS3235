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
import com.masterlock.ble.app.presenter.nav.settings.ChangeEmailPresenter;
import com.masterlock.ble.app.util.TextWatcherContinueBtn;
import com.masterlock.ble.app.util.ViewUtil;
import com.masterlock.ble.app.view.IAuthenticatedView;

public class ChangeEmailView extends LinearLayout implements IAuthenticatedView {
    private ChangeEmailPresenter mChangeEmailPresenter;
    @InjectView(2131296377)
    Button mContinueBtn;
    @InjectView(2131296396)
    TextView mCurrentEmail;
    @InjectView(2131296450)
    EditText mEmailInput;

    public ChangeEmailView(Context context) {
        super(context);
    }

    public ChangeEmailView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ChangeEmailView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mChangeEmailPresenter = new ChangeEmailPresenter(this);
            this.mChangeEmailPresenter.start();
            EditText editText = this.mEmailInput;
            editText.addTextChangedListener(new TextWatcherContinueBtn(this.mContinueBtn, editText, true));
        }
    }

    public void setCurrentEmail(String str) {
        this.mCurrentEmail.setText(str);
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296377})
    public void sendEmail() {
        ViewUtil.hideKeyboard(getContext(), this.mEmailInput.getWindowToken());
        this.mChangeEmailPresenter.validationEmail(this.mEmailInput.getText().toString());
    }

    public void displayMessage(String str) {
        Toast.makeText(getContext(), str, 0).show();
    }

    public void displayMessage(int i) {
        Toast.makeText(getContext(), i, 0).show();
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }
}
