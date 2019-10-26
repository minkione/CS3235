package com.masterlock.ble.app.activity;

import com.masterlock.ble.app.service.SignUpService;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

public final class SplashActivity$$InjectAdapter extends Binding<SplashActivity> implements Provider<SplashActivity>, MembersInjector<SplashActivity> {
    private Binding<Bus> mEventBus;
    private Binding<SignUpService> signUpService;
    private Binding<FlowActivity> supertype;

    public SplashActivity$$InjectAdapter() {
        super("com.masterlock.ble.app.activity.SplashActivity", "members/com.masterlock.ble.app.activity.SplashActivity", false, SplashActivity.class);
    }

    public void attach(Linker linker) {
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", SplashActivity.class, getClass().getClassLoader());
        this.signUpService = linker.requestBinding("com.masterlock.ble.app.service.SignUpService", SplashActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.activity.FlowActivity", SplashActivity.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mEventBus);
        set2.add(this.signUpService);
        set2.add(this.supertype);
    }

    public SplashActivity get() {
        SplashActivity splashActivity = new SplashActivity();
        injectMembers(splashActivity);
        return splashActivity;
    }

    public void injectMembers(SplashActivity splashActivity) {
        splashActivity.mEventBus = (Bus) this.mEventBus.get();
        splashActivity.signUpService = (SignUpService) this.signUpService.get();
        this.supertype.injectMembers(splashActivity);
    }
}
