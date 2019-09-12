package com.masterlock.ble.app.adapters;

import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class LockAdapter$$InjectAdapter extends Binding<LockAdapter> implements MembersInjector<LockAdapter> {
    private Binding<Bus> mBus;

    public LockAdapter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.adapters.LockAdapter", false, LockAdapter.class);
    }

    public void attach(Linker linker) {
        this.mBus = linker.requestBinding("com.squareup.otto.Bus", LockAdapter.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mBus);
    }

    public void injectMembers(LockAdapter lockAdapter) {
        lockAdapter.mBus = (Bus) this.mBus.get();
    }
}
