package com.masterlock.ble.app.presenter.lock.keysafe;

import android.content.ContentResolver;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.GuestService;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.ProductInvitationService;
import com.masterlock.ble.app.tape.UploadTaskQueue;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class LockDetailsKeySafePresenter$$InjectAdapter extends Binding<LockDetailsKeySafePresenter> implements MembersInjector<LockDetailsKeySafePresenter> {
    private Binding<ContentResolver> mContentResolver;
    private Binding<Bus> mEventBus;
    private Binding<GuestService> mGuestService;
    private Binding<ProductInvitationService> mInvitationService;
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<UploadTaskQueue> mUploadTaskQueue;
    private Binding<AuthenticatedPresenter> supertype;

    public LockDetailsKeySafePresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.lock.keysafe.LockDetailsKeySafePresenter", false, LockDetailsKeySafePresenter.class);
    }

    public void attach(Linker linker) {
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", LockDetailsKeySafePresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", LockDetailsKeySafePresenter.class, getClass().getClassLoader());
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", LockDetailsKeySafePresenter.class, getClass().getClassLoader());
        this.mInvitationService = linker.requestBinding("com.masterlock.ble.app.service.ProductInvitationService", LockDetailsKeySafePresenter.class, getClass().getClassLoader());
        this.mGuestService = linker.requestBinding("com.masterlock.ble.app.service.GuestService", LockDetailsKeySafePresenter.class, getClass().getClassLoader());
        this.mContentResolver = linker.requestBinding("android.content.ContentResolver", LockDetailsKeySafePresenter.class, getClass().getClassLoader());
        this.mUploadTaskQueue = linker.requestBinding("com.masterlock.ble.app.tape.UploadTaskQueue", LockDetailsKeySafePresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", LockDetailsKeySafePresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mScheduler);
        set2.add(this.mEventBus);
        set2.add(this.mLockService);
        set2.add(this.mInvitationService);
        set2.add(this.mGuestService);
        set2.add(this.mContentResolver);
        set2.add(this.mUploadTaskQueue);
        set2.add(this.supertype);
    }

    public void injectMembers(LockDetailsKeySafePresenter lockDetailsKeySafePresenter) {
        lockDetailsKeySafePresenter.mScheduler = (IScheduler) this.mScheduler.get();
        lockDetailsKeySafePresenter.mEventBus = (Bus) this.mEventBus.get();
        lockDetailsKeySafePresenter.mLockService = (LockService) this.mLockService.get();
        lockDetailsKeySafePresenter.mInvitationService = (ProductInvitationService) this.mInvitationService.get();
        lockDetailsKeySafePresenter.mGuestService = (GuestService) this.mGuestService.get();
        lockDetailsKeySafePresenter.mContentResolver = (ContentResolver) this.mContentResolver.get();
        lockDetailsKeySafePresenter.mUploadTaskQueue = (UploadTaskQueue) this.mUploadTaskQueue.get();
        this.supertype.injectMembers(lockDetailsKeySafePresenter);
    }
}
