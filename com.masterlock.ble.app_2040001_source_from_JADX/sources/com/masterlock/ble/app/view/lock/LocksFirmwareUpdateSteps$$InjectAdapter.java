package com.masterlock.ble.app.view.lock;

import com.masterlock.ble.app.service.LockService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class LocksFirmwareUpdateSteps$$InjectAdapter extends Binding<LocksFirmwareUpdateSteps> implements MembersInjector<LocksFirmwareUpdateSteps> {
    private Binding<LockService> mLockService;

    public LocksFirmwareUpdateSteps$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.view.lock.LocksFirmwareUpdateSteps", false, LocksFirmwareUpdateSteps.class);
    }

    public void attach(Linker linker) {
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", LocksFirmwareUpdateSteps.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockService);
    }

    public void injectMembers(LocksFirmwareUpdateSteps locksFirmwareUpdateSteps) {
        locksFirmwareUpdateSteps.mLockService = (LockService) this.mLockService.get();
    }
}
