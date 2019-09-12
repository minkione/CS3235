package com.masterlock.ble.app.presenter.nav.settings;

import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.TimezoneService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class ChangeTimeZonePresenter$$InjectAdapter extends Binding<ChangeTimeZonePresenter> implements MembersInjector<ChangeTimeZonePresenter> {
    private Binding<LockService> mLockService;
    private Binding<ProfileUpdateBasePresenter> supertype;
    private Binding<TimezoneService> timezoneService;

    public ChangeTimeZonePresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.nav.settings.ChangeTimeZonePresenter", false, ChangeTimeZonePresenter.class);
    }

    public void attach(Linker linker) {
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", ChangeTimeZonePresenter.class, getClass().getClassLoader());
        this.timezoneService = linker.requestBinding("com.masterlock.ble.app.service.TimezoneService", ChangeTimeZonePresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.nav.settings.ProfileUpdateBasePresenter", ChangeTimeZonePresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockService);
        set2.add(this.timezoneService);
        set2.add(this.supertype);
    }

    public void injectMembers(ChangeTimeZonePresenter changeTimeZonePresenter) {
        changeTimeZonePresenter.mLockService = (LockService) this.mLockService.get();
        changeTimeZonePresenter.timezoneService = (TimezoneService) this.timezoneService.get();
        this.supertype.injectMembers(changeTimeZonePresenter);
    }
}
