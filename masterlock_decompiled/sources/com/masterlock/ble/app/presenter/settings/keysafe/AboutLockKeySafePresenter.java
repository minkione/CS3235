package com.masterlock.ble.app.presenter.settings.keysafe;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.bus.FirmwareUpdateBeginEvent;
import com.masterlock.ble.app.bus.ForceStopScanEvent;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.LockScreens.ApplyChanges;
import com.masterlock.ble.app.screens.SettingsScreens.AboutLockKeySafe;
import com.masterlock.ble.app.screens.SettingsScreens.DownloadFirmwareUpdateKeySafe;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.FileUtil;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.modal.LockerModeDialogPadLock;
import com.masterlock.ble.app.view.settings.keysafe.AboutLockKeySafeView;
import com.masterlock.core.AccessType;
import com.masterlock.core.Firmware;
import com.masterlock.core.FirmwareUpdate;
import com.masterlock.core.Lock;
import com.masterlock.core.LockConfigAction;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class AboutLockKeySafePresenter extends AuthenticatedPresenter<Lock, AboutLockKeySafeView> {
    private static final String LOG_TAG = "Firmware Download";
    private Subscription mCheckForUpdateSubscription = Subscriptions.empty();
    private Dialog mDialog;
    @Inject
    Bus mEventBus;
    /* access modifiers changed from: private */
    public FirmwareUpdate mFirmwareUpdate;
    @Inject
    LockService mLockService;
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();

    public AboutLockKeySafePresenter(Lock lock, AboutLockKeySafeView aboutLockKeySafeView) {
        super(lock, aboutLockKeySafeView);
        this.model = ((AboutLockKeySafe) AppFlow.getScreen(aboutLockKeySafeView.getContext())).mLock;
        this.mEventBus.register(this);
        this.mLockService.get(((Lock) this.model).getLockId()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Lock>() {
            public void onError(Throwable th) {
            }

            public void onStart() {
                AboutLockKeySafePresenter.this.mEventBus.post(new ForceStopScanEvent((Lock) AboutLockKeySafePresenter.this.model));
            }

            public void onCompleted() {
                AboutLockKeySafePresenter aboutLockKeySafePresenter = AboutLockKeySafePresenter.this;
                aboutLockKeySafePresenter.updateUi((Lock) aboutLockKeySafePresenter.model);
            }

            public void onNext(Lock lock) {
                AboutLockKeySafePresenter.this.model = lock;
            }
        });
    }

    public void start() {
        super.start();
    }

    public void finish() {
        super.finish();
        this.mSubscription.unsubscribe();
        this.mCheckForUpdateSubscription.unsubscribe();
        this.mEventBus.unregister(this);
    }

    /* access modifiers changed from: private */
    public void updateUi(Lock lock) {
        ((AboutLockKeySafeView) this.view).setLockName(lock.getName());
        ((AboutLockKeySafeView) this.view).setDeviceId(lock.getKmsDeviceKey().getDeviceId());
        ((AboutLockKeySafeView) this.view).updateAboutLock(lock);
        if (lock.getAccessType() != AccessType.GUEST) {
            checkForFirmwareUpdate();
            return;
        }
        ((AboutLockKeySafeView) this.view).setmFirmwareUpdateAvailability(C1075R.string.empty_string);
        ((AboutLockKeySafeView) this.view).setFirmwareTitleText(String.format(((AboutLockKeySafeView) this.view).getContext().getString(C1075R.string.about_firmware_version), new Object[]{Integer.valueOf(lock.getFirmwareVersion())}));
    }

    public void checkForFirmwareUpdate() {
        this.mCheckForUpdateSubscription.unsubscribe();
        this.mCheckForUpdateSubscription = this.mLockService.checkForFirmwareUpdate((Lock) this.model).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Firmware>() {
            public void onStart() {
                AboutLockKeySafePresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
                ((AboutLockKeySafeView) AboutLockKeySafePresenter.this.view).setmFirmwareUpdateAvailability(C1075R.string.about_firmware_checking_for_updates);
            }

            public void onCompleted() {
                AboutLockKeySafePresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                AboutLockKeySafePresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ((AboutLockKeySafeView) AboutLockKeySafePresenter.this.view).setmFirmwareUpdateAvailability(C1075R.string.no_connection_alert_body);
            }

            public void onNext(Firmware firmware) {
                if (!firmware.isFirmwareUpdateAvailable) {
                    ((AboutLockKeySafeView) AboutLockKeySafePresenter.this.view).setFirmwareUptoDateText();
                } else if (FileUtil.getInstance().existsFirmwareUpdateFile(((Lock) AboutLockKeySafePresenter.this.model).getKmsId())) {
                    AboutLockKeySafePresenter.this.mFirmwareUpdate = FileUtil.getInstance().readFirmwareUpdateFile(((Lock) AboutLockKeySafePresenter.this.model).getKmsId());
                    ((AboutLockKeySafeView) AboutLockKeySafePresenter.this.view).updateLockFirmwareDetails(firmware, true);
                } else {
                    ((AboutLockKeySafeView) AboutLockKeySafePresenter.this.view).updateLockFirmwareDetails(firmware, false);
                }
            }
        });
    }

    public void goToDownloadFirmwareView() {
        AppFlow.get(((AboutLockKeySafeView) this.view).getContext()).goTo(new DownloadFirmwareUpdateKeySafe((Lock) this.model));
    }

    public void goToInstallView() {
        this.mEventBus.post(new FirmwareUpdateBeginEvent(true, (Lock) this.model));
        AppFlow.get(((AboutLockKeySafeView) this.view).getContext()).goTo(new ApplyChanges((Lock) this.model, C1075R.string.title_firmware_update, LockConfigAction.UPDATE_FIRMWARE));
    }

    public void displayLockermodeDialog() {
        LockerModeDialogPadLock lockerModeDialogPadLock = new LockerModeDialogPadLock(((AboutLockKeySafeView) this.view).getContext(), null);
        lockerModeDialogPadLock.fillCode((Lock) this.model);
        lockerModeDialogPadLock.displayStateFirmwareUpdate((Lock) this.model);
        lockerModeDialogPadLock.setPositiveButtonClickListener(new OnClickListener() {
            public final void onClick(View view) {
                AboutLockKeySafePresenter.this.mDialog.dismiss();
            }
        });
        this.mDialog = new Dialog(((AboutLockKeySafeView) this.view).getContext());
        this.mDialog.requestWindowFeature(1);
        this.mDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mDialog.setContentView(lockerModeDialogPadLock);
        this.mDialog.show();
    }
}
