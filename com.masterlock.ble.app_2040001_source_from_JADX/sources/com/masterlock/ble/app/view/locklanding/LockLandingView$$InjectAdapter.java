package com.masterlock.ble.app.view.locklanding;

import com.masterlock.ble.app.presenter.locklanding.LockLandingPresenter;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class LockLandingView$$InjectAdapter extends Binding<LockLandingView> implements MembersInjector<LockLandingView> {
    private Binding<LockLandingPresenter> lockLandingPresenter;

    public LockLandingView$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.view.locklanding.LockLandingView", false, LockLandingView.class);
    }

    public void attach(Linker linker) {
        this.lockLandingPresenter = linker.requestBinding("com.masterlock.ble.app.presenter.locklanding.LockLandingPresenter", LockLandingView.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.lockLandingPresenter);
    }

    public void injectMembers(LockLandingView lockLandingView) {
        lockLandingView.lockLandingPresenter = (LockLandingPresenter) this.lockLandingPresenter.get();
    }
}
