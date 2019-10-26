package com.masterlock.ble.app.util;

import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

public final class PasscodeTimeoutTask$$InjectAdapter extends Binding<PasscodeTimeoutTask> implements Provider<PasscodeTimeoutTask>, MembersInjector<PasscodeTimeoutTask> {
    private Binding<Bus> mEventBus;

    public PasscodeTimeoutTask$$InjectAdapter() {
        super("com.masterlock.ble.app.util.PasscodeTimeoutTask", "members/com.masterlock.ble.app.util.PasscodeTimeoutTask", false, PasscodeTimeoutTask.class);
    }

    public void attach(Linker linker) {
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", PasscodeTimeoutTask.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mEventBus);
    }

    public PasscodeTimeoutTask get() {
        PasscodeTimeoutTask passcodeTimeoutTask = new PasscodeTimeoutTask();
        injectMembers(passcodeTimeoutTask);
        return passcodeTimeoutTask;
    }

    public void injectMembers(PasscodeTimeoutTask passcodeTimeoutTask) {
        passcodeTimeoutTask.mEventBus = (Bus) this.mEventBus.get();
    }
}
