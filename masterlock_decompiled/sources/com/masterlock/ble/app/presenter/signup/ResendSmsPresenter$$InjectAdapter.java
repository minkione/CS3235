package com.masterlock.ble.app.presenter.signup;

import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.service.SignUpService;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class ResendSmsPresenter$$InjectAdapter extends Binding<ResendSmsPresenter> implements MembersInjector<ResendSmsPresenter> {
    private Binding<Bus> mEventBus;
    private Binding<SignUpService> mSignUpService;
    private Binding<Presenter> supertype;

    public ResendSmsPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.signup.ResendSmsPresenter", false, ResendSmsPresenter.class);
    }

    public void attach(Linker linker) {
        this.mSignUpService = linker.requestBinding("com.masterlock.ble.app.service.SignUpService", ResendSmsPresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", ResendSmsPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.Presenter", ResendSmsPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mSignUpService);
        set2.add(this.mEventBus);
        set2.add(this.supertype);
    }

    public void injectMembers(ResendSmsPresenter resendSmsPresenter) {
        resendSmsPresenter.mSignUpService = (SignUpService) this.mSignUpService.get();
        resendSmsPresenter.mEventBus = (Bus) this.mEventBus.get();
        this.supertype.injectMembers(resendSmsPresenter);
    }
}
