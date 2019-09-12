package com.masterlock.ble.app.presenter.lock;

import android.app.Dialog;
import android.support.p003v7.app.AppCompatDialog;
import android.view.View;
import android.view.View.OnClickListener;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.activity.LockActivity;
import com.masterlock.ble.app.analytics.Analytics;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.crypto.PBKDF2Encryptor;
import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.screens.LockScreens.AddMechanicalLock;
import com.masterlock.ble.app.screens.LockScreens.LockList;
import com.masterlock.ble.app.screens.SettingsScreens.CalibrateLock;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.SignInService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.util.ThreadUtil;
import com.masterlock.ble.app.util.ViewUtil;
import com.masterlock.ble.app.view.lock.AddLockView;
import com.masterlock.ble.app.view.modal.CalibrateModal;
import com.masterlock.ble.app.view.modal.ForgotPasscodeDialog;
import com.masterlock.ble.app.view.modal.ManageLockDialog;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import javax.inject.Inject;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.android.schedulers.AndroidSchedulers;
import p009rx.functions.Action1;
import p009rx.schedulers.Schedulers;
import p009rx.subscriptions.Subscriptions;

public class AddLockPresenter extends Presenter<String, AddLockView> {
    private AppCompatDialog mCalibrationDialog;
    /* access modifiers changed from: private */
    public Dialog mDialog;
    @Inject
    Bus mEventBus;
    @Inject
    LockService mLockService;
    private Subscription mPasscodeSubscription = Subscriptions.empty();
    @Inject
    IScheduler mScheduler;
    @Inject
    SignInService mSignUpService;
    private Subscription mSubscription = Subscriptions.empty();
    private Subscription mUpdateSubscription = Subscriptions.empty();

    static /* synthetic */ void lambda$null$4(Void voidR) {
    }

    public AddLockPresenter(AddLockView addLockView) {
        super("", addLockView);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        this.mEventBus.register(this);
    }

