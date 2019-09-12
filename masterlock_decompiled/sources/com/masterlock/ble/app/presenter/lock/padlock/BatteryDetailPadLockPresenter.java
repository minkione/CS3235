package com.masterlock.ble.app.presenter.lock.padlock;

import android.view.View;
import android.widget.ImageView;
import butterknife.ButterKnife;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.bus.UpdateToolbarEvent.Builder;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.LockScreens.BatteryDetailPadLock;
import com.masterlock.ble.app.screens.LockScreens.MoreBatteryInfoPadLock;
import com.masterlock.ble.app.view.lock.padlock.BatteryDetailPadLockView;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;

public class BatteryDetailPadLockPresenter extends AuthenticatedPresenter<Lock, BatteryDetailPadLockView> {
    ImageView mBatteryIndicator;

    public BatteryDetailPadLockPresenter(BatteryDetailPadLockView batteryDetailPadLockView) {
        super(batteryDetailPadLockView);
        this.model = ((BatteryDetailPadLock) AppFlow.getScreen(batteryDetailPadLockView.getContext())).mLock;
    }

    public void start() {
        super.start();
        this.mEventBus.post(new Builder(((BatteryDetailPadLockView) this.view).getResources()).build());
        this.mBatteryIndicator = (ImageView) ButterKnife.findById((View) this.view, (int) C1075R.C1077id.battery_indicator);
        setBatteryPercentage();
    }

    private void setBatteryPercentage() {
        if (((Lock) this.model).getRemainingBatteryPercentage() <= ((BatteryDetailPadLockView) this.view).getResources().getInteger(C1075R.integer.low_battery_percentage)) {
            this.mBatteryIndicator.setImageDrawable(((BatteryDetailPadLockView) this.view).getResources().getDrawable(C1075R.C1076drawable.ic_battery_info_red));
        } else {
            this.mBatteryIndicator.setImageDrawable(((BatteryDetailPadLockView) this.view).getResources().getDrawable(C1075R.C1076drawable.ic_battery_info_white));
        }
    }

    public void showBatteryInfoWebView() {
        AppFlow.get(((BatteryDetailPadLockView) this.view).getContext()).goTo(new MoreBatteryInfoPadLock((Lock) this.model));
    }
}
