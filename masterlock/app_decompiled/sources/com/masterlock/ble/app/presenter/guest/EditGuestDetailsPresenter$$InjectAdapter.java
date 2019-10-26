package com.masterlock.ble.app.presenter.guest;

import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.GuestService;
import com.masterlock.ble.app.service.ProductInvitationService;
import com.masterlock.ble.app.util.IScheduler;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class EditGuestDetailsPresenter$$InjectAdapter extends Binding<EditGuestDetailsPresenter> implements MembersInjector<EditGuestDetailsPresenter> {
    private Binding<GuestService> mGuestService;
    private Binding<ProductInvitationService> mProductInvitationService;
    private Binding<IScheduler> mScheduler;
    private Binding<AuthenticatedPresenter> supertype;

    public EditGuestDetailsPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.guest.EditGuestDetailsPresenter", false, EditGuestDetailsPresenter.class);
    }

    public void attach(Linker linker) {
        this.mGuestService = linker.requestBinding("com.masterlock.ble.app.service.GuestService", EditGuestDetailsPresenter.class, getClass().getClassLoader());
        this.mProductInvitationService = linker.requestBinding("com.masterlock.ble.app.service.ProductInvitationService", EditGuestDetailsPresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", EditGuestDetailsPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", EditGuestDetailsPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mGuestService);
        set2.add(this.mProductInvitationService);
        set2.add(this.mScheduler);
        set2.add(this.supertype);
    }

    public void injectMembers(EditGuestDetailsPresenter editGuestDetailsPresenter) {
        editGuestDetailsPresenter.mGuestService = (GuestService) this.mGuestService.get();
        editGuestDetailsPresenter.mProductInvitationService = (ProductInvitationService) this.mProductInvitationService.get();
        editGuestDetailsPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        this.supertype.injectMembers(editGuestDetailsPresenter);
    }
}
