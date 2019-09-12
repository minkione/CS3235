package com.masterlock.ble.app.view.nav.settings;

import android.content.Context;
import android.support.p000v4.content.ContextCompat;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.nav.settings.ChangePasswordPresenter;
import com.masterlock.ble.app.util.FloatingLabelEditTextValidator;
import com.masterlock.ble.app.util.SignUpHelper.PasswordStrength;
import com.masterlock.ble.app.util.ViewUtil;
import com.masterlock.ble.app.view.IAuthenticatedView;
import java.util.List;

public class ChangePasswordView extends LinearLayout implements IAuthenticatedView {
    private final String TAG;
    @InjectView(2131296329)
    Button bTSave;
    @InjectView(2131296462)
    EditText eTCurrentPassword;
    @InjectView(2131296461)
    EditText etConfirmPassword;
    @InjectView(2131296463)
    EditText etNewPassword;
    private boolean isPasswordInRange;
    @InjectView(2131296711)
    View passwordStrengthContainer;
    ChangePasswordPresenter presenter;
    @InjectView(2131296730)
    ScrollView scrollView;
    @InjectView(2131296877)
    TextView tVPasswordStrength;

    public ChangePasswordView(Context context) {
        this(context, null);
    }

    public ChangePasswordView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.TAG = getClass().getSimpleName();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.presenter = new ChangePasswordPresenter(this);
            this.presenter.start();
            this.presenter.setEditTextTextWatchers(this.etNewPassword, this.etConfirmPassword, this.eTCurrentPassword);
            viewSetup();
        }
    }

    public void setPasswordInRange(boolean z) {
        this.isPasswordInRange = z;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.presenter.finish();
    }

    public void displayError(ApiError apiError) {
        Toast.makeText(getContext(), apiError.getMessage(), 1).show();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296550})
    public void onClick() {
        this.presenter.showPasswordTipsModal();
    }

    /* access modifiers changed from: 0000 */
    @OnCheckedChanged({2131296367})
    public void onCheckedChange(boolean z) {
        changePasswordEditTextTextTransformation(z);
    }

    public void updatePasswordStrengthLabel(PasswordStrength passwordStrength) {
        int i;
        int i2;
        int i3;
        if (passwordStrength != null) {
            i3 = passwordStrength.getMessageResId();
            i = passwordStrength.getColorResId();
            i2 = 0;
        } else {
            i = C1075R.color.black;
            i3 = C1075R.string.empty_string;
            i2 = 8;
        }
        TextView textView = this.tVPasswordStrength;
        StringBuilder sb = new StringBuilder();
        sb.append(getContext().getString(C1075R.string.password_strength_title));
        sb.append(" ");
        sb.append(getContext().getString(i3));
        textView.setText(sb.toString());
        this.tVPasswordStrength.setTextColor(ContextCompat.getColor(getContext(), i));
        this.passwordStrengthContainer.setVisibility(i2);
        if (i2 == 0) {
            scrollToPasswordStrengthContainer();
        }
    }

    private void viewSetup() {
        this.eTCurrentPassword.setOnClickListener($$Lambda$ChangePasswordView$Eh8ETcjLSndVrrLqXh26DUNnJXE.INSTANCE);
        this.etNewPassword.setOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                ChangePasswordView.lambda$viewSetup$1(ChangePasswordView.this, view);
            }
        });
        this.etNewPassword.setOnEditorActionListener(new OnEditorActionListener() {
            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return ChangePasswordView.lambda$viewSetup$2(ChangePasswordView.this, textView, i, keyEvent);
            }
        });
        toggleContinueButton();
    }

    public static /* synthetic */ void lambda$viewSetup$1(ChangePasswordView changePasswordView, View view) {
        Log.d(changePasswordView.TAG, "onClick: EditText");
        changePasswordView.scrollToPasswordStrengthContainer();
    }

    public static /* synthetic */ boolean lambda$viewSetup$2(ChangePasswordView changePasswordView, TextView textView, int i, KeyEvent keyEvent) {
        if (i != 255) {
            return false;
        }
        ViewUtil.hideKeyboard(changePasswordView.getContext(), changePasswordView.etNewPassword.getWindowToken());
        return true;
    }

    public void scrollToPasswordStrengthContainer() {
        this.passwordStrengthContainer.post(new Runnable() {
            public final void run() {
                ChangePasswordView.lambda$scrollToPasswordStrengthContainer$3(ChangePasswordView.this);
            }
        });
    }

    public static /* synthetic */ void lambda$scrollToPasswordStrengthContainer$3(ChangePasswordView changePasswordView) {
        String str = changePasswordView.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("scrollToPasswordStrengthContainer: bottom = ");
        sb.append(changePasswordView.passwordStrengthContainer.getBottom());
        Log.d(str, sb.toString());
        if (changePasswordView.passwordStrengthContainer.getBottom() > 0) {
            changePasswordView.scrollView.smoothScrollTo(0, changePasswordView.passwordStrengthContainer.getBottom());
        }
    }

    public void toggleContinueButton() {
        boolean isEmpty = this.isPasswordInRange & FloatingLabelEditTextValidator.isEmptyTrimming(this.eTCurrentPassword, this.etConfirmPassword).isEmpty();
        if (isEmpty) {
            this.bTSave.setTextColor(-1);
        } else {
            this.bTSave.setTextColor(ContextCompat.getColor(getContext(), C1075R.color.half_white));
        }
        this.bTSave.setEnabled(isEmpty);
    }

    @OnClick({2131296329})
    public void onContinueClick(View view) {
        ViewUtil.hideKeyboard(getContext(), this.etNewPassword.getWindowToken());
        List isEmpty = FloatingLabelEditTextValidator.isEmpty(this.etConfirmPassword, this.eTCurrentPassword, this.etNewPassword);
        Log.d(this.TAG, "onClick: Continue button ");
        if (isEmpty.isEmpty() && this.presenter.getSignUpHelper().isPasswordInRange(this.etNewPassword.getText().length())) {
            this.presenter.changePassword(this.eTCurrentPassword.getText().toString(), this.etNewPassword.getText().toString(), this.etConfirmPassword.getText().toString());
        }
    }

    public void changePasswordEditTextTextTransformation(boolean z) {
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("changePasswordEditTextTextTransformation: ");
        sb.append(z);
        Log.d(str, sb.toString());
        this.presenter.removePasswordTextWatcher(this.etNewPassword);
        this.etNewPassword.setTransformationMethod(!z ? new PasswordTransformationMethod() : null);
        this.presenter.setEditTextTextWatchers(this.etNewPassword, new EditText[0]);
    }

    public void displayMessage(String str) {
        Toast.makeText(getContext(), str, 0).show();
    }

    public void displayMessage(int i) {
        Toast.makeText(getContext(), getContext().getString(i), 0).show();
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }
}
