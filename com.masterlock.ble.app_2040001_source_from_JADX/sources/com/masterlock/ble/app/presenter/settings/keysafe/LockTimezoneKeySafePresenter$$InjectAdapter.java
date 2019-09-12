package com.masterlock.ble.app.presenter.settings.keysafe;

import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.TimezoneService;
import com.masterlock.ble.app.tape.UploadTaskQueue;
import com.masterlock.ble.app.util.IScheduler;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class LockTimezoneKeySafePresenter$$InjectAdapter extends Binding<LockTimezoneKeySafePresenter> implements MembersInjector<LockTimezoneKeySafePresenter> {
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<UploadTaskQueue> mUploadTaskQueue;
    private Binding<AuthenticatedPresenter> supertype;
    private Binding<TimezoneService> timezoneService;

    public LockTimezoneKeySafePresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.settings.keysafe.LockTimezoneKeySafePresenter", false, LockTimezoneKeySafePresenter.class);
    }

    public void attach(Linker linker) {
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", LockTimezoneKeySafePresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", LockTimezoneKeySafePresenter.class, getClass().getClassLoader());
        this.mUploadTaskQueue = linker.requestBinding("com.masterlock.ble.app.tape.UploadTaskQueue", LockTimezoneKeySafePresenter.class, getClass().getClassLoader());
        this.timezoneService = linker.requestBinding("com.masterlock.ble.app.service.TimezoneService", LockTimezoneKeySafePresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", LockTimezoneKeySafePresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockService);
        set2.add(this.mScheduler);
        set2.add(this.mUploadTaskQueue);
        set2.add(this.timezoneService);
        set2.add(this.supertype);
    }

    public void injectMembers(LockTimezoneKeySafePresenter lockTimezoneKeySafePresenter) {
        lockTimezoneKeySafePresenter.mLockService = (LockService) this.mLockService.get();
        lockTimezoneKeySafePresenter.mScheduler = (IScheduler) this.mScheduler.get();
        lockTimezoneKeySafePresenter.mUploadTaskQueue = (UploadTaskQueue) this.mUploadTaskQueue.get();
        lockTimezoneKeySafePresenter.timezoneService = (TimezoneService) this.timezoneService.get();
        this.supertype.injectMembers(lockTimezoneKeySafePresenter);
    }
}
