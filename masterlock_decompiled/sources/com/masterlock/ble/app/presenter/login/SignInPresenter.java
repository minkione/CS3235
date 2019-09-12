package com.masterlock.ble.app.presenter.login;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.common.base.Strings;
import com.masterlock.api.entity.AuthRequest;
import com.masterlock.api.entity.AuthenticationWithTOSResponseWrapper;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.activity.LockActivity;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.provider.MasterlockDatabase;
import com.masterlock.ble.app.screens.SignInScreens.ForgotPasscode;
import com.masterlock.ble.app.screens.SignInScreens.ForgotUsername;
import com.masterlock.ble.app.screens.SignUpScreens.PrivacyPolicy;
import com.masterlock.ble.app.screens.SignUpScreens.SignUp;
import com.masterlock.ble.app.screens.SignUpScreens.TermsOfService;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.ProductInvitationService;
import com.masterlock.ble.app.service.SignInService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.util.IntentUtil;
import com.masterlock.ble.app.view.modal.BaseModal;
import com.masterlock.ble.app.view.modal.InvalidInvitationCodeDialog;
import com.masterlock.ble.app.view.signin.SignInView;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import java.util.List;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.android.schedulers.AndroidSchedulers;
import p009rx.schedulers.Schedulers;
import p009rx.subscriptions.Subscriptions;
import retrofit.client.Response;

public class SignInPresenter extends Presenter<AuthRequest, SignInView> {
    private MasterlockDatabase database;
    private Subscription mAcceptInvitationSubscription = Subscriptions.empty();
    @Inject
    Bus mEventBus;
    private Subscription mGetProductsSubscription = Subscriptions.empty();
    @Inject
    LockService mLockService;
    /* access modifiers changed from: private */
    public MasterLockSharedPreferences mPreferences;
    @Inject
    ProductInvitationService mProductInvitationService;
    @Inject
    IScheduler mScheduler;
    @Inject
    SignInService mSignInService;
    private Subscription mSignInSubscription = Subscriptions.empty();

    public SignInPresenter(SignInView signInView) {
        super(new AuthRequest(), signInView);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        this.mPreferences = MasterLockSharedPreferences.getInstance();
    }

