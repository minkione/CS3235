package com.masterlock.ble.app.presenter.lock.keysafe;

import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.CodeTypesUtil;
import com.masterlock.ble.app.util.IScheduler;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class PrimaryCodeUpdateKeySafePresenter$$InjectAdapter extends Binding<PrimaryCodeUpdateKeySafePresenter> implements MembersInjector<PrimaryCodeUpdateKeySafePresenter> {
    private Binding<CodeTypesUtil> mCodeTypeUtils;
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<AuthenticatedPresenter> supertype;

    public PrimaryCodeUpdateKeySafePresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.lock.keysafe.PrimaryCodeUpdateKeySafePresenter", false, PrimaryCodeUpdateKeySafePresenter.class);
    }

    public void attach(Linker linker) {
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", PrimaryCodeUpdateKeySafePresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", PrimaryCodeUpdateKeySafePresenter.class, getClass().getClassLoader());
        this.mCodeTypeUtils = linker.requestBinding("com.masterlock.ble.app.util.CodeTypesUtil", PrimaryCodeUpdateKeySafePresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", PrimaryCodeUpdateKeySafePresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockService);
        set2.add(this.mScheduler);
        set2.add(this.mCodeTypeUtils);
        set2.add(this.supertype);
    }

    public void injectMembers(PrimaryCodeUpdateKeySafePresenter primaryCodeUpdateKeySafePresenter) {
        primaryCodeUpdateKeySafePresenter.mLockService = (LockService) this.mLockService.get();
        primaryCodeUpdateKeySafePresenter.mScheduler = (IScheduler) this.mScheduler.get();
        primaryCodeUpdateKeySafePresenter.mCodeTypeUtils = (CodeTypesUtil) this.mCodeTypeUtils.get();
        this.supertype.injectMembers(primaryCodeUpdateKeySafePresenter);
    }
}
