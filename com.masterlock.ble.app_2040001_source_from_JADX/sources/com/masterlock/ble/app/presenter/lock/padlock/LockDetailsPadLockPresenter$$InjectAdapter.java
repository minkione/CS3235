package com.masterlock.ble.app.presenter.lock.padlock;

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

public final class LockDetailsPadLockPresenter$$InjectAdapter extends Binding<LockDetailsPadLockPresenter> implements MembersInjector<LockDetailsPadLockPresenter> {
    private Binding<ContentResolver> mContentResolver;
    private Binding<Bus> mEventBus;
    private Binding<GuestService> mGuestService;
    private Binding<ProductInvitationService> mInvitationService;
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<UploadTaskQueue> mUploadTaskQueue;
    private Binding<AuthenticatedPresenter> supertype;

    public LockDetailsPadLockPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.lock.padlock.LockDetailsPadLockPresenter", false, LockDetailsPadLockPresenter.class);
    }

    public void attach(Linker linker) {
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", LockDetailsPadLockPresenter.class, getClass().getClassLoader());
        this.mEventBus = linker.requestBinding("com.squareup.otto.Bus", LockDetailsPadLockPresenter.class, getClass().getClassLoader());
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", LockDetailsPadLockPresenter.class, getClass().getClassLoader());
        this.mInvitationService = linker.requestBinding("com.masterlock.ble.app.service.ProductInvitationService", LockDetailsPadLockPresenter.class, getClass().getClassLoader());
        this.mGuestService = linker.requestBinding("com.masterlock.ble.app.service.GuestService", LockDetailsPadLockPresenter.class, getClass().getClassLoader());
        this.mContentResolver = linker.requestBinding("android.content.ContentResolver", LockDetailsPadLockPresenter.class, getClass().getClassLoader());
        this.mUploadTaskQueue = linker.requestBinding("com.masterlock.ble.app.tape.UploadTaskQueue", LockDetailsPadLockPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", LockDetailsPadLockPresenter.class, getClass().getClassLoader(), false, true);
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

    public void injectMembers(LockDetailsPadLockPresenter lockDetailsPadLockPresenter) {
        lockDetailsPadLockPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        lockDetailsPadLockPresenter.mEventBus = (Bus) this.mEventBus.get();
        lockDetailsPadLockPresenter.mLockService = (LockService) this.mLockService.get();
        lockDetailsPadLockPresenter.mInvitationService = (ProductInvitationService) this.mInvitationService.get();
        lockDetailsPadLockPresenter.mGuestService = (GuestService) this.mGuestService.get();
        lockDetailsPadLockPresenter.mContentResolver = (ContentResolver) this.mContentResolver.get();
        lockDetailsPadLockPresenter.mUploadTaskQueue = (UploadTaskQueue) this.mUploadTaskQueue.get();
        this.supertype.injectMembers(lockDetailsPadLockPresenter);
    }
}
