package com.masterlock.ble.app.view.settings.padlock;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.gamma.FillImageProgressBar;
import com.masterlock.ble.app.presenter.settings.padlock.DownloadFirmwareUpdatePadLockPresenter;
import com.masterlock.ble.app.screens.SettingsScreens.DownloadFirmwareUpdatePadLock;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.square.flow.appflow.AppFlow;

public class DownloadFirmwareUpdatePadLockView extends LinearLayout implements IAuthenticatedView {
    private DownloadFirmwareUpdatePadLockPresenter mDownloadFirmwareUpdatePadLockPresenter;
    @InjectView(2131296472)
    TextView mFirmwareDownloadStatus;
    @InjectView(2131296343)
    Button mInstallUpdateButton;
    @InjectView(2131296471)
    FillImageProgressBar mProgressBar;

    public DownloadFirmwareUpdatePadLockView(Context context) {
        super(context);
    }

    public DownloadFirmwareUpdatePadLockView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DownloadFirmwareUpdatePadLockView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            this.mDownloadFirmwareUpdatePadLockPresenter = new DownloadFirmwareUpdatePadLockPresenter(((DownloadFirmwareUpdatePadLock) AppFlow.getScreen(getContext())).mLock, this);
            ButterKnife.inject((View) this);
            this.mInstallUpdateButton.setVisibility(4);
            this.mProgressBar.setProgressMax(10);
            this.mProgressBar.setIndeterminate(true);
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mDownloadFirmwareUpdatePadLockPresenter.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mDownloadFirmwareUpdatePadLockPresenter.finish();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296343})
    public void updateLock() {
        this.mDownloadFirmwareUpdatePadLockPresenter.goToInstallView();
    }

    public void updateViewSuccess() {
        this.mInstallUpdateButton.setVisibility(0);
        this.mFirmwareDownloadStatus.setText(getResources().getString(C1075R.string.firmware_downloaded_label));
    }

    public void updateViewError() {
        this.mInstallUpdateButton.setVisibility(4);
        this.mFirmwareDownloadStatus.setText(getResources().getString(C1075R.string.firmware_downloaded_label_error));
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    public void stopProgress() {
        this.mProgressBar.fillToMax();
    }
}
