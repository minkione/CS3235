package com.masterlock.ble.app.view.lock.keysafe.shackle;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.ButterKnife;

public class ShackleWakeLockView extends LinearLayout {
    public ShackleWakeLockView(Context context) {
        this(context, null);
    }

    public ShackleWakeLockView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject((View) this);
    }
}
