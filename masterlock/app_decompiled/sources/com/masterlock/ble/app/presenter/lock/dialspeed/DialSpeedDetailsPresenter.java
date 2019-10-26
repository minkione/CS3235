package com.masterlock.ble.app.presenter.lock.dialspeed;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.os.Handler;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.bus.LockSettingsEvent;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.provider.MasterlockContract.ProductCodes;
import com.masterlock.ble.app.screens.LockScreens.EditDialSpeedCodes;
import com.masterlock.ble.app.screens.SettingsScreens.LockSettings;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.lock.dialspeed.DialSpeedDetailsView;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class DialSpeedDetailsPresenter extends AuthenticatedPresenter<Lock, DialSpeedDetailsView> {
    @Inject
    Bus mBus;
    @Inject
    ContentResolver mContentResolver;
    @Inject
    LockService mLockService;
    final ContentObserver mObserver = new ContentObserver(new Handler()) {
        public void onChange(boolean z) {
            DialSpeedDetailsPresenter.this.reload();
        }
    };
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();

    public DialSpeedDetailsPresenter(DialSpeedDetailsView dialSpeedDetailsView, Lock lock) {
        super(lock, dialSpeedDetailsView);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        super.start();
        this.mBus.register(this);
        reload();
        this.mContentResolver.registerContentObserver(ProductCodes.buildProductCodesUri(((Lock) this.model).getLockId()), true, this.mObserver);
    }

    public void finish() {
        super.finish();
        this.mBus.unregister(this);
        this.mContentResolver.unregisterContentObserver(this.mObserver);
    }

    /* access modifiers changed from: private */
    public void reload() {
        if (this.model != null) {
            this.mSubscription.unsubscribe();
            this.mSubscription = this.mLockService.get(((Lock) this.model).getLockId()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Lock>() {
                public void onStart() {
                    DialSpeedDetailsPresenter.this.mBus.post(new ToggleProgressBarEvent(true));
                }

                public void onCompleted() {
                    DialSpeedDetailsPresenter.this.mBus.post(new ToggleProgressBarEvent(false));
                }

                public void onError(Throwable th) {
                    DialSpeedDetailsPresenter.this.mBus.post(new ToggleProgressBarEvent(false));
                    ((DialSpeedDetailsView) DialSpeedDetailsPresenter.this.view).displayError(ApiError.generateError(th));
                }

                public void onNext(Lock lock) {
                    if (lock == null) {
                        AppFlow.get(((DialSpeedDetailsView) DialSpeedDetailsPresenter.this.view).getContext()).goBack();
                        return;
                    }
                    DialSpeedDetailsPresenter.this.model = lock;
                    if (DialSpeedDetailsPresenter.this.view != null) {
                        ((DialSpeedDetailsView) DialSpeedDetailsPresenter.this.view).setUpViewWithLock(lock);
                    }
                }
            });
        }
    }

    @Subscribe
    public void transitionToLockSettings(LockSettingsEvent lockSettingsEvent) {
        AppFlow.get(((DialSpeedDetailsView) this.view).getContext()).goTo(new LockSettings((Lock) this.model));
    }

    public void transitionToEditCodes() {
        AppFlow.get(((DialSpeedDetailsView) this.view).getContext()).goTo(new EditDialSpeedCodes((Lock) this.model));
    }
}
