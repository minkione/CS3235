package com.masterlock.ble.app.presenter.settings.keysafe;

import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class LockNotesKeySafePresenter$$InjectAdapter extends Binding<LockNotesKeySafePresenter> implements MembersInjector<LockNotesKeySafePresenter> {
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<AuthenticatedPresenter> supertype;

    public LockNotesKeySafePresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.settings.keysafe.LockNotesKeySafePresenter", false, LockNotesKeySafePresenter.class);
    }

    public void attach(Linker linker) {
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", LockNotesKeySafePresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", LockNotesKeySafePresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", LockNotesKeySafePresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockService);
        set2.add(this.mScheduler);
        set2.add(this.supertype);
    }

    public void injectMembers(LockNotesKeySafePresenter lockNotesKeySafePresenter) {
        lockNotesKeySafePresenter.mLockService = (LockService) this.mLockService.get();
        lockNotesKeySafePresenter.mScheduler = (IScheduler) this.mScheduler.get();
        this.supertype.injectMembers(lockNotesKeySafePresenter);
    }
}
