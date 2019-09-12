package com.masterlock.ble.app.view.modal;

import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class AddGuestModal$$InjectAdapter extends Binding<AddGuestModal> implements MembersInjector<AddGuestModal> {
    private Binding<Bus> mEventBus;

    public AddGuestModal$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.view.modal.AddGuestModal", false, AddGuestModal.class);
    }

    public void attach(Linker linker) {
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", AddGuestModal.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mEventBus);
    }

    public void injectMembers(AddGuestModal addGuestModal) {
        addGuestModal.mEventBus = (Bus) this.mEventBus.get();
    }
}
