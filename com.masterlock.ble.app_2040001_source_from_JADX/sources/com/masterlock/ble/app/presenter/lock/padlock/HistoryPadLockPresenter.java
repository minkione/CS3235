package com.masterlock.ble.app.presenter.lock.padlock;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.os.Handler;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.provider.MasterlockContract.Locks;
import com.masterlock.ble.app.service.KMSDeviceLogService;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.lock.padlock.HistoryPadLockView;
import com.masterlock.core.KmsLogEntry;
import com.masterlock.core.Lock;
import com.squareup.otto.Bus;
import java.util.List;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class HistoryPadLockPresenter extends AuthenticatedPresenter<Lock, HistoryPadLockView> {
    @Inject
    ContentResolver mContentResolver;
    @Inject
    Bus mEventBus;
    @Inject
    LockService mLockService;
    @Inject
    KMSDeviceLogService mLogService;
    private Subscription mReloadSubscription = Subscriptions.empty();
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();
    final ContentObserver observer = new ContentObserver(new Handler()) {
        public void onChange(boolean z) {
            HistoryPadLockPresenter.this.reload();
        }
    };

    public HistoryPadLockPresenter(Lock lock, HistoryPadLockView historyPadLockView) {
        super(lock, historyPadLockView);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        super.start();
        this.mEventBus.register(this);
        refresh();
        this.mContentResolver.registerContentObserver(Locks.buildLockUri(((Lock) this.model).getLockId()), true, this.observer);
    }

    public void refresh() {
        if (this.model != null) {
            this.mSubscription.unsubscribe();
            this.mSubscription = this.mLogService.getLogs(((Lock) this.model).getKmsId()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<List<KmsLogEntry>>() {
                public void onNext(List<KmsLogEntry> list) {
                }

                public void onStart() {
                    HistoryPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
                }

                public void onCompleted() {
                    HistoryPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                    HistoryPadLockPresenter.this.reload();
                }

                public void onError(Throwable th) {
                    HistoryPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                    ((HistoryPadLockView) HistoryPadLockPresenter.this.view).displayError(ApiError.generateError(th));
                    HistoryPadLockPresenter.this.reload();
                }
            });
        }
    }

    public void reload() {
        if (this.model != null) {
            this.mReloadSubscription.unsubscribe();
            this.mReloadSubscription = this.mLockService.getWithFullLogs(((Lock) this.model).getLockId()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Lock>() {
                public void onStart() {
                    HistoryPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
                }

                public void onCompleted() {
                    HistoryPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                }

                public void onError(Throwable th) {
                    HistoryPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                    ((HistoryPadLockView) HistoryPadLockPresenter.this.view).displayError(ApiError.generateError(th));
                }

                public void onNext(Lock lock) {
                    HistoryPadLockPresenter.this.model = lock;
                    if (HistoryPadLockPresenter.this.view != null) {
                        ((HistoryPadLockView) HistoryPadLockPresenter.this.view).updateView((Lock) HistoryPadLockPresenter.this.model);
                    }
                }
            });
        }
    }

    public void finish() {
        this.mContentResolver.unregisterContentObserver(this.observer);
        this.mSubscription.unsubscribe();
        this.mReloadSubscription.unsubscribe();
        super.finish();
        this.mEventBus.unregister(this);
    }

    public Lock getModel() {
        return (Lock) this.model;
    }
}
