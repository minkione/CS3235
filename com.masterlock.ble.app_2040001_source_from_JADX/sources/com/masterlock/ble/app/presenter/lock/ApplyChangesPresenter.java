package com.masterlock.ble.app.presenter.lock;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import com.masterlock.api.entity.CommandsResponse;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.analytics.Analytics;
import com.masterlock.ble.app.bus.ConfigDeviceFoundEvent;
import com.masterlock.ble.app.bus.DeviceConfigSuccessEvent;
import com.masterlock.ble.app.bus.DeviceTimeoutEvent;
import com.masterlock.ble.app.bus.FirmwareCommandSuccessEvent;
import com.masterlock.ble.app.bus.FirmwareUpdateBeginEvent;
import com.masterlock.ble.app.bus.FirmwareUpdateStopEvent;
import com.masterlock.ble.app.bus.FirmwareUpdateSuccessEvent;
import com.masterlock.ble.app.bus.ForceScanEvent;
import com.masterlock.ble.app.bus.ForceStopScanEvent;
import com.masterlock.ble.app.bus.LocationPermissionEvent;
import com.masterlock.ble.app.bus.ResetKeyEvent;
import com.masterlock.ble.app.command.LockConfigListener;
import com.masterlock.ble.app.command.ResetKeysWrapper;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.LockScreens.ApplyChanges;
import com.masterlock.ble.app.screens.LockScreens.LockDetailsKeySafe;
import com.masterlock.ble.app.screens.LockScreens.LockDetailsPadLock;
import com.masterlock.ble.app.screens.SettingsScreens.AboutLockKeySafe;
import com.masterlock.ble.app.screens.SettingsScreens.AboutLockPadLock;
import com.masterlock.ble.app.service.KMSDeviceService;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.scan.BackgroundScanService;
import com.masterlock.ble.app.service.scan.BackgroundScanService.LocalBinder;
import com.masterlock.ble.app.tape.ConfirmTask;
import com.masterlock.ble.app.tape.ConfirmTaskQueue;
import com.masterlock.ble.app.util.FileUtil;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.util.LockUpdateUtil;
import com.masterlock.ble.app.view.lock.ApplyChangesView;
import com.masterlock.core.FirmwareUpdate;
import com.masterlock.core.Lock;
import com.masterlock.core.LockConfigAction;
import com.masterlock.core.LockMode;
import com.masterlock.core.LockStatus;
import com.masterlock.core.SecondaryCodeIndex;
import com.masterlock.core.SecondaryCodesUtil;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import p009rx.Subscription;
import p009rx.functions.Action0;
import p009rx.functions.Action1;
import p009rx.schedulers.Schedulers;
import p009rx.subscriptions.Subscriptions;

