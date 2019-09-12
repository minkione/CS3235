package com.masterlock.ble.app.presenter.lock;

import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.KMSDeviceService;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.tape.ConfirmTaskQueue;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class ApplyChangesPresenter$$InjectAdapter extends Binding<ApplyChangesPresenter> implements MembersInjector<ApplyChangesPresenter> {
    private Binding<ConfirmTaskQueue> mConfirmTaskQueue;
    private Binding<Bus> mEventBus;
    private Binding<KMSDeviceService> mKmsDeviceService;
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<AuthenticatedPresenter> supertype;

    public ApplyChangesPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.lock.ApplyChangesPresenter", false, ApplyChangesPresenter.class);
    }

    public void attach(Linker linker) {
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", ApplyChangesPresenter.class, getClass().getClassLoader());
        this.mKmsDeviceService = linker.requestBinding("com.masterlock.ble.app.service.KMSDeviceService", ApplyChangesPresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", ApplyChangesPresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", ApplyChangesPresenter.class, getClass().getClassLoader());
        this.mConfirmTaskQueue = linker.requestBinding("com.masterlock.ble.app.tape.ConfirmTaskQueue", ApplyChangesPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", ApplyChangesPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockService);
        set2.add(this.mKmsDeviceService);
        set2.add(this.mScheduler);
        set2.add(this.mEventBus);
        set2.add(this.mConfirmTaskQueue);
        set2.add(this.supertype);
    }

    public void injectMembers(ApplyChangesPresenter applyChangesPresenter) {
        applyChangesPresenter.mLockService = (LockService) this.mLockService.get();
        applyChangesPresenter.mKmsDeviceService = (KMSDeviceService) this.mKmsDeviceService.get();
        applyChangesPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        applyChangesPresenter.mEventBus = (Bus) this.mEventBus.get();
        applyChangesPresenter.mConfirmTaskQueue = (ConfirmTaskQueue) this.mConfirmTaskQueue.get();
        this.supertype.injectMembers(applyChangesPresenter);
    }
}
