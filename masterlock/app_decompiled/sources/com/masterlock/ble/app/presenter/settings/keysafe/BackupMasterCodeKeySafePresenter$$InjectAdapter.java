package com.masterlock.ble.app.presenter.settings.keysafe;

import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.KMSDeviceService;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class BackupMasterCodeKeySafePresenter$$InjectAdapter extends Binding<BackupMasterCodeKeySafePresenter> implements MembersInjector<BackupMasterCodeKeySafePresenter> {
    private Binding<Bus> mEventBus;
    private Binding<KMSDeviceService> mKMSDeviceService;
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<AuthenticatedPresenter> supertype;

    public BackupMasterCodeKeySafePresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.settings.keysafe.BackupMasterCodeKeySafePresenter", false, BackupMasterCodeKeySafePresenter.class);
    }

    public void attach(Linker linker) {
        this.mKMSDeviceService = linker.requestBinding("com.masterlock.ble.app.service.KMSDeviceService", BackupMasterCodeKeySafePresenter.class, getClass().getClassLoader());
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", BackupMasterCodeKeySafePresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", BackupMasterCodeKeySafePresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", BackupMasterCodeKeySafePresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", BackupMasterCodeKeySafePresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mKMSDeviceService);
        set2.add(this.mLockService);
        set2.add(this.mEventBus);
        set2.add(this.mScheduler);
        set2.add(this.supertype);
    }

    public void injectMembers(BackupMasterCodeKeySafePresenter backupMasterCodeKeySafePresenter) {
        backupMasterCodeKeySafePresenter.mKMSDeviceService = (KMSDeviceService) this.mKMSDeviceService.get();
        backupMasterCodeKeySafePresenter.mLockService = (LockService) this.mLockService.get();
        backupMasterCodeKeySafePresenter.mEventBus = (Bus) this.mEventBus.get();
        backupMasterCodeKeySafePresenter.mScheduler = (IScheduler) this.mScheduler.get();
        this.supertype.injectMembers(backupMasterCodeKeySafePresenter);
    }
}
