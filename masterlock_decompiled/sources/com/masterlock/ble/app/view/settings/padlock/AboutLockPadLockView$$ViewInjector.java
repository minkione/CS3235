package com.masterlock.ble.app.view.settings.padlock;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class AboutLockPadLockView$$ViewInjector {
    public static void inject(Finder finder, final AboutLockPadLockView aboutLockPadLockView, Object obj) {
        aboutLockPadLockView.mModelNumberTextView = (TextView) finder.findRequiredView(obj, C1075R.C1077id.model_number, "field 'mModelNumberTextView'");
        aboutLockPadLockView.mFirmwareTitleTextView = (TextView) finder.findRequiredView(obj, C1075R.C1077id.firmware_update_title, "field 'mFirmwareTitleTextView'");
        aboutLockPadLockView.mFirmwareVersionTextView = (TextView) finder.findRequiredView(obj, C1075R.C1077id.firmware_version, "field 'mFirmwareVersionTextView'");
        aboutLockPadLockView.mFirmwareReleaseDateTextView = (TextView) finder.findRequiredView(obj, C1075R.C1077id.firmware_release_date, "field 'mFirmwareReleaseDateTextView'");
        aboutLockPadLockView.mLockNameBanner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_name_banner, "field 'mLockNameBanner'");
        aboutLockPadLockView.mDeviceIdBanner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id_banner, "field 'mDeviceIdBanner'");
        aboutLockPadLockView.mLockIdTextView = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_id, "field 'mLockIdTextView'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.btn_firmware_update, "field 'mFirmwareUpdateButton' and method 'updateFirmware'");
        aboutLockPadLockView.mFirmwareUpdateButton = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                aboutLockPadLockView.updateFirmware();
            }
        });
        aboutLockPadLockView.mFirmwareUpdateAvailability = (TextView) finder.findRequiredView(obj, C1075R.C1077id.firmware_update_availability, "field 'mFirmwareUpdateAvailability'");
        aboutLockPadLockView.mFirmwareUpdateDescription = (TextView) finder.findRequiredView(obj, C1075R.C1077id.firmware_update_description, "field 'mFirmwareUpdateDescription'");
    }

    public static void reset(AboutLockPadLockView aboutLockPadLockView) {
        aboutLockPadLockView.mModelNumberTextView = null;
        aboutLockPadLockView.mFirmwareTitleTextView = null;
        aboutLockPadLockView.mFirmwareVersionTextView = null;
        aboutLockPadLockView.mFirmwareReleaseDateTextView = null;
        aboutLockPadLockView.mLockNameBanner = null;
        aboutLockPadLockView.mDeviceIdBanner = null;
        aboutLockPadLockView.mLockIdTextView = null;
        aboutLockPadLockView.mFirmwareUpdateButton = null;
        aboutLockPadLockView.mFirmwareUpdateAvailability = null;
        aboutLockPadLockView.mFirmwareUpdateDescription = null;
    }
}
