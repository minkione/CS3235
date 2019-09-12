package com.masterlock.ble.app.view.guest;

import android.content.Context;
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
import com.akta.android.p004ui.floatinglabeledittext.FloatingLabelEditText;
import com.google.common.base.Strings;
import com.google.i18n.phonenumbers.AsYouTypeFormatter;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.guest.EditGuestDetailsPresenter;
import com.masterlock.ble.app.util.TextUtils;
import com.masterlock.ble.app.util.ViewUtil;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.core.Country;
import com.masterlock.core.Guest;
import com.square.flow.appflow.AppFlow;
import com.square.flow.screenswitcher.HandlesBack;
import com.square.flow.screenswitcher.HandlesUp;

public class EditGuestDetailsView extends LinearLayout implements IAuthenticatedView, HandlesBack, HandlesUp {
    public static final String EMPTY_STRING = "";
    @InjectView(2131296385)
    FloatingLabelEditText countryCodeInput;
    /* access modifiers changed from: private */
    public String formattedNumber;
    private AsYouTypeFormatter formatter;
    @InjectView(2131296448)
    FloatingLabelEditText guestEmail;
    @InjectView(2131296502)
    FloatingLabelEditText guestFirstName;
    @InjectView(2131296505)
    FloatingLabelEditText guestLastName;
    @InjectView(2131296643)
    FloatingLabelEditText guestOrganization;
    @InjectView(2131296332)
    Button mButtonAddGuest;
    @InjectView(2131296422)
    TextView mDeviceIdBanner;
    EditGuestDetailsPresenter mEditGuestDetailsPresenter;
    @InjectView(2131296859)
    TextView mInstructions;
    @InjectView(2131296597)
    TextView mLockNameBanner;
    @InjectView(2131296653)
    FloatingLabelEditText phoneNumberInput;
    private PhoneNumberUtil phoneNumberUtil;
    private Country selectedCountry;

    public EditGuestDetailsView(Context context) {
        this(context, null);
    }

