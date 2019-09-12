package com.masterlock.ble.app.view.modal;

import com.masterlock.ble.app.util.IScheduler;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class CountriesModal$$InjectAdapter extends Binding<CountriesModal> implements MembersInjector<CountriesModal> {
    private Binding<IScheduler> mScheduler;

    public CountriesModal$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.view.modal.CountriesModal", false, CountriesModal.class);
    }

    public void attach(Linker linker) {
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", CountriesModal.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mScheduler);
    }

    public void injectMembers(CountriesModal countriesModal) {
        countriesModal.mScheduler = (IScheduler) this.mScheduler.get();
    }
}
