package com.masterlock.ble.app.presenter.lock.dialspeed;

import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.tape.UploadTaskQueue;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class EditDialSpeedCodesPresenter$$InjectAdapter extends Binding<EditDialSpeedCodesPresenter> implements MembersInjector<EditDialSpeedCodesPresenter> {
    private Binding<Bus> mBus;
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<UploadTaskQueue> mUploadTaskQueue;
    private Binding<AuthenticatedPresenter> supertype;

    public EditDialSpeedCodesPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.lock.dialspeed.EditDialSpeedCodesPresenter", false, EditDialSpeedCodesPresenter.class);
    }

    public void attach(Linker linker) {
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", EditDialSpeedCodesPresenter.class, getClass().getClassLoader());
        this.mBus = linker.requestBinding("com.squareup.otto.Bus", EditDialSpeedCodesPresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", EditDialSpeedCodesPresenter.class, getClass().getClassLoader());
        this.mUploadTaskQueue = linker.requestBinding("com.masterlock.ble.app.tape.UploadTaskQueue", EditDialSpeedCodesPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", EditDialSpeedCodesPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockService);
        set2.add(this.mBus);
        set2.add(this.mScheduler);
        set2.add(this.mUploadTaskQueue);
        set2.add(this.supertype);
    }

    public void injectMembers(EditDialSpeedCodesPresenter editDialSpeedCodesPresenter) {
        editDialSpeedCodesPresenter.mLockService = (LockService) this.mLockService.get();
        editDialSpeedCodesPresenter.mBus = (Bus) this.mBus.get();
        editDialSpeedCodesPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        editDialSpeedCodesPresenter.mUploadTaskQueue = (UploadTaskQueue) this.mUploadTaskQueue.get();
        this.supertype.injectMembers(editDialSpeedCodesPresenter);
    }
}
