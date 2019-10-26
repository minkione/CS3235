package com.masterlock.ble.app.view.settings.padlock;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.settings.padlock.BackupMasterCodePadLockPresenter;
import com.masterlock.ble.app.view.IAuthenticatedView;

public class BackupMasterCodePadLockView extends LinearLayout implements IAuthenticatedView {
    private BackupMasterCodePadLockPresenter mBackupMasterCodePadLockPresenter;
    @InjectView(2131296597)
    TextView mLockNameBanner;
    @InjectView(2131296422)
    TextView textDeviceId;

    public BackupMasterCodePadLockView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mBackupMasterCodePadLockPresenter = new BackupMasterCodePadLockPresenter(this);
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mBackupMasterCodePadLockPresenter.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mBackupMasterCodePadLockPresenter.finish();
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    public void displayError(Throwable th) {
        Toast.makeText(getContext(), th.getMessage() != null ? th.getMessage() : th.getClass().getSimpleName(), 0).show();
    }

    public void setLockName(String str) {
        this.mLockNameBanner.setText(getResources().getString(C1075R.string.about_lock_name, new Object[]{str}));
    }

    public void setDeviceId(String str) {
        this.textDeviceId.setText(str);
    }
}
