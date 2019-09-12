package com.masterlock.ble.app.presenter.guest;

import android.content.ContentResolver;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.GuestService;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.ProductInvitationService;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class InvitationListPresenter$$InjectAdapter extends Binding<InvitationListPresenter> implements MembersInjector<InvitationListPresenter> {
    private Binding<ContentResolver> mContentResolver;
    private Binding<Bus> mEventBus;
    private Binding<GuestService> mGuestService;
    private Binding<ProductInvitationService> mInvitationService;
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<AuthenticatedPresenter> supertype;

    public InvitationListPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.guest.InvitationListPresenter", false, InvitationListPresenter.class);
    }

    public void attach(Linker linker) {
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", InvitationListPresenter.class, getClass().getClassLoader());
        this.mInvitationService = linker.requestBinding("com.masterlock.ble.app.service.ProductInvitationService", InvitationListPresenter.class, getClass().getClassLoader());
        this.mGuestService = linker.requestBinding("com.masterlock.ble.app.service.GuestService", InvitationListPresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", InvitationListPresenter.class, getClass().getClassLoader());
        this.mContentResolver = linker.requestBinding("android.content.ContentResolver", InvitationListPresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", InvitationListPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", InvitationListPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockService);
        set2.add(this.mInvitationService);
        set2.add(this.mGuestService);
        set2.add(this.mEventBus);
        set2.add(this.mContentResolver);
        set2.add(this.mScheduler);
        set2.add(this.supertype);
    }

    public void injectMembers(InvitationListPresenter invitationListPresenter) {
        invitationListPresenter.mLockService = (LockService) this.mLockService.get();
        invitationListPresenter.mInvitationService = (ProductInvitationService) this.mInvitationService.get();
        invitationListPresenter.mGuestService = (GuestService) this.mGuestService.get();
        invitationListPresenter.mEventBus = (Bus) this.mEventBus.get();
        invitationListPresenter.mContentResolver = (ContentResolver) this.mContentResolver.get();
        invitationListPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        this.supertype.injectMembers(invitationListPresenter);
    }
}
