package com.masterlock.ble.app.util;

import com.masterlock.ble.app.service.SignInService;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class PasscodeUtil$$InjectAdapter extends Binding<PasscodeUtil> implements MembersInjector<PasscodeUtil> {
    private Binding<Bus> mEventBus;
    private Binding<IScheduler> mScheduler;
    private Binding<SignInService> mSignUpService;

    public PasscodeUtil$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.util.PasscodeUtil", false, PasscodeUtil.class);
    }

    public void attach(Linker linker) {
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", PasscodeUtil.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", PasscodeUtil.class, getClass().getClassLoader());
        this.mSignUpService = linker.requestBinding("com.masterlock.ble.app.service.SignInService", PasscodeUtil.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mEventBus);
        set2.add(this.mScheduler);
        set2.add(this.mSignUpService);
    }

    public void injectMembers(PasscodeUtil passcodeUtil) {
        passcodeUtil.mEventBus = (Bus) this.mEventBus.get();
        passcodeUtil.mScheduler = (IScheduler) this.mScheduler.get();
        passcodeUtil.mSignUpService = (SignInService) this.mSignUpService.get();
    }
}