    public void sendLock(String str) {
        ((AddLockView) this.view).setLoading(true);
        this.model = str;
        this.mSubscription.unsubscribe();
        this.mSubscription = this.mLockService.add((String) this.model).subscribeOn(Schedulers.m220io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Subscriber<? super T>) new Subscriber<Lock>() {
            public void onStart() {
                AddLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                AddLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                MasterLockApp.get().getAnalytics().logEvent(Analytics.CATEGORY_MASTERLOCK_EVENT, Analytics.ACTION_ADD_LOCK, Analytics.ACTION_ADD_LOCK, 1);
                if (AddLockPresenter.this.view != null) {
                    ((AddLockView) AddLockPresenter.this.view).setLoading(false);
                }
            }

            public void onError(Throwable th) {
                AddLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ApiError generateError = ApiError.generateError(th);
                if (!generateError.isHandled() && AddLockPresenter.this.view != null) {
                    ((AddLockView) AddLockPresenter.this.view).setLoading(false);
                    if (ApiError.INVALID_ACTIVATION_CODE.equals(generateError.getCode())) {
                        ((AddLockView) AddLockPresenter.this.view).displayActivationCodeError(generateError);
                    }
                    ((AddLockView) AddLockPresenter.this.view).displayError(generateError);
                }
            }

            public void onNext(Lock lock) {
                if (lock == null) {
                    return;
                }
                if (lock.isDialSpeedLock() || lock.isBiometricPadLock()) {
                    AppFlow.get(((AddLockView) AddLockPresenter.this.view).getContext()).resetTo(new LockList());
                } else {
                    AddLockPresenter.this.getLockForCalibration(lock.getLockId());
                }
            }
        });
    }

    public void finish() {
        super.finish();
        this.mSubscription.unsubscribe();
        this.mEventBus.unregister(this);
    }

    /* access modifiers changed from: private */
    public void getLockForCalibration(String str) {
        this.mSubscription.unsubscribe();
        this.mSubscription = this.mLockService.get(str).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Action1<? super T>) new Action1() {
            public final void call(Object obj) {
                AddLockPresenter.this.showCalibrationModal((Lock) obj);
            }
        }, (Action1<Throwable>) $$Lambda$WyLYczv0rYB16nM4g5IsYSSBzSI.INSTANCE);
    }

    public void goToAddMechanicalLock() {
        AppFlow.get(((AddLockView) this.view).getContext()).goTo(new AddMechanicalLock(null));
    }

    public void goToCalibration(Lock lock) {
        if (MasterLockSharedPreferences.getInstance().canManageLock()) {
            dismissCalibrationModal();
            AppFlow.get(((AddLockView) this.view).getContext()).goTo(new CalibrateLock(lock, true));
            return;
        }
        ManageLockDialog manageLockDialog = new ManageLockDialog(((AddLockView) this.view).getContext(), null);
        this.mDialog = new Dialog(((AddLockView) this.view).getContext());
        this.mDialog.requestWindowFeature(1);
        this.mDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mDialog.getWindow().setSoftInputMode(5);
        this.mDialog.setContentView(manageLockDialog);
        manageLockDialog.setPositiveButtonClickListener(new OnClickListener(manageLockDialog, lock) {
            private final /* synthetic */ ManageLockDialog f$1;
            private final /* synthetic */ Lock f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void onClick(View view) {
                AddLockPresenter.lambda$goToCalibration$0(AddLockPresenter.this, this.f$1, this.f$2, view);
            }
        });
        manageLockDialog.setNegativeButtonClickListener(new OnClickListener(manageLockDialog) {
            private final /* synthetic */ ManageLockDialog f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                AddLockPresenter.lambda$goToCalibration$1(AddLockPresenter.this, this.f$1, view);
            }
        });
        manageLockDialog.setForgotPasscodeButtonClickListener(new OnClickListener() {
            public final void onClick(View view) {
                AddLockPresenter.lambda$goToCalibration$2(AddLockPresenter.this, view);
            }
        });
        this.mDialog.show();
    }

    public static /* synthetic */ void lambda$goToCalibration$0(AddLockPresenter addLockPresenter, ManageLockDialog manageLockDialog, Lock lock, View view) {
        manageLockDialog.showLoading(true);
        addLockPresenter.mPasscodeSubscription.unsubscribe();
        addLockPresenter.mPasscodeSubscription = addLockPresenter.verifyPasscode(manageLockDialog).subscribeOn(addLockPresenter.mScheduler.background()).observeOn(addLockPresenter.mScheduler.main()).subscribe(addLockPresenter.createPasscodeSubscriberCalibration(manageLockDialog, lock));
    }

    public static /* synthetic */ void lambda$goToCalibration$1(AddLockPresenter addLockPresenter, ManageLockDialog manageLockDialog, View view) {
        ViewUtil.hideKeyboard(addLockPresenter.mDialog.getContext(), manageLockDialog.getWindowToken());
        addLockPresenter.mDialog.dismiss();
    }

    public static /* synthetic */ void lambda$goToCalibration$2(AddLockPresenter addLockPresenter, View view) {
        addLockPresenter.mDialog.dismiss();
        addLockPresenter.forgotPasscode();
    }

    private Subscriber<Void> createPasscodeSubscriberCalibration(final ManageLockDialog manageLockDialog, final Lock lock) {
        return new Subscriber<Void>() {
            public void onNext(Void voidR) {
            }

            public void onCompleted() {
                MasterLockApp.get().successfullyVerified();
                manageLockDialog.showLoading(false);
                AddLockPresenter.this.mDialog.dismiss();
                ViewUtil.hideKeyboard(AddLockPresenter.this.mDialog.getContext(), manageLockDialog.getWindowToken());
                MasterLockSharedPreferences.getInstance().putCanManageLock(true);
                LockActivity.restartPasscodeTimer();
                AddLockPresenter.this.dismissCalibrationModal();
                AppFlow.get(((AddLockView) AddLockPresenter.this.view).getContext()).goTo(new CalibrateLock(lock, true));
            }

            public void onError(Throwable th) {
                MasterLockApp.get().invalidVerification();
                if (!ApiError.generateError(th).isHandled()) {
                    manageLockDialog.showLoading(false);
                    manageLockDialog.showPasscodeError();
                    return;
                }
                AddLockPresenter.this.mDialog.dismiss();
            }
        };
    }

    /* access modifiers changed from: private */
    public void dismissCalibrationModal() {
        AppCompatDialog appCompatDialog = this.mCalibrationDialog;
        if (appCompatDialog != null && appCompatDialog.isShowing()) {
            this.mCalibrationDialog.dismiss();
        }
    }

    /* access modifiers changed from: private */
    public void showCalibrationModal(Lock lock) {
        dismissCalibrationModal();
        CalibrateModal calibrateModal = new CalibrateModal(((AddLockView) this.view).getContext(), null);
        calibrateModal.setOnCalibrationClickedListener(new OnClickListener(lock) {
            private final /* synthetic */ Lock f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                AddLockPresenter.this.goToCalibration(this.f$1);
            }
        });
        calibrateModal.setOnSkipClickedListener(new OnClickListener(lock) {
            private final /* synthetic */ Lock f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                AddLockPresenter.lambda$showCalibrationModal$5(AddLockPresenter.this, this.f$1, view);
            }
        });
        this.mCalibrationDialog = new AppCompatDialog(((AddLockView) this.view).getContext(), C1075R.style.PermissionModalStyle);
        this.mCalibrationDialog.supportRequestWindowFeature(1);
        this.mCalibrationDialog.setContentView((View) calibrateModal);
        this.mCalibrationDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mCalibrationDialog.getWindow().setLayout(-1, -1);
        this.mCalibrationDialog.show();
    }

    public static /* synthetic */ void lambda$showCalibrationModal$5(AddLockPresenter addLockPresenter, Lock lock, View view) {
        lock.setSkippedCalibration(true);
        addLockPresenter.mUpdateSubscription.unsubscribe();
        addLockPresenter.mUpdateSubscription = addLockPresenter.mLockService.updateCalibrationInfo(lock).subscribeOn(addLockPresenter.mScheduler.background()).subscribe((Action1<? super T>) $$Lambda$AddLockPresenter$68d_NwHuV4QelIP5KXWxexxwlk.INSTANCE, (Action1<Throwable>) $$Lambda$WyLYczv0rYB16nM4g5IsYSSBzSI.INSTANCE);
        addLockPresenter.mCalibrationDialog.dismiss();
        LockList lockList = new LockList();
        LockActivity.setWasLockSuccessfullyAdded(true);
        AppFlow.get(((AddLockView) addLockPresenter.view).getContext()).resetTo(lockList);
    }

    private Observable<Void> verifyPasscode(ManageLockDialog manageLockDialog) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe() {
            public final void call(Object obj) {
                AddLockPresenter.lambda$verifyPasscode$6(ManageLockDialog.this, (Subscriber) obj);
            }
        });
    }

    static /* synthetic */ void lambda$verifyPasscode$6(ManageLockDialog manageLockDialog, Subscriber subscriber) {
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

    private void forgotPasscode() {
        if (this.view != null) {
            ForgotPasscodeDialog forgotPasscodeDialog = new ForgotPasscodeDialog(((AddLockView) this.view).getContext(), null);
            this.mDialog = new Dialog(((AddLockView) this.view).getContext());
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
                    AddLockPresenter.lambda$forgotPasscode$7(AddLockPresenter.this, this.f$1, view);
                }
            });
            forgotPasscodeDialog.setNegativeButtonClickListener(new OnClickListener(forgotPasscodeDialog) {
                private final /* synthetic */ ForgotPasscodeDialog f$1;

                {
                    this.f$1 = r2;
                }

                public final void onClick(View view) {
                    AddLockPresenter.lambda$forgotPasscode$8(AddLockPresenter.this, this.f$1, view);
                }
            });
            this.mDialog.show();
        }
    }

    public static /* synthetic */ void lambda$forgotPasscode$7(AddLockPresenter addLockPresenter, ForgotPasscodeDialog forgotPasscodeDialog, View view) {
        if (forgotPasscodeDialog.isValid()) {
            forgotPasscodeDialog.showLoading(true);
            addLockPresenter.mPasscodeSubscription = addLockPresenter.mSignUpService.forgotPasscode(forgotPasscodeDialog.getEmailAddress()).subscribeOn(addLockPresenter.mScheduler.background()).observeOn(addLockPresenter.mScheduler.main()).subscribe(addLockPresenter.createForgotPasscodeSubscriber(forgotPasscodeDialog, addLockPresenter.mDialog));
            return;
        }
        forgotPasscodeDialog.showLoading(false);
    }

    public static /* synthetic */ void lambda$forgotPasscode$8(AddLockPresenter addLockPresenter, ForgotPasscodeDialog forgotPasscodeDialog, View view) {
        ViewUtil.hideKeyboard(addLockPresenter.mDialog.getContext(), forgotPasscodeDialog.getWindowToken());
        addLockPresenter.mDialog.dismiss();
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
