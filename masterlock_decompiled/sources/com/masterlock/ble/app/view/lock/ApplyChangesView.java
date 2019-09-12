package com.masterlock.ble.app.view.lock;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.lock.ApplyChangesPresenter;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.ble.app.view.lock.keysafe.SecondaryCodeSummaryView;
import com.masterlock.ble.app.view.settings.UpdateFirmwareSummaryView;
import com.masterlock.core.Lock;
import com.masterlock.core.LockConfigAction;
import com.masterlock.core.LockMode;
import com.masterlock.core.LockStatus;
import com.square.flow.screenswitcher.HandlesBack;

public class ApplyChangesView extends LinearLayout implements IAuthenticatedView, HandlesBack {
    private static final int CALIBRATE_SUMMARY = 5;
    private static final int LOCK_MODE_SUMMARY = 3;
    public static final String LOG_TAG = "ApplyChangesView";
    private static final int PRIMARY_CODE_SUMMARY = 1;
    private static final int RELOCK_TIME_SUMMARY = 2;
    private static final int RESET_KEYS_SUMMARY = 4;
    public static final int SYNCING = 0;
    private static final int UPDATE_LOCK_SUMMARY = 6;
    private static final int UPDATE_SECONDARY_CODES_SUMMARY = 7;
    ApplyChangesPresenter applyChangesPresenter;
    @InjectView(2131296361)
    View mButtonBar;
    CalibrateSummaryView mCalibrateSummaryView;
    @InjectView(2131296392)
    ViewFlipper mContentFlipper;
    Handler mHandler;
    @InjectView(2131296656)
    Button mPrimaryActionButton;
    RelockTimeSummaryView mRelockTimeSummaryView;
    ResetKeysSummaryView mResetKeysSummaryView;
    @InjectView(2131296741)
    Button mSecondaryActionButton;
    SecondaryCodeSummaryView mSecondaryCodeSummaryView;
    SyncingView mSyncingView;
    UnlockModeSummaryView mUnlockModeSummaryView;
    UpdateFirmwareSummaryView mUpdateLockView;
    ViewState mViewState;
    PrimaryCodeSummaryView primaryCodeSummaryView;
    private int totalProgress;

    enum ViewState {
        SEARCHING,
        FAILED,
        SYNCING,
        ACTION_SUMMARY
    }

    public ApplyChangesView(Context context) {
        this(context, null);
    }

