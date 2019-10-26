package com.masterlock.ble.app.presenter.locklanding;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.support.p003v7.app.AppCompatDialog;
import android.view.View;
import android.view.View.OnClickListener;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.activity.LockActivity;
import com.masterlock.ble.app.analytics.Analytics;
import com.masterlock.ble.app.bus.FirmwareUpdateBrickedEvent;
import com.masterlock.ble.app.bus.ForceScanEvent;
import com.masterlock.ble.app.bus.ManageLockEvent;
import com.masterlock.ble.app.bus.TimerCountdownFinishedEvent;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.bus.UpdateToolbarEvent.Builder;
import com.masterlock.ble.app.command.LockBrickedListener;
import com.masterlock.ble.app.crypto.PBKDF2Encryptor;
import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.provider.MasterlockContract.Locks;
import com.masterlock.ble.app.screens.LockScreens.LockDetailsKeySafe;
import com.masterlock.ble.app.screens.LockScreens.LockDetailsPadLock;
import com.masterlock.ble.app.screens.LockScreens.LockLanding;
import com.masterlock.ble.app.screens.SettingsScreens.CalibrateLock;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.SignInService;
import com.masterlock.ble.app.service.scan.BackgroundScanService;
import com.masterlock.ble.app.util.AccessWindowUtil;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.util.ThreadUtil;
import com.masterlock.ble.app.util.ViewUtil;
import com.masterlock.ble.app.view.locklanding.LockLandingView;
import com.masterlock.ble.app.view.modal.CalibrateModal;
import com.masterlock.ble.app.view.modal.ForgotPasscodeDialog;
import com.masterlock.ble.app.view.modal.LockerModeDialogKeySafe;
import com.masterlock.ble.app.view.modal.LockerModeDialogPadLock;
import com.masterlock.ble.app.view.modal.ManageLockDialog;
import com.masterlock.core.AccessType;
import com.masterlock.core.GuestInterface;
import com.masterlock.core.Lock;
import com.masterlock.core.LockStatus;
import com.masterlock.core.ScheduleType;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import flow.Screen;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import javax.inject.Inject;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.functions.Action0;
import p009rx.subscriptions.Subscriptions;

public class LockLandingPresenter extends Presenter<Lock, LockLandingView> implements LockBrickedListener {
    private boolean forceToolbarDraw = false;
    private boolean isToolbarReset = false;
    private AppCompatDialog mCalibrationDialog;
    @Inject
    ContentResolver mContentResolver;
    /* access modifiers changed from: private */
    public Dialog mDialog;
    @Inject
    Bus mEventBus;
    private Lock mLock;
    @Inject
    LockService mLockService;
    final ContentObserver mObserver = new ContentObserver(new Handler()) {
        public void onChange(boolean z) {
            LockLandingPresenter.this.reload();
        }
    };
    private Subscription mPasscodeSubscription = Subscriptions.empty();
    @Inject
    IScheduler mScheduler;
    @Inject
    SignInService mSignUpService;
    private Subscription mSubscription = Subscriptions.empty();
    private Subscription mUpdateSubscription = Subscriptions.empty();

    static /* synthetic */ void lambda$null$11(Void voidR) {
    }

    public LockLandingPresenter(Lock lock, LockLandingView lockLandingView) {
        super(lock, lockLandingView);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        this.mEventBus.register(this);
        this.mContentResolver.registerContentObserver(Locks.buildLockUri(((Lock) this.model).getLockId()), true, this.mObserver);
        updateToolBar();
        reload();
    }

    public void finish() {
        this.mSubscription.unsubscribe();
        this.mUpdateSubscription.unsubscribe();
        this.mPasscodeSubscription.unsubscribe();
        Dialog dialog = this.mDialog;
        if (dialog != null) {
            dialog.dismiss();
        }
        dismissCalibrationModal();
        this.mEventBus.unregister(this);
        this.mContentResolver.unregisterContentObserver(this.mObserver);
        super.finish();
    }

