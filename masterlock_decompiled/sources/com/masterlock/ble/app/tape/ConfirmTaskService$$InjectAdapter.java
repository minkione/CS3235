package com.masterlock.ble.app.tape;

import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

public final class ConfirmTaskService$$InjectAdapter extends Binding<ConfirmTaskService> implements Provider<ConfirmTaskService>, MembersInjector<ConfirmTaskService> {
    private Binding<ConfirmTaskQueue> queue;

    public ConfirmTaskService$$InjectAdapter() {
        super("com.masterlock.ble.app.tape.ConfirmTaskService", "members/com.masterlock.ble.app.tape.ConfirmTaskService", false, ConfirmTaskService.class);
    }

    public void attach(Linker linker) {
        this.queue = linker.requestBinding("com.masterlock.ble.app.tape.ConfirmTaskQueue", ConfirmTaskService.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.queue);
    }

    public ConfirmTaskService get() {
        ConfirmTaskService confirmTaskService = new ConfirmTaskService();
        injectMembers(confirmTaskService);
        return confirmTaskService;
    }

    public void injectMembers(ConfirmTaskService confirmTaskService) {
        confirmTaskService.queue = (ConfirmTaskQueue) this.queue.get();
    }
}
