package com.masterlock.ble.app.presenter.lock.padlock;

import android.content.ContentResolver;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.KMSDeviceLogService;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class HistoryPadLockPresenter$$InjectAdapter extends Binding<HistoryPadLockPresenter> implements MembersInjector<HistoryPadLockPresenter> {
    private Binding<ContentResolver> mContentResolver;
    private Binding<Bus> mEventBus;
    private Binding<LockService> mLockService;
    private Binding<KMSDeviceLogService> mLogService;
    private Binding<IScheduler> mScheduler;
    private Binding<AuthenticatedPresenter> supertype;

    public HistoryPadLockPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.lock.padlock.HistoryPadLockPresenter", false, HistoryPadLockPresenter.class);
    }

    public void attach(Linker linker) {
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", HistoryPadLockPresenter.class, getClass().getClassLoader());
        this.mLogService = linker.requestBinding("com.masterlock.ble.app.service.KMSDeviceLogService", HistoryPadLockPresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", HistoryPadLockPresenter.class, getClass().getClassLoader());
        this.mContentResolver = linker.requestBinding("android.content.ContentResolver", HistoryPadLockPresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", HistoryPadLockPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", HistoryPadLockPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockService);
        set2.add(this.mLogService);
        set2.add(this.mEventBus);
        set2.add(this.mContentResolver);
        set2.add(this.mScheduler);
        set2.add(this.supertype);
    }

    public void injectMembers(HistoryPadLockPresenter historyPadLockPresenter) {
        historyPadLockPresenter.mLockService = (LockService) this.mLockService.get();
        historyPadLockPresenter.mLogService = (KMSDeviceLogService) this.mLogService.get();
        historyPadLockPresenter.mEventBus = (Bus) this.mEventBus.get();
        historyPadLockPresenter.mContentResolver = (ContentResolver) this.mContentResolver.get();
        historyPadLockPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        this.supertype.injectMembers(historyPadLockPresenter);
    }
}
