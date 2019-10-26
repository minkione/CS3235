package com.masterlock.ble.app.presenter.settings;

import android.content.ContentResolver;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.ProductInvitationService;
import com.masterlock.ble.app.util.IScheduler;
import com.squareup.otto.Bus;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class LockSettingsPresenter$$InjectAdapter extends Binding<LockSettingsPresenter> implements MembersInjector<LockSettingsPresenter> {
    private Binding<Bus> mBus;
    private Binding<ContentResolver> mContentResolver;
    private Binding<LockService> mLockService;
    private Binding<ProductInvitationService> mProductInvitationService;
    private Binding<IScheduler> mScheduler;
    private Binding<AuthenticatedPresenter> supertype;

    public LockSettingsPresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.settings.LockSettingsPresenter", false, LockSettingsPresenter.class);
    }

    public void attach(Linker linker) {
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", LockSettingsPresenter.class, getClass().getClassLoader());
        this.mBus = linker.requestBinding("com.squareup.otto.Bus", LockSettingsPresenter.class, getClass().getClassLoader());
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", LockSettingsPresenter.class, getClass().getClassLoader());
        this.mContentResolver = linker.requestBinding("android.content.ContentResolver", LockSettingsPresenter.class, getClass().getClassLoader());
        this.mProductInvitationService = linker.requestBinding("com.masterlock.ble.app.service.ProductInvitationService", LockSettingsPresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", LockSettingsPresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mScheduler);
        set2.add(this.mBus);
        set2.add(this.mLockService);
        set2.add(this.mContentResolver);
        set2.add(this.mProductInvitationService);
        set2.add(this.supertype);
    }

    public void injectMembers(LockSettingsPresenter lockSettingsPresenter) {
        lockSettingsPresenter.mScheduler = (IScheduler) this.mScheduler.get();
        lockSettingsPresenter.mBus = (Bus) this.mBus.get();
        lockSettingsPresenter.mLockService = (LockService) this.mLockService.get();
        lockSettingsPresenter.mContentResolver = (ContentResolver) this.mContentResolver.get();
        lockSettingsPresenter.mProductInvitationService = (ProductInvitationService) this.mProductInvitationService.get();
        this.supertype.injectMembers(lockSettingsPresenter);
    }
}
