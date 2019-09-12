package com.masterlock.ble.app.view.lock;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;
import com.masterlock.core.LockMode;

public class UnlockModeSummaryView extends RelativeLayout implements ConfigView {
    @InjectView(2131296898)
    ImageView mUnlockModeBeta;
    @InjectView(2131296900)
    ImageView mUnlockModeImage;
    @InjectView(2131296897)
    TextView mUnlockModeText;

    public String getSecondaryButtonLabel() {
        return "";
    }

    public UnlockModeSummaryView(Context context) {
        this(context, null);
    }

    public UnlockModeSummaryView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
        }
    }

    public void updateLockMode(LockMode lockMode) {
        Drawable drawable;
        String str = "";
        Drawable drawable2 = null;
        switch (lockMode) {
            case TOUCH:
                str = getResources().getString(C1075R.string.touch_unlock_card_title);
                drawable2 = getResources().getDrawable(C1075R.C1076drawable.graphic_touch_unlock);
                drawable = null;
                break;
            case PROXIMITYSWIPE:
                str = getResources().getString(C1075R.string.proximity_swipe_card_title);
                drawable2 = getResources().getDrawable(C1075R.C1076drawable.graphic_proximity_swipe);
                drawable = getResources().getDrawable(C1075R.C1076drawable.ic_beta_swipe_box);
                break;
            default:
                drawable = null;
                break;
        }
        this.mUnlockModeText.setText(str);
        this.mUnlockModeImage.setImageDrawable(drawable2);
        this.mUnlockModeBeta.setImageDrawable(drawable);
    }

    public String getPrimaryButtonLabel() {
        return getContext().getString(C1075R.string.f165ok);
    }
}
