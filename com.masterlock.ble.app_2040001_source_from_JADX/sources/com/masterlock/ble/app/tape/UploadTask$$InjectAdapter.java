package com.masterlock.ble.app.tape;

import com.masterlock.ble.app.service.KMSDeviceLogService;
import com.masterlock.ble.app.service.KMSDeviceService;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class UploadTask$$InjectAdapter extends Binding<UploadTask> implements MembersInjector<UploadTask> {
    private Binding<KMSDeviceLogService> mKMSDeviceLogService;
    private Binding<KMSDeviceService> mKMSDeviceService;
    private Binding<KMSDeviceService> mKmsDeviceService;
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;

    public UploadTask$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.tape.UploadTask", false, UploadTask.class);
    }

    public void attach(Linker linker) {
        this.mKMSDeviceService = linker.requestBinding("com.masterlock.ble.app.service.KMSDeviceService", UploadTask.class, getClass().getClassLoader());
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", UploadTask.class, getClass().getClassLoader());
        this.mKMSDeviceLogService = linker.requestBinding("com.masterlock.ble.app.service.KMSDeviceLogService", UploadTask.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", UploadTask.class, getClass().getClassLoader());
        this.mKmsDeviceService = linker.requestBinding("com.masterlock.ble.app.service.KMSDeviceService", UploadTask.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mKMSDeviceService);
        set2.add(this.mLockService);
        set2.add(this.mKMSDeviceLogService);
        set2.add(this.mScheduler);
        set2.add(this.mKmsDeviceService);
    }

    public void injectMembers(UploadTask uploadTask) {
        uploadTask.mKMSDeviceService = (KMSDeviceService) this.mKMSDeviceService.get();
        uploadTask.mLockService = (LockService) this.mLockService.get();
        uploadTask.mKMSDeviceLogService = (KMSDeviceLogService) this.mKMSDeviceLogService.get();
        uploadTask.mScheduler = (IScheduler) this.mScheduler.get();
        uploadTask.mKmsDeviceService = (KMSDeviceService) this.mKmsDeviceService.get();
    }
}
