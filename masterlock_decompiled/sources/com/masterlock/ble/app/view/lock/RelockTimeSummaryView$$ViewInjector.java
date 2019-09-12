package com.masterlock.ble.app.view.lock;

import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class RelockTimeSummaryView$$ViewInjector {
    public static void inject(Finder finder, RelockTimeSummaryView relockTimeSummaryView, Object obj) {
        relockTimeSummaryView.mRelockTime = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_relock_time, "field 'mRelockTime'");
    }

    public static void reset(RelockTimeSummaryView relockTimeSummaryView) {
        relockTimeSummaryView.mRelockTime = null;
    }
}
