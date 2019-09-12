package com.masterlock.ble.app.presenter.lock;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.activity.LockActivity;
import com.masterlock.ble.app.bus.FirmwareUpdateBrickedEvent;
import com.masterlock.ble.app.bus.ForceScanEvent;
import com.masterlock.ble.app.bus.LocationPermissionEvent;
import com.masterlock.ble.app.bus.ManageLockEvent;
import com.masterlock.ble.app.bus.RequestLocationAndNotificationsPermissionsEvent;
import com.masterlock.ble.app.bus.SortByEvent;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.command.LockBrickedListener;
import com.masterlock.ble.app.crypto.PBKDF2Encryptor;
import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.provider.MasterlockContract.Locks;
import com.masterlock.ble.app.screens.LockScreens.LockLanding;
import com.masterlock.ble.app.service.KMSDeviceLogService;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.SignInService;
import com.masterlock.ble.app.service.scan.BackgroundScanService;
import com.masterlock.ble.app.tape.UploadTaskService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.util.ThreadUtil;
import com.masterlock.ble.app.util.ViewUtil;
import com.masterlock.ble.app.view.lock.LockListView;
import com.masterlock.ble.app.view.modal.ForgotPasscodeDialog;
import com.masterlock.ble.app.view.modal.ManageLockDialog;
import com.masterlock.core.Lock;
import com.masterlock.core.LockStatus;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import flow.Screen;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.functions.Action1;
import p009rx.subscriptions.Subscriptions;

public class LockListPresenter extends Presenter<List<Lock>, LockListView> implements LockBrickedListener {
    @Inject
    ContentResolver mContentResolver;
    Dialog mDialog;
    @Inject
    Bus mEventBus;
    @Inject
    KMSDeviceLogService mKmsDeviceLogService;
    @Inject
    LockService mLockService;
    private Subscription mReloadSubscription = Subscriptions.empty();
    @Inject
    IScheduler mScheduler;
    private Screen mScreen;
    Screen mScreenTo;
    @Inject
    SignInService mSignUpService;
    private Subscription mSubscription = Subscriptions.empty();
    private Subscription mUpdateSubscription = Subscriptions.empty();
    final ContentObserver observer = new ContentObserver(new Handler()) {
        public void onChange(boolean z) {
            LockListPresenter.this.refresh();
        }
    };

    static /* synthetic */ void lambda$checkAction$4(Throwable th) {
    }

    static /* synthetic */ void lambda$checkAction$5() {
    }

    static /* synthetic */ void lambda$checkAction$7(Throwable th) {
    }

    static /* synthetic */ void lambda$checkAction$8() {
    }

    public LockListPresenter(LockListView lockListView) {
        super(lockListView);
        this.mScreen = (Screen) AppFlow.getScreen(lockListView.getContext());
    }

