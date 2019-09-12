package com.masterlock.ble.app.util;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.crypto.PBKDF2Encryptor;
import com.masterlock.ble.app.service.SignInService;
import com.masterlock.ble.app.view.modal.ForgotPasscodeDialog;
import com.masterlock.ble.app.view.modal.ManageLockDialog;
import com.squareup.otto.Bus;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import javax.inject.Inject;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class PasscodeUtil {
    private Dialog mDialog;
    @Inject
    Bus mEventBus;
    private Subscription mPasscodeSubscription = Subscriptions.empty();
    @Inject
    IScheduler mScheduler;
    @Inject
    SignInService mSignUpService;
    private Subscription mSubscription = Subscriptions.empty();
    final ManageLockDialog manageLockDialogView;
    private MasterLockSharedPreferences prefs;
    private View view;

    public PasscodeUtil(View view2) {
        this.view = view2;
        MasterLockApp.get().inject(this);
        this.manageLockDialogView = new ManageLockDialog(view2.getContext(), null);
        this.mDialog = new Dialog(view2.getContext());
        this.mDialog.requestWindowFeature(1);
        this.mDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mDialog.getWindow().setSoftInputMode(5);
        this.mDialog.setContentView(this.manageLockDialogView);
    }

    public Dialog getDialog() {
        return this.mDialog;
    }

    public ManageLockDialog getManageLockDialog() {
        return this.manageLockDialogView;
    }

    public Observable<Void> verifyPasscode(final ManageLockDialog manageLockDialog) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Void>() {
            public void call(Subscriber<? super Void> subscriber) {
                ThreadUtil.errorOnUIThread();
                ManageLockDialog manageLockDialog = manageLockDialog;
                if (manageLockDialog != null) {
                    String passcode = manageLockDialog.getPasscode();
                    try {
                        new PBKDF2Encryptor().decrypt(MasterLockSharedPreferences.getInstance().getEncryptedAuthToken(), passcode);
                        subscriber.onCompleted();
                    } catch (UnsupportedEncodingException | IllegalArgumentException | GeneralSecurityException e) {
                        subscriber.onError(e);
                    }
                }
            }
        });
    }

    public void forgotPasscode() {
        View view2 = this.view;
        if (view2 != null) {
            ForgotPasscodeDialog forgotPasscodeDialog = new ForgotPasscodeDialog(view2.getContext(), null);
            this.mDialog = new Dialog(this.view.getContext());
            this.mDialog.requestWindowFeature(1);
            this.mDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
            this.mDialog.getWindow().setSoftInputMode(5);
            this.mDialog.setContentView(forgotPasscodeDialog);
            forgotPasscodeDialog.setPositiveButtonClickListener(new OnClickListener(forgotPasscodeDialog, this.mDialog) {
                private final /* synthetic */ ForgotPasscodeDialog f$1;
                private final /* synthetic */ Dialog f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                public final void onClick(View view) {
                    PasscodeUtil.lambda$forgotPasscode$0(PasscodeUtil.this, this.f$1, this.f$2, view);
                }
            });
            forgotPasscodeDialog.setNegativeButtonClickListener(new OnClickListener(forgotPasscodeDialog) {
                private final /* synthetic */ ForgotPasscodeDialog f$1;

                {
                    this.f$1 = r2;
                }

                public final void onClick(View view) {
                    PasscodeUtil.lambda$forgotPasscode$1(PasscodeUtil.this, this.f$1, view);
                }
            });
            this.mDialog.show();
        }
    }

    public static /* synthetic */ void lambda$forgotPasscode$0(PasscodeUtil passcodeUtil, ForgotPasscodeDialog forgotPasscodeDialog, Dialog dialog, View view2) {
        if (forgotPasscodeDialog.isValid()) {
            forgotPasscodeDialog.showLoading(true);
            passcodeUtil.mPasscodeSubscription = passcodeUtil.mSignUpService.forgotPasscode(forgotPasscodeDialog.getEmailAddress()).subscribeOn(passcodeUtil.mScheduler.background()).observeOn(passcodeUtil.mScheduler.main()).subscribe(passcodeUtil.createForgotPasscodeSubscriber(forgotPasscodeDialog, dialog));
            return;
        }
        forgotPasscodeDialog.showLoading(false);
    }

    public static /* synthetic */ void lambda$forgotPasscode$1(PasscodeUtil passcodeUtil, ForgotPasscodeDialog forgotPasscodeDialog, View view2) {
        ViewUtil.hideKeyboard(passcodeUtil.mDialog.getContext(), forgotPasscodeDialog.getWindowToken());
        passcodeUtil.mDialog.dismiss();
    }

    private Subscriber<Boolean> createForgotPasscodeSubscriber(final ForgotPasscodeDialog forgotPasscodeDialog, final Dialog dialog) {
        return new Subscriber<Boolean>() {
            public void onNext(Boolean bool) {
            }

            public void onCompleted() {
                forgotPasscodeDialog.showLoading(false);
                forgotPasscodeDialog.toastSuccess();
                ViewUtil.hideKeyboard(dialog.getContext(), forgotPasscodeDialog.getWindowToken());
                dialog.dismiss();
            }

            public void onError(Throwable th) {
                ApiError generateError = ApiError.generateError(th);
                if (!generateError.isHandled()) {
                    forgotPasscodeDialog.showLoading(false);
                    forgotPasscodeDialog.showError(generateError.getMessage());
                    return;
                }
                dialog.dismiss();
            }
        };
    }
}
