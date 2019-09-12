package com.masterlock.ble.app.activity;

import android.content.ContentResolver;
import com.masterlock.ble.app.service.GuestService;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.ProductInvitationService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.util.PermissionUtil;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

public final class LockActivity$$InjectAdapter extends Binding<LockActivity> implements Provider<LockActivity>, MembersInjector<LockActivity> {
    private Binding<ContentResolver> mContentResolver;
    private Binding<GuestService> mGuestService;
    private Binding<LockService> mLockService;
    private Binding<PermissionUtil> mPermissionUtil;
    private Binding<ProductInvitationService> mProductInvitationService;
    private Binding<IScheduler> mScheduler;
    private Binding<FlowActivity> supertype;

    public LockActivity$$InjectAdapter() {
        super("com.masterlock.ble.app.activity.LockActivity", "members/com.masterlock.ble.app.activity.LockActivity", false, LockActivity.class);
    }

    public void attach(Linker linker) {
        this.mGuestService = linker.requestBinding("com.masterlock.ble.app.service.GuestService", LockActivity.class, getClass().getClassLoader());
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", LockActivity.class, getClass().getClassLoader());
        this.mProductInvitationService = linker.requestBinding("com.masterlock.ble.app.service.ProductInvitationService", LockActivity.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", LockActivity.class, getClass().getClassLoader());
        this.mPermissionUtil = linker.requestBinding("com.masterlock.ble.app.util.PermissionUtil", LockActivity.class, getClass().getClassLoader());
        this.mContentResolver = linker.requestBinding("android.content.ContentResolver", LockActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.activity.FlowActivity", LockActivity.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mGuestService);
        set2.add(this.mLockService);
        set2.add(this.mProductInvitationService);
        set2.add(this.mScheduler);
        set2.add(this.mPermissionUtil);
        set2.add(this.mContentResolver);
        set2.add(this.supertype);
    }

    public LockActivity get() {
        LockActivity lockActivity = new LockActivity();
        injectMembers(lockActivity);
        return lockActivity;
    }

    public void injectMembers(LockActivity lockActivity) {
        lockActivity.mGuestService = (GuestService) this.mGuestService.get();
        lockActivity.mLockService = (LockService) this.mLockService.get();
        lockActivity.mProductInvitationService = (ProductInvitationService) this.mProductInvitationService.get();
        lockActivity.mScheduler = (IScheduler) this.mScheduler.get();
        lockActivity.mPermissionUtil = (PermissionUtil) this.mPermissionUtil.get();
        lockActivity.mContentResolver = (ContentResolver) this.mContentResolver.get();
        this.supertype.injectMembers(lockActivity);
    }
}
