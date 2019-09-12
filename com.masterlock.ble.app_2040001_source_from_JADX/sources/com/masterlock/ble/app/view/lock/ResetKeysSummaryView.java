package com.masterlock.ble.app.view.lock;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.masterlock.ble.app.C1075R;

public class ResetKeysSummaryView extends LinearLayout implements ConfigView {
    public String getSecondaryButtonLabel() {
        return "";
    }

    public ResetKeysSummaryView(Context context) {
        this(context, null);
    }

    public ResetKeysSummaryView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public String getPrimaryButtonLabel() {
        return getContext().getString(C1075R.string.f165ok);
    }
}
