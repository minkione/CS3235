package com.masterlock.ble.app.view.lock;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;

public class RelockTimeSummaryView extends LinearLayout implements ConfigView {
    @InjectView(2131296880)
    TextView mRelockTime;

    public String getSecondaryButtonLabel() {
        return "";
    }

    public RelockTimeSummaryView(Context context) {
        this(context, null);
    }

    public RelockTimeSummaryView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
        }
    }

    public void updateRelockTime(int i) {
        this.mRelockTime.setText(String.valueOf(i));
    }

    public String getPrimaryButtonLabel() {
        return getContext().getString(C1075R.string.f165ok);
    }
}
