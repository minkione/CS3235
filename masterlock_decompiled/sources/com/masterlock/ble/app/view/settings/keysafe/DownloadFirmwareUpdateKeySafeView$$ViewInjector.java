package com.masterlock.ble.app.view.settings.keysafe;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.gamma.FillImageProgressBar;

public class DownloadFirmwareUpdateKeySafeView$$ViewInjector {
    public static void inject(Finder finder, final DownloadFirmwareUpdateKeySafeView downloadFirmwareUpdateKeySafeView, Object obj) {
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.btn_install_update, "field 'mInstallUpdateButton' and method 'updateLock'");
        downloadFirmwareUpdateKeySafeView.mInstallUpdateButton = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                downloadFirmwareUpdateKeySafeView.updateLock();
            }
        });
        downloadFirmwareUpdateKeySafeView.mFirmwareDownloadStatus = (TextView) finder.findRequiredView(obj, C1075R.C1077id.firmware_download_status, "field 'mFirmwareDownloadStatus'");
        downloadFirmwareUpdateKeySafeView.mProgressBar = (FillImageProgressBar) finder.findRequiredView(obj, C1075R.C1077id.firmware_download_progress_bar, "field 'mProgressBar'");
    }

    public static void reset(DownloadFirmwareUpdateKeySafeView downloadFirmwareUpdateKeySafeView) {
        downloadFirmwareUpdateKeySafeView.mInstallUpdateButton = null;
        downloadFirmwareUpdateKeySafeView.mFirmwareDownloadStatus = null;
        downloadFirmwareUpdateKeySafeView.mProgressBar = null;
    }
}
