package com.masterlock.ble.app.presenter.settings.padlock;

import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.KMSDeviceService;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class BackupMasterCodePadLockPresenter$$InjectAdapter extends Binding<BackupMasterCodePadLockPresenter> implements MembersInjector<BackupMasterCodePadLockPresenter> {
    private Binding<Bus> mEventBus;
    private Binding<KMSDeviceService> mKMSDeviceService;
    private Binding<IScheduler> mScheduler;
    private Binding<AuthenticatedPresenter> supertype;

    public BackupMasterCodePadLockPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.settings.padlock.BackupMasterCodePadLockPresenter", false, BackupMasterCodePadLockPresenter.class);
    }

    public void attach(Linker linker) {
        this.mKMSDeviceService = linker.requestBinding("com.masterlock.ble.app.service.KMSDeviceService", BackupMasterCodePadLockPresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", BackupMasterCodePadLockPresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", BackupMasterCodePadLockPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", BackupMasterCodePadLockPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mKMSDeviceService);
        set2.add(this.mEventBus);
        set2.add(this.mScheduler);
        set2.add(this.supertype);
    }

    public void injectMembers(BackupMasterCodePadLockPresenter backupMasterCodePadLockPresenter) {
        backupMasterCodePadLockPresenter.mKMSDeviceService = (KMSDeviceService) this.mKMSDeviceService.get();
        backupMasterCodePadLockPresenter.mEventBus = (Bus) this.mEventBus.get();
        backupMasterCodePadLockPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        this.supertype.injectMembers(backupMasterCodePadLockPresenter);
    }
}
