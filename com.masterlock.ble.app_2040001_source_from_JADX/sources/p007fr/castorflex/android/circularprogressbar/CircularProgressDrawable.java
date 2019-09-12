package p007fr.castorflex.android.circularprogressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.masterlock.ble.app.provider.MasterlockProvider;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* renamed from: fr.castorflex.android.circularprogressbar.CircularProgressDrawable */
public class CircularProgressDrawable extends Drawable implements Animatable {
    public static final int STYLE_NORMAL = 0;
    public static final int STYLE_ROUNDED = 1;
    private final RectF mBounds;
    private final Options mOptions;
    private PBDelegate mPBDelegate;
    private final Paint mPaint;
    private final PowerManager mPowerManager;
    private boolean mRunning;

    /* renamed from: fr.castorflex.android.circularprogressbar.CircularProgressDrawable$Builder */
    public static class Builder {
        private static final Interpolator DEFAULT_ROTATION_INTERPOLATOR = new LinearInterpolator();
        private static final Interpolator DEFAULT_SWEEP_INTERPOLATOR = new FastOutSlowInInterpolator();
        private Interpolator mAngleInterpolator;
        private float mBorderWidth;
        private int[] mColors;
        private int mMaxSweepAngle;
        private int mMinSweepAngle;
        private PowerManager mPowerManager;
        private float mRotationSpeed;
        int mStyle;
        private Interpolator mSweepInterpolator;
        private float mSweepSpeed;

        public Builder(@NonNull Context context) {
            this(context, false);
        }

        public Builder(@NonNull Context context, boolean z) {
            this.mSweepInterpolator = DEFAULT_SWEEP_INTERPOLATOR;
            this.mAngleInterpolator = DEFAULT_ROTATION_INTERPOLATOR;
            initValues(context, z);
        }

        private void initValues(@NonNull Context context, boolean z) {
            this.mBorderWidth = context.getResources().getDimension(C1804R.dimen.cpb_default_stroke_width);
            this.mSweepSpeed = 1.0f;
            this.mRotationSpeed = 1.0f;
            if (z) {
                this.mColors = new int[]{-16776961};
                this.mMinSweepAngle = 20;
                this.mMaxSweepAngle = MasterlockProvider.INVITATIONS;
            } else {
                this.mColors = new int[]{context.getResources().getColor(C1804R.color.cpb_default_color)};
                this.mMinSweepAngle = context.getResources().getInteger(C1804R.integer.cpb_default_min_sweep_angle);
                this.mMaxSweepAngle = context.getResources().getInteger(C1804R.integer.cpb_default_max_sweep_angle);
            }
            this.mStyle = 1;
            this.mPowerManager = Utils.powerManager(context);
        }

        public Builder color(int i) {
            this.mColors = new int[]{i};
            return this;
        }

        public Builder colors(int[] iArr) {
            Utils.checkColors(iArr);
            this.mColors = iArr;
            return this;
        }

        public Builder sweepSpeed(float f) {
            Utils.checkSpeed(f);
            this.mSweepSpeed = f;
            return this;
        }

        public Builder rotationSpeed(float f) {
            Utils.checkSpeed(f);
            this.mRotationSpeed = f;
            return this;
        }

        public Builder minSweepAngle(int i) {
            Utils.checkAngle(i);
            this.mMinSweepAngle = i;
            return this;
        }

        public Builder maxSweepAngle(int i) {
            Utils.checkAngle(i);
            this.mMaxSweepAngle = i;
            return this;
        }

        public Builder strokeWidth(float f) {
            Utils.checkPositiveOrZero(f, "StrokeWidth");
            this.mBorderWidth = f;
            return this;
        }

        public Builder style(int i) {
            this.mStyle = i;
            return this;
        }

        public Builder sweepInterpolator(Interpolator interpolator) {
            Utils.checkNotNull(interpolator, "Sweep interpolator");
            this.mSweepInterpolator = interpolator;
            return this;
        }

