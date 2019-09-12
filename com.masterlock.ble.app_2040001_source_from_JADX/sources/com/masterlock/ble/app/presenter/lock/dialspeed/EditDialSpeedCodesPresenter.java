package com.masterlock.ble.app.presenter.lock.dialspeed;

import android.util.Log;
import com.masterlock.api.entity.KmsDeviceTrait;
import com.masterlock.api.entity.KmsUpdateTraitsRequest;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.tape.UploadTask;
import com.masterlock.ble.app.tape.UploadTaskQueue;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.lock.dialspeed.EditDialSpeedCodesView;
import com.masterlock.core.Lock;
import com.masterlock.core.ProductCode;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class EditDialSpeedCodesPresenter extends AuthenticatedPresenter<Lock, EditDialSpeedCodesView> {
    @Inject
    Bus mBus;
    private boolean mIsBiometric;
    @Inject
    LockService mLockService;
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();
    @Inject
    UploadTaskQueue mUploadTaskQueue;

    public EditDialSpeedCodesPresenter(Lock lock, EditDialSpeedCodesView editDialSpeedCodesView) {
        super(lock, editDialSpeedCodesView);
        if (lock.isBiometricPadLock()) {
            editDialSpeedCodesView.findViewById(C1075R.C1077id.dialspeed_extra_codes_container).setVisibility(8);
            this.mIsBiometric = true;
        } else {
            editDialSpeedCodesView.findViewById(C1075R.C1077id.dialspeed_extra_codes_container).setVisibility(0);
            this.mIsBiometric = false;
        }
        MasterLockApp.get().inject(this);
    }

    public void start() {
        super.start();
        this.mBus.register(this);
    }

    public void saveCodes(List<ProductCode> list) {
        if (list == null) {
            ((Lock) this.model).setProductCodes(new ArrayList());
        } else {
            ((Lock) this.model).setProductCodes(list);
        }
        this.mSubscription.unsubscribe();
        Log.w("BIOMETRIC", ((Lock) this.model).toString());
        if (this.mIsBiometric) {
            ((Lock) this.model).setPrimaryCode(((ProductCode) list.get(0)).getValue());
            ((Lock) this.model).setPrimaryCodeCounter(((Lock) this.model).getPrimaryCodeCounter() + 1);
            this.mUploadTaskQueue.add(new UploadTask(new KmsUpdateTraitsRequest((Lock) this.model, KmsDeviceTrait.generateTraitForLock((Lock) this.model, KmsDeviceTrait.PRIMARYCODE))));
            this.mLockService.updateDb((Lock) this.model).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
                public void onNext(Boolean bool) {
                }

                public void onStart() {
                    super.onStart();
                }

                public void onCompleted() {
                    AppFlow.get(((EditDialSpeedCodesView) EditDialSpeedCodesPresenter.this.view).getContext()).goBack();
                }

                public void onError(Throwable th) {
                    EditDialSpeedCodesPresenter.this.mBus.post(new ToggleProgressBarEvent(false));
                    ((EditDialSpeedCodesView) EditDialSpeedCodesPresenter.this.view).displayError(th);
                }
            });
            return;
        }
        this.mSubscription = this.mLockService.updateApi((Lock) this.model).observeOn(this.mScheduler.main()).subscribeOn(this.mScheduler.background()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
            public void onNext(Boolean bool) {
            }

            public void onStart() {
                super.onStart();
                EditDialSpeedCodesPresenter.this.mBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                EditDialSpeedCodesPresenter.this.mBus.post(new ToggleProgressBarEvent(false));
                AppFlow.get(((EditDialSpeedCodesView) EditDialSpeedCodesPresenter.this.view).getContext()).goBack();
            }

            public void onError(Throwable th) {
                EditDialSpeedCodesPresenter.this.mBus.post(new ToggleProgressBarEvent(false));
                ((EditDialSpeedCodesView) EditDialSpeedCodesPresenter.this.view).displayError(th);
            }
        });
    }

    public void finish() {
        super.finish();
        this.mBus.unregister(this);
        this.mSubscription.unsubscribe();
    }
}
