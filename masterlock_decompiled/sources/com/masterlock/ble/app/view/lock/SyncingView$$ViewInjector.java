package com.masterlock.ble.app.view.lock;

import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.gamma.FillImageProgressBar;

public class SyncingView$$ViewInjector {
    public static void inject(Finder finder, SyncingView syncingView, Object obj) {
        syncingView.mTitle = (TextView) finder.findRequiredView(obj, C1075R.C1077id.apply_changes_title, "field 'mTitle'");
        syncingView.mProgressStatus = (TextView) finder.findRequiredView(obj, C1075R.C1077id.wake_lock_progress_status, "field 'mProgressStatus'");
        syncingView.mStatus = (TextView) finder.findRequiredView(obj, C1075R.C1077id.wake_lock_status, "field 'mStatus'");
        syncingView.mInstructions = (TextView) finder.findRequiredView(obj, C1075R.C1077id.wake_lock_instructions, "field 'mInstructions'");
        syncingView.mFillImageProgressBar = (FillImageProgressBar) finder.findRequiredView(obj, C1075R.C1077id.apply_changes_progress_bar, "field 'mFillImageProgressBar'");
    }

    public static void reset(SyncingView syncingView) {
        syncingView.mTitle = null;
        syncingView.mProgressStatus = null;
        syncingView.mStatus = null;
        syncingView.mInstructions = null;
        syncingView.mFillImageProgressBar = null;
    }
}
