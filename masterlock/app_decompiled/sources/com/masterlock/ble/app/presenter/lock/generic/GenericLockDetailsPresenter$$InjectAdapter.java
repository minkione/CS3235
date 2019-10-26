package com.masterlock.ble.app.presenter.lock.generic;

import android.content.ContentResolver;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class GenericLockDetailsPresenter$$InjectAdapter extends Binding<GenericLockDetailsPresenter> implements MembersInjector<GenericLockDetailsPresenter> {
    private Binding<ContentResolver> mContentResolver;
    private Binding<Bus> mEventBus;
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<AuthenticatedPresenter> supertype;

    public GenericLockDetailsPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.lock.generic.GenericLockDetailsPresenter", false, GenericLockDetailsPresenter.class);
    }

    public void attach(Linker linker) {
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", GenericLockDetailsPresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", GenericLockDetailsPresenter.class, getClass().getClassLoader());
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", GenericLockDetailsPresenter.class, getClass().getClassLoader());
        this.mContentResolver = linker.requestBinding("android.content.ContentResolver", GenericLockDetailsPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", GenericLockDetailsPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mScheduler);
        set2.add(this.mEventBus);
        set2.add(this.mLockService);
        set2.add(this.mContentResolver);
        set2.add(this.supertype);
    }

    public void injectMembers(GenericLockDetailsPresenter genericLockDetailsPresenter) {
        genericLockDetailsPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        genericLockDetailsPresenter.mEventBus = (Bus) this.mEventBus.get();
        genericLockDetailsPresenter.mLockService = (LockService) this.mLockService.get();
        genericLockDetailsPresenter.mContentResolver = (ContentResolver) this.mContentResolver.get();
        this.supertype.injectMembers(genericLockDetailsPresenter);
    }
}
