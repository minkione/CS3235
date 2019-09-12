package com.masterlock.ble.app.presenter.signup;

import com.google.common.base.Strings;
import com.masterlock.api.entity.AccountCreationCodeConfirmationRequest;
import com.masterlock.api.entity.AccountCreationCodeConfirmationRequest.ValidationType;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.screens.SignInScreens.SignIn;
import com.masterlock.ble.app.screens.SignUpScreens.AccountDetails;
import com.masterlock.ble.app.screens.SignUpScreens.SignUp;
import com.masterlock.ble.app.service.SignUpService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.signup.ResendEmailView;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;
import retrofit.client.Response;

public class ResendEmailPresenter extends Presenter<String, ResendEmailView> {
    private static final String TAG = "ResendEmailPresenter";
    @Inject
    Bus mEventBus;
    private MasterLockSharedPreferences mPreferences;
    @Inject
    IScheduler mScheduler;
    @Inject
    SignUpService mSignUpService;
    private Subscription mSubscription = Subscriptions.empty();

    public ResendEmailPresenter(ResendEmailView resendEmailView) {
        super(resendEmailView);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        this.mPreferences = MasterLockSharedPreferences.getInstance();
    }

    public void finish() {
        super.finish();
        this.mSubscription.unsubscribe();
    }

    public String getUserEmail() {
        return this.mPreferences.getSignUpEmail();
    }

    public void resendEmail() {
        AppFlow.get(((ResendEmailView) this.view).getContext()).resetTo(new SignUp(this.mPreferences.getSignUpEmail(), this.mPreferences.getSignUpCountryCode(), this.mPreferences.getSignUpPhone()));
    }

    public void confirmAccountCreationPhoneVerificationCode(String str) {
        AccountCreationCodeConfirmationRequest accountCreationCodeConfirmationRequest = new AccountCreationCodeConfirmationRequest();
        accountCreationCodeConfirmationRequest.setValidationType(ValidationType.EMAIL);
        accountCreationCodeConfirmationRequest.setVerificationId(this.mPreferences.getVerificationId());
        accountCreationCodeConfirmationRequest.setVerificationCode(str);
        this.mSubscription.unsubscribe();
        this.mSubscription = this.mSignUpService.confirmAccountCreationCode(accountCreationCodeConfirmationRequest).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Response>() {
            public void onStart() {
                ResendEmailPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                ResendEmailPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                ResendEmailPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ApiError generateError = ApiError.generateError(th);
                if (Strings.isNullOrEmpty(generateError.getCode()) || !generateError.getCode().equals(ApiError.ACCOUNT_CREATION_INVALID_CODE)) {
                    ((ResendEmailView) ResendEmailPresenter.this.view).displayError(generateError);
                } else {
                    ((ResendEmailView) ResendEmailPresenter.this.view).displayError(((ResendEmailView) ResendEmailPresenter.this.view).getResources().getString(C1075R.string.email_error_verification));
                }
            }

            public void onNext(Response response) {
                AppFlow.get(((ResendEmailView) ResendEmailPresenter.this.view).getContext()).resetTo(new AccountDetails());
            }
        });
    }

    public void signIn() {
        AppFlow.get(((ResendEmailView) this.view).getContext()).goTo(new SignIn());
    }
}
