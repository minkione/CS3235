package com.akta.android.p004ui.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/* renamed from: com.akta.android.ui.animation.WidthAnimation */
public class WidthAnimation extends Animation {
    final int startWidth;
    final int targetWidth;
    View view;

    public boolean willChangeBounds() {
        return true;
    }

    public WidthAnimation(View view2, int i) {
        this(view2, i, null);
    }

    public WidthAnimation(View view2, int i, Interpolator interpolator) {
        this.view = view2;
        this.targetWidth = i;
        this.startWidth = view2.getWidth();
        if (interpolator == null) {
            setInterpolator(new LinearInterpolator());
        } else {
            setInterpolator(interpolator);
        }
    }

    /* access modifiers changed from: protected */
    public void applyTransformation(float f, Transformation transformation) {
        int i = this.startWidth;
        this.view.getLayoutParams().width = (int) (((float) i) + (((float) (this.targetWidth - i)) * f));
        this.view.requestLayout();
    }

    public void initialize(int i, int i2, int i3, int i4) {
        super.initialize(i, i2, i3, i4);
    }

    public int getTargetWidth() {
        return this.targetWidth;
    }
}
