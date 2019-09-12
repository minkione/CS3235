package p007fr.castorflex.android.smoothprogressbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

/* renamed from: fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable */
public class SmoothProgressDrawable extends Drawable implements Animatable {
    private static final long FRAME_DURATION = 16;
    private static final float OFFSET_PER_FRAME = 0.01f;
    private final Rect fBackgroundRect;
    private Drawable mBackgroundDrawable;
    private Rect mBounds;
    private Callbacks mCallbacks;
    private int[] mColors;
    private int mColorsIndex;
    /* access modifiers changed from: private */
    public float mCurrentOffset;
    private int mCurrentSections;
    private boolean mFinishing;
    /* access modifiers changed from: private */
    public float mFinishingOffset;
    private Interpolator mInterpolator;
    private int[] mLinearGradientColors;
    private float[] mLinearGradientPositions;
    /* access modifiers changed from: private */
    public float mMaxOffset;
    private boolean mMirrorMode;
    /* access modifiers changed from: private */
    public boolean mNewTurn;
    private Paint mPaint;
    private boolean mProgressiveStartActivated;
    /* access modifiers changed from: private */
    public float mProgressiveStartSpeed;
    /* access modifiers changed from: private */
    public float mProgressiveStopSpeed;
    private boolean mReversed;
    private boolean mRunning;
    private int mSectionsCount;
    private int mSeparatorLength;
    /* access modifiers changed from: private */
    public float mSpeed;
    private int mStartSection;
    private float mStrokeWidth;
    /* access modifiers changed from: private */
    public final Runnable mUpdater;
    private boolean mUseGradients;

    /* renamed from: fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable$Builder */
    public static class Builder {
        private Drawable mBackgroundDrawableWhenHidden;
        private int[] mColors;
        private boolean mGenerateBackgroundUsingColors;
        private boolean mGradients;
        private Interpolator mInterpolator;
        private boolean mMirrorMode;
        private Callbacks mOnProgressiveStopEndedListener;
        private boolean mProgressiveStartActivated;
        private float mProgressiveStartSpeed;
        private float mProgressiveStopSpeed;
        private boolean mReversed;
        private int mSectionsCount;
        private float mSpeed;
        private int mStrokeSeparatorLength;
        private float mStrokeWidth;

        public Builder(Context context) {
            this(context, false);
        }

        public Builder(Context context, boolean z) {
            initValues(context, z);
        }

        public SmoothProgressDrawable build() {
            if (this.mGenerateBackgroundUsingColors) {
                this.mBackgroundDrawableWhenHidden = SmoothProgressBarUtils.generateDrawableWithColors(this.mColors, this.mStrokeWidth);
            }
            SmoothProgressDrawable smoothProgressDrawable = new SmoothProgressDrawable(this.mInterpolator, this.mSectionsCount, this.mStrokeSeparatorLength, this.mColors, this.mStrokeWidth, this.mSpeed, this.mProgressiveStartSpeed, this.mProgressiveStopSpeed, this.mReversed, this.mMirrorMode, this.mOnProgressiveStopEndedListener, this.mProgressiveStartActivated, this.mBackgroundDrawableWhenHidden, this.mGradients);
            return smoothProgressDrawable;
        }

        private void initValues(Context context, boolean z) {
            Resources resources = context.getResources();
            this.mInterpolator = new AccelerateInterpolator();
            if (!z) {
                this.mSectionsCount = resources.getInteger(C1807R.integer.spb_default_sections_count);
                this.mSpeed = Float.parseFloat(resources.getString(C1807R.string.spb_default_speed));
                this.mReversed = resources.getBoolean(C1807R.bool.spb_default_reversed);
                this.mProgressiveStartActivated = resources.getBoolean(C1807R.bool.spb_default_progressiveStart_activated);
                this.mColors = new int[]{resources.getColor(C1807R.color.spb_default_color)};
                this.mStrokeSeparatorLength = resources.getDimensionPixelSize(C1807R.dimen.spb_default_stroke_separator_length);
                this.mStrokeWidth = (float) resources.getDimensionPixelOffset(C1807R.dimen.spb_default_stroke_width);
            } else {
                this.mSectionsCount = 4;
                this.mSpeed = 1.0f;
                this.mReversed = false;
                this.mProgressiveStartActivated = false;
                this.mColors = new int[]{-13388315};
                this.mStrokeSeparatorLength = 4;
                this.mStrokeWidth = 4.0f;
            }
            float f = this.mSpeed;
            this.mProgressiveStartSpeed = f;
            this.mProgressiveStopSpeed = f;
            this.mGradients = false;
        }

