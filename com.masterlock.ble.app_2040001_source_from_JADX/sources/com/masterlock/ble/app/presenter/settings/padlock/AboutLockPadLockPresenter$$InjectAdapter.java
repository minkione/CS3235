package com.masterlock.ble.app.presenter.settings.padlock;

import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class AboutLockPadLockPresenter$$InjectAdapter extends Binding<AboutLockPadLockPresenter> implements MembersInjector<AboutLockPadLockPresenter> {
    private Binding<Bus> mEventBus;
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<AuthenticatedPresenter> supertype;

    public AboutLockPadLockPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.settings.padlock.AboutLockPadLockPresenter", false, AboutLockPadLockPresenter.class);
    }

    public void attach(Linker linker) {
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", AboutLockPadLockPresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", AboutLockPadLockPresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", AboutLockPadLockPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", AboutLockPadLockPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockService);
        set2.add(this.mScheduler);
        set2.add(this.mEventBus);
        set2.add(this.supertype);
    }

    public void injectMembers(AboutLockPadLockPresenter aboutLockPadLockPresenter) {
        aboutLockPadLockPresenter.mLockService = (LockService) this.mLockService.get();
        aboutLockPadLockPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        aboutLockPadLockPresenter.mEventBus = (Bus) this.mEventBus.get();
        this.supertype.injectMembers(aboutLockPadLockPresenter);
    }
}
