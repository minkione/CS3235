package com.masterlock.ble.app.presenter.lock.keysafe;

import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.tape.UploadTaskQueue;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class LastLocationInfoKeySafePresenter$$InjectAdapter extends Binding<LastLocationInfoKeySafePresenter> implements MembersInjector<LastLocationInfoKeySafePresenter> {
    private Binding<Bus> mEventBus;
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<UploadTaskQueue> mUploadTaskQueue;
    private Binding<AuthenticatedPresenter> supertype;

    public LastLocationInfoKeySafePresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.lock.keysafe.LastLocationInfoKeySafePresenter", false, LastLocationInfoKeySafePresenter.class);
    }

    public void attach(Linker linker) {
        this.mUploadTaskQueue = linker.requestBinding("com.masterlock.ble.app.tape.UploadTaskQueue", LastLocationInfoKeySafePresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", LastLocationInfoKeySafePresenter.class, getClass().getClassLoader());
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", LastLocationInfoKeySafePresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", LastLocationInfoKeySafePresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", LastLocationInfoKeySafePresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mUploadTaskQueue);
        set2.add(this.mScheduler);
        set2.add(this.mLockService);
        set2.add(this.mEventBus);
        set2.add(this.supertype);
    }

    public void injectMembers(LastLocationInfoKeySafePresenter lastLocationInfoKeySafePresenter) {
        lastLocationInfoKeySafePresenter.mUploadTaskQueue = (UploadTaskQueue) this.mUploadTaskQueue.get();
        lastLocationInfoKeySafePresenter.mScheduler = (IScheduler) this.mScheduler.get();
        lastLocationInfoKeySafePresenter.mLockService = (LockService) this.mLockService.get();
        lastLocationInfoKeySafePresenter.mEventBus = (Bus) this.mEventBus.get();
        this.supertype.injectMembers(lastLocationInfoKeySafePresenter);
    }
}