    /* access modifiers changed from: private */
    public void reload() {
        if (this.model != null) {
            this.mSubscription.unsubscribe();
            this.mSubscription = this.mLockService.get(((Lock) this.model).getLockId()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Lock>() {
                public void onStart() {
                    LockLandingPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
                }

                public void onCompleted() {
                    LockLandingPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                }

                public void onError(Throwable th) {
                    LockLandingPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                    ((LockLandingView) LockLandingPresenter.this.view).displayError(th);
                }

                public void onNext(Lock lock) {
                    if (LockLandingPresenter.this.view != null) {
                        if (lock == null) {
                            AppFlow.get(((LockLandingView) LockLandingPresenter.this.view).getContext()).goBack();
                            return;
                        }
                        LockLandingPresenter.this.model = lock;
                        ((LockLandingView) LockLandingPresenter.this.view).updateLock(lock);
                        LockLandingPresenter.this.updateToolBar();
                    }
                }
            });
        }
    }

    public void toggleLockerMode() {
        if (this.model != null) {
            ((Lock) this.model).setLockerMode(!((Lock) this.model).isLockerMode());
            this.mUpdateSubscription.unsubscribe();
            this.mUpdateSubscription = this.mLockService.updateCommunicationsEnabled((Lock) this.model).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
                public void onNext(Boolean bool) {
                }

                public void onStart() {
                    LockLandingPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
                }

                public void onCompleted() {
                    LockLandingPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                    LockLandingPresenter.this.displayLockerModeDialog();
                    if (((Lock) LockLandingPresenter.this.model).isLockerMode()) {
                        MasterLockApp.get().getAnalytics().logEvent(Analytics.CATEGORY_MASTERLOCK_EVENT, Analytics.ACTION_ENABLE_LOCKER_MODE, Analytics.ACTION_ENABLE_LOCKER_MODE, 1);
                    }
                }

                public void onError(Throwable th) {
                    LockLandingPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                    ((LockLandingView) LockLandingPresenter.this.view).displayError(th);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void displayLockerModeDialog() {
        if (((Lock) this.model).isPadLock()) {
            LockerModeDialogPadLock lockerModeDialogPadLock = new LockerModeDialogPadLock(((LockLandingView) this.view).getContext(), null);
            lockerModeDialogPadLock.displayState(((Lock) this.model).isLockerMode(), (Lock) this.model);
            lockerModeDialogPadLock.fillCode((Lock) this.model);
            lockerModeDialogPadLock.setPositiveButtonClickListener(new OnClickListener() {
                public final void onClick(View view) {
                    LockLandingPresenter.this.mDialog.dismiss();
                }
            });
            this.mDialog = new Dialog(((LockLandingView) this.view).getContext());
            this.mDialog.requestWindowFeature(1);
            this.mDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
            this.mDialog.setContentView(lockerModeDialogPadLock);
            this.mDialog.show();
            return;
        }
        LockerModeDialogKeySafe lockerModeDialogKeySafe = new LockerModeDialogKeySafe(((LockLandingView) this.view).getContext(), null);
        lockerModeDialogKeySafe.displayState(((Lock) this.model).isLockerMode(), (Lock) this.model);
        lockerModeDialogKeySafe.fillCode((Lock) this.model);
        lockerModeDialogKeySafe.setPositiveButtonClickListener(new OnClickListener() {
            public final void onClick(View view) {
                LockLandingPresenter.this.mDialog.dismiss();
            }
        });
        this.mDialog = new Dialog(((LockLandingView) this.view).getContext());
        this.mDialog.requestWindowFeature(1);
        this.mDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mDialog.setContentView(lockerModeDialogKeySafe);
        this.mDialog.show();
    }

    public void relockTimeExpired() {
        this.mEventBus.post(new TimerCountdownFinishedEvent((Lock) this.model));
        this.mEventBus.post(new ForceScanEvent((Lock) this.model));
    }

    public void unlock() {
        if (this.model != null && this.view != null) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(((Lock) this.model).getKmsDeviceKey().getDeviceId());
            Intent intent = new Intent(((LockLandingView) this.view).getContext(), BackgroundScanService.class);
            intent.setAction(BackgroundScanService.ACTION_UNLOCK);
            intent.putExtra(BackgroundScanService.LOCK_DEVICE_IDS, arrayList);
            ((LockLandingView) this.view).getContext().startService(intent);
            ((LockLandingView) this.view).updateLock((Lock) this.model);
            updateToolBar();
        }
    }

    private void resetToolbar() {
        this.isToolbarReset = true;
        Builder builder = new Builder(((LockLandingView) this.view).getResources());
        builder.color(C1075R.color.primary).statusBarColor(C1075R.color.primary_dark);
        this.mEventBus.post(builder.build());
    }

    /* access modifiers changed from: private */
    public void updateToolBar() {
        if ((((Screen) AppFlow.getScreen(((LockLandingView) this.view).getContext())) instanceof LockLanding) && !this.isToolbarReset) {
            Builder builder = new Builder(((LockLandingView) this.view).getResources());
            if (((Lock) this.model).isLockerMode()) {
                builder.color(C1075R.color.locker_mode).statusBarColor(C1075R.color.locker_mode_status_bar);
            } else if (((Lock) this.model).getAccessType() == AccessType.GUEST && !AccessWindowUtil.hasStarted((Lock) this.model)) {
                builder.color(C1075R.color.guest_mode_out_of_schedule).statusBarColor(C1075R.color.guest_mode_out_of_schedule_status_bar);
            } else if (((Lock) this.model).getAccessType() == AccessType.GUEST && AccessWindowUtil.hasStarted((Lock) this.model) && ((Lock) this.model).getPermissions().getGuestInterface() == GuestInterface.ADVANCED && !AccessWindowUtil.isAllowedToday((Lock) this.model)) {
                builder.color(C1075R.color.guest_mode_out_of_schedule).statusBarColor(C1075R.color.guest_mode_out_of_schedule_status_bar);
            } else if (((Lock) this.model).getAccessType() == AccessType.GUEST && ((Lock) this.model).getPermissions().getScheduleType() == ScheduleType.SEVEN_AM_TO_SEVEN_PM && !AccessWindowUtil.isInsideSchedule((Lock) this.model)) {
                builder.color(C1075R.color.guest_mode_out_of_schedule).statusBarColor(C1075R.color.guest_mode_out_of_schedule_status_bar);
            } else if (((Lock) this.model).getAccessType() != AccessType.GUEST || ((Lock) this.model).getPermissions().getScheduleType() != ScheduleType.SEVEN_PM_TO_SEVEN_AM || AccessWindowUtil.isInsideSchedule((Lock) this.model)) {
                switch (((Lock) this.model).getLockStatus()) {
                    case OPENED:
                    case UNLOCKED:
                    case UNLOCKING:
                        builder.color(C1075R.color.open).statusBarColor(C1075R.color.open_status_bar);
                        break;
                    case LOCKED:
                    case LOCK_FOUND:
                        builder.color(C1075R.color.locked).statusBarColor(C1075R.color.locked_status_bar);
                        break;
                    case UPDATE_MODE:
                        builder.color(C1075R.color.locker_update).statusBarColor(C1075R.color.locker_update_status_bar);
                        break;
                    default:
                        builder.color(C1075R.color.primary).statusBarColor(C1075R.color.primary_dark);
                        break;
                }
            } else {
                builder.color(C1075R.color.guest_mode_out_of_schedule).statusBarColor(C1075R.color.guest_mode_out_of_schedule_status_bar);
            }
            this.mEventBus.post(builder.build());
        }
    }

    public void updateToolbarToDefault() {
        Builder builder = new Builder(((LockLandingView) this.view).getResources());
        builder.color(C1075R.color.primary).statusBarColor(C1075R.color.primary_dark);
        this.mEventBus.post(builder.build());
    }

    @Subscribe
    public void verifyPasscode(ManageLockEvent manageLockEvent) {
        if (MasterLockSharedPreferences.getInstance().canManageLock()) {
            goToLockDetails();
            return;
        }
        ManageLockDialog manageLockDialog = new ManageLockDialog(((LockLandingView) this.view).getContext(), null);
        this.mDialog = new Dialog(((LockLandingView) this.view).getContext());
        this.mDialog.requestWindowFeature(1);
        this.mDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mDialog.getWindow().setSoftInputMode(5);
        this.mDialog.setContentView(manageLockDialog);
        manageLockDialog.setPositiveButtonClickListener(new OnClickListener(manageLockDialog) {
            private final /* synthetic */ ManageLockDialog f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                LockLandingPresenter.lambda$verifyPasscode$2(LockLandingPresenter.this, this.f$1, view);
            }
        });
        manageLockDialog.setNegativeButtonClickListener(new OnClickListener(manageLockDialog) {
            private final /* synthetic */ ManageLockDialog f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                LockLandingPresenter.lambda$verifyPasscode$3(LockLandingPresenter.this, this.f$1, view);
            }
        });
        manageLockDialog.setForgotPasscodeButtonClickListener(new OnClickListener() {
            public final void onClick(View view) {
                LockLandingPresenter.lambda$verifyPasscode$4(LockLandingPresenter.this, view);
            }
        });
        this.mDialog.show();
    }

