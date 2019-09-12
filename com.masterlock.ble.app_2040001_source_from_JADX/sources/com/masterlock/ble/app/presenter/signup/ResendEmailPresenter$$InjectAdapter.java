package com.masterlock.ble.app.presenter.signup;

import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.service.SignUpService;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class ResendEmailPresenter$$InjectAdapter extends Binding<ResendEmailPresenter> implements MembersInjector<ResendEmailPresenter> {
    private Binding<Bus> mEventBus;
    private Binding<IScheduler> mScheduler;
    private Binding<SignUpService> mSignUpService;
    private Binding<Presenter> supertype;

    public ResendEmailPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.signup.ResendEmailPresenter", false, ResendEmailPresenter.class);
    }

    public void attach(Linker linker) {
        this.mSignUpService = linker.requestBinding("com.masterlock.ble.app.service.SignUpService", ResendEmailPresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", ResendEmailPresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", ResendEmailPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.Presenter", ResendEmailPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mSignUpService);
        set2.add(this.mEventBus);
        set2.add(this.mScheduler);
        set2.add(this.supertype);
    }

    public void injectMembers(ResendEmailPresenter resendEmailPresenter) {
        resendEmailPresenter.mSignUpService = (SignUpService) this.mSignUpService.get();
        resendEmailPresenter.mEventBus = (Bus) this.mEventBus.get();
        resendEmailPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        this.supertype.injectMembers(resendEmailPresenter);
    }
}
