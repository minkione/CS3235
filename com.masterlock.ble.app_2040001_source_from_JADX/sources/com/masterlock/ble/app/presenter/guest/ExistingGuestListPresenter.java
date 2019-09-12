package com.masterlock.ble.app.presenter.guest;

import android.view.View;
import com.masterlock.ble.app.adapters.OnItemClickListener;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.GuestScreens.ExistingGuestListFromInvitationListKeySafe;
import com.masterlock.ble.app.screens.GuestScreens.ExistingGuestListFromSettingsKeySafe;
import com.masterlock.ble.app.screens.GuestScreens.ExistingGuestListKeySafe;
import com.masterlock.ble.app.screens.GuestScreens.TemporaryAccessKeySafe;
import com.masterlock.ble.app.screens.GuestScreens.UpdateGuestFromEditGuestDetailsKeySafe;
import com.masterlock.ble.app.screens.IExistingGuestScreen;
import com.masterlock.ble.app.service.GuestService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.guest.ExistingGuestListView;
import com.masterlock.core.Guest;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;
import java.util.List;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class ExistingGuestListPresenter extends AuthenticatedPresenter<Lock, ExistingGuestListView> {
    /* access modifiers changed from: private */
    public List<Guest> mGuestList;
    @Inject
    GuestService mGuestService;
    @Inject
    IScheduler mScheduler;
    private final IExistingGuestScreen mScreen;
    private Subscription mSubscription = Subscriptions.empty();

    public ExistingGuestListPresenter(ExistingGuestListView existingGuestListView) {
        super(existingGuestListView);
        this.mScreen = (IExistingGuestScreen) AppFlow.getScreen(existingGuestListView.getContext());
        this.model = this.mScreen.getLock();
    }

    public void start() {
        super.start();
        getListOfGuests((Lock) this.model);
    }

    private void getListOfGuests(Lock lock) {
        this.mSubscription.unsubscribe();
        if (this.mScreen instanceof ExistingGuestListFromSettingsKeySafe) {
            this.mSubscription = this.mGuestService.getGuests().subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<List<Guest>>() {
                public void onStart() {
                    ExistingGuestListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
                }

                public void onCompleted() {
                    ExistingGuestListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                }

                public void onError(Throwable th) {
                    ExistingGuestListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                }

                public void onNext(List<Guest> list) {
                    ExistingGuestListPresenter.this.mGuestList = list;
                    ((ExistingGuestListView) ExistingGuestListPresenter.this.view).updateView(ExistingGuestListPresenter.this.mGuestList, ((Lock) ExistingGuestListPresenter.this.model).getName(), ((Lock) ExistingGuestListPresenter.this.model).getKmsDeviceKey().getDeviceId());
                }
            });
        } else {
            this.mSubscription = this.mGuestService.getUninvitedGuestsForProduct(lock.getLockId()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<List<Guest>>() {
                public void onStart() {
                    ExistingGuestListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
                }

                public void onCompleted() {
                    ExistingGuestListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                }

                public void onError(Throwable th) {
                    ExistingGuestListPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                }

                public void onNext(List<Guest> list) {
                    ExistingGuestListPresenter.this.mGuestList = list;
                    ((ExistingGuestListView) ExistingGuestListPresenter.this.view).updateView(ExistingGuestListPresenter.this.mGuestList, ((Lock) ExistingGuestListPresenter.this.model).getName(), ((Lock) ExistingGuestListPresenter.this.model).getKmsDeviceKey().getDeviceId());
                }
            });
        }
    }

    public OnItemClickListener getClickListener() {
        return new OnItemClickListener() {
            public final void onItemClick(View view, int i) {
                ExistingGuestListPresenter.lambda$getClickListener$0(ExistingGuestListPresenter.this, view, i);
            }
        };
    }

    public static /* synthetic */ void lambda$getClickListener$0(ExistingGuestListPresenter existingGuestListPresenter, View view, int i) {
        IExistingGuestScreen iExistingGuestScreen = existingGuestListPresenter.mScreen;
        if (iExistingGuestScreen instanceof ExistingGuestListKeySafe) {
            AppFlow.get(view.getContext()).goTo(new UpdateGuestFromEditGuestDetailsKeySafe((Lock) existingGuestListPresenter.model, (Guest) existingGuestListPresenter.mGuestList.get(i), null, true));
        } else if (iExistingGuestScreen instanceof ExistingGuestListFromInvitationListKeySafe) {
            AppFlow.get(view.getContext()).goTo(new UpdateGuestFromEditGuestDetailsKeySafe((Lock) existingGuestListPresenter.model, (Guest) existingGuestListPresenter.mGuestList.get(i), null, true));
        } else {
            AppFlow.get(view.getContext()).goTo(new TemporaryAccessKeySafe((Lock) existingGuestListPresenter.model, (Guest) existingGuestListPresenter.mGuestList.get(i)));
        }
    }
}
