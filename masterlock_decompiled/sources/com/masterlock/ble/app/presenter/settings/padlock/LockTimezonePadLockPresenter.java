package com.masterlock.ble.app.presenter.settings.padlock;

import com.masterlock.api.entity.KmsDeviceTrait;
import com.masterlock.api.entity.KmsUpdateTraitsRequest;
import com.masterlock.api.entity.Timezone;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.SettingsScreens.LockTimezonePadLock;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.TimezoneService;
import com.masterlock.ble.app.tape.UploadTask;
import com.masterlock.ble.app.tape.UploadTaskQueue;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.settings.padlock.LockTimezonePadLockView;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;
import java.util.List;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class LockTimezonePadLockPresenter extends AuthenticatedPresenter<Lock, LockTimezonePadLockView> {
    @Inject
    LockService mLockService;
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();
    @Inject
    UploadTaskQueue mUploadTaskQueue;
    @Inject
    TimezoneService timezoneService;

    public LockTimezonePadLockPresenter(LockTimezonePadLockView lockTimezonePadLockView) {
        super(lockTimezonePadLockView);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        super.start();
        this.model = ((LockTimezonePadLock) AppFlow.getScreen(((LockTimezonePadLockView) this.view).getContext())).mLock;
        getAllTimezones();
        ((LockTimezonePadLockView) this.view).setLockName(((Lock) this.model).getName());
        ((LockTimezonePadLockView) this.view).setDeviceId(((Lock) this.model).getKmsDeviceKey().getDeviceId());
    }

    public void updateTimezone(String str) {
        ((Lock) this.model).setTimezone(str);
        this.mSubscription.unsubscribe();
        this.mSubscription = this.mLockService.updateDb((Lock) this.model).observeOn(this.mScheduler.main()).subscribeOn(this.mScheduler.background()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
            public void onStart() {
                LockTimezonePadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                LockTimezonePadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                LockTimezonePadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                if (LockTimezonePadLockPresenter.this.view != null) {
                    ((LockTimezonePadLockView) LockTimezonePadLockPresenter.this.view).displayError(ApiError.generateError(th));
                }
            }

            public void onNext(Boolean bool) {
                if (LockTimezonePadLockPresenter.this.view == null) {
                    return;
                }
                if (bool.booleanValue()) {
                    ((LockTimezonePadLockView) LockTimezonePadLockPresenter.this.view).displaySuccess();
                    LockTimezonePadLockPresenter.this.mUploadTaskQueue.add(LockTimezonePadLockPresenter.this.produceAddUploadTask());
                    return;
                }
                ((LockTimezonePadLockView) LockTimezonePadLockPresenter.this.view).displayError(ApiError.generateError(new Throwable("Unable to save timezone")));
            }
        });
    }

    public void getAllTimezones() {
        this.mSubscription.unsubscribe();
        this.mSubscription = this.timezoneService.getTimezones().observeOn(this.mScheduler.main()).subscribeOn(this.mScheduler.background()).subscribe((Subscriber<? super T>) new Subscriber<List<Timezone>>() {
            public void onStart() {
                LockTimezonePadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                LockTimezonePadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                LockTimezonePadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                if (LockTimezonePadLockPresenter.this.view != null) {
                    ((LockTimezonePadLockView) LockTimezonePadLockPresenter.this.view).displayError(ApiError.generateError(th));
                }
            }

            public void onNext(List<Timezone> list) {
                if (LockTimezonePadLockPresenter.this.view != null) {
                    ((LockTimezonePadLockView) LockTimezonePadLockPresenter.this.view).setTimezones(list);
                    ((LockTimezonePadLockView) LockTimezonePadLockPresenter.this.view).updateTimezone(((Lock) LockTimezonePadLockPresenter.this.model).getTimezone());
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public UploadTask produceAddUploadTask() {
        return new UploadTask(new KmsUpdateTraitsRequest((Lock) this.model, KmsDeviceTrait.generateTraitsForLock((Lock) this.model, KmsDeviceTrait.TIMEZONE)));
    }

    public void finish() {
        this.mSubscription.unsubscribe();
        super.finish();
    }
}
