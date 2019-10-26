package com.masterlock.ble.app.view.guest;

import android.animation.LayoutTransition;
import android.content.Context;
import android.support.p000v4.content.ContextCompat;
import android.support.p003v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.presenter.guest.GrantAccessPresenter;
import com.masterlock.ble.app.util.MLDateUtils;
import com.masterlock.ble.app.util.ViewUtil;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.core.AccessType;
import com.masterlock.core.GuestInterface;
import com.masterlock.core.GuestPermissions;
import com.masterlock.core.Invitation;
import com.masterlock.core.InvitationStatus;
import com.masterlock.core.Lock;
import com.masterlock.core.ScheduleType;
import com.masterlock.core.WeekDay;
import com.square.flow.appflow.AppFlow;
import com.square.flow.screenswitcher.HandlesBack;
import com.square.flow.screenswitcher.HandlesUp;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;

public class GrantAccessView extends LinearLayout implements IAuthenticatedView, HandlesBack, HandlesUp {
    public static final boolean DISABLED = false;
    /* access modifiers changed from: private */
    public final String TAG;
    @InjectView(2131296262)
    ViewGroup accessDayContainer;
    @InjectViews({2131296369, 2131296366, 2131296371, 2131296372, 2131296370, 2131296365, 2131296368})
    List<CheckBox> accessDaysCheckBoxList;
    @InjectViews({2131296676, 2131296670, 2131296674})
    List<RadioButton> accessHoursRadioButtonList;
    @InjectView(2131296263)
    ViewGroup accessTimeContainer;
    /* access modifiers changed from: private */
    public boolean accessTimeImmediately;
    /* access modifiers changed from: private */
    public boolean accessTimeNeverExpires;
    @InjectView(2131296330)
    TextView bTSelectionMode;
    @InjectView(2131296540)
    TextView bannerTitle;
    @InjectView(2131296361)
    View buttonBar;
    @InjectView(2131296435)
    FrameLayout disabledFrameDateLeft;
    @InjectView(2131296436)
    FrameLayout disabledFrameDateRight;
    @InjectView(2131296498)
    RelativeLayout grantAccessSpecificContainer;
    /* access modifiers changed from: private */
    public GuestInterface guestInterface;
    @InjectView(2131296546)
    ImageView iVExpireCalendar;
    @InjectView(2131296539)
    LinearLayout invitationBanner;
    @InjectView(2131296857)
    TextView invitationExpiresIn;
    @InjectView(2131296566)
    View leftDateContainer;
    GrantAccessPresenter mGrantAccessPresenter;
    /* access modifiers changed from: private */
    public ScheduleType mScheduleType;
    /* access modifiers changed from: private */
    public boolean openShacklePermission;
    @InjectViews({2131296785, 2131296786, 2131296787})
    List<SwitchCompat> permissionsSwitchList;
    @InjectView(2131296671)
    RadioButton rBExpiresAt;
    @InjectView(2131296672)
    RadioButton rBImmediately;
    @InjectView(2131296673)
    RadioButton rBNeverExpires;
    @InjectView(2131296675)
    RadioButton rBStartsAt;
    OnCheckedChangeListener radioButtonOnCheckedChangeListener;
    OnCheckedChangeListener radioButtonTimeAccessLeftPanel;
    OnCheckedChangeListener radioButtonTimeAccessRightPanel;
    @InjectView(2131296670)
    RadioButton radioDay;
    @InjectView(2131296674)
    RadioButton radioNight;
    @InjectView(2131296676)
    RadioButton radioUnlimited;
    @InjectView(2131296701)
    View rightDateContainer;
    /* access modifiers changed from: private */
    public Map<WeekDay, Boolean> selectedDaysMap;
    @InjectView(2131296356)
    Button shareAccessButton;
    OnCheckedChangeListener switchOnCheckedChangeListener;
    @InjectView(2131296845)
    TextView tVAccessTimeZone;
    @InjectView(2131296823)
    TextView tVExpireDate;
    @InjectView(2131296824)
    TextView tVExpireTime;
    @InjectView(2131296835)
    TextView tVSTartTime;
    @InjectView(2131296834)
    TextView tVStartDate;
    @InjectView(2131296805)
    SwitchCompat toggleCoOwner;
    @InjectView(2131296850)
    TextView txtCoOwner;
    @InjectView(2131296422)
    TextView txtDeviceId;
    @InjectView(2131296860)
    TextView txtGuestName;
    @InjectView(2131296597)
    TextView txtLockName;
    /* access modifiers changed from: private */
    public boolean viewLastKnownLocationPermission;
    /* access modifiers changed from: private */
    public boolean viewTemporaryCodePermission;

