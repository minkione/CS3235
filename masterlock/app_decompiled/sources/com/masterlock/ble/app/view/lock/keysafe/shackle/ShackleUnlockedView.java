package com.masterlock.ble.app.view.lock.keysafe.shackle;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;

public class ShackleUnlockedView extends LinearLayout {
    @InjectView(2131296685)
    TextView mRelockTime;

    public ShackleUnlockedView(Context context) {
        this(context, null);
    }

    public ShackleUnlockedView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject((View) this);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void setRemainingRelockTime(Long l) {
        this.mRelockTime.setText(getResources().getString(C1075R.string.relock_time, new Object[]{l}));
    }
}
