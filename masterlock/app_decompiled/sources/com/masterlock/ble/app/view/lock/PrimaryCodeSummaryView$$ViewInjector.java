package com.masterlock.ble.app.view.lock;

import android.widget.LinearLayout;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class PrimaryCodeSummaryView$$ViewInjector {
    public static void inject(Finder finder, PrimaryCodeSummaryView primaryCodeSummaryView, Object obj) {
        primaryCodeSummaryView.mPrimaryCodeContainer = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.primary_code_container, "field 'mPrimaryCodeContainer'");
    }

    public static void reset(PrimaryCodeSummaryView primaryCodeSummaryView) {
        primaryCodeSummaryView.mPrimaryCodeContainer = null;
    }
}
