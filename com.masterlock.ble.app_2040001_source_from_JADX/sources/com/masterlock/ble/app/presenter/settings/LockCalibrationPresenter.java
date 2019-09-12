package com.masterlock.ble.app.presenter.settings;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.bus.ConfigDeviceFoundEvent;
import com.masterlock.ble.app.bus.DeviceConfigSuccessEvent;
import com.masterlock.ble.app.bus.DeviceTimeoutEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.LockScreens.LockList;
import com.masterlock.ble.app.screens.SettingsScreens.CalibrateLock;
import com.masterlock.ble.app.service.KMSDeviceService;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.scan.BackgroundScanService;
import com.masterlock.ble.app.service.scan.BackgroundScanService.LocalBinder;
import com.masterlock.ble.app.tape.ConfirmTaskQueue;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.settings.LockCalibrationView;
import com.masterlock.ble.app.view.settings.LockCalibrationView.LockCalibrationStep;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import javax.inject.Inject;
import p009rx.Subscription;
import p009rx.functions.Action0;
import p009rx.functions.Action1;
import p009rx.subscriptions.Subscriptions;

public class LockCalibrationPresenter extends AuthenticatedPresenter<Lock, LockCalibrationView> {
    /* access modifiers changed from: private */
    public BackgroundScanService mBackgroundScanService;
    private ServiceConnection mBackgroundServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LocalBinder localBinder = (LocalBinder) iBinder;
            LockCalibrationPresenter.this.mBoundBackgroundScanServiceConnection = true;
            LockCalibrationPresenter.this.mBackgroundScanService = localBinder.getService();
            LockCalibrationPresenter.this.mBackgroundScanService.preventUnlock(((Lock) LockCalibrationPresenter.this.model).getKmsDeviceKey().getDeviceId());
            LockCalibrationPresenter.this.lookForLock();
        }

        public void onServiceDisconnected(ComponentName componentName) {
            LockCalibrationPresenter.this.mBackgroundScanService.preventUnlock(null);
            LockCalibrationPresenter.this.mBoundBackgroundScanServiceConnection = false;
        }
    };
    /* access modifiers changed from: private */
    public boolean mBoundBackgroundScanServiceConnection;
    @Inject
    ConfirmTaskQueue mConfirmTaskQueue;
    @Inject
    Bus mEventBus;
    private boolean mFromAddLock = false;
    @Inject
    KMSDeviceService mKmsDeviceService;
    @Inject
    LockService mLockService;
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();

    static /* synthetic */ void lambda$removeCalibration$1(Void voidR) {
    }

    static /* synthetic */ void lambda$tryAgain$0(Void voidR) {
    }

    public LockCalibrationPresenter(LockCalibrationView lockCalibrationView) {
        super(lockCalibrationView);
        MasterLockApp.get().inject(this);
        CalibrateLock calibrateLock = (CalibrateLock) AppFlow.getScreen(lockCalibrationView.getContext());
        this.model = calibrateLock.mLock;
        this.mFromAddLock = calibrateLock.fromAddLock;
        lockCalibrationView.setLockName(((Lock) this.model).getName());
        lockCalibrationView.setDeviceId(((Lock) this.model).getKmsDeviceKey().getDeviceId());
        this.mEventBus.register(this);
    }

    public void start() {
        super.start();
        if (((Lock) this.model).getCalibrationInfo().hasSkipped() || ((Lock) this.model).getCalibrationInfo().getValue() != 0) {
            ((LockCalibrationView) this.view).updateView(LockCalibrationStep.STEP_SUMMARY);
        } else {
            bindBackgroundScanService();
        }
    }

    public void finish() {
        this.mSubscription.unsubscribe();
        if (this.mBoundBackgroundScanServiceConnection) {
            this.mBackgroundScanService.preventUnlock(null);
            ((LockCalibrationView) this.view).getContext().unbindService(this.mBackgroundServiceConnection);
            this.mBoundBackgroundScanServiceConnection = false;
        }
        this.mEventBus.unregister(this);
        super.finish();
    }

    private void bindBackgroundScanService() {
        ((LockCalibrationView) this.view).getContext().bindService(new Intent(((LockCalibrationView) this.view).getContext(), BackgroundScanService.class), this.mBackgroundServiceConnection, 1);
    }

    /* access modifiers changed from: private */
    public void lookForLock() {
        ((LockCalibrationView) this.view).updateView(LockCalibrationStep.STEP_WAKE);
        this.mBackgroundScanService.lookForCalibration((Lock) this.model);
    }

    public void calibrate() {
        ((LockCalibrationView) this.view).updateView(LockCalibrationStep.STEP_CALIBRATING);
        this.mBackgroundScanService.calibrate((Lock) this.model);
    }

    public void tryAgain() {
        BackgroundScanService backgroundScanService = this.mBackgroundScanService;
        if (backgroundScanService != null) {
            backgroundScanService.abortCalibration((Lock) this.model);
        }
        if (!this.mBoundBackgroundScanServiceConnection) {
            bindBackgroundScanService();
        } else {
            lookForLock();
        }
        ((Lock) this.model).setRssiThreshold(Integer.valueOf(0));
        this.mLockService.updateRssiThreshold((Lock) this.model).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Action1<? super T>) $$Lambda$LockCalibrationPresenter$z9fiwaJAU_xoesQDhXUqTgj8mo.INSTANCE, (Action1<Throwable>) $$Lambda$WyLYczv0rYB16nM4g5IsYSSBzSI.INSTANCE);
    }

    public void tryLater() {
        this.mBackgroundScanService.abortCalibration((Lock) this.model);
        onBackPressed();
    }

    public void removeCalibration() {
        ((Lock) this.model).setRssiThreshold(Integer.valueOf(0));
        this.mLockService.updateRssiThreshold((Lock) this.model).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe($$Lambda$LockCalibrationPresenter$_exAB2g6vNZrXpVxqzUsOjTC214.INSTANCE, $$Lambda$WyLYczv0rYB16nM4g5IsYSSBzSI.INSTANCE, new Action0() {
            public final void call() {
                LockCalibrationPresenter.lambda$removeCalibration$2(LockCalibrationPresenter.this);
            }
        });
    }

    public static /* synthetic */ void lambda$removeCalibration$2(LockCalibrationPresenter lockCalibrationPresenter) {
        ((LockCalibrationView) lockCalibrationPresenter.view).showRemovedCalibrationConfirmation();
        AppFlow.get(((LockCalibrationView) lockCalibrationPresenter.view).getContext()).goBack();
    }

    public void onBackPressed() {
        if (this.mFromAddLock) {
            AppFlow.get(((LockCalibrationView) this.view).getContext()).resetTo(new LockList());
        } else {
            AppFlow.get(((LockCalibrationView) this.view).getContext()).goBack();
        }
    }

    @Subscribe
    public void onLockFound(ConfigDeviceFoundEvent configDeviceFoundEvent) {
        if (((Lock) this.model).getLockId().equals(configDeviceFoundEvent.getLock().getLockId()) && !configDeviceFoundEvent.isReconfiguringLock()) {
            ((LockCalibrationView) this.view).updateView(LockCalibrationStep.STEP_STAND_BACK);
        }
    }

    @Subscribe
    public void onDeviceTimeoutEvent(DeviceTimeoutEvent deviceTimeoutEvent) {
        ((LockCalibrationView) this.view).updateView(LockCalibrationStep.STEP_NOT_FOUND);
    }

    @Subscribe
    public void onConfigSuccess(DeviceConfigSuccessEvent deviceConfigSuccessEvent) {
        ((LockCalibrationView) this.view).updateView(LockCalibrationStep.STEP_COMPLETE);
    }
}
