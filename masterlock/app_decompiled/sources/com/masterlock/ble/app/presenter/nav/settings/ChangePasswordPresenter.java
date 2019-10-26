package com.masterlock.ble.app.presenter.nav.settings;

import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.google.common.base.Strings;
import com.masterlock.api.entity.AuthRequest;
import com.masterlock.api.entity.AuthResponse;
import com.masterlock.api.entity.AuthenticationWithTOSResponseWrapper;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.bus.ChangeSoftKeyboardBehaviorEvent;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.dao.SignUpDAO;
import com.masterlock.ble.app.screens.NavScreens.AccountProfile;
import com.masterlock.ble.app.service.ProductInvitationService;
import com.masterlock.ble.app.service.SignInService;
import com.masterlock.ble.app.util.PasscodeTimeoutTask;
import com.masterlock.ble.app.util.SignUpHelper;
import com.masterlock.ble.app.util.SignUpHelper.PasswordStrength;
import com.masterlock.ble.app.view.modal.BaseModal;
import com.masterlock.ble.app.view.modal.PasswordTipsModal;
import com.masterlock.ble.app.view.nav.settings.ChangePasswordView;
import com.masterlock.core.AccountProfileInfo;
import com.masterlock.core.ProfileUpdateFields;
import com.square.flow.appflow.AppFlow;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.android.schedulers.AndroidSchedulers;
import p009rx.functions.Action1;
import p009rx.schedulers.Schedulers;
import p009rx.subscriptions.Subscriptions;
import retrofit.client.Response;

