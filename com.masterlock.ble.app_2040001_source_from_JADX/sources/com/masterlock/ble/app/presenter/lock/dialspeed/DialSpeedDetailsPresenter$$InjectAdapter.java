package com.masterlock.ble.app.presenter.lock.dialspeed;

import android.content.ContentResolver;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class DialSpeedDetailsPresenter$$InjectAdapter extends Binding<DialSpeedDetailsPresenter> implements MembersInjector<DialSpeedDetailsPresenter> {
    private Binding<Bus> mBus;
    private Binding<ContentResolver> mContentResolver;
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<AuthenticatedPresenter> supertype;

    public DialSpeedDetailsPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.lock.dialspeed.DialSpeedDetailsPresenter", false, DialSpeedDetailsPresenter.class);
    }

    public void attach(Linker linker) {
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", DialSpeedDetailsPresenter.class, getClass().getClassLoader());
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", DialSpeedDetailsPresenter.class, getClass().getClassLoader());
        this.mBus = linker.requestBinding("com.squareup.otto.Bus", DialSpeedDetailsPresenter.class, getClass().getClassLoader());
        this.mContentResolver = linker.requestBinding("android.content.ContentResolver", DialSpeedDetailsPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", DialSpeedDetailsPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mScheduler);
        set2.add(this.mLockService);
        set2.add(this.mBus);
        set2.add(this.mContentResolver);
        set2.add(this.supertype);
    }

    public void injectMembers(DialSpeedDetailsPresenter dialSpeedDetailsPresenter) {
        dialSpeedDetailsPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        dialSpeedDetailsPresenter.mLockService = (LockService) this.mLockService.get();
        dialSpeedDetailsPresenter.mBus = (Bus) this.mBus.get();
        dialSpeedDetailsPresenter.mContentResolver = (ContentResolver) this.mContentResolver.get();
        this.supertype.injectMembers(dialSpeedDetailsPresenter);
    }
}
