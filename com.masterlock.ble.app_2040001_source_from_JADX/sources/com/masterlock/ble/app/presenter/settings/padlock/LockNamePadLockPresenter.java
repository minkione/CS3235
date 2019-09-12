package com.masterlock.ble.app.presenter.settings.padlock;

import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.SettingsScreens.LockNamePadLock;
import com.masterlock.ble.app.screens.SettingsScreens.LockSettings;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.settings.padlock.LockNamePadLockView;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class LockNamePadLockPresenter extends AuthenticatedPresenter<Lock, LockNamePadLockView> {
    @Inject
    LockService mLockService;
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();

    public LockNamePadLockPresenter(LockNamePadLockView lockNamePadLockView) {
        super(lockNamePadLockView);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        super.start();
        this.model = ((LockNamePadLock) AppFlow.getScreen(((LockNamePadLockView) this.view).getContext())).mLock;
        ((LockNamePadLockView) this.view).updateName(((Lock) this.model).getName());
        ((LockNamePadLockView) this.view).setLockName(((Lock) this.model).getName());
        ((LockNamePadLockView) this.view).setDeviceId(((Lock) this.model).getKmsDeviceKey().getDeviceId());
    }

    public void updateName(String str) {
        ((Lock) this.model).setName(str);
        this.mSubscription = this.mLockService.updateApi((Lock) this.model).observeOn(this.mScheduler.main()).subscribeOn(this.mScheduler.background()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
            public void onStart() {
                LockNamePadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                LockNamePadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                LockNamePadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                if (LockNamePadLockPresenter.this.view != null) {
                    ((LockNamePadLockView) LockNamePadLockPresenter.this.view).displayError(ApiError.generateError(th));
                }
            }

            public void onNext(Boolean bool) {
                if (LockNamePadLockPresenter.this.view == null) {
                    return;
                }
                if (bool.booleanValue()) {
                    ((LockNamePadLockView) LockNamePadLockPresenter.this.view).displaySuccess();
                    AppFlow.get(((LockNamePadLockView) LockNamePadLockPresenter.this.view).getContext()).resetTo(new LockSettings((Lock) LockNamePadLockPresenter.this.model));
                    return;
                }
                ((LockNamePadLockView) LockNamePadLockPresenter.this.view).displayError(ApiError.generateError(new Throwable("Unable to save name")));
            }
        });
    }

    public void finish() {
        this.mSubscription.unsubscribe();
        super.finish();
    }

    public boolean isSameLockname(String str) {
        return ((Lock) this.model).getName().equals(str);
    }
}
