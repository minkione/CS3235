package com.masterlock.ble.app.presenter.guest;

import android.app.Dialog;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.bus.AddGuestEvent;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.provider.MasterlockContract.Locks;
import com.masterlock.ble.app.screens.GuestScreens.InvitationDetailFromInvitationListKeySafe;
import com.masterlock.ble.app.service.GuestService;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.ProductInvitationService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.guest.InvitationListView;
import com.masterlock.ble.app.view.modal.AddGuestModal;
import com.masterlock.ble.app.view.modal.DeleteInvitationDialog;
import com.masterlock.core.Guest;
import com.masterlock.core.Invitation;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import java.util.List;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class InvitationListPresenter extends AuthenticatedPresenter<Lock, InvitationListView> {
    /* access modifiers changed from: private */
    public Dialog dialog;
    @Inject
    ContentResolver mContentResolver;
    private Subscription mDeleteGuestSubscription = Subscriptions.empty();
    private Subscription mDeleteInvitationSubscription = Subscriptions.empty();
    Runnable mDialogDelay = new Runnable() {
        public void run() {
            InvitationListPresenter.this.dialog.show();
        }
    };
    @Inject
    Bus mEventBus;
    private Subscription mGetUninvitedGuestListSubscription = Subscriptions.empty();
    @Inject
    GuestService mGuestService;
    @Inject
    ProductInvitationService mInvitationService;
    @Inject
    LockService mLockService;
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();
    final ContentObserver observer = new ContentObserver(new Handler()) {
        public void onChange(boolean z) {
            InvitationListPresenter.this.refresh();
        }
    };

    public InvitationListPresenter(Lock lock, InvitationListView invitationListView) {
        super(lock, invitationListView);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        super.start();
        this.mEventBus.register(this);
        refresh();
        this.mContentResolver.registerContentObserver(Locks.buildInvitationsDirUri(((Lock) this.model).getLockId()), true, this.observer);
    }

    /* access modifiers changed from: private */
    public void refresh() {
        if (this.model != null) {
            this.mSubscription.unsubscribe();
            this.mSubscription = this.mLockService.get(((Lock) this.model).getLockId()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Lock>() {
                public void onStart() {
                    InvitationListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
                }

                public void onCompleted() {
                    InvitationListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                }

                public void onError(Throwable th) {
                    InvitationListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                    ((InvitationListView) InvitationListPresenter.this.view).displayError(ApiError.generateError(th));
                }

                public void onNext(Lock lock) {
                    if (lock == null) {
                        AppFlow.get(((InvitationListView) InvitationListPresenter.this.view).getContext()).goBack();
                        return;
                    }
                    InvitationListPresenter.this.model = lock;
                    if (InvitationListPresenter.this.view != null) {
                        ((InvitationListView) InvitationListPresenter.this.view).updateView(lock);
                    }
                }
            });
        }
    }

    public void finish() {
        if (((InvitationListView) this.view).getHandler() != null) {
            ((InvitationListView) this.view).getHandler().removeCallbacks(this.mDialogDelay);
        }
        Dialog dialog2 = this.dialog;
        if (dialog2 != null) {
            dialog2.dismiss();
            this.dialog = null;
        }
        super.finish();
        this.mSubscription.unsubscribe();
        this.mDeleteInvitationSubscription.unsubscribe();
        this.mDeleteGuestSubscription.unsubscribe();
        this.mEventBus.unregister(this);
        this.mContentResolver.unregisterContentObserver(this.observer);
    }

    @Subscribe
    public void goToAddGuest(AddGuestEvent addGuestEvent) {
        getListOfGuests((Lock) this.model);
    }

    public void deleteInvitation(Invitation invitation) {
        ((InvitationListView) this.view).getHandler().removeCallbacks(this.mDialogDelay);
        Dialog dialog2 = this.dialog;
        if (dialog2 != null && dialog2.isShowing()) {
            this.dialog.dismiss();
        }
        DeleteInvitationDialog deleteInvitationDialog = new DeleteInvitationDialog(((InvitationListView) this.view).getContext());
        this.dialog = new Dialog(((InvitationListView) this.view).getContext());
        this.dialog.requestWindowFeature(1);
        this.dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.dialog.setContentView(deleteInvitationDialog);
        deleteInvitationDialog.setRevokeAccessButtonClickListener(new OnClickListener(invitation) {
            private final /* synthetic */ Invitation f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                InvitationListPresenter.lambda$deleteInvitation$0(InvitationListPresenter.this, this.f$1, view);
            }
        });
        deleteInvitationDialog.setCancelButtonClickListener(new OnClickListener() {
            public final void onClick(View view) {
                InvitationListPresenter.this.dialog.dismiss();
            }
        });
        deleteInvitationDialog.setDeleteGuestButtonClickListener(new OnClickListener(invitation) {
            private final /* synthetic */ Invitation f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                InvitationListPresenter.lambda$deleteInvitation$2(InvitationListPresenter.this, this.f$1, view);
            }
        });
        if (!this.dialog.isShowing()) {
            ((InvitationListView) this.view).getHandler().postDelayed(this.mDialogDelay, 350);
        }
    }

    public static /* synthetic */ void lambda$deleteInvitation$0(InvitationListPresenter invitationListPresenter, Invitation invitation, View view) {
        invitationListPresenter.dialog.dismiss();
        invitationListPresenter.sendDeleteInvitation(invitation);
    }

    public static /* synthetic */ void lambda$deleteInvitation$2(InvitationListPresenter invitationListPresenter, Invitation invitation, View view) {
        invitationListPresenter.dialog.dismiss();
        invitationListPresenter.sendDeleteGuest(invitation);
    }

    private void sendDeleteGuest(Invitation invitation) {
        this.mDeleteGuestSubscription.unsubscribe();
        this.mDeleteGuestSubscription = this.mGuestService.deleteGuest(invitation.getGuest()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<String>() {
            public void onStart() {
                InvitationListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                InvitationListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                InvitationListPresenter.this.refresh();
            }

            public void onError(Throwable th) {
                InvitationListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ((InvitationListView) InvitationListPresenter.this.view).displayError(ApiError.generateError(th));
            }

            public void onNext(String str) {
                ((InvitationListView) InvitationListPresenter.this.view).showGuestDeleted();
            }
        });
    }

    public void goToInvitation(Invitation invitation) {
        AppFlow.get(((InvitationListView) this.view).getContext()).goTo(new InvitationDetailFromInvitationListKeySafe((Lock) this.model, invitation));
    }

    private void sendDeleteInvitation(Invitation invitation) {
        this.mDeleteInvitationSubscription.unsubscribe();
        this.mDeleteInvitationSubscription = this.mInvitationService.delete(((Lock) this.model).getLockId(), invitation.getId()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
            public void onStart() {
                InvitationListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                InvitationListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                InvitationListPresenter.this.refresh();
            }

            public void onError(Throwable th) {
                InvitationListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ((InvitationListView) InvitationListPresenter.this.view).displayError(ApiError.generateError(th));
            }

            public void onNext(Boolean bool) {
                ((InvitationListView) InvitationListPresenter.this.view).showAccessRevoked();
            }
        });
    }

    private void getListOfGuests(Lock lock) {
        this.mGetUninvitedGuestListSubscription.unsubscribe();
        this.mGetUninvitedGuestListSubscription = this.mGuestService.getUninvitedGuestsForProduct(lock.getLockId()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<List<Guest>>() {
            public void onStart() {
                InvitationListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                InvitationListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                InvitationListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onNext(List<Guest> list) {
                Dialog dialog = new Dialog(((InvitationListView) InvitationListPresenter.this.view).getContext());
                new AddGuestModal(((InvitationListView) InvitationListPresenter.this.view).getContext(), dialog, (Lock) InvitationListPresenter.this.model).getExistingGuestButton().setVisibility(list.isEmpty() ? 8 : 0);
                dialog.show();
            }
        });
    }
}
