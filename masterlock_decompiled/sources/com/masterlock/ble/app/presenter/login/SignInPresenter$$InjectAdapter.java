package com.masterlock.ble.app.presenter.login;

import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.ProductInvitationService;
import com.masterlock.ble.app.service.SignInService;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class SignInPresenter$$InjectAdapter extends Binding<SignInPresenter> implements MembersInjector<SignInPresenter> {
    private Binding<Bus> mEventBus;
    private Binding<LockService> mLockService;
    private Binding<ProductInvitationService> mProductInvitationService;
    private Binding<IScheduler> mScheduler;
    private Binding<SignInService> mSignInService;
    private Binding<Presenter> supertype;

    public SignInPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.login.SignInPresenter", false, SignInPresenter.class);
    }

    public void attach(Linker linker) {
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", SignInPresenter.class, getClass().getClassLoader());
        this.mSignInService = linker.requestBinding("com.masterlock.ble.app.service.SignInService", SignInPresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", SignInPresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", SignInPresenter.class, getClass().getClassLoader());
        this.mProductInvitationService = linker.requestBinding("com.masterlock.ble.app.service.ProductInvitationService", SignInPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.Presenter", SignInPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockService);
        set2.add(this.mSignInService);
        set2.add(this.mScheduler);
        set2.add(this.mEventBus);
        set2.add(this.mProductInvitationService);
        set2.add(this.supertype);
    }

    public void injectMembers(SignInPresenter signInPresenter) {
        signInPresenter.mLockService = (LockService) this.mLockService.get();
        signInPresenter.mSignInService = (SignInService) this.mSignInService.get();
        signInPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        signInPresenter.mEventBus = (Bus) this.mEventBus.get();
        signInPresenter.mProductInvitationService = (ProductInvitationService) this.mProductInvitationService.get();
        this.supertype.injectMembers(signInPresenter);
    }
}
