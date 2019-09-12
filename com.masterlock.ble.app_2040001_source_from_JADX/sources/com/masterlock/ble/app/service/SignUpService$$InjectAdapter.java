package com.masterlock.ble.app.service;

import com.masterlock.ble.app.dao.SignUpDAO;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class SignUpService$$InjectAdapter extends Binding<SignUpService> implements MembersInjector<SignUpService> {
    private Binding<SignUpDAO> mSignUpDAO;

    public SignUpService$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.service.SignUpService", false, SignUpService.class);
    }

    public void attach(Linker linker) {
        this.mSignUpDAO = linker.requestBinding("com.masterlock.ble.app.dao.SignUpDAO", SignUpService.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mSignUpDAO);
    }

    public void injectMembers(SignUpService signUpService) {
        signUpService.mSignUpDAO = (SignUpDAO) this.mSignUpDAO.get();
    }
}
