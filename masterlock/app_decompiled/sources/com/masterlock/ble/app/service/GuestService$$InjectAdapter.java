package com.masterlock.ble.app.service;

import com.masterlock.ble.app.dao.GuestDAO;
import com.masterlock.ble.app.dao.InvitationDAO;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class GuestService$$InjectAdapter extends Binding<GuestService> implements MembersInjector<GuestService> {
    private Binding<GuestDAO> mGuestDAO;
    private Binding<InvitationDAO> mInvitationDAO;
    private Binding<ProductInvitationService> mProductInvitationService;

    public GuestService$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.service.GuestService", false, GuestService.class);
    }

    public void attach(Linker linker) {
        this.mGuestDAO = linker.requestBinding("com.masterlock.ble.app.dao.GuestDAO", GuestService.class, getClass().getClassLoader());
        this.mInvitationDAO = linker.requestBinding("com.masterlock.ble.app.dao.InvitationDAO", GuestService.class, getClass().getClassLoader());
        this.mProductInvitationService = linker.requestBinding("com.masterlock.ble.app.service.ProductInvitationService", GuestService.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mGuestDAO);
        set2.add(this.mInvitationDAO);
        set2.add(this.mProductInvitationService);
    }

    public void injectMembers(GuestService guestService) {
        guestService.mGuestDAO = (GuestDAO) this.mGuestDAO.get();
        guestService.mInvitationDAO = (InvitationDAO) this.mInvitationDAO.get();
        guestService.mProductInvitationService = (ProductInvitationService) this.mProductInvitationService.get();
    }
}
