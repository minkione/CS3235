package com.masterlock.ble.app.presenter.nav.settings;

import com.google.common.base.Strings;
import com.masterlock.api.entity.ProfileEmailVerificationRequest;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.bus.ChangeSoftKeyboardBehaviorEvent;
import com.masterlock.ble.app.screens.NavScreens.AccountProfile;
import com.masterlock.ble.app.screens.NavScreens.ChangeEmailAddress;
import com.masterlock.ble.app.view.nav.settings.VerifyNewEmailView;
import com.masterlock.core.AccountProfileInfo;
import com.square.flow.appflow.AppFlow;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;
import retrofit.client.Response;

public class VerifyNewEmailPresenter extends ProfileUpdateBasePresenter<AccountProfileInfo, VerifyNewEmailView> {
    private Subscription mVerifiyEmailConfirmationCodeSubscription = Subscriptions.empty();

    public VerifyNewEmailPresenter(VerifyNewEmailView verifyNewEmailView) {
        super(verifyNewEmailView);
    }

    public void start() {
        super.start();
        ((VerifyNewEmailView) this.view).setCurrentEmail(((AccountProfileInfo) this.model).getNewEmail());
        this.mEventBus.post(new ChangeSoftKeyboardBehaviorEvent(32));
    }

    public void finish() {
        super.finish();
        this.mEventBus.post(new ChangeSoftKeyboardBehaviorEvent(16));
    }

    public void resendEmail() {
        AppFlow.get(((VerifyNewEmailView) this.view).getContext()).resetTo(new ChangeEmailAddress((AccountProfileInfo) this.model));
    }

    public void verifyEmailCode(String str) {
        this.mVerifiyEmailConfirmationCodeSubscription.unsubscribe();
        this.mVerifiyEmailConfirmationCodeSubscription = this.mProfileUpdateService.verifyEmail(new ProfileEmailVerificationRequest(((AccountProfileInfo) this.model).getNewEmail(), str)).observeOn(this.mScheduler.main()).subscribeOn(this.mScheduler.background()).subscribe((Subscriber<? super T>) new Subscriber<Response>() {
            public void onNext(Response response) {
            }

            public void onStart() {
                VerifyNewEmailPresenter.this.startProgress();
            }

            public void onCompleted() {
                VerifyNewEmailPresenter.this.stopProgress();
                VerifyNewEmailPresenter.this.prefs.putUserEmail(((AccountProfileInfo) VerifyNewEmailPresenter.this.model).getNewEmail());
                ((VerifyNewEmailView) VerifyNewEmailPresenter.this.view).displayMessage(((VerifyNewEmailView) VerifyNewEmailPresenter.this.view).getContext().getString(C1075R.string.change_email_successful_updated));
                AppFlow.get(((VerifyNewEmailView) VerifyNewEmailPresenter.this.view).getContext()).resetTo(new AccountProfile());
            }

            public void onError(Throwable th) {
                VerifyNewEmailPresenter.this.stopProgress();
                if (VerifyNewEmailPresenter.this.view != null) {
                    ApiError generateError = ApiError.generateError(th);
                    if (!Strings.isNullOrEmpty(generateError.getCode())) {
                        ErrorType errorType = null;
                        String code = generateError.getCode();
                        char c = 65535;
                        switch (code.hashCode()) {
                            case 46819474:
                                if (code.equals(ApiError.EMAIL_ALREADY_ASSOCIATED)) {
                                    c = 1;
                                    break;
                                }
                                break;
                            case 46819475:
                                if (code.equals(ApiError.INVALID_EMAIL_VERIFICATION_CODE)) {
                                    c = 0;
                                    break;
                                }
                                break;
                        }
                        switch (c) {
                            case 0:
                                errorType = ErrorType.EMAIL_INVALID_CONFIRMATION_CODE;
                                break;
                            case 1:
                                errorType = ErrorType.EMAIL_ALREADY_IN_USE;
                                break;
                        }
                        if (generateError != null) {
                            VerifyNewEmailPresenter.this.showInformationModal(errorType);
                            return;
                        }
                    }
                    ((VerifyNewEmailView) VerifyNewEmailPresenter.this.view).displayMessage(ApiError.generateError(th).getMessage());
                }
            }
        });
    }

    public void clearNewEmail() {
        ((AccountProfileInfo) this.model).setNewEmail("");
    }
}
