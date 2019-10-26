package com.masterlock.ble.app.service;

import com.masterlock.ble.app.dao.InvitationDAO;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class ProductInvitationService$$InjectAdapter extends Binding<ProductInvitationService> implements MembersInjector<ProductInvitationService> {
    private Binding<InvitationDAO> mInvitationDAO;

    public ProductInvitationService$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.service.ProductInvitationService", false, ProductInvitationService.class);
    }

    public void attach(Linker linker) {
        this.mInvitationDAO = linker.requestBinding("com.masterlock.ble.app.dao.InvitationDAO", ProductInvitationService.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mInvitationDAO);
    }

    public void injectMembers(ProductInvitationService productInvitationService) {
        productInvitationService.mInvitationDAO = (InvitationDAO) this.mInvitationDAO.get();
    }
}
