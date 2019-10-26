package com.masterlock.ble.app.presenter;

import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class AuthenticatedPresenter$$InjectAdapter extends Binding<AuthenticatedPresenter> implements MembersInjector<AuthenticatedPresenter> {
    private Binding<Bus> mEventBus;
    private Binding<Presenter> supertype;

    public AuthenticatedPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", false, AuthenticatedPresenter.class);
    }

    public void attach(Linker linker) {
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", AuthenticatedPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.Presenter", AuthenticatedPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mEventBus);
        set2.add(this.supertype);
    }

    public void injectMembers(AuthenticatedPresenter authenticatedPresenter) {
        authenticatedPresenter.mEventBus = (Bus) this.mEventBus.get();
        this.supertype.injectMembers(authenticatedPresenter);
    }
}
