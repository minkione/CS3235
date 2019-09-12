package com.masterlock.ble.app.presenter.nav.settings;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.google.common.base.Strings;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.activity.LockActivity;
import com.masterlock.ble.app.bus.ChangeSoftKeyboardBehaviorEvent;
import com.masterlock.ble.app.screens.NavScreens.AccountProfile;
import com.masterlock.ble.app.util.PasscodeUtil;
import com.masterlock.ble.app.util.ViewUtil;
import com.masterlock.ble.app.view.modal.BaseModal;
import com.masterlock.ble.app.view.modal.ManageLockDialog;
import com.masterlock.ble.app.view.nav.settings.ChangeUserNameView;
import com.masterlock.core.AccountProfileInfo;
import com.masterlock.core.ProfileUpdateFields;
import com.square.flow.appflow.AppFlow;
import java.text.MessageFormat;
import java.util.regex.Pattern;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;
import retrofit.client.Response;

public class ChangeUsernamePresenter extends ProfileUpdateBasePresenter<AccountProfileInfo, ChangeUserNameView> {
    /* access modifiers changed from: private */
    public Dialog mDialog;
    private Subscription mPasscodeSubscription = Subscriptions.empty();

    public ChangeUsernamePresenter(ChangeUserNameView changeUserNameView) {
        super(changeUserNameView);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        super.start();
        ((ChangeUserNameView) this.view).setCurrentUsername(((AccountProfileInfo) this.model).getUserName());
        this.mEventBus.post(new ChangeSoftKeyboardBehaviorEvent(32));
    }

    public void finish() {
        this.mEventBus.post(new ChangeSoftKeyboardBehaviorEvent(16));
        super.finish();
    }

    public void checkUsername(String str) {
        if (str.equals(this.prefs.getUsername())) {
            AppFlow.get(((ChangeUserNameView) this.view).getContext()).resetTo(new AccountProfile());
        } else if (!isValidUsername(str)) {
            ((ChangeUserNameView) this.view).displayMessage((int) C1075R.string.change_username_warning);
        } else {
            updateUserName(str);
        }
    }

    public boolean isValidUsername(String str) {
        return Pattern.compile("[A-Za-z]").matcher(str).find();
    }

    public void updateUserName(final String str) {
        ((AccountProfileInfo) this.model).setFieldToUpdate(ProfileUpdateFields.USER_NAME);
        ((AccountProfileInfo) this.model).setUserName(str);
        performProfileUpdate(new Subscriber<Response>() {
            public void onNext(Response response) {
            }

            public void onCompleted() {
                Toast.makeText(((ChangeUserNameView) ChangeUsernamePresenter.this.view).getContext(), ((ChangeUserNameView) ChangeUsernamePresenter.this.view).getResources().getString(C1075R.string.change_username_user_save), 0).show();
                ChangeUsernamePresenter.this.prefs.putUsername(str);
                MasterLockApp.get().logOut();
            }

            public void onError(Throwable th) {
                ApiError generateError = ApiError.generateError(th);
                if (Strings.isNullOrEmpty(generateError.getCode())) {
                    ((ChangeUserNameView) ChangeUsernamePresenter.this.view).displayMessage(generateError.getMessage());
                } else if (ApiError.USER_NAME_ALREADY_TAKEN.equals(generateError.getCode())) {
                    ChangeUsernamePresenter.this.changeUsernameModal(0);
                } else if (ApiError.INVALID_USER_NAME.equals(generateError.getCode())) {
                    ChangeUsernamePresenter.this.changeUsernameModal(1);
                } else {
                    ((ChangeUserNameView) ChangeUsernamePresenter.this.view).displayMessage(generateError.getMessage());
                }
            }
        });
    }

    public void changeUsernameModal(int i) {
        BaseModal baseModal = new BaseModal(((ChangeUserNameView) this.view).getContext());
        Dialog dialog = new Dialog(((ChangeUserNameView) this.view).getContext());
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.requestWindowFeature(1);
        dialog.setContentView(baseModal);
        dialog.setCancelable(false);
        $$Lambda$ChangeUsernamePresenter$Iql5CTFO93j0kC4IT5rJnQPnR6Q r4 = new OnClickListener(dialog) {
            private final /* synthetic */ Dialog f$0;

            {
                this.f$0 = r1;
            }

            public final void onClick(View view) {
                this.f$0.dismiss();
            }
        };
        baseModal.setTitle(MessageFormat.format(((ChangeUserNameView) this.view).getResources().getString(C1075R.string.change_username_error_title), new Object[]{Integer.valueOf(i)}));
        baseModal.setBody(MessageFormat.format(((ChangeUserNameView) this.view).getResources().getString(C1075R.string.change_username_error_description), new Object[]{Integer.valueOf(i)}));
        baseModal.getPositiveButton().setText(((ChangeUserNameView) this.view).getContext().getResources().getString(C1075R.string.f165ok));
        baseModal.setPositiveButtonClickListener(r4);
        baseModal.getNegativeButton().setVisibility(8);
        dialog.show();
    }