        public Builder interpolator(Interpolator interpolator) {
            SmoothProgressBarUtils.checkNotNull(interpolator, "Interpolator");
            this.mInterpolator = interpolator;
            return this;
        }

        public Builder sectionsCount(int i) {
            SmoothProgressBarUtils.checkPositive(i, "Sections count");
            this.mSectionsCount = i;
            return this;
        }

        public Builder separatorLength(int i) {
            SmoothProgressBarUtils.checkPositiveOrZero((float) i, "Separator length");
            this.mStrokeSeparatorLength = i;
            return this;
        }

        public Builder color(int i) {
            this.mColors = new int[]{i};
            return this;
        }

        public Builder colors(int[] iArr) {
            SmoothProgressBarUtils.checkColors(iArr);
            this.mColors = iArr;
            return this;
        }

        public Builder strokeWidth(float f) {
            SmoothProgressBarUtils.checkPositiveOrZero(f, "Width");
            this.mStrokeWidth = f;
            return this;
        }

        public Builder speed(float f) {
            SmoothProgressBarUtils.checkSpeed(f);
            this.mSpeed = f;
            return this;
        }

        public Builder progressiveStartSpeed(float f) {
            SmoothProgressBarUtils.checkSpeed(f);
            this.mProgressiveStartSpeed = f;
            return this;
        }

        public Builder progressiveStopSpeed(float f) {
            SmoothProgressBarUtils.checkSpeed(f);
            this.mProgressiveStopSpeed = f;
            return this;
        }

        public Builder reversed(boolean z) {
            this.mReversed = z;
            return this;
        }

        public Builder mirrorMode(boolean z) {
            this.mMirrorMode = z;
            return this;
        }

        public Builder progressiveStart(boolean z) {
            this.mProgressiveStartActivated = z;
            return this;
        }

        public Builder callbacks(Callbacks callbacks) {
            this.mOnProgressiveStopEndedListener = callbacks;
            return this;
        }

        public Builder backgroundDrawable(Drawable drawable) {
            this.mBackgroundDrawableWhenHidden = drawable;
            return this;
        }

        public Builder generateBackgroundUsingColors() {
            this.mGenerateBackgroundUsingColors = true;
            return this;
        }

        public Builder gradients() {
            return gradients(true);
        }

        public Builder gradients(boolean z) {
            this.mGradients = z;
            return this;
        }
    }

    /* renamed from: fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable$Callbacks */
    public interface Callbacks {
        void onStart();

        void onStop();
    }

    public int getOpacity() {
        return -2;
    }

