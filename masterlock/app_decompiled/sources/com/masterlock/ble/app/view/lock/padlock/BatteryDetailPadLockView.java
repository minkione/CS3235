package com.masterlock.ble.app.view.lock.padlock;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.analytics.Analytics;
import com.masterlock.ble.app.presenter.lock.padlock.BatteryDetailPadLockPresenter;
import com.masterlock.ble.app.screens.LockScreens.BatteryDetailPadLock;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;

public class BatteryDetailPadLockView extends LinearLayout implements IAuthenticatedView {
    BatteryDetailPadLockPresenter mBatteryDetailPadLockPresenter;
    @InjectView(2131296865)
    TextView mHowToChange;
    @InjectView(2131296891)
    TextView mWhereIsBattery;

    public BatteryDetailPadLockView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mBatteryDetailPadLockPresenter = new BatteryDetailPadLockPresenter(this);
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mBatteryDetailPadLockPresenter.start();
            Lock lock = ((BatteryDetailPadLock) AppFlow.getScreen(getContext())).mLock;
            if (lock.getModelNumber() != null && lock.getModelNumber().contains("4401")) {
                this.mWhereIsBattery.setText(getResources().getText(C1075R.string.where_is_battery_description_outdoor));
                this.mHowToChange.setText(getResources().getText(C1075R.string.how_do_i_change_description_outdoor));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mBatteryDetailPadLockPresenter.finish();
    }

    @OnClick({2131296345})
    public void showBatteryInfoWebView() {
        MasterLockApp.get().getAnalytics().logEvent(Analytics.CATEGORY_MASTERLOCK_EVENT, Analytics.ACTION_EXTERNAL_LINK, Analytics.ACTION_EXTERNAL_LINK, 1);
        this.mBatteryDetailPadLockPresenter.showBatteryInfoWebView();
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }
}
