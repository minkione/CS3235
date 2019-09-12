package com.masterlock.ble.app.presenter.lock.padlock;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.LockScreens.ApplyChanges;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.lock.padlock.PrimaryCodeUpdatePadLockView;
import com.masterlock.ble.app.view.modal.SimpleDialog;
import com.masterlock.core.Lock;
import com.masterlock.core.LockCodeDirection;
import com.masterlock.core.LockConfigAction;
import com.square.flow.appflow.AppFlow;
import java.util.List;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class PrimaryCodeUpdatePadLockPresenter extends AuthenticatedPresenter<Lock, PrimaryCodeUpdatePadLockView> {
    public static final int REQUIRED_CODE_LENGTH = 7;
    @Inject
    LockService mLockService;
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();

    public PrimaryCodeUpdatePadLockPresenter(Lock lock, PrimaryCodeUpdatePadLockView primaryCodeUpdatePadLockView) {
        super(lock, primaryCodeUpdatePadLockView);
    }

    public void start() {
        super.start();
    }

    public void finish() {
        super.finish();
    }

    public void savePrimaryCode(List<LockCodeDirection> list) {
        if (((Lock) this.model).isLockerMode()) {
            showLockerModeDialog();
            return;
        }
        if (list.size() == 7) {
            StringBuilder sb = new StringBuilder();
            for (LockCodeDirection value : list) {
                sb.append(value.getValue());
            }
            ((Lock) this.model).setPrimaryCode(sb.toString());
            AppFlow.get(((PrimaryCodeUpdatePadLockView) this.view).getContext()).goTo(new ApplyChanges((Lock) this.model, C1075R.string.title_primary_code, LockConfigAction.PRIMARY_CODE));
        } else {
            Toast.makeText(((PrimaryCodeUpdatePadLockView) this.view).getContext(), ((PrimaryCodeUpdatePadLockView) this.view).getResources().getString(C1075R.string.update_primary_code_error), 0).show();
        }
    }

    private void updateDbLock(Lock lock) {
        this.mSubscription = this.mLockService.updateDb(lock).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
            public void onCompleted() {
                Log.d("PRIMARY CODE UPDATE", "UPDATE COMPLETE");
                AppFlow.get(((PrimaryCodeUpdatePadLockView) PrimaryCodeUpdatePadLockPresenter.this.view).getContext()).goTo(new ApplyChanges((Lock) PrimaryCodeUpdatePadLockPresenter.this.model, C1075R.string.title_primary_code, LockConfigAction.PRIMARY_CODE));
            }

            public void onError(Throwable th) {
                Log.d("PRIMARY CODE UPDATE", "UPDATE ERROR");
            }

            public void onNext(Boolean bool) {
                StringBuilder sb = new StringBuilder();
                sb.append("UPDATE WAS ");
                sb.append(bool);
                Log.d("PRIMARY CODE UPDATE", sb.toString());
            }
        });
    }

    private void showLockerModeDialog() {
        SimpleDialog simpleDialog = new SimpleDialog(((PrimaryCodeUpdatePadLockView) this.view).getContext(), null);
        Dialog dialog = new Dialog(((PrimaryCodeUpdatePadLockView) this.view).getContext());
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.setContentView(simpleDialog);
        simpleDialog.setMessage((int) C1075R.string.locker_mode_body);
        simpleDialog.setPositiveButton((int) C1075R.string.f165ok);
        simpleDialog.setPositiveButtonClickListener(new OnClickListener(dialog) {
            private final /* synthetic */ Dialog f$0;

            {
                this.f$0 = r1;
            }

            public final void onClick(View view) {
                this.f$0.dismiss();
            }
        });
        dialog.show();
    }
}
