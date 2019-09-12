package com.masterlock.ble.app.activity;

import com.masterlock.ble.app.util.PermissionUtil;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

public final class SignUpActivity$$InjectAdapter extends Binding<SignUpActivity> implements Provider<SignUpActivity>, MembersInjector<SignUpActivity> {
    private Binding<PermissionUtil> permissionUtil;
    private Binding<FlowActivity> supertype;

    public SignUpActivity$$InjectAdapter() {
        super("com.masterlock.ble.app.activity.SignUpActivity", "members/com.masterlock.ble.app.activity.SignUpActivity", false, SignUpActivity.class);
    }

    public void attach(Linker linker) {
        this.permissionUtil = linker.requestBinding("com.masterlock.ble.app.util.PermissionUtil", SignUpActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.activity.FlowActivity", SignUpActivity.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.permissionUtil);
        set2.add(this.supertype);
    }

    public SignUpActivity get() {
        SignUpActivity signUpActivity = new SignUpActivity();
        injectMembers(signUpActivity);
        return signUpActivity;
    }

    public void injectMembers(SignUpActivity signUpActivity) {
        signUpActivity.permissionUtil = (PermissionUtil) this.permissionUtil.get();
        this.supertype.injectMembers(signUpActivity);
    }
}
