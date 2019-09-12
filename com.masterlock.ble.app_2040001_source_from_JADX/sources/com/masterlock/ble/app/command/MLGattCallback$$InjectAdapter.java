package com.masterlock.ble.app.command;

import com.masterlock.ble.app.tape.ConfirmTaskQueue;
import com.masterlock.ble.app.tape.UploadTaskQueue;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class MLGattCallback$$InjectAdapter extends Binding<MLGattCallback> implements MembersInjector<MLGattCallback> {
    private Binding<ConfirmTaskQueue> mConfirmTaskQueue;
    private Binding<UploadTaskQueue> mUploadTaskQueue;

    public MLGattCallback$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.command.MLGattCallback", false, MLGattCallback.class);
    }

    public void attach(Linker linker) {
        this.mUploadTaskQueue = linker.requestBinding("com.masterlock.ble.app.tape.UploadTaskQueue", MLGattCallback.class, getClass().getClassLoader());
        this.mConfirmTaskQueue = linker.requestBinding("com.masterlock.ble.app.tape.ConfirmTaskQueue", MLGattCallback.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mUploadTaskQueue);
        set2.add(this.mConfirmTaskQueue);
    }

    public void injectMembers(MLGattCallback mLGattCallback) {
        mLGattCallback.mUploadTaskQueue = (UploadTaskQueue) this.mUploadTaskQueue.get();
        mLGattCallback.mConfirmTaskQueue = (ConfirmTaskQueue) this.mConfirmTaskQueue.get();
    }
}
