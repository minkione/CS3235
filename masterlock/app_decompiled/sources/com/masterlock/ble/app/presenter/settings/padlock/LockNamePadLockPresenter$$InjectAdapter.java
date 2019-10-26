package com.masterlock.ble.app.presenter.settings.padlock;

import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class LockNamePadLockPresenter$$InjectAdapter extends Binding<LockNamePadLockPresenter> implements MembersInjector<LockNamePadLockPresenter> {
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<AuthenticatedPresenter> supertype;

    public LockNamePadLockPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.settings.padlock.LockNamePadLockPresenter", false, LockNamePadLockPresenter.class);
    }

    public void attach(Linker linker) {
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", LockNamePadLockPresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", LockNamePadLockPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", LockNamePadLockPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockService);
        set2.add(this.mScheduler);
        set2.add(this.supertype);
    }

    public void injectMembers(LockNamePadLockPresenter lockNamePadLockPresenter) {
        lockNamePadLockPresenter.mLockService = (LockService) this.mLockService.get();
        lockNamePadLockPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        this.supertype.injectMembers(lockNamePadLockPresenter);
    }
}
