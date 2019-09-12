package com.masterlock.ble.app.presenter.login;

import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.service.SignInService;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class ForgotUsernamePresenter$$InjectAdapter extends Binding<ForgotUsernamePresenter> implements MembersInjector<ForgotUsernamePresenter> {
    private Binding<Bus> mEventBus;
    private Binding<IScheduler> mScheduler;
    private Binding<SignInService> mSignInService;
    private Binding<Presenter> supertype;

    public ForgotUsernamePresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.login.ForgotUsernamePresenter", false, ForgotUsernamePresenter.class);
    }

    public void attach(Linker linker) {
        this.mSignInService = linker.requestBinding("com.masterlock.ble.app.service.SignInService", ForgotUsernamePresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", ForgotUsernamePresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", ForgotUsernamePresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.Presenter", ForgotUsernamePresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mSignInService);
        set2.add(this.mScheduler);
        set2.add(this.mEventBus);
        set2.add(this.supertype);
    }

    public void injectMembers(ForgotUsernamePresenter forgotUsernamePresenter) {
        forgotUsernamePresenter.mSignInService = (SignInService) this.mSignInService.get();
        forgotUsernamePresenter.mScheduler = (IScheduler) this.mScheduler.get();
        forgotUsernamePresenter.mEventBus = (Bus) this.mEventBus.get();
        this.supertype.injectMembers(forgotUsernamePresenter);
    }
}
