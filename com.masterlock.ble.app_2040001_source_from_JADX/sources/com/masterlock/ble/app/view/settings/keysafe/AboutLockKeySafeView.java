package com.masterlock.ble.app.view.settings.keysafe;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.settings.keysafe.AboutLockKeySafePresenter;
import com.masterlock.ble.app.screens.SettingsScreens.AboutLockKeySafe;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.core.Firmware;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;

public class AboutLockKeySafeView extends LinearLayout implements IAuthenticatedView {
    public ArrayAdapter listAdapter;
    private AboutLockKeySafePresenter mAboutLockKeySafePresenter;
    @InjectView(2131296422)
    TextView mDeviceIdBanner;
    @InjectView(2131296473)
    TextView mFirmwareReleaseDateTextView;
    @InjectView(2131296476)
    TextView mFirmwareTitleTextView;
    @InjectView(2131296474)
    TextView mFirmwareUpdateAvailability;
    @InjectView(2131296341)
    Button mFirmwareUpdateButton;
    @InjectView(2131296475)
    TextView mFirmwareUpdateDescription;
    @InjectView(2131296477)
    TextView mFirmwareVersionTextView;
    private boolean mHasFuFile;
    private Lock mLock;
    @InjectView(2131296594)
    TextView mLockIdTextView;
    @InjectView(2131296597)
    TextView mLockNameBanner;
    @InjectView(2131296627)
    TextView mModelNumberTextView;

    public AboutLockKeySafeView(Context context) {
        this(context, null);
    }

    public AboutLockKeySafeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void displayError(ApiError apiError) {
        Toast.makeText(getContext(), apiError.getMessage(), 1).show();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            AboutLockKeySafe aboutLockKeySafe = (AboutLockKeySafe) AppFlow.getScreen(getContext());
            this.mAboutLockKeySafePresenter = new AboutLockKeySafePresenter(aboutLockKeySafe.mLock, this);
            this.mFirmwareUpdateButton.setVisibility(4);
            this.mLock = aboutLockKeySafe.mLock;
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    public void updateAboutLock(Lock lock) {
        String modelNumber = lock.getModelNumber();
        String deviceId = lock.getKmsDeviceKey().getDeviceId();
        this.mModelNumberTextView.setText(modelNumber);
        this.mLockIdTextView.setText(deviceId);
    }

    public void updateLockFirmwareDetails(Firmware firmware, boolean z) {
        this.mHasFuFile = z;
        if (this.mHasFuFile) {
            this.mFirmwareUpdateAvailability.setText(getResources().getString(C1075R.string.about_firmware_ready_to_install));
            this.mFirmwareUpdateButton.setText(getResources().getString(C1075R.string.btn_download_firmware_label_install));
        } else {
            this.mFirmwareUpdateAvailability.setText(getResources().getString(C1075R.string.about_firmware_new_version_available));
            this.mFirmwareUpdateButton.setText(getResources().getString(C1075R.string.btn_download_firmware_label));
        }
        TextView textView = this.mFirmwareUpdateDescription;
        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(C1075R.string.firmware_description_update_description));
        sb.append("\n");
        sb.append(firmware.lockDescription);
        textView.setText(sb.toString());
        this.mFirmwareUpdateButton.setVisibility(0);
        this.mFirmwareTitleTextView.setText(String.format(getResources().getString(C1075R.string.about_firmware_version), new Object[]{Integer.valueOf(this.mLock.getFirmwareVersion())}));
        this.mFirmwareVersionTextView.setText(String.format(getResources().getString(C1075R.string.about_firmware_version), new Object[]{String.valueOf(firmware.firmwareVersion)}));
        TextView textView2 = this.mFirmwareReleaseDateTextView;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(getResources().getString(C1075R.string.firmware_description_release_date));
        sb2.append(" ");
        sb2.append(String.valueOf(firmware.releaseDate));
        textView2.setText(sb2.toString());
        try {
            String str = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionName;
            int intValue = versionCompare(String.valueOf(firmware.minAndroidBuild), str).intValue();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Compare result: ");
            sb3.append(intValue);
            sb3.append("\tappBuild: ");
            sb3.append(str);
            sb3.append("\tminAndroidBuild: ");
            sb3.append((float) firmware.minAndroidBuild);
            Log.d("ABOUT LOCK VIEW", sb3.toString());
            if (intValue > 0) {
                Log.d("ABOUT LOCK VIEW", "Firmware Update min build greater than app build number");
                this.mFirmwareUpdateAvailability.setText(getResources().getString(C1075R.string.notif_firmware_update_build));
                this.mFirmwareUpdateButton.setVisibility(4);
                return;
            }
            Log.d("ABOUT LOCK VIEW", "Firmware Update min number is equal or lesser than app build number");
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Integer versionCompare(String str, String str2) {
        String[] split = str.split("\\.");
        String[] split2 = str2.split("\\.");
        int i = 0;
        while (i < split.length && i < split2.length && split[i].equals(split2[i])) {
            i++;
        }
        if (i >= split.length || i >= split2.length) {
            return Integer.valueOf(Integer.signum(split.length - split2.length));
        }
        return Integer.valueOf(Integer.signum(Integer.valueOf(split[i]).compareTo(Integer.valueOf(split2[i]))));
    }

    public void setFirmwareTitleText(String str) {
        this.mFirmwareTitleTextView.setText(str);
    }

    public void setFirmwareUptoDateText() {
        this.mFirmwareTitleTextView.setText(String.format(getResources().getString(C1075R.string.about_firmware_version), new Object[]{Integer.valueOf(this.mLock.getFirmwareVersion())}));
        this.mFirmwareUpdateAvailability.setText(getResources().getString(C1075R.string.about_firmware_up_to_date));
    }

    public void setLockName(String str) {
        this.mLockNameBanner.setText(String.format(getResources().getString(C1075R.string.about_lock_name), new Object[]{str}));
    }

    public void setDeviceId(String str) {
        this.mDeviceIdBanner.setText(str);
    }

    public void setmFirmwareUpdateAvailability(int i) {
        this.mFirmwareUpdateAvailability.setText(getResources().getString(i));
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296341})
    public void updateFirmware() {
        AboutLockKeySafe aboutLockKeySafe = (AboutLockKeySafe) AppFlow.getScreen(getContext());
        if (!this.mHasFuFile) {
            this.mAboutLockKeySafePresenter.goToDownloadFirmwareView();
        } else if (this.mLock.isLockerMode()) {
            this.mAboutLockKeySafePresenter.displayLockermodeDialog();
        } else {
            this.mAboutLockKeySafePresenter.goToInstallView();
        }
    }
}
