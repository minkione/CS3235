package com.masterlock.ble.app.presenter.nav.settings;

import android.app.Dialog;
import android.content.res.Resources;
import android.telephony.PhoneNumberUtils;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.bus.ChangeSoftKeyboardBehaviorEvent;
import com.masterlock.ble.app.screens.NavScreens.AccountProfile;
import com.masterlock.ble.app.screens.NavScreens.VerifyNewPhone;
import com.masterlock.ble.app.util.TextUtils;
import com.masterlock.ble.app.view.modal.BaseModal;
import com.masterlock.ble.app.view.modal.CountriesModal;
import com.masterlock.ble.app.view.modal.CountriesModal.CountrySelectedListener;
import com.masterlock.ble.app.view.nav.settings.ChangePhoneNumberView;
import com.masterlock.core.AccountProfileInfo;
import com.masterlock.core.Country;
import com.masterlock.core.ProfileUpdateFields;
import com.square.flow.appflow.AppFlow;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.concurrent.TimeUnit;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;
import retrofit.client.Response;

public class ChangePhoneNumberPresenter extends ProfileUpdateBasePresenter<AccountProfileInfo, ChangePhoneNumberView> {
    private final long RESEND_IGNORE_TIME = TimeUnit.HOURS.toMillis(1);
    /* access modifiers changed from: private */
    public Dialog mDialog;
    private Subscription mRemovePhoneNumberSubscription = Subscriptions.empty();

    public ChangePhoneNumberPresenter(ChangePhoneNumberView changePhoneNumberView) {
        super(changePhoneNumberView);
    }

    public void start() {
        super.start();
        Country lookForCountryCode = lookForCountryCode(((AccountProfileInfo) this.model).getAlphaCountryCode());
        updateSelectedCountryCode(lookForCountryCode.callingCode[0], lookForCountryCode.cca2);
        ((ChangePhoneNumberView) this.view).displayCountryCode(((AccountProfileInfo) this.model).getCountryCode(), ((AccountProfileInfo) this.model).getAlphaCountryCode());
        ((ChangePhoneNumberView) this.view).phoneNumberEditTextSetup(((AccountProfileInfo) this.model).getAlphaCountryCode());
        ((ChangePhoneNumberView) this.view).setCurrentPhoneNumber((AccountProfileInfo) this.model);
        this.mEventBus.post(new ChangeSoftKeyboardBehaviorEvent(32));
    }

    public void finish() {
        super.finish();
    }

