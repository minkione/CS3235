package com.masterlock.ble.app.presenter.lock.keysafe;

import android.content.ContentResolver;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class UnlockShacklePresenter$$InjectAdapter extends Binding<UnlockShacklePresenter> implements MembersInjector<UnlockShacklePresenter> {
    private Binding<ContentResolver> mContentResolver;
    private Binding<Bus> mEventBus;
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<AuthenticatedPresenter> supertype;

    public UnlockShacklePresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.lock.keysafe.UnlockShacklePresenter", false, UnlockShacklePresenter.class);
    }

    public void attach(Linker linker) {
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", UnlockShacklePresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", UnlockShacklePresenter.class, getClass().getClassLoader());
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", UnlockShacklePresenter.class, getClass().getClassLoader());
        this.mContentResolver = linker.requestBinding("android.content.ContentResolver", UnlockShacklePresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", UnlockShacklePresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mScheduler);
        set2.add(this.mEventBus);
        set2.add(this.mLockService);
        set2.add(this.mContentResolver);
        set2.add(this.supertype);
    }

    public void injectMembers(UnlockShacklePresenter unlockShacklePresenter) {
        unlockShacklePresenter.mScheduler = (IScheduler) this.mScheduler.get();
        unlockShacklePresenter.mEventBus = (Bus) this.mEventBus.get();
        unlockShacklePresenter.mLockService = (LockService) this.mLockService.get();
        unlockShacklePresenter.mContentResolver = (ContentResolver) this.mContentResolver.get();
        this.supertype.injectMembers(unlockShacklePresenter);
    }
}
