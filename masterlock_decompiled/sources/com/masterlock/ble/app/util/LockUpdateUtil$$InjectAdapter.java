package com.masterlock.ble.app.util;

import com.masterlock.ble.app.service.LockService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

public final class LockUpdateUtil$$InjectAdapter extends Binding<LockUpdateUtil> implements Provider<LockUpdateUtil>, MembersInjector<LockUpdateUtil> {
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;

    public LockUpdateUtil$$InjectAdapter() {
        super("com.masterlock.ble.app.util.LockUpdateUtil", "members/com.masterlock.ble.app.util.LockUpdateUtil", false, LockUpdateUtil.class);
    }

    public void attach(Linker linker) {
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", LockUpdateUtil.class, getClass().getClassLoader());
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", LockUpdateUtil.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mScheduler);
        set2.add(this.mLockService);
    }

    public LockUpdateUtil get() {
        LockUpdateUtil lockUpdateUtil = new LockUpdateUtil();
        injectMembers(lockUpdateUtil);
        return lockUpdateUtil;
    }

    public void injectMembers(LockUpdateUtil lockUpdateUtil) {
        lockUpdateUtil.mScheduler = (IScheduler) this.mScheduler.get();
        lockUpdateUtil.mLockService = (LockService) this.mLockService.get();
    }
}
