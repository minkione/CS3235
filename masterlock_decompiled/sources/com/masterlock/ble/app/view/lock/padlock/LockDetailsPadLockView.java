package com.masterlock.ble.app.view.lock.padlock;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.p003v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.ButterKnife.Setter;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import butterknife.Optional;
import com.google.common.base.Strings;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.adapters.HistoryAdapter;
import com.masterlock.ble.app.adapters.InvitationAdapter;
import com.masterlock.ble.app.adapters.listeners.ItemClickListener;
import com.masterlock.ble.app.adapters.listeners.ItemSwipeListener;
import com.masterlock.ble.app.presenter.lock.padlock.LockDetailsPadLockPresenter;
import com.masterlock.ble.app.screens.LockScreens.LockDetailsPadLock;
import com.masterlock.ble.app.screens.SettingsScreens.AboutLockPadLock;
import com.masterlock.ble.app.util.AccessWindowUtil;
import com.masterlock.ble.app.util.MLDateUtils;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.core.AccessType;
import com.masterlock.core.GuestInterface;
import com.masterlock.core.KmsLogEntry;
import com.masterlock.core.Lock;
import com.masterlock.core.LockCodeDirection;
import com.masterlock.core.ScheduleType;
import com.square.flow.appflow.AppFlow;
import com.square.flow.screenswitcher.HandlesBack;
import com.square.flow.screenswitcher.HandlesUp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class LockDetailsPadLockView extends LinearLayout implements IAuthenticatedView, ItemClickListener, ItemSwipeListener, HandlesBack, HandlesUp {
    static final Setter<View, Boolean> SHOW = $$Lambda$LockDetailsPadLockView$i5hvBEktPM7qWDAyZy0Qroq4I.INSTANCE;
    private static final String TAG = "LockDetailsPadLockView";
    private boolean access = true;
    @InjectView(2131296336)
    @Optional
    Button btnClear;
    @InjectView(2131296421)
    TextView deviceId;
    @InjectView(2131296504)
    @Optional
    LinearLayout guestLocationContainer;
    private boolean inFirmwareUpdate = false;
    @InjectView(2131296605)
    TextView lockerModeApplied;
    @InjectView(2131296264)
    @Optional
    TextView mAccessWindowRestricted;
    @InjectView(2131296265)
    @Optional
    CardView mAccessWindowRestrictedCard;
    @InjectView(2131296296)
    LinearLayout mAdditionalDetailsContainer;
    @InjectView(2131296849)
    TextView mBattery;
    @InjectView(2131296312)
    LinearLayout mBatteryContainer;
    @InjectView(2131296313)
    ImageView mBatteryIndicator;
    @InjectView(2131296332)
    @Optional
    Button mBtnAddGuest;
    @InjectView(2131296334)
    @Optional
    Button mBtnAllGuests;
    @InjectView(2131296342)
    @Optional
    Button mBtnHistory;
    @InjectView(2131296344)
    Button mBtnLockerMode;
    @InjectView(2131296347)
    @Optional
    Button mBtnPrimaryCode;
    @InjectView(2131296361)
    RelativeLayout mButtonBar;
    /* access modifiers changed from: private */
    public CountDownTimer mCountDownTimer;
    @InjectView(2131296588)
    @Optional
    ImageView mGpsPermissionIV;
    @InjectViews({2131296511, 2131296510, 2131296512, 2131296334})
    @Optional
    List<View> mGuestListViews;
    @InjectView(2131296510)
    @Optional
    LinearLayout mGuestsContainer;
    @InjectView(2131296513)
    RelativeLayout mHeader;
    @InjectView(2131296515)
    @Optional
    LinearLayout mHistoryContainer;
    @InjectView(2131296867)
    TextView mInstructions;
    private InvitationAdapter mInvitationAdapter;
    @InjectView(2131296659)
    @Optional
    LinearLayout mLockCodeContainer;
    /* access modifiers changed from: private */
    public LockDetailsPadLockPresenter mLockDetailsPadLockPresenter;
    @InjectView(2131296868)
    TextView mLockLastRecord;
    @InjectView(2131296872)
    TextView mLockState;
    @InjectView(2131296876)
    @Optional
    TextView mOwnerEmail;
    @InjectView(2131296881)
    TextView mRelock;
    @InjectView(2131296880)
    TextView mRelockTime;
    @InjectView(2131296882)
    TextView mRelockUnit;
    @InjectView(2131296717)
    @Optional
    LinearLayout mRollingCodeContainer;
    @InjectView(2131296718)
    @Optional
    TextView mRollingCodeExpiration;
    @InjectView(2131296883)
    @Optional
    TextView mRollingInstructions;
    @InjectView(2131296750)
    @Optional
    CardView mServiceCodeCard;
    @InjectView(2131296613)
    @Optional
    FrameLayout map;
    @InjectView(2131296636)
    @Optional
    TextView noLocationText;

    static /* synthetic */ void lambda$static$0(View view, Boolean bool, int i) {
        view.setVisibility(bool.booleanValue() ? 0 : 8);
    }

    public LockDetailsPadLockView(Context context) {
        super(context);
    }

    public LockDetailsPadLockView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public LockDetailsPadLockView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            LockDetailsPadLock lockDetailsPadLock = (LockDetailsPadLock) AppFlow.getScreen(getContext());
            this.mLockDetailsPadLockPresenter = new LockDetailsPadLockPresenter(lockDetailsPadLock.mLock, this);
            setupInitialBackgroundColor(lockDetailsPadLock.mLock);
            if (!lockDetailsPadLock.mFromNotification) {
                Log.d("LOCK DETAILS VIEW", "mFromNotification false");
                this.mLockDetailsPadLockPresenter.reload();
            }
        }
    }

    public void setupInitialBackgroundColor(Lock lock) {
        this.inFirmwareUpdate = false;
        if (lock.isLockerMode()) {
            setBackgroundColor(getResources().getColor(C1075R.color.locker_mode));
            return;
        }
        switch (lock.getLockStatus()) {
            case LOCK_FOUND:
                this.mHeader.setBackgroundColor(getResources().getColor(C1075R.color.locked));
                return;
            case LOCKED:
                this.mHeader.setBackgroundColor(getResources().getColor(C1075R.color.locked));
                return;
            case UNLOCKED:
                this.mHeader.setBackgroundColor(getResources().getColor(C1075R.color.open));
                return;
            case UNLOCKING:
                this.mHeader.setBackgroundColor(getResources().getColor(C1075R.color.open));
                return;
            case OPENED:
                this.mHeader.setBackgroundColor(getResources().getColor(C1075R.color.open));
                return;
            case UPDATE_MODE:
                this.mHeader.setBackgroundColor(getResources().getColor(C1075R.color.locker_update));
                this.inFirmwareUpdate = true;
                return;
            default:
                this.mHeader.setBackgroundColor(getResources().getColor(C1075R.color.primary));
                return;
        }
    }

    public void updateView(Lock lock) {
        this.deviceId.setText(lock.getKmsDeviceKey().getDeviceId());
        Resources resources = getResources();
        int i = 8;
        if (lock.getRemainingBatteryPercentage() < 0) {
            this.mBatteryContainer.setVisibility(8);
        } else if (lock.getRemainingBatteryPercentage() <= getResources().getInteger(C1075R.integer.low_battery_percentage)) {
            this.mBatteryContainer.setVisibility(0);
            this.mBatteryIndicator.setImageDrawable(getResources().getDrawable(C1075R.C1076drawable.ic_battery_info_red));
            this.mBatteryIndicator.setVisibility(0);
        } else {
            this.mBatteryIndicator.setImageDrawable(getResources().getDrawable(C1075R.C1076drawable.ic_battery_info_gray));
            this.mBatteryIndicator.setVisibility(0);
        }
        this.mRelock.setText(resources.getQuantityString(C1075R.plurals.relock_time, lock.getRelockTimeInSeconds(), new Object[]{Integer.valueOf(lock.getRelockTimeInSeconds())}));
        setLastLockRecord(lock);
        updateLockState(lock);
        if (lock.getAccessType() == AccessType.OWNER || lock.getAccessType() == AccessType.CO_OWNER) {
            setOwnerDetails(lock);
            this.mLockDetailsPadLockPresenter.checkForGpsPermission();
        }
        if (lock.getAccessType() == AccessType.GUEST) {
            setGuestDetails(lock);
            this.mRelock.setClickable(false);
            this.mLockDetailsPadLockPresenter.checkForGpsPermission();
        }
        if (this.mLockDetailsPadLockPresenter.isLocationAvailable()) {
            this.map.setVisibility(0);
            this.noLocationText.setVisibility(4);
            Button button = this.btnClear;
            if (lock.getAccessType() != AccessType.GUEST) {
                i = 0;
            }
            button.setVisibility(i);
            return;
        }
        this.map.setVisibility(8);
        this.noLocationText.setVisibility(0);
        this.btnClear.setVisibility(8);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mLockDetailsPadLockPresenter.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelTimer();
        this.mLockDetailsPadLockPresenter.finish();
    }

    public void displayError(Throwable th) {
        Toast.makeText(getContext(), th.getMessage() != null ? th.getMessage() : th.getClass().getSimpleName(), 0).show();
    }

    /* access modifiers changed from: 0000 */
    public void transitionBackgroundColor(int i) {
        int color = ((ColorDrawable) this.mHeader.getBackground()).getColor();
        ValueAnimator ofObject = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{Integer.valueOf(color), Integer.valueOf(i)});
        ofObject.addUpdateListener(new AnimatorUpdateListener() {
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                LockDetailsPadLockView.this.mHeader.setBackgroundColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
            }
        });
        ofObject.start();
    }

    private void setOwnerDetails(Lock lock) {
        if (this.access) {
            LayoutInflater.from(getContext()).inflate(C1075R.layout.lock_owner_details_pad_lock, this.mAdditionalDetailsContainer, true);
            this.access = false;
            ButterKnife.inject((View) this);
        }
        if (!TextUtils.isEmpty(lock.getPrimaryCode())) {
            fillCode(this.mLockCodeContainer, lock.generateLockPrimaryCodeList());
        }
        setGuestList(lock);
        setHistoryList(lock);
    }

    private void setLastLockRecord(Lock lock) {
        if (lock == null || lock.getLogs() == null || lock.getLogs().size() <= 0) {
            this.mLockLastRecord.setVisibility(8);
            return;
        }
        int i = 0;
        KmsLogEntry kmsLogEntry = (KmsLogEntry) lock.getLogs().get(0);
        StringBuilder sb = new StringBuilder(kmsLogEntry.getMessage());
        sb.append(" • ");
        try {
            sb.append(MLDateUtils.parseServerDateFormat(kmsLogEntry.getCreatedOn(), getResources()));
        } catch (ParseException e) {
            Log.e(TAG, "getView: failed parsing date", e);
        }
        this.mLockLastRecord.setText(sb);
        TextView textView = this.mLockLastRecord;
        if (lock.getAccessType() == AccessType.GUEST) {
            i = 4;
        }
        textView.setVisibility(i);
    }

    private void setGuestList(Lock lock) {
        ButterKnife.inject((View) this);
        this.mGuestsContainer.removeAllViews();
        if (lock.getInvitations() == null || lock.getInvitations().size() <= 0) {
            ButterKnife.apply(this.mGuestListViews, SHOW, Boolean.valueOf(false));
            return;
        }
        ButterKnife.apply(this.mGuestListViews, SHOW, Boolean.valueOf(true));
        this.mInvitationAdapter = new InvitationAdapter(getContext(), lock.getInvitations());
        this.mInvitationAdapter.setLock(lock);
        this.mInvitationAdapter.setItemClickListener(this);
        this.mInvitationAdapter.setItemSwipeListener(this);
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
        int min = Math.min(3, lock.getInvitations().size());
        for (int i = 0; i < min; i++) {
            LinearLayout linearLayout = this.mGuestsContainer;
            linearLayout.addView(this.mInvitationAdapter.getView(i, null, linearLayout));
            if (i < min - 1) {
                this.mGuestsContainer.addView(layoutInflater.inflate(C1075R.layout.divider, this.mGuestsContainer, false));
            }
        }
    }

    private void setHistoryList(Lock lock) {
        ButterKnife.inject((View) this);
        this.mHistoryContainer.removeAllViews();
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
        if (lock.getLogs() == null || lock.getLogs().size() <= 0) {
            TextView textView = (TextView) layoutInflater.inflate(C1075R.layout.history_list_empty, this.mHistoryContainer, false);
            textView.setText(C1075R.string.no_history);
            this.mHistoryContainer.addView(textView);
            return;
        }
        ArrayList arrayList = new ArrayList();
        int i = 0;
        for (KmsLogEntry kmsLogEntry : lock.getLogs()) {
            int i2 = i + 1;
            if (i >= 3) {
                break;
            }
            arrayList.add(kmsLogEntry);
            i = i2;
        }
        HistoryAdapter historyAdapter = new HistoryAdapter(getContext(), arrayList, lock);
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            LinearLayout linearLayout = this.mHistoryContainer;
            linearLayout.addView(historyAdapter.getView(i3, null, linearLayout));
            if (i3 < 2) {
                this.mHistoryContainer.addView(layoutInflater.inflate(C1075R.layout.divider, this.mHistoryContainer, false));
            }
        }
    }

    private void setGuestDetails(Lock lock) {
        int i = 0;
        if (this.access) {
            LayoutInflater.from(getContext()).inflate(C1075R.layout.lock_guest_details_pad_lock, this.mAdditionalDetailsContainer, true);
            ButterKnife.inject((View) this);
            this.access = false;
        }
        if (AccessWindowUtil.isWithinAccessWindow(lock)) {
            this.mRollingCodeExpiration.setText(buildExpirationString(lock));
            this.mServiceCodeCard.setVisibility(0);
            this.mAccessWindowRestrictedCard.setVisibility(8);
            fillCode(this.mRollingCodeContainer, LockCodeDirection.generateLockDirectionListFromStringCode(lock.getServiceCode()));
            if (Strings.isNullOrEmpty(lock.getServiceCode())) {
                this.mServiceCodeCard.setVisibility(8);
            } else {
                this.mServiceCodeCard.setVisibility(lock.getPermissions().isViewTemporaryCodePermission() ? 0 : 8);
            }
            LinearLayout linearLayout = this.guestLocationContainer;
            if (!lock.getPermissions().isViewLastKnownLocationPermission()) {
                i = 8;
            }
            linearLayout.setVisibility(i);
            return;
        }
        Resources resources = MasterLockApp.get().getResources();
        String str = "";
        this.mButtonBar.setVisibility(8);
        this.mServiceCodeCard.setVisibility(8);
        this.guestLocationContainer.setVisibility(8);
        this.mAccessWindowRestrictedCard.setVisibility(0);
        if (!AccessWindowUtil.hasStarted(lock)) {
            str = getResources().getString(C1075R.string.guest_detail_access_have_not_started);
        } else if (!AccessWindowUtil.isAllowedToday(lock)) {
            str = resources.getString(C1075R.string.lock_details_guest_no_access_today);
        } else if (!AccessWindowUtil.isInsideSchedule(lock)) {
            str = String.format(getResources().getString(lock.getPermissions().getScheduleType() == ScheduleType.SEVEN_AM_TO_SEVEN_PM ? C1075R.string.service_code_available_7am_7pm : C1075R.string.service_code_available_7pm_7am), new Object[]{lock.getTimeZoneOffset(), lock.getLocalizedTimeZone()});
        }
        this.mAccessWindowRestricted.setText(str);
    }

    private String buildExpirationString(Lock lock) {
        StringBuilder sb = new StringBuilder();
        try {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getResources().getString(C1075R.string.expires));
            sb2.append(MLDateUtils.parseServerDateFormat(lock.getServiceCodeExpiresOn(), getResources()));
            sb.append(sb2.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private void fillCode(LinearLayout linearLayout, List<LockCodeDirection> list) {
        int i;
        int i2;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
        linearLayout.removeAllViews();
        for (LockCodeDirection lockCodeDirection : list) {
            switch (lockCodeDirection) {
                case LEFT:
                    i2 = C1075R.C1076drawable.ic_arrow_left;
                    i = C1075R.string.lock_code_left;
                    break;
                case UP:
                    i2 = C1075R.C1076drawable.ic_arrow_up;
                    i = C1075R.string.lock_code_up;
                    break;
                case RIGHT:
                    i2 = C1075R.C1076drawable.ic_arrow_right;
                    i = C1075R.string.lock_code_right;
                    break;
                case DOWN:
                    i2 = C1075R.C1076drawable.ic_arrow_down;
                    i = C1075R.string.lock_code_down;
                    break;
                default:
                    i2 = 0;
                    i = 0;
                    break;
            }
            if (i2 != 0 || i != 0) {
                View inflate = layoutInflater.inflate(C1075R.layout.lock_code_item_pad_lock, linearLayout, false);
                ((ImageView) ButterKnife.findById(inflate, (int) C1075R.C1077id.img_code_direction)).setImageResource(i2);
                ((TextView) ButterKnife.findById(inflate, (int) C1075R.C1077id.txt_code_direction)).setText(i);
                linearLayout.addView(inflate);
            }
        }
    }

    private void updateLockState(Lock lock) {
        String str;
        if (lock.getLockStatus() != null) {
            boolean z = false;
            this.inFirmwareUpdate = false;
            if (lock.isLockerMode()) {
                transitionBackgroundColor(getResources().getColor(C1075R.color.locker_mode));
                this.mLockState.setText(getResources().getString(C1075R.string.locker_mode_title));
                this.mInstructions.setText(getResources().getString(C1075R.string.locker_mode_body));
                z = true;
            } else if (lock.getAccessType() == AccessType.GUEST && !AccessWindowUtil.hasStarted(lock)) {
                transitionBackgroundColor(getResources().getColor(C1075R.color.guest_mode_out_of_schedule));
                this.mLockState.setText(getResources().getString(C1075R.string.guest_detail_access_pending));
                this.mInstructions.setText("");
            } else if (lock.getAccessType() == AccessType.GUEST && AccessWindowUtil.hasStarted(lock) && lock.getPermissions().getGuestInterface() == GuestInterface.ADVANCED && !AccessWindowUtil.isAllowedToday(lock)) {
                transitionBackgroundColor(getResources().getColor(C1075R.color.guest_mode_out_of_schedule));
                this.mLockState.setText(getResources().getString(C1075R.string.guest_detail_access_scheduled));
                this.mInstructions.setText("");
            } else if (lock.getAccessType() == AccessType.GUEST && lock.getPermissions().getScheduleType() == ScheduleType.SEVEN_AM_TO_SEVEN_PM && !AccessWindowUtil.isInsideSchedule(lock)) {
                transitionBackgroundColor(getResources().getColor(C1075R.color.guest_mode_out_of_schedule));
                this.mLockState.setText(getResources().getString(C1075R.string.guest_detail_access_day));
                this.mInstructions.setText("");
            } else if (lock.getAccessType() != AccessType.GUEST || lock.getPermissions().getScheduleType() != ScheduleType.SEVEN_PM_TO_SEVEN_AM || AccessWindowUtil.isInsideSchedule(lock)) {
                this.mButtonBar.setVisibility(0);
                hideTimer();
                switch (lock.getLockStatus()) {
                    case LOCK_FOUND:
                        transitionBackgroundColor(getResources().getColor(C1075R.color.locked));
                        this.mLockState.setText(getResources().getString(C1075R.string.lock_found_state_title));
                        this.mInstructions.setText("");
                        break;
                    case LOCKED:
                        transitionBackgroundColor(getResources().getColor(C1075R.color.locked));
                        this.mLockState.setText(getResources().getString(C1075R.string.locked_state_title));
                        this.mInstructions.setText(getResources().getString(C1075R.string.locked_state_body));
                        break;
                    case UNLOCKED:
                        transitionBackgroundColor(getResources().getColor(C1075R.color.open));
                        this.mLockState.setText(getResources().getString(C1075R.string.unlocked_state_title));
                        this.mInstructions.setText(getResources().getString(C1075R.string.unlocked_state_body));
                        initializeTimer(((long) (lock.getRelockTimeInSeconds() * 1000)) - (System.currentTimeMillis() - lock.getLastUnlocked()));
                        break;
                    case UNLOCKING:
                        transitionBackgroundColor(getResources().getColor(C1075R.color.open));
                        this.mLockState.setText(getResources().getString(C1075R.string.unlocking_state_title));
                        this.mInstructions.setText(getResources().getString(C1075R.string.unlocking_state_body));
                        break;
                    case OPENED:
                        transitionBackgroundColor(getResources().getColor(C1075R.color.open));
                        this.mLockState.setText(getResources().getString(C1075R.string.opened_state_title));
                        this.mInstructions.setText(getResources().getString(C1075R.string.opened_state_body));
                        break;
                    case UPDATE_MODE:
                        this.inFirmwareUpdate = true;
                        transitionBackgroundColor(getResources().getColor(C1075R.color.locker_update));
                        this.mLockState.setText(getResources().getString(C1075R.string.title_firmware_update_mode));
                        this.mInstructions.setText(getResources().getString(C1075R.string.firmware_update_state_body));
                        break;
                    default:
                        transitionBackgroundColor(getResources().getColor(C1075R.color.primary));
                        this.mLockState.setText(getResources().getString(C1075R.string.lock_unreachable));
                        switch (lock.getLockMode()) {
                            case TOUCH:
                                str = getResources().getString(C1075R.string.unreachable_state_body_touch);
                                break;
                            case PROXIMITYSWIPE:
                                str = getResources().getString(C1075R.string.unreachable_state_body_proximity);
                                break;
                            default:
                                str = "";
                                break;
                        }
                        this.mInstructions.setText(str);
                        break;
                }
            } else {
                transitionBackgroundColor(getResources().getColor(C1075R.color.guest_mode_out_of_schedule));
                this.mLockState.setText(getResources().getString(C1075R.string.guest_detail_access_night));
                this.mInstructions.setText("");
            }
            updateButtonBar(z);
        }
    }

    private void updateButtonBar(boolean z) {
        if (this.inFirmwareUpdate) {
            this.mBtnLockerMode.setText(getResources().getString(C1075R.string.continue_firmware_update));
            this.lockerModeApplied.setVisibility(4);
        } else if (z) {
            this.mBtnLockerMode.setText(getResources().getString(C1075R.string.locker_mode_disable));
            this.lockerModeApplied.setVisibility(0);
        } else {
            this.mBtnLockerMode.setText(getResources().getString(C1075R.string.locker_mode_apply));
            this.lockerModeApplied.setVisibility(4);
        }
    }

    private void initializeTimer(long j) {
        this.mRelockTime.setVisibility(0);
        this.mRelockUnit.setVisibility(0);
        C15341 r2 = new CountDownTimer(j, 250) {
            int secondsLeft;

            public void onTick(long j) {
                float f = ((float) j) / 1000.0f;
                if (Math.round(f) != this.secondsLeft) {
                    this.secondsLeft = Math.round(f);
                    LockDetailsPadLockView.this.mRelockTime.setText(LockDetailsPadLockView.this.getResources().getString(C1075R.string.relock_time, new Object[]{Integer.valueOf(this.secondsLeft)}));
                }
            }

            public void onFinish() {
                LockDetailsPadLockView.this.mCountDownTimer = null;
                LockDetailsPadLockView.this.mLockDetailsPadLockPresenter.relockTimeExpired();
            }
        };
        this.mCountDownTimer = r2;
        this.mCountDownTimer.start();
    }

    private void hideTimer() {
        this.mRelockTime.setVisibility(8);
        this.mRelockUnit.setVisibility(8);
    }

    private void cancelTimer() {
        CountDownTimer countDownTimer = this.mCountDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296636})
    @Optional
    public void goToInfoLocation() {
        this.mLockDetailsPadLockPresenter.goToLastLocation();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296347})
    @Optional
    public void gotoPrimaryCode() {
        this.mLockDetailsPadLockPresenter.updatePrimaryCode();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296332})
    @Optional
    public void gotoAddGuest() {
        this.mLockDetailsPadLockPresenter.goToAddGuest();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296334})
    @Optional
    public void gotoAllGuestsList() {
        this.mLockDetailsPadLockPresenter.goToInvitationList();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296342})
    @Optional
    public void gotoHistoryList() {
        this.mLockDetailsPadLockPresenter.goToHistory();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296344})
    public void toggleLockerMode() {
        if (this.inFirmwareUpdate) {
            this.mLockDetailsPadLockPresenter.updateToolbarToDefault();
            AppFlow.get(getContext()).goTo(new AboutLockPadLock(this.mLockDetailsPadLockPresenter.getLock()));
            return;
        }
        this.mLockDetailsPadLockPresenter.toggleLockerMode();
    }

    @OnClick({2131296336})
    @Optional
    public void clear() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (LockDetailsPadLockView.this.btnClear != null && LockDetailsPadLockView.this.btnClear.isEnabled()) {
                    LockDetailsPadLockView.this.mLockDetailsPadLockPresenter.showClearLastLocationDialog();
                }
            }
        }, 150);
    }

    @OnClick({2131296588})
    @Optional
    public void locationDisabled() {
        this.mLockDetailsPadLockPresenter.locationPermissionRequest();
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    @OnClick({2131296312})
    @Optional
    public void goToBatteryDetail() {
        this.mLockDetailsPadLockPresenter.goToBatteryDetail();
    }

    @OnClick({2131296687})
    public void showRelockTime() {
        this.mLockDetailsPadLockPresenter.showRelockTime();
    }

    public void onItemClick(View view, int i) {
        this.mLockDetailsPadLockPresenter.goToInvitation(this.mInvitationAdapter.getItem(i));
    }

    public void onItemSwipe(View view, int i) {
        this.mLockDetailsPadLockPresenter.deleteInvitation(this.mInvitationAdapter.getItem(i));
    }

    public void showAccessRevoked() {
        Toast.makeText(getContext(), getContext().getString(C1075R.string.guest_access_revoked_explanation), 1).show();
    }

    public void showGuestDeleted() {
        Toast.makeText(getContext(), getContext().getString(C1075R.string.guest_deleted_explanation), 1).show();
    }

    public boolean onUpPressed() {
        return this.mLockDetailsPadLockPresenter.onBackPressed();
    }

    public boolean onBackPressed() {
        return this.mLockDetailsPadLockPresenter.onBackPressed();
    }

    public void showGpsPermissionIcon(boolean z) {
        this.mGpsPermissionIV.setVisibility(z ? 0 : 8);
    }

    public void enableClearLastLocationButton(boolean z) {
        Button button = this.btnClear;
        if (button != null) {
            button.setOnClickListener(null);
            this.btnClear.setEnabled(z);
        }
    }
}
