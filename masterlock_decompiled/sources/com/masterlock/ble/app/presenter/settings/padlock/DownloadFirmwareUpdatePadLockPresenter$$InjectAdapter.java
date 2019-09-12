package com.masterlock.ble.app.presenter.settings.padlock;

import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class DownloadFirmwareUpdatePadLockPresenter$$InjectAdapter extends Binding<DownloadFirmwareUpdatePadLockPresenter> implements MembersInjector<DownloadFirmwareUpdatePadLockPresenter> {
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<AuthenticatedPresenter> supertype;

    public DownloadFirmwareUpdatePadLockPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.settings.padlock.DownloadFirmwareUpdatePadLockPresenter", false, DownloadFirmwareUpdatePadLockPresenter.class);
    }

    public void attach(Linker linker) {
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", DownloadFirmwareUpdatePadLockPresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", DownloadFirmwareUpdatePadLockPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", DownloadFirmwareUpdatePadLockPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockService);
        set2.add(this.mScheduler);
        set2.add(this.supertype);
    }

    public void injectMembers(DownloadFirmwareUpdatePadLockPresenter downloadFirmwareUpdatePadLockPresenter) {
        downloadFirmwareUpdatePadLockPresenter.mLockService = (LockService) this.mLockService.get();
        downloadFirmwareUpdatePadLockPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        this.supertype.injectMembers(downloadFirmwareUpdatePadLockPresenter);
    }
}
