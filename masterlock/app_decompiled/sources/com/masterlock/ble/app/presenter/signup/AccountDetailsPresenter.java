package com.masterlock.ble.app.presenter.signup;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.google.common.base.Strings;
import com.masterlock.api.entity.AuthResponse;
import com.masterlock.api.entity.AuthenticationWithTOSResponseWrapper;
import com.masterlock.api.entity.CreateAccountRequest;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.activity.LockActivity;
import com.masterlock.ble.app.bus.ChangeSoftKeyboardBehaviorEvent;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.dao.SignUpDAO;
import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.screens.SignInScreens.SignIn;
import com.masterlock.ble.app.service.ProductInvitationService;
import com.masterlock.ble.app.service.SignInService;
import com.masterlock.ble.app.service.SignUpService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.util.IntentUtil;
import com.masterlock.ble.app.util.PasscodeTimeoutTask;
import com.masterlock.ble.app.util.SignUpHelper;
import com.masterlock.ble.app.util.SignUpHelper.PasswordStrength;
import com.masterlock.ble.app.view.modal.BaseModal;
import com.masterlock.ble.app.view.modal.PasswordTipsModal;
import com.masterlock.ble.app.view.signup.AccountDetailsView;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import java.util.TimeZone;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.android.schedulers.AndroidSchedulers;
import p009rx.schedulers.Schedulers;
import p009rx.subscriptions.Subscriptions;
import retrofit.client.Response;

