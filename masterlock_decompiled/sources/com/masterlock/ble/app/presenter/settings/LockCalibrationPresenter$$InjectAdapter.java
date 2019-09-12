package com.masterlock.ble.app.presenter.settings;

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

public final class LockCalibrationPresenter$$InjectAdapter extends Binding<LockCalibrationPresenter> implements MembersInjector<LockCalibrationPresenter> {
    private Binding<ConfirmTaskQueue> mConfirmTaskQueue;
    private Binding<Bus> mEventBus;
    private Binding<KMSDeviceService> mKmsDeviceService;
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<AuthenticatedPresenter> supertype;

    public LockCalibrationPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.settings.LockCalibrationPresenter", false, LockCalibrationPresenter.class);
    }

    public void attach(Linker linker) {
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", LockCalibrationPresenter.class, getClass().getClassLoader());
        this.mKmsDeviceService = linker.requestBinding("com.masterlock.ble.app.service.KMSDeviceService", LockCalibrationPresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", LockCalibrationPresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", LockCalibrationPresenter.class, getClass().getClassLoader());
        this.mConfirmTaskQueue = linker.requestBinding("com.masterlock.ble.app.tape.ConfirmTaskQueue", LockCalibrationPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", LockCalibrationPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockService);
        set2.add(this.mKmsDeviceService);
        set2.add(this.mScheduler);
        set2.add(this.mEventBus);
        set2.add(this.mConfirmTaskQueue);
        set2.add(this.supertype);
    }

    public void injectMembers(LockCalibrationPresenter lockCalibrationPresenter) {
        lockCalibrationPresenter.mLockService = (LockService) this.mLockService.get();
        lockCalibrationPresenter.mKmsDeviceService = (KMSDeviceService) this.mKmsDeviceService.get();
        lockCalibrationPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        lockCalibrationPresenter.mEventBus = (Bus) this.mEventBus.get();
        lockCalibrationPresenter.mConfirmTaskQueue = (ConfirmTaskQueue) this.mConfirmTaskQueue.get();
        this.supertype.injectMembers(lockCalibrationPresenter);
    }
}
