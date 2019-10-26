package com.masterlock.ble.app.screens;

import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.screens.SettingsScreens.LockSettings;
import com.masterlock.core.Lock;
import com.masterlock.core.LockConfigAction;
import com.masterlock.core.SecondaryCodeIndex;
import com.masterlock.core.SecondaryCodesUtil;
import flow.HasParent;
import flow.Layout;

public class LockScreens {

    @Layout(2131427434)
    public static class AddLock extends AppScreen implements ILockDetails, HasParent<LockList> {
        public int getTitleResourceId() {
            return C1075R.string.title_add_lock;
        }

        public LockList getParent() {
            return new LockList();
        }
    }

    @Layout(2131427433)
    public static class AddMechanicalLock extends AppScreen implements HasParent<ILockDetails> {
        boolean isEditing;
        Lock lock;

        public AddMechanicalLock(Lock lock2) {
            this.lock = lock2;
            this.isEditing = lock2 != null;
        }

        public ILockDetails getParent() {
            return this.isEditing ? new GenericLock(this.lock) : new AddLock();
        }

        public int getTitleResourceId() {
            return this.isEditing ? C1075R.string.edit_lock_title : C1075R.string.title_add_lock;
        }

        public Lock getLock() {
            return this.lock;
        }
    }

    @Layout(2131427372)
    public static class ApplyChanges extends AppScreen implements HasParent<LockSettings> {
        public final Lock mLock;
        public final LockConfigAction mLockConfigAction;
        private final int mTitleResourceId;

        public ApplyChanges(Lock lock, int i, LockConfigAction lockConfigAction) {
            this.mLock = lock;
            this.mTitleResourceId = i;
            this.mLockConfigAction = lockConfigAction;
        }

        public int getTitleResourceId() {
            return this.mTitleResourceId;
        }

        public LockSettings getParent() {
            return new LockSettings(this.mLock);
        }
    }

