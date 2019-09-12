package com.masterlock.ble.app.presenter.settings.padlock;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import butterknife.ButterKnife;
import com.masterlock.api.entity.MasterBackupResponse;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.SettingsScreens.BackupMasterCombinationPadLock;
import com.masterlock.ble.app.service.KMSDeviceService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.settings.padlock.BackupMasterCodePadLockView;
import com.masterlock.core.Lock;
import com.masterlock.core.LockCodeDirection;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import java.util.List;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class BackupMasterCodePadLockPresenter extends AuthenticatedPresenter<Lock, BackupMasterCodePadLockView> {
    @Inject
    Bus mEventBus;
    @Inject
    KMSDeviceService mKMSDeviceService;
    private Lock mLock;
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();

    public BackupMasterCodePadLockPresenter(BackupMasterCodePadLockView backupMasterCodePadLockView) {
        super(backupMasterCodePadLockView);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        super.start();
        this.mLock = ((BackupMasterCombinationPadLock) AppFlow.getScreen(((BackupMasterCodePadLockView) this.view).getContext())).mLock;
        ((BackupMasterCodePadLockView) this.view).setLockName(this.mLock.getName());
        ((BackupMasterCodePadLockView) this.view).setDeviceId(this.mLock.getKmsDeviceKey().getDeviceId());
        getMasterCode();
    }

    public void getMasterCode() {
        this.mSubscription.unsubscribe();
        this.mSubscription = this.mKMSDeviceService.getMasterCode(this.mLock).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<MasterBackupResponse>() {
            public void onStart() {
                BackupMasterCodePadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                BackupMasterCodePadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                BackupMasterCodePadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ((BackupMasterCodePadLockView) BackupMasterCodePadLockPresenter.this.view).displayError(th);
            }

            public void onNext(MasterBackupResponse masterBackupResponse) {
                BackupMasterCodePadLockPresenter.this.showMasterCode(masterBackupResponse.masterBackupCode);
            }
        });
    }

    public void showMasterCode(String str) {
        LinearLayout linearLayout = (LinearLayout) ButterKnife.findById((View) this.view, (int) C1075R.C1077id.backup_code_container);
        LayoutParams layoutParams = new LayoutParams(-2, -2, 1.0f);
        layoutParams.gravity = 80;
        List generateLockDirectionListFromStringCode = LockCodeDirection.generateLockDirectionListFromStringCode(str);
        int size = generateLockDirectionListFromStringCode.size();
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(((BackupMasterCodePadLockView) this.view).getContext());
            imageView.setImageResource(getDrawableForDirection((LockCodeDirection) generateLockDirectionListFromStringCode.get(i)));
            imageView.setLayoutParams(layoutParams);
            linearLayout.addView(imageView);
        }
    }

    private int getDrawableForDirection(LockCodeDirection lockCodeDirection) {
        switch (lockCodeDirection) {
            case LEFT:
                return C1075R.C1076drawable.ic_arrow_left;
            case UP:
                return C1075R.C1076drawable.ic_arrow_up;
            case RIGHT:
                return C1075R.C1076drawable.ic_arrow_right;
            default:
                return C1075R.C1076drawable.ic_arrow_down;
        }
    }

    public void finish() {
        this.mSubscription.unsubscribe();
        super.finish();
    }
}
