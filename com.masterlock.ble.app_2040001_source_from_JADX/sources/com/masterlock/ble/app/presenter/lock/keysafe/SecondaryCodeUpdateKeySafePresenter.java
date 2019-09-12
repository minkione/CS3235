package com.masterlock.ble.app.presenter.lock.keysafe;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.LockScreens.ApplyChanges;
import com.masterlock.ble.app.screens.LockScreens.SecondaryCodeUpdateKeySafe;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.CodeTypesUtil;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.lock.keysafe.SecondaryCodeUpdateKeySafeView;
import com.masterlock.ble.app.view.modal.PrimarySecondaryCodeErrorDialog;
import com.masterlock.ble.app.view.modal.SimpleDialog;
import com.masterlock.core.Lock;
import com.masterlock.core.LockConfigAction;
import com.masterlock.core.SecondaryCodeIndex;
import com.square.flow.appflow.AppFlow;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class SecondaryCodeUpdateKeySafePresenter extends AuthenticatedPresenter<Lock, SecondaryCodeUpdateKeySafeView> {
    public static final int MAX_REQUIRED_CODE_LENGTH = 8;
    public static final int MIN_REQUIRED_CODE_LENGTH = 4;
    @Inject
    CodeTypesUtil mCodeTypesUtil;
    private boolean mFromLockDetails;
    private SecondaryCodeIndex mIndex;
    @Inject
    LockService mLockService;
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();
    private String secondaryPasscode;

    public SecondaryCodeUpdateKeySafePresenter(Lock lock, SecondaryCodeUpdateKeySafeView secondaryCodeUpdateKeySafeView) {
        super(lock, secondaryCodeUpdateKeySafeView);
        SecondaryCodeUpdateKeySafe secondaryCodeUpdateKeySafe = (SecondaryCodeUpdateKeySafe) AppFlow.getScreen(secondaryCodeUpdateKeySafeView.getContext());
        this.mFromLockDetails = secondaryCodeUpdateKeySafe.mFromLockDetails;
        this.mIndex = secondaryCodeUpdateKeySafe.mIndex;
    }

    public void start() {
        super.start();
    }

    public void finish() {
        this.mSubscription.unsubscribe();
        super.finish();
    }

    public void validateCode(final String str) {
        this.mSubscription.unsubscribe();
        this.mSubscription = this.mCodeTypesUtil.validateSecondaryCode((Lock) this.model, str, ((Lock) this.model).getAllSecondaryCodes()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
            public void onCompleted() {
                Log.d(getClass().getSimpleName(), "Validation Complete");
            }

            public void onError(Throwable th) {
                String simpleName = getClass().getSimpleName();
                StringBuilder sb = new StringBuilder();
                sb.append("Error validating secondary code: ");
                sb.append(th.getMessage());
                Log.d(simpleName, sb.toString());
            }

            public void onNext(Boolean bool) {
                if (bool.booleanValue()) {
                    SecondaryCodeUpdateKeySafePresenter.this.saveSecondaryCode(str);
                } else {
                    SecondaryCodeUpdateKeySafePresenter.this.showErrorDialog();
                }
            }
        });
    }

    public void saveSecondaryCode(String str) {
        Log.d("SecondaryCodeToSave", str);
        if (((Lock) this.model).isLockerMode()) {
            showLockerModeDialog();
            return;
        }
        if (str.length() >= 4 && str.length() <= 8) {
            ((Lock) this.model).setSecondaryCodeAt(this.mIndex, str);
            if (this.mFromLockDetails) {
                goToApplyChanges();
            } else {
                AppFlow.get(((SecondaryCodeUpdateKeySafeView) this.view).getContext()).goBack();
            }
        }
    }

    private void showLockerModeDialog() {
        SimpleDialog simpleDialog = new SimpleDialog(((SecondaryCodeUpdateKeySafeView) this.view).getContext(), null);
        Dialog dialog = new Dialog(((SecondaryCodeUpdateKeySafeView) this.view).getContext());
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

    public void goToApplyChanges() {
        AppFlow.get(((SecondaryCodeUpdateKeySafeView) this.view).getContext()).goTo(new ApplyChanges((Lock) this.model, C1075R.string.title_secondary_codes, LockConfigAction.SECONDARY_CODES));
    }

    /* access modifiers changed from: private */
    public void showErrorDialog() {
        PrimarySecondaryCodeErrorDialog primarySecondaryCodeErrorDialog = new PrimarySecondaryCodeErrorDialog(((SecondaryCodeUpdateKeySafeView) this.view).getContext(), null);
        Dialog dialog = new Dialog(((SecondaryCodeUpdateKeySafeView) this.view).getContext());
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
