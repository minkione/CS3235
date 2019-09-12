package com.masterlock.ble.app.presenter.settings.keysafe;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.masterlock.api.entity.MasterBackUpDialSpeedResponse;
import com.masterlock.api.entity.MasterBackupResponse;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.SettingsScreens.BackupMasterCombinationKeySafe;
import com.masterlock.ble.app.service.KMSDeviceService;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.settings.keysafe.BackupMasterCodeKeySafeView;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import java.util.ArrayList;
import java.util.Iterator;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class BackupMasterCodeKeySafePresenter extends AuthenticatedPresenter<Lock, BackupMasterCodeKeySafeView> {
    @Inject
    Bus mEventBus;
    @Inject
    KMSDeviceService mKMSDeviceService;
    private Lock mLock;
    @Inject
    LockService mLockService;
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();

    public BackupMasterCodeKeySafePresenter(BackupMasterCodeKeySafeView backupMasterCodeKeySafeView) {
        super(backupMasterCodeKeySafeView);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        String str;
        super.start();
        this.mLock = ((BackupMasterCombinationKeySafe) AppFlow.getScreen(((BackupMasterCodeKeySafeView) this.view).getContext())).mLock;
        ((BackupMasterCodeKeySafeView) this.view).setLockName(this.mLock.getName());
        BackupMasterCodeKeySafeView backupMasterCodeKeySafeView = (BackupMasterCodeKeySafeView) this.view;
        if (this.mLock.isDialSpeedLock() || this.mLock.isBiometricPadLock()) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.mLock.getModelName());
            sb.append(" ");
            sb.append(this.mLock.getModelNumber());
            str = sb.toString();
        } else {
            str = this.mLock.getKmsDeviceKey().getDeviceId();
        }
        backupMasterCodeKeySafeView.setDeviceId(str);
        if (this.mLock.isDialSpeedLock()) {
            getMasterCodeForDialSpeed();
        } else {
            getMasterCode();
        }
    }

    public void getMasterCode() {
        this.mSubscription.unsubscribe();
        this.mSubscription = this.mKMSDeviceService.getMasterCode(this.mLock).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<MasterBackupResponse>() {
            public void onStart() {
                BackupMasterCodeKeySafePresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                BackupMasterCodeKeySafePresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                BackupMasterCodeKeySafePresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ((BackupMasterCodeKeySafeView) BackupMasterCodeKeySafePresenter.this.view).displayError(th);
            }

            public void onNext(MasterBackupResponse masterBackupResponse) {
                ((BackupMasterCodeKeySafeView) BackupMasterCodeKeySafePresenter.this.view).displayBackupMasterCode(masterBackupResponse.masterBackupCode);
            }
        });
    }

    public void getMasterCodeForDialSpeed() {
        this.mSubscription.unsubscribe();
        this.mSubscription = this.mLockService.getMasterCodeForDialSpeed(this.mLock).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<MasterBackUpDialSpeedResponse>() {
            public void onStart() {
                BackupMasterCodeKeySafePresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                BackupMasterCodeKeySafePresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                BackupMasterCodeKeySafePresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ((BackupMasterCodeKeySafeView) BackupMasterCodeKeySafePresenter.this.view).displayError(th);
            }

            public void onNext(MasterBackUpDialSpeedResponse masterBackUpDialSpeedResponse) {
                ((BackupMasterCodeKeySafeView) BackupMasterCodeKeySafePresenter.this.view).displayBackupMasterCode(masterBackUpDialSpeedResponse.archiveResponse.masterCode);
            }
        });
    }

    public void showMasterCode(String str) {
        LinearLayout linearLayout = (LinearLayout) ButterKnife.findById((View) this.view, (int) C1075R.C1077id.backup_code_container);
        LayoutParams layoutParams = new LayoutParams(-2, -2, 1.0f);
        layoutParams.gravity = 80;
        ArrayList arrayList = new ArrayList();
        for (char valueOf : str.toCharArray()) {
            arrayList.add(String.valueOf(valueOf));
        }
        arrayList.add(0, "");
        arrayList.add("");
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            String str2 = (String) it.next();
            TextView textView = new TextView(((BackupMasterCodeKeySafeView) this.view).getContext());
            textView.setText(str2);
            textView.setLayoutParams(layoutParams);
            textView.setTextSize(24.0f);
            textView.setTextColor(((BackupMasterCodeKeySafeView) this.view).getResources().getColor(C1075R.color.medium_grey));
            linearLayout.addView(textView);
        }
    }

    public void finish() {
        this.mSubscription.unsubscribe();
        super.finish();
    }
}
