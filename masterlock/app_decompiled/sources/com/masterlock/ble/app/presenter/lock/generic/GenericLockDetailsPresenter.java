package com.masterlock.ble.app.presenter.lock.generic;

import android.app.Dialog;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.bus.DeleteLockEvent;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.provider.MasterlockContract.Locks;
import com.masterlock.ble.app.screens.LockScreens.AddMechanicalLock;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.lock.generic.GenericLockDetailsView;
import com.masterlock.ble.app.view.modal.SimpleDialog;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class GenericLockDetailsPresenter extends AuthenticatedPresenter<Lock, GenericLockDetailsView> {
    /* access modifiers changed from: private */
    public boolean isLoading = false;
    @Inject
    ContentResolver mContentResolver;
    private Subscription mDeleteSubscription = Subscriptions.empty();
    @Inject
    Bus mEventBus;
    @Inject
    LockService mLockService;
    final ContentObserver mObserver = new ContentObserver(new Handler()) {
        public void onChange(boolean z) {
            GenericLockDetailsPresenter.this.reload();
        }
    };
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();

    public GenericLockDetailsPresenter(GenericLockDetailsView genericLockDetailsView, Lock lock) {
        super(lock, genericLockDetailsView);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        super.start();
        reload();
        this.mEventBus.register(this);
        this.mContentResolver.registerContentObserver(Locks.buildLockUri(((Lock) this.model).getLockId()), true, this.mObserver);
    }

    public void goToEditGenericLock() {
        AppFlow.get(((GenericLockDetailsView) this.view).getContext()).goTo(new AddMechanicalLock((Lock) this.model));
    }

    @Subscribe
    public void deleteLock(DeleteLockEvent deleteLockEvent) {
        deleteLock();
    }

    public void deleteLock() {
        SimpleDialog simpleDialog = new SimpleDialog(((GenericLockDetailsView) this.view).getContext(), null);
        Dialog dialog = new Dialog(((GenericLockDetailsView) this.view).getContext());
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
                GenericLockDetailsPresenter.lambda$deleteLock$0(GenericLockDetailsPresenter.this, this.f$1, view);
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

    public static /* synthetic */ void lambda$deleteLock$0(GenericLockDetailsPresenter genericLockDetailsPresenter, Dialog dialog, View view) {
        dialog.dismiss();
        genericLockDetailsPresenter.deleteLockCall();
    }

    private void deleteLockCall() {
        this.mDeleteSubscription.unsubscribe();
        this.mDeleteSubscription = this.mLockService.delete(((Lock) this.model).getLockId()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
            public void onStart() {
                GenericLockDetailsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
                GenericLockDetailsPresenter.this.isLoading = true;
            }

            public void onCompleted() {
                GenericLockDetailsPresenter.this.isLoading = false;
                GenericLockDetailsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                GenericLockDetailsPresenter.this.isLoading = false;
                ((GenericLockDetailsView) GenericLockDetailsPresenter.this.view).displayError(th);
                GenericLockDetailsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onNext(Boolean bool) {
                GenericLockDetailsPresenter.this.isLoading = false;
                if (!bool.booleanValue()) {
                    ((GenericLockDetailsView) GenericLockDetailsPresenter.this.view).displayError(new Throwable(((GenericLockDetailsView) GenericLockDetailsPresenter.this.view).getResources().getString(C1075R.string.error_unable_to_delete_lock)));
                    return;
                }
                AppFlow.get(((GenericLockDetailsView) GenericLockDetailsPresenter.this.view).getContext()).goBack();
            }
        });
    }

    public void reload() {
        if (this.model != null) {
            this.mSubscription.unsubscribe();
            this.mSubscription = this.mLockService.get(((Lock) this.model).getLockId()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Lock>() {
                public void onStart() {
                    GenericLockDetailsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
                }

                public void onCompleted() {
                    GenericLockDetailsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                }

                public void onError(Throwable th) {
                    GenericLockDetailsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                    ((GenericLockDetailsView) GenericLockDetailsPresenter.this.view).displayError(th);
                }

                public void onNext(Lock lock) {
                    if (lock == null) {
                        AppFlow.get(((GenericLockDetailsView) GenericLockDetailsPresenter.this.view).getContext()).goBack();
                        return;
                    }
                    GenericLockDetailsPresenter.this.model = lock;
                    if (GenericLockDetailsPresenter.this.view != null) {
                        ((GenericLockDetailsView) GenericLockDetailsPresenter.this.view).updateView(lock);
                    }
                }
            });
        }
    }

    public void finish() {
        super.finish();
        this.mEventBus.unregister(this);
        this.mDeleteSubscription.unsubscribe();
        this.mSubscription.unsubscribe();
        this.mContentResolver.unregisterContentObserver(this.mObserver);
    }
}
