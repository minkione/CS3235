package com.masterlock.ble.app.view.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.view.lock.ConfigView;

public class UpdateFirmwareSummaryView extends LinearLayout implements ConfigView {
    public String getSecondaryButtonLabel() {
        return "";
    }

    public UpdateFirmwareSummaryView(Context context) {
        this(context, null);
    }

    public UpdateFirmwareSummaryView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject((View) this);
    }

    public String getPrimaryButtonLabel() {
        return getContext().getString(C1075R.string.f165ok);
    }
}
