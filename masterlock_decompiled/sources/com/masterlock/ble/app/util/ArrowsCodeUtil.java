package com.masterlock.ble.app.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.masterlock.ble.app.C1075R;
import com.masterlock.core.LockCodeDirection;
import java.util.List;

public class ArrowsCodeUtil {
    public void showMasterCode(String str, LinearLayout linearLayout, Context context) {
        if (str.length() > 25) {
            str = str.substring(0, 25);
        }
        if (isOnlyArrows(str)) {
            inflateArrows(str, linearLayout, context);
        } else {
            inflateDigits(str, context, linearLayout);
        }
    }

    private void inflateArrows(String str, LinearLayout linearLayout, Context context) {
        linearLayout.removeAllViews();
        LayoutParams layoutParams = new LayoutParams(-2, -2, 1.0f);
        layoutParams.gravity = 80;
        List generateLockDirectionListFromStringCode = LockCodeDirection.generateLockDirectionListFromStringCode(str);
        int size = generateLockDirectionListFromStringCode.size();
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(getDrawableForDirection((LockCodeDirection) generateLockDirectionListFromStringCode.get(i)));
            imageView.setLayoutParams(layoutParams);
            linearLayout.addView(imageView);
        }
    }

    private void inflateDigits(String str, Context context, LinearLayout linearLayout) {
        linearLayout.removeAllViews();
        char[] charArray = str.toCharArray();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        int i = 0;
        while (i < charArray.length) {
            char c = i < charArray.length ? charArray[i] : ' ';
            View inflate = layoutInflater.inflate(C1075R.layout.lock_code_item_key_safe, linearLayout, false);
            ((TextView) ButterKnife.findById(inflate, (int) C1075R.C1077id.txt_code_direction)).setText(String.format("%s", new Object[]{Character.valueOf(c)}));
            linearLayout.addView(inflate);
            i++;
        }
    }

    private int getDrawableForDirection(LockCodeDirection lockCodeDirection) {
        switch (lockCodeDirection) {
            case LEFT:
                return C1075R.C1076drawable.ic_arrow_left;
            case UP:
                return C1075R.C1076drawable.ic_arrow_up;
            case RIGHT:
                return C1075R.C1076drawable.ic_arrow_right;
            default:
                return C1075R.C1076drawable.ic_arrow_down;
        }
    }

    private boolean isOnlyArrows(String str) {
        char[] charArray = str.toCharArray();
        int i = 0;
        boolean z = true;
        while (i < charArray.length) {
            char c = i < charArray.length ? charArray[i] : ' ';
            if (!(c == 'D' || c == 'L' || c == 'R' || c == 'U')) {
                z = false;
            }
            i++;
        }
        return z;
    }
}
