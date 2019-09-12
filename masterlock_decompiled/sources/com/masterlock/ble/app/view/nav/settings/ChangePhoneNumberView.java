package com.masterlock.ble.app.view.nav.settings;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.google.common.base.Strings;
import com.google.i18n.phonenumbers.AsYouTypeFormatter;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.nav.settings.ChangePhoneNumberPresenter;
import com.masterlock.ble.app.util.TextUtils;
import com.masterlock.ble.app.util.TextWatcherContinueBtn;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.core.AccountProfileInfo;
import java.util.Locale;

public class ChangePhoneNumberView extends LinearLayout implements IAuthenticatedView {
    /* access modifiers changed from: private */
    public String formattedNumber;
    private AsYouTypeFormatter formatter;
    @InjectView(2131296376)
    TextView mChangeCountryCode;
    @InjectView(2131296393)
    Button mContinueBtn;
    @InjectView(2131296395)
    TextView mCountryCode;
    @InjectView(2131296579)
    RelativeLayout mCurrentPhoneContainer;
    @InjectView(2131296397)
    TextView mCurrentPhoneNumber;
    @InjectView(2131296654)
    EditText mPhoneNumber;
    private ChangePhoneNumberPresenter mPresenter;
    @InjectView(2131296691)
    TextView mRemovePhoneNumber;
    private PhoneNumberUtil phoneNumberUtil;

    public ChangePhoneNumberView(Context context) {
        super(context);
    }

    public ChangePhoneNumberView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.phoneNumberUtil = PhoneNumberUtil.getInstance();
            this.mPresenter = new ChangePhoneNumberPresenter(this);
            this.mPresenter.start();
            EditText editText = this.mPhoneNumber;
            editText.addTextChangedListener(new TextWatcherContinueBtn(this.mContinueBtn, editText, true));
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mPresenter.finish();
    }

    public void phoneNumberEditTextSetup(String str) {
        this.formatter = this.phoneNumberUtil.getAsYouTypeFormatter(str);
        this.mPhoneNumber.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (!ChangePhoneNumberView.this.mPhoneNumber.getText().toString().equals(ChangePhoneNumberView.this.formattedNumber)) {
                    ChangePhoneNumberView.this.formatPhone();
                }
            }

            public void afterTextChanged(Editable editable) {
                if (!ChangePhoneNumberView.this.mPhoneNumber.getText().toString().equals(ChangePhoneNumberView.this.formattedNumber)) {
                    if (ChangePhoneNumberView.this.mPhoneNumber.getText().toString().equals("")) {
                        ChangePhoneNumberView.this.formattedNumber = "";
                    }
                    ChangePhoneNumberView.this.mPhoneNumber.setText(ChangePhoneNumberView.this.formattedNumber);
                }
                ChangePhoneNumberView.this.mPhoneNumber.setSelection(ChangePhoneNumberView.this.formattedNumber.length());
            }
        });
    }

    public void setCurrentPhoneNumber(AccountProfileInfo accountProfileInfo) {
        if (accountProfileInfo.isPhoneVerified()) {
            this.mCurrentPhoneNumber.setText(formatMobilePhoneInformation(accountProfileInfo));
            this.mChangeCountryCode.setEnabled(false);
            return;
        }
        this.mCurrentPhoneContainer.setVisibility(8);
        this.mRemovePhoneNumber.setVisibility(8);
        if (!Strings.isNullOrEmpty(accountProfileInfo.getPhoneNumber())) {
            this.mPhoneNumber.setText(TextUtils.getNationalPhone(accountProfileInfo.getPhoneNumber()));
        }
    }

    /* access modifiers changed from: private */
    public void formatPhone() {
        this.formatter.clear();
        String replace = this.mPhoneNumber.getText().toString().replace(" ", "").replace("-", "").replace("(", "").replace(")", "");
        for (int i = 0; i < replace.length(); i++) {
            this.formattedNumber = this.formatter.inputDigit(replace.charAt(i));
        }
    }

    public void displayCountryCode(String str, String str2) {
        if (str.contains("+")) {
            str = str.replace("+", "");
        }
        TextView textView = this.mCountryCode;
        StringBuilder sb = new StringBuilder();
        sb.append("+");
        sb.append(str);
        textView.setText(sb.toString());
        this.formatter = this.phoneNumberUtil.getAsYouTypeFormatter(str2);
        formatPhone();
        this.mPhoneNumber.setText(this.formattedNumber);
    }

    public void displayMessage(String str) {
        Toast.makeText(getContext(), str, 0).show();
    }

    public void displayMessage(int i) {
        Toast.makeText(getContext(), getContext().getString(i), 0).show();
    }

    public static String formatMobilePhoneInformation(AccountProfileInfo accountProfileInfo) {
        String str;
        String nationalPhone = TextUtils.getNationalPhone(accountProfileInfo.getPhoneNumber());
        String displayCountry = new Locale("", accountProfileInfo.getAlphaCountryCode()).getDisplayCountry();
        StringBuilder sb = new StringBuilder();
        sb.append(nationalPhone);
        if (Strings.isNullOrEmpty(displayCountry)) {
            str = "";
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("\n+");
            sb2.append(accountProfileInfo.getCountryCode());
            sb2.append(" ");
            sb2.append(displayCountry);
            str = sb2.toString();
        }
        sb.append(str);
        return sb.toString();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296393})
    public void onContinueClicked() {
        this.mPresenter.updatePhoneNumber(this.mPhoneNumber.getText().toString());
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296376})
    public void openModal() {
        this.mPresenter.showCountriesDialog();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296691})
    public void onRemovePhoneNumberClicked() {
        this.mPresenter.showRemovePhoneModal();
    }

    public void enableRemovePhoneNumberButton(boolean z) {
        View findById = ButterKnife.findById((View) this, (int) C1075R.C1077id.remove_phone_number);
        if (findById != null) {
            findById.setEnabled(z);
        }
    }
}
