package com.masterlock.ble.app.presenter.lock.keysafe;

import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.CodeTypesUtil;
import com.masterlock.ble.app.util.IScheduler;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

public final class SecondaryCodeUpdateKeySafePresenter$$InjectAdapter extends Binding<SecondaryCodeUpdateKeySafePresenter> implements MembersInjector<SecondaryCodeUpdateKeySafePresenter> {
    private Binding<CodeTypesUtil> mCodeTypesUtil;
    private Binding<LockService> mLockService;
    private Binding<IScheduler> mScheduler;
    private Binding<AuthenticatedPresenter> supertype;

    public SecondaryCodeUpdateKeySafePresenter$$InjectAdapter() {
        super(null, "members/com.masterlock.ble.app.presenter.lock.keysafe.SecondaryCodeUpdateKeySafePresenter", false, SecondaryCodeUpdateKeySafePresenter.class);
    }

    public void attach(Linker linker) {
        this.mLockService = linker.requestBinding("com.masterlock.ble.app.service.LockService", SecondaryCodeUpdateKeySafePresenter.class, getClass().getClassLoader());
        this.mScheduler = linker.requestBinding("com.masterlock.ble.app.util.IScheduler", SecondaryCodeUpdateKeySafePresenter.class, getClass().getClassLoader());
        this.mCodeTypesUtil = linker.requestBinding("com.masterlock.ble.app.util.CodeTypesUtil", SecondaryCodeUpdateKeySafePresenter.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.masterlock.ble.app.presenter.AuthenticatedPresenter", SecondaryCodeUpdateKeySafePresenter.class, getClass().getClassLoader(), false, true);
    }

    public void getDependencies(Set<Binding<?>> set, Set<Binding<?>> set2) {
        set2.add(this.mLockService);
        set2.add(this.mScheduler);
        set2.add(this.mCodeTypesUtil);
        set2.add(this.supertype);
    }

    public void injectMembers(SecondaryCodeUpdateKeySafePresenter secondaryCodeUpdateKeySafePresenter) {
        secondaryCodeUpdateKeySafePresenter.mLockService = (LockService) this.mLockService.get();
        secondaryCodeUpdateKeySafePresenter.mScheduler = (IScheduler) this.mScheduler.get();
        secondaryCodeUpdateKeySafePresenter.mCodeTypesUtil = (CodeTypesUtil) this.mCodeTypesUtil.get();
        this.supertype.injectMembers(secondaryCodeUpdateKeySafePresenter);
    }
}
