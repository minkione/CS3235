package com.masterlock.ble.app.view.lock.keysafe;

import android.support.p003v7.widget.CardView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class LockDetailsKeySafeView$$ViewInjector {
    public static void inject(Finder finder, final LockDetailsKeySafeView lockDetailsKeySafeView, Object obj) {
        lockDetailsKeySafeView.map = (FrameLayout) finder.findOptionalView(obj, C1075R.C1077id.map_key_safe);
        lockDetailsKeySafeView.guesTempCodeContainer = (LinearLayout) finder.findOptionalView(obj, C1075R.C1077id.guest_temporary_code_container);
        lockDetailsKeySafeView.guestLocationContainer = (LinearLayout) finder.findOptionalView(obj, C1075R.C1077id.guest_last_location_container);
        View findOptionalView = finder.findOptionalView(obj, C1075R.C1077id.noLocationText);
        lockDetailsKeySafeView.noLocationText = (TextView) findOptionalView;
        if (findOptionalView != null) {
            findOptionalView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    lockDetailsKeySafeView.goToInfoLocation();
                }
            });
        }
        lockDetailsKeySafeView.deviceId = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id, "field 'deviceId'");
        lockDetailsKeySafeView.mHeader = (RelativeLayout) finder.findRequiredView(obj, C1075R.C1077id.header_container, "field 'mHeader'");
        lockDetailsKeySafeView.mLockState = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_lock_state, "field 'mLockState'");
        lockDetailsKeySafeView.mLockLastRecord = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_last_record, "field 'mLockLastRecord'");
        lockDetailsKeySafeView.mInstructions = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_instructions, "field 'mInstructions'");
        lockDetailsKeySafeView.mRelockTime = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_relock_time, "field 'mRelockTime'");
        lockDetailsKeySafeView.mRelockUnit = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_relock_unit, "field 'mRelockUnit'");
        View findOptionalView2 = finder.findOptionalView(obj, C1075R.C1077id.remove_shackle_container);
        lockDetailsKeySafeView.mRemoveShackleContainer = (LinearLayout) findOptionalView2;
        if (findOptionalView2 != null) {
            findOptionalView2.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    lockDetailsKeySafeView.unlockShackle();
                }
            });
        }
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.battery_container, "field 'mBatteryContainer'");
        lockDetailsKeySafeView.mBatteryContainer = (LinearLayout) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                lockDetailsKeySafeView.goToBatteryDetail();
            }
        });
        lockDetailsKeySafeView.mBattery = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_battery_percent, "field 'mBattery'");
        lockDetailsKeySafeView.mBatteryIndicator = (ImageView) finder.findRequiredView(obj, C1075R.C1077id.battery_indicator, "field 'mBatteryIndicator'");
        lockDetailsKeySafeView.mRelock = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_relock_time_config, "field 'mRelock'");
        lockDetailsKeySafeView.mAdditionalDetailsContainer = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.additional_details_container, "field 'mAdditionalDetailsContainer'");
        lockDetailsKeySafeView.mLockCodeContainer = (LinearLayout) finder.findOptionalView(obj, C1075R.C1077id.primary_lock_code_container);
        lockDetailsKeySafeView.mPrimaryCodeInstructions = (TextView) finder.findOptionalView(obj, C1075R.C1077id.txtKeySafeInsertPrimaryCodeInstructions);
        lockDetailsKeySafeView.mSecondaryLockCodeContainer = (LinearLayout) finder.findOptionalView(obj, C1075R.C1077id.secondary_lock_code_container);
        View findOptionalView3 = finder.findOptionalView(obj, C1075R.C1077id.btn_primary_code);
        lockDetailsKeySafeView.mBtnPrimaryCode = (Button) findOptionalView3;
        if (findOptionalView3 != null) {
            findOptionalView3.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    lockDetailsKeySafeView.gotoPrimaryCode();
                }
            });
        }
        View findOptionalView4 = finder.findOptionalView(obj, C1075R.C1077id.btn_secondary_codes);
        lockDetailsKeySafeView.mBtnSecondaryCodes = (Button) findOptionalView4;
        if (findOptionalView4 != null) {
            findOptionalView4.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    lockDetailsKeySafeView.goToChangeSecondaryCodes();
                }
            });
        }
        View findOptionalView5 = finder.findOptionalView(obj, C1075R.C1077id.btn_add_guest);
        lockDetailsKeySafeView.mBtnAddGuest = (Button) findOptionalView5;
        if (findOptionalView5 != null) {
            findOptionalView5.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    lockDetailsKeySafeView.gotoAddGuest();
                }
            });
        }
        View findOptionalView6 = finder.findOptionalView(obj, C1075R.C1077id.btn_all_guests);
        lockDetailsKeySafeView.mBtnAllGuests = (Button) findOptionalView6;
        if (findOptionalView6 != null) {
            findOptionalView6.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    lockDetailsKeySafeView.gotoAllGuestsList();
                }
            });
        }
        View findOptionalView7 = finder.findOptionalView(obj, C1075R.C1077id.location_disabled);
        lockDetailsKeySafeView.mGpsPermissionIV = (ImageView) findOptionalView7;
        if (findOptionalView7 != null) {
            findOptionalView7.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    lockDetailsKeySafeView.locationDisabled();
                }
            });
        }
        View findOptionalView8 = finder.findOptionalView(obj, C1075R.C1077id.btn_clear);
        lockDetailsKeySafeView.btnClear = (Button) findOptionalView8;
        if (findOptionalView8 != null) {
            findOptionalView8.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    lockDetailsKeySafeView.clear();
                }
            });
        }
        lockDetailsKeySafeView.mGuestsContainer = (LinearLayout) finder.findOptionalView(obj, C1075R.C1077id.guests_container);
        lockDetailsKeySafeView.mHistoryContainer = (LinearLayout) finder.findOptionalView(obj, C1075R.C1077id.history_container);
        View findOptionalView9 = finder.findOptionalView(obj, C1075R.C1077id.btn_history);
        lockDetailsKeySafeView.mBtnHistory = (Button) findOptionalView9;
        if (findOptionalView9 != null) {
            findOptionalView9.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    lockDetailsKeySafeView.gotoHistoryList();
                }
            });
        }
        lockDetailsKeySafeView.lockerModeApplied = (TextView) finder.findRequiredView(obj, C1075R.C1077id.locker_mode_applied, "field 'lockerModeApplied'");
        lockDetailsKeySafeView.mButtonBar = (RelativeLayout) finder.findRequiredView(obj, C1075R.C1077id.button_bar, "field 'mButtonBar'");
        View findRequiredView2 = finder.findRequiredView(obj, C1075R.C1077id.btn_locker_mode, "field 'mBtnLockerMode' and method 'toggleLockerMode'");
        lockDetailsKeySafeView.mBtnLockerMode = (Button) findRequiredView2;
        findRequiredView2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                lockDetailsKeySafeView.toggleLockerMode();
            }
        });
        lockDetailsKeySafeView.mServiceCodeCard = (CardView) finder.findOptionalView(obj, C1075R.C1077id.service_code_card);
        lockDetailsKeySafeView.mAccessWindowRestrictedCard = (CardView) finder.findOptionalView(obj, C1075R.C1077id.access_window_restriction_card);
        lockDetailsKeySafeView.mAccessWindowRestricted = (TextView) finder.findOptionalView(obj, C1075R.C1077id.access_window_restricted);
        lockDetailsKeySafeView.mRollingCodeContainer = (LinearLayout) finder.findOptionalView(obj, C1075R.C1077id.rolling_code_container);
        lockDetailsKeySafeView.mRollingCodeExpiration = (TextView) finder.findOptionalView(obj, C1075R.C1077id.rolling_code_expiration);
        lockDetailsKeySafeView.mRollingInstructions = (TextView) finder.findOptionalView(obj, C1075R.C1077id.txt_rolling_instructions);
        lockDetailsKeySafeView.mOwnerEmail = (TextView) finder.findOptionalView(obj, C1075R.C1077id.txt_owner_email);
        lockDetailsKeySafeView.tVSecondaryCodesInstructions = (TextView) finder.findOptionalView(obj, C1075R.C1077id.txtKeySafeInsertSecondaryKeyInstructions);
        lockDetailsKeySafeView.tvRemoveShackle = (TextView) finder.findOptionalView(obj, C1075R.C1077id.tv_removeShackle);
        finder.findRequiredView(obj, C1075R.C1077id.relock_time_container, "method 'showRelockTime'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                lockDetailsKeySafeView.showRelockTime();
            }
        });
        lockDetailsKeySafeView.mGuestListViews = Finder.listOf(finder.findOptionalView(obj, C1075R.C1077id.guests_divider1), finder.findOptionalView(obj, C1075R.C1077id.guests_container), finder.findOptionalView(obj, C1075R.C1077id.guests_divider2), finder.findOptionalView(obj, C1075R.C1077id.btn_all_guests));
    }

    public static void reset(LockDetailsKeySafeView lockDetailsKeySafeView) {
        lockDetailsKeySafeView.map = null;
        lockDetailsKeySafeView.guesTempCodeContainer = null;
        lockDetailsKeySafeView.guestLocationContainer = null;
        lockDetailsKeySafeView.noLocationText = null;
        lockDetailsKeySafeView.deviceId = null;
        lockDetailsKeySafeView.mHeader = null;
        lockDetailsKeySafeView.mLockState = null;
        lockDetailsKeySafeView.mLockLastRecord = null;
        lockDetailsKeySafeView.mInstructions = null;
        lockDetailsKeySafeView.mRelockTime = null;
        lockDetailsKeySafeView.mRelockUnit = null;
        lockDetailsKeySafeView.mRemoveShackleContainer = null;
        lockDetailsKeySafeView.mBatteryContainer = null;
        lockDetailsKeySafeView.mBattery = null;
        lockDetailsKeySafeView.mBatteryIndicator = null;
        lockDetailsKeySafeView.mRelock = null;
        lockDetailsKeySafeView.mAdditionalDetailsContainer = null;
        lockDetailsKeySafeView.mLockCodeContainer = null;
        lockDetailsKeySafeView.mPrimaryCodeInstructions = null;
        lockDetailsKeySafeView.mSecondaryLockCodeContainer = null;
        lockDetailsKeySafeView.mBtnPrimaryCode = null;
        lockDetailsKeySafeView.mBtnSecondaryCodes = null;
        lockDetailsKeySafeView.mBtnAddGuest = null;
        lockDetailsKeySafeView.mBtnAllGuests = null;
        lockDetailsKeySafeView.mGpsPermissionIV = null;
        lockDetailsKeySafeView.btnClear = null;
        lockDetailsKeySafeView.mGuestsContainer = null;
        lockDetailsKeySafeView.mHistoryContainer = null;
        lockDetailsKeySafeView.mBtnHistory = null;
        lockDetailsKeySafeView.lockerModeApplied = null;
        lockDetailsKeySafeView.mButtonBar = null;
        lockDetailsKeySafeView.mBtnLockerMode = null;
        lockDetailsKeySafeView.mServiceCodeCard = null;
        lockDetailsKeySafeView.mAccessWindowRestrictedCard = null;
        lockDetailsKeySafeView.mAccessWindowRestricted = null;
        lockDetailsKeySafeView.mRollingCodeContainer = null;
        lockDetailsKeySafeView.mRollingCodeExpiration = null;
        lockDetailsKeySafeView.mRollingInstructions = null;
        lockDetailsKeySafeView.mOwnerEmail = null;
        lockDetailsKeySafeView.tVSecondaryCodesInstructions = null;
        lockDetailsKeySafeView.tvRemoveShackle = null;
        lockDetailsKeySafeView.mGuestListViews = null;
    }
}
