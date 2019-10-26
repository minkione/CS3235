package com.masterlock.ble.app.view.lock.keysafe;

import android.widget.LinearLayout;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class SecondaryCodeSummaryView$$ViewInjector {
    public static void inject(Finder finder, SecondaryCodeSummaryView secondaryCodeSummaryView, Object obj) {
        secondaryCodeSummaryView.mSecondaryCodeContainer = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.secondary_codes_container, "field 'mSecondaryCodeContainer'");
    }

    public static void reset(SecondaryCodeSummaryView secondaryCodeSummaryView) {
        secondaryCodeSummaryView.mSecondaryCodeContainer = null;
    }
}
