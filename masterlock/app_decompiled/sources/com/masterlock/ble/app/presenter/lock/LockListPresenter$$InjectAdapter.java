package com.masterlock.ble.app.presenter.lock;

import android.content.ContentResolver;
import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.service.KMSDeviceLogService;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.SignInService;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class LockListPresenter$$InjectAdapter extends Binding<LockListPresenter> implements MembersInjector<LockListPresenter> {
    private Binding<ContentResolver> mContentResolver;
    private Binding<Bus> mEventBus;
    private Binding<KMSDeviceLogService> mKmsDeviceLogService;
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<SignInService> mSignUpService;
    private Binding<Presenter> supertype;

    public LockListPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.lock.LockListPresenter", false, LockListPresenter.class);
    }

    public void attach(Linker linker) {
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", LockListPresenter.class, getClass().getClassLoader());
        this.mSignUpService = linker.requestBinding("com.masterlock.ble.app.service.SignInService", LockListPresenter.class, getClass().getClassLoader());
        this.mKmsDeviceLogService = linker.requestBinding("com.masterlock.ble.app.service.KMSDeviceLogService", LockListPresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", LockListPresenter.class, getClass().getClassLoader());
        this.mContentResolver = linker.requestBinding("android.content.ContentResolver", LockListPresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", LockListPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.Presenter", LockListPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockService);
        set2.add(this.mSignUpService);
        set2.add(this.mKmsDeviceLogService);
        set2.add(this.mEventBus);
        set2.add(this.mContentResolver);
        set2.add(this.mScheduler);
        set2.add(this.supertype);
    }

    public void injectMembers(LockListPresenter lockListPresenter) {
        lockListPresenter.mLockService = (LockService) this.mLockService.get();
        lockListPresenter.mSignUpService = (SignInService) this.mSignUpService.get();
        lockListPresenter.mKmsDeviceLogService = (KMSDeviceLogService) this.mKmsDeviceLogService.get();
        lockListPresenter.mEventBus = (Bus) this.mEventBus.get();
        lockListPresenter.mContentResolver = (ContentResolver) this.mContentResolver.get();
        lockListPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        this.supertype.injectMembers(lockListPresenter);
    }
}
