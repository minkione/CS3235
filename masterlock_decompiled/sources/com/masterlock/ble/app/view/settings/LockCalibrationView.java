package com.masterlock.ble.app.view.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.settings.LockCalibrationPresenter;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.ble.app.view.settings.calibration.CalibrationCalibratingView;
import com.masterlock.ble.app.view.settings.calibration.CalibrationCompleteView;
import com.masterlock.ble.app.view.settings.calibration.CalibrationNotFoundView;
import com.masterlock.ble.app.view.settings.calibration.CalibrationStandBackView;
import com.masterlock.ble.app.view.settings.calibration.CalibrationSummaryView;
import com.masterlock.ble.app.view.settings.calibration.CalibrationWakeView;
import com.square.flow.screenswitcher.HandlesBack;
import com.square.flow.screenswitcher.HandlesUp;

public class LockCalibrationView extends LinearLayout implements IAuthenticatedView, HandlesBack, HandlesUp {
    @InjectView(2131296422)
    TextView deviceIdTxt;
    @InjectView(2131296361)
    View mButtonBar;
    CalibrationCalibratingView mCalibrationCalibratingView;
    CalibrationCompleteView mCalibrationCompleteView;
    CalibrationNotFoundView mCalibrationNotFoundView;
    CalibrationStandBackView mCalibrationStandBackView;
    LockCalibrationStep mCalibrationStep;
    CalibrationSummaryView mCalibrationSummaryView;
    CalibrationWakeView mCalibrationWakeView;
    @InjectView(2131296392)
    ViewFlipper mContentFlipper;
    LockCalibrationPresenter mLockCalibrationPresenter;
    @InjectView(2131296597)
    TextView mLockNameBanner;
    @InjectView(2131296656)
    Button mPrimaryActionButton;
    @InjectView(2131296741)
    Button mSecondaryActionButton;
    @InjectView(2131296810)
    Button mTopActionButton;
    @InjectView(2131296813)
    View mTopButtonBar;

    public enum LockCalibrationStep {
        STEP_WAKE(0),
        STEP_STAND_BACK(1),
        STEP_CALIBRATING(2),
        STEP_COMPLETE(3),
        STEP_SUMMARY(4),
        STEP_NOT_FOUND(5);
        
        private int value;

        private LockCalibrationStep(int i) {
            this.value = i;
        }

        public static LockCalibrationStep fromKey(int i) {
            LockCalibrationStep[] values;
            for (LockCalibrationStep lockCalibrationStep : values()) {
                if (lockCalibrationStep.getValue() == i) {
                    return lockCalibrationStep;
                }
            }
            return STEP_NOT_FOUND;
        }

        public int getValue() {
            return this.value;
        }
    }

    public LockCalibrationView(Context context) {
        this(context, null);
    }