    public void signIn(String str, String str2) {
        ((AuthRequest) this.model).username = str;
        ((AuthRequest) this.model).passcode = str2;
        this.mSignInSubscription.unsubscribe();
        this.mSignInSubscription = this.mSignInService.signIn((AuthRequest) this.model).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<AuthenticationWithTOSResponseWrapper>() {
            public void onCompleted() {
            }

            public void onStart() {
                SignInPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onError(Throwable th) {
                SignInPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ((SignInView) SignInPresenter.this.view).displayError(ApiError.generateError(th));
            }

            public void onNext(AuthenticationWithTOSResponseWrapper authenticationWithTOSResponseWrapper) {
                LockActivity.setMustRequestPermissions(true);
                MasterLockSharedPreferences instance = MasterLockSharedPreferences.getInstance();
                instance.putAuthToken(authenticationWithTOSResponseWrapper.getAuthResponse().token);
                instance.putEncryptedAuthToken(authenticationWithTOSResponseWrapper.getAuthResponse().token, ((AuthRequest) SignInPresenter.this.model).passcode);
                instance.putUsername(authenticationWithTOSResponseWrapper.getAuthResponse().userName);
                instance.putUserFirstName(authenticationWithTOSResponseWrapper.getAuthResponse().userFirstName);
                instance.putUserLastName(authenticationWithTOSResponseWrapper.getAuthResponse().userLastName);
                instance.putUserEmail(authenticationWithTOSResponseWrapper.getAuthResponse().email);
                instance.putUserPhoneNumber(authenticationWithTOSResponseWrapper.getAuthResponse().mobilePhoneNumber);
                instance.putUserPhoneCC(authenticationWithTOSResponseWrapper.getAuthResponse().phoneCountryCode);
                instance.putUserAlphaCC(authenticationWithTOSResponseWrapper.getAuthResponse().alphaCountyCode);
                instance.putUserPhoneIsVerified(authenticationWithTOSResponseWrapper.getAuthResponse().mobilePhoneNumberIsVerified);
                instance.putUserTimeZone(authenticationWithTOSResponseWrapper.getAuthResponse().timeZone);
                if (authenticationWithTOSResponseWrapper.getAuthResponse().isTermsOfServiceCurrent) {
                    instance.putAcceptedTermsOfServiceVersion(authenticationWithTOSResponseWrapper.getTermsOfService().getVersion());
                } else {
                    instance.putAcceptedTermsOfServiceVersion(-1);
                }
                String validInvitationCode = SignInPresenter.this.mPreferences.getValidInvitationCode();
                if (!Strings.isNullOrEmpty(validInvitationCode)) {
                    SignInPresenter.this.acceptInvitation(validInvitationCode);
                } else {
                    SignInPresenter.this.getProducts();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void getProducts() {
        this.mGetProductsSubscription.unsubscribe();
        this.mGetProductsSubscription = this.mLockService.getProducts().subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<List<Lock>>() {
            public void onStart() {
                SignInPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                SignInPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                ((SignInView) SignInPresenter.this.view).displayError(ApiError.generateError(th));
                SignInPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onNext(List<Lock> list) {
                SignInPresenter.this.transitionToNext();
            }
        });
    }

    /* access modifiers changed from: private */
    public void transitionToNext() {
        MasterLockSharedPreferences.getInstance().putCanManageLock(true);
        LockActivity.restartPasscodeTimer();
        Intent intent = new Intent(((SignInView) this.view).getContext(), LockActivity.class);
        intent.setFlags(IntentUtil.CLEAR_STACK);
        ((SignInView) this.view).getContext().startActivity(intent);
    }

    /* access modifiers changed from: private */
    public void acceptInvitation(String str) {
        this.mAcceptInvitationSubscription.unsubscribe();
        this.mAcceptInvitationSubscription = this.mProductInvitationService.acceptInvitation(str).subscribeOn(Schedulers.m220io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Subscriber<? super T>) new Subscriber<Response>() {
            public void onCompleted() {
            }

            public void onError(Throwable th) {
                ApiError generateError = ApiError.generateError(th);
                if (ApiError.USER_ALREADY_HAS_ACCESS.equals(generateError.getCode())) {
                    SignInPresenter.this.showAcceptInvitationErrorModal();
                    SignInPresenter.this.mPreferences.putValidInvitationCode("");
                    SignInPresenter.this.getProducts();
                } else if (ApiError.INVALID_INVITATION_CODE.equals(generateError.getCode())) {
                    SignInPresenter.this.showInvalidInvitationDialog();
                    SignInPresenter.this.mPreferences.putValidInvitationCode("");
                    SignInPresenter.this.getProducts();
                } else {
                    ((SignInView) SignInPresenter.this.view).displayError(generateError);
                }
            }

            public void onNext(Response response) {
                SignInPresenter.this.mPreferences.putValidInvitationCode("");
                SignInPresenter.this.getProducts();
            }
        });
    }

    /* access modifiers changed from: private */
    public void showAcceptInvitationErrorModal() {
        BaseModal baseModal = new BaseModal(((SignInView) this.view).getContext());
        Dialog dialog = new Dialog(((SignInView) this.view).getContext());
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.requestWindowFeature(1);
        dialog.setContentView(baseModal);
        dialog.setCancelable(false);
        $$Lambda$SignInPresenter$CzDOg21gC_4x_c1hWiLBdEHn1E r2 = new OnClickListener(dialog) {
            private final /* synthetic */ Dialog f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                SignInPresenter.lambda$showAcceptInvitationErrorModal$0(SignInPresenter.this, this.f$1, view);
            }
        };
        baseModal.setTitle(((SignInView) this.view).getResources().getString(C1075R.string.error));
        baseModal.setBody(((SignInView) this.view).getResources().getString(C1075R.string.error_access_invitation_message));
        baseModal.getPositiveButton().setText(((SignInView) this.view).getContext().getResources().getString(C1075R.string.accept_button));
        baseModal.setPositiveButtonClickListener(r2);
        baseModal.getNegativeButton().setVisibility(8);
        dialog.show();
    }

    public static /* synthetic */ void lambda$showAcceptInvitationErrorModal$0(SignInPresenter signInPresenter, Dialog dialog, View view) {
        dialog.dismiss();
        signInPresenter.getProducts();
    }

    public void showInvalidInvitationDialog() {
        InvalidInvitationCodeDialog invalidInvitationCodeDialog = new InvalidInvitationCodeDialog(((SignInView) this.view).getContext());
        Dialog dialog = new Dialog(((SignInView) this.view).getContext());
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

    public void signUp() {
        AppFlow.get(((SignInView) this.view).getContext()).resetTo(new SignUp());
    }

    public void forgotUsername() {
        AppFlow.get(((SignInView) this.view).getContext()).goTo(new ForgotUsername());
    }

    public void forgotPasscode() {
        AppFlow.get(((SignInView) this.view).getContext()).goTo(new ForgotPasscode());
    }

    public void finish() {
        super.finish();
        this.mGetProductsSubscription.unsubscribe();
        this.mSignInSubscription.unsubscribe();
        this.mAcceptInvitationSubscription.unsubscribe();
        this.mSignInService = null;
    }

    public void showTermsOfService() {
        AppFlow.get(((SignInView) this.view).getContext()).goTo(new TermsOfService(false));
    }

    public void showPrivacyPolicy() {
        AppFlow.get(((SignInView) this.view).getContext()).goTo(new PrivacyPolicy());
    }
}
