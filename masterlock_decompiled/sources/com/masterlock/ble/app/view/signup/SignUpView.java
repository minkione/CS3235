package com.masterlock.ble.app.view.signup;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcelable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.google.i18n.phonenumbers.AsYouTypeFormatter;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.signup.SignUpPresenter;
import com.masterlock.ble.app.util.TextUtils;
import com.masterlock.ble.app.util.ViewUtil;
import com.masterlock.core.Country;
import java.util.Locale;
import javax.annotation.Nonnull;

public class SignUpView extends LinearLayout {
    @InjectView(2131296395)
    TextView countryCode;
    @InjectView(2131296449)
    EditText emailAddressInput;
    /* access modifiers changed from: private */
    public String formatedNumber;
    private AsYouTypeFormatter formatter;
    @InjectView(2131296660)
    TextView mPrivacyPolicy;
    @InjectView(2131296654)
    EditText phoneNumberInput;
    private PhoneNumberUtil phoneNumberUtil;
    /* access modifiers changed from: private */
    public SignUpPresenter signUpPresenter;
    @InjectView(2131296815)
    TextView termsOfServiceTextView;

    public SignUpView(Context context) {
        this(context, null);
    }

    public SignUpView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.formatedNumber = "";
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.signUpPresenter = new SignUpPresenter(this);
            this.signUpPresenter.start();
            setupTOSClickableSpan();
            setupPrivacyPolicyClickableSpan();
            this.phoneNumberUtil = PhoneNumberUtil.getInstance();
            this.formatter = this.phoneNumberUtil.getAsYouTypeFormatter(TextUtils.DEFAULT_COUNTRY_ISO);
            this.phoneNumberInput.addTextChangedListener(new TextWatcher() {
                CharSequence number;

                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    if (!SignUpView.this.phoneNumberInput.getText().toString().equals(SignUpView.this.formatedNumber)) {
                        SignUpView.this.formatPhone();
                    }
                }

                public void afterTextChanged(Editable editable) {
                    if (!SignUpView.this.phoneNumberInput.getText().toString().equals(SignUpView.this.formatedNumber)) {
                        if (SignUpView.this.phoneNumberInput.getText().toString().equals("")) {
                            SignUpView.this.formatedNumber = "";
                        }
                        SignUpView.this.phoneNumberInput.setText(SignUpView.this.formatedNumber);
                    }
                    SignUpView.this.phoneNumberInput.setSelection(SignUpView.this.formatedNumber.length());
                }
            });
            this.emailAddressInput.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void afterTextChanged(Editable editable) {
                    SignUpView.this.enableContinue(editable.length() > 0);
                }
            });
            this.emailAddressInput.setOnEditorActionListener(new OnEditorActionListener() {
                public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    return SignUpView.lambda$onAttachedToWindow$0(SignUpView.this, textView, i, keyEvent);
                }
            });
        }
    }

    public static /* synthetic */ boolean lambda$onAttachedToWindow$0(SignUpView signUpView, TextView textView, int i, KeyEvent keyEvent) {
        if (i != 5) {
            return false;
        }
        signUpView.phoneNumberInput.requestFocus();
        return true;
    }

    public void formatPhone() {
        this.formatter.clear();
        String replace = this.phoneNumberInput.getText().toString().replace(" ", "").replace("-", "").replace("(", "").replace(")", "");
        for (int i = 0; i < replace.length(); i++) {
            this.formatedNumber = this.formatter.inputDigit(replace.charAt(i));
        }
    }

    public void restoreHierarchyState(@Nonnull SparseArray<Parcelable> sparseArray) {
        super.restoreHierarchyState(sparseArray);
        onAttachedToWindow();
    }

    private void setupTOSClickableSpan() {
        if (this.termsOfServiceTextView != null) {
            String string = getResources().getString(C1075R.string.terms_of_service);
            int i = 0;
            String string2 = getResources().getString(C1075R.string.terms_of_service_agreement, new Object[]{string});
            SpannableString spannableString = new SpannableString(string2);
            C16523 r3 = new ClickableSpan() {
                public void onClick(View view) {
                    SignUpView.this.signUpPresenter.showTermsOfService();
                }

                public void updateDrawState(TextPaint textPaint) {
                    super.updateDrawState(textPaint);
                    textPaint.setUnderlineText(false);
                    textPaint.setColor(SignUpView.this.getResources().getColor(C1075R.color.text_color_primary_inverse));
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
                    SignUpView.this.signUpPresenter.showPrivacyPolicy();
                }

                public void updateDrawState(TextPaint textPaint) {
                    super.updateDrawState(textPaint);
                    textPaint.setUnderlineText(false);
                    textPaint.setColor(SignUpView.this.getResources().getColor(C1075R.color.text_color_primary_inverse));
                }
            }, 0, string.length(), 33);
            this.mPrivacyPolicy.setText(spannableString, BufferType.SPANNABLE);
            this.mPrivacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.signUpPresenter.finish();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296376})
    public void openModal() {
        this.signUpPresenter.showCountriesDialog();
    }

    public void setCountryCode(Country country) {
        this.countryCode.setText(country.callingCode[0]);
        this.formatter = this.phoneNumberUtil.getAsYouTypeFormatter(country.cca2);
        formatPhone();
        this.phoneNumberInput.setText(this.formatedNumber);
    }

    public void setLastInfo(String str, String str2, String str3) {
        this.emailAddressInput.setText(str);
        this.countryCode.setText(str2);
        this.phoneNumberInput.setText(str3);
        if (str2.equals("")) {
            this.countryCode.setText("+1");
        }
    }

    public void setInvitationEmailAndDisableField(String str) {
        this.emailAddressInput.setText(str);
        this.emailAddressInput.setEnabled(false);
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296393})
    public void sendEmail() {
        ViewUtil.hideKeyboard(getContext(), this.emailAddressInput.getWindowToken());
        if (android.text.TextUtils.isEmpty(this.emailAddressInput.getText().toString())) {
            Toast.makeText(getContext(), C1075R.string.empty_email_validation, 1).show();
        } else {
            this.signUpPresenter.sendEmail(this.emailAddressInput.getText().toString(), this.countryCode.getText().toString(), this.phoneNumberInput.getText().toString().replace(" ", "").replace("-", "").replace("(", "").replace(")", ""));
        }
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296757})
    public void navigateToSignIn() {
        this.signUpPresenter.signIn();
    }

    public void displayError(ApiError apiError) {
        Toast.makeText(getContext(), apiError.getMessage(), 1).show();
    }

    /* access modifiers changed from: private */
    public void enableContinue(boolean z) {
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

    public void enableNavigationButton(boolean z) {
        ButterKnife.findById((View) this, (int) C1075R.C1077id.continue_button).setEnabled(z);
        ButterKnife.findById((View) this, (int) C1075R.C1077id.sign_in_button).setEnabled(z);
    }
}
