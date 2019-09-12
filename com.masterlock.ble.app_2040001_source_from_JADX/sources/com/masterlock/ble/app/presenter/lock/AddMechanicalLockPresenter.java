package com.masterlock.ble.app.presenter.lock;

import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.analytics.Analytics;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.screens.LockScreens.LockList;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.lock.AddMechanicalLockView;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.android.schedulers.AndroidSchedulers;
import p009rx.schedulers.Schedulers;
import p009rx.subscriptions.Subscriptions;

public class AddMechanicalLockPresenter extends Presenter<Lock, AddMechanicalLockView> {
    @Inject
    Bus mEventBus;
    @Inject
    LockService mLockService;
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();

    public AddMechanicalLockPresenter(AddMechanicalLockView addMechanicalLockView, Lock lock) {
        super(lock, addMechanicalLockView);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        this.mEventBus.register(this);
        if (this.model != null) {
            ((AddMechanicalLockView) this.view).updateViewWithLock((Lock) this.model);
        }
    }

    public void finish() {
        super.finish();
        this.mEventBus.unregister(this);
    }

    public void updateLock(String str, String str2) {
        ((Lock) this.model).setName(str);
        ((Lock) this.model).setNotes(str2);
        this.mSubscription.unsubscribe();
        this.mSubscription = this.mLockService.updateApi((Lock) this.model).subscribeOn(Schedulers.m220io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
            public void onNext(Boolean bool) {
            }

            public void onStart() {
                ((AddMechanicalLockView) AddMechanicalLockPresenter.this.view).setLoading(true);
                AddMechanicalLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                AddMechanicalLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                if (AddMechanicalLockPresenter.this.view != null) {
                    ((AddMechanicalLockView) AddMechanicalLockPresenter.this.view).setLoading(false);
                    AppFlow.get(((AddMechanicalLockView) AddMechanicalLockPresenter.this.view).getContext()).goBack();
                }
            }

            public void onError(Throwable th) {
                ((AddMechanicalLockView) AddMechanicalLockPresenter.this.view).displayError(th);
            }
        });
    }

    public void createLock(String str, String str2) {
        this.mSubscription.unsubscribe();
        this.mSubscription = this.mLockService.createMechanicalLock(str, str2).subscribeOn(Schedulers.m220io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Subscriber<? super T>) new Subscriber<Lock>() {
            public void onNext(Lock lock) {
            }

            public void onStart() {
                ((AddMechanicalLockView) AddMechanicalLockPresenter.this.view).setLoading(true);
                AddMechanicalLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                AddMechanicalLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                MasterLockApp.get().getAnalytics().logEvent(Analytics.CATEGORY_MASTERLOCK_EVENT, Analytics.ACTION_ADD_LOCK, Analytics.ACTION_ADD_LOCK, 1);
                if (AddMechanicalLockPresenter.this.view != null) {
                    ((AddMechanicalLockView) AddMechanicalLockPresenter.this.view).setLoading(false);
                    AppFlow.get(((AddMechanicalLockView) AddMechanicalLockPresenter.this.view).getContext()).resetTo(new LockList());
                }
            }

            public void onError(Throwable th) {
                AddMechanicalLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ApiError generateError = ApiError.generateError(th);
                if (!generateError.isHandled() && AddMechanicalLockPresenter.this.view != null) {
                    ((AddMechanicalLockView) AddMechanicalLockPresenter.this.view).setLoading(false);
                    ((AddMechanicalLockView) AddMechanicalLockPresenter.this.view).displayError(generateError);
                }
            }
        });
    }
}
