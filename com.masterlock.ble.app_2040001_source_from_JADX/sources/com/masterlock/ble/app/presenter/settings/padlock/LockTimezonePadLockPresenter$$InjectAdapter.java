package com.masterlock.ble.app.presenter.settings.padlock;

import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.TimezoneService;
import com.masterlock.ble.app.tape.UploadTaskQueue;
import com.masterlock.ble.app.util.IScheduler;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class LockTimezonePadLockPresenter$$InjectAdapter extends Binding<LockTimezonePadLockPresenter> implements MembersInjector<LockTimezonePadLockPresenter> {
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<UploadTaskQueue> mUploadTaskQueue;
    private Binding<AuthenticatedPresenter> supertype;
    private Binding<TimezoneService> timezoneService;

    public LockTimezonePadLockPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.settings.padlock.LockTimezonePadLockPresenter", false, LockTimezonePadLockPresenter.class);
    }

    public void attach(Linker linker) {
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", LockTimezonePadLockPresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", LockTimezonePadLockPresenter.class, getClass().getClassLoader());
        this.mUploadTaskQueue = linker.requestBinding("com.masterlock.ble.app.tape.UploadTaskQueue", LockTimezonePadLockPresenter.class, getClass().getClassLoader());
        this.timezoneService = linker.requestBinding("com.masterlock.ble.app.service.TimezoneService", LockTimezonePadLockPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", LockTimezonePadLockPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockService);
        set2.add(this.mScheduler);
        set2.add(this.mUploadTaskQueue);
        set2.add(this.timezoneService);
        set2.add(this.supertype);
    }

    public void injectMembers(LockTimezonePadLockPresenter lockTimezonePadLockPresenter) {
        lockTimezonePadLockPresenter.mLockService = (LockService) this.mLockService.get();
        lockTimezonePadLockPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        lockTimezonePadLockPresenter.mUploadTaskQueue = (UploadTaskQueue) this.mUploadTaskQueue.get();
        lockTimezonePadLockPresenter.timezoneService = (TimezoneService) this.timezoneService.get();
        this.supertype.injectMembers(lockTimezonePadLockPresenter);
    }
}