    public void start() {
        this.mEventBus.register(this);
        refresh();
        this.mContentResolver.registerContentObserver(Locks.CONTENT_URI, true, this.observer);
        final Context context = ((LockListView) this.view).getContext();
        if (VERSION.SDK_INT >= 26) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    Context context = context;
                    context.startForegroundService(new Intent(context, UploadTaskService.class));
                }
            });
        } else {
            context.startService(new Intent(context, UploadTaskService.class));
        }
    }

    public void reload() {
        if (this.mLockService != null) {
            Log.v("FIRMWARE", "Called reload");
            this.mReloadSubscription.unsubscribe();
            this.mReloadSubscription = this.mLockService.getProducts().subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<List<Lock>>() {
                public void onStart() {
                    LockListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
                }

                public void onCompleted() {
                    LockListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                }

                public void onError(Throwable th) {
                    LockListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                    ((LockListView) LockListPresenter.this.view).displayError(ApiError.generateError(th));
                }

                public void onNext(List<Lock> list) {
                    LockListPresenter.this.refresh();
                    if (list == null) {
                        AppFlow.get(((LockListView) LockListPresenter.this.view).getContext()).goBack();
                        return;
                    }
                    LockListPresenter.this.model = list;
                    ((LockListView) LockListPresenter.this.view).updateItems(list);
                }
            });
        }
    }

    public void refresh() {
        Log.v("FIRMWARE", "Called refresh");
        this.mSubscription.unsubscribe();
        this.mSubscription = this.mLockService.getAll().subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<List<Lock>>() {
            public void onCompleted() {
            }

            public void onStart() {
                LockListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onError(Throwable th) {
                LockListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ((LockListView) LockListPresenter.this.view).displayError(ApiError.generateError(th));
            }

            public void onNext(List<Lock> list) {
                if (list == null) {
                    AppFlow.get(((LockListView) LockListPresenter.this.view).getContext()).goBack();
                    return;
                }
                LockListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                LockListPresenter.this.model = list;
                if (LockListPresenter.this.view != null) {
                    ((LockListView) LockListPresenter.this.view).updateItems(list);
                }
                if (!((List) LockListPresenter.this.model).isEmpty()) {
                    if (((List) LockListPresenter.this.model).size() <= 1 || !LockActivity.wasLockSuccessfullyAdded()) {
                        LockListPresenter.this.mEventBus.post(new RequestLocationAndNotificationsPermissionsEvent());
                    } else {
                        LockListPresenter.this.mEventBus.post(new LocationPermissionEvent());
                    }
                }
            }
        });
    }

    public void finish() {
        super.finish();
        this.mSubscription.unsubscribe();
        this.mReloadSubscription.unsubscribe();
        this.mUpdateSubscription.unsubscribe();
        this.mEventBus.unregister(this);
        this.mContentResolver.unregisterContentObserver(this.observer);
    }

    @Subscribe
    public void checkPassword(ManageLockEvent manageLockEvent) {
        this.mScreenTo = manageLockEvent.getScreenTo();
        if (MasterLockSharedPreferences.getInstance().canManageLock()) {
            AppFlow.get(((LockListView) this.view).getContext()).goTo(this.mScreenTo);
            return;
        }
        ManageLockDialog manageLockDialog = new ManageLockDialog(((LockListView) this.view).getContext(), null);
        this.mDialog = new Dialog(((LockListView) this.view).getContext());
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
                LockListPresenter.lambda$checkPassword$0(LockListPresenter.this, this.f$1, view);
            }
        });
        manageLockDialog.setNegativeButtonClickListener(new OnClickListener(manageLockDialog) {
            private final /* synthetic */ ManageLockDialog f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                LockListPresenter.lambda$checkPassword$1(LockListPresenter.this, this.f$1, view);
            }
        });
        manageLockDialog.setForgotPasscodeButtonClickListener(new OnClickListener() {
            public final void onClick(View view) {
                LockListPresenter.lambda$checkPassword$2(LockListPresenter.this, view);
            }
        });
        this.mDialog.show();
    }

    public static /* synthetic */ void lambda$checkPassword$0(LockListPresenter lockListPresenter, ManageLockDialog manageLockDialog, View view) {
        manageLockDialog.showLoading(true);
        lockListPresenter.mSubscription.unsubscribe();
        lockListPresenter.mSubscription = lockListPresenter.verifyPasscode(manageLockDialog).subscribeOn(lockListPresenter.mScheduler.background()).observeOn(lockListPresenter.mScheduler.main()).subscribe(lockListPresenter.createPasscodeSubscriber(manageLockDialog));
    }

    public static /* synthetic */ void lambda$checkPassword$1(LockListPresenter lockListPresenter, ManageLockDialog manageLockDialog, View view) {
        ViewUtil.hideKeyboard(lockListPresenter.mDialog.getContext(), manageLockDialog.getWindowToken());
        lockListPresenter.mDialog.dismiss();
    }

    public static /* synthetic */ void lambda$checkPassword$2(LockListPresenter lockListPresenter, View view) {
        lockListPresenter.mDialog.dismiss();
        lockListPresenter.forgotPasscode();
    }

    public void removeLockerMode() {
        ArrayList arrayList = new ArrayList();
        for (Lock lock : (List) this.model) {
            lock.setLockerMode(false);
            arrayList.add(lock);
        }
        this.mUpdateSubscription.unsubscribe();
        this.mUpdateSubscription = this.mLockService.updateCommunicationsEnabled((List<Lock>) arrayList).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
            public void onNext(Boolean bool) {
            }

            public void onStart() {
                LockListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                LockListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ((LockListView) LockListPresenter.this.view).updateItems((List) LockListPresenter.this.model);
            }

            public void onError(Throwable th) {
                LockListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ((LockListView) LockListPresenter.this.view).displayError(th);
            }
        });
    }

    @Subscribe
    public void onFirmwareUpdateBrickedEvent(FirmwareUpdateBrickedEvent firmwareUpdateBrickedEvent) {
        if (this.model != null && ((List) this.model).size() >= 0) {
            int indexOf = ((List) this.model).indexOf(firmwareUpdateBrickedEvent.getLock());
            if (indexOf >= 0) {
                Lock lock = (Lock) ((List) this.model).get(indexOf);
                if (lock.getLockStatus() != LockStatus.UPDATE_MODE) {
                    lock.setLockStatus(firmwareUpdateBrickedEvent.getLock().getLockStatus());
                    this.mUpdateSubscription.unsubscribe();
                    this.mUpdateSubscription = this.mLockService.updateDb(lock).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
                        public void onNext(Boolean bool) {
                        }

                        public void onStart() {
                            LockListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
                        }

                        public void onCompleted() {
                            LockListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                            LockListPresenter.this.refresh();
                        }

                        public void onError(Throwable th) {
                            LockListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                            ((LockListView) LockListPresenter.this.view).displayError(th);
                        }
                    });
                }
            }
        }
    }

    public void checkAction(String str, String str2) {
        if (str != null && str2 != null) {
            if (BackgroundScanService.ACTION_CALIBRATION.equals(str)) {
                this.mSubscription = this.mLockService.get(str2).observeOn(this.mScheduler.main()).subscribeOn(this.mScheduler.background()).subscribe(new Action1() {
                    public final void call(Object obj) {
                        AppFlow.get(((LockListView) LockListPresenter.this.view).getContext()).goTo(new LockLanding((Lock) obj, true));
                    }
                }, $$Lambda$LockListPresenter$J97azLtt93b9epnAw6SAtdcXOs.INSTANCE, $$Lambda$LockListPresenter$d3JiPhX0aBU5nEjvCv7aiaVE.INSTANCE);
            } else if (BackgroundScanService.ACTION_LOCK_MODIFY.equals(str)) {
                this.mSubscription = this.mLockService.get(str2).observeOn(this.mScheduler.main()).subscribeOn(this.mScheduler.background()).subscribe(new Action1() {
                    public final void call(Object obj) {
                        AppFlow.get(((LockListView) LockListPresenter.this.view).getContext()).goTo(new LockLanding((Lock) obj));
                    }
                }, $$Lambda$LockListPresenter$8tV2uC88HQYBzZ8rr2lkKXsquss.INSTANCE, $$Lambda$LockListPresenter$exdBHTSeKPZ8tZ1hvv3kHsRk0.INSTANCE);
            }
        }
    }

    public void forceRescan() {
        this.mEventBus.post(new ForceScanEvent((List) this.model));
    }

    @Subscribe
    public void onSortByEvent(SortByEvent sortByEvent) {
        MasterLockSharedPreferences.getInstance().putSortBy(sortByEvent.getSortId());
        ((LockListView) this.view).updateItems((List) this.model);
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
                LockListPresenter.this.mDialog.dismiss();
                ViewUtil.hideKeyboard(LockListPresenter.this.mDialog.getContext(), manageLockDialog.getWindowToken());
                MasterLockSharedPreferences.getInstance().putCanManageLock(true);
                LockActivity.restartPasscodeTimer();
                if (LockListPresenter.this.mScreenTo != null) {
                    AppFlow.get(((LockListView) LockListPresenter.this.view).getContext()).goTo(LockListPresenter.this.mScreenTo);
                }
            }

            public void onError(Throwable th) {
                MasterLockApp.get().invalidVerification();
                if (!ApiError.generateError(th).isHandled()) {
                    manageLockDialog.showLoading(false);
                    manageLockDialog.showPasscodeError();
                    return;
                }
                LockListPresenter.this.mDialog.dismiss();
            }
        };
    }

    private void forgotPasscode() {
        if (this.view != null) {
            ForgotPasscodeDialog forgotPasscodeDialog = new ForgotPasscodeDialog(((LockListView) this.view).getContext(), null);
            this.mDialog = new Dialog(((LockListView) this.view).getContext());
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
                    LockListPresenter.lambda$forgotPasscode$9(LockListPresenter.this, this.f$1, view);
                }
            });
            forgotPasscodeDialog.setNegativeButtonClickListener(new OnClickListener(forgotPasscodeDialog) {
                private final /* synthetic */ ForgotPasscodeDialog f$1;

                {
                    this.f$1 = r2;
                }

                public final void onClick(View view) {
                    LockListPresenter.lambda$forgotPasscode$10(LockListPresenter.this, this.f$1, view);
                }
            });
            this.mDialog.show();
        }
    }

    public static /* synthetic */ void lambda$forgotPasscode$9(LockListPresenter lockListPresenter, ForgotPasscodeDialog forgotPasscodeDialog, View view) {
        if (forgotPasscodeDialog.isValid()) {
            forgotPasscodeDialog.showLoading(true);
            lockListPresenter.mSubscription = lockListPresenter.mSignUpService.forgotPasscode(forgotPasscodeDialog.getEmailAddress()).subscribeOn(lockListPresenter.mScheduler.background()).observeOn(lockListPresenter.mScheduler.main()).subscribe(lockListPresenter.createForgotPasscodeSubscriber(forgotPasscodeDialog, lockListPresenter.mDialog));
            return;
        }
        forgotPasscodeDialog.showLoading(false);
    }

    public static /* synthetic */ void lambda$forgotPasscode$10(LockListPresenter lockListPresenter, ForgotPasscodeDialog forgotPasscodeDialog, View view) {
        ViewUtil.hideKeyboard(lockListPresenter.mDialog.getContext(), forgotPasscodeDialog.getWindowToken());
        lockListPresenter.mDialog.dismiss();
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
