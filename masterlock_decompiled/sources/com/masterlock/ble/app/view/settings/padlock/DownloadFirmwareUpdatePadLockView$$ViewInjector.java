package com.masterlock.ble.app.view.settings.padlock;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.gamma.FillImageProgressBar;

public class DownloadFirmwareUpdatePadLockView$$ViewInjector {
    public static void inject(Finder finder, final DownloadFirmwareUpdatePadLockView downloadFirmwareUpdatePadLockView, Object obj) {
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.btn_install_update, "field 'mInstallUpdateButton' and method 'updateLock'");
        downloadFirmwareUpdatePadLockView.mInstallUpdateButton = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                downloadFirmwareUpdatePadLockView.updateLock();
            }
        });
        downloadFirmwareUpdatePadLockView.mFirmwareDownloadStatus = (TextView) finder.findRequiredView(obj, C1075R.C1077id.firmware_download_status, "field 'mFirmwareDownloadStatus'");
        downloadFirmwareUpdatePadLockView.mProgressBar = (FillImageProgressBar) finder.findRequiredView(obj, C1075R.C1077id.firmware_download_progress_bar, "field 'mProgressBar'");
    }

    public static void reset(DownloadFirmwareUpdatePadLockView downloadFirmwareUpdatePadLockView) {
        downloadFirmwareUpdatePadLockView.mInstallUpdateButton = null;
        downloadFirmwareUpdatePadLockView.mFirmwareDownloadStatus = null;
        downloadFirmwareUpdatePadLockView.mProgressBar = null;
    }
}
