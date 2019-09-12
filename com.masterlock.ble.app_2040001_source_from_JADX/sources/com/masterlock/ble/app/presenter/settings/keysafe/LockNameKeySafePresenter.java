package com.masterlock.ble.app.presenter.settings.keysafe;

import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.SettingsScreens.LockNameKeySafe;
import com.masterlock.ble.app.screens.SettingsScreens.LockSettings;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.settings.keysafe.LockNameKeySafeView;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class LockNameKeySafePresenter extends AuthenticatedPresenter<Lock, LockNameKeySafeView> {
    @Inject
    LockService mLockService;
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();

    public LockNameKeySafePresenter(LockNameKeySafeView lockNameKeySafeView) {
        super(lockNameKeySafeView);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        super.start();
        this.model = ((LockNameKeySafe) AppFlow.getScreen(((LockNameKeySafeView) this.view).getContext())).mLock;
        ((LockNameKeySafeView) this.view).updateName(((Lock) this.model).getName());
        ((LockNameKeySafeView) this.view).setLockName(((Lock) this.model).getName());
        ((LockNameKeySafeView) this.view).setDeviceId(((Lock) this.model).getKmsDeviceKey().getDeviceId());
    }

    public void updateName(String str) {
        ((Lock) this.model).setName(str);
        this.mSubscription = this.mLockService.updateApi((Lock) this.model).observeOn(this.mScheduler.main()).subscribeOn(this.mScheduler.background()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
            public void onStart() {
                LockNameKeySafePresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                LockNameKeySafePresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                LockNameKeySafePresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                if (LockNameKeySafePresenter.this.view != null) {
                    ((LockNameKeySafeView) LockNameKeySafePresenter.this.view).displayError(ApiError.generateError(th));
                }
            }

            public void onNext(Boolean bool) {
                if (LockNameKeySafePresenter.this.view == null) {
                    return;
                }
                if (bool.booleanValue()) {
                    ((LockNameKeySafeView) LockNameKeySafePresenter.this.view).displaySuccess();
                    AppFlow.get(((LockNameKeySafeView) LockNameKeySafePresenter.this.view).getContext()).resetTo(new LockSettings((Lock) LockNameKeySafePresenter.this.model));
                    return;
                }
                ((LockNameKeySafeView) LockNameKeySafePresenter.this.view).displayError(ApiError.generateError(new Throwable("Unable to save name")));
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
