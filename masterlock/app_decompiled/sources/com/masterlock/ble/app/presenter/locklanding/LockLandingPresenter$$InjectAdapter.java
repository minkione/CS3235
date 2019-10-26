package com.masterlock.ble.app.presenter.locklanding;

import android.content.ContentResolver;
import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.SignInService;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class LockLandingPresenter$$InjectAdapter extends Binding<LockLandingPresenter> implements MembersInjector<LockLandingPresenter> {
    private Binding<ContentResolver> mContentResolver;
    private Binding<Bus> mEventBus;
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<SignInService> mSignUpService;
    private Binding<Presenter> supertype;

    public LockLandingPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.locklanding.LockLandingPresenter", false, LockLandingPresenter.class);
    }

    public void attach(Linker linker) {
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", LockLandingPresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", LockLandingPresenter.class, getClass().getClassLoader());
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", LockLandingPresenter.class, getClass().getClassLoader());
        this.mSignUpService = linker.requestBinding("com.masterlock.ble.app.service.SignInService", LockLandingPresenter.class, getClass().getClassLoader());
        this.mContentResolver = linker.requestBinding("android.content.ContentResolver", LockLandingPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.Presenter", LockLandingPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mScheduler);
        set2.add(this.mEventBus);
        set2.add(this.mLockService);
        set2.add(this.mSignUpService);
        set2.add(this.mContentResolver);
        set2.add(this.supertype);
    }

    public void injectMembers(LockLandingPresenter lockLandingPresenter) {
        lockLandingPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        lockLandingPresenter.mEventBus = (Bus) this.mEventBus.get();
        lockLandingPresenter.mLockService = (LockService) this.mLockService.get();
        lockLandingPresenter.mSignUpService = (SignInService) this.mSignUpService.get();
        lockLandingPresenter.mContentResolver = (ContentResolver) this.mContentResolver.get();
        this.supertype.injectMembers(lockLandingPresenter);
    }
}
