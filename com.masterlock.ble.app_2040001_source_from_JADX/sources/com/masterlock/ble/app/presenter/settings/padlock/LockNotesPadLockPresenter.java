package com.masterlock.ble.app.presenter.settings.padlock;

import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.SettingsScreens.LockNotesPadLock;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.settings.padlock.LockNotesPadLockView;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class LockNotesPadLockPresenter extends AuthenticatedPresenter<Lock, LockNotesPadLockView> {
    @Inject
    LockService mLockService;
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();

    public LockNotesPadLockPresenter(LockNotesPadLockView lockNotesPadLockView) {
        super(lockNotesPadLockView);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        super.start();
        this.model = ((LockNotesPadLock) AppFlow.getScreen(((LockNotesPadLockView) this.view).getContext())).mLock;
        ((LockNotesPadLockView) this.view).updateNotes(((Lock) this.model).getNotes());
        ((LockNotesPadLockView) this.view).setLockName(((Lock) this.model).getName());
        ((LockNotesPadLockView) this.view).setDeviceId(((Lock) this.model).getKmsDeviceKey().getDeviceId());
    }

    public void updateNotes(String str) {
        ((Lock) this.model).setNotes(str);
        this.mSubscription = this.mLockService.updateApi((Lock) this.model).observeOn(this.mScheduler.main()).subscribeOn(this.mScheduler.background()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
            public void onStart() {
                LockNotesPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                LockNotesPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                LockNotesPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                if (LockNotesPadLockPresenter.this.view != null) {
                    ((LockNotesPadLockView) LockNotesPadLockPresenter.this.view).displayError(ApiError.generateError(th));
                }
            }

            public void onNext(Boolean bool) {
                if (LockNotesPadLockPresenter.this.view == null) {
                    return;
                }
                if (bool.booleanValue()) {
                    ((LockNotesPadLockView) LockNotesPadLockPresenter.this.view).displaySuccess();
                } else {
                    ((LockNotesPadLockView) LockNotesPadLockPresenter.this.view).displayError(ApiError.generateError(new Throwable("Unable to save notes")));
                }
            }
        });
    }

    public void finish() {
        this.mSubscription.unsubscribe();
        super.finish();
    }
}
