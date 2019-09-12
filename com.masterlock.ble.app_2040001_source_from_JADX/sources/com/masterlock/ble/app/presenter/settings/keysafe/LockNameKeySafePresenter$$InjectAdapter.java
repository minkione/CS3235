package com.masterlock.ble.app.presenter.settings.keysafe;

import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class LockNameKeySafePresenter$$InjectAdapter extends Binding<LockNameKeySafePresenter> implements MembersInjector<LockNameKeySafePresenter> {
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<AuthenticatedPresenter> supertype;

    public LockNameKeySafePresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.settings.keysafe.LockNameKeySafePresenter", false, LockNameKeySafePresenter.class);
    }

    public void attach(Linker linker) {
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", LockNameKeySafePresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", LockNameKeySafePresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", LockNameKeySafePresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockService);
        set2.add(this.mScheduler);
        set2.add(this.supertype);
    }

    public void injectMembers(LockNameKeySafePresenter lockNameKeySafePresenter) {
        lockNameKeySafePresenter.mLockService = (LockService) this.mLockService.get();
        lockNameKeySafePresenter.mScheduler = (IScheduler) this.mScheduler.get();
        this.supertype.injectMembers(lockNameKeySafePresenter);
    }
}
