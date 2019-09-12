package com.masterlock.ble.app.util;

import com.masterlock.ble.app.service.KMSDeviceService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

public final class CodeTypesUtil$$InjectAdapter extends Binding<CodeTypesUtil> implements Provider<CodeTypesUtil>, MembersInjector<CodeTypesUtil> {
    private Binding<KMSDeviceService> mKMSDeviceService;

    public CodeTypesUtil$$InjectAdapter() {
        super("com.masterlock.ble.app.util.CodeTypesUtil", "members/com.masterlock.ble.app.util.CodeTypesUtil", false, CodeTypesUtil.class);
    }

    public void attach(Linker linker) {
        this.mKMSDeviceService = linker.requestBinding("com.masterlock.ble.app.service.KMSDeviceService", CodeTypesUtil.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mKMSDeviceService);
    }

    public CodeTypesUtil get() {
        CodeTypesUtil codeTypesUtil = new CodeTypesUtil();
        injectMembers(codeTypesUtil);
        return codeTypesUtil;
    }

    public void injectMembers(CodeTypesUtil codeTypesUtil) {
        codeTypesUtil.mKMSDeviceService = (KMSDeviceService) this.mKMSDeviceService.get();
    }
}
