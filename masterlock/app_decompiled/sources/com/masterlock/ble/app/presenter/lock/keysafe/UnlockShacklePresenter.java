package com.masterlock.ble.app.presenter.lock.keysafe;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IBinder;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.provider.MasterlockContract.Locks;
import com.masterlock.ble.app.screens.LockScreens.UnlockShackle;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.scan.BackgroundScanService;
import com.masterlock.ble.app.service.scan.BackgroundScanService.LocalBinder;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.lock.keysafe.UnlockShackleView;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class UnlockShacklePresenter extends AuthenticatedPresenter<Lock, UnlockShackleView> {
    private boolean hasUnlocked = false;
    /* access modifiers changed from: private */
    public BackgroundScanService mBackgroundScanService;
    private ServiceConnection mBackgroundServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LocalBinder localBinder = (LocalBinder) iBinder;
            UnlockShacklePresenter.this.mBoundBackgroundScanServiceConnection = true;
            UnlockShacklePresenter.this.mBackgroundScanService = localBinder.getService();
            UnlockShacklePresenter.this.mBackgroundScanService.preventDoorUnlock(((Lock) UnlockShacklePresenter.this.model).getKmsDeviceKey().getDeviceId());
            UnlockShacklePresenter.this.unlockShackle();
        }

        public void onServiceDisconnected(ComponentName componentName) {
            UnlockShacklePresenter.this.mBackgroundScanService.preventDoorUnlock(null);
            UnlockShacklePresenter.this.mBoundBackgroundScanServiceConnection = false;
        }
    };
    /* access modifiers changed from: private */
    public boolean mBoundBackgroundScanServiceConnection;
    @Inject
    ContentResolver mContentResolver;
    @Inject
    Bus mEventBus;
    @Inject
    LockService mLockService;
    final ContentObserver mObserver = new ContentObserver(new Handler()) {
        public void onChange(boolean z) {
            UnlockShacklePresenter.this.reload();
        }
    };
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();

    public UnlockShacklePresenter(UnlockShackleView unlockShackleView) {
        super(((UnlockShackle) AppFlow.getScreen(unlockShackleView.getContext())).mLock, unlockShackleView);
        this.mEventBus.register(this);
    }

    public void start() {
        super.start();
        this.mContentResolver.registerContentObserver(Locks.buildLockUri(((Lock) this.model).getLockId()), true, this.mObserver);
        bindBackgroundScanService();
    }

    public void finish() {
        this.mSubscription.unsubscribe();
        this.mContentResolver.unregisterContentObserver(this.mObserver);
        this.hasUnlocked = false;
        if (this.mBoundBackgroundScanServiceConnection) {
            this.mBackgroundScanService.preventDoorUnlock(null);
            ((UnlockShackleView) this.view).getContext().unbindService(this.mBackgroundServiceConnection);
            this.mBoundBackgroundScanServiceConnection = false;
        }
        super.finish();
    }

    public void reload() {
        if (this.model != null) {
            this.mSubscription.unsubscribe();
            this.mSubscription = this.mLockService.get(((Lock) this.model).getLockId()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Lock>() {
                public void onStart() {
                    UnlockShacklePresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
                }

                public void onCompleted() {
                    UnlockShacklePresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                }

                public void onError(Throwable th) {
                    UnlockShacklePresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                    ((UnlockShackleView) UnlockShacklePresenter.this.view).displayError(th);
                }

                public void onNext(Lock lock) {
                    if (lock == null) {
                        AppFlow.get(((UnlockShackleView) UnlockShacklePresenter.this.view).getContext()).goBack();
                        return;
                    }
                    UnlockShacklePresenter.this.model = lock;
                    if (UnlockShacklePresenter.this.view != null) {
                        ((UnlockShackleView) UnlockShacklePresenter.this.view).updateView(lock);
                    }
                }
            });
        }
    }

    private void bindBackgroundScanService() {
        ((UnlockShackleView) this.view).getContext().bindService(new Intent(((UnlockShackleView) this.view).getContext(), BackgroundScanService.class), this.mBackgroundServiceConnection, 1);
    }

    public void unlockShackle() {
        this.mBackgroundScanService.unlockShackle((Lock) this.model);
        this.hasUnlocked = true;
    }

    public void tryLater() {
        this.mBackgroundScanService.abortUnlockShackle((Lock) this.model);
        AppFlow.get(((UnlockShackleView) this.view).getContext()).goBack();
    }

    public void onBackPressed() {
        this.mBackgroundScanService.abortUnlockShackle((Lock) this.model);
    }

    public boolean hasUnlocked() {
        return this.hasUnlocked;
    }
}
