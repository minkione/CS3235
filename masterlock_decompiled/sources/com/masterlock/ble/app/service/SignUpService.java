package com.masterlock.ble.app.service;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.masterlock.api.client.AccountClient;
import com.masterlock.api.entity.AccountCreationCodeConfirmationRequest;
import com.masterlock.api.entity.AuthResponse;
import com.masterlock.api.entity.CreateAccountRequest;
import com.masterlock.api.entity.EmailPhoneVerificationRequest;
import com.masterlock.api.entity.EmailPhoneVerificationResponse;
import com.masterlock.api.entity.TermsOfService;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.dao.SignUpDAO;
import com.masterlock.ble.app.util.ThreadUtil;
import javax.inject.Inject;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;
import p009rx.functions.Func1;
import retrofit.client.Response;

public class SignUpService {
    public static final String API_KEY = MasterLockApp.get().getString(C1075R.string.api_key);
    AccountClient accountClient;
    @Inject
    SignUpDAO mSignUpDAO;
    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    MasterLockSharedPreferences preferences = MasterLockSharedPreferences.getInstance();
    TermsOfServiceService termsOfServiceService;

    public SignUpService(TermsOfServiceService termsOfServiceService2, AccountClient accountClient2) {
        this.termsOfServiceService = termsOfServiceService2;
        this.accountClient = accountClient2;
        MasterLockApp.get().inject(this);
    }

    public Observable<EmailPhoneVerificationResponse> signUp(String str, String str2, String str3) {
        return this.termsOfServiceService.getTermsOfServiceAsHTML().flatMap(new Func1(str, str2, str3) {
            private final /* synthetic */ String f$1;
            private final /* synthetic */ String f$2;
            private final /* synthetic */ String f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            public final Object call(Object obj) {
                return SignUpService.lambda$signUp$0(SignUpService.this, this.f$1, this.f$2, this.f$3, (TermsOfService) obj);
            }
        });
    }

    public static /* synthetic */ Observable lambda$signUp$0(SignUpService signUpService, String str, String str2, String str3, TermsOfService termsOfService) {
        String str4;
        ThreadUtil.errorOnUIThread();
        EmailPhoneVerificationRequest emailPhoneVerificationRequest = new EmailPhoneVerificationRequest();
        emailPhoneVerificationRequest.verificationId = signUpService.preferences.getVerificationId();
        emailPhoneVerificationRequest.email = str;
        if (str2.equals("") || str3.equals("")) {
            str4 = "";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(str3);
            str4 = sb.toString();
        }
        emailPhoneVerificationRequest.phone = str4;
        emailPhoneVerificationRequest.phoneCountryCode = str2;
        emailPhoneVerificationRequest.alphaCountyCode = signUpService.phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(str2.substring(1)));
        emailPhoneVerificationRequest.source = API_KEY;
        emailPhoneVerificationRequest.redirectUrl = MasterLockApp.get().getString(C1075R.string.redirect_uri);
        signUpService.preferences.putSignUpEmail(str);
        signUpService.preferences.putSignUpCountryCode(str2);
        signUpService.preferences.putSignUpPhone(str3);
        return signUpService.createEmailPhoneVerification(emailPhoneVerificationRequest);
    }

    private Observable<EmailPhoneVerificationResponse> createEmailPhoneVerification(EmailPhoneVerificationRequest emailPhoneVerificationRequest) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(emailPhoneVerificationRequest) {
            private final /* synthetic */ EmailPhoneVerificationRequest f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                SignUpService.lambda$createEmailPhoneVerification$1(SignUpService.this, this.f$1, (Subscriber) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$createEmailPhoneVerification$1(SignUpService signUpService, EmailPhoneVerificationRequest emailPhoneVerificationRequest, Subscriber subscriber) {
        ThreadUtil.errorOnUIThread();
        EmailPhoneVerificationResponse createEmailPhoneVerification = signUpService.accountClient.createEmailPhoneVerification(API_KEY, emailPhoneVerificationRequest);
        signUpService.preferences.putVerificationId(createEmailPhoneVerification.verificationId);
        subscriber.onNext(createEmailPhoneVerification);
        subscriber.onCompleted();
    }

    public Observable<AuthResponse> createAccount(final CreateAccountRequest createAccountRequest) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<AuthResponse>() {
            public void call(Subscriber<? super AuthResponse> subscriber) {
                ThreadUtil.errorOnUIThread();
                subscriber.onNext(SignUpService.this.accountClient.createAccount(SignUpService.API_KEY, createAccountRequest));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Response> confirmAccountCreationCode(AccountCreationCodeConfirmationRequest accountCreationCodeConfirmationRequest) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(accountCreationCodeConfirmationRequest) {
            private final /* synthetic */ AccountCreationCodeConfirmationRequest f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                SignUpService.lambda$confirmAccountCreationCode$2(SignUpService.this, this.f$1, (Subscriber) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$confirmAccountCreationCode$2(SignUpService signUpService, AccountCreationCodeConfirmationRequest accountCreationCodeConfirmationRequest, Subscriber subscriber) {
        ThreadUtil.errorOnUIThread();
        subscriber.onNext(signUpService.accountClient.confirmAccountCreationCode(API_KEY, accountCreationCodeConfirmationRequest.getVerificationId(), accountCreationCodeConfirmationRequest.getValidationType(), accountCreationCodeConfirmationRequest.getVerificationCode()));
        subscriber.onCompleted();
    }

    public Observable<Boolean> containsDictionaryWords(final String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Boolean>() {
            public void call(Subscriber<? super Boolean> subscriber) {
                ThreadUtil.errorOnUIThread();
                subscriber.onNext(Boolean.valueOf(SignUpService.this.mSignUpDAO.getMatchingDictionaryWords(str).size() > 0));
                subscriber.onCompleted();
            }
        });
    }
}
