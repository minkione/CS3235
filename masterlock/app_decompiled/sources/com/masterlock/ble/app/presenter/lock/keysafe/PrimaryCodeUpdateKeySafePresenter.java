package com.masterlock.ble.app.presenter.lock.keysafe;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.LockScreens.ApplyChanges;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.CodeTypesUtil;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.lock.keysafe.PrimaryCodeUpdateKeySafeView;
import com.masterlock.ble.app.view.modal.PrimarySecondaryCodeErrorDialog;
import com.masterlock.ble.app.view.modal.SimpleDialog;
import com.masterlock.core.Lock;
import com.masterlock.core.LockConfigAction;
import com.square.flow.appflow.AppFlow;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class PrimaryCodeUpdateKeySafePresenter extends AuthenticatedPresenter<Lock, PrimaryCodeUpdateKeySafeView> {
    public static final int MAX_REQUIRED_CODE_LENGTH = 8;
    public static final int MIN_REQUIRED_CODE_LENGTH = 4;
    @Inject
    CodeTypesUtil mCodeTypeUtils;
    @Inject
    LockService mLockService;
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();

    public PrimaryCodeUpdateKeySafePresenter(Lock lock, PrimaryCodeUpdateKeySafeView primaryCodeUpdateKeySafeView) {
        super(lock, primaryCodeUpdateKeySafeView);
    }

    public void start() {
        super.start();
    }

    public void finish() {
        this.mSubscription.unsubscribe();
        super.finish();
    }

    public void savePrimaryCode(String str) {
        if (((Lock) this.model).isLockerMode()) {
            showLockerModeDialog();
            return;
        }
        if (str.length() < 4 || str.length() > 8) {
            Toast.makeText(((PrimaryCodeUpdateKeySafeView) this.view).getContext(), ((PrimaryCodeUpdateKeySafeView) this.view).getResources().getString(C1075R.string.update_primary_code_error), 0).show();
        } else {
            ((Lock) this.model).setPrimaryCode(str);
            AppFlow.get(((PrimaryCodeUpdateKeySafeView) this.view).getContext()).goTo(new ApplyChanges((Lock) this.model, C1075R.string.title_primary_code, LockConfigAction.PRIMARY_CODE));
        }
    }

    private void showLockerModeDialog() {
        SimpleDialog simpleDialog = new SimpleDialog(((PrimaryCodeUpdateKeySafeView) this.view).getContext(), null);
        Dialog dialog = new Dialog(((PrimaryCodeUpdateKeySafeView) this.view).getContext());
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

    public void validateCode(final String str) {
        this.mSubscription.unsubscribe();
        this.mSubscription = this.mCodeTypeUtils.validatePrimaryCode((Lock) this.model, str).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
            public void onCompleted() {
                Log.d(getClass().getSimpleName(), "Primary Code Validation Complete");
            }

            public void onError(Throwable th) {
                String simpleName = getClass().getSimpleName();
                StringBuilder sb = new StringBuilder();
                sb.append("Error validating primary code: ");
                sb.append(th.getMessage());
                Log.d(simpleName, sb.toString());
            }

            public void onNext(Boolean bool) {
                if (bool.booleanValue()) {
                    PrimaryCodeUpdateKeySafePresenter.this.savePrimaryCode(str);
                } else {
                    PrimaryCodeUpdateKeySafePresenter.this.showErrorDialog();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void showErrorDialog() {
        PrimarySecondaryCodeErrorDialog primarySecondaryCodeErrorDialog = new PrimarySecondaryCodeErrorDialog(((PrimaryCodeUpdateKeySafeView) this.view).getContext(), null);
        Dialog dialog = new Dialog(((PrimaryCodeUpdateKeySafeView) this.view).getContext());
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.setContentView(primarySecondaryCodeErrorDialog);
        primarySecondaryCodeErrorDialog.setPositiveButtonClickListener(new OnClickListener(dialog) {
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