    public static /* synthetic */ void lambda$verifyPasscode$2(LockLandingPresenter lockLandingPresenter, ManageLockDialog manageLockDialog, View view) {
        manageLockDialog.showLoading(true);
        lockLandingPresenter.mPasscodeSubscription.unsubscribe();
        lockLandingPresenter.mPasscodeSubscription = lockLandingPresenter.verifyPasscode(manageLockDialog).subscribeOn(lockLandingPresenter.mScheduler.background()).observeOn(lockLandingPresenter.mScheduler.main()).subscribe(lockLandingPresenter.createPasscodeSubscriber(manageLockDialog));
    }

    public static /* synthetic */ void lambda$verifyPasscode$3(LockLandingPresenter lockLandingPresenter, ManageLockDialog manageLockDialog, View view) {
        ViewUtil.hideKeyboard(lockLandingPresenter.mDialog.getContext(), manageLockDialog.getWindowToken());
        lockLandingPresenter.mDialog.dismiss();
    }

    public static /* synthetic */ void lambda$verifyPasscode$4(LockLandingPresenter lockLandingPresenter, View view) {
        lockLandingPresenter.mDialog.dismiss();
        lockLandingPresenter.forgotPasscode();
    }

    private Observable<Void> verifyPasscode(final ManageLockDialog manageLockDialog) {
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

    private Subscriber<Void> createPasscodeSubscriber(final ManageLockDialog manageLockDialog) {
        return new Subscriber<Void>() {
            public void onNext(Void voidR) {
            }

            public void onCompleted() {
                MasterLockApp.get().successfullyVerified();
                manageLockDialog.showLoading(false);
                LockLandingPresenter.this.mDialog.dismiss();
                ViewUtil.hideKeyboard(LockLandingPresenter.this.mDialog.getContext(), manageLockDialog.getWindowToken());
                MasterLockSharedPreferences.getInstance().putCanManageLock(true);
                LockActivity.restartPasscodeTimer();
                LockLandingPresenter.this.goToLockDetails();
            }

            public void onError(Throwable th) {
                MasterLockApp.get().invalidVerification();
                if (!ApiError.generateError(th).isHandled()) {
                    manageLockDialog.showLoading(false);
                    manageLockDialog.showPasscodeError();
                    return;
                }
                LockLandingPresenter.this.mDialog.dismiss();
            }
        };
    }

    private void forgotPasscode() {
        if (this.view != null) {
            ForgotPasscodeDialog forgotPasscodeDialog = new ForgotPasscodeDialog(((LockLandingView) this.view).getContext(), null);
            this.mDialog = new Dialog(((LockLandingView) this.view).getContext());
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
                    LockLandingPresenter.lambda$forgotPasscode$5(LockLandingPresenter.this, this.f$1, view);
                }
            });
            forgotPasscodeDialog.setNegativeButtonClickListener(new OnClickListener(forgotPasscodeDialog) {
                private final /* synthetic */ ForgotPasscodeDialog f$1;

                {
                    this.f$1 = r2;
                }

                public final void onClick(View view) {
                    LockLandingPresenter.lambda$forgotPasscode$6(LockLandingPresenter.this, this.f$1, view);
                }
            });
            this.mDialog.show();
        }
    }

    public static /* synthetic */ void lambda$forgotPasscode$5(LockLandingPresenter lockLandingPresenter, ForgotPasscodeDialog forgotPasscodeDialog, View view) {
        if (forgotPasscodeDialog.isValid()) {
            forgotPasscodeDialog.showLoading(true);
            lockLandingPresenter.mPasscodeSubscription = lockLandingPresenter.mSignUpService.forgotPasscode(forgotPasscodeDialog.getEmailAddress()).subscribeOn(lockLandingPresenter.mScheduler.background()).observeOn(lockLandingPresenter.mScheduler.main()).subscribe(lockLandingPresenter.createForgotPasscodeSubscriber(forgotPasscodeDialog, lockLandingPresenter.mDialog));
            return;
        }
        forgotPasscodeDialog.showLoading(false);
    }

    public static /* synthetic */ void lambda$forgotPasscode$6(LockLandingPresenter lockLandingPresenter, ForgotPasscodeDialog forgotPasscodeDialog, View view) {
        ViewUtil.hideKeyboard(lockLandingPresenter.mDialog.getContext(), forgotPasscodeDialog.getWindowToken());
        lockLandingPresenter.mDialog.dismiss();
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

    /* access modifiers changed from: private */
    public void goToLockDetails() {
        AppFlow.get(((LockLandingView) this.view).getContext()).goTo(((Lock) this.model).isPadLock() ? new LockDetailsPadLock((Lock) this.model, false) : new LockDetailsKeySafe((Lock) this.model, false));
    }

    public void onBackPressed() {
        resetToolbar();
    }

    public void onUpPressed() {
        resetToolbar();
    }

    @Subscribe
    public void onFirmwareUpdateBrickedEvent(FirmwareUpdateBrickedEvent firmwareUpdateBrickedEvent) {
        if (((Lock) this.model).getLockId().equals(firmwareUpdateBrickedEvent.getLock().getLockId()) && ((Lock) this.model).getLockStatus() != LockStatus.UPDATE_MODE) {
            ((Lock) this.model).setLockStatus(firmwareUpdateBrickedEvent.getLock().getLockStatus());
            this.mLockService.updateDb((Lock) this.model).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
                public void onError(Throwable th) {
                }

                public void onNext(Boolean bool) {
                }

                public void onCompleted() {
                    LockLandingPresenter.this.reload();
                }
            });
        }
    }

    public void setForceToolbarDraw(boolean z) {
        this.forceToolbarDraw = z;
    }

    public void forceRescan() {
        this.mEventBus.post(new ForceScanEvent((Lock) this.model));
    }

    public void goToCalibration() {
        if (!MasterLockSharedPreferences.getInstance().canManageLock() || this.view == null) {
            ManageLockDialog manageLockDialog = new ManageLockDialog(((LockLandingView) this.view).getContext(), null);
            this.mDialog = new Dialog(((LockLandingView) this.view).getContext());
            this.mDialog.requestWindowFeature(1);
            this.mDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
            this.mDialog.getWindow().setSoftInputMode(5);
            this.mDialog.setContentView(manageLockDialog);
            manageLockDialog.setPositiveButtonClickListener(new OnClickListener(manageLockDialog) {
                private final /* synthetic */ ManageLockDialog f$1;

                {
                    this.f$1 = r2;
                }

                public final void onClick(View view) {
                    LockLandingPresenter.lambda$goToCalibration$7(LockLandingPresenter.this, this.f$1, view);
                }
            });
            manageLockDialog.setNegativeButtonClickListener(new OnClickListener(manageLockDialog) {
                private final /* synthetic */ ManageLockDialog f$1;

                {
                    this.f$1 = r2;
                }

                public final void onClick(View view) {
                    LockLandingPresenter.lambda$goToCalibration$8(LockLandingPresenter.this, this.f$1, view);
                }
            });
            manageLockDialog.setForgotPasscodeButtonClickListener(new OnClickListener() {
                public final void onClick(View view) {
                    LockLandingPresenter.lambda$goToCalibration$9(LockLandingPresenter.this, view);
                }
            });
            this.mDialog.show();
            return;
        }
        dismissCalibrationModal();
        AppFlow.get(((LockLandingView) this.view).getContext()).goTo(new CalibrateLock((Lock) this.model));
    }

    public static /* synthetic */ void lambda$goToCalibration$7(LockLandingPresenter lockLandingPresenter, ManageLockDialog manageLockDialog, View view) {
        manageLockDialog.showLoading(true);
        lockLandingPresenter.mPasscodeSubscription.unsubscribe();
        lockLandingPresenter.mPasscodeSubscription = lockLandingPresenter.verifyPasscode(manageLockDialog).subscribeOn(lockLandingPresenter.mScheduler.background()).observeOn(lockLandingPresenter.mScheduler.main()).subscribe(lockLandingPresenter.createPasscodeSubscriberCalibration(manageLockDialog));
    }

    public static /* synthetic */ void lambda$goToCalibration$8(LockLandingPresenter lockLandingPresenter, ManageLockDialog manageLockDialog, View view) {
        ViewUtil.hideKeyboard(lockLandingPresenter.mDialog.getContext(), manageLockDialog.getWindowToken());
        lockLandingPresenter.mDialog.dismiss();
    }

    public static /* synthetic */ void lambda$goToCalibration$9(LockLandingPresenter lockLandingPresenter, View view) {
        lockLandingPresenter.mDialog.dismiss();
        lockLandingPresenter.forgotPasscode();
    }

    private Subscriber<Void> createPasscodeSubscriberCalibration(final ManageLockDialog manageLockDialog) {
        return new Subscriber<Void>() {
            public void onNext(Void voidR) {
            }

            public void onCompleted() {
                MasterLockApp.get().successfullyVerified();
                manageLockDialog.showLoading(false);
                LockLandingPresenter.this.mDialog.dismiss();
                ViewUtil.hideKeyboard(LockLandingPresenter.this.mDialog.getContext(), manageLockDialog.getWindowToken());
                MasterLockSharedPreferences.getInstance().putCanManageLock(true);
                LockActivity.restartPasscodeTimer();
                LockLandingPresenter.this.dismissCalibrationModal();
                AppFlow.get(((LockLandingView) LockLandingPresenter.this.view).getContext()).goTo(new CalibrateLock((Lock) LockLandingPresenter.this.model));
            }

            public void onError(Throwable th) {
                MasterLockApp.get().invalidVerification();
                if (!ApiError.generateError(th).isHandled()) {
                    manageLockDialog.showLoading(false);
                    manageLockDialog.showPasscodeError();
                    return;
                }
                LockLandingPresenter.this.mDialog.dismiss();
            }
        };
    }

    private void lockNeedsCalibration() {
        if (!((Lock) this.model).getCalibrationInfo().hasSkipped() && ((Lock) this.model).getCalibrationInfo().getValue() == 0) {
            showCalibrationModal();
        }
    }

    /* access modifiers changed from: private */
    public void dismissCalibrationModal() {
        AppCompatDialog appCompatDialog = this.mCalibrationDialog;
        if (appCompatDialog != null && appCompatDialog.isShowing()) {
            this.mCalibrationDialog.dismiss();
        }
    }

    private void showCalibrationModal() {
        dismissCalibrationModal();
        CalibrateModal calibrateModal = new CalibrateModal(((LockLandingView) this.view).getContext(), null);
        calibrateModal.setOnCalibrationClickedListener(new OnClickListener() {
            public final void onClick(View view) {
                LockLandingPresenter.this.goToCalibration();
            }
        });
        calibrateModal.setOnSkipClickedListener(new OnClickListener() {
            public final void onClick(View view) {
                LockLandingPresenter.lambda$showCalibrationModal$13(LockLandingPresenter.this, view);
            }
        });
        this.mCalibrationDialog = new AppCompatDialog(((LockLandingView) this.view).getContext(), C1075R.style.PermissionModalStyle);
        this.mCalibrationDialog.supportRequestWindowFeature(1);
        this.mCalibrationDialog.setContentView((View) calibrateModal);
        this.mCalibrationDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mCalibrationDialog.getWindow().setLayout(-1, -1);
        this.mCalibrationDialog.show();
    }

    public static /* synthetic */ void lambda$showCalibrationModal$13(LockLandingPresenter lockLandingPresenter, View view) {
        ((Lock) lockLandingPresenter.model).setSkippedCalibration(true);
        lockLandingPresenter.mUpdateSubscription.unsubscribe();
        lockLandingPresenter.mUpdateSubscription = lockLandingPresenter.mLockService.updateCalibrationInfo((Lock) lockLandingPresenter.model).subscribeOn(lockLandingPresenter.mScheduler.background()).subscribe($$Lambda$LockLandingPresenter$eZh7ik3z3jXq51CYqBgPTQUFyIc.INSTANCE, $$Lambda$WyLYczv0rYB16nM4g5IsYSSBzSI.INSTANCE, new Action0() {
            public final void call() {
                LockLandingPresenter.this.mCalibrationDialog.dismiss();
            }
        });
    }
}
