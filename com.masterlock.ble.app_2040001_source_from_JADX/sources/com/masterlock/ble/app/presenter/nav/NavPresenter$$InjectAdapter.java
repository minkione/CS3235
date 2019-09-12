package com.masterlock.ble.app.presenter.nav;

import com.masterlock.ble.app.presenter.Presenter;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class NavPresenter$$InjectAdapter extends Binding<NavPresenter> implements MembersInjector<NavPresenter> {
    private Binding<Bus> mEventBus;
    private Binding<Presenter> supertype;

    public NavPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.nav.NavPresenter", false, NavPresenter.class);
    }

    public void attach(Linker linker) {
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", NavPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.Presenter", NavPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mEventBus);
        set2.add(this.supertype);
    }

    public void injectMembers(NavPresenter navPresenter) {
        navPresenter.mEventBus = (Bus) this.mEventBus.get();
        this.supertype.injectMembers(navPresenter);
    }
}