public class AccountDetailsPresenter extends Presenter<CreateAccountRequest, AccountDetailsView> {
    /* access modifiers changed from: private */
    public PasswordStrength currentPasswordStrength;
    private Subscription mAcceptInvitationSubscription = Subscriptions.empty();
    private Dialog mDialog;
    @Inject
    Bus mEventBus;
    /* access modifiers changed from: private */
    public MasterLockSharedPreferences mPreferences;
    @Inject
    ProductInvitationService mProductInvitationService;
    @Inject
    IScheduler mScheduler;
    @Inject
    SignInService mSignInService;
    @Inject
    SignUpDAO mSignUpDAO;
    @Inject
    SignUpHelper mSignUpHelper;
    private Subscription mSubscription = Subscriptions.empty();
    private Subscription mValidatePasswordSubscription = Subscriptions.empty();
    private TextWatcher passwordTextWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void afterTextChanged(Editable editable) {
            AccountDetailsPresenter.this.validatePassword(editable.toString());
        }
    };
    @Inject
    SignUpService signUpService;

    public AccountDetailsPresenter(AccountDetailsView accountDetailsView) {
        super(accountDetailsView);
        MasterLockApp.get().inject(this);
        this.mEventBus.register(this);
        this.mPreferences = MasterLockSharedPreferences.getInstance();
    }

    public void start() {
        this.model = new CreateAccountRequest();
        ((CreateAccountRequest) this.model).emailVerificationId = this.mPreferences.getVerificationId();
        this.mEventBus.post(new ChangeSoftKeyboardBehaviorEvent(16));
    }

    public void finish() {
        super.finish();
        this.mSubscription.unsubscribe();
        this.mEventBus.post(new ChangeSoftKeyboardBehaviorEvent(32));
        this.mEventBus.unregister(this);
    }

    public void createAccount(String str, String str2, String str3, String str4) {
        ((CreateAccountRequest) this.model).username = str;
        ((CreateAccountRequest) this.model).firstName = str2;
        ((CreateAccountRequest) this.model).lastName = str3;
        ((CreateAccountRequest) this.model).passcode = str4;
        ((CreateAccountRequest) this.model).securityQuestion = ((AccountDetailsView) this.view).getContext().getString(C1075R.string.ml_security_question);
        ((CreateAccountRequest) this.model).securityAnswer = ((AccountDetailsView) this.view).getContext().getString(C1075R.string.ml_security_answer);
        ((CreateAccountRequest) this.model).emailOptIn = false;
        ((CreateAccountRequest) this.model).timezone = TimeZone.getDefault().getID();
        this.mSubscription.unsubscribe();
        this.mSubscription = this.signUpService.createAccount((CreateAccountRequest) this.model).subscribeOn(Schedulers.m220io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Subscriber<? super T>) new Subscriber<AuthResponse>() {
            public void onCompleted() {
            }

            public void onStart() {
                AccountDetailsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onError(Throwable th) {
                AccountDetailsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ((AccountDetailsView) AccountDetailsPresenter.this.view).displayError(ApiError.generateError(th));
            }

            public void onNext(AuthResponse authResponse) {
                LockActivity.setMustRequestPermissions(true);
                AccountDetailsPresenter.this.processResponse(authResponse);
                AccountDetailsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                AccountDetailsPresenter.this.schedulePasscodeTimeout();
                MasterLockSharedPreferences.getInstance().putCanManageLock(true);
                AccountDetailsPresenter.this.termsOfServiceValidation();
                String validInvitationCode = AccountDetailsPresenter.this.mPreferences.getValidInvitationCode();
                if (!Strings.isNullOrEmpty(validInvitationCode)) {
                    AccountDetailsPresenter.this.acceptInvitation(validInvitationCode);
                } else {
                    AccountDetailsPresenter.this.transitionToNext();
                }
            }
        });
    }

    public void termsOfServiceValidation() {
        this.mSubscription.unsubscribe();
        this.mSubscription = this.mSignInService.createAuthWrapper().subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<AuthenticationWithTOSResponseWrapper>() {
            public void onCompleted() {
            }

            public void onStart() {
                AccountDetailsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onError(Throwable th) {
                AccountDetailsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ((AccountDetailsView) AccountDetailsPresenter.this.view).displayError(ApiError.generateError(th));
            }

            public void onNext(AuthenticationWithTOSResponseWrapper authenticationWithTOSResponseWrapper) {
                MasterLockSharedPreferences.getInstance().putAcceptedTermsOfServiceVersion(authenticationWithTOSResponseWrapper.getTermsOfService().getVersion());
            }
        });
    }

    /* access modifiers changed from: private */
    public void processResponse(AuthResponse authResponse) {
        MasterLockSharedPreferences instance = MasterLockSharedPreferences.getInstance();
        instance.putAuthToken(authResponse.token);
        instance.putEncryptedAuthToken(authResponse.token, ((CreateAccountRequest) this.model).passcode);
        instance.putUsername(((CreateAccountRequest) this.model).username);
        instance.putUserFirstName(authResponse.userFirstName);
        instance.putUserLastName(authResponse.userLastName);
        instance.putUserEmail(authResponse.email);
        instance.putUserPhoneNumber(authResponse.mobilePhoneNumber);
        instance.putUserPhoneCC(authResponse.phoneCountryCode);
        instance.putUserAlphaCC(authResponse.alphaCountyCode);
        instance.putUserPhoneIsVerified(authResponse.mobilePhoneNumberIsVerified);
        instance.putUserTimeZone(authResponse.timeZone);
    }

    /* access modifiers changed from: private */
    public void transitionToNext() {
        this.mPreferences.clearSignUp();
        MasterLockSharedPreferences.getInstance().putCanManageLock(true);
        LockActivity.restartPasscodeTimer();
        Intent intent = new Intent(((AccountDetailsView) this.view).getContext(), LockActivity.class);
        intent.setFlags(IntentUtil.CLEAR_STACK);
        ((AccountDetailsView) this.view).getContext().startActivity(intent);
    }

    public void signIn() {
        AppFlow.get(((AccountDetailsView) this.view).getContext()).goTo(new SignIn());
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
                    AccountDetailsPresenter.this.showAcceptInvitationErrorModal();
                    AccountDetailsPresenter.this.mPreferences.putValidInvitationCode("");
                    return;
                }
                ((AccountDetailsView) AccountDetailsPresenter.this.view).displayError(generateError);
            }

            public void onNext(Response response) {
                AccountDetailsPresenter.this.mPreferences.putValidInvitationCode("");
                AccountDetailsPresenter.this.transitionToNext();
            }
        });
    }

    /* access modifiers changed from: private */
    public void showAcceptInvitationErrorModal() {
        BaseModal baseModal = new BaseModal(((AccountDetailsView) this.view).getContext());
        baseModal.setTitle(((AccountDetailsView) this.view).getResources().getString(C1075R.string.error));
        Dialog dialog = new Dialog(((AccountDetailsView) this.view).getContext());
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.requestWindowFeature(1);
        dialog.setContentView(baseModal);
        dialog.setCancelable(false);
        $$Lambda$AccountDetailsPresenter$B2Ckht8E8JsQgeEvKvgpKLySEzw r2 = new OnClickListener(dialog) {
            private final /* synthetic */ Dialog f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                AccountDetailsPresenter.lambda$showAcceptInvitationErrorModal$0(AccountDetailsPresenter.this, this.f$1, view);
            }
        };
        baseModal.setBody(((AccountDetailsView) this.view).getResources().getString(C1075R.string.error_access_invitation_message));
        baseModal.getPositiveButton().setText(((AccountDetailsView) this.view).getContext().getResources().getString(C1075R.string.accept_button));
        baseModal.setPositiveButtonClickListener(r2);
        baseModal.getNegativeButton().setVisibility(8);
        dialog.show();
    }

    public static /* synthetic */ void lambda$showAcceptInvitationErrorModal$0(AccountDetailsPresenter accountDetailsPresenter, Dialog dialog, View view) {
        dialog.dismiss();
        accountDetailsPresenter.transitionToNext();
    }

    /* access modifiers changed from: private */
    public void validatePassword(String str) {
        boolean isPasswordInRange = this.mSignUpHelper.isPasswordInRange(str.length());
        ((AccountDetailsView) this.view).setPasswordInRange(isPasswordInRange);
        ((AccountDetailsView) this.view).toggleContinueButton();
        if (!isPasswordInRange) {
            ((AccountDetailsView) this.view).updatePasswordStrengthLabel(null);
            return;
        }
        this.mValidatePasswordSubscription.unsubscribe();
        this.mValidatePasswordSubscription = this.mSignUpHelper.validatePassword(str, this.mSignUpDAO).subscribeOn(Schedulers.m220io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Subscriber<? super T>) new Subscriber<PasswordStrength>() {
            public void onCompleted() {
            }

            public void onError(Throwable th) {
                String simpleName = getClass().getSimpleName();
                StringBuilder sb = new StringBuilder();
                sb.append("onError: ");
                sb.append(th.getMessage());
                Log.d(simpleName, sb.toString());
            }

            public void onNext(PasswordStrength passwordStrength) {
                Log.d(getClass().getSimpleName(), "onNext: ");
                AccountDetailsPresenter.this.currentPasswordStrength = passwordStrength;
                ((AccountDetailsView) AccountDetailsPresenter.this.view).updatePasswordStrengthLabel(AccountDetailsPresenter.this.currentPasswordStrength);
            }
        });
    }

    public void schedulePasscodeTimeout() {
        new ScheduledThreadPoolExecutor(1).schedule(new PasscodeTimeoutTask(), 15, TimeUnit.MINUTES);
    }

    public void setEditTextTextWatchers(EditText editText, EditText... editTextArr) {
        editText.addTextChangedListener(this.passwordTextWatcher);
        C13445 r4 = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                ((AccountDetailsView) AccountDetailsPresenter.this.view).toggleContinueButton();
            }
        };
        for (EditText addTextChangedListener : editTextArr) {
            addTextChangedListener.addTextChangedListener(r4);
        }
    }

    public void removePasswordTextWatcher(EditText editText) {
        editText.removeTextChangedListener(this.passwordTextWatcher);
    }

    public void showPasswordTipsModal() {
        PasswordTipsModal passwordTipsModal = new PasswordTipsModal(((AccountDetailsView) this.view).getContext(), null);
        this.mDialog = new Dialog(((AccountDetailsView) this.view).getContext());
        this.mDialog.requestWindowFeature(1);
        this.mDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mDialog.setContentView(passwordTipsModal);
        passwordTipsModal.initialize(this.mSignUpHelper.getPasswordCriteriaMap());
        passwordTipsModal.setPositiveButtonClickListener(new OnClickListener() {
            public final void onClick(View view) {
                AccountDetailsPresenter.this.mDialog.dismiss();
            }
        });
        this.mDialog.show();
    }

    public SignUpHelper getSignUpHelper() {
        return this.mSignUpHelper;
    }
}
