package com.masterlock.ble.app.presenter.nav;

import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.TimezoneService;
import com.masterlock.ble.app.util.IScheduler;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class AccountProfilePresenter$$InjectAdapter extends Binding<AccountProfilePresenter> implements MembersInjector<AccountProfilePresenter> {
    private Binding<IScheduler> mScheduler;
    private Binding<TimezoneService> mTimezoneService;
    private Binding<AuthenticatedPresenter> supertype;

    public AccountProfilePresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.nav.AccountProfilePresenter", false, AccountProfilePresenter.class);
    }

    public void attach(Linker linker) {
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", AccountProfilePresenter.class, getClass().getClassLoader());
        this.mTimezoneService = linker.requestBinding("com.masterlock.ble.app.service.TimezoneService", AccountProfilePresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", AccountProfilePresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mScheduler);
        set2.add(this.mTimezoneService);
        set2.add(this.supertype);
    }

    public void injectMembers(AccountProfilePresenter accountProfilePresenter) {
        accountProfilePresenter.mScheduler = (IScheduler) this.mScheduler.get();
        accountProfilePresenter.mTimezoneService = (TimezoneService) this.mTimezoneService.get();
        this.supertype.injectMembers(accountProfilePresenter);
    }
}
