package com.masterlock.ble.app.presenter.guest;

import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.GuestService;
import com.masterlock.ble.app.util.IScheduler;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class ExistingGuestListPresenter$$InjectAdapter extends Binding<ExistingGuestListPresenter> implements MembersInjector<ExistingGuestListPresenter> {
    private Binding<GuestService> mGuestService;
    private Binding<IScheduler> mScheduler;
    private Binding<AuthenticatedPresenter> supertype;

    public ExistingGuestListPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.guest.ExistingGuestListPresenter", false, ExistingGuestListPresenter.class);
    }

    public void attach(Linker linker) {
        this.mGuestService = linker.requestBinding("com.masterlock.ble.app.service.GuestService", ExistingGuestListPresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", ExistingGuestListPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", ExistingGuestListPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mGuestService);
        set2.add(this.mScheduler);
        set2.add(this.supertype);
    }

    public void injectMembers(ExistingGuestListPresenter existingGuestListPresenter) {
        existingGuestListPresenter.mGuestService = (GuestService) this.mGuestService.get();
        existingGuestListPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        this.supertype.injectMembers(existingGuestListPresenter);
    }
}
