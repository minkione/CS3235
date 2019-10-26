package com.masterlock.ble.app.view.locklanding;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher.ViewFactory;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.presenter.locklanding.LockLandingPresenter;
import com.masterlock.ble.app.screens.LockScreens.LockLanding;
import com.masterlock.ble.app.screens.SettingsScreens.AboutLockKeySafe;
import com.masterlock.ble.app.util.AccessWindowUtil;
import com.masterlock.ble.app.util.LandingScrollView;
import com.masterlock.ble.app.util.MLDateUtils;
import com.masterlock.ble.app.util.ViewUtil;
import com.masterlock.core.AccessType;
import com.masterlock.core.GuestInterface;
import com.masterlock.core.KmsLogEntry;
import com.masterlock.core.Lock;
import com.masterlock.core.LockMode;
import com.masterlock.core.ScheduleType;
import com.square.flow.appflow.AppFlow;
import com.square.flow.screenswitcher.HandlesBack;
import com.square.flow.screenswitcher.HandlesUp;
import java.text.ParseException;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import p007fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class LockLandingView extends RelativeLayout implements HandlesBack, HandlesUp {
    private static final int DEFAULT_FOOTER = 0;
    public static final int OFF_SCREEN = 0;
    public static final int ON_SCREEN = 1;
    private static final String TAG = "LockLandingView";
    private static final int TIMER_FOOTER = 1;
    @InjectView(2131296308)
    TextView auditDetail;
    @InjectView(2131296313)
    ImageView batteryIndicator;
    @InjectView(2131296314)
    TextView batteryPercentage;
    @InjectView(2131296361)
    View buttonBar;
    @InjectView(2131296421)
    TextView deviceId;
    @InjectView(2131296492)
    ViewFlipper footerStub;
    @InjectView(2131296554)
    LandingScrollView landingScroll;
    @InjectView(2131296587)
    SmoothProgressBar loadingProgressBar;
    @InjectView(2131296589)
    ImageView lockArrowLarge;
    @InjectView(2131296590)
    ImageView lockArrowMedium;
    @InjectView(2131296591)
    ImageView lockArrowSmall;
    @InjectView(2131296593)
    ImageSwitcher lockIcon;
    @Inject
    LockLandingPresenter lockLandingPresenter;
    @InjectView(2131296596)
    TextView lockName;
    @InjectView(2131296605)
    TextView lockerModeApplied;
    @InjectView(2131296608)
    Button lockerModeToggle;
    AnimatorSet mArrowAnimatorSet;
    private int mButtonBarState;
    public boolean mCalibration;
    /* access modifiers changed from: private */
    public CountDownTimer mCountDownTimer;
    View mDefaultFooterView;
    private Lock mLock;
    /* access modifiers changed from: private */
    public boolean mLockFound;
    private boolean mLockIsInFirmwareUpdate;
    View mTimerFooterView;
    OnTouchListener mTouchListener;
    @InjectView(2131296685)
    TextView relockTime;
    @InjectView(2131296777)
    TextView stateBody;
    @InjectView(2131296779)
    TextView stateTitle;
    private boolean wasInLockerMode;

    class ImageViewFactory implements ViewFactory {
        ImageViewFactory() {
        }

        public View makeView() {
            ImageView imageView = new ImageView(LockLandingView.this.getContext());
            imageView.setScaleType(ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new LayoutParams(-1, -1));
            return imageView;
        }
    }

    public LockLandingView(Context context) {
        this(context, null);
    }

    public LockLandingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mButtonBarState = 1;
        this.mLockFound = false;
        this.wasInLockerMode = false;
        this.mLockIsInFirmwareUpdate = false;
        this.mTouchListener = new OnTouchListener() {
            float bottomY;
            float offset;
            float startY;

            public boolean onTouch(final View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 0:
                        this.startY = view.getY();
                        this.bottomY = (float) (LockLandingView.this.stateBody.getTop() - LockLandingView.this.lockIcon.getHeight());
                        this.offset = motionEvent.getY();
                        LockLandingView.this.landingScroll.setIsScrollable(false);
                        break;
                    case 1:
                        view.animate().setListener(new AnimatorListener() {
                            public void onAnimationCancel(Animator animator) {
                            }

                            public void onAnimationRepeat(Animator animator) {
                            }

                            public void onAnimationStart(Animator animator) {
                                view.setOnTouchListener(null);
                                LockLandingView.this.landingScroll.setIsScrollable(true);
                            }

                            public void onAnimationEnd(Animator animator) {
                                if (LockLandingView.this.mLockFound) {
                                    view.setOnTouchListener(LockLandingView.this.mTouchListener);
                                }
                            }
                        });
                        if (view.getY() != this.bottomY) {
                            LockLandingView.this.transitionButtonBar(1);
                            view.animate().y(this.startY).setInterpolator(new LinearInterpolator()).setDuration(300).start();
                            break;
                        } else {
                            LockLandingView.this.lockLandingPresenter.unlock();
                            view.animate().y(this.startY).setInterpolator(new DecelerateInterpolator(1.0f)).setDuration(1600).start();
                            break;
                        }
                    case 2:
                        float y = motionEvent.getY() - this.offset;
                        float y2 = view.getY() + y;
                        LockLandingView.this.transitionButtonBar(0);
                        float f = this.startY;
                        if (y2 >= f) {
                            float f2 = this.bottomY;
                            if (y2 <= f2) {
                                view.setY(view.getY() + y);
                                break;
                            } else {
                                view.setY(f2);
                                break;
                            }
                        } else {
                            view.setY(f);
                            break;
                        }
                }
                return true;
            }
        };
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            LockLanding lockLanding = (LockLanding) AppFlow.getScreen(getContext());
            this.mLock = lockLanding.mLock;
            this.mCalibration = lockLanding.mCalibration;
            lockLanding.mCalibration = false;
            this.lockLandingPresenter = new LockLandingPresenter(this.mLock, this);
            this.lockLandingPresenter.start();
            LayoutInflater from = LayoutInflater.from(getContext());
            this.mDefaultFooterView = from.inflate(C1075R.layout.lock_landing_default_footer, null);
            this.mTimerFooterView = from.inflate(C1075R.layout.lock_landing_timer_footer, null);
            this.footerStub = (ViewFlipper) findViewById(C1075R.C1077id.footer_stub);
            this.footerStub.addView(this.mDefaultFooterView, 0);
            this.footerStub.addView(this.mTimerFooterView, 1);
            ButterKnife.inject((View) this);
            this.lockIcon.setFactory(new ImageViewFactory());
            setUpSwipeAnimation();
            setupInitialBackgroundColor(this.mLock);
            updateLock(this.mLock);
            this.lockLandingPresenter.forceRescan();
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.lockLandingPresenter.setForceToolbarDraw(true);
            this.lockLandingPresenter.start();
            MasterLockSharedPreferences.getInstance().putLockLandingId(this.mLock.getLockId());
            if (this.mCalibration) {
                this.lockLandingPresenter.goToCalibration();
            }
        }
    }

    private void setUpSwipeAnimation() {
        PropertyValuesHolder ofKeyframe = PropertyValuesHolder.ofKeyframe("alpha", new Keyframe[]{Keyframe.ofFloat(0.0f, 0.3f), Keyframe.ofFloat(0.5f, 1.0f), Keyframe.ofFloat(1.0f, 0.3f)});
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this.lockArrowLarge, new PropertyValuesHolder[]{ofKeyframe});
        ObjectAnimator ofPropertyValuesHolder2 = ObjectAnimator.ofPropertyValuesHolder(this.lockArrowMedium, new PropertyValuesHolder[]{ofKeyframe});
        ObjectAnimator ofPropertyValuesHolder3 = ObjectAnimator.ofPropertyValuesHolder(this.lockArrowSmall, new PropertyValuesHolder[]{ofKeyframe});
        this.mArrowAnimatorSet = new AnimatorSet().setDuration(700);
        ofPropertyValuesHolder2.setStartDelay(166);
        ofPropertyValuesHolder3.setStartDelay(333);
        this.mArrowAnimatorSet.playTogether(new Animator[]{ofPropertyValuesHolder, ofPropertyValuesHolder2, ofPropertyValuesHolder3});
        this.mArrowAnimatorSet.addListener(new AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                LockLandingView.this.mArrowAnimatorSet.setStartDelay(200);
                LockLandingView.this.mArrowAnimatorSet.start();
            }
        });
    }

    private void clearLockIconTouchListener() {
        this.mLockFound = false;
        this.lockIcon.animate().withEndAction(null);
        this.lockIcon.setOnTouchListener(null);
    }

    public void setupInitialBackgroundColor(Lock lock) {
        if (lock.isLockerMode()) {
            setBackgroundColor(getResources().getColor(C1075R.color.locker_mode));
            return;
        }
        switch (lock.getLockStatus()) {
            case LOCK_FOUND:
                setBackgroundColor(getResources().getColor(C1075R.color.locked));
                this.mLockIsInFirmwareUpdate = false;
                return;
            case LOCKED:
                setBackgroundColor(getResources().getColor(C1075R.color.locked));
                this.mLockIsInFirmwareUpdate = false;
                return;
            case UNLOCKED:
                setBackgroundColor(getResources().getColor(C1075R.color.open));
                this.mLockIsInFirmwareUpdate = false;
                return;
            case UNLOCKING:
                setBackgroundColor(getResources().getColor(C1075R.color.open));
                this.mLockIsInFirmwareUpdate = false;
                return;
            case OPENED:
                setBackgroundColor(getResources().getColor(C1075R.color.open));
                this.mLockIsInFirmwareUpdate = false;
                return;
            case UPDATE_MODE:
                setBackgroundColor(getResources().getColor(C1075R.color.locker_update));
                this.mLockIsInFirmwareUpdate = true;
                updateButtonBar(true);
                return;
            default:
                setBackgroundColor(getResources().getColor(C1075R.color.primary));
                this.mLockIsInFirmwareUpdate = false;
                return;
        }
    }

    public void updateLock(Lock lock) {
        this.mArrowAnimatorSet.cancel();
        if (lock.getRemainingBatteryPercentage() < 0) {
            this.batteryIndicator.setVisibility(4);
        } else if (lock.getRemainingBatteryPercentage() <= getResources().getInteger(C1075R.integer.low_battery_percentage)) {
            this.batteryIndicator.setVisibility(0);
        } else {
            this.batteryIndicator.setVisibility(4);
        }
        if (lock.isLockerMode()) {
            updateToLockerMode(lock);
            this.wasInLockerMode = true;
        } else if (lock.getAccessType() != AccessType.GUEST) {
            updateLockState(lock);
        } else if (!AccessWindowUtil.hasStarted(lock)) {
            this.buttonBar.setVisibility(4);
            updateToAccessHaveNotStarted(lock);
        } else if (lock.getPermissions().getGuestInterface() == GuestInterface.ADVANCED && !AccessWindowUtil.isAllowedToday(lock)) {
            this.buttonBar.setVisibility(4);
            updateToAccessNotAvailableToday(lock);
        } else if (lock.getPermissions().getScheduleType() == ScheduleType.TWENTY_FOUR_SEVEN || AccessWindowUtil.isInsideSchedule(lock)) {
            updateLockState(lock);
        } else {
            this.buttonBar.setVisibility(4);
            updateToOutOfSchedule(lock);
        }
    }

    private void updateLockState(Lock lock) {
        switch (lock.getLockStatus()) {
            case LOCK_FOUND:
                updateToLockFound(lock);
                return;
            case LOCKED:
                updateToLocked(lock);
                return;
            case UNLOCKED:
                updateToUnlocked(lock);
                return;
            case UNLOCKING:
                updateToUnlocking(lock);
                return;
            case OPENED:
                updateToOpened(lock);
                updateDefaultFooter(lock);
                return;
            case UPDATE_MODE:
                updateToFirmwareUpdate(lock);
                return;
            default:
                updateToUnreachable(lock);
                return;
        }
    }

    /* access modifiers changed from: 0000 */
    public void updateToUnreachable(Lock lock) {
        transitionBackgroundColor(getResources().getColor(C1075R.color.primary));
        this.footerStub.setDisplayedChild(0);
        transitionButtonBar(1);
        this.stateTitle.setText(getResources().getString(C1075R.string.lock_unreachable));
        if (lock.getLockMode() == LockMode.TOUCH) {
            this.stateBody.setText(getResources().getString(C1075R.string.unreachable_state_body_touch));
        } else {
            this.stateBody.setText(getResources().getString(C1075R.string.unreachable_state_body_proximity));
        }
        ImageSwitcher imageSwitcher = this.lockIcon;
        int i = lock.isPadLock() ? C1075R.C1076drawable.ic_locked_white : lock.isShackledKeySafe() ? C1075R.C1076drawable.ic_shackled_keysafe_locked_white : C1075R.C1076drawable.ic_wallmount_keysafe_locked;
        imageSwitcher.setImageResource(i);
        clearLockIconTouchListener();
        updateHeader(false);
        updateDefaultFooter(lock);
        updateButtonBar(false);
    }

    /* access modifiers changed from: 0000 */
    public void updateToLockFound(Lock lock) {
        transitionBackgroundColor(getResources().getColor(C1075R.color.locked));
        this.footerStub.setDisplayedChild(0);
        transitionButtonBar(1);
        this.stateTitle.setText(getResources().getString(C1075R.string.lock_found_state_title));
        this.stateBody.setText(C1075R.string.lock_found_state_body);
        this.lockIcon.setImageDrawable(getResources().getDrawable(C1075R.C1076drawable.ic_lockfound_white));
        this.mLockFound = true;
        this.lockIcon.setOnTouchListener(this.mTouchListener);
        updateHeader(true);
        updateDefaultFooter(lock);
        updateButtonBar(false);
        this.mArrowAnimatorSet.start();
    }

    /* access modifiers changed from: 0000 */
    public void updateToUnlocked(Lock lock) {
        if (!this.wasInLockerMode) {
            initializeTimer(((long) (lock.getRelockTimeInSeconds() * 1000)) - (System.currentTimeMillis() - lock.getLastUnlocked()));
            this.footerStub.setDisplayedChild(1);
        } else {
            this.wasInLockerMode = false;
        }
        transitionBackgroundColor(getResources().getColor(C1075R.color.open));
        transitionButtonBar(0);
        this.stateTitle.setText(getResources().getString(C1075R.string.unlocked_state_title));
        this.stateBody.setText(getResources().getString(C1075R.string.unlocked_state_body));
        this.lockIcon.setInAnimation(AnimationUtils.loadAnimation(getContext(), 17432576));
        this.lockIcon.setOutAnimation(AnimationUtils.loadAnimation(getContext(), 17432577));
        ImageSwitcher imageSwitcher = this.lockIcon;
        Resources resources = getResources();
        int i = lock.isPadLock() ? C1075R.C1076drawable.ic_locked_white : lock.isShackledKeySafe() ? C1075R.C1076drawable.ic_shackled_keysafe_locked_white : C1075R.C1076drawable.ic_wallmount_keysafe_locked;
        imageSwitcher.setImageDrawable(resources.getDrawable(i));
        clearLockIconTouchListener();
        updateHeader(false);
        updateButtonBar(false);
    }

    /* access modifiers changed from: 0000 */
    public void updateToUnlocking(Lock lock) {
        this.footerStub.setDisplayedChild(0);
        transitionBackgroundColor(getResources().getColor(C1075R.color.open));
        transitionButtonBar(0);
        this.stateTitle.setText(getResources().getString(C1075R.string.unlocking_state_title));
        this.stateBody.setText(getResources().getString(C1075R.string.unlocking_state_body));
        this.lockIcon.setInAnimation(AnimationUtils.loadAnimation(getContext(), 17432576));
        this.lockIcon.setOutAnimation(AnimationUtils.loadAnimation(getContext(), 17432577));
        ImageSwitcher imageSwitcher = this.lockIcon;
        Resources resources = getResources();
        int i = lock.isPadLock() ? C1075R.C1076drawable.ic_locked_white : lock.isShackledKeySafe() ? C1075R.C1076drawable.ic_shackled_keysafe_locked_white : C1075R.C1076drawable.ic_wallmount_keysafe_locked;
        imageSwitcher.setImageDrawable(resources.getDrawable(i));
        clearLockIconTouchListener();
        updateHeader(false);
        updateButtonBar(false);
        updateDefaultFooter(lock);
    }

    /* access modifiers changed from: 0000 */
    public void updateToOpened(Lock lock) {
        this.footerStub.setDisplayedChild(0);
        transitionBackgroundColor(getResources().getColor(C1075R.color.open));
        transitionButtonBar(0);
        this.stateTitle.setText(getResources().getString(C1075R.string.opened_state_title));
        this.stateBody.setText(getResources().getString(C1075R.string.opened_state_body));
        this.lockIcon.setImageDrawable(getResources().getDrawable(C1075R.C1076drawable.ic_unlocked_white));
        clearLockIconTouchListener();
        updateHeader(false);
        updateButtonBar(false);
    }

    /* access modifiers changed from: 0000 */
    public void updateToLocked(Lock lock) {
        this.footerStub.setDisplayedChild(0);
        transitionBackgroundColor(getResources().getColor(C1075R.color.locked));
        transitionButtonBar(1);
        this.stateTitle.setText(getResources().getString(C1075R.string.locked_state_title));
        this.stateBody.setText(getResources().getString(C1075R.string.locked_state_body));
        this.lockIcon.setImageDrawable(getResources().getDrawable(C1075R.C1076drawable.ic_lockfound_white));
        clearLockIconTouchListener();
        updateHeader(false);
        updateDefaultFooter(lock);
        updateButtonBar(false);
    }

    /* access modifiers changed from: 0000 */
    public void updateToLockerMode(Lock lock) {
        int i;
        Resources resources;
        this.footerStub.setDisplayedChild(0);
        transitionBackgroundColor(getResources().getColor(C1075R.color.locker_mode));
        transitionButtonBar(1);
        TextView textView = this.stateTitle;
        if (lock.isPadLock()) {
            resources = getResources();
            i = C1075R.string.locker_mode_title;
        } else {
            resources = getResources();
            i = C1075R.string.lock_communications_disabled;
        }
        textView.setText(resources.getString(i));
        this.stateBody.setText(getResources().getString(C1075R.string.locker_mode_body));
        this.lockIcon.setImageDrawable(getResources().getDrawable(lock.isPadLock() ? C1075R.C1076drawable.ic_lockermode_white : C1075R.C1076drawable.ic_keysafe_communications_disabled));
        clearLockIconTouchListener();
        updateHeader(false);
        updateDefaultFooter(lock);
        updateButtonBar(true);
    }

    /* access modifiers changed from: 0000 */
    public void updateToFirmwareUpdate(Lock lock) {
        this.footerStub.setDisplayedChild(0);
        transitionBackgroundColor(getResources().getColor(C1075R.color.locker_update));
        transitionButtonBar(1);
        this.stateTitle.setText(getResources().getString(C1075R.string.firmware_update_state_title));
        this.stateBody.setText(getResources().getString(C1075R.string.firmware_update_state_body));
        ImageSwitcher imageSwitcher = this.lockIcon;
        Resources resources = getResources();
        int i = lock.isPadLock() ? C1075R.C1076drawable.ic_locked_white : lock.isShackledKeySafe() ? C1075R.C1076drawable.ic_shackled_keysafe_locked_white : C1075R.C1076drawable.ic_wallmount_keysafe_locked;
        imageSwitcher.setImageDrawable(resources.getDrawable(i));
        clearLockIconTouchListener();
        this.mLockIsInFirmwareUpdate = true;
        updateHeader(false);
        updateDefaultFooter(lock);
        updateButtonBar(true);
    }

    /* access modifiers changed from: 0000 */
    public void updateToOutOfSchedule(Lock lock) {
        this.footerStub.setDisplayedChild(0);
        transitionBackgroundColor(getResources().getColor(C1075R.color.guest_mode_out_of_schedule));
        transitionButtonBar(0);
        if (lock.getPermissions().getScheduleType() == ScheduleType.SEVEN_AM_TO_SEVEN_PM) {
            this.stateTitle.setText(getResources().getString(C1075R.string.guest_detail_access_day));
            this.stateBody.setText(String.format(getResources().getString(C1075R.string.service_code_available_7am_7pm), new Object[]{lock.getTimeZoneOffset(), lock.getLocalizedTimeZone()}));
            this.lockIcon.setImageDrawable(getResources().getDrawable(C1075R.C1076drawable.ic_out_of_schedule_day_white));
        } else if (lock.getPermissions().getScheduleType() == ScheduleType.SEVEN_PM_TO_SEVEN_AM) {
            this.stateTitle.setText(getResources().getString(C1075R.string.guest_detail_access_night));
            this.stateBody.setText(String.format(getResources().getString(C1075R.string.service_code_available_7pm_7am), new Object[]{lock.getTimeZoneOffset(), lock.getLocalizedTimeZone()}));
            this.lockIcon.setImageDrawable(getResources().getDrawable(C1075R.C1076drawable.ic_out_of_schedule_night_white));
        }
        clearLockIconTouchListener();
        updateHeader(false);
        updateDefaultFooter(lock);
        updateButtonBar(false);
    }

    /* access modifiers changed from: 0000 */
    public void updateToAccessHaveNotStarted(Lock lock) {
        this.footerStub.setDisplayedChild(0);
        transitionBackgroundColor(getResources().getColor(C1075R.color.guest_mode_out_of_schedule));
        transitionButtonBar(0);
        this.stateBody.setText(getResources().getString(C1075R.string.guest_detail_access_have_not_started));
        this.lockIcon.setImageResource(C1075R.C1076drawable.ic_locklanding_no_access_today);
        clearLockIconTouchListener();
        updateHeader(false);
        updateDefaultFooter(lock);
        updateButtonBar(false);
    }

    /* access modifiers changed from: 0000 */
    public void updateToAccessNotAvailableToday(Lock lock) {
        String str;
        this.stateTitle.setText(C1075R.string.guest_detail_access_scheduled);
        this.footerStub.setDisplayedChild(0);
        transitionBackgroundColor(getResources().getColor(C1075R.color.guest_mode_out_of_schedule));
        transitionButtonBar(0);
        switch (lock.getPermissions().getScheduleType()) {
            case SEVEN_AM_TO_SEVEN_PM:
                str = String.format(getResources().getString(C1075R.string.lock_landing_guest_from_schedule), new Object[]{getResources().getString(C1075R.string.guest_detail_7am_7pm), lock.getLocalizedTimeZone()});
                break;
            case SEVEN_PM_TO_SEVEN_AM:
                str = String.format(getResources().getString(C1075R.string.lock_landing_guest_from_schedule), new Object[]{getResources().getString(C1075R.string.guest_detail_7pm_7am), lock.getLocalizedTimeZone()});
                break;
            default:
                str = "";
                break;
        }
        this.stateBody.setText(String.format(getResources().getString(C1075R.string.lock_landing_guest_no_access_today), new Object[]{lock.getPermissions().getSelectedDaysFormattedFullName(), str}));
        this.lockIcon.setImageResource(C1075R.C1076drawable.ic_locklanding_no_access_today);
        clearLockIconTouchListener();
        updateHeader(false);
        updateDefaultFooter(lock);
        updateButtonBar(false);
    }

    private void updateButtonBar(boolean z) {
        if (this.mLockIsInFirmwareUpdate) {
            this.lockerModeApplied.setText(getResources().getString(C1075R.string.continue_firmware_update_label));
            this.lockerModeToggle.setText(getResources().getString(C1075R.string.continue_firmware_update));
        } else if (this.mLock.isPadLock()) {
            if (z) {
                this.lockerModeToggle.setText(getResources().getString(C1075R.string.locker_mode_disable));
                this.lockerModeApplied.setVisibility(0);
                return;
            }
            this.lockerModeToggle.setText(getResources().getString(C1075R.string.locker_mode_apply));
            this.lockerModeApplied.setVisibility(4);
        } else if (z) {
            this.lockerModeToggle.setText(getResources().getString(C1075R.string.lock_communications_enable));
            this.lockerModeApplied.setText(getResources().getString(C1075R.string.lock_communications_disabled));
            this.lockerModeApplied.setVisibility(0);
        } else {
            this.lockerModeToggle.setText(getResources().getString(C1075R.string.lock_communications_disable));
            this.lockerModeApplied.setText(getResources().getString(C1075R.string.lock_communications_enabled));
            this.lockerModeApplied.setVisibility(0);
        }
    }

    private void updateDefaultFooter(Lock lock) {
        if (lock != null) {
            this.lockName.setText(getResources().getString(C1075R.string.about_lock_name, new Object[]{lock.getName()}));
            this.deviceId.setText(lock.getKmsDeviceKey().getDeviceId());
            String str = "";
            if (lock.getAccessType() == AccessType.GUEST) {
                if (AccessWindowUtil.hasStarted(lock)) {
                    if (lock.getKmsDeviceKey() != null) {
                        switch (lock.getPermissions().getScheduleType()) {
                            case SEVEN_AM_TO_SEVEN_PM:
                                str = getResources().getString(C1075R.string.day);
                                break;
                            case SEVEN_PM_TO_SEVEN_AM:
                                str = getResources().getString(C1075R.string.night);
                                break;
                            case TWENTY_FOUR_SEVEN:
                                str = getResources().getString(C1075R.string.unlimited);
                                break;
                            default:
                                str = "";
                                break;
                        }
                    }
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(getResources().getString(C1075R.string.guest_detail_access_start));
                    sb.append(DateTimeFormat.forPattern(getContext().getString(C1075R.string.guest_detail_temporary_code_date_time)).print((ReadableInstant) new DateTime((Object) lock.getPermissions().getStartAtDate())));
                    str = sb.toString();
                }
            } else {
                str = parseLastLockRecord(lock);
            }
            this.auditDetail.setText(str);
        }
    }

    private String parseLastLockRecord(Lock lock) {
        String str = "";
        if (lock == null || lock.getLogs() == null || lock.getLogs().size() <= 0) {
            return str;
        }
        KmsLogEntry kmsLogEntry = (KmsLogEntry) lock.getLogs().get(0);
        StringBuilder sb = new StringBuilder(kmsLogEntry.getMessage());
        sb.append(" • ");
        try {
            sb.append(MLDateUtils.parseServerDateFormat(kmsLogEntry.getCreatedOn(), getResources()));
        } catch (ParseException e) {
            Log.e(TAG, "getView: failed parsing date", e);
        }
        return sb.toString();
    }

    private void updateHeader(boolean z) {
        if (z) {
            this.lockArrowLarge.setVisibility(0);
            this.lockArrowMedium.setVisibility(0);
            this.lockArrowSmall.setVisibility(0);
            return;
        }
        this.lockArrowLarge.setVisibility(8);
        this.lockArrowMedium.setVisibility(8);
        this.lockArrowSmall.setVisibility(8);
    }

    /* access modifiers changed from: 0000 */
    public void transitionBackgroundColor(int i) {
        int color = ((ColorDrawable) getBackground()).getColor();
        ValueAnimator ofObject = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{Integer.valueOf(color), Integer.valueOf(i)});
        ofObject.addUpdateListener(new AnimatorUpdateListener() {
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                LockLandingView.this.setBackgroundColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
            }
        });
        ofObject.start();
    }

    /* access modifiers changed from: 0000 */
    public void transitionButtonBar(int i) {
        if (getBottom() > 0) {
            int height = this.buttonBar.getHeight();
            if (i == 0 && this.mButtonBarState != 0) {
                this.buttonBar.animate().y((float) getBottom()).start();
                this.mButtonBarState = 0;
            } else if (i == 1 && this.mButtonBarState != 1) {
                this.buttonBar.animate().y((float) (getBottom() - height)).start();
                this.mButtonBarState = 1;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void initializeTimer(long j) {
        CountDownTimer countDownTimer = this.mCountDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        C15573 r1 = new CountDownTimer(j, 250) {
            int secondsLeft;

            public void onTick(long j) {
                float f = ((float) j) / 1000.0f;
                if (Math.round(f) != this.secondsLeft) {
                    this.secondsLeft = Math.round(f);
                    LockLandingView.this.relockTime.setText(LockLandingView.this.getResources().getString(C1075R.string.relock_time, new Object[]{Integer.valueOf(this.secondsLeft)}));
                }
            }

            public void onFinish() {
                LockLandingView.this.mCountDownTimer = null;
                LockLandingView.this.lockLandingPresenter.relockTimeExpired();
            }
        };
        this.mCountDownTimer = r1;
        this.mCountDownTimer.start();
    }

    private void cancelTimer() {
        CountDownTimer countDownTimer = this.mCountDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void displayError(Throwable th) {
        Toast.makeText(getContext(), th.getMessage() != null ? th.getMessage() : th.getClass().getSimpleName(), 0).show();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.lockLandingPresenter.finish();
        cancelTimer();
        AnimatorSet animatorSet = this.mArrowAnimatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        ViewUtil.hideKeyboard(getContext(), getWindowToken());
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296608})
    public void toggleLockerMode() {
        if (this.mLockIsInFirmwareUpdate) {
            this.lockLandingPresenter.updateToolbarToDefault();
            AppFlow.get(getContext()).goTo(new AboutLockKeySafe(this.mLock));
            return;
        }
        this.lockLandingPresenter.toggleLockerMode();
    }

    public boolean onBackPressed() {
        this.lockLandingPresenter.onBackPressed();
        return false;
    }

    public boolean onUpPressed() {
        this.lockLandingPresenter.onUpPressed();
        return false;
    }
}
