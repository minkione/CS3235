package com.masterlock.ble.app.view.signin;

import android.content.Context;
import android.os.Parcelable;
import android.support.p000v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.login.SignInPresenter;
import com.masterlock.ble.app.util.FloatingLabelEditTextValidator;
import com.masterlock.ble.app.util.ViewUtil;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nonnull;

public class SignInView extends LinearLayout {
    @InjectView(2131296494)
    TextView forgotUsernamePasscodeTextView;
    @InjectView(2131296660)
    TextView mPrivacyPolicy;
    @InjectView(2131296650)
    EditText passwordEditText;
    @InjectView(2131296757)
    Button signInButton;
    OnClickListener signInOnClickListener;
    /* access modifiers changed from: private */
    public SignInPresenter signInPresenter;
    @InjectView(2131296815)
    TextView termsOfServiceTextView;
    @InjectView(2131296906)
    EditText userNameEditText;

    public SignInView(Context context) {
        this(context, null);
    }

    public SignInView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.signInOnClickListener = new OnClickListener() {
            public void onClick(View view) {
                ViewUtil.hideKeyboard(SignInView.this.getContext(), SignInView.this.passwordEditText.getWindowToken());
                List<EditText> isEmpty = FloatingLabelEditTextValidator.isEmpty(SignInView.this.userNameEditText, SignInView.this.passwordEditText);
                if (isEmpty.isEmpty()) {
                    SignInView.this.signInPresenter.signIn(SignInView.this.userNameEditText.getText().toString(), SignInView.this.passwordEditText.getText().toString());
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
            this.signInPresenter = new SignInPresenter(this);
            this.signInPresenter.start();
            setupForgotPasswordClickableSpan();
            setupTOSClickableSpan();
            setupPrivacyPolicyClickableSpan();
            setEditTextTextWatchers();
            toggleSignInButton();
        }
    }

    public void restoreHierarchyState(@Nonnull SparseArray<Parcelable> sparseArray) {
        super.restoreHierarchyState(sparseArray);
        setupForgotPasswordClickableSpan();
        setupTOSClickableSpan();
        setupPrivacyPolicyClickableSpan();
    }

    private void setupForgotPasswordClickableSpan() {
        if (this.forgotUsernamePasscodeTextView != null) {
            String string = getResources().getString(C1075R.string.username);
            String string2 = getResources().getString(C1075R.string.password);
            String string3 = getResources().getString(C1075R.string.forgot_username_password, new Object[]{string, string2});
            SpannableString spannableString = new SpannableString(string3);
            ClickableSpan createForgotUsernameClickableSpan = createForgotUsernameClickableSpan();
            int indexOf = string3.toLowerCase(Locale.getDefault()).indexOf(string.toLowerCase(Locale.getDefault()));
            int length = string.length() + indexOf;
            if (indexOf > -1) {
                spannableString.setSpan(createForgotUsernameClickableSpan, indexOf, length, 33);
            }
            ClickableSpan createForgotPasscodeClickableSpan = createForgotPasscodeClickableSpan();
            int indexOf2 = string3.toLowerCase(Locale.getDefault()).indexOf(string2.toLowerCase(Locale.getDefault()));
            int length2 = string2.length() + indexOf2;
            if (indexOf2 > -1) {
                spannableString.setSpan(createForgotPasscodeClickableSpan, indexOf2, length2, 33);
            }
            this.forgotUsernamePasscodeTextView.setText(spannableString, BufferType.SPANNABLE);
            this.forgotUsernamePasscodeTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private ClickableSpan createForgotUsernameClickableSpan() {
        return new ClickableSpan() {
            public void onClick(View view) {
                SignInView.this.signInPresenter.forgotUsername();
            }

            public void updateDrawState(TextPaint textPaint) {
                super.updateDrawState(textPaint);
                textPaint.setUnderlineText(false);
                textPaint.setColor(-1);
            }
        };
    }

    private ClickableSpan createForgotPasscodeClickableSpan() {
        return new ClickableSpan() {
            public void onClick(View view) {
                SignInView.this.signInPresenter.forgotPasscode();
            }

            public void updateDrawState(TextPaint textPaint) {
                super.updateDrawState(textPaint);
                textPaint.setUnderlineText(false);
                textPaint.setColor(-1);
            }
        };
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296759})
    public void signUp() {
        this.signInPresenter.signUp();
    }

    public void displayError(ApiError apiError) {
        Toast.makeText(getContext(), apiError.getMessage(), 1).show();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.signInPresenter.finish();
    }

    private void setupTOSClickableSpan() {
        if (this.termsOfServiceTextView != null) {
            String string = getResources().getString(C1075R.string.terms_of_service);
            int i = 0;
            String string2 = getResources().getString(C1075R.string.terms_of_service_agreement, new Object[]{string});
            SpannableString spannableString = new SpannableString(string2);
            C16344 r3 = new ClickableSpan() {
                public void onClick(View view) {
                    SignInView.this.signInPresenter.showTermsOfService();
                }

                public void updateDrawState(TextPaint textPaint) {
                    super.updateDrawState(textPaint);
                    textPaint.setUnderlineText(false);
                    textPaint.setColor(-1);
                }
            };
            int indexOf = string2.toLowerCase(Locale.getDefault()).indexOf(string.toLowerCase(Locale.getDefault()));
            int length = string.length() + indexOf;
            if (indexOf < 0) {
                length = string2.length();
            } else {
                i = indexOf;
            }
            spannableString.setSpan(r3, i, length, 33);
            this.termsOfServiceTextView.setText(spannableString, BufferType.SPANNABLE);
            this.termsOfServiceTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private void setupPrivacyPolicyClickableSpan() {
        if (this.mPrivacyPolicy != null) {
            String string = getResources().getString(C1075R.string.privacy_policy);
            SpannableString spannableString = new SpannableString(string);
            spannableString.setSpan(new ClickableSpan() {
                public void onClick(View view) {
                    SignInView.this.signInPresenter.showPrivacyPolicy();
                }

                public void updateDrawState(TextPaint textPaint) {
                    super.updateDrawState(textPaint);
                    textPaint.setUnderlineText(false);
                    textPaint.setColor(-1);
                }
            }, 0, string.length(), 33);
            this.mPrivacyPolicy.setText(spannableString, BufferType.SPANNABLE);
            this.mPrivacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    public void toggleSignInButton() {
        if (FloatingLabelEditTextValidator.isEmpty(this.userNameEditText, this.passwordEditText).isEmpty()) {
            this.signInButton.setTextColor(-1);
            this.signInButton.setOnClickListener(this.signInOnClickListener);
            return;
        }
        this.signInButton.setTextColor(ContextCompat.getColor(getContext(), C1075R.color.half_white));
        this.signInButton.setOnClickListener(null);
    }

    private void setEditTextTextWatchers() {
        C16366 r0 = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                SignInView.this.toggleSignInButton();
            }
        };
        this.passwordEditText.addTextChangedListener(r0);
        this.userNameEditText.addTextChangedListener(r0);
    }
}
