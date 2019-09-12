package com.masterlock.ble.app.presenter.nav;

import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.service.SignInService;
import com.masterlock.ble.app.util.IScheduler;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class AccountSettingsPresenter$$InjectAdapter extends Binding<AccountSettingsPresenter> implements MembersInjector<AccountSettingsPresenter> {
    private Binding<IScheduler> mScheduler;
    private Binding<SignInService> mSignUpService;
    private Binding<Presenter> supertype;

    public AccountSettingsPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.nav.AccountSettingsPresenter", false, AccountSettingsPresenter.class);
    }

    public void attach(Linker linker) {
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", AccountSettingsPresenter.class, getClass().getClassLoader());
        this.mSignUpService = linker.requestBinding("com.masterlock.ble.app.service.SignInService", AccountSettingsPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.Presenter", AccountSettingsPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mScheduler);
        set2.add(this.mSignUpService);
        set2.add(this.supertype);
    }

    public void injectMembers(AccountSettingsPresenter accountSettingsPresenter) {
        accountSettingsPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        accountSettingsPresenter.mSignUpService = (SignInService) this.mSignUpService.get();
        this.supertype.injectMembers(accountSettingsPresenter);
    }
}
