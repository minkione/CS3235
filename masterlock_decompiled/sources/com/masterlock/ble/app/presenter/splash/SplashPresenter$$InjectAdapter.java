package com.masterlock.ble.app.presenter.splash;

import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.TermsOfServiceService;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class SplashPresenter$$InjectAdapter extends Binding<SplashPresenter> implements MembersInjector<SplashPresenter> {
    private Binding<Bus> mEventBus;
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<TermsOfServiceService> mTermsOfServiceService;
    private Binding<Presenter> supertype;

    public SplashPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.splash.SplashPresenter", false, SplashPresenter.class);
    }

    public void attach(Linker linker) {
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", SplashPresenter.class, getClass().getClassLoader());
        this.mTermsOfServiceService = linker.requestBinding("com.masterlock.ble.app.service.TermsOfServiceService", SplashPresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", SplashPresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", SplashPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.Presenter", SplashPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockService);
        set2.add(this.mTermsOfServiceService);
        set2.add(this.mScheduler);
        set2.add(this.mEventBus);
        set2.add(this.supertype);
    }

    public void injectMembers(SplashPresenter splashPresenter) {
        splashPresenter.mLockService = (LockService) this.mLockService.get();
        splashPresenter.mTermsOfServiceService = (TermsOfServiceService) this.mTermsOfServiceService.get();
        splashPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        splashPresenter.mEventBus = (Bus) this.mEventBus.get();
        this.supertype.injectMembers(splashPresenter);
    }
}
