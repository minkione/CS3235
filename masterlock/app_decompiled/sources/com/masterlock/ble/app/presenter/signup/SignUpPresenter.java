package com.masterlock.ble.app.presenter.signup;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.common.base.Strings;
import com.masterlock.api.entity.EmailPhoneVerificationResponse;
import com.masterlock.api.entity.InvitationValidateResponse;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.activity.SignUpActivity;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.bus.ValidateInvitationCodeEvent;
import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.screens.SignInScreens.SignIn;
import com.masterlock.ble.app.screens.SignUpScreens.PrivacyPolicy;
import com.masterlock.ble.app.screens.SignUpScreens.ResendEmail;
import com.masterlock.ble.app.screens.SignUpScreens.SignUp;
import com.masterlock.ble.app.screens.SignUpScreens.TermsOfService;
import com.masterlock.ble.app.service.ProductInvitationService;
import com.masterlock.ble.app.service.SignUpService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.modal.CountriesModal;
import com.masterlock.ble.app.view.modal.CountriesModal.CountrySelectedListener;
import com.masterlock.ble.app.view.modal.InvalidInvitationCodeDialog;
import com.masterlock.ble.app.view.signup.SignUpView;
import com.masterlock.core.Country;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.android.schedulers.AndroidSchedulers;
import p009rx.schedulers.Schedulers;
import p009rx.subscriptions.Subscriptions;

public class SignUpPresenter extends Presenter<Void, SignUpView> {
    private static final String TAG = "SignUpPresenter";
    /* access modifiers changed from: private */
    public Dialog mDialog;
    @Inject
    Bus mEventBus;
    private Subscription mInvitationCodeValidationSubscription = Subscriptions.empty();
    /* access modifiers changed from: private */
    public MasterLockSharedPreferences mPreferences;
    @Inject
    ProductInvitationService mProductInvitationClient;
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();
    @Inject
    SignUpService signUpService;

    public SignUpPresenter(SignUpView signUpView) {
        super(signUpView);
        MasterLockApp.get().inject(this);
        this.mEventBus.register(this);
    }

    public void start() {
        this.mPreferences = MasterLockSharedPreferences.getInstance();
        SignUp signUp = (SignUp) AppFlow.getScreen(((SignUpView) this.view).getContext());
        ((SignUpView) this.view).setLastInfo(signUp.email, signUp.f171cc, signUp.phone);
        if (SignUpActivity.shouldValidateInvitation) {
            validateInvitationCodeEvent(null);
            SignUpActivity.shouldValidateInvitation = false;
        }
    }

    public void sendEmail(String str, String str2, String str3) {
        this.mSubscription.unsubscribe();
        this.mSubscription = this.signUpService.signUp(str, str2, str3).subscribeOn(Schedulers.m220io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Subscriber<? super T>) new Subscriber<EmailPhoneVerificationResponse>() {
            public void onStart() {
                SignUpPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                SignUpPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                SignUpPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ((SignUpView) SignUpPresenter.this.view).displayError(ApiError.generateError(th));
            }

            public void onNext(EmailPhoneVerificationResponse emailPhoneVerificationResponse) {
                if (emailPhoneVerificationResponse.verificationId != null) {
                    if (!emailPhoneVerificationResponse.error.equals("")) {
                        ((SignUpView) SignUpPresenter.this.view).displayError(ApiError.generateError(new Throwable(emailPhoneVerificationResponse.error.split(ApiError.MESSAGE_DELIMITER)[1])));
                    }
                    AppFlow.get(((SignUpView) SignUpPresenter.this.view).getContext()).goTo(new ResendEmail());
                }
            }
        });
    }

    public void showTermsOfService() {
        AppFlow.get(((SignUpView) this.view).getContext()).goTo(new TermsOfService(false));
    }

    public void showPrivacyPolicy() {
        AppFlow.get(((SignUpView) this.view).getContext()).goTo(new PrivacyPolicy());
    }

    public void signIn() {
        AppFlow.get(((SignUpView) this.view).getContext()).goTo(new SignIn());
    }

    public void finish() {
        super.finish();
        this.mSubscription.unsubscribe();
        this.mInvitationCodeValidationSubscription.unsubscribe();
        Dialog dialog = this.mDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mDialog.dismiss();
        }
        this.signUpService = null;
        this.mEventBus.unregister(this);
    }

    public void showCountriesDialog() {
        CountriesModal countriesModal = new CountriesModal(((SignUpView) this.view).getContext(), null);
        countriesModal.setCountrySelectedListener(new CountrySelectedListener() {
            public void onCountrySelected(Country country) {
                ((SignUpView) SignUpPresenter.this.view).setCountryCode(country);
                SignUpPresenter.this.mDialog.dismiss();
            }

            public void onCloseClicked() {
                SignUpPresenter.this.mDialog.dismiss();
            }
        });
        this.mDialog = new Dialog(((SignUpView) this.view).getContext());
        this.mDialog.requestWindowFeature(1);
        this.mDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mDialog.setContentView(countriesModal);
        this.mDialog.show();
    }

    @Subscribe
    public void validateInvitationCodeEvent(ValidateInvitationCodeEvent validateInvitationCodeEvent) {
        if (AppFlow.getScreen(((SignUpView) this.view).getContext()) instanceof SignUp) {
            String invitationCode = this.mPreferences.getInvitationCode();
            if (!Strings.isNullOrEmpty(invitationCode)) {
                verifyInvitation(invitationCode);
            }
        }
    }

    public void verifyInvitation(final String str) {
        this.mInvitationCodeValidationSubscription.unsubscribe();
        this.mInvitationCodeValidationSubscription = this.mProductInvitationClient.validateInvitation(str).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<InvitationValidateResponse>() {
            public void onStart() {
                ((SignUpView) SignUpPresenter.this.view).enableNavigationButton(false);
            }

            public void onCompleted() {
                ((SignUpView) SignUpPresenter.this.view).enableNavigationButton(true);
            }

            public void onError(Throwable th) {
                ApiError generateError = ApiError.generateError(th);
                if (ApiError.INVALID_INVITATION_CODE.equals(generateError.getCode())) {
                    SignUpPresenter.this.mPreferences.putInvitationCode("");
                    SignUpPresenter.this.showInvalidInvitationDialog();
                } else {
                    ((SignUpView) SignUpPresenter.this.view).displayError(generateError);
                }
                SignUpPresenter.this.mPreferences.putValidInvitationCode("");
                ((SignUpView) SignUpPresenter.this.view).enableNavigationButton(true);
            }

            public void onNext(InvitationValidateResponse invitationValidateResponse) {
                if (!Strings.isNullOrEmpty(invitationValidateResponse.getGuestMail())) {
                    ((SignUpView) SignUpPresenter.this.view).setInvitationEmailAndDisableField(invitationValidateResponse.getGuestMail());
                }
                SignUpPresenter.this.mPreferences.putInvitationCode("");
                SignUpPresenter.this.mPreferences.putValidInvitationCode(str);
            }
        });
    }

    public void showInvalidInvitationDialog() {
        InvalidInvitationCodeDialog invalidInvitationCodeDialog = new InvalidInvitationCodeDialog(((SignUpView) this.view).getContext());
        Dialog dialog = new Dialog(((SignUpView) this.view).getContext());
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.setContentView(invalidInvitationCodeDialog);
        invalidInvitationCodeDialog.setPositiveButtonOnClickListener(new OnClickListener(dialog) {
            private final /* synthetic */ Dialog f$0;

            {
                this.f$0 = r1;
            }

            public final void onClick(View view) {
                this.f$0.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
}
