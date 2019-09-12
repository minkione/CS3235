package com.masterlock.ble.app.screens;

import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.screens.LockScreens.DialSpeedDetails;
import com.masterlock.ble.app.screens.LockScreens.ILockDetails;
import com.masterlock.ble.app.screens.LockScreens.LockDetailsKeySafe;
import com.masterlock.ble.app.screens.LockScreens.LockDetailsPadLock;
import com.masterlock.core.FirmwareUpdate;
import com.masterlock.core.Lock;
import flow.HasParent;
import flow.Layout;

public class SettingsScreens {

    @Layout(2131427449)
    public static class AboutLockKeySafe extends AppScreen implements HasParent<LockSettings> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.about_lock_title;
        }

        public AboutLockKeySafe(Lock lock) {
            this.mLock = lock;
        }

        public LockSettings getParent() {
            return new LockSettings(this.mLock);
        }
    }

    @Layout(2131427450)
    public static class AboutLockPadLock extends AppScreen implements HasParent<LockSettings> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.about_lock_title;
        }

        public AboutLockPadLock(Lock lock) {
            this.mLock = lock;
        }

        public LockSettings getParent() {
            return new LockSettings(this.mLock);
        }
    }

    @Layout(2131427363)
    public static class AdjustRelockTimeKeySafe extends AppScreen implements HasParent<ShowRelockTimeKeySafe> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_adjust_relock_time;
        }

        public AdjustRelockTimeKeySafe(Lock lock) {
            this.mLock = lock;
        }

        public ShowRelockTimeKeySafe getParent() {
            return new ShowRelockTimeKeySafe(this.mLock);
        }
    }

    @Layout(2131427364)
    public static class AdjustRelockTimePadLock extends AppScreen implements HasParent<ShowRelockTimePadLock> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_adjust_relock_time;
        }

        public AdjustRelockTimePadLock(Lock lock) {
            this.mLock = lock;
        }

        public ShowRelockTimePadLock getParent() {
            return new ShowRelockTimePadLock(this.mLock);
        }
    }

    @Layout(2131427374)
    public static class BackupMasterCombinationKeySafe extends AppScreen implements HasParent<LockSettings> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_backup_master_combination;
        }

        public BackupMasterCombinationKeySafe(Lock lock) {
            this.mLock = lock;
        }

        public LockSettings getParent() {
            return new LockSettings(this.mLock);
        }
    }

    @Layout(2131427375)
    public static class BackupMasterCombinationPadLock extends AppScreen implements HasParent<LockSettings> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_backup_master_combination;
        }

        public BackupMasterCombinationPadLock(Lock lock) {
            this.mLock = lock;
        }

        public LockSettings getParent() {
            return new LockSettings(this.mLock);
        }
    }

    @Layout(2131427451)
    public static class CalibrateLock extends AppScreen implements MultiParent {
        public final boolean fromAddLock;
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.calibrate_title;
        }

        public CalibrateLock(Lock lock) {
            this.mLock = lock;
            this.fromAddLock = false;
        }

        public CalibrateLock(Lock lock, boolean z) {
            this.mLock = lock;
            this.fromAddLock = z;
        }
    }

    @Layout(2131427403)
    public static class DownloadFirmwareUpdateKeySafe extends AppScreen implements HasParent<LockSettings> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_firmware_update;
        }

        public DownloadFirmwareUpdateKeySafe(Lock lock) {
            this.mLock = lock;
        }

        public LockSettings getParent() {
            return new LockSettings(this.mLock);
        }
    }

    @Layout(2131427404)
    public static class DownloadFirmwareUpdatePadLock extends AppScreen implements HasParent<LockSettings> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_firmware_update;
        }

        public DownloadFirmwareUpdatePadLock(Lock lock) {
            this.mLock = lock;
        }

        public LockSettings getParent() {
            return new LockSettings(this.mLock);
        }
    }

    @Layout(2131427423)
    public static class InstallFirmwareUpdateKeySafe extends AppScreen implements HasParent<LockSettings> {
        public final FirmwareUpdate mFirmware;
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_firmware_update;
        }

        public InstallFirmwareUpdateKeySafe(Lock lock, FirmwareUpdate firmwareUpdate) {
            this.mLock = lock;
            this.mFirmware = firmwareUpdate;
        }

        public LockSettings getParent() {
            return new LockSettings(this.mLock);
        }
    }

    @Layout(2131427424)
    public static class InstallFirmwareUpdatePadLock extends AppScreen implements HasParent<LockSettings> {
        public final FirmwareUpdate mFirmware;
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_firmware_update;
        }

        public InstallFirmwareUpdatePadLock(Lock lock, FirmwareUpdate firmwareUpdate) {
            this.mLock = lock;
            this.mFirmware = firmwareUpdate;
        }

        public LockSettings getParent() {
            return new LockSettings(this.mLock);
        }
    }

    @Layout(2131427452)
    public static class LockNameKeySafe extends AppScreen implements HasParent<LockSettings> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.lock_name_label;
        }

        public LockNameKeySafe(Lock lock) {
            this.mLock = lock;
        }

        public LockSettings getParent() {
            return new LockSettings(this.mLock);
        }
    }

    @Layout(2131427453)
    public static class LockNamePadLock extends AppScreen implements HasParent<LockSettings> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.lock_name_label;
        }

        public LockNamePadLock(Lock lock) {
            this.mLock = lock;
        }

        public LockSettings getParent() {
            return new LockSettings(this.mLock);
        }
    }

    @Layout(2131427454)
    public static class LockNotesKeySafe extends AppScreen implements HasParent<LockSettings> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.lock_notes_title;
        }

        public LockNotesKeySafe(Lock lock) {
            this.mLock = lock;
        }

        public LockSettings getParent() {
            return new LockSettings(this.mLock);
        }
    }

    @Layout(2131427455)
    public static class LockNotesPadLock extends AppScreen implements HasParent<LockSettings> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.lock_notes_title;
        }

        public LockNotesPadLock(Lock lock) {
            this.mLock = lock;
        }

        public LockSettings getParent() {
            return new LockSettings(this.mLock);
        }
    }

    @Layout(2131427460)
    public static class LockSettings extends AppScreen implements HasParent<ILockDetails> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_lock_settings;
        }

        public LockSettings(Lock lock) {
            this.mLock = lock;
        }

        public ILockDetails getParent() {
            if (this.mLock.isDialSpeedLock() || this.mLock.isBiometricPadLock()) {
                return new DialSpeedDetails(this.mLock);
            }
            return this.mLock.isPadLock() ? new LockDetailsPadLock(this.mLock, false) : new LockDetailsKeySafe(this.mLock, false);
        }
    }

    @Layout(2131427456)
    public static class LockTimezoneKeySafe extends AppScreen implements HasParent<LockSettings> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_lock_settings_timezone;
        }

        public LockTimezoneKeySafe(Lock lock) {
            this.mLock = lock;
        }

        public LockSettings getParent() {
            return new LockSettings(this.mLock);
        }
    }

    @Layout(2131427457)
    public static class LockTimezonePadLock extends AppScreen implements HasParent<LockSettings> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_lock_settings_timezone;
        }

        public LockTimezonePadLock(Lock lock) {
            this.mLock = lock;
        }

        public LockSettings getParent() {
            return new LockSettings(this.mLock);
        }
    }

    @Layout(2131427458)
    public static class ResetKeysKeySafe extends AppScreen implements HasParent<LockSettings> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.reset_keys_label;
        }

        public ResetKeysKeySafe(Lock lock) {
            this.mLock = lock;
        }

        public LockSettings getParent() {
            return new LockSettings(this.mLock);
        }
    }

    @Layout(2131427459)
    public static class ResetKeysPadLock extends AppScreen implements HasParent<LockSettings> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.reset_keys_label;
        }

        public ResetKeysPadLock(Lock lock) {
            this.mLock = lock;
        }

        public LockSettings getParent() {
            return new LockSettings(this.mLock);
        }
    }

    @Layout(2131427512)
    public static class ShareTemporaryCodes extends AppScreen implements MultiParent {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_share_temporary_code;
        }

        public ShareTemporaryCodes(Lock lock) {
            this.mLock = lock;
        }
    }

    @Layout(2131427513)
    public static class ShowRelockTimeKeySafe extends AppScreen implements HasParent<LockDetailsKeySafe> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_adjust_relock_time;
        }

        public ShowRelockTimeKeySafe(Lock lock) {
            this.mLock = lock;
        }

        public LockDetailsKeySafe getParent() {
            return new LockDetailsKeySafe(this.mLock, false);
        }
    }

    @Layout(2131427514)
    public static class ShowRelockTimePadLock extends AppScreen implements HasParent<LockDetailsPadLock> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_adjust_relock_time;
        }

        public ShowRelockTimePadLock(Lock lock) {
            this.mLock = lock;
        }

        public LockDetailsPadLock getParent() {
            return new LockDetailsPadLock(this.mLock, false);
        }
    }

    @Layout(2131427525)
    public static class UnlockModeListKeySafe extends AppScreen implements HasParent<LockSettings> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_unlock_mode;
        }

        public UnlockModeListKeySafe(Lock lock) {
            this.mLock = lock;
        }

        public LockSettings getParent() {
            return new LockSettings(this.mLock);
        }
    }

    @Layout(2131427526)
    public static class UnlockModeListPadLock extends AppScreen implements HasParent<LockSettings> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_unlock_mode;
        }

        public UnlockModeListPadLock(Lock lock) {
            this.mLock = lock;
        }

        public LockSettings getParent() {
            return new LockSettings(this.mLock);
        }
    }
}
