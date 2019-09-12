package com.masterlock.ble.app.presenter.signup;

import com.masterlock.api.entity.AccountCreationCodeConfirmationRequest;
import com.masterlock.api.entity.AccountCreationCodeConfirmationRequest.ValidationType;
import com.masterlock.api.entity.EmailPhoneVerificationResponse;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.screens.SignInScreens.SignIn;
import com.masterlock.ble.app.screens.SignUpScreens.AccountDetails;
import com.masterlock.ble.app.service.SignUpService;
import com.masterlock.ble.app.view.signup.ResendSmsView;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.android.schedulers.AndroidSchedulers;
import p009rx.schedulers.Schedulers;
import p009rx.subscriptions.Subscriptions;
import retrofit.client.Response;

public class ResendSmsPresenter extends Presenter<String, ResendSmsView> {
    private Subscription mConfirmSmsCodeSubscription = Subscriptions.empty();
    @Inject
    Bus mEventBus;
    private MasterLockSharedPreferences mPreferences;
    @Inject
    SignUpService mSignUpService;
    private Subscription mSubscription = Subscriptions.empty();

    public ResendSmsPresenter(ResendSmsView resendSmsView) {
        super(resendSmsView);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        this.mPreferences = MasterLockSharedPreferences.getInstance();
    }

    public String getPhoneNumber() {
        return this.mPreferences.getSignUpPhone();
    }

    public int getSkipCount() {
        return this.mPreferences.getSkipSmsVerification();
    }

    public void skipSmsVerification() {
        AppFlow.get(((ResendSmsView) this.view).getContext()).goTo(new AccountDetails());
    }

    public void resendSms() {
        int skipSmsVerification = this.mPreferences.getSkipSmsVerification() + 1;
        if (skipSmsVerification >= 3) {
            ((ResendSmsView) this.view).redrawToSkipValidation();
        } else {
            this.mSubscription.unsubscribe();
            this.mSubscription = this.mSignUpService.signUp(null, this.mPreferences.getSignUpCountryCode(), this.mPreferences.getSignUpPhone()).subscribeOn(Schedulers.m220io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Subscriber<? super T>) new Subscriber<EmailPhoneVerificationResponse>() {
                public void onStart() {
                    ResendSmsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
                }

                public void onCompleted() {
                    ResendSmsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                }

                public void onError(Throwable th) {
                    ResendSmsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                    ((ResendSmsView) ResendSmsPresenter.this.view).displayError(ApiError.generateError(th));
                }

                public void onNext(EmailPhoneVerificationResponse emailPhoneVerificationResponse) {
                    if (!emailPhoneVerificationResponse.error.equals("")) {
                        ((ResendSmsView) ResendSmsPresenter.this.view).displayError(ApiError.generateError(new Throwable(emailPhoneVerificationResponse.error.split(ApiError.MESSAGE_DELIMITER)[1])));
                    }
                }
            });
        }
        this.mPreferences.putSkipSmsVerification(skipSmsVerification);
    }

    public void confirmAccountCreationPhoneVerificationCode(String str) {
        AccountCreationCodeConfirmationRequest accountCreationCodeConfirmationRequest = new AccountCreationCodeConfirmationRequest();
        accountCreationCodeConfirmationRequest.setValidationType(ValidationType.SMS);
        accountCreationCodeConfirmationRequest.setVerificationId(this.mPreferences.getVerificationId());
        accountCreationCodeConfirmationRequest.setVerificationCode(str);
        this.mConfirmSmsCodeSubscription.unsubscribe();
        this.mConfirmSmsCodeSubscription = this.mSignUpService.confirmAccountCreationCode(accountCreationCodeConfirmationRequest).subscribeOn(Schedulers.m220io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Subscriber<? super T>) new Subscriber<Response>() {
            public void onStart() {
                ResendSmsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                ResendSmsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                ResendSmsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                if (ApiError.generateError(th).getCode().equals(ApiError.ACCOUNT_CREATION_INVALID_CODE)) {
                    ((ResendSmsView) ResendSmsPresenter.this.view).displayError(ApiError.generateError(new Throwable(((ResendSmsView) ResendSmsPresenter.this.view).getResources().getString(C1075R.string.sms_error_verification))));
                } else {
                    ((ResendSmsView) ResendSmsPresenter.this.view).displayError(ApiError.generateError(th));
                }
            }

            public void onNext(Response response) {
                AppFlow.get(((ResendSmsView) ResendSmsPresenter.this.view).getContext()).goTo(new AccountDetails());
            }
        });
    }

    public void finish() {
        super.finish();
        this.mSubscription.unsubscribe();
        this.mConfirmSmsCodeSubscription.unsubscribe();
    }

    public void signIn() {
        AppFlow.get(((ResendSmsView) this.view).getContext()).goTo(new SignIn());
    }
}