    public LockCalibrationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mCalibrationStep = LockCalibrationStep.STEP_WAKE;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mLockCalibrationPresenter = new LockCalibrationPresenter(this);
            initContentFlipper();
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mLockCalibrationPresenter.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mLockCalibrationPresenter.finish();
    }

    private void initContentFlipper() {
        LayoutInflater from = LayoutInflater.from(getContext());
        this.mCalibrationWakeView = (CalibrationWakeView) from.inflate(C1075R.layout.calibration_wake_view, this.mContentFlipper, false);
        this.mContentFlipper.addView(this.mCalibrationWakeView, LockCalibrationStep.STEP_WAKE.getValue());
        this.mCalibrationStandBackView = (CalibrationStandBackView) from.inflate(C1075R.layout.calibration_stand_back_view, this.mContentFlipper, false);
        this.mContentFlipper.addView(this.mCalibrationStandBackView, LockCalibrationStep.STEP_STAND_BACK.getValue());
        this.mCalibrationCalibratingView = (CalibrationCalibratingView) from.inflate(C1075R.layout.calibration_calibrating_view, this.mContentFlipper, false);
        this.mContentFlipper.addView(this.mCalibrationCalibratingView, LockCalibrationStep.STEP_CALIBRATING.getValue());
        this.mCalibrationCompleteView = (CalibrationCompleteView) from.inflate(C1075R.layout.calibration_complete_view, this.mContentFlipper, false);
        this.mContentFlipper.addView(this.mCalibrationCompleteView, LockCalibrationStep.STEP_COMPLETE.getValue());
        this.mCalibrationSummaryView = (CalibrationSummaryView) from.inflate(C1075R.layout.calibration_summary_view, this.mContentFlipper, false);
        this.mContentFlipper.addView(this.mCalibrationSummaryView, LockCalibrationStep.STEP_SUMMARY.getValue());
        this.mCalibrationNotFoundView = (CalibrationNotFoundView) from.inflate(C1075R.layout.calibration_not_found_view, this.mContentFlipper, false);
        this.mContentFlipper.addView(this.mCalibrationNotFoundView, LockCalibrationStep.STEP_NOT_FOUND.getValue());
    }

    public void setLockName(String str) {
        this.mLockNameBanner.setText(String.format(getResources().getString(C1075R.string.about_lock_name), new Object[]{str}));
    }

    public void setDeviceId(String str) {
        this.deviceIdTxt.setText(str);
    }

    private void displayWake() {
        this.mCalibrationStep = LockCalibrationStep.STEP_WAKE;
        this.mContentFlipper.setDisplayedChild(LockCalibrationStep.STEP_WAKE.getValue());
        updateButtons();
    }

    private void displayStandBack() {
        this.mCalibrationStep = LockCalibrationStep.STEP_STAND_BACK;
        this.mContentFlipper.setDisplayedChild(LockCalibrationStep.STEP_STAND_BACK.getValue());
        updateButtons();
    }

    private void displayCalibrating() {
        this.mCalibrationStep = LockCalibrationStep.STEP_CALIBRATING;
        this.mContentFlipper.setDisplayedChild(LockCalibrationStep.STEP_CALIBRATING.getValue());
        updateButtons();
    }

    private void displayComplete() {
        this.mCalibrationStep = LockCalibrationStep.STEP_COMPLETE;
        this.mContentFlipper.setDisplayedChild(LockCalibrationStep.STEP_COMPLETE.getValue());
        updateButtons();
    }

    private void displaySummary() {
        this.mCalibrationStep = LockCalibrationStep.STEP_SUMMARY;
        this.mContentFlipper.setDisplayedChild(LockCalibrationStep.STEP_SUMMARY.getValue());
        updateButtons();
    }

    private void displayNotFound() {
        this.mCalibrationStep = LockCalibrationStep.STEP_NOT_FOUND;
        this.mContentFlipper.setDisplayedChild(LockCalibrationStep.STEP_NOT_FOUND.getValue());
        updateButtons();
    }

    private void updateButtons() {
        switch (this.mCalibrationStep) {
            case STEP_WAKE:
                this.mPrimaryActionButton.setText(getResources().getString(C1075R.string.primary_try_later));
                this.mPrimaryActionButton.setVisibility(0);
                this.mSecondaryActionButton.setVisibility(8);
                this.mTopButtonBar.setVisibility(8);
                return;
            case STEP_STAND_BACK:
                this.mPrimaryActionButton.setText(C1075R.string.calibration_ok_ready);
                this.mPrimaryActionButton.setVisibility(0);
                this.mSecondaryActionButton.setVisibility(8);
                this.mTopButtonBar.setVisibility(8);
                return;
            case STEP_CALIBRATING:
                this.mPrimaryActionButton.setVisibility(8);
                this.mSecondaryActionButton.setVisibility(8);
                this.mTopButtonBar.setVisibility(8);
                return;
            case STEP_COMPLETE:
                this.mPrimaryActionButton.setText(getResources().getString(C1075R.string.f165ok));
                this.mPrimaryActionButton.setVisibility(0);
                this.mSecondaryActionButton.setVisibility(8);
                this.mTopButtonBar.setVisibility(8);
                return;
            case STEP_SUMMARY:
                this.mPrimaryActionButton.setText(C1075R.string.calibration_remove);
                this.mPrimaryActionButton.setVisibility(0);
                this.mSecondaryActionButton.setVisibility(8);
                this.mTopButtonBar.setVisibility(0);
                return;
            default:
                this.mPrimaryActionButton.setText(getResources().getString(C1075R.string.primary_try_again));
                this.mPrimaryActionButton.setVisibility(0);
                this.mSecondaryActionButton.setText(getResources().getString(C1075R.string.primary_try_later));
                this.mSecondaryActionButton.setVisibility(0);
                this.mTopButtonBar.setVisibility(8);
                return;
        }
    }

    public void updateView(LockCalibrationStep lockCalibrationStep) {
        switch (lockCalibrationStep) {
            case STEP_WAKE:
                displayWake();
                break;
            case STEP_STAND_BACK:
                displayStandBack();
                break;
            case STEP_CALIBRATING:
                displayCalibrating();
                break;
            case STEP_COMPLETE:
                displayComplete();
                break;
            case STEP_SUMMARY:
                displaySummary();
                break;
            case STEP_NOT_FOUND:
                displayNotFound();
                break;
        }
        updateButtons();
    }

    @OnClick({2131296656, 2131296741, 2131296810})
    public void onActionClicked(View view) {
        if (view.getId() == C1075R.C1077id.primary_action_button) {
            switch (this.mCalibrationStep) {
                case STEP_WAKE:
                    this.mLockCalibrationPresenter.tryLater();
                    return;
                case STEP_STAND_BACK:
                    this.mLockCalibrationPresenter.calibrate();
                    return;
                case STEP_COMPLETE:
                    this.mLockCalibrationPresenter.onBackPressed();
                    return;
                case STEP_SUMMARY:
                    this.mLockCalibrationPresenter.removeCalibration();
                    return;
                case STEP_NOT_FOUND:
                    this.mLockCalibrationPresenter.tryAgain();
                    return;
                default:
                    return;
            }
        } else if (view.getId() == C1075R.C1077id.secondary_action_button) {
            switch (this.mCalibrationStep) {
                case STEP_SUMMARY:
                    this.mLockCalibrationPresenter.removeCalibration();
                    return;
                case STEP_NOT_FOUND:
                    this.mLockCalibrationPresenter.tryLater();
                    return;
                default:
                    return;
            }
        } else if (view.getId() == C1075R.C1077id.top_action_button && C15961.f180x51fe869[this.mCalibrationStep.ordinal()] == 5) {
            this.mLockCalibrationPresenter.tryAgain();
        }
    }

    public void showRemovedCalibrationConfirmation() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.calibration_removed), 1).show();
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    public boolean onBackPressed() {
        this.mLockCalibrationPresenter.onBackPressed();
        return true;
    }

    public boolean onUpPressed() {
        this.mLockCalibrationPresenter.onBackPressed();
        return true;
    }
}