    @Layout(2131427377)
    public static class BatteryDetailKeySafe extends AppScreen implements HasParent<LockDetailsKeySafe> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_battery_details;
        }

        public BatteryDetailKeySafe(Lock lock) {
            this.mLock = lock;
        }

        public LockDetailsKeySafe getParent() {
            return new LockDetailsKeySafe(this.mLock, false);
        }
    }

    @Layout(2131427378)
    public static class BatteryDetailPadLock extends AppScreen implements HasParent<LockDetailsPadLock> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_battery_details;
        }

        public BatteryDetailPadLock(Lock lock) {
            this.mLock = lock;
        }

        public LockDetailsPadLock getParent() {
            return new LockDetailsPadLock(this.mLock, false);
        }
    }

    @Layout(2131427391)
    public static class ChangeSecondaryCodesKeySafe extends AppScreen implements HasParent<LockDetailsKeySafe> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_secondary_codes;
        }

        public ChangeSecondaryCodesKeySafe(Lock lock) {
            this.mLock = lock;
        }

        public LockDetailsKeySafe getParent() {
            return new LockDetailsKeySafe(this.mLock, false);
        }
    }

    @Layout(2131427400)
    public static class DialSpeedDetails extends AppScreen implements ILockDetails, HasParent<LockList> {
        Lock lock;

        public int getMenuResourceId() {
            return C1075R.C1078menu.lock_detail_menu;
        }

        public int getTitleResourceId() {
            return C1075R.string.lock_details;
        }

        public DialSpeedDetails(Lock lock2) {
            this.lock = lock2;
        }

        public LockList getParent() {
            return new LockList();
        }

        public Lock getLock() {
            return this.lock;
        }
    }

    @Layout(2131427401)
    public static class EditDialSpeedCodes extends AppScreen implements HasParent<DialSpeedDetails> {
        Lock lock;

        public int getTitleResourceId() {
            return C1075R.string.edit_codes;
        }

        public EditDialSpeedCodes(Lock lock2) {
            this.lock = lock2;
        }

        public DialSpeedDetails getParent() {
            return new DialSpeedDetails(this.lock);
        }

        public Lock getLock() {
            return this.lock;
        }
    }

    @Layout(2131427413)
    public static class GenericLock extends AppScreen implements ILockDetails, HasParent<LockList> {
        public final Lock mLock;

        public int getMenuResourceId() {
            return C1075R.C1078menu.generic_lock_menu;
        }

        public int getTitleResourceId() {
            return C1075R.string.lock_details;
        }

        public GenericLock(Lock lock) {
            this.mLock = lock;
        }

        public LockList getParent() {
            return new LockList();
        }
    }

    @Layout(2131427421)
    public static class HistoryKeySafe extends AppScreen implements HasParent<LockDetailsKeySafe> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_histories_list;
        }

        public HistoryKeySafe(Lock lock) {
            this.mLock = lock;
        }

        public LockDetailsKeySafe getParent() {
            return new LockDetailsKeySafe(this.mLock, false);
        }
    }

    @Layout(2131427422)
    public static class HistoryPadLock extends AppScreen implements HasParent<LockDetailsPadLock> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_histories_list;
        }

        public HistoryPadLock(Lock lock) {
            this.mLock = lock;
        }

        public LockDetailsPadLock getParent() {
            return new LockDetailsPadLock(this.mLock, false);
        }
    }

    public interface ILockDetails {
    }

    @Layout(2131427429)
    public static class LastLocationInfoKeySafe extends AppScreen implements HasParent<LockDetailsKeySafe> {
        public final Lock mLock;

        public int getMenuResourceId() {
            return C1075R.C1078menu.last_location_menu;
        }

        public int getTitleResourceId() {
            return C1075R.string.title_last_location;
        }

        public LastLocationInfoKeySafe(Lock lock) {
            this.mLock = lock;
        }

        public LockDetailsKeySafe getParent() {
            return new LockDetailsKeySafe(this.mLock, false);
        }
    }

    @Layout(2131427430)
    public static class LastLocationInfoPadLock extends AppScreen implements HasParent<LockDetailsPadLock> {
        public final Lock mLock;

        public int getMenuResourceId() {
            return C1075R.C1078menu.last_location_menu;
        }

        public int getTitleResourceId() {
            return C1075R.string.title_last_location;
        }

        public LastLocationInfoPadLock(Lock lock) {
            this.mLock = lock;
        }

        public LockDetailsPadLock getParent() {
            return new LockDetailsPadLock(this.mLock, false);
        }
    }

    @Layout(2131427437)
    public static class LockDetailsKeySafe extends AppScreen implements ILockDetails, HasParent<LockLanding> {
        public boolean mEnterUpdateScreen;
        public boolean mFromNotification;
        public final Lock mLock;

        public int getMenuResourceId() {
            return C1075R.C1078menu.lock_detail_menu;
        }

        public LockDetailsKeySafe(Lock lock, boolean z) {
            this.mLock = lock;
            SecondaryCodesUtil.setUtilForLock(lock);
            this.mFromNotification = z;
            if (z) {
                this.mEnterUpdateScreen = true;
            }
        }

        public LockLanding getParent() {
            return new LockLanding(this.mLock);
        }
    }

    @Layout(2131427438)
    public static class LockDetailsPadLock extends AppScreen implements ILockDetails, HasParent<LockLanding> {
        public boolean mEnterUpdateScreen;
        public boolean mFromNotification;
        public final Lock mLock;

        public int getMenuResourceId() {
            return C1075R.C1078menu.lock_detail_menu;
        }

        public LockDetailsPadLock(Lock lock, boolean z) {
            this.mLock = lock;
            this.mFromNotification = z;
            if (z) {
                this.mEnterUpdateScreen = true;
            }
        }

        public LockLanding getParent() {
            return new LockLanding(this.mLock);
        }
    }

    @Layout(2131427443)
    public static class LockLanding extends AppScreen implements HasParent<LockList> {
        public boolean mCalibration = false;
        public Lock mLock;

        public int getMenuResourceId() {
            return C1075R.C1078menu.lock_landing_menu;
        }

        public int getTitleResourceId() {
            return C1075R.string.empty_string;
        }

        public LockLanding(Lock lock) {
            this.mLock = lock;
        }

        public LockLanding(Lock lock, boolean z) {
            this.mLock = lock;
            this.mCalibration = z;
        }

        public LockList getParent() {
            return new LockList();
        }
    }

    @Layout(2131427445)
    public static class LockList extends AppScreen {
        public String mAction;
        public String mLockId;

        public int getMenuResourceId() {
            return C1075R.C1078menu.locks_menu;
        }

        public int getTitleResourceId() {
            return C1075R.string.title_locks;
        }

        public LockList() {
        }

        public LockList(String str, String str2) {
            this.mAction = str;
            this.mLockId = str2;
        }
    }

    @Layout(2131427465)
    public static class MoreBatteryInfoKeySafe extends AppScreen implements HasParent<BatteryDetailKeySafe> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_more_battery_info;
        }

        public MoreBatteryInfoKeySafe(Lock lock) {
            this.mLock = lock;
        }

        public BatteryDetailKeySafe getParent() {
            return new BatteryDetailKeySafe(this.mLock);
        }
    }

    @Layout(2131427466)
    public static class MoreBatteryInfoPadLock extends AppScreen implements HasParent<BatteryDetailPadLock> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_more_battery_info;
        }

        public MoreBatteryInfoPadLock(Lock lock) {
            this.mLock = lock;
        }

        public BatteryDetailPadLock getParent() {
            return new BatteryDetailPadLock(this.mLock);
        }
    }

    @Layout(2131427490)
    public static class PrimaryCodeUpdateKeySafe extends AppScreen implements HasParent<LockDetailsKeySafe> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_primary_code;
        }

        public PrimaryCodeUpdateKeySafe(Lock lock) {
            this.mLock = lock;
        }

        public LockDetailsKeySafe getParent() {
            return new LockDetailsKeySafe(this.mLock, false);
        }
    }

    @Layout(2131427491)
    public static class PrimaryCodeUpdatePadLock extends AppScreen implements HasParent<LockDetailsPadLock> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_update_primary_code;
        }

        public PrimaryCodeUpdatePadLock(Lock lock) {
            this.mLock = lock;
        }

        public LockDetailsPadLock getParent() {
            return new LockDetailsPadLock(this.mLock, false);
        }
    }

    @Layout(2131427502)
    public static class SecondaryCodeUpdateKeySafe extends AppScreen implements HasParent<ChangeSecondaryCodesKeySafe> {
        public final boolean mFromLockDetails;
        public final SecondaryCodeIndex mIndex;
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.title_update_secondary_code;
        }

        public SecondaryCodeUpdateKeySafe(Lock lock, boolean z, SecondaryCodeIndex secondaryCodeIndex) {
            this.mLock = lock;
            this.mFromLockDetails = z;
            this.mIndex = secondaryCodeIndex;
        }

        public ChangeSecondaryCodesKeySafe getParent() {
            return new ChangeSecondaryCodesKeySafe(this.mLock);
        }
    }

    @Layout(2131427462)
    public static class Steps extends AppScreen implements HasParent<LockList> {
        public final Lock mLock;

        public int getMenuResourceId() {
            return 0;
        }

        public int getTitleResourceId() {
            return C1075R.string.firmware_update_instructions_title;
        }

        public Steps(Lock lock) {
            this.mLock = lock;
        }

        public LockList getParent() {
            return new LockList();
        }
    }

    @Layout(2131427527)
    public static class UnlockShackle extends AppScreen implements HasParent<LockDetailsKeySafe> {
        public final Lock mLock;

        public int getTitleResourceId() {
            return C1075R.string.remove_shackle_title;
        }

        public UnlockShackle(Lock lock) {
            this.mLock = lock;
        }

        public LockDetailsKeySafe getParent() {
            return new LockDetailsKeySafe(this.mLock, false);
        }
    }

    private LockScreens() {
    }
}
