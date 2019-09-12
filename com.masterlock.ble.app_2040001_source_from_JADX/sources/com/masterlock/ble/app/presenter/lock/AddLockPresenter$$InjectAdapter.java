package com.masterlock.ble.app.presenter.lock;

import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.SignInService;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class AddLockPresenter$$InjectAdapter extends Binding<AddLockPresenter> implements MembersInjector<AddLockPresenter> {
    private Binding<Bus> mEventBus;
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<SignInService> mSignUpService;
    private Binding<Presenter> supertype;

    public AddLockPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.lock.AddLockPresenter", false, AddLockPresenter.class);
    }

    public void attach(Linker linker) {
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", AddLockPresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", AddLockPresenter.class, getClass().getClassLoader());
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", AddLockPresenter.class, getClass().getClassLoader());
        this.mSignUpService = linker.requestBinding("com.masterlock.ble.app.service.SignInService", AddLockPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.Presenter", AddLockPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mScheduler);
        set2.add(this.mEventBus);
        set2.add(this.mLockService);
        set2.add(this.mSignUpService);
        set2.add(this.supertype);
    }

    public void injectMembers(AddLockPresenter addLockPresenter) {
        addLockPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        addLockPresenter.mEventBus = (Bus) this.mEventBus.get();
        addLockPresenter.mLockService = (LockService) this.mLockService.get();
        addLockPresenter.mSignUpService = (SignInService) this.mSignUpService.get();
        this.supertype.injectMembers(addLockPresenter);
    }
}
