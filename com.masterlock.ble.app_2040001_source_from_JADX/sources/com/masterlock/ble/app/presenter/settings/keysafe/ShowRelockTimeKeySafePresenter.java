package com.masterlock.ble.app.presenter.settings.keysafe;

import com.masterlock.ble.app.bus.UpdateToolbarEvent.Builder;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.SettingsScreens.AdjustRelockTimeKeySafe;
import com.masterlock.ble.app.screens.SettingsScreens.ShowRelockTimeKeySafe;
import com.masterlock.ble.app.view.settings.keysafe.ShowRelockTimeKeySafeView;
import com.masterlock.core.AccessType;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;

public class ShowRelockTimeKeySafePresenter extends AuthenticatedPresenter<Lock, ShowRelockTimeKeySafeView> {
    public ShowRelockTimeKeySafePresenter(ShowRelockTimeKeySafeView showRelockTimeKeySafeView) {
        super(showRelockTimeKeySafeView);
        this.model = ((ShowRelockTimeKeySafe) AppFlow.getScreen(showRelockTimeKeySafeView.getContext())).mLock;
    }

    public void start() {
        super.start();
        this.mEventBus.post(new Builder(((ShowRelockTimeKeySafeView) this.view).getResources()).build());
        ((ShowRelockTimeKeySafeView) this.view).updateView(((Lock) this.model).getRelockTimeInSeconds());
        if (((Lock) this.model).getAccessType() == AccessType.GUEST) {
            ((ShowRelockTimeKeySafeView) this.view).displayButtonBar(false);
        }
    }

    public void goToAdjustRelockTime() {
        AppFlow.get(((ShowRelockTimeKeySafeView) this.view).getContext()).goTo(new AdjustRelockTimeKeySafe((Lock) this.model));
    }
}
