package com.masterlock.ble.app.view.lock;

import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class UnlockModeSummaryView$$ViewInjector {
    public static void inject(Finder finder, UnlockModeSummaryView unlockModeSummaryView, Object obj) {
        unlockModeSummaryView.mUnlockModeText = (TextView) finder.findRequiredView(obj, C1075R.C1077id.unlock_mode, "field 'mUnlockModeText'");
        unlockModeSummaryView.mUnlockModeImage = (ImageView) finder.findRequiredView(obj, C1075R.C1077id.unlock_mode_image, "field 'mUnlockModeImage'");
        unlockModeSummaryView.mUnlockModeBeta = (ImageView) finder.findRequiredView(obj, C1075R.C1077id.unlock_mode_beta, "field 'mUnlockModeBeta'");
    }

    public static void reset(UnlockModeSummaryView unlockModeSummaryView) {
        unlockModeSummaryView.mUnlockModeText = null;
        unlockModeSummaryView.mUnlockModeImage = null;
        unlockModeSummaryView.mUnlockModeBeta = null;
    }
}
