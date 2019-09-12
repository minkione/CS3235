package com.masterlock.ble.app.view.lock.keysafe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.view.lock.ConfigView;
import com.masterlock.core.LockCodeDirection;
import java.util.List;

public class SecondaryCodeSummaryView extends LinearLayout implements ConfigView {
    @InjectView(2131296745)
    LinearLayout mSecondaryCodeContainer;

    public String getSecondaryButtonLabel() {
        return "";
    }

    public SecondaryCodeSummaryView(Context context) {
        this(context, null);
    }

    public SecondaryCodeSummaryView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject((View) this);
    }

    public void fillCode(List<LockCodeDirection> list) {
        int i;
        int i2;
        this.mSecondaryCodeContainer.removeAllViews();
        for (LockCodeDirection lockCodeDirection : list) {
            switch (lockCodeDirection) {
                case LEFT:
                    i2 = C1075R.C1076drawable.ic_arrow_left;
                    i = C1075R.string.lock_code_left;
                    break;
                case UP:
                    i2 = C1075R.C1076drawable.ic_arrow_up;
                    i = C1075R.string.lock_code_up;
                    break;
                case RIGHT:
                    i2 = C1075R.C1076drawable.ic_arrow_right;
                    i = C1075R.string.lock_code_right;
                    break;
                case DOWN:
                    i2 = C1075R.C1076drawable.ic_arrow_down;
                    i = C1075R.string.lock_code_down;
                    break;
                default:
                    i2 = 0;
                    i = 0;
                    break;
            }
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
            if (i2 != 0 || i != 0) {
                View inflate = layoutInflater.inflate(C1075R.layout.lock_code_item_pad_lock, this.mSecondaryCodeContainer, false);
                ((ImageView) ButterKnife.findById(inflate, (int) C1075R.C1077id.img_code_direction)).setImageResource(i2);
                ((TextView) ButterKnife.findById(inflate, (int) C1075R.C1077id.txt_code_direction)).setText(i);
                this.mSecondaryCodeContainer.addView(inflate);
            }
        }
    }

    public String getPrimaryButtonLabel() {
        return getContext().getString(C1075R.string.f165ok);
    }
}
