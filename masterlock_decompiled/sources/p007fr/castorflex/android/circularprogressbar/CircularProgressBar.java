package p007fr.castorflex.android.circularprogressbar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import p007fr.castorflex.android.circularprogressbar.CircularProgressDrawable.Builder;
import p007fr.castorflex.android.circularprogressbar.CircularProgressDrawable.OnEndListener;

/* renamed from: fr.castorflex.android.circularprogressbar.CircularProgressBar */
public class CircularProgressBar extends ProgressBar {
    public CircularProgressBar(Context context) {
        this(context, null);
    }

    public CircularProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C1804R.attr.cpbStyle);
    }

    public CircularProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        if (isInEditMode()) {
            setIndeterminateDrawable(new Builder(context, true).build());
            return;
        }
        Resources resources = context.getResources();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C1804R.styleable.CircularProgressBar, i, 0);
        int color = obtainStyledAttributes.getColor(C1804R.styleable.CircularProgressBar_cpb_color, resources.getColor(C1804R.color.cpb_default_color));
        float dimension = obtainStyledAttributes.getDimension(C1804R.styleable.CircularProgressBar_cpb_stroke_width, resources.getDimension(C1804R.dimen.cpb_default_stroke_width));
        float f = obtainStyledAttributes.getFloat(C1804R.styleable.CircularProgressBar_cpb_sweep_speed, Float.parseFloat(resources.getString(C1804R.string.cpb_default_sweep_speed)));
        float f2 = obtainStyledAttributes.getFloat(C1804R.styleable.CircularProgressBar_cpb_rotation_speed, Float.parseFloat(resources.getString(C1804R.string.cpb_default_rotation_speed)));
        int resourceId = obtainStyledAttributes.getResourceId(C1804R.styleable.CircularProgressBar_cpb_colors, 0);
        int integer = obtainStyledAttributes.getInteger(C1804R.styleable.CircularProgressBar_cpb_min_sweep_angle, resources.getInteger(C1804R.integer.cpb_default_min_sweep_angle));
        int integer2 = obtainStyledAttributes.getInteger(C1804R.styleable.CircularProgressBar_cpb_max_sweep_angle, resources.getInteger(C1804R.integer.cpb_default_max_sweep_angle));
        obtainStyledAttributes.recycle();
        int[] iArr = null;
        if (resourceId != 0) {
            iArr = resources.getIntArray(resourceId);
        }
        Builder maxSweepAngle = new Builder(context).sweepSpeed(f).rotationSpeed(f2).strokeWidth(dimension).minSweepAngle(integer).maxSweepAngle(integer2);
        if (iArr == null || iArr.length <= 0) {
            maxSweepAngle.color(color);
        } else {
            maxSweepAngle.colors(iArr);
        }
        setIndeterminateDrawable(maxSweepAngle.build());
    }

    private CircularProgressDrawable checkIndeterminateDrawable() {
        Drawable indeterminateDrawable = getIndeterminateDrawable();
        if (indeterminateDrawable != null && (indeterminateDrawable instanceof CircularProgressDrawable)) {
            return (CircularProgressDrawable) indeterminateDrawable;
        }
        throw new RuntimeException("The drawable is not a CircularProgressDrawable");
    }

    public void progressiveStop() {
        checkIndeterminateDrawable().progressiveStop();
    }

    public void progressiveStop(OnEndListener onEndListener) {
        checkIndeterminateDrawable().progressiveStop(onEndListener);
    }
}
