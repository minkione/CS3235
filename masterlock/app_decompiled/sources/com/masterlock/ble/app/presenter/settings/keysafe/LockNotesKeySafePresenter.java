package com.masterlock.ble.app.presenter.settings.keysafe;

import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.SettingsScreens.LockNotesKeySafe;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.settings.keysafe.LockNotesKeySafeView;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class LockNotesKeySafePresenter extends AuthenticatedPresenter<Lock, LockNotesKeySafeView> {
    @Inject
    LockService mLockService;
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();

    public LockNotesKeySafePresenter(LockNotesKeySafeView lockNotesKeySafeView) {
        super(lockNotesKeySafeView);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        super.start();
        this.model = ((LockNotesKeySafe) AppFlow.getScreen(((LockNotesKeySafeView) this.view).getContext())).mLock;
        ((LockNotesKeySafeView) this.view).updateNotes(((Lock) this.model).getNotes());
        ((LockNotesKeySafeView) this.view).setLockName(((Lock) this.model).getName());
        ((LockNotesKeySafeView) this.view).setDeviceId(((Lock) this.model).getKmsDeviceKey().getDeviceId());
    }

    public void updateNotes(String str) {
        ((Lock) this.model).setNotes(str);
        this.mSubscription = this.mLockService.updateApi((Lock) this.model).observeOn(this.mScheduler.main()).subscribeOn(this.mScheduler.background()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
            public void onStart() {
                LockNotesKeySafePresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                LockNotesKeySafePresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                LockNotesKeySafePresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                if (LockNotesKeySafePresenter.this.view != null) {
                    ((LockNotesKeySafeView) LockNotesKeySafePresenter.this.view).displayError(ApiError.generateError(th));
                }
            }

            public void onNext(Boolean bool) {
                if (LockNotesKeySafePresenter.this.view == null) {
                    return;
                }
                if (bool.booleanValue()) {
                    ((LockNotesKeySafeView) LockNotesKeySafePresenter.this.view).displaySuccess();
                } else {
                    ((LockNotesKeySafeView) LockNotesKeySafePresenter.this.view).displayError(ApiError.generateError(new Throwable("Unable to save notes")));
                }
            }
        });
    }

    public void finish() {
        this.mSubscription.unsubscribe();
        super.finish();
    }
}
