package com.masterlock.ble.app.presenter.nav;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.activity.LockActivity;
import com.masterlock.ble.app.crypto.PBKDF2Encryptor;
import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.screens.NavScreens.AccountProfile;
import com.masterlock.ble.app.screens.NavScreens.ChangeLanguage;
import com.masterlock.ble.app.screens.NavScreens.NotificationsOptions;
import com.masterlock.ble.app.service.SignInService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.util.ThreadUtil;
import com.masterlock.ble.app.util.ViewUtil;
import com.masterlock.ble.app.view.modal.ForgotPasscodeDialog;
import com.masterlock.ble.app.view.modal.ManageLockDialog;
import com.masterlock.ble.app.view.nav.AccountSettingsView;
import com.square.flow.appflow.AppFlow;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import javax.inject.Inject;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class AccountSettingsPresenter extends Presenter<Void, AccountSettingsView> {
    /* access modifiers changed from: private */
    public Dialog mDialog;
    private Subscription mPasscodeSubscription = Subscriptions.empty();
    @Inject
    IScheduler mScheduler;
    @Inject
    SignInService mSignUpService;

    public void start() {
    }

    public AccountSettingsPresenter(AccountSettingsView accountSettingsView) {
        super(accountSettingsView);
        MasterLockApp.get().inject(this);
    }

    public void finish() {
        super.finish();
    }

    public void verifyPassword(int i) {
        if (MasterLockSharedPreferences.getInstance().canManageLock()) {
            ((AccountSettingsView) this.view).goToSelectedItemScreen(i);
            return;
        }
        Dialog dialog = this.mDialog;
        if (dialog != null) {
            ViewUtil.hideKeyboard(dialog.getContext(), ((AccountSettingsView) this.view).getWindowToken());
            this.mDialog.dismiss();
        }
        ManageLockDialog manageLockDialog = new ManageLockDialog(((AccountSettingsView) this.view).getContext(), null);
        this.mDialog = new Dialog(((AccountSettingsView) this.view).getContext());
        this.mDialog.requestWindowFeature(1);
        this.mDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mDialog.getWindow().setSoftInputMode(5);
        this.mDialog.setContentView(manageLockDialog);
        manageLockDialog.setPositiveButtonClickListener(new OnClickListener(manageLockDialog, i) {
            private final /* synthetic */ ManageLockDialog f$1;
            private final /* synthetic */ int f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void onClick(View view) {
                AccountSettingsPresenter.lambda$verifyPassword$0(AccountSettingsPresenter.this, this.f$1, this.f$2, view);
            }
        });
        manageLockDialog.setNegativeButtonClickListener(new OnClickListener(manageLockDialog) {
            private final /* synthetic */ ManageLockDialog f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                AccountSettingsPresenter.lambda$verifyPassword$1(AccountSettingsPresenter.this, this.f$1, view);
            }
        });
        manageLockDialog.setForgotPasscodeButtonClickListener(new OnClickListener() {
            public final void onClick(View view) {
                AccountSettingsPresenter.lambda$verifyPassword$2(AccountSettingsPresenter.this, view);
            }
        });
        this.mDialog.show();
    }

    public static /* synthetic */ void lambda$verifyPassword$0(AccountSettingsPresenter accountSettingsPresenter, ManageLockDialog manageLockDialog, int i, View view) {
        manageLockDialog.showLoading(true);
        accountSettingsPresenter.mPasscodeSubscription.unsubscribe();
        accountSettingsPresenter.mPasscodeSubscription = accountSettingsPresenter.verifyPasscode(manageLockDialog).subscribeOn(accountSettingsPresenter.mScheduler.background()).observeOn(accountSettingsPresenter.mScheduler.main()).subscribe(accountSettingsPresenter.createPasscodeSubscriber(manageLockDialog, i));
    }

    public static /* synthetic */ void lambda$verifyPassword$1(AccountSettingsPresenter accountSettingsPresenter, ManageLockDialog manageLockDialog, View view) {
        ViewUtil.hideKeyboard(accountSettingsPresenter.mDialog.getContext(), manageLockDialog.getWindowToken());
        accountSettingsPresenter.mDialog.dismiss();
    }

    public static /* synthetic */ void lambda$verifyPassword$2(AccountSettingsPresenter accountSettingsPresenter, View view) {
        accountSettingsPresenter.mDialog.dismiss();
        accountSettingsPresenter.forgotPasscode();
    }

    private Observable<Void> verifyPasscode(ManageLockDialog manageLockDialog) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe() {
            public final void call(Object obj) {
                AccountSettingsPresenter.lambda$verifyPasscode$3(ManageLockDialog.this, (Subscriber) obj);
            }
        });
    }

    static /* synthetic */ void lambda$verifyPasscode$3(ManageLockDialog manageLockDialog, Subscriber subscriber) {
        ThreadUtil.errorOnUIThread();
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

    private Subscriber<Void> createPasscodeSubscriber(final ManageLockDialog manageLockDialog, final int i) {
        return new Subscriber<Void>() {
            public void onNext(Void voidR) {
            }

            public void onCompleted() {
                MasterLockApp.get().successfullyVerified();
                manageLockDialog.showLoading(false);
                AccountSettingsPresenter.this.mDialog.dismiss();
                ViewUtil.hideKeyboard(AccountSettingsPresenter.this.mDialog.getContext(), manageLockDialog.getWindowToken());
                MasterLockSharedPreferences.getInstance().putCanManageLock(true);
                LockActivity.restartPasscodeTimer();
                ((AccountSettingsView) AccountSettingsPresenter.this.view).goToSelectedItemScreen(i);
            }

            public void onError(Throwable th) {
                MasterLockApp.get().invalidVerification();
                if (!ApiError.generateError(th).isHandled()) {
                    manageLockDialog.showLoading(false);
                    manageLockDialog.showPasscodeError();
                    return;
                }
                AccountSettingsPresenter.this.mDialog.dismiss();
            }
        };
    }

    private void forgotPasscode() {
        if (this.view != null) {
            ForgotPasscodeDialog forgotPasscodeDialog = new ForgotPasscodeDialog(((AccountSettingsView) this.view).getContext(), null);
            this.mDialog = new Dialog(((AccountSettingsView) this.view).getContext());
            this.mDialog.requestWindowFeature(1);
            this.mDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
            this.mDialog.getWindow().setSoftInputMode(5);
            this.mDialog.setContentView(forgotPasscodeDialog);
            forgotPasscodeDialog.setPositiveButtonClickListener(new OnClickListener(forgotPasscodeDialog) {
                private final /* synthetic */ ForgotPasscodeDialog f$1;

                {
                    this.f$1 = r2;
                }

                public final void onClick(View view) {
                    AccountSettingsPresenter.lambda$forgotPasscode$4(AccountSettingsPresenter.this, this.f$1, view);
                }
            });
            forgotPasscodeDialog.setNegativeButtonClickListener(new OnClickListener(forgotPasscodeDialog) {
                private final /* synthetic */ ForgotPasscodeDialog f$1;

                {
                    this.f$1 = r2;
                }

                public final void onClick(View view) {
                    AccountSettingsPresenter.lambda$forgotPasscode$5(AccountSettingsPresenter.this, this.f$1, view);
                }
            });
            this.mDialog.show();
        }
    }

    public static /* synthetic */ void lambda$forgotPasscode$4(AccountSettingsPresenter accountSettingsPresenter, ForgotPasscodeDialog forgotPasscodeDialog, View view) {
        if (forgotPasscodeDialog.isValid()) {
            forgotPasscodeDialog.showLoading(true);
            accountSettingsPresenter.mPasscodeSubscription = accountSettingsPresenter.mSignUpService.forgotPasscode(forgotPasscodeDialog.getEmailAddress()).subscribeOn(accountSettingsPresenter.mScheduler.background()).observeOn(accountSettingsPresenter.mScheduler.main()).subscribe(accountSettingsPresenter.createForgotPasscodeSubscriber(forgotPasscodeDialog, accountSettingsPresenter.mDialog));
            return;
        }
        forgotPasscodeDialog.showLoading(false);
    }

    public static /* synthetic */ void lambda$forgotPasscode$5(AccountSettingsPresenter accountSettingsPresenter, ForgotPasscodeDialog forgotPasscodeDialog, View view) {
        ViewUtil.hideKeyboard(accountSettingsPresenter.mDialog.getContext(), forgotPasscodeDialog.getWindowToken());
        accountSettingsPresenter.mDialog.dismiss();
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

    public void goToAccountProfile() {
        AppFlow.get(((AccountSettingsView) this.view).getContext()).goTo(new AccountProfile());
    }

    public void goToLanguageSelection() {
        AppFlow.get(((AccountSettingsView) this.view).getContext()).goTo(new ChangeLanguage());
    }

    public void goToNotifications() {
        AppFlow.get(((AccountSettingsView) this.view).getContext()).goTo(new NotificationsOptions());
    }
}
