package com.masterlock.ble.app.view.lock;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.gamma.FillImageProgressBar;

public class SyncingView extends LinearLayout implements ConfigView {
    int imageLevel;
    DisplayState mDisplayState;
    @InjectView(2131296303)
    public FillImageProgressBar mFillImageProgressBar;
    @InjectView(2131296908)
    TextView mInstructions;
    @InjectView(2131296909)
    TextView mProgressStatus;
    @InjectView(2131296910)
    TextView mStatus;
    @InjectView(2131296304)
    TextView mTitle;
    private int totalProgress;

    public SyncingView(Context context) {
        this(context, null);
    }

    public SyncingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.imageLevel = 0;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject((View) this);
    }

    public void updateView(DisplayState displayState) {
        this.mDisplayState = displayState;
        this.mTitle.setText(displayState.mTitle);
        this.mStatus.setText(displayState.mStatus);
        if (TextUtils.isEmpty(displayState.mInstructions)) {
            this.mInstructions.setVisibility(8);
            return;
        }
        this.mInstructions.setVisibility(0);
        this.mInstructions.setText(displayState.mInstructions);
    }

    public String getPrimaryButtonLabel() {
        return this.mDisplayState.mPrimaryButtonLabel;
    }

    public String getSecondaryButtonLabel() {
        return this.mDisplayState.mSecondaryButtonLabel;
    }

    public void searching() {
        updateView(new DisplayStateBuilder().setmTitle(getResources().getString(C1075R.string.primary_code_wake_your_lock_title)).setmStatus(getResources().getString(C1075R.string.primary_code_search_for_lock)).setmInstructions(getResources().getString(C1075R.string.wake_lock_instructions)).setmPrimaryButtonLabel(getResources().getString(C1075R.string.primary_try_later)).createDisplayState());
        updateImageLevel(0);
        this.mProgressStatus.setVisibility(4);
        this.mProgressStatus.setText("");
        this.mFillImageProgressBar.setProgress(0);
        this.mFillImageProgressBar.resetToFixed(C1075R.C1076drawable.graphic_search_mobile);
    }

    public void searchingCalibration() {
        updateView(new DisplayStateBuilder().setmTitle(getResources().getString(C1075R.string.calibration_wake_lock_title)).setmInstructions(getResources().getString(C1075R.string.calibration_wake_lock_body)).setmPrimaryButtonLabel(getResources().getString(C1075R.string.primary_try_later)).createDisplayState());
        updateImageLevel(0);
        this.mProgressStatus.setVisibility(4);
        this.mProgressStatus.setText("");
        this.mFillImageProgressBar.setProgress(0);
        this.mFillImageProgressBar.resetToFixed(C1075R.C1076drawable.wake_up_device);
    }

    public void syncing() {
        updateView(new DisplayStateBuilder().setmTitle(getResources().getString(C1075R.string.lock_found_state_title)).setmInstructions(getResources().getString(C1075R.string.syncing_firmware_update)).setmStatusIcon(getResources().getDrawable(C1075R.C1076drawable.graphic_synced)).createDisplayState());
        this.mProgressStatus.setText("");
        this.mProgressStatus.setVisibility(0);
        this.mFillImageProgressBar.setProgress(0);
        this.mFillImageProgressBar.resetToFixed(C1075R.C1076drawable.graphic_synced);
    }

    public void syncingCalibration() {
        updateView(new DisplayStateBuilder().setmTitle(getResources().getString(C1075R.string.lock_found_state_title)).setmInstructions(getResources().getString(C1075R.string.syncing_firmware_update)).setmStatusIcon(getResources().getDrawable(C1075R.C1076drawable.graphic_synced)).createDisplayState());
        this.mProgressStatus.setText("");
        this.mProgressStatus.setVisibility(0);
        this.mFillImageProgressBar.setProgress(0);
        this.mFillImageProgressBar.resetToFixed(C1075R.C1076drawable.graphic_synced);
    }

    public void syncingFirmwareUpdate() {
        updateView(new DisplayStateBuilder().setmTitle(getResources().getString(C1075R.string.lock_found_state_title)).setmInstructions(getResources().getString(C1075R.string.syncing_firmware_update)).createDisplayState());
        StringBuilder sb = new StringBuilder();
        sb.append("ImageLevel: ");
        sb.append(this.imageLevel);
        Log.d("SyncingView", sb.toString());
        this.mProgressStatus.setVisibility(0);
        int i = this.imageLevel;
        if (i == 0) {
            this.mFillImageProgressBar.setProgress(0);
            this.mProgressStatus.setText("0%");
        } else {
            updateImageLevel(i);
        }
        this.mFillImageProgressBar.resetToProgress(C1075R.C1076drawable.ic_lock_anim);
    }

    public void syncingRestoreConfig() {
        updateView(new DisplayStateBuilder().setmTitle(getResources().getString(C1075R.string.lock_found_state_title)).setmInstructions(getResources().getString(C1075R.string.syncing_restore_configuration)).createDisplayState());
        this.mFillImageProgressBar.resetToFixed(C1075R.C1076drawable.graphic_search_mobile);
        this.mProgressStatus.setVisibility(4);
    }

    public void updateImageLevel(int i) {
        this.imageLevel = i;
        if (this.mFillImageProgressBar.getProgressMax() > 0 || !this.mProgressStatus.getText().equals("")) {
            float progressMax = (((float) i) / ((float) this.mFillImageProgressBar.getProgressMax())) * 100.0f;
            this.mProgressStatus.setText(String.format("%d%%", new Object[]{Integer.valueOf((int) progressMax)}));
        } else {
            this.mProgressStatus.setText("0%");
        }
        this.mFillImageProgressBar.setProgress(i);
    }

    public void failure() {
        updateView(new DisplayStateBuilder().setmTitle(getResources().getString(C1075R.string.primary_code_unable_to_unlock_lock_title)).setmStatusIcon(getResources().getDrawable(C1075R.C1076drawable.graphic_sync_fail)).setmInstructions(getResources().getString(C1075R.string.primary_couldnt_change)).setmPrimaryButtonLabel(getResources().getString(C1075R.string.primary_try_again)).setmSecondaryButtonLabel(getResources().getString(C1075R.string.primary_try_later)).createDisplayState());
        this.imageLevel = 0;
        this.mProgressStatus.setText("");
        this.mFillImageProgressBar.setProgress(0);
        this.mFillImageProgressBar.resetToFixed(C1075R.C1076drawable.graphic_sync_fail);
    }

    public void setTotalProgress(int i) {
        this.totalProgress = i;
    }
}
