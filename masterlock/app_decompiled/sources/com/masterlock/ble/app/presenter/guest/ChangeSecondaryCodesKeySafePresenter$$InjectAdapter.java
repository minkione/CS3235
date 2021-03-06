package com.masterlock.ble.app.presenter.guest;

import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class ChangeSecondaryCodesKeySafePresenter$$InjectAdapter extends Binding<ChangeSecondaryCodesKeySafePresenter> implements MembersInjector<ChangeSecondaryCodesKeySafePresenter> {
    private Binding<Bus> mEventBus;
    private Binding<AuthenticatedPresenter> supertype;

    public ChangeSecondaryCodesKeySafePresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.guest.ChangeSecondaryCodesKeySafePresenter", false, ChangeSecondaryCodesKeySafePresenter.class);
    }

    public void attach(Linker linker) {
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", ChangeSecondaryCodesKeySafePresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", ChangeSecondaryCodesKeySafePresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mEventBus);
        set2.add(this.supertype);
    }

    public void injectMembers(ChangeSecondaryCodesKeySafePresenter changeSecondaryCodesKeySafePresenter) {
        changeSecondaryCodesKeySafePresenter.mEventBus = (Bus) this.mEventBus.get();
        this.supertype.injectMembers(changeSecondaryCodesKeySafePresenter);
    }
}