    public GrantAccessView(Context context) {
        this(context, null);
    }

    public GrantAccessView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.TAG = getClass().getSimpleName();
        this.selectedDaysMap = new HashMap();
        this.mScheduleType = ScheduleType.TWENTY_FOUR_SEVEN;
        this.accessTimeImmediately = true;
        this.accessTimeNeverExpires = true;
        this.switchOnCheckedChangeListener = new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                switch (compoundButton.getId()) {
                    case C1075R.C1077id.switch_open_shackle /*2131296785*/:
                        GrantAccessView.this.openShacklePermission = z;
                        return;
                    case C1075R.C1077id.switch_view_last_known_location /*2131296786*/:
                        GrantAccessView.this.viewLastKnownLocationPermission = z;
                        return;
                    case C1075R.C1077id.switch_view_temporary_code /*2131296787*/:
                        GrantAccessView.this.viewTemporaryCodePermission = z;
                        return;
                    default:
                        return;
                }
            }
        };
        this.radioButtonTimeAccessLeftPanel = new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    if (GrantAccessView.this.accessTimeImmediately = compoundButton.getId() == C1075R.C1077id.radio_inmediately) {
                        GrantAccessView.this.rBStartsAt.setChecked(false);
                        if (!GrantAccessView.this.rBImmediately.isChecked()) {
                            GrantAccessView.this.rBImmediately.setChecked(true);
                        }
                        GrantAccessView.this.mGrantAccessPresenter.setStartAtDate(null);
                        GrantAccessView.this.updateStartDate(null);
                        GrantAccessView.this.enableSelectExpireDateControls(true);
                        GrantAccessView.this.enableAccessDaysControls(true);
                        GrantAccessView.this.enableAccessTimeControls(true);
                    } else {
                        GrantAccessView.this.mGrantAccessPresenter.setStartAtDate(null);
                        GrantAccessView.this.updateStartDate(null);
                        if (!GrantAccessView.this.isAccessTimeNeverExpires()) {
                            GrantAccessView.this.mGrantAccessPresenter.setExpireAtDate(null);
                            GrantAccessView.this.updateExpireDate(null);
                        }
                        GrantAccessView.this.rBImmediately.setChecked(false);
                        if (!GrantAccessView.this.rBStartsAt.isChecked()) {
                            GrantAccessView.this.rBStartsAt.setChecked(true);
                        }
                        GrantAccessView.this.enableSelectExpireDateControls(false);
                        if (GrantAccessView.this.rBImmediately.isEnabled()) {
                            GrantAccessView.this.mGrantAccessPresenter.showStartDateSelectionDialog();
                        }
                    }
                    GrantAccessView.this.toggleLeftDatePanel();
                }
            }
        };
        this.radioButtonTimeAccessRightPanel = new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    if (GrantAccessView.this.accessTimeNeverExpires = compoundButton.getId() == C1075R.C1077id.radio_never) {
                        GrantAccessView.this.rBExpiresAt.setChecked(false);
                        if (!GrantAccessView.this.rBNeverExpires.isChecked()) {
                            GrantAccessView.this.rBNeverExpires.setChecked(true);
                        }
                        GrantAccessView.this.mGrantAccessPresenter.setExpireAtDate(null);
                        GrantAccessView.this.updateExpireDate(null);
                        GrantAccessView.this.enableAccessDaysControls(true);
                        GrantAccessView.this.enableAccessTimeControls(true);
                    } else {
                        GrantAccessView.this.mGrantAccessPresenter.setExpireAtDate(null);
                        GrantAccessView.this.updateExpireDate(null);
                        GrantAccessView.this.rBNeverExpires.setChecked(false);
                        if (!GrantAccessView.this.rBExpiresAt.isChecked()) {
                            GrantAccessView.this.rBExpiresAt.setChecked(true);
                        }
                        if (GrantAccessView.this.rBNeverExpires.isEnabled()) {
                            GrantAccessView.this.mGrantAccessPresenter.showExpireDateSelectionDialog();
                        }
                    }
                    GrantAccessView.this.toggleRightDatePanel();
                }
            }
        };
        this.radioButtonOnCheckedChangeListener = new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    int id = compoundButton.getId();
                    if (id == C1075R.C1077id.radio_day) {
                        GrantAccessView.this.mScheduleType = ScheduleType.SEVEN_AM_TO_SEVEN_PM;
                    } else if (id == C1075R.C1077id.radio_night) {
                        GrantAccessView.this.mScheduleType = ScheduleType.SEVEN_PM_TO_SEVEN_AM;
                    } else if (id == C1075R.C1077id.radio_unlimited) {
                        GrantAccessView.this.mScheduleType = ScheduleType.TWENTY_FOUR_SEVEN;
                    }
                    for (RadioButton radioButton : GrantAccessView.this.accessHoursRadioButtonList) {
                        if (radioButton.getId() != compoundButton.getId()) {
                            radioButton.setChecked(false);
                        }
                    }
                    String access$300 = GrantAccessView.this.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("mScheduleType: ");
                    sb.append(GrantAccessView.this.mScheduleType);
                    Log.d(access$300, sb.toString());
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mGrantAccessPresenter = new GrantAccessPresenter(this);
            this.mGrantAccessPresenter.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mGrantAccessPresenter.finish();
        ViewUtil.hideKeyboard(getContext(), this.shareAccessButton.getWindowToken());
    }

    public void updateViewTimeZone(String str) {
        this.tVAccessTimeZone.setText(str);
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    public void displayError(Throwable th) {
        Toast.makeText(getContext(), th.getMessage() != null ? th.getMessage() : th.getClass().getSimpleName(), 0).show();
    }

    public void updateViewWithGuestInfo(String str, String str2, String str3) {
        this.txtGuestName.setText(str3);
        this.txtLockName.setText(str);
        this.txtDeviceId.setText(str2);
    }

    public void updateViewForGrantAccess() {
        this.toggleCoOwner.setOnCheckedChangeListener(this.mGrantAccessPresenter.getToggleCoOwnerListener());
        changeGuestInterfaceMode(GuestInterface.fromKey(MasterLockSharedPreferences.getInstance().getGuestInterfaceSelectionModeForLockModel(this.mGrantAccessPresenter.getModel().getModelNumber())));
    }

    public void updateViewForUpdateAccess(Invitation invitation, Lock lock) {
        if (!invitation.hasAccepted()) {
            if (!InvitationStatus.PENDING.equals(invitation.getStatus()) || invitation.isExpired()) {
                this.bannerTitle.setText(getResources().getString(C1075R.string.invitation_expired));
                this.invitationExpiresIn.setVisibility(8);
                this.invitationBanner.setBackgroundColor(getResources().getColor(C1075R.color.red));
                this.buttonBar.setVisibility(8);
            } else {
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append(getResources().getString(C1075R.string.expires));
                    sb.append(MLDateUtils.parseServerDateFormat(invitation.getExpiresOn(), getResources()));
                    this.invitationExpiresIn.setText(sb.toString());
                    this.buttonBar.setVisibility(0);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            this.invitationBanner.setVisibility(0);
            disableFields();
            recoverControlStateFromInvitation(invitation, lock);
        } else {
            recoverControlStateFromInvitation(invitation, lock);
            disableTimeAndDayContainer();
            this.toggleCoOwner.setOnCheckedChangeListener(this.mGrantAccessPresenter.getToggleCoOwnerListener());
            this.shareAccessButton.setText(getResources().getString(C1075R.string.update_access));
            this.invitationBanner.setVisibility(8);
        }
        updateToggles(invitation);
    }

    public void updateViewAfterCreateInvitation(Invitation invitation, Lock lock) {
        if (invitation != null) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(getResources().getString(C1075R.string.expires));
                sb.append(MLDateUtils.parseServerDateFormat(invitation.getExpiresOn(), getResources()));
                this.invitationExpiresIn.setText(sb.toString());
                this.buttonBar.setVisibility(0);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            this.invitationBanner.setVisibility(0);
            disableFields();
            updateToggles(invitation);
        }
    }

    private void disableFields() {
        this.toggleCoOwner.setEnabled(false);
        enableSelectionModeControl(false);
        enableSpecificTimeControls(false);
        enableAccessTimeControls(false);
        enableAccessDaysControls(false);
        enablePermissionControls(false);
        enableCalendarModalControls(false);
    }

    public void updateToggles(Invitation invitation) {
        if (invitation != null) {
            this.toggleCoOwner.setChecked(AccessType.CO_OWNER.equals(invitation.getAccessType()));
            switch (invitation.getScheduleType()) {
                case SEVEN_AM_TO_SEVEN_PM:
                    this.radioDay.setChecked(true);
                    return;
                case SEVEN_PM_TO_SEVEN_AM:
                    this.radioNight.setChecked(true);
                    return;
                case TWENTY_FOUR_SEVEN:
                    this.radioUnlimited.setChecked(true);
                    return;
                default:
                    Log.d(this.TAG, "updateToggles: invalid Schedule type");
                    return;
            }
        }
    }

    public void viewSetup() {
        if (this.mGrantAccessPresenter.getModel().isShackledKeySafe()) {
            showShacklePermissionControl();
        }
        accessDaysSetup();
        accessTimeSetup();
        for (SwitchCompat onCheckedChangeListener : this.permissionsSwitchList) {
            onCheckedChangeListener.setOnCheckedChangeListener(this.switchOnCheckedChangeListener);
        }
        for (RadioButton onCheckedChangeListener2 : this.accessHoursRadioButtonList) {
            onCheckedChangeListener2.setOnCheckedChangeListener(this.radioButtonOnCheckedChangeListener);
        }
        this.rBImmediately.setOnCheckedChangeListener(this.radioButtonTimeAccessLeftPanel);
        this.rBStartsAt.setOnCheckedChangeListener(this.radioButtonTimeAccessLeftPanel);
        this.rBNeverExpires.setOnCheckedChangeListener(this.radioButtonTimeAccessRightPanel);
        this.rBExpiresAt.setOnCheckedChangeListener(this.radioButtonTimeAccessRightPanel);
        this.bTSelectionMode.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                GrantAccessView grantAccessView = GrantAccessView.this;
                grantAccessView.changeGuestInterfaceMode(grantAccessView.guestInterface == GuestInterface.SIMPLE ? GuestInterface.ADVANCED : GuestInterface.SIMPLE);
            }
        });
        ((RelativeLayout) ButterKnife.findById((View) this, (int) C1075R.C1077id.main_container)).getLayoutTransition().enableTransitionType(4);
        ((RelativeLayout) ButterKnife.findById((View) this, (int) C1075R.C1077id.body_container)).getLayoutTransition().enableTransitionType(4);
        LayoutTransition layoutTransition = ((ViewGroup) ButterKnife.findById((View) this, (int) C1075R.C1077id.date_layout_left)).getLayoutTransition();
        layoutTransition.setStartDelay(2, 0);
        long j = (long) 100;
        layoutTransition.setDuration(j);
        layoutTransition.enableTransitionType(4);
        LayoutTransition layoutTransition2 = ((ViewGroup) ButterKnife.findById((View) this, (int) C1075R.C1077id.date_layout_right)).getLayoutTransition();
        layoutTransition2.setStartDelay(2, 0);
        layoutTransition2.setDuration(j);
        layoutTransition2.enableTransitionType(4);
        this.shareAccessButton.setOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                GrantAccessView.this.mGrantAccessPresenter.validateAndSendRequest();
            }
        });
    }

    private void accessTimeSetup() {
        toggleLeftDatePanel();
        toggleRightDatePanel();
    }

    private void accessDaysSetup() {
        String[] shortWeekdays = new DateFormatSymbols().getShortWeekdays();
        int i = 0;
        while (i < this.accessDaysCheckBoxList.size()) {
            CheckBox checkBox = (CheckBox) this.accessDaysCheckBoxList.get(i);
            int i2 = i + 1;
            checkBox.setText(shortWeekdays[i2]);
            checkBox.setTag(WeekDay.values()[i]);
            checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    GrantAccessView.this.selectedDaysMap.put((WeekDay) compoundButton.getTag(), Boolean.valueOf(z));
                    String access$300 = GrantAccessView.this.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onCheckedChanged: ");
                    sb.append(GrantAccessView.this.selectedDaysMap.toString());
                    Log.d(access$300, sb.toString());
                }
            });
            i = i2;
        }
        populateSelectedDaysMap();
    }

    /* access modifiers changed from: private */
    public void toggleLeftDatePanel() {
        if (this.accessTimeImmediately) {
            if (this.disabledFrameDateLeft.getVisibility() != 0) {
                this.disabledFrameDateLeft.setVisibility(0);
            }
        } else if (this.disabledFrameDateLeft.getVisibility() != 8) {
            this.disabledFrameDateLeft.setVisibility(8);
        }
    }

    /* access modifiers changed from: private */
    public void toggleRightDatePanel() {
        if (this.accessTimeNeverExpires) {
            if (this.disabledFrameDateRight.getVisibility() != 0) {
                this.disabledFrameDateRight.setVisibility(0);
            }
        } else if (this.disabledFrameDateRight.getVisibility() != 8) {
            this.disabledFrameDateRight.setVisibility(8);
        }
    }

    /* access modifiers changed from: private */
    public void changeGuestInterfaceMode(GuestInterface guestInterface2) {
        int i;
        int i2;
        if (guestInterface2 == GuestInterface.SIMPLE) {
            i2 = 8;
            i = C1075R.string.guest_selection_mode_advanced;
        } else {
            i2 = 0;
            i = C1075R.string.guest_selection_mode_simple;
        }
        this.bTSelectionMode.setText(i);
        this.accessTimeContainer.setVisibility(i2);
        this.accessDayContainer.setVisibility(i2);
        this.rBImmediately.setChecked(true);
        this.rBNeverExpires.setChecked(true);
        updateStartDate(null);
        updateExpireDate(null);
        checkUnlimitedAccessRadioButton(true);
        checkAccessDaysControls(true);
        checkPermissionsControlsDefaultValues();
        this.mGrantAccessPresenter.setStartAtDate(null);
        this.mGrantAccessPresenter.setExpireAtDate(null);
        this.guestInterface = guestInterface2;
        MasterLockSharedPreferences.getInstance().putGuestInterfaceSelectionModeForLockModel(this.mGrantAccessPresenter.getModel().getModelNumber(), guestInterface2.getValue());
    }

    private void checkPermissionsControlsDefaultValues() {
        for (SwitchCompat switchCompat : this.permissionsSwitchList) {
            switchCompat.setChecked(switchCompat.getId() != C1075R.C1077id.switch_open_shackle);
        }
    }

    public void updateStartDate(DateTime dateTime) {
        String[] strArr = {getContext().getString(C1075R.string.date_place_holder), getContext().getString(C1075R.string.time_place_holder)};
        if (dateTime != null) {
            strArr = DateTimeFormat.forPattern(getDateTimeFormat()).print((ReadableInstant) dateTime).split("-");
        }
        this.tVStartDate.setText(strArr[0]);
        this.tVSTartTime.setText(strArr[1]);
    }

    public void updateExpireDate(DateTime dateTime) {
        String[] strArr = {getContext().getString(C1075R.string.date_place_holder), getContext().getString(C1075R.string.time_place_holder)};
        if (dateTime != null) {
            strArr = DateTimeFormat.forPattern(getDateTimeFormat()).print((ReadableInstant) dateTime).split("-");
        }
        this.tVExpireDate.setText(strArr[0]);
        this.tVExpireTime.setText(strArr[1]);
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296553})
    public void clickStartCalendar() {
        this.mGrantAccessPresenter.showStartDateSelectionDialog();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296546})
    public void clickExpireCalendar() {
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("clickExpireCalendar: ");
        sb.append(ButterKnife.findById((View) this, (int) C1075R.C1077id.iv_expires_calendar).isEnabled());
        Log.d(str, sb.toString());
        this.mGrantAccessPresenter.showExpireDateSelectionDialog();
    }

    public void displayAccessUpdatedToast() {
        Toast.makeText(getContext(), getContext().getString(C1075R.string.grant_access_updated_success_alert_text), 1).show();
        AppFlow.get(getContext()).goBack();
    }

    public void hideAccessWindow(boolean z) {
        this.grantAccessSpecificContainer.setVisibility(z ? 8 : 0);
    }

    private void hideCoOwner() {
        this.txtCoOwner.setVisibility(8);
        this.toggleCoOwner.setVisibility(8);
    }

    public boolean onBackPressed() {
        return this.mGrantAccessPresenter.showExitAddGuestFlowModal(true);
    }

    public boolean onUpPressed() {
        return this.mGrantAccessPresenter.showExitAddGuestFlowModal(true);
    }

    public void showInvitationSent() {
        Toast.makeText(getContext(), getContext().getString(C1075R.string.grant_access_success_alert_title), 1).show();
    }

    public void showTempCodeSent() {
        Toast.makeText(getContext(), getContext().getString(C1075R.string.send_temp_code_success_alert_text), 1).show();
    }

    public void enableSelectExpireDateControls(boolean z) {
        this.rBExpiresAt.setEnabled(z);
        ButterKnife.findById((View) this, (int) C1075R.C1077id.iv_expires_calendar).setEnabled(z);
    }

    public void checkUnlimitedAccessRadioButton(boolean z) {
        ((RadioButton) ButterKnife.findById((View) this, (int) C1075R.C1077id.radio_unlimited)).setChecked(z);
    }

    public void enableAccessTimeControls(boolean z) {
        for (RadioButton enabled : this.accessHoursRadioButtonList) {
            enabled.setEnabled(z);
        }
    }

    public void enablePermissionControls(boolean z) {
        for (SwitchCompat enabled : this.permissionsSwitchList) {
            enabled.setEnabled(z);
        }
    }

    public void enableSpecificTimeControls(boolean z) {
        this.rBImmediately.setEnabled(z);
        this.rBNeverExpires.setEnabled(z);
        this.rBExpiresAt.setEnabled(z);
        this.rBStartsAt.setEnabled(z);
    }

    public void enableAccessDaysControls(boolean z) {
        for (CheckBox enabled : this.accessDaysCheckBoxList) {
            enabled.setEnabled(z);
        }
    }

    public boolean isAtLeastOneDaySelected() {
        for (Entry value : this.selectedDaysMap.entrySet()) {
            if (((Boolean) value.getValue()).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    private void enableSelectionModeControl(boolean z) {
        this.bTSelectionMode.setEnabled(z);
        this.bTSelectionMode.setTextColor(ContextCompat.getColor(getContext(), C1075R.color.medium_grey));
    }

    private void enableCalendarModalControls(boolean z) {
        ButterKnife.findById((View) this, (int) C1075R.C1077id.iv_start_calendar).setEnabled(z);
        ButterKnife.findById((View) this, (int) C1075R.C1077id.iv_expires_calendar).setEnabled(z);
    }

    public void checkAccessDaysControls(boolean z) {
        for (CheckBox checked : this.accessDaysCheckBoxList) {
            checked.setChecked(z);
        }
    }

    public void checkPermissions(boolean z) {
        for (SwitchCompat checked : this.permissionsSwitchList) {
            checked.setChecked(z);
        }
    }

    public void showShacklePermissionControl() {
        ButterKnife.findById((View) this, (int) C1075R.C1077id.switch_open_shackle).setVisibility(0);
    }

    public boolean isAccessTimeImmediately() {
        return this.accessTimeImmediately;
    }

    public boolean isAccessTimeNeverExpires() {
        return this.accessTimeNeverExpires;
    }

    public ScheduleType getScheduleType() {
        return this.mScheduleType;
    }

    public Map<WeekDay, Boolean> getSelectedDaysMap() {
        return this.selectedDaysMap;
    }

    public boolean isOpenShacklePermission() {
        return this.openShacklePermission;
    }

    public boolean isViewTemporaryCodePermission() {
        return this.viewTemporaryCodePermission;
    }

    public boolean isViewLastKnownLocationPermission() {
        return this.viewLastKnownLocationPermission;
    }

    private void populateSelectedDaysMap() {
        for (WeekDay put : WeekDay.values()) {
            this.selectedDaysMap.put(put, Boolean.valueOf(false));
        }
    }

    public void recoverControlStateFromInvitation(Invitation invitation, Lock lock) {
        boolean isEnabled = this.rBStartsAt.isEnabled();
        boolean isEnabled2 = this.rBExpiresAt.isEnabled();
        boolean isEnabled3 = this.rBImmediately.isEnabled();
        boolean isEnabled4 = this.rBNeverExpires.isEnabled();
        boolean isEnabled5 = this.iVExpireCalendar.isEnabled();
        this.rBStartsAt.setEnabled(false);
        this.rBExpiresAt.setEnabled(false);
        this.rBImmediately.setEnabled(false);
        this.rBNeverExpires.setEnabled(false);
        GuestPermissions guestPermissions = invitation.getGuestPermissions();
        changeGuestInterfaceMode(guestPermissions.getGuestInterface());
        if (guestPermissions.getGuestInterface() == GuestInterface.SIMPLE) {
            this.rBStartsAt.setChecked(false);
            this.rBExpiresAt.setChecked(false);
        } else {
            if (guestPermissions.getStartAtDate() == null) {
                this.rBImmediately.setChecked(true);
            } else {
                this.rBStartsAt.setChecked(true);
                DateTime dateTime = new DateTime((Object) invitation.getGuestPermissions().getStartAtDate());
                this.mGrantAccessPresenter.setStartAtDate(dateTime);
                updateStartDate(dateTime);
            }
            if (guestPermissions.getExpiresAtDate() == null) {
                this.rBNeverExpires.setChecked(true);
            } else {
                this.rBExpiresAt.setChecked(true);
                DateTime dateTime2 = new DateTime((Object) invitation.getGuestPermissions().getExpiresAtDate());
                this.mGrantAccessPresenter.setExpireAtDate(dateTime2);
                updateExpireDate(dateTime2);
                enableSelectExpireDateControls(true);
            }
            for (CheckBox checkBox : this.accessDaysCheckBoxList) {
                switch (checkBox.getId()) {
                    case C1075R.C1077id.cb_friday /*2131296365*/:
                        checkBox.setChecked(guestPermissions.isFriday());
                        break;
                    case C1075R.C1077id.cb_monday /*2131296366*/:
                        checkBox.setChecked(guestPermissions.isMonday());
                        break;
                    case C1075R.C1077id.cb_saturday /*2131296368*/:
                        checkBox.setChecked(guestPermissions.isSaturday());
                        break;
                    case C1075R.C1077id.cb_sunday /*2131296369*/:
                        checkBox.setChecked(guestPermissions.isSunday());
                        break;
                    case C1075R.C1077id.cb_thursday /*2131296370*/:
                        checkBox.setChecked(guestPermissions.isThursday());
                        break;
                    case C1075R.C1077id.cb_tuesday /*2131296371*/:
                        checkBox.setChecked(guestPermissions.isTuesday());
                        break;
                    case C1075R.C1077id.cb_wednesday /*2131296372*/:
                        checkBox.setChecked(guestPermissions.isWednesday());
                        break;
                }
            }
            if (guestPermissions.getExpiresAtDate() != null) {
                boolean shouldShowTimeControls = this.mGrantAccessPresenter.shouldShowTimeControls();
                enableAccessTimeControls(shouldShowTimeControls);
                if (!shouldShowTimeControls) {
                    this.radioUnlimited.setChecked(true);
                }
                boolean shouldShowDaysControls = this.mGrantAccessPresenter.shouldShowDaysControls();
                enableAccessDaysControls(shouldShowDaysControls);
                if (!shouldShowDaysControls) {
                    checkAccessDaysControls(true);
                }
            }
        }
        for (SwitchCompat switchCompat : this.permissionsSwitchList) {
            switch (switchCompat.getId()) {
                case C1075R.C1077id.switch_open_shackle /*2131296785*/:
                    switchCompat.setChecked(guestPermissions.isOpenShacklePermission());
                    break;
                case C1075R.C1077id.switch_view_last_known_location /*2131296786*/:
                    switchCompat.setChecked(guestPermissions.isViewLastKnownLocationPermission());
                    break;
                case C1075R.C1077id.switch_view_temporary_code /*2131296787*/:
                    switchCompat.setChecked(guestPermissions.isViewTemporaryCodePermission());
                    break;
            }
        }
        this.rBStartsAt.setEnabled(isEnabled);
        this.rBExpiresAt.setEnabled(isEnabled2);
        this.rBImmediately.setEnabled(isEnabled3);
        this.rBNeverExpires.setEnabled(isEnabled4);
        this.iVExpireCalendar.setEnabled(isEnabled5);
    }

    private void disableTimeAndDayContainer() {
        boolean z = false;
        boolean z2 = this.mGrantAccessPresenter.getStartAtDate() != null;
        if (this.mGrantAccessPresenter.getExpireAtDate() != null) {
            z = true;
        }
        if (z2 && z) {
            boolean shouldShowTimeControls = this.mGrantAccessPresenter.shouldShowTimeControls();
            enableAccessTimeControls(shouldShowTimeControls);
            if (!shouldShowTimeControls) {
                checkUnlimitedAccessRadioButton(true);
            }
            boolean shouldShowDaysControls = this.mGrantAccessPresenter.shouldShowDaysControls();
            enableAccessDaysControls(shouldShowDaysControls);
            if (!shouldShowDaysControls) {
                checkAccessDaysControls(true);
            }
        }
    }

    public void checkNeverExpiresControl(boolean z) {
        this.rBNeverExpires.setChecked(z);
    }

    public GuestInterface getGuestInterface() {
        return this.guestInterface;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x003b  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0041  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String getDateTimeFormat() {
        /*
            r3 = this;
            java.util.Locale r0 = java.util.Locale.getDefault()
            java.lang.String r0 = r0.getLanguage()
            int r1 = r0.hashCode()
            r2 = 3201(0xc81, float:4.486E-42)
            if (r1 == r2) goto L_0x002d
            r2 = 3246(0xcae, float:4.549E-42)
            if (r1 == r2) goto L_0x0023
            r2 = 3276(0xccc, float:4.59E-42)
            if (r1 == r2) goto L_0x0019
            goto L_0x0037
        L_0x0019:
            java.lang.String r1 = "fr"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0037
            r0 = 0
            goto L_0x0038
        L_0x0023:
            java.lang.String r1 = "es"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0037
            r0 = 1
            goto L_0x0038
        L_0x002d:
            java.lang.String r1 = "de"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0037
            r0 = 2
            goto L_0x0038
        L_0x0037:
            r0 = -1
        L_0x0038:
            switch(r0) {
                case 0: goto L_0x0041;
                case 1: goto L_0x003e;
                case 2: goto L_0x003e;
                default: goto L_0x003b;
            }
        L_0x003b:
            java.lang.String r0 = "MM/dd/yyyy-h:mm a"
            return r0
        L_0x003e:
            java.lang.String r0 = "dd/MM/yyyy-h:mm a"
            return r0
        L_0x0041:
            java.lang.String r0 = "dd/MM/yyyy-HH'H'mm"
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.masterlock.ble.app.view.guest.GrantAccessView.getDateTimeFormat():java.lang.String");
    }
}