    public void verifyPassword() {
        PasscodeUtil passcodeUtil = new PasscodeUtil((View) this.view);
        ManageLockDialog manageLockDialog = passcodeUtil.getManageLockDialog();
        this.mDialog = passcodeUtil.getDialog();
        manageLockDialog.setPositiveButtonClickListener(new OnClickListener(manageLockDialog, passcodeUtil) {
            private final /* synthetic */ ManageLockDialog f$1;
            private final /* synthetic */ PasscodeUtil f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void onClick(View view) {
                ChangeUsernamePresenter.lambda$verifyPassword$1(ChangeUsernamePresenter.this, this.f$1, this.f$2, view);
            }
        });
        manageLockDialog.setNegativeButtonClickListener(new OnClickListener(manageLockDialog) {
            private final /* synthetic */ ManageLockDialog f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                ChangeUsernamePresenter.lambda$verifyPassword$2(ChangeUsernamePresenter.this, this.f$1, view);
            }
        });
        manageLockDialog.setForgotPasscodeButtonClickListener(new OnClickListener(passcodeUtil) {
            private final /* synthetic */ PasscodeUtil f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                ChangeUsernamePresenter.lambda$verifyPassword$3(ChangeUsernamePresenter.this, this.f$1, view);
            }
        });
        this.mDialog.show();
    }

    public static /* synthetic */ void lambda$verifyPassword$1(ChangeUsernamePresenter changeUsernamePresenter, ManageLockDialog manageLockDialog, PasscodeUtil passcodeUtil, View view) {
        manageLockDialog.showLoading(true);
        changeUsernamePresenter.mPasscodeSubscription.unsubscribe();
        changeUsernamePresenter.mPasscodeSubscription = passcodeUtil.verifyPasscode(manageLockDialog).subscribeOn(changeUsernamePresenter.mScheduler.background()).observeOn(changeUsernamePresenter.mScheduler.main()).subscribe(changeUsernamePresenter.createPasscodeSubscriber(manageLockDialog));
    }

    public static /* synthetic */ void lambda$verifyPassword$2(ChangeUsernamePresenter changeUsernamePresenter, ManageLockDialog manageLockDialog, View view) {
        ViewUtil.hideKeyboard(changeUsernamePresenter.mDialog.getContext(), manageLockDialog.getWindowToken());
        changeUsernamePresenter.mDialog.dismiss();
    }

    public static /* synthetic */ void lambda$verifyPassword$3(ChangeUsernamePresenter changeUsernamePresenter, PasscodeUtil passcodeUtil, View view) {
        changeUsernamePresenter.mDialog.dismiss();
        passcodeUtil.forgotPasscode();
    }

    private Subscriber<Void> createPasscodeSubscriber(final ManageLockDialog manageLockDialog) {
        return new Subscriber<Void>() {
            public void onNext(Void voidR) {
            }

            public void onCompleted() {
                MasterLockApp.get().successfullyVerified();
                manageLockDialog.showLoading(false);
                ChangeUsernamePresenter.this.mDialog.dismiss();
                ViewUtil.hideKeyboard(ChangeUsernamePresenter.this.mDialog.getContext(), manageLockDialog.getWindowToken());
                MasterLockSharedPreferences.getInstance().putCanManageLock(true);
                LockActivity.restartPasscodeTimer();
                ChangeUsernamePresenter.this.updateUserName(null);
            }

            public void onError(Throwable th) {
                MasterLockApp.get().invalidVerification();
                if (!ApiError.generateError(th).isHandled()) {
                    manageLockDialog.showLoading(false);
                    manageLockDialog.showPasscodeError();
                    return;
                }
                ChangeUsernamePresenter.this.mDialog.dismiss();
            }
        };
    }
}
