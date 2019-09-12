package com.masterlock.ble.app.view.signup;

import android.content.Context;
import android.support.p000v4.content.ContextCompat;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.presenter.signup.AccountDetailsPresenter;
import com.masterlock.ble.app.util.FloatingLabelEditTextValidator;
import com.masterlock.ble.app.util.SignUpHelper.PasswordStrength;
import com.masterlock.ble.app.util.ViewUtil;
import java.util.List;

public class AccountDetailsView extends LinearLayout {
    /* access modifiers changed from: private */
    public final String TAG;
    @InjectView(2131296393)
    Button continueButton;
    @InjectView(2131296478)
    EditText firstNameEditText;
    private boolean isPasswordInRange;
    @InjectView(2131296563)
    EditText lastNameEditText;
    private OnClickListener onClickContinueButton;
    @InjectView(2131296650)
    EditText passwordEditText;
    @InjectView(2131296711)
    View passwordStrengthContainer;
    @InjectView(2131296722)
    CheckBox passwordSwitch;
    AccountDetailsPresenter presenter;
    @InjectView(2131296730)
    ScrollView scrollView;
    @InjectView(2131296877)
    TextView tVPasswordStrength;
    @InjectView(2131296906)
    EditText userNameEditText;

    public AccountDetailsView(Context context) {
        this(context, null);
    }

    public AccountDetailsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.TAG = getClass().getSimpleName();
        this.onClickContinueButton = new OnClickListener() {
            public void onClick(View view) {
                ViewUtil.hideKeyboard(AccountDetailsView.this.getContext(), AccountDetailsView.this.passwordEditText.getWindowToken());
                List isEmpty = FloatingLabelEditTextValidator.isEmpty(AccountDetailsView.this.userNameEditText, AccountDetailsView.this.firstNameEditText, AccountDetailsView.this.passwordEditText);
                Log.d(AccountDetailsView.this.TAG, "onClick: Continue button ");
                if (isEmpty.isEmpty() && AccountDetailsView.this.presenter.getSignUpHelper().isPasswordInRange(AccountDetailsView.this.passwordEditText.getText().length())) {
                    AccountDetailsView.this.presenter.createAccount(AccountDetailsView.this.userNameEditText.getText().toString(), AccountDetailsView.this.firstNameEditText.getText().toString(), AccountDetailsView.this.lastNameEditText.getText().toString(), AccountDetailsView.this.passwordEditText.getText().toString());
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            MasterLockApp.get().inject(this);
            ButterKnife.inject((View) this);
            this.presenter = new AccountDetailsPresenter(this);
            this.presenter.start();
            AccountDetailsPresenter accountDetailsPresenter = this.presenter;
            EditText editText = this.passwordEditText;
            EditText editText2 = this.firstNameEditText;
            accountDetailsPresenter.setEditTextTextWatchers(editText, this.userNameEditText, editText2, editText2);
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
    @OnCheckedChanged({2131296722})
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
        this.firstNameEditText.setOnClickListener($$Lambda$AccountDetailsView$grKgKY07RdkP8Nu4afpAPMvBQyE.INSTANCE);
        this.passwordEditText.setOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                AccountDetailsView.lambda$viewSetup$1(AccountDetailsView.this, view);
            }
        });
        this.passwordEditText.setOnEditorActionListener(new OnEditorActionListener() {
            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return AccountDetailsView.lambda$viewSetup$2(AccountDetailsView.this, textView, i, keyEvent);
            }
        });
        toggleContinueButton();
    }

    public static /* synthetic */ void lambda$viewSetup$1(AccountDetailsView accountDetailsView, View view) {
        Log.d(accountDetailsView.TAG, "onClick: EditText");
        accountDetailsView.scrollToPasswordStrengthContainer();
    }

    public static /* synthetic */ boolean lambda$viewSetup$2(AccountDetailsView accountDetailsView, TextView textView, int i, KeyEvent keyEvent) {
        if (i != 255) {
            return false;
        }
        ViewUtil.hideKeyboard(accountDetailsView.getContext(), accountDetailsView.passwordEditText.getWindowToken());
        return true;
    }

    public void scrollToPasswordStrengthContainer() {
        this.passwordStrengthContainer.post(new Runnable() {
            public final void run() {
                AccountDetailsView.lambda$scrollToPasswordStrengthContainer$3(AccountDetailsView.this);
            }
        });
    }

    public static /* synthetic */ void lambda$scrollToPasswordStrengthContainer$3(AccountDetailsView accountDetailsView) {
        String str = accountDetailsView.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("scrollToPasswordStrengthContainer: bottom = ");
        sb.append(accountDetailsView.passwordStrengthContainer.getBottom());
        Log.d(str, sb.toString());
        if (accountDetailsView.passwordStrengthContainer.getBottom() > 0) {
            accountDetailsView.scrollView.smoothScrollTo(0, accountDetailsView.passwordStrengthContainer.getBottom());
        }
    }

    public void toggleContinueButton() {
        boolean z;
        if (this.isPasswordInRange && FloatingLabelEditTextValidator.isEmptyTrimming(this.firstNameEditText, this.userNameEditText).isEmpty()) {
            this.continueButton.setTextColor(-1);
            this.continueButton.setOnClickListener(this.onClickContinueButton);
            return;
        }
        this.continueButton.setTextColor(ContextCompat.getColor(getContext(), C1075R.color.half_white));
        this.continueButton.setOnClickListener(null);
    }

    public void changePasswordEditTextTextTransformation(boolean z) {
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("changePasswordEditTextTextTransformation: ");
        sb.append(z);
        Log.d(str, sb.toString());
        this.presenter.removePasswordTextWatcher(this.passwordEditText);
        this.passwordEditText.setTransformationMethod(!z ? new PasswordTransformationMethod() : null);
        this.presenter.setEditTextTextWatchers(this.passwordEditText, new EditText[0]);
    }
}
