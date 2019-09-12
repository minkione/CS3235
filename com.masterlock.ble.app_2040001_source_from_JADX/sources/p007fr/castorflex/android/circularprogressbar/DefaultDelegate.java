package p007fr.castorflex.android.circularprogressbar;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import p007fr.castorflex.android.circularprogressbar.CircularProgressDrawable.OnEndListener;

/* renamed from: fr.castorflex.android.circularprogressbar.DefaultDelegate */
class DefaultDelegate implements PBDelegate {
    /* access modifiers changed from: private */
    public static final ArgbEvaluator COLOR_EVALUATOR = new ArgbEvaluator();
    private static final long END_ANIMATOR_DURATION = 200;
    private static final Interpolator END_INTERPOLATOR = new LinearInterpolator();
    private static final long ROTATION_ANIMATOR_DURATION = 2000;
    private static final long SWEEP_ANIMATOR_DURATION = 600;
    private final Interpolator mAngleInterpolator;
    /* access modifiers changed from: private */
    public final int[] mColors;
    /* access modifiers changed from: private */
    public int mCurrentColor;
    private float mCurrentEndRatio = 1.0f;
    /* access modifiers changed from: private */
    public int mCurrentIndexColor;
    private float mCurrentRotationAngle = 0.0f;
    private float mCurrentRotationAngleOffset = 0.0f;
    private float mCurrentSweepAngle;
    /* access modifiers changed from: private */
    public ValueAnimator mEndAnimator;
    /* access modifiers changed from: private */
    public boolean mFirstSweepAnimation;
    /* access modifiers changed from: private */
    public final int mMaxSweepAngle;
    /* access modifiers changed from: private */
    public final int mMinSweepAngle;
    /* access modifiers changed from: private */
    public boolean mModeAppearing;
    /* access modifiers changed from: private */
    public OnEndListener mOnEndListener;
    /* access modifiers changed from: private */
    public final CircularProgressDrawable mParent;
    private ValueAnimator mRotationAnimator;
    private final float mRotationSpeed;
    /* access modifiers changed from: private */
    public ValueAnimator mSweepAppearingAnimator;
    /* access modifiers changed from: private */
    public ValueAnimator mSweepDisappearingAnimator;
    private final Interpolator mSweepInterpolator;
    private final float mSweepSpeed;

    DefaultDelegate(@NonNull CircularProgressDrawable circularProgressDrawable, @NonNull Options options) {
        this.mParent = circularProgressDrawable;
        this.mSweepInterpolator = options.sweepInterpolator;
        this.mAngleInterpolator = options.angleInterpolator;
        this.mCurrentIndexColor = 0;
        this.mColors = options.colors;
        this.mCurrentColor = this.mColors[0];
        this.mSweepSpeed = options.sweepSpeed;
        this.mRotationSpeed = options.rotationSpeed;
        this.mMinSweepAngle = options.minSweepAngle;
        this.mMaxSweepAngle = options.maxSweepAngle;
        setupAnimations();
    }

    private void reinitValues() {
        this.mFirstSweepAnimation = true;
        this.mCurrentEndRatio = 1.0f;
        this.mParent.getCurrentPaint().setColor(this.mCurrentColor);
    }

    public void draw(Canvas canvas, Paint paint) {
        float f;
        float f2;
        float f3 = this.mCurrentRotationAngle - this.mCurrentRotationAngleOffset;
        float f4 = this.mCurrentSweepAngle;
        if (!this.mModeAppearing) {
            f3 += 360.0f - f4;
        }
        float f5 = f3 % 360.0f;
        float f6 = this.mCurrentEndRatio;
        if (f6 < 1.0f) {
            float f7 = f6 * f4;
            f2 = (f5 + (f4 - f7)) % 360.0f;
            f = f7;
        } else {
            f2 = f5;
            f = f4;
        }
        canvas.drawArc(this.mParent.getDrawableBounds(), f2, f, false, paint);
    }

    public void start() {
        this.mEndAnimator.cancel();
        reinitValues();
        this.mRotationAnimator.start();
        this.mSweepAppearingAnimator.start();
    }

    public void stop() {
        stopAnimators();
    }

    private void stopAnimators() {
        this.mRotationAnimator.cancel();
        this.mSweepAppearingAnimator.cancel();
        this.mSweepDisappearingAnimator.cancel();
        this.mEndAnimator.cancel();
    }

    /* access modifiers changed from: private */
    public void setAppearing() {
        this.mModeAppearing = true;
        this.mCurrentRotationAngleOffset += (float) this.mMinSweepAngle;
    }

    /* access modifiers changed from: private */
    public void setDisappearing() {
        this.mModeAppearing = false;
        this.mCurrentRotationAngleOffset += (float) (360 - this.mMaxSweepAngle);
    }

    /* access modifiers changed from: private */
    public void setCurrentRotationAngle(float f) {
        this.mCurrentRotationAngle = f;
        this.mParent.invalidate();
    }

    /* access modifiers changed from: private */
    public void setCurrentSweepAngle(float f) {
        this.mCurrentSweepAngle = f;
        this.mParent.invalidate();
    }

    /* access modifiers changed from: private */
    public void setEndRatio(float f) {
        this.mCurrentEndRatio = f;
        this.mParent.invalidate();
    }

