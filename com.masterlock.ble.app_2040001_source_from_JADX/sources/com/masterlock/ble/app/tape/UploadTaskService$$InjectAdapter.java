package com.masterlock.ble.app.tape;

import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

public final class UploadTaskService$$InjectAdapter extends Binding<UploadTaskService> implements Provider<UploadTaskService>, MembersInjector<UploadTaskService> {
    private Binding<Bus> bus;
    private Binding<UploadTaskQueue> queue;

    public UploadTaskService$$InjectAdapter() {
        super("com.masterlock.ble.app.tape.UploadTaskService", "members/com.masterlock.ble.app.tape.UploadTaskService", false, UploadTaskService.class);
    }

    public void attach(Linker linker) {
        this.queue = linker.requestBinding("com.masterlock.ble.app.tape.UploadTaskQueue", UploadTaskService.class, getClass().getClassLoader());
        this.bus = linker.requestBinding("com.squareup.otto.Bus", UploadTaskService.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.queue);
        set2.add(this.bus);
    }

    public UploadTaskService get() {
        UploadTaskService uploadTaskService = new UploadTaskService();
        injectMembers(uploadTaskService);
        return uploadTaskService;
    }

    public void injectMembers(UploadTaskService uploadTaskService) {
        uploadTaskService.queue = (UploadTaskQueue) this.queue.get();
        uploadTaskService.bus = (Bus) this.bus.get();
    }
}
