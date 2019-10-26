package com.masterlock.ble.app.presenter.settings.keysafe;

import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class DownloadFirmwareUpdateKeySafePresenter$$InjectAdapter extends Binding<DownloadFirmwareUpdateKeySafePresenter> implements MembersInjector<DownloadFirmwareUpdateKeySafePresenter> {
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<AuthenticatedPresenter> supertype;

    public DownloadFirmwareUpdateKeySafePresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.settings.keysafe.DownloadFirmwareUpdateKeySafePresenter", false, DownloadFirmwareUpdateKeySafePresenter.class);
    }

    public void attach(Linker linker) {
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", DownloadFirmwareUpdateKeySafePresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", DownloadFirmwareUpdateKeySafePresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", DownloadFirmwareUpdateKeySafePresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockService);
        set2.add(this.mScheduler);
        set2.add(this.supertype);
    }

    public void injectMembers(DownloadFirmwareUpdateKeySafePresenter downloadFirmwareUpdateKeySafePresenter) {
        downloadFirmwareUpdateKeySafePresenter.mLockService = (LockService) this.mLockService.get();
        downloadFirmwareUpdateKeySafePresenter.mScheduler = (IScheduler) this.mScheduler.get();
        this.supertype.injectMembers(downloadFirmwareUpdateKeySafePresenter);
    }
}
