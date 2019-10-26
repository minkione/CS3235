package com.masterlock.ble.app.view.settings.keysafe;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class AboutLockKeySafeView$$ViewInjector {
    public static void inject(Finder finder, final AboutLockKeySafeView aboutLockKeySafeView, Object obj) {
        aboutLockKeySafeView.mModelNumberTextView = (TextView) finder.findRequiredView(obj, C1075R.C1077id.model_number, "field 'mModelNumberTextView'");
        aboutLockKeySafeView.mFirmwareTitleTextView = (TextView) finder.findRequiredView(obj, C1075R.C1077id.firmware_update_title, "field 'mFirmwareTitleTextView'");
        aboutLockKeySafeView.mFirmwareVersionTextView = (TextView) finder.findRequiredView(obj, C1075R.C1077id.firmware_version, "field 'mFirmwareVersionTextView'");
        aboutLockKeySafeView.mFirmwareReleaseDateTextView = (TextView) finder.findRequiredView(obj, C1075R.C1077id.firmware_release_date, "field 'mFirmwareReleaseDateTextView'");
        aboutLockKeySafeView.mLockNameBanner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_name_banner, "field 'mLockNameBanner'");
        aboutLockKeySafeView.mDeviceIdBanner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id_banner, "field 'mDeviceIdBanner'");
        aboutLockKeySafeView.mLockIdTextView = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_id, "field 'mLockIdTextView'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.btn_firmware_update, "field 'mFirmwareUpdateButton' and method 'updateFirmware'");
        aboutLockKeySafeView.mFirmwareUpdateButton = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                aboutLockKeySafeView.updateFirmware();
            }
        });
        aboutLockKeySafeView.mFirmwareUpdateAvailability = (TextView) finder.findRequiredView(obj, C1075R.C1077id.firmware_update_availability, "field 'mFirmwareUpdateAvailability'");
        aboutLockKeySafeView.mFirmwareUpdateDescription = (TextView) finder.findRequiredView(obj, C1075R.C1077id.firmware_update_description, "field 'mFirmwareUpdateDescription'");
    }

    public static void reset(AboutLockKeySafeView aboutLockKeySafeView) {
        aboutLockKeySafeView.mModelNumberTextView = null;
        aboutLockKeySafeView.mFirmwareTitleTextView = null;
        aboutLockKeySafeView.mFirmwareVersionTextView = null;
        aboutLockKeySafeView.mFirmwareReleaseDateTextView = null;
        aboutLockKeySafeView.mLockNameBanner = null;
        aboutLockKeySafeView.mDeviceIdBanner = null;
        aboutLockKeySafeView.mLockIdTextView = null;
        aboutLockKeySafeView.mFirmwareUpdateButton = null;
        aboutLockKeySafeView.mFirmwareUpdateAvailability = null;
        aboutLockKeySafeView.mFirmwareUpdateDescription = null;
    }
}
