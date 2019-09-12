package com.masterlock.ble.app.presenter.guest;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.LockScreens.ApplyChanges;
import com.masterlock.ble.app.screens.LockScreens.SecondaryCodeUpdateKeySafe;
import com.masterlock.ble.app.view.lock.keysafe.ChangeSecondaryCodesKeySafeView;
import com.masterlock.ble.app.view.modal.BaseModal;
import com.masterlock.core.Lock;
import com.masterlock.core.LockConfigAction;
import com.masterlock.core.SecondaryCodeIndex;
import com.masterlock.core.SecondaryCodesUtil;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import javax.inject.Inject;

public class ChangeSecondaryCodesKeySafePresenter extends AuthenticatedPresenter<Lock, ChangeSecondaryCodesKeySafeView> {
    public static final String TAG = "ChangeSecondaryCodesKeySafePresenter";
    private Dialog mDialogShowUpdateSecondaryCodesWarning;
    @Inject
    Bus mEventBus;

    public ChangeSecondaryCodesKeySafePresenter(Lock lock, ChangeSecondaryCodesKeySafeView changeSecondaryCodesKeySafeView) {
        super(lock, changeSecondaryCodesKeySafeView);
    }

    public void start() {
        super.start();
        this.mEventBus.register(this);
    }

    public void finish() {
        super.finish();
        this.mEventBus.unregister(this);
        Dialog dialog = this.mDialogShowUpdateSecondaryCodesWarning;
        if (dialog != null && dialog.isShowing()) {
            this.mDialogShowUpdateSecondaryCodesWarning.dismiss();
        }
    }

    public void goToUpdateSecondaryCode(SecondaryCodeIndex secondaryCodeIndex) {
        AppFlow.get(((ChangeSecondaryCodesKeySafeView) this.view).getContext()).goTo(new SecondaryCodeUpdateKeySafe((Lock) this.model, false, secondaryCodeIndex));
    }

    public void goToApplyChanges() {
        AppFlow.get(((ChangeSecondaryCodesKeySafeView) this.view).getContext()).goTo(new ApplyChanges((Lock) this.model, C1075R.string.title_secondary_codes, LockConfigAction.SECONDARY_CODES));
    }

    public void showDeleteSecondaryCodeModal(OnClickListener onClickListener) {
        BaseModal baseModal = new BaseModal(((ChangeSecondaryCodesKeySafeView) this.view).getContext());
        baseModal.setTitle(((ChangeSecondaryCodesKeySafeView) this.view).getResources().getString(C1075R.string.delete_secondary_code_modal_title));
        Dialog dialog = new Dialog(((ChangeSecondaryCodesKeySafeView) this.view).getContext());
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.requestWindowFeature(1);
        dialog.setContentView(baseModal);
        C1135x1feb0723 r2 = new OnClickListener(onClickListener, dialog) {
            private final /* synthetic */ OnClickListener f$1;
            private final /* synthetic */ Dialog f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void onClick(View view) {
                ChangeSecondaryCodesKeySafePresenter.lambda$showDeleteSecondaryCodeModal$0(ChangeSecondaryCodesKeySafePresenter.this, this.f$1, this.f$2, view);
            }
        };
        baseModal.setBody(((ChangeSecondaryCodesKeySafeView) this.view).getResources().getString(C1075R.string.delete_secondary_code_confirmation));
        baseModal.getPositiveButton().setText(((ChangeSecondaryCodesKeySafeView) this.view).getContext().getResources().getString(C1075R.string.delete));
        baseModal.setPositiveButtonClickListener(r2);
        baseModal.setNegativeButtonClickListener(r2);
        dialog.show();
    }

    public static /* synthetic */ void lambda$showDeleteSecondaryCodeModal$0(ChangeSecondaryCodesKeySafePresenter changeSecondaryCodesKeySafePresenter, OnClickListener onClickListener, Dialog dialog, View view) {
        if (view.getId() == C1075R.C1077id.positive_button) {
            onClickListener.onClick((View) changeSecondaryCodesKeySafePresenter.view);
        }
        dialog.dismiss();
    }

    public boolean showUpdateSecondaryCodesWarningModal() {
        if (!SecondaryCodesUtil.hasPendingOperations((Lock) this.model)) {
            return false;
        }
        BaseModal baseModal = new BaseModal(((ChangeSecondaryCodesKeySafeView) this.view).getContext());
        this.mDialogShowUpdateSecondaryCodesWarning = new Dialog(((ChangeSecondaryCodesKeySafeView) this.view).getContext());
        this.mDialogShowUpdateSecondaryCodesWarning.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mDialogShowUpdateSecondaryCodesWarning.requestWindowFeature(1);
        this.mDialogShowUpdateSecondaryCodesWarning.setContentView(baseModal);
        this.mDialogShowUpdateSecondaryCodesWarning.setCancelable(false);
        C1136xe392bdf8 r1 = new OnClickListener() {
            public final void onClick(View view) {
                ChangeSecondaryCodesKeySafePresenter.lambda$showUpdateSecondaryCodesWarningModal$1(ChangeSecondaryCodesKeySafePresenter.this, view);
            }
        };
        C1137x326d57df r2 = new OnClickListener() {
            public final void onClick(View view) {
                ChangeSecondaryCodesKeySafePresenter.this.mDialogShowUpdateSecondaryCodesWarning.dismiss();
            }
        };
        baseModal.setTitle(((ChangeSecondaryCodesKeySafeView) this.view).getContext().getString(C1075R.string.warning));
        baseModal.setBody(((ChangeSecondaryCodesKeySafeView) this.view).getContext().getString(C1075R.string.secondary_codes_update_warning));
        baseModal.getPositiveButton().setText(((ChangeSecondaryCodesKeySafeView) this.view).getContext().getString(C1075R.string.accept_button));
        baseModal.getNegativeButton().setText(((ChangeSecondaryCodesKeySafeView) this.view).getContext().getString(C1075R.string.cancel));
        baseModal.setPositiveButtonClickListener(r1);
        baseModal.setNegativeButtonClickListener(r2);
        this.mDialogShowUpdateSecondaryCodesWarning.show();
        return true;
    }

    public static /* synthetic */ void lambda$showUpdateSecondaryCodesWarningModal$1(ChangeSecondaryCodesKeySafePresenter changeSecondaryCodesKeySafePresenter, View view) {
        changeSecondaryCodesKeySafePresenter.mDialogShowUpdateSecondaryCodesWarning.dismiss();
        AppFlow.get(((ChangeSecondaryCodesKeySafeView) changeSecondaryCodesKeySafePresenter.view).getContext()).goBack();
    }
}
