package com.masterlock.ble.app.presenter.settings.padlock;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.LockScreens.ApplyChanges;
import com.masterlock.ble.app.screens.SettingsScreens.UnlockModeListPadLock;
import com.masterlock.ble.app.view.modal.SimpleDialog;
import com.masterlock.ble.app.view.settings.padlock.UnlockModePadLockListView;
import com.masterlock.core.Lock;
import com.masterlock.core.LockConfigAction;
import com.masterlock.core.LockMode;
import com.square.flow.appflow.AppFlow;

public class UnlockModePadLockPresenter extends AuthenticatedPresenter<Lock, UnlockModePadLockListView> {
    public UnlockModePadLockPresenter(UnlockModePadLockListView unlockModePadLockListView) {
        super(((UnlockModeListPadLock) AppFlow.getScreen(unlockModePadLockListView.getContext())).mLock, unlockModePadLockListView);
    }

    public void start() {
        super.start();
        this.model = ((UnlockModeListPadLock) AppFlow.getScreen(((UnlockModePadLockListView) this.view).getContext())).mLock;
        ((UnlockModePadLockListView) this.view).setLockName(((Lock) this.model).getName());
        ((UnlockModePadLockListView) this.view).setDeviceId(((Lock) this.model).getKmsDeviceKey().getDeviceId());
    }

    public Lock getLock() {
        return (Lock) this.model;
    }

    public void setLockMode(LockMode lockMode) {
        getLock().setLockMode(lockMode);
    }

    public void saveNewUnlockMode() {
        if (((Lock) this.model).isLockerMode()) {
            showLockerModeDialog();
        } else {
            AppFlow.get(((UnlockModePadLockListView) this.view).getContext()).goTo(new ApplyChanges((Lock) this.model, C1075R.string.title_unlock_mode, LockConfigAction.LOCK_MODE));
        }
    }

    private void showLockerModeDialog() {
        SimpleDialog simpleDialog = new SimpleDialog(((UnlockModePadLockListView) this.view).getContext(), null);
        Dialog dialog = new Dialog(((UnlockModePadLockListView) this.view).getContext());
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