        public Builder angleInterpolator(Interpolator interpolator) {
            Utils.checkNotNull(interpolator, "Angle interpolator");
            this.mAngleInterpolator = interpolator;
            return this;
        }

        public CircularProgressDrawable build() {
            PowerManager powerManager = this.mPowerManager;
            Options options = new Options(this.mAngleInterpolator, this.mSweepInterpolator, this.mBorderWidth, this.mColors, this.mSweepSpeed, this.mRotationSpeed, this.mMinSweepAngle, this.mMaxSweepAngle, this.mStyle);
            return new CircularProgressDrawable(powerManager, options);
        }
    }

    /* renamed from: fr.castorflex.android.circularprogressbar.CircularProgressDrawable$OnEndListener */
    public interface OnEndListener {
        void onEnd(CircularProgressDrawable circularProgressDrawable);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* renamed from: fr.castorflex.android.circularprogressbar.CircularProgressDrawable$Style */
    public @interface Style {
    }

    public int getOpacity() {
        return -3;
    }

    private CircularProgressDrawable(PowerManager powerManager, Options options) {
        this.mBounds = new RectF();
        this.mOptions = options;
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(android.graphics.Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(options.borderWidth);
        this.mPaint.setStrokeCap(options.style == 1 ? Cap.ROUND : Cap.BUTT);
        this.mPaint.setColor(options.colors[0]);
        this.mPowerManager = powerManager;
        initDelegate();
    }

    public void draw(@NonNull Canvas canvas) {
        if (isRunning()) {
            this.mPBDelegate.draw(canvas, this.mPaint);
        }
    }

    public void setAlpha(int i) {
        this.mPaint.setAlpha(i);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        float f = this.mOptions.borderWidth;
        float f2 = f / 2.0f;
        this.mBounds.left = ((float) rect.left) + f2 + 0.5f;
        this.mBounds.right = (((float) rect.right) - f2) - 0.5f;
        this.mBounds.top = ((float) rect.top) + f2 + 0.5f;
        this.mBounds.bottom = (((float) rect.bottom) - f2) - 0.5f;
    }

    public void start() {
        initDelegate();
        this.mPBDelegate.start();
        this.mRunning = true;
        invalidateSelf();
    }

    private void initDelegate() {
        if (Utils.isPowerSaveModeEnabled(this.mPowerManager)) {
            PBDelegate pBDelegate = this.mPBDelegate;
            if (pBDelegate == null || !(pBDelegate instanceof PowerSaveModeDelegate)) {
                PBDelegate pBDelegate2 = this.mPBDelegate;
                if (pBDelegate2 != null) {
                    pBDelegate2.stop();
                }
                this.mPBDelegate = new PowerSaveModeDelegate(this);
                return;
            }
            return;
        }
        PBDelegate pBDelegate3 = this.mPBDelegate;
        if (pBDelegate3 == null || (pBDelegate3 instanceof PowerSaveModeDelegate)) {
            PBDelegate pBDelegate4 = this.mPBDelegate;
            if (pBDelegate4 != null) {
                pBDelegate4.stop();
            }
            this.mPBDelegate = new DefaultDelegate(this, this.mOptions);
        }
    }

    public void stop() {
        this.mRunning = false;
        this.mPBDelegate.stop();
        invalidateSelf();
    }

    /* access modifiers changed from: 0000 */
    @UiThread
    public void invalidate() {
        if (getCallback() == null) {
            stop();
        }
        invalidateSelf();
    }

    public boolean isRunning() {
        return this.mRunning;
    }

    /* access modifiers changed from: 0000 */
    public Paint getCurrentPaint() {
        return this.mPaint;
    }

    /* access modifiers changed from: 0000 */
    public RectF getDrawableBounds() {
        return this.mBounds;
    }

    public void progressiveStop(OnEndListener onEndListener) {
        this.mPBDelegate.progressiveStop(onEndListener);
    }

    public void progressiveStop() {
        progressiveStop(null);
    }
}
