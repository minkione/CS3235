package com.masterlock.ble.app.service.scan;

import com.masterlock.api.client.KMSDeviceClient;
import com.masterlock.api.client.KMSDeviceLogClient;
import com.masterlock.api.client.ProductClient;
import com.masterlock.ble.app.service.LockService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

public final class FirmwareUpdateService$$InjectAdapter extends Binding<FirmwareUpdateService> implements Provider<FirmwareUpdateService>, MembersInjector<FirmwareUpdateService> {
    private Binding<KMSDeviceLogClient> mKMSDeviceLogClient;
    private Binding<KMSDeviceClient> mKmsDeviceClient;
    private Binding<LockService> mLockService;
    private Binding<ProductClient> mProductClient;

    public FirmwareUpdateService$$InjectAdapter() {
        super("com.masterlock.ble.app.service.scan.FirmwareUpdateService", "members/com.masterlock.ble.app.service.scan.FirmwareUpdateService", false, FirmwareUpdateService.class);
    }

    public void attach(Linker linker) {
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", FirmwareUpdateService.class, getClass().getClassLoader());
        this.mProductClient = linker.requestBinding("com.masterlock.api.client.ProductClient", FirmwareUpdateService.class, getClass().getClassLoader());
        this.mKMSDeviceLogClient = linker.requestBinding("com.masterlock.api.client.KMSDeviceLogClient", FirmwareUpdateService.class, getClass().getClassLoader());
        this.mKmsDeviceClient = linker.requestBinding("com.masterlock.api.client.KMSDeviceClient", FirmwareUpdateService.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockService);
        set2.add(this.mProductClient);
        set2.add(this.mKMSDeviceLogClient);
        set2.add(this.mKmsDeviceClient);
    }

    public FirmwareUpdateService get() {
        FirmwareUpdateService firmwareUpdateService = new FirmwareUpdateService();
        injectMembers(firmwareUpdateService);
        return firmwareUpdateService;
    }

    public void injectMembers(FirmwareUpdateService firmwareUpdateService) {
        firmwareUpdateService.mLockService = (LockService) this.mLockService.get();
        firmwareUpdateService.mProductClient = (ProductClient) this.mProductClient.get();
        firmwareUpdateService.mKMSDeviceLogClient = (KMSDeviceLogClient) this.mKMSDeviceLogClient.get();
        firmwareUpdateService.mKmsDeviceClient = (KMSDeviceClient) this.mKmsDeviceClient.get();
    }
}
