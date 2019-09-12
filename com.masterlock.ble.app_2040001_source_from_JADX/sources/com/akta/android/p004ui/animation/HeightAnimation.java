package com.akta.android.p004ui.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/* renamed from: com.akta.android.ui.animation.HeightAnimation */
public class HeightAnimation extends Animation {
    final int startHeight;
    final int targetHeight;
    View view;

    public boolean willChangeBounds() {
        return true;
    }

    public HeightAnimation(View view2, int i) {
        this(view2, i, null);
    }

    public HeightAnimation(View view2, int i, Interpolator interpolator) {
        this.view = view2;
        this.targetHeight = i;
        this.startHeight = view2.getHeight();
        if (interpolator == null) {
            setInterpolator(new LinearInterpolator());
        } else {
            setInterpolator(interpolator);
        }
    }

    /* access modifiers changed from: protected */
    public void applyTransformation(float f, Transformation transformation) {
        int i = this.startHeight;
        this.view.getLayoutParams().height = (int) (((float) i) + (((float) (this.targetHeight - i)) * f));
        this.view.requestLayout();
    }

    public void initialize(int i, int i2, int i3, int i4) {
        super.initialize(i, i2, i3, i4);
    }

    public int getTargetHeight() {
        return this.targetHeight;
    }
}
