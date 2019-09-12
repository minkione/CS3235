package com.masterlock.ble.app.view.lock.padlock;

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

public class LockDetailsPadLockView$$ViewInjector {
    public static void inject(Finder finder, final LockDetailsPadLockView lockDetailsPadLockView, Object obj) {
        lockDetailsPadLockView.map = (FrameLayout) finder.findOptionalView(obj, C1075R.C1077id.map_pad_lock);
        View findOptionalView = finder.findOptionalView(obj, C1075R.C1077id.noLocationText);
        lockDetailsPadLockView.noLocationText = (TextView) findOptionalView;
        if (findOptionalView != null) {
            findOptionalView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    lockDetailsPadLockView.goToInfoLocation();
                }
            });
        }
        lockDetailsPadLockView.deviceId = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id, "field 'deviceId'");
        lockDetailsPadLockView.mHeader = (RelativeLayout) finder.findRequiredView(obj, C1075R.C1077id.header_container, "field 'mHeader'");
        lockDetailsPadLockView.mLockState = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_lock_state, "field 'mLockState'");
        lockDetailsPadLockView.mLockLastRecord = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_last_record, "field 'mLockLastRecord'");
        lockDetailsPadLockView.mInstructions = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_instructions, "field 'mInstructions'");
        lockDetailsPadLockView.mRelockTime = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_relock_time, "field 'mRelockTime'");
        lockDetailsPadLockView.mRelockUnit = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_relock_unit, "field 'mRelockUnit'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.battery_container, "field 'mBatteryContainer'");
        lockDetailsPadLockView.mBatteryContainer = (LinearLayout) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                lockDetailsPadLockView.goToBatteryDetail();
            }
        });
        lockDetailsPadLockView.mBattery = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_battery_percent, "field 'mBattery'");
        lockDetailsPadLockView.mBatteryIndicator = (ImageView) finder.findRequiredView(obj, C1075R.C1077id.battery_indicator, "field 'mBatteryIndicator'");
        lockDetailsPadLockView.mRelock = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_relock_time_config, "field 'mRelock'");
        lockDetailsPadLockView.mAdditionalDetailsContainer = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.additional_details_container, "field 'mAdditionalDetailsContainer'");
        lockDetailsPadLockView.mLockCodeContainer = (LinearLayout) finder.findOptionalView(obj, C1075R.C1077id.primary_lock_code_container);
        View findOptionalView2 = finder.findOptionalView(obj, C1075R.C1077id.btn_primary_code);
        lockDetailsPadLockView.mBtnPrimaryCode = (Button) findOptionalView2;
        if (findOptionalView2 != null) {
            findOptionalView2.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    lockDetailsPadLockView.gotoPrimaryCode();
                }
            });
        }
        View findOptionalView3 = finder.findOptionalView(obj, C1075R.C1077id.btn_add_guest);
        lockDetailsPadLockView.mBtnAddGuest = (Button) findOptionalView3;
        if (findOptionalView3 != null) {
            findOptionalView3.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    lockDetailsPadLockView.gotoAddGuest();
                }
            });
        }
        View findOptionalView4 = finder.findOptionalView(obj, C1075R.C1077id.btn_all_guests);
        lockDetailsPadLockView.mBtnAllGuests = (Button) findOptionalView4;
        if (findOptionalView4 != null) {
            findOptionalView4.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    lockDetailsPadLockView.gotoAllGuestsList();
                }
            });
        }
        View findOptionalView5 = finder.findOptionalView(obj, C1075R.C1077id.location_disabled);
        lockDetailsPadLockView.mGpsPermissionIV = (ImageView) findOptionalView5;
        if (findOptionalView5 != null) {
            findOptionalView5.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    lockDetailsPadLockView.locationDisabled();
                }
            });
        }
        View findOptionalView6 = finder.findOptionalView(obj, C1075R.C1077id.btn_clear);
        lockDetailsPadLockView.btnClear = (Button) findOptionalView6;
        if (findOptionalView6 != null) {
            findOptionalView6.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    lockDetailsPadLockView.clear();
                }
            });
        }
        lockDetailsPadLockView.mGuestsContainer = (LinearLayout) finder.findOptionalView(obj, C1075R.C1077id.guests_container);
        lockDetailsPadLockView.mHistoryContainer = (LinearLayout) finder.findOptionalView(obj, C1075R.C1077id.history_container);
        View findOptionalView7 = finder.findOptionalView(obj, C1075R.C1077id.btn_history);
        lockDetailsPadLockView.mBtnHistory = (Button) findOptionalView7;
        if (findOptionalView7 != null) {
            findOptionalView7.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    lockDetailsPadLockView.gotoHistoryList();
                }
            });
        }
        lockDetailsPadLockView.lockerModeApplied = (TextView) finder.findRequiredView(obj, C1075R.C1077id.locker_mode_applied, "field 'lockerModeApplied'");
        lockDetailsPadLockView.mButtonBar = (RelativeLayout) finder.findRequiredView(obj, C1075R.C1077id.button_bar, "field 'mButtonBar'");
        View findRequiredView2 = finder.findRequiredView(obj, C1075R.C1077id.btn_locker_mode, "field 'mBtnLockerMode' and method 'toggleLockerMode'");
        lockDetailsPadLockView.mBtnLockerMode = (Button) findRequiredView2;
        findRequiredView2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                lockDetailsPadLockView.toggleLockerMode();
            }
        });
        lockDetailsPadLockView.mServiceCodeCard = (CardView) finder.findOptionalView(obj, C1075R.C1077id.service_code_card);
        lockDetailsPadLockView.guestLocationContainer = (LinearLayout) finder.findOptionalView(obj, C1075R.C1077id.guest_last_location_container);
        lockDetailsPadLockView.mAccessWindowRestrictedCard = (CardView) finder.findOptionalView(obj, C1075R.C1077id.access_window_restriction_card);
        lockDetailsPadLockView.mAccessWindowRestricted = (TextView) finder.findOptionalView(obj, C1075R.C1077id.access_window_restricted);
        lockDetailsPadLockView.mRollingCodeContainer = (LinearLayout) finder.findOptionalView(obj, C1075R.C1077id.rolling_code_container);
        lockDetailsPadLockView.mRollingCodeExpiration = (TextView) finder.findOptionalView(obj, C1075R.C1077id.rolling_code_expiration);
        lockDetailsPadLockView.mRollingInstructions = (TextView) finder.findOptionalView(obj, C1075R.C1077id.txt_rolling_instructions);
        lockDetailsPadLockView.mOwnerEmail = (TextView) finder.findOptionalView(obj, C1075R.C1077id.txt_owner_email);
        finder.findRequiredView(obj, C1075R.C1077id.relock_time_container, "method 'showRelockTime'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                lockDetailsPadLockView.showRelockTime();
            }
        });
        lockDetailsPadLockView.mGuestListViews = Finder.listOf(finder.findOptionalView(obj, C1075R.C1077id.guests_divider1), finder.findOptionalView(obj, C1075R.C1077id.guests_container), finder.findOptionalView(obj, C1075R.C1077id.guests_divider2), finder.findOptionalView(obj, C1075R.C1077id.btn_all_guests));
    }

    public static void reset(LockDetailsPadLockView lockDetailsPadLockView) {
        lockDetailsPadLockView.map = null;
        lockDetailsPadLockView.noLocationText = null;
        lockDetailsPadLockView.deviceId = null;
        lockDetailsPadLockView.mHeader = null;
        lockDetailsPadLockView.mLockState = null;
        lockDetailsPadLockView.mLockLastRecord = null;
        lockDetailsPadLockView.mInstructions = null;
        lockDetailsPadLockView.mRelockTime = null;
        lockDetailsPadLockView.mRelockUnit = null;
        lockDetailsPadLockView.mBatteryContainer = null;
        lockDetailsPadLockView.mBattery = null;
        lockDetailsPadLockView.mBatteryIndicator = null;
        lockDetailsPadLockView.mRelock = null;
        lockDetailsPadLockView.mAdditionalDetailsContainer = null;
        lockDetailsPadLockView.mLockCodeContainer = null;
        lockDetailsPadLockView.mBtnPrimaryCode = null;
        lockDetailsPadLockView.mBtnAddGuest = null;
        lockDetailsPadLockView.mBtnAllGuests = null;
        lockDetailsPadLockView.mGpsPermissionIV = null;
        lockDetailsPadLockView.btnClear = null;
        lockDetailsPadLockView.mGuestsContainer = null;
        lockDetailsPadLockView.mHistoryContainer = null;
        lockDetailsPadLockView.mBtnHistory = null;
        lockDetailsPadLockView.lockerModeApplied = null;
        lockDetailsPadLockView.mButtonBar = null;
        lockDetailsPadLockView.mBtnLockerMode = null;
        lockDetailsPadLockView.mServiceCodeCard = null;
        lockDetailsPadLockView.guestLocationContainer = null;
        lockDetailsPadLockView.mAccessWindowRestrictedCard = null;
        lockDetailsPadLockView.mAccessWindowRestricted = null;
        lockDetailsPadLockView.mRollingCodeContainer = null;
        lockDetailsPadLockView.mRollingCodeExpiration = null;
        lockDetailsPadLockView.mRollingInstructions = null;
        lockDetailsPadLockView.mOwnerEmail = null;
        lockDetailsPadLockView.mGuestListViews = null;
    }
}