    public EditGuestDetailsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.formattedNumber = "";
    }

    private void init() {
        this.guestEmail.getEditText().addTextChangedListener(ViewUtil.createHideErrorTextWatcher(this.guestEmail));
        this.guestFirstName.getEditText().addTextChangedListener(ViewUtil.createHideErrorTextWatcher(this.guestFirstName));
        this.guestLastName.getEditText().addTextChangedListener(ViewUtil.createHideErrorTextWatcher(this.guestLastName));
        this.phoneNumberInput.getEditText().addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                EditGuestDetailsView.this.phoneNumberInput.hideError();
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (!EditGuestDetailsView.this.phoneNumberInput.getText().toString().equals(EditGuestDetailsView.this.formattedNumber)) {
                    EditGuestDetailsView.this.formatPhone();
                }
            }

            public void afterTextChanged(Editable editable) {
                if (!EditGuestDetailsView.this.phoneNumberInput.getText().toString().equals(EditGuestDetailsView.this.formattedNumber)) {
                    if (EditGuestDetailsView.this.phoneNumberInput.getText().toString().equals("")) {
                        EditGuestDetailsView.this.formattedNumber = "";
                    }
                    EditGuestDetailsView.this.phoneNumberInput.setText(EditGuestDetailsView.this.formattedNumber);
                }
                EditGuestDetailsView.this.phoneNumberInput.getEditText().setSelection(EditGuestDetailsView.this.formattedNumber.length());
            }
        });
        this.mButtonAddGuest.setEnabled(true);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            AppFlow.get(getContext());
            this.phoneNumberUtil = PhoneNumberUtil.getInstance();
            this.formatter = this.phoneNumberUtil.getAsYouTypeFormatter(TextUtils.DEFAULT_COUNTRY_ISO);
            this.mEditGuestDetailsPresenter = new EditGuestDetailsPresenter(this);
            this.mEditGuestDetailsPresenter.start();
            init();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mEditGuestDetailsPresenter.finish();
        ViewUtil.hideKeyboard(getContext(), this.guestFirstName.getWindowToken());
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    public void updateView(Guest guest) {
        this.guestFirstName.setText(guest.getFirstName() != null ? guest.getFirstName() : "");
        this.guestFirstName.getEditText().requestFocus();
        this.guestLastName.setText(guest.getLastName() != null ? guest.getLastName() : "");
        this.guestLastName.getEditText().requestFocus();
        this.guestEmail.setText(guest.getEmail() != null ? guest.getEmail() : "");
        this.guestEmail.getEditText().requestFocus();
        this.guestOrganization.setText(guest.getOrganization() != null ? guest.getOrganization() : "");
        this.guestOrganization.getEditText().requestFocus();
        this.phoneNumberInput.setText(guest.getMobileNumberE164() != null ? TextUtils.convertPhoneFromE164(guest.getMobileNumberE164(), guest.getAlphaCountryCode()) : "");
        this.phoneNumberInput.getEditText().requestFocus();
        if (guest.getAlphaCountryCode() != null) {
            this.selectedCountry = this.mEditGuestDetailsPresenter.lookForCountryCode(guest.getAlphaCountryCode());
            setCountryCode(this.selectedCountry);
        }
        clearFocus();
    }

    @OnClick({2131296332})
    public void addGuest() {
        if (validateRequiredFields()) {
            ViewUtil.hideKeyboard(getContext(), getWindowToken());
            this.mButtonAddGuest.setEnabled(false);
            String str = null;
            String trim = Strings.isNullOrEmpty(this.guestFirstName.getText()) ? null : this.guestFirstName.getText().trim();
            String trim2 = Strings.isNullOrEmpty(this.guestLastName.getText()) ? null : this.guestLastName.getText().trim();
            String trim3 = Strings.isNullOrEmpty(this.guestEmail.getText()) ? null : this.guestEmail.getText().trim();
            String trim4 = Strings.isNullOrEmpty(this.guestOrganization.getText()) ? null : this.guestOrganization.getText().trim();
            if (!Strings.isNullOrEmpty(this.phoneNumberInput.getText())) {
                str = TextUtils.convertPhoneToE164(this.phoneNumberInput.getText().trim(), this.selectedCountry.cca2);
            }
            this.mEditGuestDetailsPresenter.performClickAction(trim, trim2, str, trim3, trim4, this.selectedCountry.callingCode[0].replace("+", ""), this.selectedCountry.cca2);
        }
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296376})
    public void openModal() {
        this.mEditGuestDetailsPresenter.showCountriesDialog();
    }

    public void setCountryCode(Country country) {
        this.formatter = this.phoneNumberUtil.getAsYouTypeFormatter(country.cca2);
        formatPhone();
        this.phoneNumberInput.setText(this.formattedNumber);
        ((EditText) ButterKnife.findById((View) this, (int) C1075R.C1077id.area_code)).setText(country.callingCode[0]);
        ((EditText) ButterKnife.findById((View) this, (int) C1075R.C1077id.txt_country)).setText(country.cca3);
        this.selectedCountry = country;
        this.mEditGuestDetailsPresenter.storeCountryOnView(this.selectedCountry);
    }

    private boolean validateRequiredFields() {
        boolean z;
        if (Strings.isNullOrEmpty(this.guestFirstName.getText())) {
            showFirstNameError(getContext().getString(C1075R.string.first_name_required));
            z = false;
        } else {
            z = true;
        }
        if (!Strings.isNullOrEmpty(this.guestEmail.getText()) && !TextUtils.isValidEmail(this.guestEmail.getText())) {
            showEmailError(getContext().getString(C1075R.string.error_invalid_email));
            z = false;
        }
        this.phoneNumberInput.hideError();
        if (Strings.isNullOrEmpty(this.phoneNumberInput.getText()) || TextUtils.isValidPhoneNumber(this.phoneNumberInput.getText(), this.selectedCountry.cca2)) {
            return z;
        }
        showMobileNumberError(getContext().getString(C1075R.string.error_invalid_phone_number));
        return false;
    }

    public void displayError(Throwable th) {
        Toast.makeText(getContext(), th.getMessage() != null ? th.getMessage() : th.getClass().getSimpleName(), 0).show();
        this.mButtonAddGuest.setEnabled(true);
    }

    public void showFirstNameError(String str) {
        this.guestFirstName.showError(str, -1, -1);
    }

    public void showLastNameError(String str) {
        this.guestLastName.showError(str, -1, -1);
    }

    public void showEmailError(String str) {
        this.guestEmail.showError(str, -1, -1);
    }

    public void showMobileNumberError(String str) {
        this.phoneNumberInput.showError(str, -1, -1);
    }

    public void displayGuestUpdatedToast() {
        Toast.makeText(getContext(), getContext().getString(C1075R.string.guest_details_form_updated_alert_text), 1).show();
    }

    public void displayGuestAddedToast() {
        Toast.makeText(getContext(), getContext().getString(C1075R.string.guest_details_form_guest_added), 1).show();
    }

    public void setLockBanner(String str, String str2) {
        this.mLockNameBanner.setText(str);
        this.mDeviceIdBanner.setText(str2);
    }

    public Country getSelectedCountry() {
        return this.selectedCountry;
    }

    public void showInstructions(boolean z) {
        this.mInstructions.setVisibility(z ? 0 : 8);
    }

    public void enableAddGuestButton(boolean z) {
        this.mButtonAddGuest.setEnabled(z);
    }

    public void formatPhone() {
        this.formatter.clear();
        String replace = this.phoneNumberInput.getText().toString().replace(" ", "").replace("-", "").replace("(", "").replace(")", "");
        for (int i = 0; i < replace.length(); i++) {
            this.formattedNumber = this.formatter.inputDigit(replace.charAt(i));
        }
    }

    public boolean onUpPressed() {
        this.mEditGuestDetailsPresenter.goBack();
        return true;
    }

    public boolean onBackPressed() {
        this.mEditGuestDetailsPresenter.goBack();
        return true;
    }
}
