package com.masterlock.ble.app.presenter.settings.keysafe;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.LockScreens.ApplyChanges;
import com.masterlock.ble.app.screens.SettingsScreens.ResetKeysKeySafe;
import com.masterlock.ble.app.view.modal.ResetKeysDialog;
import com.masterlock.ble.app.view.modal.SimpleDialog;
import com.masterlock.ble.app.view.settings.keysafe.ResetKeysKeySafeView;
import com.masterlock.core.Lock;
import com.masterlock.core.LockConfigAction;
import com.square.flow.appflow.AppFlow;

public class ResetKeysKeySafePresenter extends AuthenticatedPresenter<Lock, ResetKeysKeySafeView> {
    public ResetKeysKeySafePresenter(ResetKeysKeySafeView resetKeysKeySafeView) {
        super(((ResetKeysKeySafe) AppFlow.getScreen(resetKeysKeySafeView.getContext())).mLock, resetKeysKeySafeView);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        super.start();
        this.model = ((ResetKeysKeySafe) AppFlow.getScreen(((ResetKeysKeySafeView) this.view).getContext())).mLock;
        ((ResetKeysKeySafeView) this.view).setLockName(((Lock) this.model).getName());
        ((ResetKeysKeySafeView) this.view).setDeviceId(((Lock) this.model).getKmsDeviceKey().getDeviceId());
    }

    public void resetKeys() {
        if (((Lock) this.model).isLockerMode()) {
            showLockerModeDialog();
        } else {
            showConfirmationDialog();
        }
    }

    public void showConfirmationDialog() {
        ResetKeysDialog resetKeysDialog = new ResetKeysDialog(((ResetKeysKeySafeView) this.view).getContext(), null);
        final Dialog dialog = new Dialog(((ResetKeysKeySafeView) this.view).getContext());
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.setContentView(resetKeysDialog);
        resetKeysDialog.setPositiveButton((int) C1075R.string.reset_keys_label);
        resetKeysDialog.setNegativeButton((int) C1075R.string.cancel);
        resetKeysDialog.setPositiveButtonClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                ResetKeysKeySafePresenter.this.performResetKeys();
            }
        });
        resetKeysDialog.setNegativeButtonClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /* access modifiers changed from: private */
    public void performResetKeys() {
        AppFlow.get(((ResetKeysKeySafeView) this.view).getContext()).goTo(new ApplyChanges((Lock) this.model, C1075R.string.reset_keys_label, LockConfigAction.RESET_KEYS));
    }

    private void showLockerModeDialog() {
        SimpleDialog simpleDialog = new SimpleDialog(((ResetKeysKeySafeView) this.view).getContext(), null);
        Dialog dialog = new Dialog(((ResetKeysKeySafeView) this.view).getContext());
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
