package p007fr.castorflex.android.circularprogressbar;

import android.view.animation.Interpolator;

/* renamed from: fr.castorflex.android.circularprogressbar.Options */
class Options {
    final Interpolator angleInterpolator;
    final float borderWidth;
    final int[] colors;
    final int maxSweepAngle;
    final int minSweepAngle;
    final float rotationSpeed;
    final int style;
    final Interpolator sweepInterpolator;
    final float sweepSpeed;

    Options(Interpolator interpolator, Interpolator interpolator2, float f, int[] iArr, float f2, float f3, int i, int i2, int i3) {
        this.angleInterpolator = interpolator;
        this.sweepInterpolator = interpolator2;
        this.borderWidth = f;
        this.colors = iArr;
        this.sweepSpeed = f2;
        this.rotationSpeed = f3;
        this.minSweepAngle = i;
        this.maxSweepAngle = i2;
        this.style = i3;
    }
}
