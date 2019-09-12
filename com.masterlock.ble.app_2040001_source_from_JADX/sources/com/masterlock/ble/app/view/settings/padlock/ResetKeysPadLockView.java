package com.masterlock.ble.app.view.settings.padlock;

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
import com.masterlock.ble.app.presenter.settings.padlock.ResetKeysPadLockPresenter;
import com.masterlock.ble.app.view.IAuthenticatedView;

public class ResetKeysPadLockView extends LinearLayout implements IAuthenticatedView {
    @InjectView(2131296597)
    TextView mLockNameBanner;
    private ResetKeysPadLockPresenter mResetKeysPadLockPresenter;
    @InjectView(2131296422)
    TextView textDeviceId;

    public ResetKeysPadLockView(Context context) {
        this(context, null);
    }

    public ResetKeysPadLockView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mResetKeysPadLockPresenter = new ResetKeysPadLockPresenter(this);
            this.mResetKeysPadLockPresenter.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mResetKeysPadLockPresenter.finish();
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    @OnClick({2131296348})
    public void onSaveClicked() {
        this.mResetKeysPadLockPresenter.resetKeys();
    }

    public void setLockName(String str) {
        this.mLockNameBanner.setText(getResources().getString(C1075R.string.about_lock_name, new Object[]{str}));
    }

    public void setDeviceId(String str) {
        this.textDeviceId.setText(str);
    }
}
