package com.masterlock.ble.app.presenter.signup;

import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.service.TermsOfServiceService;
import com.masterlock.ble.app.util.IScheduler;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class TermsOfServicePresenter$$InjectAdapter extends Binding<TermsOfServicePresenter> implements MembersInjector<TermsOfServicePresenter> {
    private Binding<IScheduler> mScheduler;
    private Binding<Presenter> supertype;
    private Binding<TermsOfServiceService> termsOfServiceService;

    public TermsOfServicePresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.signup.TermsOfServicePresenter", false, TermsOfServicePresenter.class);
    }

    public void attach(Linker linker) {
        this.termsOfServiceService = linker.requestBinding("com.masterlock.ble.app.service.TermsOfServiceService", TermsOfServicePresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", TermsOfServicePresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.Presenter", TermsOfServicePresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.termsOfServiceService);
        set2.add(this.mScheduler);
        set2.add(this.supertype);
    }

    public void injectMembers(TermsOfServicePresenter termsOfServicePresenter) {
        termsOfServicePresenter.termsOfServiceService = (TermsOfServiceService) this.termsOfServiceService.get();
        termsOfServicePresenter.mScheduler = (IScheduler) this.mScheduler.get();
        this.supertype.injectMembers(termsOfServicePresenter);
    }
}