    private SmoothProgressDrawable(Interpolator interpolator, int i, int i2, int[] iArr, float f, float f2, float f3, float f4, boolean z, boolean z2, Callbacks callbacks, boolean z3, Drawable drawable, boolean z4) {
        this.fBackgroundRect = new Rect();
        this.mUpdater = new Runnable() {
            public void run() {
                if (SmoothProgressDrawable.this.isFinishing()) {
                    SmoothProgressDrawable smoothProgressDrawable = SmoothProgressDrawable.this;
                    smoothProgressDrawable.mFinishingOffset = smoothProgressDrawable.mFinishingOffset + (SmoothProgressDrawable.this.mProgressiveStopSpeed * SmoothProgressDrawable.OFFSET_PER_FRAME);
                    SmoothProgressDrawable smoothProgressDrawable2 = SmoothProgressDrawable.this;
                    smoothProgressDrawable2.mCurrentOffset = smoothProgressDrawable2.mCurrentOffset + (SmoothProgressDrawable.this.mProgressiveStopSpeed * SmoothProgressDrawable.OFFSET_PER_FRAME);
                    if (SmoothProgressDrawable.this.mFinishingOffset >= 1.0f) {
                        SmoothProgressDrawable.this.stop();
                    }
                } else if (SmoothProgressDrawable.this.isStarting()) {
                    SmoothProgressDrawable smoothProgressDrawable3 = SmoothProgressDrawable.this;
                    smoothProgressDrawable3.mCurrentOffset = smoothProgressDrawable3.mCurrentOffset + (SmoothProgressDrawable.this.mProgressiveStartSpeed * SmoothProgressDrawable.OFFSET_PER_FRAME);
                } else {
                    SmoothProgressDrawable smoothProgressDrawable4 = SmoothProgressDrawable.this;
                    smoothProgressDrawable4.mCurrentOffset = smoothProgressDrawable4.mCurrentOffset + (SmoothProgressDrawable.this.mSpeed * SmoothProgressDrawable.OFFSET_PER_FRAME);
                }
                if (SmoothProgressDrawable.this.mCurrentOffset >= SmoothProgressDrawable.this.mMaxOffset) {
                    SmoothProgressDrawable.this.mNewTurn = true;
                    SmoothProgressDrawable smoothProgressDrawable5 = SmoothProgressDrawable.this;
                    smoothProgressDrawable5.mCurrentOffset = smoothProgressDrawable5.mCurrentOffset - SmoothProgressDrawable.this.mMaxOffset;
                }
                if (SmoothProgressDrawable.this.isRunning()) {
                    SmoothProgressDrawable smoothProgressDrawable6 = SmoothProgressDrawable.this;
                    smoothProgressDrawable6.scheduleSelf(smoothProgressDrawable6.mUpdater, SystemClock.uptimeMillis() + 16);
                }
                SmoothProgressDrawable.this.invalidateSelf();
            }
        };
        this.mRunning = false;
        this.mInterpolator = interpolator;
        this.mSectionsCount = i;
        this.mStartSection = 0;
        int i3 = this.mSectionsCount;
        this.mCurrentSections = i3;
        this.mSeparatorLength = i2;
        this.mSpeed = f2;
        this.mProgressiveStartSpeed = f3;
        this.mProgressiveStopSpeed = f4;
        this.mReversed = z;
        this.mColors = iArr;
        this.mColorsIndex = 0;
        this.mMirrorMode = z2;
        this.mFinishing = false;
        this.mBackgroundDrawable = drawable;
        this.mStrokeWidth = f;
        this.mMaxOffset = 1.0f / ((float) i3);
        this.mPaint = new Paint();
        this.mPaint.setStrokeWidth(f);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setDither(false);
        this.mPaint.setAntiAlias(false);
        this.mProgressiveStartActivated = z3;
        this.mCallbacks = callbacks;
        this.mUseGradients = z4;
        refreshLinearGradientOptions();
    }

    public void setInterpolator(Interpolator interpolator) {
        if (interpolator != null) {
            this.mInterpolator = interpolator;
            invalidateSelf();
            return;
        }
        throw new IllegalArgumentException("Interpolator cannot be null");
    }

    public void setColors(int[] iArr) {
        if (iArr == null || iArr.length == 0) {
            throw new IllegalArgumentException("Colors cannot be null or empty");
        }
        this.mColorsIndex = 0;
        this.mColors = iArr;
        refreshLinearGradientOptions();
        invalidateSelf();
    }

    public void setColor(int i) {
        setColors(new int[]{i});
    }

    public void setSpeed(float f) {
        if (f >= 0.0f) {
            this.mSpeed = f;
            invalidateSelf();
            return;
        }
        throw new IllegalArgumentException("Speed must be >= 0");
    }

    public void setProgressiveStartSpeed(float f) {
        if (f >= 0.0f) {
            this.mProgressiveStartSpeed = f;
            invalidateSelf();
            return;
        }
        throw new IllegalArgumentException("SpeedProgressiveStart must be >= 0");
    }

    public void setProgressiveStopSpeed(float f) {
        if (f >= 0.0f) {
            this.mProgressiveStopSpeed = f;
            invalidateSelf();
            return;
        }
        throw new IllegalArgumentException("SpeedProgressiveStop must be >= 0");
    }

