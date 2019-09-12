package com.masterlock.ble.app.presenter.settings.padlock;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.bus.FirmwareUpdateBeginEvent;
import com.masterlock.ble.app.bus.ForceStopScanEvent;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.LockScreens.ApplyChanges;
import com.masterlock.ble.app.screens.SettingsScreens.AboutLockPadLock;
import com.masterlock.ble.app.screens.SettingsScreens.DownloadFirmwareUpdatePadLock;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.FileUtil;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.modal.LockerModeDialogPadLock;
import com.masterlock.ble.app.view.settings.padlock.AboutLockPadLockView;
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

public class AboutLockPadLockPresenter extends AuthenticatedPresenter<Lock, AboutLockPadLockView> {
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

    public AboutLockPadLockPresenter(Lock lock, AboutLockPadLockView aboutLockPadLockView) {
        super(lock, aboutLockPadLockView);
        this.model = ((AboutLockPadLock) AppFlow.getScreen(aboutLockPadLockView.getContext())).mLock;
        this.mEventBus.register(this);
        this.mLockService.get(((Lock) this.model).getLockId()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Lock>() {
            public void onError(Throwable th) {
            }

            public void onStart() {
                AboutLockPadLockPresenter.this.mEventBus.post(new ForceStopScanEvent((Lock) AboutLockPadLockPresenter.this.model));
            }

            public void onCompleted() {
                AboutLockPadLockPresenter aboutLockPadLockPresenter = AboutLockPadLockPresenter.this;
                aboutLockPadLockPresenter.updateUi((Lock) aboutLockPadLockPresenter.model);
            }

            public void onNext(Lock lock) {
                AboutLockPadLockPresenter.this.model = lock;
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
        ((AboutLockPadLockView) this.view).setLockName(lock.getName());
        ((AboutLockPadLockView) this.view).setDeviceId(lock.getKmsDeviceKey().getDeviceId());
        ((AboutLockPadLockView) this.view).updateAboutLock(lock);
        if (lock.getAccessType() != AccessType.GUEST) {
            checkForFirmwareUpdate();
            return;
        }
        ((AboutLockPadLockView) this.view).setmFirmwareUpdateAvailability(C1075R.string.empty_string);
        ((AboutLockPadLockView) this.view).setFirmwareTitleText(String.format(MasterLockApp.get().getString(C1075R.string.about_firmware_version), new Object[]{Integer.valueOf(lock.getFirmwareVersion())}));
    }

    public void checkForFirmwareUpdate() {
        this.mCheckForUpdateSubscription.unsubscribe();
        this.mCheckForUpdateSubscription = this.mLockService.checkForFirmwareUpdate((Lock) this.model).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Firmware>() {
            public void onStart() {
                AboutLockPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
                ((AboutLockPadLockView) AboutLockPadLockPresenter.this.view).setmFirmwareUpdateAvailability(C1075R.string.about_firmware_checking_for_updates);
            }

            public void onCompleted() {
                AboutLockPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                AboutLockPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ((AboutLockPadLockView) AboutLockPadLockPresenter.this.view).setmFirmwareUpdateAvailability(C1075R.string.no_connection_alert_body);
            }

            public void onNext(Firmware firmware) {
                if (!firmware.isFirmwareUpdateAvailable) {
                    ((AboutLockPadLockView) AboutLockPadLockPresenter.this.view).setFirmwareUptoDateText();
                } else if (FileUtil.getInstance().existsFirmwareUpdateFile(((Lock) AboutLockPadLockPresenter.this.model).getKmsId())) {
                    AboutLockPadLockPresenter.this.mFirmwareUpdate = FileUtil.getInstance().readFirmwareUpdateFile(((Lock) AboutLockPadLockPresenter.this.model).getKmsId());
                    ((AboutLockPadLockView) AboutLockPadLockPresenter.this.view).updateLockFirmwareDetails(firmware, true);
                } else {
                    ((AboutLockPadLockView) AboutLockPadLockPresenter.this.view).updateLockFirmwareDetails(firmware, false);
                }
            }
        });
    }

    public void goToDownloadFirmwareView() {
        AppFlow.get(((AboutLockPadLockView) this.view).getContext()).goTo(new DownloadFirmwareUpdatePadLock((Lock) this.model));
    }

    public void goToInstallView() {
        this.mEventBus.post(new FirmwareUpdateBeginEvent(true, (Lock) this.model));
        AppFlow.get(((AboutLockPadLockView) this.view).getContext()).goTo(new ApplyChanges((Lock) this.model, C1075R.string.title_firmware_update, LockConfigAction.UPDATE_FIRMWARE));
    }

    public void displayLockermodeDialog() {
        LockerModeDialogPadLock lockerModeDialogPadLock = new LockerModeDialogPadLock(((AboutLockPadLockView) this.view).getContext(), null);
        lockerModeDialogPadLock.fillCode((Lock) this.model);
        lockerModeDialogPadLock.displayStateFirmwareUpdate((Lock) this.model);
        lockerModeDialogPadLock.setPositiveButtonClickListener(new OnClickListener() {
            public final void onClick(View view) {
                AboutLockPadLockPresenter.this.mDialog.dismiss();
            }
        });
        this.mDialog = new Dialog(((AboutLockPadLockView) this.view).getContext());
        this.mDialog.requestWindowFeature(1);
        this.mDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mDialog.setContentView(lockerModeDialogPadLock);
        this.mDialog.show();
    }
}
