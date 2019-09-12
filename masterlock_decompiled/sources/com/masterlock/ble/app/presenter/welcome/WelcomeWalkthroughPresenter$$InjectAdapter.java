package com.masterlock.ble.app.presenter.welcome;

import com.masterlock.ble.app.presenter.Presenter;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class WelcomeWalkthroughPresenter$$InjectAdapter extends Binding<WelcomeWalkthroughPresenter> implements MembersInjector<WelcomeWalkthroughPresenter> {
    private Binding<Bus> mEventBus;
    private Binding<Presenter> supertype;

    public WelcomeWalkthroughPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.welcome.WelcomeWalkthroughPresenter", false, WelcomeWalkthroughPresenter.class);
    }

    public void attach(Linker linker) {
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", WelcomeWalkthroughPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.Presenter", WelcomeWalkthroughPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mEventBus);
        set2.add(this.supertype);
    }

    public void injectMembers(WelcomeWalkthroughPresenter welcomeWalkthroughPresenter) {
        welcomeWalkthroughPresenter.mEventBus = (Bus) this.mEventBus.get();
        this.supertype.injectMembers(welcomeWalkthroughPresenter);
    }
}
