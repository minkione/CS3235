package com.masterlock.ble.app.service.scan;

import com.masterlock.api.client.KMSDeviceClient;
import com.masterlock.api.client.KMSDeviceLogClient;
import com.masterlock.api.client.ProductClient;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class ScanService$$InjectAdapter extends Binding<ScanService> implements MembersInjector<ScanService> {
    private Binding<Bus> mEventBus;
    private Binding<KMSDeviceLogClient> mKMSDeviceLogClient;
    private Binding<KMSDeviceClient> mKmsDeviceClient;
    private Binding<ProductClient> mProductClient;

    public ScanService$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.service.scan.ScanService", false, ScanService.class);
    }

    public void attach(Linker linker) {
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", ScanService.class, getClass().getClassLoader());
        this.mProductClient = linker.requestBinding("com.masterlock.api.client.ProductClient", ScanService.class, getClass().getClassLoader());
        this.mKMSDeviceLogClient = linker.requestBinding("com.masterlock.api.client.KMSDeviceLogClient", ScanService.class, getClass().getClassLoader());
        this.mKmsDeviceClient = linker.requestBinding("com.masterlock.api.client.KMSDeviceClient", ScanService.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mEventBus);
        set2.add(this.mProductClient);
        set2.add(this.mKMSDeviceLogClient);
        set2.add(this.mKmsDeviceClient);
    }

    public void injectMembers(ScanService scanService) {
        scanService.mEventBus = (Bus) this.mEventBus.get();
        scanService.mProductClient = (ProductClient) this.mProductClient.get();
        scanService.mKMSDeviceLogClient = (KMSDeviceLogClient) this.mKMSDeviceLogClient.get();
        scanService.mKmsDeviceClient = (KMSDeviceClient) this.mKmsDeviceClient.get();
    }
}