public class ApplyChangesPresenter extends AuthenticatedPresenter<Lock, ApplyChangesView> implements LockConfigListener {
    private static final String LOG_TAG = "com.masterlock.ble.app.presenter.lock.ApplyChangesPresenter";
    private static final int SYNCING_TIMEOUT_IN_MILLIS = 1200000;
    /* access modifiers changed from: private */
    public BackgroundScanService mBackgroundScanService;
    private ServiceConnection mBackgroundServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LocalBinder localBinder = (LocalBinder) iBinder;
            ApplyChangesPresenter.this.mBoundBackgroundScanServiceConnection = true;
            ApplyChangesPresenter.this.mBackgroundScanService = localBinder.getService();
            ApplyChangesPresenter.this.mBackgroundScanService.preventUnlock(((Lock) ApplyChangesPresenter.this.model).getKmsDeviceKey().getDeviceId());
            ApplyChangesPresenter.this.performAction();
        }

        public void onServiceDisconnected(ComponentName componentName) {
            ApplyChangesPresenter.this.mBackgroundScanService.preventUnlock(null);
            ApplyChangesPresenter.this.mBoundBackgroundScanServiceConnection = false;
        }
    };
    /* access modifiers changed from: private */
    public boolean mBoundBackgroundScanServiceConnection;
    @Inject
    ConfirmTaskQueue mConfirmTaskQueue;
    @Inject
    Bus mEventBus;
    private boolean mFinishedUpdating;
    FirmwareUpdate mFirmwareUpdate;
    private Handler mHandler = new Handler();
    @Inject
    KMSDeviceService mKmsDeviceService;
    private LockConfigAction mLockConfigAction;
    @Inject
    LockService mLockService;
    @Inject
    IScheduler mScheduler;
    private SecondaryCodeIndex mSecondaryCodeIndex;
    private Subscription mSubscription = Subscriptions.empty();
    private Runnable mSyncingRunnable;
    private boolean mUpdateStarted = false;

    public ApplyChangesPresenter(ApplyChangesView applyChangesView) {
        super(applyChangesView);
        ApplyChanges applyChanges = (ApplyChanges) AppFlow.getScreen(applyChangesView.getContext());
        this.model = applyChanges.mLock;
        this.mLockConfigAction = applyChanges.mLockConfigAction;
        this.mEventBus.register(this);
        this.mFinishedUpdating = false;
    }

    public void start() {
        super.start();
        bindBackgroundScanService();
        if (this.mLockConfigAction == LockConfigAction.UPDATE_FIRMWARE) {
            goToInstallView();
        }
    }

    public void finish() {
        this.mSubscription.unsubscribe();
        BackgroundScanService backgroundScanService = this.mBackgroundScanService;
        backgroundScanService.isUpdatingLock = false;
        backgroundScanService.abortFirmwareUpdateCommand((Lock) this.model);
        if (this.mBoundBackgroundScanServiceConnection) {
            this.mBackgroundScanService.preventUnlock(null);
            abortPendingAction();
            ((ApplyChangesView) this.view).getContext().unbindService(this.mBackgroundServiceConnection);
            this.mBoundBackgroundScanServiceConnection = false;
        }
        try {
            this.mEventBus.unregister(this);
        } catch (IllegalArgumentException unused) {
            Log.d(LOG_TAG, "mEventBus already unregistered");
        }
        super.finish();
    }

    public void tryLater() {
        this.mUpdateStarted = false;
        Lock abortPendingAction = abortPendingAction();
        if (abortPendingAction != null && !((Lock) this.model).equals(abortPendingAction)) {
            return;
        }
        if (this.mLockConfigAction == LockConfigAction.UPDATE_FIRMWARE) {
            AppFlow.get(((ApplyChangesView) this.view).getContext()).resetTo(((Lock) this.model).isPadLock() ? new AboutLockPadLock((Lock) this.model) : new AboutLockKeySafe((Lock) this.model));
        } else {
            AppFlow.get(((ApplyChangesView) this.view).getContext()).goBack();
        }
    }

    public void tryAgain() {
        if (!this.mBoundBackgroundScanServiceConnection) {
            bindBackgroundScanService();
        } else if (this.mLockConfigAction == LockConfigAction.UPDATE_FIRMWARE) {
            ((Lock) this.model).setNumberOfCommands(0);
            this.mBackgroundScanService.startTimeoutTimerForFirmwareUpdate((Lock) this.model);
            performAction();
            goToInstallView();
        } else {
            performAction();
        }
        ((ApplyChangesView) this.view).displaySearching();
    }

    private Lock abortPendingAction() {
        Log.d(LOG_TAG, "Aborting pending action");
        switch (this.mLockConfigAction) {
            case PRIMARY_CODE:
                return this.mBackgroundScanService.abortUpdatePrimaryCode((Lock) this.model);
            case RELOCK_TIME:
                return this.mBackgroundScanService.abortRelockTime((Lock) this.model);
            case LOCK_MODE:
                ((Lock) this.model).setLockMode(((Lock) this.model).getLockMode() == LockMode.TOUCH ? LockMode.PROXIMITYSWIPE : LockMode.TOUCH);
                return this.mBackgroundScanService.abortUpdateLockMode((Lock) this.model);
            case RESET_KEYS:
                return this.mBackgroundScanService.abortResetKeys((Lock) this.model);
            case UPDATE_FIRMWARE:
                this.mEventBus.post(new FirmwareUpdateStopEvent(true));
                return this.mBackgroundScanService.abortFirmwareUpdateCommand((Lock) this.model);
            case SECONDARY_CODES:
                return this.mBackgroundScanService.abortUpdateSecondaryCodes((Lock) this.model);
            default:
                return null;
        }
    }

    public void summaryOk() {
        AppFlow.get(((ApplyChangesView) this.view).getContext()).resetTo(((Lock) this.model).isPadLock() ? new LockDetailsPadLock((Lock) this.model, false) : new LockDetailsKeySafe((Lock) this.model, false));
        this.mBackgroundScanService.isUpdatingLock = false;
    }

    @Subscribe
    public void onLockFound(ConfigDeviceFoundEvent configDeviceFoundEvent) {
        Log.d(LOG_TAG, "On Lock Found");
        if (((Lock) this.model).getLockId().equals(configDeviceFoundEvent.getLock().getLockId()) && !configDeviceFoundEvent.isReconfiguringLock()) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("CONFIG ACTION: ");
            sb.append(configDeviceFoundEvent.getLockConfigAction());
            sb.append("\nIs updating lock: ");
            sb.append(configDeviceFoundEvent.isReconfiguringLock());
            Log.d(str, sb.toString());
            if (configDeviceFoundEvent.getLockConfigAction() != null && configDeviceFoundEvent.getLockConfigAction().equals(LockConfigAction.LOCK_MODE)) {
                ((ApplyChangesView) this.view).displaySyncing();
            } else if (this.mLockConfigAction.equals(LockConfigAction.UPDATE_FIRMWARE)) {
                ((ApplyChangesView) this.view).displayFirmwareSyncing(((Lock) this.model).getTotalNumberOfCommands());
            } else {
                ((ApplyChangesView) this.view).displaySyncing();
            }
        }
    }

    @Subscribe
    public void onConfigError(DeviceTimeoutEvent deviceTimeoutEvent) {
        if (((Lock) this.model).getLockId().equals(deviceTimeoutEvent.getLock().getLockId())) {
            ((ApplyChangesView) this.view).displaySyncFailure();
        }
    }

    @Subscribe
    public void onKeyReset(ResetKeyEvent resetKeyEvent) {
        if (((Lock) this.model).getLockId().equals(resetKeyEvent.getResetKeysWrapper().getLock().getLockId())) {
            ((ApplyChangesView) this.view).displayActionSummary(this.mLockConfigAction, (Lock) this.model);
        }
    }

    @Subscribe
    public void onConfigSuccess(DeviceConfigSuccessEvent deviceConfigSuccessEvent) {
        if (((Lock) this.model).getLockId().equals(deviceConfigSuccessEvent.getLock().getLockId())) {
            this.model = deviceConfigSuccessEvent.getLock();
            this.mHandler.removeCallbacks(this.mSyncingRunnable);
            if (this.mLockConfigAction.equals(LockConfigAction.UPDATE_FIRMWARE)) {
                ((Lock) this.model).setIsUpdating(false);
            }
            ((ApplyChangesView) this.view).displayActionSummary(this.mLockConfigAction, (Lock) this.model);
            this.mSubscription = this.mLockService.updateDb((Lock) this.model).observeOn(this.mScheduler.main()).subscribeOn(this.mScheduler.background()).delay(1, TimeUnit.SECONDS).subscribe(new Action1() {
                public final void call(Object obj) {
                    ApplyChangesPresenter.lambda$onConfigSuccess$0(ApplyChangesPresenter.this, (Boolean) obj);
                }
            }, $$Lambda$ApplyChangesPresenter$37gt_nmlQFhNlNeoHyMvcTofhY.INSTANCE, new Action0() {
                public final void call() {
                    ApplyChangesPresenter.lambda$onConfigSuccess$2(ApplyChangesPresenter.this);
                }
            });
        }
    }

    public static /* synthetic */ void lambda$onConfigSuccess$0(ApplyChangesPresenter applyChangesPresenter, Boolean bool) {
        if (!bool.booleanValue()) {
            Log.e(LOG_TAG, ((ApplyChangesView) applyChangesPresenter.view).getContext().getString(C1075R.string.apply_change_save_error));
        }
        if (!((Lock) applyChangesPresenter.model).isPadLock()) {
            SecondaryCodesUtil.updateOriginalCodes((Lock) applyChangesPresenter.model);
        }
    }

    static /* synthetic */ void lambda$onConfigSuccess$1(Throwable th) {
        Log.e(LOG_TAG, th.getMessage() != null ? th.getMessage() : th.getClass().getSimpleName());
    }

    public static /* synthetic */ void lambda$onConfigSuccess$2(ApplyChangesPresenter applyChangesPresenter) {
        Log.d(LOG_TAG, "Success updating lock");
        applyChangesPresenter.sendTracking();
    }

    @Subscribe
    public void onFirmwareCommandSuccess(FirmwareCommandSuccessEvent firmwareCommandSuccessEvent) {
        if (((Lock) this.model).getLockId().equals(firmwareCommandSuccessEvent.getLock().getLockId())) {
            this.mEventBus.post(new ForceStopScanEvent((Lock) this.model));
            Log.v(LOG_TAG, "Firmware command success");
            this.model = firmwareCommandSuccessEvent.getLock();
            updateFirmwareUpdateProgress((Lock) this.model);
            if (firmwareCommandSuccessEvent.isLastCommand()) {
                Log.i(LOG_TAG, "FIRMWARE SUCCESS, IS LAST COMMAND");
                ((ApplyChangesView) this.view).displayUpdateRestoreConfig();
                this.mBackgroundScanService.onForceScanEvent(new ForceScanEvent((Lock) this.model));
            }
        }
    }

    public void updateFirmwareUpdateProgress(Lock lock) {
        lock.setNumberOfCommands(lock.getNumberOfCommands() - 1);
        ((ApplyChangesView) this.view).setTotalProgress(lock.getTotalNumberOfCommands());
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("NumberOfCommands: ");
        sb.append(lock.getNumberOfCommands());
        sb.append(", TotalNumberOfCommands: ");
        sb.append(lock.getTotalNumberOfCommands());
        Log.d(str, sb.toString());
        if (lock.getNumberOfCommands() <= 0) {
            ((ApplyChangesView) this.view).updateProgress(0);
        } else {
            ((ApplyChangesView) this.view).updateProgress(lock.getNumberOfCommands());
        }
    }

    @Subscribe
    public void onFirmwareUpdateSuccess(FirmwareUpdateSuccessEvent firmwareUpdateSuccessEvent) {
        Log.d(LOG_TAG, "Firmware update and configuration restoration succeeded.");
        this.mFinishedUpdating = true;
        this.mEventBus.unregister(this);
        ((Lock) this.model).setIsUpdating(false);
        completeFirmwareUpdate();
    }

    public void completeFirmwareUpdate() {
        this.mConfirmTaskQueue.add(new ConfirmTask((Lock) this.model, this.mFirmwareUpdate));
        ((ApplyChangesView) this.view).updateProgress(0);
        MasterLockApp.get().getAnalytics().logEvent(Analytics.CATEGORY_MASTERLOCK_EVENT, Analytics.ACTION_UPDATED_FIRMWARE, Analytics.ACTION_UPDATED_FIRMWARE, 1);
        ((ApplyChangesView) this.view).displayActionSummary(LockConfigAction.UPDATE_FIRMWARE, (Lock) this.model);
        if (FileUtil.getInstance().existsFirmwareUpdateFile(((Lock) this.model).getKmsId())) {
            FileUtil.getInstance().deleteFirmwareUpdateFile(((Lock) this.model).getKmsId());
        }
        removeLockFromUpdate((Lock) this.model);
        updateStatus(LockStatus.UNKNOWN);
        resetFirmwareUpdateConfig((Lock) this.model);
    }

    public void updateStatus(LockStatus lockStatus) {
        ((Lock) this.model).setLockStatus(lockStatus);
        this.mLockService.updateStatus((Lock) this.model).subscribeOn(Schedulers.m220io()).subscribe();
    }

    public Lock resetFirmwareUpdateConfig(Lock lock) {
        Log.v("FIRMWARE", "DELETING COMMANDS");
        lock.setFirmwareUpdateCommand("");
        lock.setFirmwareUpdateCommands(new ArrayList());
        lock.setNumberOfCommands(0);
        lock.setFirmwareFirstCommand("");
        lock.setFirmwareLastCommand("");
        return lock;
    }

    private void sendTracking() {
        switch (this.mLockConfigAction) {
            case PRIMARY_CODE:
                MasterLockApp.get().getAnalytics().logEvent(Analytics.CATEGORY_MASTERLOCK_EVENT, Analytics.ACTION_CHANGE_PRIMARY_CODE, Analytics.ACTION_CHANGE_PRIMARY_CODE, 1);
                return;
            case RELOCK_TIME:
                MasterLockApp.get().getAnalytics().logEvent(Analytics.CATEGORY_MASTERLOCK_EVENT, Analytics.ACTION_CHANGE_RELOCK_TIME, Analytics.ACTION_CHANGE_RELOCK_TIME, 1);
                return;
            case LOCK_MODE:
                MasterLockApp.get().getAnalytics().logEvent(Analytics.CATEGORY_MASTERLOCK_EVENT, Analytics.ACTION_CHANGE_UNLOCK_MODE, Analytics.ACTION_CHANGE_UNLOCK_MODE, 1);
                return;
            default:
                return;
        }
    }

    private void bindBackgroundScanService() {
        ((ApplyChangesView) this.view).getContext().bindService(new Intent(((ApplyChangesView) this.view).getContext(), BackgroundScanService.class), this.mBackgroundServiceConnection, 1);
    }

    /* access modifiers changed from: private */
    public void performAction() {
        StringBuilder sb = new StringBuilder();
        sb.append("ACTION DETECTED ");
        sb.append(this.mLockConfigAction);
        Log.d("FIRMWARE", sb.toString());
        switch (this.mLockConfigAction) {
            case PRIMARY_CODE:
                this.mBackgroundScanService.updatePrimaryCode((Lock) this.model);
                return;
            case RELOCK_TIME:
                this.mBackgroundScanService.updateRelockTime((Lock) this.model);
                return;
            case LOCK_MODE:
                this.mBackgroundScanService.updateLockMode((Lock) this.model);
                return;
            case RESET_KEYS:
                resetKeys();
                return;
            case UPDATE_FIRMWARE:
                this.mEventBus.post(new FirmwareUpdateBeginEvent(true, (Lock) this.model));
                return;
            case SECONDARY_CODES:
                this.mBackgroundScanService.updateSecondaryCodes((Lock) this.model);
                return;
            default:
                return;
        }
    }

    private void resetKeys() {
        this.mSubscription = this.mKmsDeviceService.resetKeys((Lock) this.model).observeOn(this.mScheduler.main()).subscribeOn(this.mScheduler.background()).subscribe((Action1<? super T>) new Action1() {
            public final void call(Object obj) {
                ApplyChangesPresenter.this.mBackgroundScanService.resetKeys(new ResetKeysWrapper((Lock) ApplyChangesPresenter.this.model, (CommandsResponse) obj));
            }
        }, (Action1<Throwable>) new Action1() {
            public final void call(Object obj) {
                ApplyChangesPresenter.lambda$resetKeys$4(ApplyChangesPresenter.this, (Throwable) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$resetKeys$4(ApplyChangesPresenter applyChangesPresenter, Throwable th) {
        ((ApplyChangesView) applyChangesPresenter.view).displaySyncFailure();
        ((ApplyChangesView) applyChangesPresenter.view).displayError(th.getMessage() != null ? th.getMessage() : th.getClass().getSimpleName());
    }

    public void removeLockFromUpdate(Lock lock) {
        LockUpdateUtil.getInstance().removeUpdateAvailableForLock(lock.getLockId());
    }

    public void stopFirmwareUpdateProcess() {
        this.mEventBus.post(new FirmwareUpdateStopEvent(true));
    }

    public void goToInstallView() {
        this.mFirmwareUpdate = FileUtil.getInstance().readFirmwareUpdateFile(((Lock) this.model).getKmsId());
        if (this.mFirmwareUpdate != null) {
            ((ApplyChangesView) this.view).setTotalProgress(0);
            ((ApplyChangesView) this.view).updateProgress(0);
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Firmware commands: ");
            sb.append(this.mFirmwareUpdate.commands);
            Log.d(str, sb.toString());
            sendFirmwareUpdateCommand(this.mFirmwareUpdate.commands);
            return;
        }
        Log.d(LOG_TAG, "Firmware Update commands null");
    }

    public void sendFirmwareUpdateCommand(List list) {
        if (list != null) {
            Log.d(LOG_TAG, "Sending firmware command");
            Map map = (Map) list.get(0);
            ((Lock) this.model).setFirmwareUpdateCommands(list);
            ((Lock) this.model).setFirmwareFirstCommand((String) ((Map) list.get(0)).get("Command"));
            ((Lock) this.model).setFirmwareLastCommand((String) ((Map) list.get(list.size() - 1)).get("Command"));
            ((Lock) this.model).setFirmwareUpdateCommand(((Lock) this.model).getFirmwareFirstCommand());
            sendCommand(map);
        }
    }

    public void sendCommand(Map<String, String> map) {
        AppFlow.get(((ApplyChangesView) this.view).getContext()).replaceTo(new ApplyChanges((Lock) this.model, C1075R.string.title_firmware_update, LockConfigAction.UPDATE_FIRMWARE));
    }

    public void onBackPressed() {
        abortPendingAction();
    }

    public void sendRequestNotificationPermissionEvent() {
        this.mEventBus.post(new LocationPermissionEvent());
    }
}
