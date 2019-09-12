package com.masterlock.ble.app.tape;

import com.masterlock.ble.app.service.KMSDeviceService;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class ConfirmTask$$InjectAdapter extends Binding<ConfirmTask> implements MembersInjector<ConfirmTask> {
    private Binding<KMSDeviceService> mKmsDeviceService;
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;

    public ConfirmTask$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.tape.ConfirmTask", false, ConfirmTask.class);
    }

    public void attach(Linker linker) {
        this.mKmsDeviceService = linker.requestBinding("com.masterlock.ble.app.service.KMSDeviceService", ConfirmTask.class, getClass().getClassLoader());
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", ConfirmTask.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", ConfirmTask.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mKmsDeviceService);
        set2.add(this.mLockService);
        set2.add(this.mScheduler);
    }

    public void injectMembers(ConfirmTask confirmTask) {
        confirmTask.mKmsDeviceService = (KMSDeviceService) this.mKmsDeviceService.get();
        confirmTask.mLockService = (LockService) this.mLockService.get();
        confirmTask.mScheduler = (IScheduler) this.mScheduler.get();
    }
}
