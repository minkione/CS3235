package com.masterlock.ble.app.view.lock;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import com.masterlock.ble.app.C1075R;

public class CalibrateSummaryView extends LinearLayout implements ConfigView {
    public String getSecondaryButtonLabel() {
        return "";
    }

    public CalibrateSummaryView(Context context) {
        this(context, null);
    }

    public CalibrateSummaryView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
        }
    }

    public String getPrimaryButtonLabel() {
        return getContext().getString(C1075R.string.f165ok);
    }
}
