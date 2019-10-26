package com.masterlock.ble.app.presenter.nav.settings;

import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.ProfileUpdateService;
import com.masterlock.ble.app.util.IScheduler;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class ProfileUpdateBasePresenter$$InjectAdapter extends Binding<ProfileUpdateBasePresenter> implements MembersInjector<ProfileUpdateBasePresenter> {
    private Binding<ProfileUpdateService> mProfileUpdateService;
    private Binding<IScheduler> mScheduler;
    private Binding<AuthenticatedPresenter> supertype;

    public ProfileUpdateBasePresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.nav.settings.ProfileUpdateBasePresenter", false, ProfileUpdateBasePresenter.class);
    }

    public void attach(Linker linker) {
        this.mProfileUpdateService = linker.requestBinding("com.masterlock.ble.app.service.ProfileUpdateService", ProfileUpdateBasePresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", ProfileUpdateBasePresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", ProfileUpdateBasePresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mProfileUpdateService);
        set2.add(this.mScheduler);
        set2.add(this.supertype);
    }

    public void injectMembers(ProfileUpdateBasePresenter profileUpdateBasePresenter) {
        profileUpdateBasePresenter.mProfileUpdateService = (ProfileUpdateService) this.mProfileUpdateService.get();
        profileUpdateBasePresenter.mScheduler = (IScheduler) this.mScheduler.get();
        this.supertype.injectMembers(profileUpdateBasePresenter);
    }
}
