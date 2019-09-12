package com.masterlock.ble.app.view.guest;

import android.support.p003v7.widget.SwitchCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class GrantAccessView$$ViewInjector {
    public static void inject(Finder finder, final GrantAccessView grantAccessView, Object obj) {
        grantAccessView.grantAccessSpecificContainer = (RelativeLayout) finder.findRequiredView(obj, C1075R.C1077id.grant_access_specific_container, "field 'grantAccessSpecificContainer'");
        grantAccessView.invitationBanner = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.invitation_banner, "field 'invitationBanner'");
        grantAccessView.invitationExpiresIn = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_expires_in, "field 'invitationExpiresIn'");
        grantAccessView.shareAccessButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.btn_send_access, "field 'shareAccessButton'");
        grantAccessView.buttonBar = finder.findRequiredView(obj, C1075R.C1077id.button_bar, "field 'buttonBar'");
        grantAccessView.bannerTitle = (TextView) finder.findRequiredView(obj, C1075R.C1077id.invitation_batter_title, "field 'bannerTitle'");
        grantAccessView.txtCoOwner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_co_owner, "field 'txtCoOwner'");
        grantAccessView.toggleCoOwner = (SwitchCompat) finder.findRequiredView(obj, C1075R.C1077id.toggle_co_owner, "field 'toggleCoOwner'");
        grantAccessView.radioUnlimited = (RadioButton) finder.findRequiredView(obj, C1075R.C1077id.radio_unlimited, "field 'radioUnlimited'");
        grantAccessView.radioDay = (RadioButton) finder.findRequiredView(obj, C1075R.C1077id.radio_day, "field 'radioDay'");
        grantAccessView.radioNight = (RadioButton) finder.findRequiredView(obj, C1075R.C1077id.radio_night, "field 'radioNight'");
        grantAccessView.txtLockName = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_name_banner, "field 'txtLockName'");
        grantAccessView.txtDeviceId = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id_banner, "field 'txtDeviceId'");
        grantAccessView.txtGuestName = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_guest_name, "field 'txtGuestName'");
        grantAccessView.accessTimeContainer = (ViewGroup) finder.findRequiredView(obj, C1075R.C1077id.access_time_container, "field 'accessTimeContainer'");
        grantAccessView.accessDayContainer = (ViewGroup) finder.findRequiredView(obj, C1075R.C1077id.access_days_container, "field 'accessDayContainer'");
        grantAccessView.bTSelectionMode = (TextView) finder.findRequiredView(obj, C1075R.C1077id.bt_selection_mode, "field 'bTSelectionMode'");
        grantAccessView.rBImmediately = (RadioButton) finder.findRequiredView(obj, C1075R.C1077id.radio_inmediately, "field 'rBImmediately'");
        grantAccessView.rBNeverExpires = (RadioButton) finder.findRequiredView(obj, C1075R.C1077id.radio_never, "field 'rBNeverExpires'");
        grantAccessView.rBStartsAt = (RadioButton) finder.findRequiredView(obj, C1075R.C1077id.radio_starts_at, "field 'rBStartsAt'");
        grantAccessView.rBExpiresAt = (RadioButton) finder.findRequiredView(obj, C1075R.C1077id.radio_expires_at, "field 'rBExpiresAt'");
        grantAccessView.disabledFrameDateLeft = (FrameLayout) finder.findRequiredView(obj, C1075R.C1077id.disabled_frame_date_left, "field 'disabledFrameDateLeft'");
        grantAccessView.disabledFrameDateRight = (FrameLayout) finder.findRequiredView(obj, C1075R.C1077id.disabled_frame_date_right, "field 'disabledFrameDateRight'");
        grantAccessView.leftDateContainer = finder.findRequiredView(obj, C1075R.C1077id.left_date_container, "field 'leftDateContainer'");
        grantAccessView.rightDateContainer = finder.findRequiredView(obj, C1075R.C1077id.right_date_container, "field 'rightDateContainer'");
        grantAccessView.tVStartDate = (TextView) finder.findRequiredView(obj, C1075R.C1077id.tv_start_date, "field 'tVStartDate'");
        grantAccessView.tVSTartTime = (TextView) finder.findRequiredView(obj, C1075R.C1077id.tv_start_time, "field 'tVSTartTime'");
        grantAccessView.tVExpireDate = (TextView) finder.findRequiredView(obj, C1075R.C1077id.tv_expires_date, "field 'tVExpireDate'");
        grantAccessView.tVExpireTime = (TextView) finder.findRequiredView(obj, C1075R.C1077id.tv_expires_time, "field 'tVExpireTime'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.iv_expires_calendar, "field 'iVExpireCalendar' and method 'clickExpireCalendar'");
        grantAccessView.iVExpireCalendar = (ImageView) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                grantAccessView.clickExpireCalendar();
            }
        });
        grantAccessView.tVAccessTimeZone = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_access_time, "field 'tVAccessTimeZone'");
        finder.findRequiredView(obj, C1075R.C1077id.iv_start_calendar, "method 'clickStartCalendar'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                grantAccessView.clickStartCalendar();
            }
        });
        grantAccessView.accessDaysCheckBoxList = Finder.listOf((CheckBox) finder.findRequiredView(obj, C1075R.C1077id.cb_sunday, "accessDaysCheckBoxList"), (CheckBox) finder.findRequiredView(obj, C1075R.C1077id.cb_monday, "accessDaysCheckBoxList"), (CheckBox) finder.findRequiredView(obj, C1075R.C1077id.cb_tuesday, "accessDaysCheckBoxList"), (CheckBox) finder.findRequiredView(obj, C1075R.C1077id.cb_wednesday, "accessDaysCheckBoxList"), (CheckBox) finder.findRequiredView(obj, C1075R.C1077id.cb_thursday, "accessDaysCheckBoxList"), (CheckBox) finder.findRequiredView(obj, C1075R.C1077id.cb_friday, "accessDaysCheckBoxList"), (CheckBox) finder.findRequiredView(obj, C1075R.C1077id.cb_saturday, "accessDaysCheckBoxList"));
        grantAccessView.permissionsSwitchList = Finder.listOf((SwitchCompat) finder.findRequiredView(obj, C1075R.C1077id.switch_open_shackle, "permissionsSwitchList"), (SwitchCompat) finder.findRequiredView(obj, C1075R.C1077id.switch_view_last_known_location, "permissionsSwitchList"), (SwitchCompat) finder.findRequiredView(obj, C1075R.C1077id.switch_view_temporary_code, "permissionsSwitchList"));
        grantAccessView.accessHoursRadioButtonList = Finder.listOf((RadioButton) finder.findRequiredView(obj, C1075R.C1077id.radio_unlimited, "accessHoursRadioButtonList"), (RadioButton) finder.findRequiredView(obj, C1075R.C1077id.radio_day, "accessHoursRadioButtonList"), (RadioButton) finder.findRequiredView(obj, C1075R.C1077id.radio_night, "accessHoursRadioButtonList"));
    }

    public static void reset(GrantAccessView grantAccessView) {
        grantAccessView.grantAccessSpecificContainer = null;
        grantAccessView.invitationBanner = null;
        grantAccessView.invitationExpiresIn = null;
        grantAccessView.shareAccessButton = null;
        grantAccessView.buttonBar = null;
        grantAccessView.bannerTitle = null;
        grantAccessView.txtCoOwner = null;
        grantAccessView.toggleCoOwner = null;
        grantAccessView.radioUnlimited = null;
        grantAccessView.radioDay = null;
        grantAccessView.radioNight = null;
        grantAccessView.txtLockName = null;
        grantAccessView.txtDeviceId = null;
        grantAccessView.txtGuestName = null;
        grantAccessView.accessTimeContainer = null;
        grantAccessView.accessDayContainer = null;
        grantAccessView.bTSelectionMode = null;
        grantAccessView.rBImmediately = null;
        grantAccessView.rBNeverExpires = null;
        grantAccessView.rBStartsAt = null;
        grantAccessView.rBExpiresAt = null;
        grantAccessView.disabledFrameDateLeft = null;
        grantAccessView.disabledFrameDateRight = null;
        grantAccessView.leftDateContainer = null;
        grantAccessView.rightDateContainer = null;
        grantAccessView.tVStartDate = null;
        grantAccessView.tVSTartTime = null;
        grantAccessView.tVExpireDate = null;
        grantAccessView.tVExpireTime = null;
        grantAccessView.iVExpireCalendar = null;
        grantAccessView.tVAccessTimeZone = null;
        grantAccessView.accessDaysCheckBoxList = null;
        grantAccessView.permissionsSwitchList = null;
        grantAccessView.accessHoursRadioButtonList = null;
    }
}
