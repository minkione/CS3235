package com.masterlock.ble.app.presenter.signup;

import com.masterlock.ble.app.dao.SignUpDAO;
import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.service.ProductInvitationService;
import com.masterlock.ble.app.service.SignInService;
import com.masterlock.ble.app.service.SignUpService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.util.SignUpHelper;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class AccountDetailsPresenter$$InjectAdapter extends Binding<AccountDetailsPresenter> implements MembersInjector<AccountDetailsPresenter> {
    private Binding<Bus> mEventBus;
    private Binding<ProductInvitationService> mProductInvitationService;
    private Binding<IScheduler> mScheduler;
    private Binding<SignInService> mSignInService;
    private Binding<SignUpDAO> mSignUpDAO;
    private Binding<SignUpHelper> mSignUpHelper;
    private Binding<SignUpService> signUpService;
    private Binding<Presenter> supertype;

    public AccountDetailsPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.signup.AccountDetailsPresenter", false, AccountDetailsPresenter.class);
    }

    public void attach(Linker linker) {
        this.mProductInvitationService = linker.requestBinding("com.masterlock.ble.app.service.ProductInvitationService", AccountDetailsPresenter.class, getClass().getClassLoader());
        this.signUpService = linker.requestBinding("com.masterlock.ble.app.service.SignUpService", AccountDetailsPresenter.class, getClass().getClassLoader());
        this.mSignInService = linker.requestBinding("com.masterlock.ble.app.service.SignInService", AccountDetailsPresenter.class, getClass().getClassLoader());
        this.mSignUpHelper = linker.requestBinding("com.masterlock.ble.app.util.SignUpHelper", AccountDetailsPresenter.class, getClass().getClassLoader());
        this.mSignUpDAO = linker.requestBinding("com.masterlock.ble.app.dao.SignUpDAO", AccountDetailsPresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", AccountDetailsPresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", AccountDetailsPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.Presenter", AccountDetailsPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mProductInvitationService);
        set2.add(this.signUpService);
        set2.add(this.mSignInService);
        set2.add(this.mSignUpHelper);
        set2.add(this.mSignUpDAO);
        set2.add(this.mEventBus);
        set2.add(this.mScheduler);
        set2.add(this.supertype);
    }

    public void injectMembers(AccountDetailsPresenter accountDetailsPresenter) {
        accountDetailsPresenter.mProductInvitationService = (ProductInvitationService) this.mProductInvitationService.get();
        accountDetailsPresenter.signUpService = (SignUpService) this.signUpService.get();
        accountDetailsPresenter.mSignInService = (SignInService) this.mSignInService.get();
        accountDetailsPresenter.mSignUpHelper = (SignUpHelper) this.mSignUpHelper.get();
        accountDetailsPresenter.mSignUpDAO = (SignUpDAO) this.mSignUpDAO.get();
        accountDetailsPresenter.mEventBus = (Bus) this.mEventBus.get();
        accountDetailsPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        this.supertype.injectMembers(accountDetailsPresenter);
    }
}