public class ChangePasswordPresenter extends ProfileUpdateBasePresenter<AccountProfileInfo, ChangePasswordView> {
    private PasswordStrength currentPasswordStrength;
    private Dialog mDialog;
    @Inject
    ProductInvitationService mProductInvitationService;
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
            ChangePasswordPresenter.this.validatePassword(editable.toString());
        }
    };

    private enum ErrorType {
        IDENTICAL_PASSWORD,
        NEW_PASSWORD_DONT_MATCH,
        CURRENT_PASSWORD_INCORRECT
    }

    private void processResponse(AuthResponse authResponse) {
    }

    public ChangePasswordPresenter(ChangePasswordView changePasswordView) {
        super(changePasswordView);
    }

    public void start() {
        super.start();
        this.mEventBus.post(new ChangeSoftKeyboardBehaviorEvent(16));
    }

    public void finish() {
        this.mSubscription.unsubscribe();
        this.mEventBus.post(new ChangeSoftKeyboardBehaviorEvent(32));
        super.finish();
    }

    public void changePassword(String str, String str2, String str3) {
        if (str.equals(str2)) {
            showErrorDialog(ErrorType.IDENTICAL_PASSWORD);
        } else if (!str2.equals(str3)) {
            showErrorDialog(ErrorType.NEW_PASSWORD_DONT_MATCH);
        } else {
            ((AccountProfileInfo) this.model).setCurrentPassword(str);
            ((AccountProfileInfo) this.model).setNewPassword(str2);
            ((AccountProfileInfo) this.model).setFieldToUpdate(ProfileUpdateFields.PASSWORD);
            performProfileUpdate(new Subscriber<Response>() {
                public void onNext(Response response) {
                }

                public void onCompleted() {
                    ((ChangePasswordView) ChangePasswordPresenter.this.view).displayMessage((int) C1075R.string.profile_setting_change_password_success);
                    MasterLockApp.get().logOut();
                }

                public void onError(Throwable th) {
                    ApiError generateError = ApiError.generateError(th);
                    if (Strings.isNullOrEmpty(generateError.getCode()) || !ApiError.CURRENT_PASSWORD_INCORRECT.equals(generateError.getCode())) {
                        ((ChangePasswordView) ChangePasswordPresenter.this.view).displayMessage(generateError.getMessage());
                    } else {
                        ChangePasswordPresenter.this.showErrorDialog(ErrorType.CURRENT_PASSWORD_INCORRECT);
                    }
                }
            });
        }
    }

    private void reauthenticate(final String str) {
        AuthRequest authRequest = new AuthRequest();
        authRequest.username = this.prefs.getUsername();
        authRequest.passcode = str;
        this.mSubscription.unsubscribe();
        this.mSubscription = this.mSignInService.signIn(authRequest).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<AuthenticationWithTOSResponseWrapper>() {
            public void onStart() {
                ChangePasswordPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                AppFlow.get(((ChangePasswordView) ChangePasswordPresenter.this.view).getContext()).resetTo(new AccountProfile());
            }

            public void onError(Throwable th) {
                ChangePasswordPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ((ChangePasswordView) ChangePasswordPresenter.this.view).displayError(ApiError.generateError(th));
            }

            public void onNext(AuthenticationWithTOSResponseWrapper authenticationWithTOSResponseWrapper) {
                ChangePasswordPresenter.this.prefs.putAuthToken(authenticationWithTOSResponseWrapper.getAuthResponse().token);
                ChangePasswordPresenter.this.prefs.putEncryptedAuthToken(authenticationWithTOSResponseWrapper.getAuthResponse().token, str);
            }
        });
    }

    /* access modifiers changed from: private */
    public void validatePassword(String str) {
        boolean isPasswordInRange = this.mSignUpHelper.isPasswordInRange(str.length());
        ((ChangePasswordView) this.view).setPasswordInRange(isPasswordInRange);
        ((ChangePasswordView) this.view).toggleContinueButton();
        if (!isPasswordInRange) {
            ((ChangePasswordView) this.view).updatePasswordStrengthLabel(null);
            return;
        }
        this.mValidatePasswordSubscription.unsubscribe();
        this.mValidatePasswordSubscription = this.mSignUpHelper.validatePassword(str, this.mSignUpDAO).subscribeOn(Schedulers.m220io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Action1<? super T>) new Action1() {
            public final void call(Object obj) {
                ChangePasswordPresenter.lambda$validatePassword$0(ChangePasswordPresenter.this, (PasswordStrength) obj);
            }
        }, (Action1<Throwable>) new Action1() {
            public final void call(Object obj) {
                ChangePasswordPresenter.lambda$validatePassword$1(ChangePasswordPresenter.this, (Throwable) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$validatePassword$0(ChangePasswordPresenter changePasswordPresenter, PasswordStrength passwordStrength) {
        Log.d(changePasswordPresenter.getClass().getSimpleName(), "onNext: ");
        changePasswordPresenter.currentPasswordStrength = passwordStrength;
        ((ChangePasswordView) changePasswordPresenter.view).updatePasswordStrengthLabel(changePasswordPresenter.currentPasswordStrength);
    }

    public static /* synthetic */ void lambda$validatePassword$1(ChangePasswordPresenter changePasswordPresenter, Throwable th) {
        String simpleName = changePasswordPresenter.getClass().getSimpleName();
        StringBuilder sb = new StringBuilder();
        sb.append("onError: ");
        sb.append(th.getMessage());
        Log.d(simpleName, sb.toString());
    }

    public void schedulePasscodeTimeout() {
        new ScheduledThreadPoolExecutor(1).schedule(new PasscodeTimeoutTask(), 15, TimeUnit.MINUTES);
    }

    public void setEditTextTextWatchers(EditText editText, EditText... editTextArr) {
        editText.addTextChangedListener(this.passwordTextWatcher);
        C12823 r4 = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                ((ChangePasswordView) ChangePasswordPresenter.this.view).toggleContinueButton();
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
        PasswordTipsModal passwordTipsModal = new PasswordTipsModal(((ChangePasswordView) this.view).getContext(), null);
        this.mDialog = new Dialog(((ChangePasswordView) this.view).getContext());
        this.mDialog.requestWindowFeature(1);
        this.mDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mDialog.setContentView(passwordTipsModal);
        passwordTipsModal.initialize(this.mSignUpHelper.getPasswordCriteriaMap());
        passwordTipsModal.setPositiveButtonClickListener(new OnClickListener() {
            public final void onClick(View view) {
                ChangePasswordPresenter.this.mDialog.dismiss();
            }
        });
        this.mDialog.show();
    }

    public SignUpHelper getSignUpHelper() {
        return this.mSignUpHelper;
    }

    /* access modifiers changed from: private */
    public void showErrorDialog(ErrorType errorType) {
        BaseModal baseModal = new BaseModal(((ChangePasswordView) this.view).getContext());
        Dialog dialog = new Dialog(((ChangePasswordView) this.view).getContext());
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.requestWindowFeature(1);
        dialog.setContentView(baseModal);
        dialog.setCancelable(false);
        String str = "";
        String str2 = "";
        switch (errorType) {
            case IDENTICAL_PASSWORD:
                str = ((ChangePasswordView) this.view).getResources().getString(C1075R.string.profile_setting_change_password_error_identical_title);
                str2 = ((ChangePasswordView) this.view).getResources().getString(C1075R.string.profile_setting_change_password_error_identical);
                break;
            case NEW_PASSWORD_DONT_MATCH:
                str = ((ChangePasswordView) this.view).getResources().getString(C1075R.string.profile_setting_change_password_error_dont_match_title);
                str2 = ((ChangePasswordView) this.view).getResources().getString(C1075R.string.profile_setting_change_password_error_dont_match);
                break;
            case CURRENT_PASSWORD_INCORRECT:
                str = ((ChangePasswordView) this.view).getResources().getString(C1075R.string.profile_setting_change_password_error_current_title);
                str2 = ((ChangePasswordView) this.view).getResources().getString(C1075R.string.profile_setting_change_password_error_current);
                break;
        }
        baseModal.setTitle(str);
        baseModal.setBody(str2);
        baseModal.getPositiveButton().setText(((ChangePasswordView) this.view).getContext().getResources().getString(C1075R.string.f165ok));
        baseModal.setPositiveButtonClickListener(new OnClickListener(dialog) {
            private final /* synthetic */ Dialog f$0;

            {
                this.f$0 = r1;
            }

            public final void onClick(View view) {
                this.f$0.dismiss();
            }
        });
        baseModal.getNegativeButton().setVisibility(8);
        dialog.show();
    }
}
