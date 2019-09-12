package com.masterlock.ble.app.view.settings.calibration;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.ButterKnife;

public class CalibrationCalibratingView extends LinearLayout {
    public CalibrationCalibratingView(Context context) {
        this(context, null);
    }

    public CalibrationCalibratingView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject((View) this);
    }
}
