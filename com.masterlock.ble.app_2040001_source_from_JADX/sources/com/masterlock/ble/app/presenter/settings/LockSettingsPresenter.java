package com.masterlock.ble.app.presenter.settings;

import android.app.Dialog;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.analytics.Analytics;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.provider.MasterlockContract.Locks;
import com.masterlock.ble.app.screens.LockScreens.LockList;
import com.masterlock.ble.app.screens.SettingsScreens.AboutLockKeySafe;
import com.masterlock.ble.app.screens.SettingsScreens.BackupMasterCombinationKeySafe;
import com.masterlock.ble.app.screens.SettingsScreens.BackupMasterCombinationPadLock;
import com.masterlock.ble.app.screens.SettingsScreens.CalibrateLock;
import com.masterlock.ble.app.screens.SettingsScreens.LockNameKeySafe;
import com.masterlock.ble.app.screens.SettingsScreens.LockNotesKeySafe;
import com.masterlock.ble.app.screens.SettingsScreens.LockTimezoneKeySafe;
import com.masterlock.ble.app.screens.SettingsScreens.ResetKeysKeySafe;
import com.masterlock.ble.app.screens.SettingsScreens.ShareTemporaryCodes;
import com.masterlock.ble.app.screens.SettingsScreens.UnlockModeListKeySafe;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.ProductInvitationService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.modal.AddGuestModal;
import com.masterlock.ble.app.view.modal.SimpleDialog;
import com.masterlock.ble.app.view.settings.LockSettingsView;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import java.util.List;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class LockSettingsPresenter extends AuthenticatedPresenter<Lock, LockSettingsView> {
    @Inject
    Bus mBus;
    @Inject
    ContentResolver mContentResolver;
    private Subscription mDeleteSubscription = Subscriptions.empty();
    @Inject
    LockService mLockService;
    final ContentObserver mObserver = new ContentObserver(new Handler()) {
        public void onChange(boolean z) {
            LockSettingsPresenter.this.reload();
        }
    };
    @Inject
    ProductInvitationService mProductInvitationService;
    private Subscription mRefreshLocksSubscription = Subscriptions.empty();
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();

    public LockSettingsPresenter(Lock lock, LockSettingsView lockSettingsView) {
        super(lock, lockSettingsView);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        super.start();
        reload();
        this.mBus.register(this);
        this.mContentResolver.registerContentObserver(Locks.buildLockUri(((Lock) this.model).getLockId()), true, this.mObserver);
    }

    public void finish() {
        super.finish();
        this.mSubscription.unsubscribe();
        this.mDeleteSubscription.unsubscribe();
        this.mRefreshLocksSubscription.unsubscribe();
        this.mContentResolver.unregisterContentObserver(this.mObserver);
        this.mBus.unregister(this);
    }

    /* access modifiers changed from: private */
    public void reload() {
        if (this.model != null) {
            this.mSubscription.unsubscribe();
            this.mSubscription = this.mLockService.get(((Lock) this.model).getLockId()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Lock>() {
                public void onStart() {
                    LockSettingsPresenter.this.mBus.post(new ToggleProgressBarEvent(true));
                }

                public void onCompleted() {
                    LockSettingsPresenter.this.mBus.post(new ToggleProgressBarEvent(false));
                }

                public void onError(Throwable th) {
                    LockSettingsPresenter.this.mBus.post(new ToggleProgressBarEvent(false));
                    ((LockSettingsView) LockSettingsPresenter.this.view).displayError(ApiError.generateError(th));
                }

                public void onNext(Lock lock) {
                    if (lock == null) {
                        AppFlow.get(((LockSettingsView) LockSettingsPresenter.this.view).getContext()).goBack();
                        return;
                    }
                    LockSettingsPresenter.this.model = lock;
                    if (LockSettingsPresenter.this.view != null) {
                        ((LockSettingsView) LockSettingsPresenter.this.view).updateView(lock);
                    }
                }
            });
        }
    }

    public void deleteLock() {
        SimpleDialog simpleDialog = new SimpleDialog(((LockSettingsView) this.view).getContext(), null);
        Dialog dialog = new Dialog(((LockSettingsView) this.view).getContext());
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.setContentView(simpleDialog);
        simpleDialog.setPositiveButton((int) C1075R.string.delete);
        simpleDialog.setMessage((int) C1075R.string.delete_confirm_message);
        simpleDialog.setPositiveButtonClickListener(new OnClickListener(dialog) {
            private final /* synthetic */ Dialog f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                LockSettingsPresenter.lambda$deleteLock$0(LockSettingsPresenter.this, this.f$1, view);
            }
        });
        simpleDialog.setNegativeButtonClickListener(new OnClickListener(dialog) {
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

    public static /* synthetic */ void lambda$deleteLock$0(LockSettingsPresenter lockSettingsPresenter, Dialog dialog, View view) {
        dialog.dismiss();
        lockSettingsPresenter.sendDelete();
        ((LockSettingsView) lockSettingsPresenter.view).enableDelete(false);
    }

    private void sendDelete() {
        this.mDeleteSubscription.unsubscribe();
        this.mDeleteSubscription = this.mLockService.delete(((Lock) this.model).getLockId()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
            public void onStart() {
                LockSettingsPresenter.this.mBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                LockSettingsPresenter.this.mBus.post(new ToggleProgressBarEvent(false));
                MasterLockApp.get().getAnalytics().logEvent(Analytics.CATEGORY_MASTERLOCK_EVENT, Analytics.ACTION_DELETE_LOCK, Analytics.ACTION_DELETE_LOCK, 1);
            }

            public void onError(Throwable th) {
                ApiError generateError = ApiError.generateError(th);
                LockSettingsPresenter.this.mBus.post(new ToggleProgressBarEvent(false));
                ((LockSettingsView) LockSettingsPresenter.this.view).enableDelete(true);
                if (ApiError.INVALID_ID.equals(generateError.getCode())) {
                    LockSettingsPresenter.this.refreshLocks();
                } else {
                    ((LockSettingsView) LockSettingsPresenter.this.view).displayError(generateError);
                }
            }

            public void onNext(Boolean bool) {
                if (!bool.booleanValue()) {
                    ((LockSettingsView) LockSettingsPresenter.this.view).displayError(new Throwable(((LockSettingsView) LockSettingsPresenter.this.view).getResources().getString(C1075R.string.error_unable_to_delete_lock)));
                    ((LockSettingsView) LockSettingsPresenter.this.view).enableDelete(true);
                    return;
                }
                AppFlow.get(((LockSettingsView) LockSettingsPresenter.this.view).getContext()).resetTo(new LockList());
            }
        });
    }

    /* access modifiers changed from: private */
    public void refreshLocks() {
        this.mRefreshLocksSubscription.unsubscribe();
        this.mRefreshLocksSubscription = this.mLockService.getProducts().subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<List<Lock>>() {
            public void onNext(List<Lock> list) {
            }

            public void onStart() {
                LockSettingsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
                AppFlow.get(((LockSettingsView) LockSettingsPresenter.this.view).getContext()).resetTo(new LockList());
            }

            public void onCompleted() {
                LockSettingsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                LockSettingsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }
        });
    }

    public void goToUnlockMode() {
        AppFlow.get(((LockSettingsView) this.view).getContext()).goTo(new UnlockModeListKeySafe((Lock) this.model));
    }

    public void calibrate() {
        AppFlow.get(((LockSettingsView) this.view).getContext()).goTo(new CalibrateLock((Lock) this.model));
    }

    public void goToBackupMasterCombination() {
        if (((Lock) this.model).isPadLock()) {
            AppFlow.get(((LockSettingsView) this.view).getContext()).goTo(new BackupMasterCombinationPadLock((Lock) this.model));
        } else {
            AppFlow.get(((LockSettingsView) this.view).getContext()).goTo(new BackupMasterCombinationKeySafe((Lock) this.model));
        }
    }

    public void renameLock() {
        AppFlow.get(((LockSettingsView) this.view).getContext()).goTo(new LockNameKeySafe((Lock) this.model));
    }

    public void editNotes() {
        AppFlow.get(((LockSettingsView) this.view).getContext()).goTo(new LockNotesKeySafe((Lock) this.model));
    }

    public void editTimeZone() {
        AppFlow.get(((LockSettingsView) this.view).getContext()).goTo(new LockTimezoneKeySafe((Lock) this.model));
    }

    public void goToAboutLock() {
        AppFlow.get(((LockSettingsView) this.view).getContext()).goTo(new AboutLockKeySafe((Lock) this.model));
    }

    public void goToUpdateFirmware() {
        AppFlow.get(((LockSettingsView) this.view).getContext()).goTo(new AboutLockKeySafe((Lock) this.model));
    }

    public void goToShareTemporaryCodes() {
        AppFlow.get(((LockSettingsView) this.view).getContext()).goTo(new ShareTemporaryCodes((Lock) this.model));
    }

    public void showGuestSelectionModal() {
        Dialog dialog = new Dialog(((LockSettingsView) this.view).getContext());
        AddGuestModal addGuestModal = new AddGuestModal(((LockSettingsView) this.view).getContext(), dialog, (Lock) this.model);
        addGuestModal.getBody().setText(((LockSettingsView) this.view).getContext().getString(C1075R.string.txt_temp_code_guest_modal_desc));
        addGuestModal.getExistingGuestButton().setText(((LockSettingsView) this.view).getContext().getString(C1075R.string.send_temp_code_modal_existing));
        addGuestModal.getAddFromContactsButton().setText(((LockSettingsView) this.view).getContext().getString(C1075R.string.send_temp_code_modal_contacts));
        addGuestModal.getEnterManuallyButton().setText(((LockSettingsView) this.view).getContext().getString(C1075R.string.send_temp_code_modal_manually));
        dialog.show();
    }

    public void resetKeys() {
        AppFlow.get(((LockSettingsView) this.view).getContext()).goTo(new ResetKeysKeySafe((Lock) this.model));
    }
}
