package com.masterlock.ble.app.view.settings.keysafe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.settings.keysafe.BackupMasterCodeKeySafePresenter;
import com.masterlock.ble.app.screens.SettingsScreens.BackupMasterCombinationKeySafe;
import com.masterlock.ble.app.util.ArrowsCodeUtil;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.square.flow.appflow.AppFlow;

public class BackupMasterCodeKeySafeView extends LinearLayout implements IAuthenticatedView {
    @InjectView(2131296310)
    LinearLayout backupMasterCodeContainer;
    @InjectView(2131296311)
    TextView mBackupCodeInstructions;
    private BackupMasterCodeKeySafePresenter mBackupMasterCodeKeySafePresenter;
    @InjectView(2131296597)
    TextView mLockNameBanner;
    @InjectView(2131296422)
    TextView textDeviceId;

    public BackupMasterCodeKeySafeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mBackupMasterCodeKeySafePresenter = new BackupMasterCodeKeySafePresenter(this);
            if (!((BackupMasterCombinationKeySafe) AppFlow.getScreen(getContext())).mLock.isShackledKeySafe()) {
                this.mBackupCodeInstructions.setVisibility(8);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mBackupMasterCodeKeySafePresenter.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mBackupMasterCodeKeySafePresenter.finish();
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    public void displayError(Throwable th) {
        Toast.makeText(getContext(), th.getMessage() != null ? th.getMessage() : th.getClass().getSimpleName(), 0).show();
    }

    public void setLockName(String str) {
        this.mLockNameBanner.setText(String.format(getResources().getString(C1075R.string.about_lock_name), new Object[]{str}));
    }

    public void setDeviceId(String str) {
        this.textDeviceId.setText(str);
    }

    public void displayBackupMasterCode(String str) {
        new ArrowsCodeUtil().showMasterCode(str, this.backupMasterCodeContainer, getContext());
    }
}
