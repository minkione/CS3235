package com.masterlock.ble.app.presenter.guest;

import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.ProductInvitationService;
import com.masterlock.ble.app.tape.UploadTaskQueue;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class GrantAccessPresenter$$InjectAdapter extends Binding<GrantAccessPresenter> implements MembersInjector<GrantAccessPresenter> {
    private Binding<Bus> mEventBus;
    private Binding<LockService> mLockService;
    private Binding<ProductInvitationService> mProductInvitationService;
    private Binding<IScheduler> mScheduler;
    private Binding<UploadTaskQueue> mUploadTaskQueue;
    private Binding<AuthenticatedPresenter> supertype;

    public GrantAccessPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.guest.GrantAccessPresenter", false, GrantAccessPresenter.class);
    }

    public void attach(Linker linker) {
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", GrantAccessPresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", GrantAccessPresenter.class, getClass().getClassLoader());
        this.mProductInvitationService = linker.requestBinding("com.masterlock.ble.app.service.ProductInvitationService", GrantAccessPresenter.class, getClass().getClassLoader());
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", GrantAccessPresenter.class, getClass().getClassLoader());
        this.mUploadTaskQueue = linker.requestBinding("com.masterlock.ble.app.tape.UploadTaskQueue", GrantAccessPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", GrantAccessPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mScheduler);
        set2.add(this.mEventBus);
        set2.add(this.mProductInvitationService);
        set2.add(this.mLockService);
        set2.add(this.mUploadTaskQueue);
        set2.add(this.supertype);
    }

    public void injectMembers(GrantAccessPresenter grantAccessPresenter) {
        grantAccessPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        grantAccessPresenter.mEventBus = (Bus) this.mEventBus.get();
        grantAccessPresenter.mProductInvitationService = (ProductInvitationService) this.mProductInvitationService.get();
        grantAccessPresenter.mLockService = (LockService) this.mLockService.get();
        grantAccessPresenter.mUploadTaskQueue = (UploadTaskQueue) this.mUploadTaskQueue.get();
        this.supertype.injectMembers(grantAccessPresenter);
    }
}