    public void updatePhoneNumber(String str) {
        String convertPhoneToE164 = TextUtils.convertPhoneToE164(PhoneNumberUtils.stripSeparators(str).trim(), ((AccountProfileInfo) this.model).getAlphaCountryCode());
        if (!Strings.isNullOrEmpty(convertPhoneToE164)) {
            str = convertPhoneToE164;
        }
        if (str.equals(((AccountProfileInfo) this.model).getPhoneNumber()) && ((AccountProfileInfo) this.model).isPhoneVerified()) {
            showErrorModal(((ChangePhoneNumberView) this.view).getContext().getString(C1075R.string.profile_setting_change_phone_same_number_error_title), ((ChangePhoneNumberView) this.view).getContext().getString(C1075R.string.profile_setting_change_phone_same_number_error_body));
        } else if (shouldResendSMS()) {
            ((AccountProfileInfo) this.model).setFieldToUpdate(ProfileUpdateFields.MOBILE_PHONE_NUMBER);
            ((AccountProfileInfo) this.model).setNewPhoneNumber(str);
            performProfileUpdate(new Subscriber<Response>() {
                public void onNext(Response response) {
                }

                public void onCompleted() {
                    ((AccountProfileInfo) ChangePhoneNumberPresenter.this.model).setPhoneVerified(false);
                    ((AccountProfileInfo) ChangePhoneNumberPresenter.this.model).setPhoneNumber(((AccountProfileInfo) ChangePhoneNumberPresenter.this.model).getNewPhoneNumber());
                    ChangePhoneNumberPresenter.this.prefs.putUserPhoneNumber(((AccountProfileInfo) ChangePhoneNumberPresenter.this.model).getNewPhoneNumber());
                    ChangePhoneNumberPresenter.this.prefs.putUserAlphaCC(((AccountProfileInfo) ChangePhoneNumberPresenter.this.model).getAlphaCountryCode());
                    ChangePhoneNumberPresenter.this.prefs.putUserPhoneCC(((AccountProfileInfo) ChangePhoneNumberPresenter.this.model).getCountryCode());
                    ChangePhoneNumberPresenter.this.prefs.putUserPhoneIsVerified(((AccountProfileInfo) ChangePhoneNumberPresenter.this.model).isPhoneVerified());
                    List intentSMSResendPeriods = ChangePhoneNumberPresenter.this.prefs.getIntentSMSResendPeriods();
                    intentSMSResendPeriods.add(String.valueOf(System.currentTimeMillis()));
                    ChangePhoneNumberPresenter.this.prefs.putIntentSMSResendPeriods(intentSMSResendPeriods);
                    ((ChangePhoneNumberView) ChangePhoneNumberPresenter.this.view).displayMessage((int) C1075R.string.profile_setting_change_new_phone_number_sms_sent);
                    AppFlow.get(((ChangePhoneNumberView) ChangePhoneNumberPresenter.this.view).getContext()).goTo(new VerifyNewPhone((AccountProfileInfo) ChangePhoneNumberPresenter.this.model));
                }

                public void onError(Throwable th) {
                    ApiError generateError = ApiError.generateError(th);
                    if (Strings.isNullOrEmpty(generateError.getCode()) || !generateError.getCode().equals(ApiError.INVALID_MOBILE_PHONE)) {
                        ((ChangePhoneNumberView) ChangePhoneNumberPresenter.this.view).displayMessage(generateError.getMessage());
                        return;
                    }
                    ChangePhoneNumberPresenter changePhoneNumberPresenter = ChangePhoneNumberPresenter.this;
                    changePhoneNumberPresenter.showErrorModal(((ChangePhoneNumberView) changePhoneNumberPresenter.view).getContext().getString(C1075R.string.profile_setting_change_phone_cannot_verify_error_title), ((ChangePhoneNumberView) ChangePhoneNumberPresenter.this.view).getContext().getString(C1075R.string.profile_setting_change_phone_cannot_verify_error_body));
                }
            });
        }
    }

    public boolean shouldResendSMS() {
        List intentSMSResendPeriods = this.prefs.getIntentSMSResendPeriods();
        if (intentSMSResendPeriods.size() > 0) {
            long currentTimeMillis = System.currentTimeMillis() - this.RESEND_IGNORE_TIME;
            if (intentSMSResendPeriods.size() > 4) {
                if (Long.parseLong((String) intentSMSResendPeriods.get(4)) > currentTimeMillis) {
                    showModal(null, Integer.valueOf(C1075R.string.profile_setting_change_new_phone_number_sms_limit_error));
                    return false;
                }
                intentSMSResendPeriods.clear();
                this.prefs.clearItentSMSResendPeriods();
            }
        }
        return true;
    }

