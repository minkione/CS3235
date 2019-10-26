package com.masterlock.ble.app.presenter.nav.settings;

import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class LanguageSelectionPresenter$$InjectAdapter extends Binding<LanguageSelectionPresenter> implements MembersInjector<LanguageSelectionPresenter> {
    private Binding<Bus> mEventBus;
    private Binding<IScheduler> mScheduler;
    private Binding<Presenter> supertype;

    public LanguageSelectionPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.nav.settings.LanguageSelectionPresenter", false, LanguageSelectionPresenter.class);
    }

    public void attach(Linker linker) {
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", LanguageSelectionPresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", LanguageSelectionPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.Presenter", LanguageSelectionPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mEventBus);
        set2.add(this.mScheduler);
        set2.add(this.supertype);
    }

    public void injectMembers(LanguageSelectionPresenter languageSelectionPresenter) {
        languageSelectionPresenter.mEventBus = (Bus) this.mEventBus.get();
        languageSelectionPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        this.supertype.injectMembers(languageSelectionPresenter);
    }
}
