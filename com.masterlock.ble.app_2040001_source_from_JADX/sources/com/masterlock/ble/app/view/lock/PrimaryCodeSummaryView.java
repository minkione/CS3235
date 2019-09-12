package com.masterlock.ble.app.view.lock;

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
import com.masterlock.core.LockCodeDirection;
import java.util.List;

public class PrimaryCodeSummaryView extends LinearLayout implements ConfigView {
    @InjectView(2131296657)
    LinearLayout mPrimaryCodeContainer;

    public String getSecondaryButtonLabel() {
        return "";
    }

    public PrimaryCodeSummaryView(Context context) {
        this(context, null);
    }

    public PrimaryCodeSummaryView(Context context, AttributeSet attributeSet) {
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
        this.mPrimaryCodeContainer.removeAllViews();
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
                View inflate = layoutInflater.inflate(C1075R.layout.lock_code_item_pad_lock, this.mPrimaryCodeContainer, false);
                ((ImageView) ButterKnife.findById(inflate, (int) C1075R.C1077id.img_code_direction)).setImageResource(i2);
                ((TextView) ButterKnife.findById(inflate, (int) C1075R.C1077id.txt_code_direction)).setText(i);
                this.mPrimaryCodeContainer.addView(inflate);
            }
        }
    }

    public void fillCode(String str) {
        inflateDigits(str.toCharArray(), (LayoutInflater) getContext().getSystemService("layout_inflater"), this.mPrimaryCodeContainer);
    }

    private void inflateDigits(char[] cArr, LayoutInflater layoutInflater, LinearLayout linearLayout) {
        linearLayout.removeAllViews();
        int i = 0;
        while (i < 8) {
            char c = i < cArr.length ? cArr[i] : ' ';
            View inflate = layoutInflater.inflate(C1075R.layout.lock_code_item_key_safe, linearLayout, false);
            ((TextView) ButterKnife.findById(inflate, (int) C1075R.C1077id.txt_code_direction)).setText(String.format("%s", new Object[]{Character.valueOf(c)}));
            linearLayout.addView(inflate);
            i++;
        }
    }

    public String getPrimaryButtonLabel() {
        return getContext().getString(C1075R.string.f165ok);
    }
}
