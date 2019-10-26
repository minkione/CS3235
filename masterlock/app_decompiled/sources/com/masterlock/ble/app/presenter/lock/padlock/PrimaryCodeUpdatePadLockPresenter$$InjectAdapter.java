package com.masterlock.ble.app.presenter.lock.padlock;

import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class PrimaryCodeUpdatePadLockPresenter$$InjectAdapter extends Binding<PrimaryCodeUpdatePadLockPresenter> implements MembersInjector<PrimaryCodeUpdatePadLockPresenter> {
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<AuthenticatedPresenter> supertype;

    public PrimaryCodeUpdatePadLockPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.lock.padlock.PrimaryCodeUpdatePadLockPresenter", false, PrimaryCodeUpdatePadLockPresenter.class);
    }

    public void attach(Linker linker) {
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", PrimaryCodeUpdatePadLockPresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", PrimaryCodeUpdatePadLockPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", PrimaryCodeUpdatePadLockPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockService);
        set2.add(this.mScheduler);
        set2.add(this.supertype);
    }

    public void injectMembers(PrimaryCodeUpdatePadLockPresenter primaryCodeUpdatePadLockPresenter) {
        primaryCodeUpdatePadLockPresenter.mLockService = (LockService) this.mLockService.get();
        primaryCodeUpdatePadLockPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        this.supertype.injectMembers(primaryCodeUpdatePadLockPresenter);
    }
}
