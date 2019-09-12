package com.masterlock.ble.app.presenter.lock.keysafe;

import android.view.View;
import android.widget.ImageView;
import butterknife.ButterKnife;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.bus.UpdateToolbarEvent.Builder;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.LockScreens.BatteryDetailKeySafe;
import com.masterlock.ble.app.screens.LockScreens.MoreBatteryInfoKeySafe;
import com.masterlock.ble.app.view.lock.keysafe.BatteryDetailKeySafeView;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;

public class BatteryDetailKeySafePresenter extends AuthenticatedPresenter<Lock, BatteryDetailKeySafeView> {
    ImageView mBatteryIndicator;

    public BatteryDetailKeySafePresenter(BatteryDetailKeySafeView batteryDetailKeySafeView) {
        super(batteryDetailKeySafeView);
        this.model = ((BatteryDetailKeySafe) AppFlow.getScreen(batteryDetailKeySafeView.getContext())).mLock;
    }

    public void start() {
        super.start();
        this.mEventBus.post(new Builder(((BatteryDetailKeySafeView) this.view).getResources()).build());
        this.mBatteryIndicator = (ImageView) ButterKnife.findById((View) this.view, (int) C1075R.C1077id.battery_indicator);
        setBatteryPercentage();
    }

    private void setBatteryPercentage() {
        if (((Lock) this.model).getRemainingBatteryPercentage() <= ((BatteryDetailKeySafeView) this.view).getResources().getInteger(C1075R.integer.low_battery_percentage)) {
            this.mBatteryIndicator.setImageDrawable(((BatteryDetailKeySafeView) this.view).getResources().getDrawable(C1075R.C1076drawable.ic_battery_info_red));
        } else {
            this.mBatteryIndicator.setImageDrawable(((BatteryDetailKeySafeView) this.view).getResources().getDrawable(C1075R.C1076drawable.ic_battery_info_white));
        }
    }

    public void showBatteryInfoWebView() {
        AppFlow.get(((BatteryDetailKeySafeView) this.view).getContext()).goTo(new MoreBatteryInfoKeySafe((Lock) this.model));
    }
}