    public void setSectionsCount(int i) {
        if (i > 0) {
            this.mSectionsCount = i;
            this.mMaxOffset = 1.0f / ((float) this.mSectionsCount);
            this.mCurrentOffset %= this.mMaxOffset;
            refreshLinearGradientOptions();
            invalidateSelf();
            return;
        }
        throw new IllegalArgumentException("SectionsCount must be > 0");
    }

    public void setSeparatorLength(int i) {
        if (i >= 0) {
            this.mSeparatorLength = i;
            invalidateSelf();
            return;
        }
        throw new IllegalArgumentException("SeparatorLength must be >= 0");
    }

    public void setStrokeWidth(float f) {
        if (f >= 0.0f) {
            this.mPaint.setStrokeWidth(f);
            invalidateSelf();
            return;
        }
        throw new IllegalArgumentException("The strokeWidth must be >= 0");
    }

    public void setReversed(boolean z) {
        if (this.mReversed != z) {
            this.mReversed = z;
            invalidateSelf();
        }
    }

    public void setMirrorMode(boolean z) {
        if (this.mMirrorMode != z) {
            this.mMirrorMode = z;
            invalidateSelf();
        }
    }

    public void setBackgroundDrawable(Drawable drawable) {
        if (this.mBackgroundDrawable != drawable) {
            this.mBackgroundDrawable = drawable;
            invalidateSelf();
        }
    }

    public Drawable getBackgroundDrawable() {
        return this.mBackgroundDrawable;
    }

    public int[] getColors() {
        return this.mColors;
    }

    public float getStrokeWidth() {
        return this.mStrokeWidth;
    }

    public void setProgressiveStartActivated(boolean z) {
        this.mProgressiveStartActivated = z;
    }

    public void setUseGradients(boolean z) {
        if (this.mUseGradients != z) {
            this.mUseGradients = z;
            refreshLinearGradientOptions();
            invalidateSelf();
        }
    }

    /* access modifiers changed from: protected */
    public void refreshLinearGradientOptions() {
        if (this.mUseGradients) {
            int i = this.mSectionsCount;
            this.mLinearGradientColors = new int[(i + 2)];
            this.mLinearGradientPositions = new float[(i + 2)];
            return;
        }
        this.mPaint.setShader(null);
        this.mLinearGradientColors = null;
        this.mLinearGradientPositions = null;
    }

    public void draw(Canvas canvas) {
        this.mBounds = getBounds();
        canvas.clipRect(this.mBounds);
        if (this.mNewTurn) {
            this.mColorsIndex = decrementColor(this.mColorsIndex);
            this.mNewTurn = false;
            if (isFinishing()) {
                this.mStartSection++;
                if (this.mStartSection > this.mSectionsCount) {
                    stop();
                    return;
                }
            }
            int i = this.mCurrentSections;
            if (i < this.mSectionsCount) {
                this.mCurrentSections = i + 1;
            }
        }
        if (this.mUseGradients) {
            drawGradient(canvas);
        }
        drawStrokes(canvas);
    }

