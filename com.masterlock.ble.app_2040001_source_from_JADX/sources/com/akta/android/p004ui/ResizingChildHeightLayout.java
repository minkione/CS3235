package com.akta.android.p004ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;

/* renamed from: com.akta.android.ui.ResizingChildHeightLayout */
public class ResizingChildHeightLayout extends LinearLayout {
    private int maxChildHeight = 0;

    public ResizingChildHeightLayout(Context context) {
        super(context);
    }

    public ResizingChildHeightLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ResizingChildHeightLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        this.maxChildHeight = 0;
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec((int) (((float) MeasureSpec.getSize(i)) / ((float) getChildCount())), Integer.MIN_VALUE);
        int makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i2), 0);
        int childCount = getChildCount();
        int i3 = 0;
        for (int i4 = 0; i4 < childCount; i4++) {
            View childAt = getChildAt(i4);
            if (childAt.getVisibility() != 8) {
                childAt.measure(makeMeasureSpec, makeMeasureSpec2);
                i3 = Math.max(i3, childAt.getMeasuredWidth());
                this.maxChildHeight = Math.max(this.maxChildHeight, childAt.getMeasuredHeight());
            }
        }
        setMeasuredDimension(resolveSize(i3, i), resolveSize(this.maxChildHeight, i2));
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5 = i3 - i;
        int childCount = getChildCount();
        int i6 = 0;
        for (int i7 = 0; i7 < childCount; i7++) {
            if (getChildAt(i7).getVisibility() != 8) {
                i6++;
            }
        }
        if (i6 != 0) {
            int i8 = (int) (((float) i5) / ((float) childCount));
            for (int i9 = 0; i9 < childCount; i9++) {
                View childAt = getChildAt(i9);
                if (childAt.getVisibility() != 8) {
                    int i10 = i8 * i9;
                    childAt.layout(i10, 0, i10 + i8, this.maxChildHeight);
                }
            }
        }
    }
}
