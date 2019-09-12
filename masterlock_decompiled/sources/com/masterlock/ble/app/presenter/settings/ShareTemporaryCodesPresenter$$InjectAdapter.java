package com.masterlock.ble.app.presenter.settings;

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

public final class ShareTemporaryCodesPresenter$$InjectAdapter extends Binding<ShareTemporaryCodesPresenter> implements MembersInjector<ShareTemporaryCodesPresenter> {
    private Binding<Bus> mBus;
    private Binding<LockService> mLockService;
    private Binding<ProductInvitationService> mProductInvitationService;
    private Binding<IScheduler> mScheduler;
    private Binding<UploadTaskQueue> mUploadTaskQueue;
    private Binding<AuthenticatedPresenter> supertype;

    public ShareTemporaryCodesPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.settings.ShareTemporaryCodesPresenter", false, ShareTemporaryCodesPresenter.class);
    }

    public void attach(Linker linker) {
        this.mProductInvitationService = linker.requestBinding("com.masterlock.ble.app.service.ProductInvitationService", ShareTemporaryCodesPresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", ShareTemporaryCodesPresenter.class, getClass().getClassLoader());
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", ShareTemporaryCodesPresenter.class, getClass().getClassLoader());
        this.mBus = linker.requestBinding("com.squareup.otto.Bus", ShareTemporaryCodesPresenter.class, getClass().getClassLoader());
        this.mUploadTaskQueue = linker.requestBinding("com.masterlock.ble.app.tape.UploadTaskQueue", ShareTemporaryCodesPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", ShareTemporaryCodesPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mProductInvitationService);
        set2.add(this.mScheduler);
        set2.add(this.mLockService);
        set2.add(this.mBus);
        set2.add(this.mUploadTaskQueue);
        set2.add(this.supertype);
    }

    public void injectMembers(ShareTemporaryCodesPresenter shareTemporaryCodesPresenter) {
        shareTemporaryCodesPresenter.mProductInvitationService = (ProductInvitationService) this.mProductInvitationService.get();
        shareTemporaryCodesPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        shareTemporaryCodesPresenter.mLockService = (LockService) this.mLockService.get();
        shareTemporaryCodesPresenter.mBus = (Bus) this.mBus.get();
        shareTemporaryCodesPresenter.mUploadTaskQueue = (UploadTaskQueue) this.mUploadTaskQueue.get();
        this.supertype.injectMembers(shareTemporaryCodesPresenter);
    }
}
