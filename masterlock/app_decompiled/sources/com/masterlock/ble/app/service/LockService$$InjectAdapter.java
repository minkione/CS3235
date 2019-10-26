package com.masterlock.ble.app.service;

import com.masterlock.ble.app.dao.InvitationDAO;
import com.masterlock.ble.app.dao.LockDAO;
import com.masterlock.ble.app.util.LocalResourcesHelper;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class LockService$$InjectAdapter extends Binding<LockService> implements MembersInjector<LockService> {
    private Binding<InvitationDAO> mInvitationDAO;
    private Binding<LocalResourcesHelper> mLocalResourcesHelper;
    private Binding<LockDAO> mLockDAO;

    public LockService$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.service.LockService", false, LockService.class);
    }

    public void attach(Linker linker) {
        this.mLockDAO = linker.requestBinding("com.masterlock.ble.app.dao.LockDAO", LockService.class, getClass().getClassLoader());
        this.mInvitationDAO = linker.requestBinding("com.masterlock.ble.app.dao.InvitationDAO", LockService.class, getClass().getClassLoader());
        this.mLocalResourcesHelper = linker.requestBinding("com.masterlock.ble.app.util.LocalResourcesHelper", LockService.class, getClass().getClassLoader());
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockDAO);
        set2.add(this.mInvitationDAO);
        set2.add(this.mLocalResourcesHelper);
    }

    public void injectMembers(LockService lockService) {
        lockService.mLockDAO = (LockDAO) this.mLockDAO.get();
        lockService.mInvitationDAO = (InvitationDAO) this.mInvitationDAO.get();
        lockService.mLocalResourcesHelper = (LocalResourcesHelper) this.mLocalResourcesHelper.get();
    }
}
