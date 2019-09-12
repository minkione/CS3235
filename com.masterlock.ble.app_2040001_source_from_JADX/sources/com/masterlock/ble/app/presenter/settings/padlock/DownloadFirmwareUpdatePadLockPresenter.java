package com.masterlock.ble.app.presenter.settings.padlock;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.bus.FirmwareUpdateBeginEvent;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.LockScreens.ApplyChanges;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.FileUtil;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.modal.LockerModeDialogPadLock;
import com.masterlock.ble.app.view.settings.padlock.DownloadFirmwareUpdatePadLockView;
import com.masterlock.core.FirmwareUpdate;
import com.masterlock.core.Lock;
import com.masterlock.core.LockConfigAction;
import com.square.flow.appflow.AppFlow;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class DownloadFirmwareUpdatePadLockPresenter extends AuthenticatedPresenter<Lock, DownloadFirmwareUpdatePadLockView> {
    private static final String LOG_TAG = "Firmware Download";
    private Dialog mDialog;
    /* access modifiers changed from: private */
    public FirmwareUpdate mFirmwareUpdate;
    @Inject
    LockService mLockService;
    @Inject
    IScheduler mScheduler;
    private Subscription mUpdateSubscription = Subscriptions.empty();

    public DownloadFirmwareUpdatePadLockPresenter(Lock lock, DownloadFirmwareUpdatePadLockView downloadFirmwareUpdatePadLockView) {
        super(lock, downloadFirmwareUpdatePadLockView);
    }

    public void start() {
        super.start();
        downloadFirmwareUpdate();
    }

    public void finish() {
        super.finish();
        this.mUpdateSubscription.unsubscribe();
    }

    public void downloadFirmwareUpdate() {
        this.mEventBus.post(new ToggleProgressBarEvent(true));
        this.mUpdateSubscription.unsubscribe();
        this.mUpdateSubscription = this.mLockService.getFirmwareUpdate((Lock) this.model).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<FirmwareUpdate>() {
            public void onStart() {
            }

            public void onCompleted() {
                DownloadFirmwareUpdatePadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ((DownloadFirmwareUpdatePadLockView) DownloadFirmwareUpdatePadLockPresenter.this.view).updateViewSuccess();
                ((DownloadFirmwareUpdatePadLockView) DownloadFirmwareUpdatePadLockPresenter.this.view).stopProgress();
                FileUtil.getInstance().saveFirmwareUpdateToFile(DownloadFirmwareUpdatePadLockPresenter.this.mFirmwareUpdate, ((Lock) DownloadFirmwareUpdatePadLockPresenter.this.model).getKmsId());
            }

            public void onError(Throwable th) {
                DownloadFirmwareUpdatePadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ((DownloadFirmwareUpdatePadLockView) DownloadFirmwareUpdatePadLockPresenter.this.view).updateViewError();
                ((DownloadFirmwareUpdatePadLockView) DownloadFirmwareUpdatePadLockPresenter.this.view).stopProgress();
            }

            public void onNext(FirmwareUpdate firmwareUpdate) {
                Log.v("Firmware", firmwareUpdate.toString());
                DownloadFirmwareUpdatePadLockPresenter.this.mFirmwareUpdate = firmwareUpdate;
                if (firmwareUpdate.commands.isEmpty() || firmwareUpdate.commands.size() < 2 || firmwareUpdate.kMSReferenceHandler == null) {
                    throw new IndexOutOfBoundsException("Firmware commands are empty");
                }
            }
        });
    }

    public void goToInstallView() {
        if (((Lock) this.model).isLockerMode()) {
            displayLockermodeDialog();
            return;
        }
        this.mEventBus.post(new FirmwareUpdateBeginEvent(true, (Lock) this.model));
        sendFirmwareUpdateCommand(this.mFirmwareUpdate.commands);
    }

    public void displayLockermodeDialog() {
        LockerModeDialogPadLock lockerModeDialogPadLock = new LockerModeDialogPadLock(((DownloadFirmwareUpdatePadLockView) this.view).getContext(), null);
        lockerModeDialogPadLock.fillCode((Lock) this.model);
        lockerModeDialogPadLock.displayStateFirmwareUpdate((Lock) this.model);
        lockerModeDialogPadLock.setPositiveButtonClickListener(new OnClickListener() {
            public final void onClick(View view) {
                DownloadFirmwareUpdatePadLockPresenter.this.mDialog.dismiss();
            }
        });
        this.mDialog = new Dialog(((DownloadFirmwareUpdatePadLockView) this.view).getContext());
        this.mDialog.requestWindowFeature(1);
        this.mDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mDialog.setContentView(lockerModeDialogPadLock);
        this.mDialog.show();
    }

    public void sendFirmwareUpdateCommand(List list) {
        if (list != null) {
            Map map = (Map) list.get(0);
            ((Lock) this.model).setFirmwareUpdateCommands(list);
            ((Lock) this.model).setFirmwareFirstCommand((String) ((Map) list.get(0)).get("Command"));
            ((Lock) this.model).setFirmwareLastCommand((String) ((Map) list.get(list.size() - 1)).get("Command"));
            ((Lock) this.model).setFirmwareUpdateCommand(((Lock) this.model).getFirmwareFirstCommand());
            ((Lock) this.model).setIsUpdating(true);
            sendCommand(map);
        }
    }

    public void sendCommand(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("COMMANDS BEFORE SENDING TO APPLY CHANGES");
        sb.append(((Lock) this.model).getFirmwareUpdateCommands());
        Log.v("FIRMWARE", sb.toString());
        AppFlow.get(((DownloadFirmwareUpdatePadLockView) this.view).getContext()).goTo(new ApplyChanges((Lock) this.model, C1075R.string.title_firmware_update, LockConfigAction.UPDATE_FIRMWARE));
    }
}
