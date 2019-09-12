package com.masterlock.ble.app.presenter;

import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.squareup.otto.Bus;
import javax.inject.Inject;

public class AuthenticatedPresenter<M, V extends IAuthenticatedView> extends Presenter<M, V> {
    @Inject
    protected Bus mEventBus;

    public void start() {
    }

    public AuthenticatedPresenter(V v) {
        super(v);
        MasterLockApp.get().inject(this);
    }

    public AuthenticatedPresenter(long j, V v) {
        super(j, v);
        MasterLockApp.get().inject(this);
    }

    public AuthenticatedPresenter(M m, V v) {
        super(m, v);
        MasterLockApp.get().inject(this);
    }
}