    private void drawGradient(Canvas canvas) {
        float f = 1.0f / ((float) this.mSectionsCount);
        int i = this.mColorsIndex;
        float[] fArr = this.mLinearGradientPositions;
        int i2 = 0;
        fArr[0] = 0.0f;
        fArr[fArr.length - 1] = 1.0f;
        int i3 = i - 1;
        if (i3 < 0) {
            i3 += this.mColors.length;
        }
        this.mLinearGradientColors[0] = this.mColors[i3];
        while (i2 < this.mSectionsCount) {
            float interpolation = this.mInterpolator.getInterpolation((((float) i2) * f) + this.mCurrentOffset);
            i2++;
            this.mLinearGradientPositions[i2] = interpolation;
            int[] iArr = this.mLinearGradientColors;
            int[] iArr2 = this.mColors;
            iArr[i2] = iArr2[i];
            i = (i + 1) % iArr2.length;
        }
        int[] iArr3 = this.mLinearGradientColors;
        iArr3[iArr3.length - 1] = this.mColors[i];
        float abs = (float) ((!this.mReversed || !this.mMirrorMode) ? this.mBounds.left : Math.abs(this.mBounds.left - this.mBounds.right) / 2);
        int i4 = this.mMirrorMode ? this.mReversed ? this.mBounds.left : Math.abs(this.mBounds.left - this.mBounds.right) / 2 : this.mBounds.right;
        LinearGradient linearGradient = new LinearGradient(abs, ((float) this.mBounds.centerY()) - (this.mStrokeWidth / 2.0f), (float) i4, (this.mStrokeWidth / 2.0f) + ((float) this.mBounds.centerY()), this.mLinearGradientColors, this.mLinearGradientPositions, this.mMirrorMode ? TileMode.MIRROR : TileMode.CLAMP);
        this.mPaint.setShader(linearGradient);
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x00fa  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00fe  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void drawStrokes(android.graphics.Canvas r24) {
        /*
            r23 = this;
            r8 = r23
            r9 = r24
            boolean r0 = r8.mReversed
            r10 = 1065353216(0x3f800000, float:1.0)
            r11 = 0
            if (r0 == 0) goto L_0x001a
            android.graphics.Rect r0 = r8.mBounds
            int r0 = r0.width()
            float r0 = (float) r0
            r9.translate(r0, r11)
            r0 = -1082130432(0xffffffffbf800000, float:-1.0)
            r9.scale(r0, r10)
        L_0x001a:
            android.graphics.Rect r0 = r8.mBounds
            int r0 = r0.width()
            boolean r1 = r8.mMirrorMode
            if (r1 == 0) goto L_0x0026
            int r0 = r0 / 2
        L_0x0026:
            r12 = r0
            int r0 = r8.mSeparatorLength
            int r0 = r0 + r12
            int r1 = r8.mSectionsCount
            int r13 = r0 + r1
            android.graphics.Rect r0 = r8.mBounds
            int r14 = r0.centerY()
            int r0 = r8.mSectionsCount
            float r1 = (float) r0
            float r15 = r10 / r1
            int r1 = r8.mColorsIndex
            int r2 = r8.mStartSection
            int r3 = r8.mCurrentSections
            if (r2 != r3) goto L_0x0049
            if (r3 != r0) goto L_0x0049
            int r0 = r24.getWidth()
            float r0 = (float) r0
            goto L_0x004a
        L_0x0049:
            r0 = 0
        L_0x004a:
            r2 = 0
            r6 = r0
            r3 = r1
            r4 = 0
            r5 = 0
            r7 = 0
        L_0x0050:
            int r0 = r8.mCurrentSections
            if (r7 > r0) goto L_0x0112
            float r0 = (float) r7
            float r0 = r0 * r15
            float r1 = r8.mCurrentOffset
            float r0 = r0 + r1
            float r1 = r0 - r15
            float r1 = java.lang.Math.max(r11, r1)
            android.view.animation.Interpolator r2 = r8.mInterpolator
            float r2 = r2.getInterpolation(r1)
            android.view.animation.Interpolator r11 = r8.mInterpolator
            float r0 = java.lang.Math.min(r0, r10)
            float r0 = r11.getInterpolation(r0)
            float r2 = r2 - r0
            float r0 = java.lang.Math.abs(r2)
            float r2 = (float) r13
            float r0 = r0 * r2
            int r0 = (int) r0
            float r11 = (float) r0
            float r1 = r1 + r11
            int r0 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
            if (r0 >= 0) goto L_0x0089
            int r0 = r8.mSeparatorLength
            float r0 = (float) r0
            float r0 = java.lang.Math.min(r11, r0)
            r16 = r0
            goto L_0x008b
        L_0x0089:
            r16 = 0
        L_0x008b:
            int r0 = (r11 > r16 ? 1 : (r11 == r16 ? 0 : -1))
            if (r0 <= 0) goto L_0x0092
            float r0 = r11 - r16
            goto L_0x0093
        L_0x0092:
            r0 = 0
        L_0x0093:
            float r1 = r4 + r0
            int r0 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1))
            if (r0 <= 0) goto L_0x00e8
            int r0 = r8.mStartSection
            if (r7 < r0) goto L_0x00e8
            android.view.animation.Interpolator r0 = r8.mInterpolator
            r17 = r3
            float r3 = r8.mFinishingOffset
            float r3 = java.lang.Math.min(r3, r10)
            float r0 = r0.getInterpolation(r3)
            float r0 = r0 * r2
            float r2 = (float) r12
            float r3 = java.lang.Math.min(r2, r4)
            float r18 = java.lang.Math.max(r0, r3)
            float r19 = java.lang.Math.min(r2, r1)
            float r3 = (float) r14
            r0 = r23
            r20 = r1
            r1 = r24
            r2 = r12
            r21 = r17
            r17 = r3
            r3 = r18
            r22 = r4
            r4 = r17
            r10 = r5
            r5 = r19
            r19 = r12
            r12 = r6
            r6 = r17
            r17 = r13
            r13 = r7
            r7 = r21
            r0.drawLine(r1, r2, r3, r4, r5, r6, r7)
            int r0 = r8.mStartSection
            if (r13 != r0) goto L_0x00f5
            int r0 = r8.mSeparatorLength
            float r0 = (float) r0
            float r18 = r18 - r0
            r6 = r18
            goto L_0x00f6
        L_0x00e8:
            r20 = r1
            r21 = r3
            r22 = r4
            r10 = r5
            r19 = r12
            r17 = r13
            r12 = r6
            r13 = r7
        L_0x00f5:
            r6 = r12
        L_0x00f6:
            int r0 = r8.mCurrentSections
            if (r13 != r0) goto L_0x00fe
            float r4 = r22 + r11
            r5 = r4
            goto L_0x00ff
        L_0x00fe:
            r5 = r10
        L_0x00ff:
            float r4 = r20 + r16
            r1 = r21
            int r3 = r8.incrementColor(r1)
            int r7 = r13 + 1
            r13 = r17
            r12 = r19
            r10 = 1065353216(0x3f800000, float:1.0)
            r11 = 0
            goto L_0x0050
        L_0x0112:
            r10 = r5
            r12 = r6
            r8.drawBackgroundIfNeeded(r9, r12, r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p007fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable.drawStrokes(android.graphics.Canvas):void");
    }

    private void drawLine(Canvas canvas, int i, float f, float f2, float f3, float f4, int i2) {
        int i3 = i;
        this.mPaint.setColor(this.mColors[i2]);
        if (!this.mMirrorMode) {
            canvas.drawLine(f, f2, f3, f4, this.mPaint);
        } else if (this.mReversed) {
            float f5 = (float) i3;
            canvas.drawLine(f5 + f, f2, f5 + f3, f4, this.mPaint);
            canvas.drawLine(f5 - f, f2, f5 - f3, f4, this.mPaint);
        } else {
            Canvas canvas2 = canvas;
            float f6 = f2;
            float f7 = f4;
            canvas2.drawLine(f, f6, f3, f7, this.mPaint);
            float f8 = (float) (i3 * 2);
            canvas2.drawLine(f8 - f, f6, f8 - f3, f7, this.mPaint);
        }
    }

    private void drawBackgroundIfNeeded(Canvas canvas, float f, float f2) {
        if (this.mBackgroundDrawable != null) {
            this.fBackgroundRect.top = (int) ((((float) canvas.getHeight()) - this.mStrokeWidth) / 2.0f);
            this.fBackgroundRect.bottom = (int) ((((float) canvas.getHeight()) + this.mStrokeWidth) / 2.0f);
            Rect rect = this.fBackgroundRect;
            rect.left = 0;
            rect.right = this.mMirrorMode ? canvas.getWidth() / 2 : canvas.getWidth();
            this.mBackgroundDrawable.setBounds(this.fBackgroundRect);
            if (!isRunning()) {
                if (this.mMirrorMode) {
                    canvas.save();
                    canvas.translate((float) (canvas.getWidth() / 2), 0.0f);
                    drawBackground(canvas, 0.0f, (float) this.fBackgroundRect.width());
                    canvas.scale(-1.0f, 1.0f);
                    drawBackground(canvas, 0.0f, (float) this.fBackgroundRect.width());
                    canvas.restore();
                } else {
                    drawBackground(canvas, 0.0f, (float) this.fBackgroundRect.width());
                }
            } else if (isFinishing() || isStarting()) {
                if (f > f2) {
                    float f3 = f2;
                    f2 = f;
                    f = f3;
                }
                if (f > 0.0f) {
                    if (this.mMirrorMode) {
                        canvas.save();
                        canvas.translate((float) (canvas.getWidth() / 2), 0.0f);
                        if (this.mReversed) {
                            drawBackground(canvas, 0.0f, f);
                            canvas.scale(-1.0f, 1.0f);
                            drawBackground(canvas, 0.0f, f);
                        } else {
                            drawBackground(canvas, ((float) (canvas.getWidth() / 2)) - f, (float) (canvas.getWidth() / 2));
                            canvas.scale(-1.0f, 1.0f);
                            drawBackground(canvas, ((float) (canvas.getWidth() / 2)) - f, (float) (canvas.getWidth() / 2));
                        }
                        canvas.restore();
                    } else {
                        drawBackground(canvas, 0.0f, f);
                    }
                }
                if (f2 <= ((float) canvas.getWidth())) {
                    if (this.mMirrorMode) {
                        canvas.save();
                        canvas.translate((float) (canvas.getWidth() / 2), 0.0f);
                        if (this.mReversed) {
                            drawBackground(canvas, f2, (float) (canvas.getWidth() / 2));
                            canvas.scale(-1.0f, 1.0f);
                            drawBackground(canvas, f2, (float) (canvas.getWidth() / 2));
                        } else {
                            drawBackground(canvas, 0.0f, ((float) (canvas.getWidth() / 2)) - f2);
                            canvas.scale(-1.0f, 1.0f);
                            drawBackground(canvas, 0.0f, ((float) (canvas.getWidth() / 2)) - f2);
                        }
                        canvas.restore();
                    } else {
                        drawBackground(canvas, f2, (float) canvas.getWidth());
                    }
                }
            }
        }
    }

    private void drawBackground(Canvas canvas, float f, float f2) {
        int save = canvas.save();
        canvas.clipRect(f, (float) ((int) ((((float) canvas.getHeight()) - this.mStrokeWidth) / 2.0f)), f2, (float) ((int) ((((float) canvas.getHeight()) + this.mStrokeWidth) / 2.0f)));
        this.mBackgroundDrawable.draw(canvas);
        canvas.restoreToCount(save);
    }

    private int incrementColor(int i) {
        int i2 = i + 1;
        if (i2 >= this.mColors.length) {
            return 0;
        }
        return i2;
    }

    private int decrementColor(int i) {
        int i2 = i - 1;
        return i2 < 0 ? this.mColors.length - 1 : i2;
    }

    public void progressiveStart() {
        progressiveStart(0);
    }

    public void progressiveStart(int i) {
        resetProgressiveStart(i);
        start();
    }

    private void resetProgressiveStart(int i) {
        checkColorIndex(i);
        this.mCurrentOffset = 0.0f;
        this.mFinishing = false;
        this.mFinishingOffset = 0.0f;
        this.mStartSection = 0;
        this.mCurrentSections = 0;
        this.mColorsIndex = i;
    }

    public void progressiveStop() {
        this.mFinishing = true;
        this.mStartSection = 0;
    }

    public void setAlpha(int i) {
        this.mPaint.setAlpha(i);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    public void start() {
        if (this.mProgressiveStartActivated) {
            resetProgressiveStart(0);
        }
        if (!isRunning()) {
            Callbacks callbacks = this.mCallbacks;
            if (callbacks != null) {
                callbacks.onStart();
            }
            scheduleSelf(this.mUpdater, SystemClock.uptimeMillis() + 16);
            invalidateSelf();
        }
    }

    public void stop() {
        if (isRunning()) {
            Callbacks callbacks = this.mCallbacks;
            if (callbacks != null) {
                callbacks.onStop();
            }
            this.mRunning = false;
            unscheduleSelf(this.mUpdater);
        }
    }

    public void scheduleSelf(Runnable runnable, long j) {
        this.mRunning = true;
        super.scheduleSelf(runnable, j);
    }

    public boolean isRunning() {
        return this.mRunning;
    }

    public boolean isStarting() {
        return this.mCurrentSections < this.mSectionsCount;
    }

    public boolean isFinishing() {
        return this.mFinishing;
    }

    public void setCallbacks(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    private void checkColorIndex(int i) {
        if (i < 0 || i >= this.mColors.length) {
            throw new IllegalArgumentException(String.format("Index %d not valid", new Object[]{Integer.valueOf(i)}));
        }
    }
}
