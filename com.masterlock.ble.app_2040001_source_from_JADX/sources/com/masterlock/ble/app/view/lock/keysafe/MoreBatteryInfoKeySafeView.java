package com.masterlock.ble.app.view.lock.keysafe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.lock.keysafe.MoreBatteryInfoKeySafePresenter;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.ble.app.view.nav.MasterLockWebView;

public class MoreBatteryInfoKeySafeView extends LinearLayout implements IAuthenticatedView {
    @InjectView(2131296628)
    MasterLockWebView mHelpWebView;
    MoreBatteryInfoKeySafePresenter mMoreBatteryInfoKeySafePresenter;

    public MoreBatteryInfoKeySafeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            this.mMoreBatteryInfoKeySafePresenter = new MoreBatteryInfoKeySafePresenter(this);
            ButterKnife.inject((View) this);
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mMoreBatteryInfoKeySafePresenter.start();
            this.mHelpWebView.loadUrl(getResources().getString(C1075R.string.more_battery_info_keysafe_url));
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mMoreBatteryInfoKeySafePresenter.finish();
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }
}
