package com.masterlock.ble.app.activity;

import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class FlowActivity$$InjectAdapter extends Binding<FlowActivity> implements MembersInjector<FlowActivity> {
    private Binding<Bus> mEventBus;

    public FlowActivity$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.activity.FlowActivity", false, FlowActivity.class);
    }

    public void attach(Linker linker) {
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", FlowActivity.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mEventBus);
    }

    public void injectMembers(FlowActivity flowActivity) {
        flowActivity.mEventBus = (Bus) this.mEventBus.get();
    }
}
