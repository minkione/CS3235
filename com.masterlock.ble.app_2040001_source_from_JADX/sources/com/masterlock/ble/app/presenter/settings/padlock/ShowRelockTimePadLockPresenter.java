package com.masterlock.ble.app.presenter.settings.padlock;

import com.masterlock.ble.app.bus.UpdateToolbarEvent.Builder;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.SettingsScreens.AdjustRelockTimePadLock;
import com.masterlock.ble.app.screens.SettingsScreens.ShowRelockTimePadLock;
import com.masterlock.ble.app.view.settings.padlock.ShowRelockTimePadLockView;
import com.masterlock.core.AccessType;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;

public class ShowRelockTimePadLockPresenter extends AuthenticatedPresenter<Lock, ShowRelockTimePadLockView> {
    public ShowRelockTimePadLockPresenter(ShowRelockTimePadLockView showRelockTimePadLockView) {
        super(showRelockTimePadLockView);
        this.model = ((ShowRelockTimePadLock) AppFlow.getScreen(showRelockTimePadLockView.getContext())).mLock;
    }

    public void start() {
        super.start();
        this.mEventBus.post(new Builder(((ShowRelockTimePadLockView) this.view).getResources()).build());
        ((ShowRelockTimePadLockView) this.view).updateView(((Lock) this.model).getRelockTimeInSeconds());
        if (((Lock) this.model).getAccessType() == AccessType.GUEST) {
            ((ShowRelockTimePadLockView) this.view).displayButtonBar(false);
        }
    }

    public void goToAdjustRelockTime() {
        AppFlow.get(((ShowRelockTimePadLockView) this.view).getContext()).goTo(new AdjustRelockTimePadLock((Lock) this.model));
    }
}
