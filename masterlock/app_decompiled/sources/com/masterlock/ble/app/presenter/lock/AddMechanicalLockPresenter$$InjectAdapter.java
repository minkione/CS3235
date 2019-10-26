package com.masterlock.ble.app.presenter.lock;

import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class AddMechanicalLockPresenter$$InjectAdapter extends Binding<AddMechanicalLockPresenter> implements MembersInjector<AddMechanicalLockPresenter> {
    private Binding<Bus> mEventBus;
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<Presenter> supertype;

    public AddMechanicalLockPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.lock.AddMechanicalLockPresenter", false, AddMechanicalLockPresenter.class);
    }

    public void attach(Linker linker) {
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", AddMechanicalLockPresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", AddMechanicalLockPresenter.class, getClass().getClassLoader());
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", AddMechanicalLockPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.Presenter", AddMechanicalLockPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mScheduler);
        set2.add(this.mEventBus);
        set2.add(this.mLockService);
        set2.add(this.supertype);
    }

    public void injectMembers(AddMechanicalLockPresenter addMechanicalLockPresenter) {
        addMechanicalLockPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        addMechanicalLockPresenter.mEventBus = (Bus) this.mEventBus.get();
        addMechanicalLockPresenter.mLockService = (LockService) this.mLockService.get();
        this.supertype.injectMembers(addMechanicalLockPresenter);
    }
}