    public ApplyChangesView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.totalProgress = 0;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.applyChangesPresenter = new ApplyChangesPresenter(this);
            this.mHandler = new Handler();
            initContentFlipper();
        }
    }

    private void initContentFlipper() {
        LayoutInflater from = LayoutInflater.from(getContext());
        this.mSyncingView = (SyncingView) from.inflate(C1075R.layout.apply_changes_syncing, this.mContentFlipper, false);
        this.mContentFlipper.addView(this.mSyncingView, 0);
        this.primaryCodeSummaryView = (PrimaryCodeSummaryView) from.inflate(C1075R.layout.apply_changes_primary_code_summary, this.mContentFlipper, false);
        this.mContentFlipper.addView(this.primaryCodeSummaryView, 1);
        this.mRelockTimeSummaryView = (RelockTimeSummaryView) from.inflate(C1075R.layout.apply_changes_relock_time_summary, this.mContentFlipper, false);
        this.mContentFlipper.addView(this.mRelockTimeSummaryView, 2);
        this.mUnlockModeSummaryView = (UnlockModeSummaryView) from.inflate(C1075R.layout.apply_changes_unlock_mode_summary, this.mContentFlipper, false);
        this.mContentFlipper.addView(this.mUnlockModeSummaryView, 3);
        this.mResetKeysSummaryView = (ResetKeysSummaryView) from.inflate(C1075R.layout.apply_changes_reset_keys_summary, this.mContentFlipper, false);
        this.mContentFlipper.addView(this.mResetKeysSummaryView, 4);
        this.mCalibrateSummaryView = (CalibrateSummaryView) from.inflate(C1075R.layout.apply_changes_calibrate_summary, this.mContentFlipper, false);
        this.mContentFlipper.addView(this.mCalibrateSummaryView, 5);
        this.mUpdateLockView = (UpdateFirmwareSummaryView) from.inflate(C1075R.layout.apply_firmware_update_summary, this.mContentFlipper, false);
        this.mContentFlipper.addView(this.mUpdateLockView, 6);
        this.mSecondaryCodeSummaryView = (SecondaryCodeSummaryView) from.inflate(C1075R.layout.apply_changes_secondary_codes_summary, this.mContentFlipper, false);
        this.mContentFlipper.addView(this.mSecondaryCodeSummaryView, 7);
    }

    public void displaySearching() {
        this.mViewState = ViewState.SEARCHING;
        this.mContentFlipper.setDisplayedChild(0);
        this.mSyncingView.searching();
        updateButtons(this.mSyncingView);
    }

    public void displaySyncing() {
        this.mViewState = ViewState.SYNCING;
        this.mContentFlipper.setDisplayedChild(0);
        this.mSyncingView.syncing();
        updateButtons(this.mSyncingView);
    }

    public void displayFirmwareSyncing(int i) {
        this.mViewState = ViewState.SYNCING;
        this.mContentFlipper.setDisplayedChild(0);
        this.totalProgress = i;
        this.mSyncingView.mFillImageProgressBar.setProgressMax(i);
        this.mSyncingView.setTotalProgress(i);
        this.mSyncingView.syncingFirmwareUpdate();
        updateButtons(this.mSyncingView);
    }

    public int getTotalProgress() {
        return this.totalProgress;
    }

    public void displayUpdateRestoreConfig() {
        this.mViewState = ViewState.SYNCING;
        this.mContentFlipper.setDisplayedChild(0);
        this.mSyncingView.syncingRestoreConfig();
        updateButtons(this.mSyncingView);
    }

    public void setTotalProgress(int i) {
        this.mSyncingView.mFillImageProgressBar.setProgressMax(i);
        this.mSyncingView.setTotalProgress(i);
        this.totalProgress = i;
    }

    public void updateProgress(int i) {
        SyncingView syncingView = this.mSyncingView;
        if (i != 0) {
            i = this.totalProgress - i;
        }
        syncingView.updateImageLevel(i);
    }

    public void displaySyncFailure() {
        this.mViewState = ViewState.FAILED;
        this.mContentFlipper.setDisplayedChild(0);
        this.mSyncingView.updateImageLevel(0);
        this.totalProgress = 0;
        updateProgress(0);
        this.mSyncingView.failure();
        updateButtons(this.mSyncingView);
    }

    public void displayActionSummary(LockConfigAction lockConfigAction, Lock lock) {
        this.mViewState = ViewState.ACTION_SUMMARY;
        switch (lockConfigAction) {
            case LOCK_MODE:
                this.mHandler.post(new Runnable(lock) {
                    private final /* synthetic */ Lock f$1;

                    {
                        this.f$1 = r2;
                    }

                    public final void run() {
                        ApplyChangesView.lambda$displayActionSummary$0(ApplyChangesView.this, this.f$1);
                    }
                });
                return;
            case PRIMARY_CODE:
                this.mHandler.post(new Runnable(lock) {
                    private final /* synthetic */ Lock f$1;

                    {
                        this.f$1 = r2;
                    }

                    public final void run() {
                        ApplyChangesView.lambda$displayActionSummary$1(ApplyChangesView.this, this.f$1);
                    }
                });
                return;
            case SECONDARY_CODES:
                this.mHandler.post(new Runnable() {
                    public final void run() {
                        ApplyChangesView.lambda$displayActionSummary$2(ApplyChangesView.this);
                    }
                });
                return;
            case RELOCK_TIME:
                this.mHandler.post(new Runnable(lock) {
                    private final /* synthetic */ Lock f$1;

                    {
                        this.f$1 = r2;
                    }

                    public final void run() {
                        ApplyChangesView.lambda$displayActionSummary$3(ApplyChangesView.this, this.f$1);
                    }
                });
                return;
            case RESET_KEYS:
                this.mHandler.post(new Runnable() {
                    public final void run() {
                        ApplyChangesView.lambda$displayActionSummary$4(ApplyChangesView.this);
                    }
                });
                return;
            case UPDATE_FIRMWARE:
                this.mHandler.post(new Runnable() {
                    public final void run() {
                        ApplyChangesView.lambda$displayActionSummary$5(ApplyChangesView.this);
                    }
                });
                return;
            default:
                return;
        }
    }

    public static /* synthetic */ void lambda$displayActionSummary$0(ApplyChangesView applyChangesView, Lock lock) {
        applyChangesView.mContentFlipper.setDisplayedChild(3);
        applyChangesView.mUnlockModeSummaryView.updateLockMode(lock.getLockMode());
        applyChangesView.applyChangesPresenter.updateStatus(LockStatus.UNREACHABLE);
        applyChangesView.updateButtons(applyChangesView.mUnlockModeSummaryView);
        if (lock.getLockMode() == LockMode.PROXIMITYSWIPE) {
            applyChangesView.applyChangesPresenter.sendRequestNotificationPermissionEvent();
        }
    }

    public static /* synthetic */ void lambda$displayActionSummary$1(ApplyChangesView applyChangesView, Lock lock) {
        applyChangesView.mContentFlipper.setDisplayedChild(1);
        if (lock.isPadLock()) {
            applyChangesView.primaryCodeSummaryView.fillCode(lock.generateLockPrimaryCodeList());
        } else {
            applyChangesView.primaryCodeSummaryView.fillCode(lock.getPrimaryCode());
        }
        applyChangesView.updateButtons(applyChangesView.primaryCodeSummaryView);
    }

    public static /* synthetic */ void lambda$displayActionSummary$2(ApplyChangesView applyChangesView) {
        applyChangesView.mContentFlipper.setDisplayedChild(7);
        applyChangesView.updateButtons(applyChangesView.mSecondaryCodeSummaryView);
        applyChangesView.applyChangesPresenter.updateStatus(LockStatus.UNKNOWN);
    }

    public static /* synthetic */ void lambda$displayActionSummary$3(ApplyChangesView applyChangesView, Lock lock) {
        applyChangesView.mContentFlipper.setDisplayedChild(2);
        applyChangesView.mRelockTimeSummaryView.updateRelockTime(lock.getRelockTimeInSeconds());
        applyChangesView.updateButtons(applyChangesView.mRelockTimeSummaryView);
    }

    public static /* synthetic */ void lambda$displayActionSummary$4(ApplyChangesView applyChangesView) {
        applyChangesView.mContentFlipper.setDisplayedChild(4);
        applyChangesView.updateButtons(applyChangesView.mResetKeysSummaryView);
    }

    public static /* synthetic */ void lambda$displayActionSummary$5(ApplyChangesView applyChangesView) {
        applyChangesView.mContentFlipper.setDisplayedChild(6);
        applyChangesView.updateButtons(applyChangesView.mUpdateLockView);
        applyChangesView.applyChangesPresenter.updateStatus(LockStatus.UNKNOWN);
    }

    public void updateButtons(ConfigView configView) {
        boolean isEmpty = TextUtils.isEmpty(configView.getPrimaryButtonLabel());
        boolean isEmpty2 = TextUtils.isEmpty(configView.getSecondaryButtonLabel());
        int i = 8;
        if (!isEmpty || !isEmpty2) {
            this.mButtonBar.setVisibility(0);
            this.mPrimaryActionButton.setVisibility(isEmpty ? 8 : 0);
            this.mPrimaryActionButton.setText(configView.getPrimaryButtonLabel());
            Button button = this.mSecondaryActionButton;
            if (!isEmpty2) {
                i = 0;
            }
            button.setVisibility(i);
            this.mSecondaryActionButton.setText(configView.getSecondaryButtonLabel());
            return;
        }
        this.mButtonBar.setVisibility(8);
    }

    @OnClick({2131296656})
    @Optional
    public void onPrimaryClick() {
        switch (this.mViewState) {
            case SEARCHING:
                this.applyChangesPresenter.tryLater();
                return;
            case FAILED:
                displaySearching();
                this.applyChangesPresenter.tryAgain();
                return;
            case ACTION_SUMMARY:
                this.applyChangesPresenter.summaryOk();
                return;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append(this.mViewState);
                sb.append(" not supported");
                throw new UnsupportedOperationException(sb.toString());
        }
    }

    @OnClick({2131296741})
    @Optional
    public void onSecondaryClick() {
        if (C14711.f176xd31eff1c[this.mViewState.ordinal()] == 2) {
            this.applyChangesPresenter.tryLater();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.mViewState);
        sb.append(" not supported");
        throw new UnsupportedOperationException(sb.toString());
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.applyChangesPresenter.start();
            displaySearching();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.applyChangesPresenter.finish();
        this.applyChangesPresenter = null;
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    public void displayError(String str) {
        Toast.makeText(getContext(), str, 1).show();
    }

    public void stopFirmwareUpdate() {
        this.applyChangesPresenter.stopFirmwareUpdateProcess();
    }

    public boolean onBackPressed() {
        this.applyChangesPresenter.onBackPressed();
        return false;
    }
}
