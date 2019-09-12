package com.masterlock.ble.app.presenter.settings.keysafe;

import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class AboutLockKeySafePresenter$$InjectAdapter extends Binding<AboutLockKeySafePresenter> implements MembersInjector<AboutLockKeySafePresenter> {
    private Binding<Bus> mEventBus;
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<AuthenticatedPresenter> supertype;

    public AboutLockKeySafePresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.settings.keysafe.AboutLockKeySafePresenter", false, AboutLockKeySafePresenter.class);
    }

    public void attach(Linker linker) {
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", AboutLockKeySafePresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", AboutLockKeySafePresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", AboutLockKeySafePresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", AboutLockKeySafePresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockService);
        set2.add(this.mScheduler);
        set2.add(this.mEventBus);
        set2.add(this.supertype);
    }

    public void injectMembers(AboutLockKeySafePresenter aboutLockKeySafePresenter) {
        aboutLockKeySafePresenter.mLockService = (LockService) this.mLockService.get();
        aboutLockKeySafePresenter.mScheduler = (IScheduler) this.mScheduler.get();
        aboutLockKeySafePresenter.mEventBus = (Bus) this.mEventBus.get();
        this.supertype.injectMembers(aboutLockKeySafePresenter);
    }
}
