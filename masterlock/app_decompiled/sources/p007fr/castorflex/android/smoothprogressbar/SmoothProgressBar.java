package p007fr.castorflex.android.smoothprogressbar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import p007fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable.Builder;
import p007fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable.Callbacks;

/* renamed from: fr.castorflex.android.smoothprogressbar.SmoothProgressBar */
public class SmoothProgressBar extends ProgressBar {
    private static final int INTERPOLATOR_ACCELERATE = 0;
    private static final int INTERPOLATOR_ACCELERATEDECELERATE = 2;
    private static final int INTERPOLATOR_DECELERATE = 3;
    private static final int INTERPOLATOR_LINEAR = 1;

    public SmoothProgressBar(Context context) {
        this(context, null);
    }

    public SmoothProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C1807R.attr.spbStyle);
    }

    public SmoothProgressBar(Context context, AttributeSet attributeSet, int i) {
        Interpolator interpolator;
        Context context2 = context;
        super(context, attributeSet, i);
        if (isInEditMode()) {
            setIndeterminateDrawable(new Builder(context2, true).build());
            return;
        }
        Resources resources = context.getResources();
        TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet, C1807R.styleable.SmoothProgressBar, i, 0);
        int color = obtainStyledAttributes.getColor(C1807R.styleable.SmoothProgressBar_spb_color, resources.getColor(C1807R.color.spb_default_color));
        int integer = obtainStyledAttributes.getInteger(C1807R.styleable.SmoothProgressBar_spb_sections_count, resources.getInteger(C1807R.integer.spb_default_sections_count));
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(C1807R.styleable.SmoothProgressBar_spb_stroke_separator_length, resources.getDimensionPixelSize(C1807R.dimen.spb_default_stroke_separator_length));
        float dimension = obtainStyledAttributes.getDimension(C1807R.styleable.SmoothProgressBar_spb_stroke_width, resources.getDimension(C1807R.dimen.spb_default_stroke_width));
        float f = obtainStyledAttributes.getFloat(C1807R.styleable.SmoothProgressBar_spb_speed, Float.parseFloat(resources.getString(C1807R.string.spb_default_speed)));
        float f2 = obtainStyledAttributes.getFloat(C1807R.styleable.SmoothProgressBar_spb_progressiveStart_speed, f);
        float f3 = obtainStyledAttributes.getFloat(C1807R.styleable.SmoothProgressBar_spb_progressiveStop_speed, f);
        int integer2 = obtainStyledAttributes.getInteger(C1807R.styleable.SmoothProgressBar_spb_interpolator, -1);
        boolean z = obtainStyledAttributes.getBoolean(C1807R.styleable.SmoothProgressBar_spb_reversed, resources.getBoolean(C1807R.bool.spb_default_reversed));
        boolean z2 = obtainStyledAttributes.getBoolean(C1807R.styleable.SmoothProgressBar_spb_mirror_mode, resources.getBoolean(C1807R.bool.spb_default_mirror_mode));
        int resourceId = obtainStyledAttributes.getResourceId(C1807R.styleable.SmoothProgressBar_spb_colors, 0);
        boolean z3 = obtainStyledAttributes.getBoolean(C1807R.styleable.SmoothProgressBar_spb_progressiveStart_activated, resources.getBoolean(C1807R.bool.spb_default_progressiveStart_activated));
        int i2 = color;
        Drawable drawable = obtainStyledAttributes.getDrawable(C1807R.styleable.SmoothProgressBar_spb_background);
        boolean z4 = obtainStyledAttributes.getBoolean(C1807R.styleable.SmoothProgressBar_spb_generate_background_with_colors, false);
        boolean z5 = obtainStyledAttributes.getBoolean(C1807R.styleable.SmoothProgressBar_spb_gradients, false);
        obtainStyledAttributes.recycle();
        int[] iArr = null;
        if (integer2 == -1) {
            interpolator = getInterpolator();
        } else {
            interpolator = null;
        }
        if (interpolator == null) {
            switch (integer2) {
                case 1:
                    interpolator = new LinearInterpolator();
                    break;
                case 2:
                    interpolator = new AccelerateDecelerateInterpolator();
                    break;
                case 3:
                    interpolator = new DecelerateInterpolator();
                    break;
                default:
                    interpolator = new AccelerateInterpolator();
                    break;
            }
        }
        if (resourceId != 0) {
            iArr = resources.getIntArray(resourceId);
        }
        Builder gradients = new Builder(context2).speed(f).progressiveStartSpeed(f2).progressiveStopSpeed(f3).interpolator(interpolator).sectionsCount(integer).separatorLength(dimensionPixelSize).strokeWidth(dimension).reversed(z).mirrorMode(z2).progressiveStart(z3).gradients(z5);
        if (drawable != null) {
            gradients.backgroundDrawable(drawable);
        }
        if (z4) {
            gradients.generateBackgroundUsingColors();
        }
        if (iArr == null || iArr.length <= 0) {
            gradients.color(i2);
        } else {
            gradients.colors(iArr);
        }
        setIndeterminateDrawable(gradients.build());
    }

    public void applyStyle(int i) {
        Interpolator interpolator = null;
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(null, C1807R.styleable.SmoothProgressBar, 0, i);
        if (obtainStyledAttributes.hasValue(C1807R.styleable.SmoothProgressBar_spb_color)) {
            setSmoothProgressDrawableColor(obtainStyledAttributes.getColor(C1807R.styleable.SmoothProgressBar_spb_color, 0));
        }
        if (obtainStyledAttributes.hasValue(C1807R.styleable.SmoothProgressBar_spb_colors)) {
            int resourceId = obtainStyledAttributes.getResourceId(C1807R.styleable.SmoothProgressBar_spb_colors, 0);
            if (resourceId != 0) {
                int[] intArray = getResources().getIntArray(resourceId);
                if (intArray != null && intArray.length > 0) {
                    setSmoothProgressDrawableColors(intArray);
                }
            }
        }
        if (obtainStyledAttributes.hasValue(C1807R.styleable.SmoothProgressBar_spb_sections_count)) {
            setSmoothProgressDrawableSectionsCount(obtainStyledAttributes.getInteger(C1807R.styleable.SmoothProgressBar_spb_sections_count, 0));
        }
        if (obtainStyledAttributes.hasValue(C1807R.styleable.SmoothProgressBar_spb_stroke_separator_length)) {
            setSmoothProgressDrawableSeparatorLength(obtainStyledAttributes.getDimensionPixelSize(C1807R.styleable.SmoothProgressBar_spb_stroke_separator_length, 0));
        }
        if (obtainStyledAttributes.hasValue(C1807R.styleable.SmoothProgressBar_spb_stroke_width)) {
            setSmoothProgressDrawableStrokeWidth(obtainStyledAttributes.getDimension(C1807R.styleable.SmoothProgressBar_spb_stroke_width, 0.0f));
        }
        if (obtainStyledAttributes.hasValue(C1807R.styleable.SmoothProgressBar_spb_speed)) {
            setSmoothProgressDrawableSpeed(obtainStyledAttributes.getFloat(C1807R.styleable.SmoothProgressBar_spb_speed, 0.0f));
        }
        if (obtainStyledAttributes.hasValue(C1807R.styleable.SmoothProgressBar_spb_progressiveStart_speed)) {
            setSmoothProgressDrawableProgressiveStartSpeed(obtainStyledAttributes.getFloat(C1807R.styleable.SmoothProgressBar_spb_progressiveStart_speed, 0.0f));
        }
        if (obtainStyledAttributes.hasValue(C1807R.styleable.SmoothProgressBar_spb_progressiveStop_speed)) {
            setSmoothProgressDrawableProgressiveStopSpeed(obtainStyledAttributes.getFloat(C1807R.styleable.SmoothProgressBar_spb_progressiveStop_speed, 0.0f));
        }
        if (obtainStyledAttributes.hasValue(C1807R.styleable.SmoothProgressBar_spb_reversed)) {
            setSmoothProgressDrawableReversed(obtainStyledAttributes.getBoolean(C1807R.styleable.SmoothProgressBar_spb_reversed, false));
        }
        if (obtainStyledAttributes.hasValue(C1807R.styleable.SmoothProgressBar_spb_mirror_mode)) {
            setSmoothProgressDrawableMirrorMode(obtainStyledAttributes.getBoolean(C1807R.styleable.SmoothProgressBar_spb_mirror_mode, false));
        }
        if (obtainStyledAttributes.hasValue(C1807R.styleable.SmoothProgressBar_spb_progressiveStart_activated)) {
            setProgressiveStartActivated(obtainStyledAttributes.getBoolean(C1807R.styleable.SmoothProgressBar_spb_progressiveStart_activated, false));
        }
        if (obtainStyledAttributes.hasValue(C1807R.styleable.SmoothProgressBar_spb_progressiveStart_activated)) {
            setProgressiveStartActivated(obtainStyledAttributes.getBoolean(C1807R.styleable.SmoothProgressBar_spb_progressiveStart_activated, false));
        }
        if (obtainStyledAttributes.hasValue(C1807R.styleable.SmoothProgressBar_spb_gradients)) {
            setSmoothProgressDrawableUseGradients(obtainStyledAttributes.getBoolean(C1807R.styleable.SmoothProgressBar_spb_gradients, false));
        }
        if (obtainStyledAttributes.hasValue(C1807R.styleable.SmoothProgressBar_spb_generate_background_with_colors) && obtainStyledAttributes.getBoolean(C1807R.styleable.SmoothProgressBar_spb_generate_background_with_colors, false)) {
            setSmoothProgressDrawableBackgroundDrawable(SmoothProgressBarUtils.generateDrawableWithColors(checkIndeterminateDrawable().getColors(), checkIndeterminateDrawable().getStrokeWidth()));
        }
        if (obtainStyledAttributes.hasValue(C1807R.styleable.SmoothProgressBar_spb_interpolator)) {
            switch (obtainStyledAttributes.getInteger(C1807R.styleable.SmoothProgressBar_spb_interpolator, -1)) {
                case 0:
                    interpolator = new AccelerateInterpolator();
                    break;
                case 1:
                    interpolator = new LinearInterpolator();
                    break;
                case 2:
                    interpolator = new AccelerateDecelerateInterpolator();
                    break;
                case 3:
                    interpolator = new DecelerateInterpolator();
                    break;
            }
            if (interpolator != null) {
                setInterpolator(interpolator);
            }
        }
        obtainStyledAttributes.recycle();
    }

    /* access modifiers changed from: protected */
    public synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isIndeterminate() && (getIndeterminateDrawable() instanceof SmoothProgressDrawable) && !((SmoothProgressDrawable) getIndeterminateDrawable()).isRunning()) {
            getIndeterminateDrawable().draw(canvas);
        }
    }

    private SmoothProgressDrawable checkIndeterminateDrawable() {
        Drawable indeterminateDrawable = getIndeterminateDrawable();
        if (indeterminateDrawable != null && (indeterminateDrawable instanceof SmoothProgressDrawable)) {
            return (SmoothProgressDrawable) indeterminateDrawable;
        }
        throw new RuntimeException("The drawable is not a SmoothProgressDrawable");
    }

    public void setInterpolator(Interpolator interpolator) {
        super.setInterpolator(interpolator);
        Drawable indeterminateDrawable = getIndeterminateDrawable();
        if (indeterminateDrawable != null && (indeterminateDrawable instanceof SmoothProgressDrawable)) {
            ((SmoothProgressDrawable) indeterminateDrawable).setInterpolator(interpolator);
        }
    }

    public void setSmoothProgressDrawableInterpolator(Interpolator interpolator) {
        checkIndeterminateDrawable().setInterpolator(interpolator);
    }

    public void setSmoothProgressDrawableColors(int[] iArr) {
        checkIndeterminateDrawable().setColors(iArr);
    }

    public void setSmoothProgressDrawableColor(int i) {
        checkIndeterminateDrawable().setColor(i);
    }

    public void setSmoothProgressDrawableSpeed(float f) {
        checkIndeterminateDrawable().setSpeed(f);
    }

    public void setSmoothProgressDrawableProgressiveStartSpeed(float f) {
        checkIndeterminateDrawable().setProgressiveStartSpeed(f);
    }

    public void setSmoothProgressDrawableProgressiveStopSpeed(float f) {
        checkIndeterminateDrawable().setProgressiveStopSpeed(f);
    }

    public void setSmoothProgressDrawableSectionsCount(int i) {
        checkIndeterminateDrawable().setSectionsCount(i);
    }

    public void setSmoothProgressDrawableSeparatorLength(int i) {
        checkIndeterminateDrawable().setSeparatorLength(i);
    }

    public void setSmoothProgressDrawableStrokeWidth(float f) {
        checkIndeterminateDrawable().setStrokeWidth(f);
    }

    public void setSmoothProgressDrawableReversed(boolean z) {
        checkIndeterminateDrawable().setReversed(z);
    }

    public void setSmoothProgressDrawableMirrorMode(boolean z) {
        checkIndeterminateDrawable().setMirrorMode(z);
    }

    public void setProgressiveStartActivated(boolean z) {
        checkIndeterminateDrawable().setProgressiveStartActivated(z);
    }

    public void setSmoothProgressDrawableCallbacks(Callbacks callbacks) {
        checkIndeterminateDrawable().setCallbacks(callbacks);
    }

    public void setSmoothProgressDrawableBackgroundDrawable(Drawable drawable) {
        checkIndeterminateDrawable().setBackgroundDrawable(drawable);
    }

    public void setSmoothProgressDrawableUseGradients(boolean z) {
        checkIndeterminateDrawable().setUseGradients(z);
    }

    public void progressiveStart() {
        checkIndeterminateDrawable().progressiveStart();
    }

    public void progressiveStop() {
        checkIndeterminateDrawable().progressiveStop();
    }
}