    private void removePhoneNumber(String str) {
        this.mRemovePhoneNumberSubscription.unsubscribe();
        this.mRemovePhoneNumberSubscription = this.mProfileUpdateService.removeMobilePhone(str.replace("+", "")).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Response>() {
            public void onNext(Response response) {
            }

            public void onStart() {
                if (ChangePhoneNumberPresenter.this.view != null) {
                    ((ChangePhoneNumberView) ChangePhoneNumberPresenter.this.view).enableRemovePhoneNumberButton(false);
                }
                ChangePhoneNumberPresenter.this.startProgress();
            }

            public void onCompleted() {
                ChangePhoneNumberPresenter.this.stopProgress();
                ChangePhoneNumberPresenter.this.prefs.putUserPhoneCC("");
                ChangePhoneNumberPresenter.this.prefs.putUserAlphaCC("");
                ChangePhoneNumberPresenter.this.prefs.putUserPhoneNumber("");
                ChangePhoneNumberPresenter.this.prefs.putUserPhoneIsVerified(false);
                AppFlow.get(((ChangePhoneNumberView) ChangePhoneNumberPresenter.this.view).getContext()).resetTo(new AccountProfile());
            }

            public void onError(Throwable th) {
                ChangePhoneNumberPresenter.this.stopProgress();
                if (ChangePhoneNumberPresenter.this.view != null) {
                    ((ChangePhoneNumberView) ChangePhoneNumberPresenter.this.view).displayMessage(ApiError.generateError(th).getMessage());
                    ((ChangePhoneNumberView) ChangePhoneNumberPresenter.this.view).enableRemovePhoneNumberButton(true);
                }
            }
        });
    }

    public void showModal(Integer num, Integer num2) {
        int i;
        Resources resources = ((ChangePhoneNumberView) this.view).getContext().getResources();
        BaseModal baseModal = new BaseModal(((ChangePhoneNumberView) this.view).getContext());
        Dialog dialog = new Dialog(((ChangePhoneNumberView) this.view).getContext());
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.requestWindowFeature(1);
        dialog.setContentView(baseModal);
        $$Lambda$ChangePhoneNumberPresenter$jK2P1d3j4NhabGUDHyCcShJzaQc r3 = new OnClickListener(dialog) {
            private final /* synthetic */ Dialog f$0;

            {
                this.f$0 = r1;
            }

            public final void onClick(View view) {
                this.f$0.dismiss();
            }
        };
        if (num == null) {
            i = C1075R.string.error;
        } else {
            i = num.intValue();
        }
        baseModal.setTitle(resources.getString(i));
        baseModal.setBody(Html.fromHtml(resources.getString(num2.intValue())));
        baseModal.getPositiveButton().setText(resources.getString(C1075R.string.f165ok));
        baseModal.setPositiveButtonClickListener(r3);
        baseModal.getNegativeButton().setVisibility(8);
        dialog.show();
    }

    public void showCountriesDialog() {
        CountriesModal countriesModal = new CountriesModal(((ChangePhoneNumberView) this.view).getContext(), null);
        countriesModal.setCountrySelectedListener(new CountrySelectedListener() {
            public void onCountrySelected(Country country) {
                ((ChangePhoneNumberView) ChangePhoneNumberPresenter.this.view).displayCountryCode(country.callingCode[0], country.cca2);
                ChangePhoneNumberPresenter.this.updateSelectedCountryCode(country.callingCode[0], country.cca2);
                ChangePhoneNumberPresenter.this.mDialog.dismiss();
            }

            public void onCloseClicked() {
                ChangePhoneNumberPresenter.this.mDialog.dismiss();
            }
        });
        this.mDialog = new Dialog(((ChangePhoneNumberView) this.view).getContext());
        this.mDialog.requestWindowFeature(1);
        this.mDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mDialog.setContentView(countriesModal);
        this.mDialog.show();
    }

    public void showRemovePhoneModal() {
        BaseModal baseModal = new BaseModal(((ChangePhoneNumberView) this.view).getContext(), null);
        baseModal.setTitle(((ChangePhoneNumberView) this.view).getResources().getString(C1075R.string.profile_setting_change_phone_remove_phone_title));
        baseModal.setBody(((ChangePhoneNumberView) this.view).getResources().getString(C1075R.string.profile_setting_change_phone_remove_phone_body));
        baseModal.setNegativeButtonClickListener(new OnClickListener() {
            public final void onClick(View view) {
                ChangePhoneNumberPresenter.this.dismissRemovePhoneDialog();
            }
        });
        baseModal.setPositiveButtonClickListener(new OnClickListener() {
            public final void onClick(View view) {
                ChangePhoneNumberPresenter.lambda$showRemovePhoneModal$2(ChangePhoneNumberPresenter.this, view);
            }
        });
        baseModal.getPositiveButton().setVisibility(0);
        baseModal.getPositiveButton().setText(C1075R.string.yes);
        baseModal.getNegativeButton().setVisibility(0);
        baseModal.getNegativeButton().setText(C1075R.string.f164no);
        this.mDialog = new Dialog(((ChangePhoneNumberView) this.view).getContext());
        this.mDialog.requestWindowFeature(1);
        this.mDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mDialog.setContentView(baseModal);
        this.mDialog.show();
    }

    public static /* synthetic */ void lambda$showRemovePhoneModal$2(ChangePhoneNumberPresenter changePhoneNumberPresenter, View view) {
        changePhoneNumberPresenter.removePhoneNumber(TextUtils.convertPhoneToE164(PhoneNumberUtils.stripSeparators(((AccountProfileInfo) changePhoneNumberPresenter.model).getPhoneNumber()).trim(), ((AccountProfileInfo) changePhoneNumberPresenter.model).getAlphaCountryCode()));
        changePhoneNumberPresenter.dismissRemovePhoneDialog();
    }

    public void showErrorModal(String str, String str2) {
        BaseModal baseModal = new BaseModal(((ChangePhoneNumberView) this.view).getContext());
        Dialog dialog = new Dialog(((ChangePhoneNumberView) this.view).getContext());
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.requestWindowFeature(1);
        dialog.setContentView(baseModal);
        $$Lambda$ChangePhoneNumberPresenter$4cAdHT03GpQxeIdcg2OGGBHFnUw r2 = new OnClickListener(dialog) {
            private final /* synthetic */ Dialog f$0;

            {
                this.f$0 = r1;
            }

            public final void onClick(View view) {
                this.f$0.dismiss();
            }
        };
        if (Strings.isNullOrEmpty(str)) {
            str = ((ChangePhoneNumberView) this.view).getContext().getString(C1075R.string.error);
        }
        baseModal.setTitle(str);
        baseModal.setBody(Html.fromHtml(str2));
        baseModal.getPositiveButton().setText(((ChangePhoneNumberView) this.view).getContext().getResources().getString(C1075R.string.f165ok));
        baseModal.setPositiveButtonClickListener(r2);
        baseModal.getNegativeButton().setVisibility(8);
        dialog.show();
    }

    /* access modifiers changed from: private */
    public void dismissRemovePhoneDialog() {
        Dialog dialog = this.mDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mDialog.dismiss();
        }
    }

    public Country lookForCountryCode(String str) {
        Country country;
        Country country2 = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(((ChangePhoneNumberView) this.view).getContext().getAssets().open("country.json")));
            List list = (List) new Gson().fromJson((Reader) bufferedReader, new TypeToken<List<Country>>() {
            }.getType());
            bufferedReader.close();
            int i = 0;
            int i2 = 0;
            while (true) {
                if (i >= list.size()) {
                    break;
                }
                Country country3 = (Country) list.get(i);
                if (country3.cca2.equals(TextUtils.DEFAULT_COUNTRY_ISO)) {
                    i2 = i;
                }
                if (country3.cca2.equals(str)) {
                    country2 = country3;
                    break;
                }
                i++;
            }
            if (country2 != null) {
                country = country2;
            } else {
                country = (Country) list.get(i2);
            }
        } catch (IOException e) {
            e.printStackTrace();
            country = null;
        }
        country.callingCode[0] = country.callingCode[0];
        return country;
    }

    /* access modifiers changed from: private */
    public void updateSelectedCountryCode(String str, String str2) {
        AccountProfileInfo accountProfileInfo = (AccountProfileInfo) this.model;
        if (str.contains("+")) {
            str = str.replace("+", "");
        }
        accountProfileInfo.setCountryCode(str);
        ((AccountProfileInfo) this.model).setAlphaCountryCode(str2);
    }
}
