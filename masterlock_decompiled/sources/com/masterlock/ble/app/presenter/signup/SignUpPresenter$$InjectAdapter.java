package com.masterlock.ble.app.presenter.signup;

import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.service.ProductInvitationService;
import com.masterlock.ble.app.service.SignUpService;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class SignUpPresenter$$InjectAdapter extends Binding<SignUpPresenter> implements MembersInjector<SignUpPresenter> {
    private Binding<Bus> mEventBus;
    private Binding<ProductInvitationService> mProductInvitationClient;
    private Binding<IScheduler> mScheduler;
    private Binding<SignUpService> signUpService;
    private Binding<Presenter> supertype;

    public SignUpPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.signup.SignUpPresenter", false, SignUpPresenter.class);
    }

    public void attach(Linker linker) {
        this.signUpService = linker.requestBinding("com.masterlock.ble.app.service.SignUpService", SignUpPresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", SignUpPresenter.class, getClass().getClassLoader());
        this.mProductInvitationClient = linker.requestBinding("com.masterlock.ble.app.service.ProductInvitationService", SignUpPresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", SignUpPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.Presenter", SignUpPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.signUpService);
        set2.add(this.mEventBus);
        set2.add(this.mProductInvitationClient);
        set2.add(this.mScheduler);
        set2.add(this.supertype);
    }

    public void injectMembers(SignUpPresenter signUpPresenter) {
        signUpPresenter.signUpService = (SignUpService) this.signUpService.get();
        signUpPresenter.mEventBus = (Bus) this.mEventBus.get();
        signUpPresenter.mProductInvitationClient = (ProductInvitationService) this.mProductInvitationClient.get();
        signUpPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        this.supertype.injectMembers(signUpPresenter);
    }
}
