package com.masterlock.ble.app.tape;

import com.masterlock.api.provider.AuthenticationStore;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.analytics.Analytics;
import com.masterlock.ble.app.command.ResetKeysWrapper;
import com.masterlock.ble.app.service.KMSDeviceService;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.core.FirmwareUpdate;
import com.masterlock.core.Lock;
import com.squareup.tape.Task;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class ConfirmTask implements Task<ITaskCallback> {
    private AuthenticationStore mAuthStore;
    private FirmwareUpdate mFirmwareUpdate;
    @Inject
    transient KMSDeviceService mKmsDeviceService;
    private Lock mLock;
    @Inject
    transient LockService mLockService;
    private ResetKeysWrapper mResetKeysWrapper;
    @Inject
    transient IScheduler mScheduler;
    private transient Subscription mSubscription;
    private String username = MasterLockSharedPreferences.getInstance().getUsername();

    public ConfirmTask(ResetKeysWrapper resetKeysWrapper) {
        this.mResetKeysWrapper = resetKeysWrapper;
    }

    public ConfirmTask(Lock lock, FirmwareUpdate firmwareUpdate) {
        this.mLock = lock;
        this.mFirmwareUpdate = firmwareUpdate;
    }

    public void execute(final ITaskCallback iTaskCallback) {
        MasterLockApp.get().inject(this);
        this.mSubscription = Subscriptions.empty();
        ResetKeysWrapper resetKeysWrapper = this.mResetKeysWrapper;
        if (resetKeysWrapper != null) {
            this.mSubscription = this.mKmsDeviceService.confirmKeyReset(resetKeysWrapper, this.username).observeOn(this.mScheduler.main()).subscribeOn(this.mScheduler.background()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
                public void onCompleted() {
                    MasterLockApp.get().getAnalytics().logEvent(Analytics.CATEGORY_MASTERLOCK_EVENT, Analytics.ACTION_RESET_KEYS, Analytics.ACTION_RESET_KEYS, 1);
                }

                public void onError(Throwable th) {
                    if (th.getMessage() != null) {
                        th.getMessage();
                    } else {
                        th.getClass().getSimpleName();
                    }
                    iTaskCallback.onFailure(null);
                }

                public void onNext(Boolean bool) {
                    iTaskCallback.onSuccess();
                }
            });
            return;
        }
        FirmwareUpdate firmwareUpdate = this.mFirmwareUpdate;
        if (firmwareUpdate != null) {
            Lock lock = this.mLock;
            if (lock != null) {
                this.mSubscription = this.mLockService.confirmFirmwareUpdate(lock, firmwareUpdate, this.username).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Lock>() {
                    public void onCompleted() {
                    }

                    public void onError(Throwable th) {
                        if (th.getMessage() != null) {
                            th.getMessage();
                        } else {
                            th.getClass().getSimpleName();
                        }
                        iTaskCallback.onFailure(null);
                    }

                    public void onNext(Lock lock) {
                        iTaskCallback.onSuccess();
                    }
                });
            }
        }
    }
}
