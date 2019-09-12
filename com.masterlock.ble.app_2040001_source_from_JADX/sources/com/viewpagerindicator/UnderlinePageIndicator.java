package com.viewpagerindicator;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.p000v4.view.MotionEventCompat;
import android.support.p000v4.view.ViewConfigurationCompat;
import android.support.p000v4.view.ViewPager;
import android.support.p000v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.ViewConfiguration;
import com.masterlock.ble.app.C1075R;

public class UnderlinePageIndicator extends View implements PageIndicator {
    private static final int FADE_FRAME_MS = 30;
    private static final int INVALID_POINTER = -1;
    private int mActivePointerId;
    private int mCurrentPage;
    /* access modifiers changed from: private */
    public int mFadeBy;
    private int mFadeDelay;
    private int mFadeLength;
    /* access modifiers changed from: private */
    public final Runnable mFadeRunnable;
    /* access modifiers changed from: private */
    public boolean mFades;
    private boolean mIsDragging;
    private float mLastMotionX;
    private OnPageChangeListener mListener;
    /* access modifiers changed from: private */
    public final Paint mPaint;
    private float mPositionOffset;
    private int mScrollState;
    private int mSelectedColor;
    private int mTouchSlop;
    private ViewPager mViewPager;

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        int currentPage;

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.currentPage = parcel.readInt();
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.currentPage);
        }
    }

    public UnderlinePageIndicator(Context context) {
        this(context, null);
    }

    public UnderlinePageIndicator(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C1075R.attr.vpiUnderlinePageIndicatorStyle);
    }

    public UnderlinePageIndicator(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mPaint = new Paint(1);
        this.mLastMotionX = -1.0f;
        this.mActivePointerId = -1;
        this.mFadeRunnable = new Runnable() {
            public void run() {
                if (UnderlinePageIndicator.this.mFades) {
                    int max = Math.max(UnderlinePageIndicator.this.mPaint.getAlpha() - UnderlinePageIndicator.this.mFadeBy, 0);
                    UnderlinePageIndicator.this.mPaint.setAlpha(max);
                    UnderlinePageIndicator.this.invalidate();
                    if (max > 0) {
                        UnderlinePageIndicator.this.postDelayed(this, 30);
                    }
                }
            }
        };
        if (!isInEditMode()) {
            Resources resources = getResources();
            boolean z = resources.getBoolean(C1075R.bool.default_underline_indicator_fades);
            int integer = resources.getInteger(C1075R.integer.default_underline_indicator_fade_delay);
            int integer2 = resources.getInteger(C1075R.integer.default_underline_indicator_fade_length);
            int color = resources.getColor(C1075R.color.default_underline_indicator_selected_color);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C1075R.styleable.UnderlinePageIndicator, i, 0);
            setFades(obtainStyledAttributes.getBoolean(3, z));
            setSelectedColor(obtainStyledAttributes.getColor(4, color));
            setFadeDelay(obtainStyledAttributes.getInteger(1, integer));
            setFadeLength(obtainStyledAttributes.getInteger(2, integer2));
            Drawable drawable = obtainStyledAttributes.getDrawable(0);
            if (drawable != null) {
                setBackgroundDrawable(drawable);
            }
            obtainStyledAttributes.recycle();
            this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(ViewConfiguration.get(context));
        }
    }

    public boolean getFades() {
        return this.mFades;
    }

    public void setFades(boolean z) {
        if (z != this.mFades) {
            this.mFades = z;
            if (z) {
                post(this.mFadeRunnable);
                return;
            }
            removeCallbacks(this.mFadeRunnable);
            this.mPaint.setAlpha(255);
            invalidate();
        }
    }

    public int getFadeDelay() {
        return this.mFadeDelay;
    }

    public void setFadeDelay(int i) {
        this.mFadeDelay = i;
    }

    public int getFadeLength() {
        return this.mFadeLength;
    }

    public void setFadeLength(int i) {
        this.mFadeLength = i;
        this.mFadeBy = 255 / (this.mFadeLength / 30);
    }

    public int getSelectedColor() {
        return this.mPaint.getColor();
    }

    public void setSelectedColor(int i) {
        this.mSelectedColor = i;
        this.mPaint.setColor(i);
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ViewPager viewPager = this.mViewPager;
        if (viewPager != null) {
            int count = viewPager.getAdapter().getCount();
            if (count != 0) {
                if (this.mCurrentPage >= count) {
                    setCurrentItem(count - 1);
                    return;
                }
                int paddingLeft = getPaddingLeft();
                float width = ((float) ((getWidth() - paddingLeft) - getPaddingRight())) / (((float) count) * 1.0f);
                float f = ((float) paddingLeft) + ((((float) this.mCurrentPage) + this.mPositionOffset) * width);
                Canvas canvas2 = canvas;
                canvas2.drawRect(f, (float) getPaddingTop(), f + width, (float) (getHeight() - getPaddingBottom()), this.mPaint);
            }
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (super.onTouchEvent(motionEvent)) {
            return true;
        }
        ViewPager viewPager = this.mViewPager;
        int i = 0;
        if (viewPager == null || viewPager.getAdapter().getCount() == 0) {
            return false;
        }
        int action = motionEvent.getAction() & 255;
        switch (action) {
            case 0:
                this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, 0);
                this.mLastMotionX = motionEvent.getX();
                break;
            case 1:
            case 3:
                if (!this.mIsDragging) {
                    int count = this.mViewPager.getAdapter().getCount();
                    float width = (float) getWidth();
                    float f = width / 2.0f;
                    float f2 = width / 6.0f;
                    if (this.mCurrentPage > 0 && motionEvent.getX() < f - f2) {
                        if (action != 3) {
                            this.mViewPager.setCurrentItem(this.mCurrentPage - 1);
                        }
                        return true;
                    } else if (this.mCurrentPage < count - 1 && motionEvent.getX() > f + f2) {
                        if (action != 3) {
                            this.mViewPager.setCurrentItem(this.mCurrentPage + 1);
                        }
                        return true;
                    }
                }
                this.mIsDragging = false;
                this.mActivePointerId = -1;
                if (this.mViewPager.isFakeDragging()) {
                    this.mViewPager.endFakeDrag();
                    break;
                }
                break;
            case 2:
                float x = MotionEventCompat.getX(motionEvent, MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId));
                float f3 = x - this.mLastMotionX;
                if (!this.mIsDragging && Math.abs(f3) > ((float) this.mTouchSlop)) {
                    this.mIsDragging = true;
                }
                if (this.mIsDragging) {
                    this.mLastMotionX = x;
                    if (this.mViewPager.isFakeDragging() || this.mViewPager.beginFakeDrag()) {
                        this.mViewPager.fakeDragBy(f3);
                        break;
                    }
                }
                break;
            case 5:
                int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
                this.mLastMotionX = MotionEventCompat.getX(motionEvent, actionIndex);
                this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, actionIndex);
                break;
            case 6:
                int actionIndex2 = MotionEventCompat.getActionIndex(motionEvent);
                if (MotionEventCompat.getPointerId(motionEvent, actionIndex2) == this.mActivePointerId) {
                    if (actionIndex2 == 0) {
                        i = 1;
                    }
                    this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, i);
                }
                this.mLastMotionX = MotionEventCompat.getX(motionEvent, MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId));
                break;
        }
        return true;
    }

    public void setViewPager(ViewPager viewPager) {
        ViewPager viewPager2 = this.mViewPager;
        if (viewPager2 != viewPager) {
            if (viewPager2 != null) {
                viewPager2.setOnPageChangeListener(null);
            }
            if (viewPager.getAdapter() != null) {
                this.mViewPager = viewPager;
                this.mViewPager.setOnPageChangeListener(this);
                invalidate();
                post(new Runnable() {
                    public void run() {
                        if (UnderlinePageIndicator.this.mFades) {
                            UnderlinePageIndicator underlinePageIndicator = UnderlinePageIndicator.this;
                            underlinePageIndicator.post(underlinePageIndicator.mFadeRunnable);
                        }
                    }
                });
                return;
            }
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
    }

    public void setViewPager(ViewPager viewPager, int i) {
        setViewPager(viewPager);
        setCurrentItem(i);
    }

    public void setCurrentItem(int i) {
        ViewPager viewPager = this.mViewPager;
        if (viewPager != null) {
            viewPager.setCurrentItem(i);
            this.mCurrentPage = i;
            invalidate();
            return;
        }
        throw new IllegalStateException("ViewPager has not been bound.");
    }

    public void notifyDataSetChanged() {
        invalidate();
    }

    public void onPageScrollStateChanged(int i) {
        this.mScrollState = i;
        OnPageChangeListener onPageChangeListener = this.mListener;
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrollStateChanged(i);
        }
    }

    public void onPageScrolled(int i, float f, int i2) {
        this.mCurrentPage = i;
        this.mPositionOffset = f;
        if (this.mFades) {
            if (i2 > 0) {
                removeCallbacks(this.mFadeRunnable);
                this.mPaint.setAlpha(255);
            } else if (this.mScrollState != 1) {
                postDelayed(this.mFadeRunnable, (long) this.mFadeDelay);
            }
        }
        invalidate();
        OnPageChangeListener onPageChangeListener = this.mListener;
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrolled(i, f, i2);
        }
    }

    public void onPageSelected(int i) {
        if (this.mScrollState == 0) {
            this.mCurrentPage = i;
            this.mPositionOffset = 0.0f;
            invalidate();
            this.mFadeRunnable.run();
        }
        OnPageChangeListener onPageChangeListener = this.mListener;
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelected(i);
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.mListener = onPageChangeListener;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mCurrentPage = savedState.currentPage;
        requestLayout();
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.currentPage = this.mCurrentPage;
        return savedState;
    }
}