    private void setupAnimations() {
        this.mRotationAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 360.0f});
        this.mRotationAnimator.setInterpolator(this.mAngleInterpolator);
        this.mRotationAnimator.setDuration((long) (2000.0f / this.mRotationSpeed));
        this.mRotationAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                DefaultDelegate.this.setCurrentRotationAngle(Utils.getAnimatedFraction(valueAnimator) * 360.0f);
            }
        });
        this.mRotationAnimator.setRepeatCount(-1);
        this.mRotationAnimator.setRepeatMode(1);
        this.mSweepAppearingAnimator = ValueAnimator.ofFloat(new float[]{(float) this.mMinSweepAngle, (float) this.mMaxSweepAngle});
        this.mSweepAppearingAnimator.setInterpolator(this.mSweepInterpolator);
        this.mSweepAppearingAnimator.setDuration((long) (600.0f / this.mSweepSpeed));
        this.mSweepAppearingAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float f;
                float animatedFraction = Utils.getAnimatedFraction(valueAnimator);
                if (DefaultDelegate.this.mFirstSweepAnimation) {
                    f = animatedFraction * ((float) DefaultDelegate.this.mMaxSweepAngle);
                } else {
                    f = (animatedFraction * ((float) (DefaultDelegate.this.mMaxSweepAngle - DefaultDelegate.this.mMinSweepAngle))) + ((float) DefaultDelegate.this.mMinSweepAngle);
                }
                DefaultDelegate.this.setCurrentSweepAngle(f);
            }
        });
        this.mSweepAppearingAnimator.addListener(new SimpleAnimatorListener() {
            public void onAnimationStart(Animator animator) {
                super.onAnimationStart(animator);
                DefaultDelegate.this.mModeAppearing = true;
            }

            /* access modifiers changed from: protected */
            public void onPreAnimationEnd(Animator animator) {
                if (isStartedAndNotCancelled()) {
                    DefaultDelegate.this.mFirstSweepAnimation = false;
                    DefaultDelegate.this.setDisappearing();
                    DefaultDelegate.this.mSweepDisappearingAnimator.start();
                }
            }
        });
        this.mSweepDisappearingAnimator = ValueAnimator.ofFloat(new float[]{(float) this.mMaxSweepAngle, (float) this.mMinSweepAngle});
        this.mSweepDisappearingAnimator.setInterpolator(this.mSweepInterpolator);
        this.mSweepDisappearingAnimator.setDuration((long) (600.0f / this.mSweepSpeed));
        this.mSweepDisappearingAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = Utils.getAnimatedFraction(valueAnimator);
                DefaultDelegate defaultDelegate = DefaultDelegate.this;
                defaultDelegate.setCurrentSweepAngle(((float) defaultDelegate.mMaxSweepAngle) - (animatedFraction * ((float) (DefaultDelegate.this.mMaxSweepAngle - DefaultDelegate.this.mMinSweepAngle))));
                float currentPlayTime = ((float) valueAnimator.getCurrentPlayTime()) / ((float) valueAnimator.getDuration());
                if (DefaultDelegate.this.mColors.length > 1 && currentPlayTime > 0.7f) {
                    DefaultDelegate.this.mParent.getCurrentPaint().setColor(((Integer) DefaultDelegate.COLOR_EVALUATOR.evaluate((currentPlayTime - 0.7f) / 0.3f, Integer.valueOf(DefaultDelegate.this.mCurrentColor), Integer.valueOf(DefaultDelegate.this.mColors[(DefaultDelegate.this.mCurrentIndexColor + 1) % DefaultDelegate.this.mColors.length]))).intValue());
                }
            }
        });
        this.mSweepDisappearingAnimator.addListener(new SimpleAnimatorListener() {
            /* access modifiers changed from: protected */
            public void onPreAnimationEnd(Animator animator) {
                if (isStartedAndNotCancelled()) {
                    DefaultDelegate.this.setAppearing();
                    DefaultDelegate defaultDelegate = DefaultDelegate.this;
                    defaultDelegate.mCurrentIndexColor = (defaultDelegate.mCurrentIndexColor + 1) % DefaultDelegate.this.mColors.length;
                    DefaultDelegate defaultDelegate2 = DefaultDelegate.this;
                    defaultDelegate2.mCurrentColor = defaultDelegate2.mColors[DefaultDelegate.this.mCurrentIndexColor];
                    DefaultDelegate.this.mParent.getCurrentPaint().setColor(DefaultDelegate.this.mCurrentColor);
                    DefaultDelegate.this.mSweepAppearingAnimator.start();
                }
            }
        });
        this.mEndAnimator = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
        this.mEndAnimator.setInterpolator(END_INTERPOLATOR);
        this.mEndAnimator.setDuration(END_ANIMATOR_DURATION);
        this.mEndAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                DefaultDelegate.this.setEndRatio(1.0f - Utils.getAnimatedFraction(valueAnimator));
            }
        });
    }

    public void progressiveStop(OnEndListener onEndListener) {
        if (this.mParent.isRunning() && !this.mEndAnimator.isRunning()) {
            this.mOnEndListener = onEndListener;
            this.mEndAnimator.addListener(new SimpleAnimatorListener() {
                public void onPreAnimationEnd(Animator animator) {
                    DefaultDelegate.this.mEndAnimator.removeListener(this);
                    OnEndListener access$1700 = DefaultDelegate.this.mOnEndListener;
                    DefaultDelegate.this.mOnEndListener = null;
                    if (isStartedAndNotCancelled()) {
                        DefaultDelegate.this.setEndRatio(0.0f);
                        DefaultDelegate.this.mParent.stop();
                        if (access$1700 != null) {
                            access$1700.onEnd(DefaultDelegate.this.mParent);
                        }
                    }
                }
            });
            this.mEndAnimator.start();
        }
    }
}
