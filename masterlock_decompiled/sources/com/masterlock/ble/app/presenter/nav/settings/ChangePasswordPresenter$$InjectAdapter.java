package com.masterlock.ble.app.presenter.nav.settings;

import com.masterlock.ble.app.dao.SignUpDAO;
import com.masterlock.ble.app.service.ProductInvitationService;
import com.masterlock.ble.app.service.SignInService;
import com.masterlock.ble.app.util.SignUpHelper;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class ChangePasswordPresenter$$InjectAdapter extends Binding<ChangePasswordPresenter> implements MembersInjector<ChangePasswordPresenter> {
    private Binding<ProductInvitationService> mProductInvitationService;
    private Binding<SignInService> mSignInService;
    private Binding<SignUpDAO> mSignUpDAO;
    private Binding<SignUpHelper> mSignUpHelper;
    private Binding<ProfileUpdateBasePresenter> supertype;

    public ChangePasswordPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.nav.settings.ChangePasswordPresenter", false, ChangePasswordPresenter.class);
    }

    public void attach(Linker linker) {
        this.mProductInvitationService = linker.requestBinding("com.masterlock.ble.app.service.ProductInvitationService", ChangePasswordPresenter.class, getClass().getClassLoader());
        this.mSignInService = linker.requestBinding("com.masterlock.ble.app.service.SignInService", ChangePasswordPresenter.class, getClass().getClassLoader());
        this.mSignUpHelper = linker.requestBinding("com.masterlock.ble.app.util.SignUpHelper", ChangePasswordPresenter.class, getClass().getClassLoader());
        this.mSignUpDAO = linker.requestBinding("com.masterlock.ble.app.dao.SignUpDAO", ChangePasswordPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.nav.settings.ProfileUpdateBasePresenter", ChangePasswordPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mProductInvitationService);
        set2.add(this.mSignInService);
        set2.add(this.mSignUpHelper);
        set2.add(this.mSignUpDAO);
        set2.add(this.supertype);
    }

    public void injectMembers(ChangePasswordPresenter changePasswordPresenter) {
        changePasswordPresenter.mProductInvitationService = (ProductInvitationService) this.mProductInvitationService.get();
        changePasswordPresenter.mSignInService = (SignInService) this.mSignInService.get();
        changePasswordPresenter.mSignUpHelper = (SignUpHelper) this.mSignUpHelper.get();
        changePasswordPresenter.mSignUpDAO = (SignUpDAO) this.mSignUpDAO.get();
        this.supertype.injectMembers(changePasswordPresenter);
    }
}
