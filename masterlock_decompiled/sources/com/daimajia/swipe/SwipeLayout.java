package com.daimajia.swipe;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.p000v4.view.ViewCompat;
import android.support.p000v4.widget.ViewDragHelper;
import android.support.p000v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SwipeLayout extends FrameLayout {
    private GestureDetector gestureDetector;
    /* access modifiers changed from: private */
    public DoubleClickListener mDoubleClickListener;
    /* access modifiers changed from: private */
    public int mDragDistance;
    /* access modifiers changed from: private */
    public DragEdge mDragEdge;
    private ViewDragHelper mDragHelper;
    private Callback mDragHelperCallback;
    private int mEventCounter;
    private float mHorizontalSwipeOffset;
    private List<OnLayout> mOnLayoutListeners;
    private Map<View, ArrayList<OnRevealListener>> mRevealListeners;
    private Map<View, Boolean> mShowEntirely;
    /* access modifiers changed from: private */
    public ShowMode mShowMode;
    private List<SwipeDenier> mSwipeDeniers;
    private boolean mSwipeEnabled;
    /* access modifiers changed from: private */
    public List<SwipeListener> mSwipeListeners;
    private boolean mTouchConsumedByChild;
    private float mVerticalSwipeOffset;

    /* renamed from: sX */
    private float f34sX;

    /* renamed from: sY */
    private float f35sY;

    public interface DoubleClickListener {
        void onDoubleClick(SwipeLayout swipeLayout, boolean z);
    }

    public enum DragEdge {
        Left,
        Right,
        Top,
        Bottom
    }

    public interface OnLayout {
        void onLayout(SwipeLayout swipeLayout);
    }

    public interface OnRevealListener {
        void onReveal(View view, DragEdge dragEdge, float f, int i);
    }

    public enum ShowMode {
        LayDown,
        PullOut
    }

    public enum Status {
        Middle,
        Open,
        Close
    }

    public interface SwipeDenier {
        boolean shouldDenySwipe(MotionEvent motionEvent);
    }

    class SwipeDetector extends SimpleOnGestureListener {
        public boolean onDown(MotionEvent motionEvent) {
            return true;
        }

        SwipeDetector() {
        }

        public boolean onSingleTapUp(MotionEvent motionEvent) {
            if (SwipeLayout.this.mDoubleClickListener == null) {
                SwipeLayout.this.performAdapterViewItemClick(motionEvent);
            }
            return true;
        }

        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            if (SwipeLayout.this.mDoubleClickListener != null) {
                SwipeLayout.this.performAdapterViewItemClick(motionEvent);
            }
            return true;
        }

        public void onLongPress(MotionEvent motionEvent) {
            SwipeLayout.this.performLongClick();
        }

        public boolean onDoubleTap(MotionEvent motionEvent) {
            if (SwipeLayout.this.mDoubleClickListener != null) {
                ViewGroup bottomView = SwipeLayout.this.getBottomView();
                ViewGroup surfaceView = SwipeLayout.this.getSurfaceView();
                if (motionEvent.getX() <= ((float) bottomView.getLeft()) || motionEvent.getX() >= ((float) bottomView.getRight()) || motionEvent.getY() <= ((float) bottomView.getTop()) || motionEvent.getY() >= ((float) bottomView.getBottom())) {
                    bottomView = surfaceView;
                }
                SwipeLayout.this.mDoubleClickListener.onDoubleClick(SwipeLayout.this, bottomView == surfaceView);
            }
            return true;
        }
    }

    public interface SwipeListener {
        void onClose(SwipeLayout swipeLayout);

        void onHandRelease(SwipeLayout swipeLayout, float f, float f2);

        void onOpen(SwipeLayout swipeLayout);

        void onStartClose(SwipeLayout swipeLayout);

        void onStartOpen(SwipeLayout swipeLayout);

        void onUpdate(SwipeLayout swipeLayout, int i, int i2);
    }

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SwipeLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDragDistance = 0;
        this.mSwipeListeners = new ArrayList();
        this.mSwipeDeniers = new ArrayList();
        this.mRevealListeners = new HashMap();
        this.mShowEntirely = new HashMap();
        this.mSwipeEnabled = true;
        this.mDragHelperCallback = new Callback() {
            public int clampViewPositionHorizontal(View view, int i, int i2) {
                if (view == SwipeLayout.this.getSurfaceView()) {
                    switch (C04602.$SwitchMap$com$daimajia$swipe$SwipeLayout$DragEdge[SwipeLayout.this.mDragEdge.ordinal()]) {
                        case 1:
                        case 2:
                            return SwipeLayout.this.getPaddingLeft();
                        case 3:
                            if (i < SwipeLayout.this.getPaddingLeft()) {
                                return SwipeLayout.this.getPaddingLeft();
                            }
                            if (i > SwipeLayout.this.getPaddingLeft() + SwipeLayout.this.mDragDistance) {
                                return SwipeLayout.this.getPaddingLeft() + SwipeLayout.this.mDragDistance;
                            }
                            break;
                        case 4:
                            if (i > SwipeLayout.this.getPaddingLeft()) {
                                return SwipeLayout.this.getPaddingLeft();
                            }
                            if (i < SwipeLayout.this.getPaddingLeft() - SwipeLayout.this.mDragDistance) {
                                return SwipeLayout.this.getPaddingLeft() - SwipeLayout.this.mDragDistance;
                            }
                            break;
                    }
                } else if (view == SwipeLayout.this.getBottomView()) {
                    switch (C04602.$SwitchMap$com$daimajia$swipe$SwipeLayout$DragEdge[SwipeLayout.this.mDragEdge.ordinal()]) {
                        case 1:
                        case 2:
                            return SwipeLayout.this.getPaddingLeft();
                        case 3:
                            if (SwipeLayout.this.mShowMode == ShowMode.PullOut && i > SwipeLayout.this.getPaddingLeft()) {
                                return SwipeLayout.this.getPaddingLeft();
                            }
                        case 4:
                            if (SwipeLayout.this.mShowMode == ShowMode.PullOut && i < SwipeLayout.this.getMeasuredWidth() - SwipeLayout.this.mDragDistance) {
                                return SwipeLayout.this.getMeasuredWidth() - SwipeLayout.this.mDragDistance;
                            }
                    }
                }
                return i;
            }

            public int clampViewPositionVertical(View view, int i, int i2) {
                if (view == SwipeLayout.this.getSurfaceView()) {
                    switch (C04602.$SwitchMap$com$daimajia$swipe$SwipeLayout$DragEdge[SwipeLayout.this.mDragEdge.ordinal()]) {
                        case 1:
                            if (i < SwipeLayout.this.getPaddingTop()) {
                                return SwipeLayout.this.getPaddingTop();
                            }
                            if (i > SwipeLayout.this.getPaddingTop() + SwipeLayout.this.mDragDistance) {
                                return SwipeLayout.this.getPaddingTop() + SwipeLayout.this.mDragDistance;
                            }
                            break;
                        case 2:
                            if (i < SwipeLayout.this.getPaddingTop() - SwipeLayout.this.mDragDistance) {
                                return SwipeLayout.this.getPaddingTop() - SwipeLayout.this.mDragDistance;
                            }
                            if (i > SwipeLayout.this.getPaddingTop()) {
                                return SwipeLayout.this.getPaddingTop();
                            }
                            break;
                        case 3:
                        case 4:
                            return SwipeLayout.this.getPaddingTop();
                    }
                } else {
                    switch (C04602.$SwitchMap$com$daimajia$swipe$SwipeLayout$DragEdge[SwipeLayout.this.mDragEdge.ordinal()]) {
                        case 1:
                            if (SwipeLayout.this.mShowMode == ShowMode.PullOut) {
                                if (i > SwipeLayout.this.getPaddingTop()) {
                                    return SwipeLayout.this.getPaddingTop();
                                }
                            } else if (SwipeLayout.this.getSurfaceView().getTop() + i2 < SwipeLayout.this.getPaddingTop()) {
                                return SwipeLayout.this.getPaddingTop();
                            } else {
                                if (SwipeLayout.this.getSurfaceView().getTop() + i2 > SwipeLayout.this.getPaddingTop() + SwipeLayout.this.mDragDistance) {
                                    return SwipeLayout.this.getPaddingTop() + SwipeLayout.this.mDragDistance;
                                }
                            }
                            break;
                        case 2:
                            if (SwipeLayout.this.mShowMode == ShowMode.PullOut) {
                                if (i < SwipeLayout.this.getMeasuredHeight() - SwipeLayout.this.mDragDistance) {
                                    return SwipeLayout.this.getMeasuredHeight() - SwipeLayout.this.mDragDistance;
                                }
                            } else if (SwipeLayout.this.getSurfaceView().getTop() + i2 >= SwipeLayout.this.getPaddingTop()) {
                                return SwipeLayout.this.getPaddingTop();
                            } else {
                                if (SwipeLayout.this.getSurfaceView().getTop() + i2 <= SwipeLayout.this.getPaddingTop() - SwipeLayout.this.mDragDistance) {
                                    return SwipeLayout.this.getPaddingTop() - SwipeLayout.this.mDragDistance;
                                }
                            }
                            break;
                        case 3:
                        case 4:
                            return SwipeLayout.this.getPaddingTop();
                    }
                }
                return i;
            }

            public boolean tryCaptureView(View view, int i) {
                return view == SwipeLayout.this.getSurfaceView() || view == SwipeLayout.this.getBottomView();
            }

            public int getViewHorizontalDragRange(View view) {
                return SwipeLayout.this.mDragDistance;
            }

            public int getViewVerticalDragRange(View view) {
                return SwipeLayout.this.mDragDistance;
            }

            public void onViewReleased(View view, float f, float f2) {
                super.onViewReleased(view, f, f2);
                for (SwipeListener onHandRelease : SwipeLayout.this.mSwipeListeners) {
                    onHandRelease.onHandRelease(SwipeLayout.this, f, f2);
                }
                if (view == SwipeLayout.this.getSurfaceView()) {
                    SwipeLayout.this.processSurfaceRelease(f, f2);
                } else if (view == SwipeLayout.this.getBottomView()) {
                    if (SwipeLayout.this.getShowMode() == ShowMode.PullOut) {
                        SwipeLayout.this.processBottomPullOutRelease(f, f2);
                    } else if (SwipeLayout.this.getShowMode() == ShowMode.LayDown) {
                        SwipeLayout.this.processBottomLayDownMode(f, f2);
                    }
                }
                SwipeLayout.this.invalidate();
            }

            public void onViewPositionChanged(View view, int i, int i2, int i3, int i4) {
                int left = SwipeLayout.this.getSurfaceView().getLeft();
                int right = SwipeLayout.this.getSurfaceView().getRight();
                int top = SwipeLayout.this.getSurfaceView().getTop();
                int bottom = SwipeLayout.this.getSurfaceView().getBottom();
                if (view == SwipeLayout.this.getSurfaceView()) {
                    if (SwipeLayout.this.mShowMode == ShowMode.PullOut) {
                        if (SwipeLayout.this.mDragEdge == DragEdge.Left || SwipeLayout.this.mDragEdge == DragEdge.Right) {
                            SwipeLayout.this.getBottomView().offsetLeftAndRight(i3);
                        } else {
                            SwipeLayout.this.getBottomView().offsetTopAndBottom(i4);
                        }
                    }
                } else if (view == SwipeLayout.this.getBottomView()) {
                    if (SwipeLayout.this.mShowMode == ShowMode.PullOut) {
                        SwipeLayout.this.getSurfaceView().offsetLeftAndRight(i3);
                        SwipeLayout.this.getSurfaceView().offsetTopAndBottom(i4);
                    } else {
                        SwipeLayout swipeLayout = SwipeLayout.this;
                        Rect access$700 = swipeLayout.computeBottomLayDown(swipeLayout.mDragEdge);
                        SwipeLayout.this.getBottomView().layout(access$700.left, access$700.top, access$700.right, access$700.bottom);
                        int left2 = SwipeLayout.this.getSurfaceView().getLeft() + i3;
                        int top2 = SwipeLayout.this.getSurfaceView().getTop() + i4;
                        if (SwipeLayout.this.mDragEdge == DragEdge.Left && left2 < SwipeLayout.this.getPaddingLeft()) {
                            left2 = SwipeLayout.this.getPaddingLeft();
                        } else if (SwipeLayout.this.mDragEdge == DragEdge.Right && left2 > SwipeLayout.this.getPaddingLeft()) {
                            left2 = SwipeLayout.this.getPaddingLeft();
                        } else if (SwipeLayout.this.mDragEdge == DragEdge.Top && top2 < SwipeLayout.this.getPaddingTop()) {
                            top2 = SwipeLayout.this.getPaddingTop();
                        } else if (SwipeLayout.this.mDragEdge == DragEdge.Bottom && top2 > SwipeLayout.this.getPaddingTop()) {
                            top2 = SwipeLayout.this.getPaddingTop();
                        }
                        SwipeLayout.this.getSurfaceView().layout(left2, top2, SwipeLayout.this.getMeasuredWidth() + left2, SwipeLayout.this.getMeasuredHeight() + top2);
                    }
                }
                SwipeLayout.this.dispatchRevealEvent(left, top, right, bottom);
                SwipeLayout.this.dispatchSwipeEvent(left, top, i3, i4);
                SwipeLayout.this.invalidate();
            }
        };
        this.mEventCounter = 0;
        this.mTouchConsumedByChild = false;
        this.f34sX = -1.0f;
        this.f35sY = -1.0f;
        this.gestureDetector = new GestureDetector(getContext(), new SwipeDetector());
        this.mDragHelper = ViewDragHelper.create(this, this.mDragHelperCallback);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0457R.styleable.SwipeLayout);
        int i2 = obtainStyledAttributes.getInt(C0457R.styleable.SwipeLayout_drag_edge, DragEdge.Right.ordinal());
        this.mHorizontalSwipeOffset = obtainStyledAttributes.getDimension(C0457R.styleable.SwipeLayout_horizontalSwipeOffset, 0.0f);
        this.mVerticalSwipeOffset = obtainStyledAttributes.getDimension(C0457R.styleable.SwipeLayout_verticalSwipeOffset, 0.0f);
        this.mDragEdge = DragEdge.values()[i2];
        this.mShowMode = ShowMode.values()[obtainStyledAttributes.getInt(C0457R.styleable.SwipeLayout_show_mode, ShowMode.PullOut.ordinal())];
    }

    public void addSwipeListener(SwipeListener swipeListener) {
        this.mSwipeListeners.add(swipeListener);
    }

    public void removeSwipeListener(SwipeListener swipeListener) {
        this.mSwipeListeners.remove(swipeListener);
    }

    public void addSwipeDenier(SwipeDenier swipeDenier) {
        this.mSwipeDeniers.add(swipeDenier);
    }

    public void removeSwipeDenier(SwipeDenier swipeDenier) {
        this.mSwipeDeniers.remove(swipeDenier);
    }

    public void removeAllSwipeDeniers() {
        this.mSwipeDeniers.clear();
    }

    public void addRevealListener(int i, OnRevealListener onRevealListener) {
        View findViewById = findViewById(i);
        if (findViewById != null) {
            if (!this.mShowEntirely.containsKey(findViewById)) {
                this.mShowEntirely.put(findViewById, Boolean.valueOf(false));
            }
            if (this.mRevealListeners.get(findViewById) == null) {
                this.mRevealListeners.put(findViewById, new ArrayList());
            }
            ((ArrayList) this.mRevealListeners.get(findViewById)).add(onRevealListener);
            return;
        }
        throw new IllegalArgumentException("Child does not belong to SwipeListener.");
    }

    public void addRevealListener(int[] iArr, OnRevealListener onRevealListener) {
        for (int addRevealListener : iArr) {
            addRevealListener(addRevealListener, onRevealListener);
        }
    }

    public void removeRevealListener(int i, OnRevealListener onRevealListener) {
        View findViewById = findViewById(i);
        if (findViewById != null) {
            this.mShowEntirely.remove(findViewById);
            if (this.mRevealListeners.containsKey(findViewById)) {
                ((ArrayList) this.mRevealListeners.get(findViewById)).remove(onRevealListener);
            }
        }
    }

    public void removeAllRevealListeners(int i) {
        View findViewById = findViewById(i);
        if (findViewById != null) {
            this.mRevealListeners.remove(findViewById);
            this.mShowEntirely.remove(findViewById);
        }
    }

    /* access modifiers changed from: protected */
    public boolean isViewTotallyFirstShowed(View view, Rect rect, DragEdge dragEdge, int i, int i2, int i3, int i4) {
        if (((Boolean) this.mShowEntirely.get(view)).booleanValue()) {
            return false;
        }
        int i5 = rect.left;
        int i6 = rect.right;
        int i7 = rect.top;
        int i8 = rect.bottom;
        boolean z = true;
        if (getShowMode() != ShowMode.LayDown ? getShowMode() != ShowMode.PullOut || ((dragEdge != DragEdge.Right || i6 > getWidth()) && ((dragEdge != DragEdge.Left || i5 < getPaddingLeft()) && ((dragEdge != DragEdge.Top || i7 < getPaddingTop()) && (dragEdge != DragEdge.Bottom || i8 > getHeight())))) : (dragEdge != DragEdge.Right || i3 > i5) && ((dragEdge != DragEdge.Left || i < i6) && ((dragEdge != DragEdge.Top || i2 < i8) && (dragEdge != DragEdge.Bottom || i4 > i7)))) {
            z = false;
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public boolean isViewShowing(View view, Rect rect, DragEdge dragEdge, int i, int i2, int i3, int i4) {
        int i5 = rect.left;
        int i6 = rect.right;
        int i7 = rect.top;
        int i8 = rect.bottom;
        if (getShowMode() == ShowMode.LayDown) {
            switch (dragEdge) {
                case Top:
                    if (i2 >= i7 && i2 < i8) {
                        return true;
                    }
                case Bottom:
                    if (i4 > i7 && i4 <= i8) {
                        return true;
                    }
                case Left:
                    if (i < i6 && i >= i5) {
                        return true;
                    }
                case Right:
                    if (i3 > i5 && i3 <= i6) {
                        return true;
                    }
            }
        } else if (getShowMode() == ShowMode.PullOut) {
            switch (dragEdge) {
                case Top:
                    if (i7 < getPaddingTop() && i8 >= getPaddingTop()) {
                        return true;
                    }
                case Bottom:
                    if (i7 < getHeight() && i7 >= getPaddingTop()) {
                        return true;
                    }
                case Left:
                    if (i6 >= getPaddingLeft() && i5 < getPaddingLeft()) {
                        return true;
                    }
                case Right:
                    if (i5 <= getWidth() && i6 > getWidth()) {
                        return true;
                    }
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public Rect getRelativePosition(View view) {
        Rect rect = new Rect(view.getLeft(), view.getTop(), 0, 0);
        View view2 = view;
        while (view2.getParent() != null && view2 != getRootView()) {
            view2 = (View) view2.getParent();
            if (view2 == this) {
                break;
            }
            rect.left += view2.getLeft();
            rect.top += view2.getTop();
        }
        rect.right = rect.left + view.getMeasuredWidth();
        rect.bottom = rect.top + view.getMeasuredHeight();
        return rect;
    }

    /* access modifiers changed from: protected */
    public void dispatchSwipeEvent(int i, int i2, int i3, int i4) {
        DragEdge dragEdge = getDragEdge();
        boolean z = false;
        if (dragEdge != DragEdge.Left ? dragEdge != DragEdge.Right ? dragEdge != DragEdge.Top ? dragEdge != DragEdge.Bottom || i4 <= 0 : i4 >= 0 : i3 <= 0 : i3 >= 0) {
            z = true;
        }
        dispatchSwipeEvent(i, i2, z);
    }

    /* access modifiers changed from: protected */
    public void dispatchSwipeEvent(int i, int i2, boolean z) {
        safeBottomView();
        Status openStatus = getOpenStatus();
        if (!this.mSwipeListeners.isEmpty()) {
            this.mEventCounter++;
            for (SwipeListener swipeListener : this.mSwipeListeners) {
                if (this.mEventCounter == 1) {
                    if (z) {
                        swipeListener.onStartOpen(this);
                    } else {
                        swipeListener.onStartClose(this);
                    }
                }
                swipeListener.onUpdate(this, i - getPaddingLeft(), i2 - getPaddingTop());
            }
            if (openStatus == Status.Close) {
                for (SwipeListener onClose : this.mSwipeListeners) {
                    onClose.onClose(this);
                }
                this.mEventCounter = 0;
            }
            if (openStatus == Status.Open) {
                getBottomView().setEnabled(true);
                for (SwipeListener onOpen : this.mSwipeListeners) {
                    onOpen.onOpen(this);
                }
                this.mEventCounter = 0;
            }
        }
    }

    private void safeBottomView() {
        Status openStatus = getOpenStatus();
        ViewGroup bottomView = getBottomView();
        if (openStatus == Status.Close) {
            if (bottomView.getVisibility() != 4) {
                bottomView.setVisibility(4);
            }
        } else if (bottomView.getVisibility() != 0) {
            bottomView.setVisibility(0);
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchRevealEvent(int i, int i2, int i3, int i4) {
        if (!this.mRevealListeners.isEmpty()) {
            for (Entry entry : this.mRevealListeners.entrySet()) {
                View view = (View) entry.getKey();
                Rect relativePosition = getRelativePosition(view);
                if (isViewShowing(view, relativePosition, this.mDragEdge, i, i2, i3, i4)) {
                    int i5 = 0;
                    this.mShowEntirely.put(view, Boolean.valueOf(false));
                    float f = 0.0f;
                    if (getShowMode() != ShowMode.LayDown) {
                        if (getShowMode() == ShowMode.PullOut) {
                            switch (this.mDragEdge) {
                                case Top:
                                    i5 = relativePosition.bottom - getPaddingTop();
                                    f = ((float) i5) / ((float) view.getHeight());
                                    break;
                                case Bottom:
                                    i5 = relativePosition.top - getHeight();
                                    f = ((float) i5) / ((float) view.getHeight());
                                    break;
                                case Left:
                                    i5 = relativePosition.right - getPaddingLeft();
                                    f = ((float) i5) / ((float) view.getWidth());
                                    break;
                                case Right:
                                    i5 = relativePosition.left - getWidth();
                                    f = ((float) i5) / ((float) view.getWidth());
                                    break;
                            }
                        }
                    } else {
                        switch (this.mDragEdge) {
                            case Top:
                                i5 = relativePosition.top - i2;
                                f = ((float) i5) / ((float) view.getHeight());
                                break;
                            case Bottom:
                                i5 = relativePosition.bottom - i4;
                                f = ((float) i5) / ((float) view.getHeight());
                                break;
                            case Left:
                                i5 = relativePosition.left - i;
                                f = ((float) i5) / ((float) view.getWidth());
                                break;
                            case Right:
                                i5 = relativePosition.right - i3;
                                f = ((float) i5) / ((float) view.getWidth());
                                break;
                        }
                    }
                    Iterator it = ((ArrayList) entry.getValue()).iterator();
                    while (it.hasNext()) {
                        ((OnRevealListener) it.next()).onReveal(view, this.mDragEdge, Math.abs(f), i5);
                        if (Math.abs(f) == 1.0f) {
                            this.mShowEntirely.put(view, Boolean.valueOf(true));
                        }
                    }
                }
                if (isViewTotallyFirstShowed(view, relativePosition, this.mDragEdge, i, i2, i3, i4)) {
                    this.mShowEntirely.put(view, Boolean.valueOf(true));
                    Iterator it2 = ((ArrayList) entry.getValue()).iterator();
                    while (it2.hasNext()) {
                        OnRevealListener onRevealListener = (OnRevealListener) it2.next();
                        if (this.mDragEdge == DragEdge.Left || this.mDragEdge == DragEdge.Right) {
                            onRevealListener.onReveal(view, this.mDragEdge, 1.0f, view.getWidth());
                        } else {
                            onRevealListener.onReveal(view, this.mDragEdge, 1.0f, view.getHeight());
                        }
                    }
                }
            }
        }
    }

    public void computeScroll() {
        super.computeScroll();
        if (this.mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void addOnLayoutListener(OnLayout onLayout) {
        if (this.mOnLayoutListeners == null) {
            this.mOnLayoutListeners = new ArrayList();
        }
        this.mOnLayoutListeners.add(onLayout);
    }

    public void removeOnLayoutListener(OnLayout onLayout) {
        List<OnLayout> list = this.mOnLayoutListeners;
        if (list != null) {
            list.remove(onLayout);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (getChildCount() == 2) {
            if (!(getChildAt(0) instanceof ViewGroup) || !(getChildAt(1) instanceof ViewGroup)) {
                throw new IllegalArgumentException("The 2 children in SwipeLayout must be an instance of ViewGroup");
            }
            if (this.mShowMode == ShowMode.PullOut) {
                layoutPullOut();
            } else if (this.mShowMode == ShowMode.LayDown) {
                layoutLayDown();
            }
            safeBottomView();
            if (this.mOnLayoutListeners != null) {
                for (int i5 = 0; i5 < this.mOnLayoutListeners.size(); i5++) {
                    ((OnLayout) this.mOnLayoutListeners.get(i5)).onLayout(this);
                }
                return;
            }
            return;
        }
        throw new IllegalStateException("You need 2  views in SwipeLayout");
    }

    /* access modifiers changed from: 0000 */
    public void layoutPullOut() {
        Rect computeSurfaceLayoutArea = computeSurfaceLayoutArea(false);
        getSurfaceView().layout(computeSurfaceLayoutArea.left, computeSurfaceLayoutArea.top, computeSurfaceLayoutArea.right, computeSurfaceLayoutArea.bottom);
        Rect computeBottomLayoutAreaViaSurface = computeBottomLayoutAreaViaSurface(ShowMode.PullOut, computeSurfaceLayoutArea);
        getBottomView().layout(computeBottomLayoutAreaViaSurface.left, computeBottomLayoutAreaViaSurface.top, computeBottomLayoutAreaViaSurface.right, computeBottomLayoutAreaViaSurface.bottom);
        bringChildToFront(getSurfaceView());
    }

    /* access modifiers changed from: 0000 */
    public void layoutLayDown() {
        Rect computeSurfaceLayoutArea = computeSurfaceLayoutArea(false);
        getSurfaceView().layout(computeSurfaceLayoutArea.left, computeSurfaceLayoutArea.top, computeSurfaceLayoutArea.right, computeSurfaceLayoutArea.bottom);
        Rect computeBottomLayoutAreaViaSurface = computeBottomLayoutAreaViaSurface(ShowMode.LayDown, computeSurfaceLayoutArea);
        getBottomView().layout(computeBottomLayoutAreaViaSurface.left, computeBottomLayoutAreaViaSurface.top, computeBottomLayoutAreaViaSurface.right, computeBottomLayoutAreaViaSurface.bottom);
        bringChildToFront(getSurfaceView());
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mDragEdge == DragEdge.Left || this.mDragEdge == DragEdge.Right) {
            this.mDragDistance = getBottomView().getMeasuredWidth() - dp2px(this.mHorizontalSwipeOffset);
        } else {
            this.mDragDistance = getBottomView().getMeasuredHeight() - dp2px(this.mVerticalSwipeOffset);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean z = true;
        if (!isEnabled() || !isEnabledInAdapterView()) {
            return true;
        }
        if (!isSwipeEnabled()) {
            return false;
        }
        for (SwipeDenier swipeDenier : this.mSwipeDeniers) {
            if (swipeDenier != null && swipeDenier.shouldDenySwipe(motionEvent)) {
                return false;
            }
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 3) {
            switch (actionMasked) {
                case 0:
                    Status openStatus = getOpenStatus();
                    if (openStatus != Status.Close) {
                        if (openStatus == Status.Open) {
                            if (childNeedHandleTouchEvent(getBottomView(), motionEvent) == null) {
                                z = false;
                            }
                            this.mTouchConsumedByChild = z;
                            break;
                        }
                    } else {
                        if (childNeedHandleTouchEvent(getSurfaceView(), motionEvent) == null) {
                            z = false;
                        }
                        this.mTouchConsumedByChild = z;
                        break;
                    }
                    break;
                case 1:
                    break;
            }
        }
        this.mTouchConsumedByChild = false;
        if (this.mTouchConsumedByChild) {
            return false;
        }
        return this.mDragHelper.shouldInterceptTouchEvent(motionEvent);
    }

    private View childNeedHandleTouchEvent(ViewGroup viewGroup, MotionEvent motionEvent) {
        if (viewGroup == null) {
            return null;
        }
        if (viewGroup.onTouchEvent(motionEvent)) {
            return viewGroup;
        }
        for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = viewGroup.getChildAt(childCount);
            if (childAt instanceof ViewGroup) {
                View childNeedHandleTouchEvent = childNeedHandleTouchEvent((ViewGroup) childAt, motionEvent);
                if (childNeedHandleTouchEvent != null) {
                    return childNeedHandleTouchEvent;
                }
            } else if (childNeedHandleTouchEvent(viewGroup.getChildAt(childCount), motionEvent)) {
                return viewGroup.getChildAt(childCount);
            }
        }
        return null;
    }

    private boolean childNeedHandleTouchEvent(View view, MotionEvent motionEvent) {
        if (view == null) {
            return false;
        }
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        int i = iArr[0];
        int i2 = iArr[1];
        if (motionEvent.getRawX() <= ((float) i) || motionEvent.getRawX() >= ((float) (i + view.getWidth())) || motionEvent.getRawY() <= ((float) i2) || motionEvent.getRawY() >= ((float) (i2 + view.getHeight()))) {
            return false;
        }
        return view.onTouchEvent(motionEvent);
    }

    /* JADX WARNING: Removed duplicated region for block: B:102:0x0112  */
    /* JADX WARNING: Removed duplicated region for block: B:104:0x0116  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0096  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00c2  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x00ec  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(android.view.MotionEvent r14) {
        /*
            r13 = this;
            boolean r0 = r13.isEnabledInAdapterView()
            r1 = 1
            if (r0 == 0) goto L_0x0153
            boolean r0 = r13.isEnabled()
            if (r0 != 0) goto L_0x000f
            goto L_0x0153
        L_0x000f:
            boolean r0 = r13.isSwipeEnabled()
            if (r0 != 0) goto L_0x001a
            boolean r14 = super.onTouchEvent(r14)
            return r14
        L_0x001a:
            int r0 = r14.getActionMasked()
            android.view.ViewParent r2 = r13.getParent()
            android.view.GestureDetector r3 = r13.gestureDetector
            r3.onTouchEvent(r14)
            com.daimajia.swipe.SwipeLayout$Status r3 = r13.getOpenStatus()
            r4 = 0
            com.daimajia.swipe.SwipeLayout$Status r5 = com.daimajia.swipe.SwipeLayout.Status.Close
            if (r3 != r5) goto L_0x0035
            android.view.ViewGroup r4 = r13.getSurfaceView()
            goto L_0x003d
        L_0x0035:
            com.daimajia.swipe.SwipeLayout$Status r5 = com.daimajia.swipe.SwipeLayout.Status.Open
            if (r3 != r5) goto L_0x003d
            android.view.ViewGroup r4 = r13.getBottomView()
        L_0x003d:
            r5 = 0
            switch(r0) {
                case 0: goto L_0x0130;
                case 1: goto L_0x0124;
                case 2: goto L_0x0043;
                case 3: goto L_0x0124;
                default: goto L_0x0041;
            }
        L_0x0041:
            goto L_0x014a
        L_0x0043:
            float r0 = r14.getRawX()
            float r6 = r13.f34sX
            float r0 = r0 - r6
            float r6 = r14.getRawY()
            float r7 = r13.f35sY
            float r6 = r6 - r7
            float r7 = r6 / r0
            float r7 = java.lang.Math.abs(r7)
            double r7 = (double) r7
            double r7 = java.lang.Math.atan(r7)
            double r7 = java.lang.Math.toDegrees(r7)
            float r7 = (float) r7
            com.daimajia.swipe.SwipeLayout$DragEdge r8 = r13.mDragEdge
            com.daimajia.swipe.SwipeLayout$DragEdge r9 = com.daimajia.swipe.SwipeLayout.DragEdge.Right
            r10 = 1106247680(0x41f00000, float:30.0)
            r11 = 0
            if (r8 != r9) goto L_0x008f
            com.daimajia.swipe.SwipeLayout$Status r8 = com.daimajia.swipe.SwipeLayout.Status.Open
            if (r3 != r8) goto L_0x0072
            int r8 = (r0 > r11 ? 1 : (r0 == r11 ? 0 : -1))
            if (r8 > 0) goto L_0x007a
        L_0x0072:
            com.daimajia.swipe.SwipeLayout$Status r8 = com.daimajia.swipe.SwipeLayout.Status.Close
            if (r3 != r8) goto L_0x007c
            int r8 = (r0 > r11 ? 1 : (r0 == r11 ? 0 : -1))
            if (r8 >= 0) goto L_0x007c
        L_0x007a:
            r8 = 1
            goto L_0x007d
        L_0x007c:
            r8 = 0
        L_0x007d:
            if (r8 != 0) goto L_0x0086
            com.daimajia.swipe.SwipeLayout$Status r8 = com.daimajia.swipe.SwipeLayout.Status.Middle
            if (r3 != r8) goto L_0x0084
            goto L_0x0086
        L_0x0084:
            r8 = 0
            goto L_0x0087
        L_0x0086:
            r8 = 1
        L_0x0087:
            int r9 = (r7 > r10 ? 1 : (r7 == r10 ? 0 : -1))
            if (r9 > 0) goto L_0x008d
            if (r8 != 0) goto L_0x008f
        L_0x008d:
            r8 = 1
            goto L_0x0090
        L_0x008f:
            r8 = 0
        L_0x0090:
            com.daimajia.swipe.SwipeLayout$DragEdge r9 = r13.mDragEdge
            com.daimajia.swipe.SwipeLayout$DragEdge r12 = com.daimajia.swipe.SwipeLayout.DragEdge.Left
            if (r9 != r12) goto L_0x00ba
            com.daimajia.swipe.SwipeLayout$Status r9 = com.daimajia.swipe.SwipeLayout.Status.Open
            if (r3 != r9) goto L_0x009e
            int r9 = (r0 > r11 ? 1 : (r0 == r11 ? 0 : -1))
            if (r9 < 0) goto L_0x00a6
        L_0x009e:
            com.daimajia.swipe.SwipeLayout$Status r9 = com.daimajia.swipe.SwipeLayout.Status.Close
            if (r3 != r9) goto L_0x00a8
            int r0 = (r0 > r11 ? 1 : (r0 == r11 ? 0 : -1))
            if (r0 <= 0) goto L_0x00a8
        L_0x00a6:
            r0 = 1
            goto L_0x00a9
        L_0x00a8:
            r0 = 0
        L_0x00a9:
            if (r0 != 0) goto L_0x00b2
            com.daimajia.swipe.SwipeLayout$Status r0 = com.daimajia.swipe.SwipeLayout.Status.Middle
            if (r3 != r0) goto L_0x00b0
            goto L_0x00b2
        L_0x00b0:
            r0 = 0
            goto L_0x00b3
        L_0x00b2:
            r0 = 1
        L_0x00b3:
            int r9 = (r7 > r10 ? 1 : (r7 == r10 ? 0 : -1))
            if (r9 > 0) goto L_0x00b9
            if (r0 != 0) goto L_0x00ba
        L_0x00b9:
            r8 = 1
        L_0x00ba:
            com.daimajia.swipe.SwipeLayout$DragEdge r0 = r13.mDragEdge
            com.daimajia.swipe.SwipeLayout$DragEdge r9 = com.daimajia.swipe.SwipeLayout.DragEdge.Top
            r10 = 1114636288(0x42700000, float:60.0)
            if (r0 != r9) goto L_0x00e6
            com.daimajia.swipe.SwipeLayout$Status r0 = com.daimajia.swipe.SwipeLayout.Status.Open
            if (r3 != r0) goto L_0x00ca
            int r0 = (r6 > r11 ? 1 : (r6 == r11 ? 0 : -1))
            if (r0 < 0) goto L_0x00d2
        L_0x00ca:
            com.daimajia.swipe.SwipeLayout$Status r0 = com.daimajia.swipe.SwipeLayout.Status.Close
            if (r3 != r0) goto L_0x00d4
            int r0 = (r6 > r11 ? 1 : (r6 == r11 ? 0 : -1))
            if (r0 <= 0) goto L_0x00d4
        L_0x00d2:
            r0 = 1
            goto L_0x00d5
        L_0x00d4:
            r0 = 0
        L_0x00d5:
            if (r0 != 0) goto L_0x00de
            com.daimajia.swipe.SwipeLayout$Status r0 = com.daimajia.swipe.SwipeLayout.Status.Middle
            if (r3 != r0) goto L_0x00dc
            goto L_0x00de
        L_0x00dc:
            r0 = 0
            goto L_0x00df
        L_0x00de:
            r0 = 1
        L_0x00df:
            int r9 = (r7 > r10 ? 1 : (r7 == r10 ? 0 : -1))
            if (r9 < 0) goto L_0x00e5
            if (r0 != 0) goto L_0x00e6
        L_0x00e5:
            r8 = 1
        L_0x00e6:
            com.daimajia.swipe.SwipeLayout$DragEdge r0 = r13.mDragEdge
            com.daimajia.swipe.SwipeLayout$DragEdge r9 = com.daimajia.swipe.SwipeLayout.DragEdge.Bottom
            if (r0 != r9) goto L_0x0110
            com.daimajia.swipe.SwipeLayout$Status r0 = com.daimajia.swipe.SwipeLayout.Status.Open
            if (r3 != r0) goto L_0x00f4
            int r0 = (r6 > r11 ? 1 : (r6 == r11 ? 0 : -1))
            if (r0 > 0) goto L_0x00fc
        L_0x00f4:
            com.daimajia.swipe.SwipeLayout$Status r0 = com.daimajia.swipe.SwipeLayout.Status.Close
            if (r3 != r0) goto L_0x00fe
            int r0 = (r6 > r11 ? 1 : (r6 == r11 ? 0 : -1))
            if (r0 >= 0) goto L_0x00fe
        L_0x00fc:
            r0 = 1
            goto L_0x00ff
        L_0x00fe:
            r0 = 0
        L_0x00ff:
            if (r0 != 0) goto L_0x0108
            com.daimajia.swipe.SwipeLayout$Status r0 = com.daimajia.swipe.SwipeLayout.Status.Middle
            if (r3 != r0) goto L_0x0106
            goto L_0x0108
        L_0x0106:
            r0 = 0
            goto L_0x0109
        L_0x0108:
            r0 = 1
        L_0x0109:
            int r3 = (r7 > r10 ? 1 : (r7 == r10 ? 0 : -1))
            if (r3 < 0) goto L_0x010f
            if (r0 != 0) goto L_0x0110
        L_0x010f:
            r8 = 1
        L_0x0110:
            if (r8 == 0) goto L_0x0116
            r2.requestDisallowInterceptTouchEvent(r5)
            return r5
        L_0x0116:
            if (r4 == 0) goto L_0x011b
            r4.setPressed(r5)
        L_0x011b:
            r2.requestDisallowInterceptTouchEvent(r1)
            android.support.v4.widget.ViewDragHelper r0 = r13.mDragHelper
            r0.processTouchEvent(r14)
            goto L_0x0152
        L_0x0124:
            r0 = -1082130432(0xffffffffbf800000, float:-1.0)
            r13.f34sX = r0
            r13.f35sY = r0
            if (r4 == 0) goto L_0x014a
            r4.setPressed(r5)
            goto L_0x014a
        L_0x0130:
            android.support.v4.widget.ViewDragHelper r0 = r13.mDragHelper
            r0.processTouchEvent(r14)
            r2.requestDisallowInterceptTouchEvent(r1)
            float r0 = r14.getRawX()
            r13.f34sX = r0
            float r14 = r14.getRawY()
            r13.f35sY = r14
            if (r4 == 0) goto L_0x0149
            r4.setPressed(r1)
        L_0x0149:
            return r1
        L_0x014a:
            r2.requestDisallowInterceptTouchEvent(r1)
            android.support.v4.widget.ViewDragHelper r0 = r13.mDragHelper
            r0.processTouchEvent(r14)
        L_0x0152:
            return r1
        L_0x0153:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.daimajia.swipe.SwipeLayout.onTouchEvent(android.view.MotionEvent):boolean");
    }

    private boolean isEnabledInAdapterView() {
        AdapterView adapterView = getAdapterView();
        if (adapterView != null) {
            Adapter adapter = adapterView.getAdapter();
            if (adapter != null) {
                int positionForView = adapterView.getPositionForView(this);
                if (adapter instanceof BaseAdapter) {
                    return ((BaseAdapter) adapter).isEnabled(positionForView);
                }
                if (adapter instanceof ListAdapter) {
                    return ((ListAdapter) adapter).isEnabled(positionForView);
                }
            }
        }
        return true;
    }

    public void setSwipeEnabled(boolean z) {
        this.mSwipeEnabled = z;
    }

    public boolean isSwipeEnabled() {
        return this.mSwipeEnabled;
    }

    private boolean insideAdapterView() {
        return getAdapterView() != null;
    }

    private AdapterView getAdapterView() {
        for (ViewParent parent = getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof AdapterView) {
                return (AdapterView) parent;
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void performAdapterViewItemClick(MotionEvent motionEvent) {
        for (ViewParent parent = getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof AdapterView) {
                AdapterView adapterView = (AdapterView) parent;
                int positionForView = adapterView.getPositionForView(this);
                if (positionForView != -1 && adapterView.performItemClick(adapterView.getChildAt(positionForView - adapterView.getFirstVisiblePosition()), positionForView, adapterView.getAdapter().getItemId(positionForView))) {
                    return;
                }
            } else if ((parent instanceof View) && ((View) parent).performClick()) {
                return;
            }
        }
    }

    public void setDragEdge(DragEdge dragEdge) {
        this.mDragEdge = dragEdge;
        requestLayout();
    }

    public void setDragDistance(int i) {
        if (i >= 0) {
            this.mDragDistance = dp2px((float) i);
            requestLayout();
            return;
        }
        throw new IllegalArgumentException("Drag distance can not be < 0");
    }

    public void setShowMode(ShowMode showMode) {
        this.mShowMode = showMode;
        requestLayout();
    }

    public DragEdge getDragEdge() {
        return this.mDragEdge;
    }

    public int getDragDistance() {
        return this.mDragDistance;
    }

    public ShowMode getShowMode() {
        return this.mShowMode;
    }

    public ViewGroup getSurfaceView() {
        return (ViewGroup) getChildAt(1);
    }

    public ViewGroup getBottomView() {
        return (ViewGroup) getChildAt(0);
    }

    public Status getOpenStatus() {
        int left = getSurfaceView().getLeft();
        int top = getSurfaceView().getTop();
        if (left == getPaddingLeft() && top == getPaddingTop()) {
            return Status.Close;
        }
        if (left == getPaddingLeft() - this.mDragDistance || left == getPaddingLeft() + this.mDragDistance || top == getPaddingTop() - this.mDragDistance || top == getPaddingTop() + this.mDragDistance) {
            return Status.Open;
        }
        return Status.Middle;
    }

    /* access modifiers changed from: private */
    public void processSurfaceRelease(float f, float f2) {
        if (f == 0.0f && getOpenStatus() == Status.Middle) {
            close();
        }
        if (this.mDragEdge == DragEdge.Left || this.mDragEdge == DragEdge.Right) {
            if (f > 0.0f) {
                if (this.mDragEdge == DragEdge.Left) {
                    open();
                } else {
                    close();
                }
            }
            if (f >= 0.0f) {
                return;
            }
            if (this.mDragEdge == DragEdge.Left) {
                close();
            } else {
                open();
            }
        } else {
            if (f2 > 0.0f) {
                if (this.mDragEdge == DragEdge.Top) {
                    open();
                } else {
                    close();
                }
            }
            if (f2 >= 0.0f) {
                return;
            }
            if (this.mDragEdge == DragEdge.Top) {
                close();
            } else {
                open();
            }
        }
    }

    /* access modifiers changed from: private */
    public void processBottomPullOutRelease(float f, float f2) {
        if (f == 0.0f && getOpenStatus() == Status.Middle) {
            close();
        }
        if (this.mDragEdge == DragEdge.Left || this.mDragEdge == DragEdge.Right) {
            if (f > 0.0f) {
                if (this.mDragEdge == DragEdge.Left) {
                    open();
                } else {
                    close();
                }
            }
            if (f >= 0.0f) {
                return;
            }
            if (this.mDragEdge == DragEdge.Left) {
                close();
            } else {
                open();
            }
        } else {
            if (f2 > 0.0f) {
                if (this.mDragEdge == DragEdge.Top) {
                    open();
                } else {
                    close();
                }
            }
            if (f2 >= 0.0f) {
                return;
            }
            if (this.mDragEdge == DragEdge.Top) {
                close();
            } else {
                open();
            }
        }
    }

    /* access modifiers changed from: private */
    public void processBottomLayDownMode(float f, float f2) {
        if (f == 0.0f && getOpenStatus() == Status.Middle) {
            close();
        }
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        if (f < 0.0f && this.mDragEdge == DragEdge.Right) {
            paddingLeft -= this.mDragDistance;
        }
        if (f > 0.0f && this.mDragEdge == DragEdge.Left) {
            paddingLeft += this.mDragDistance;
        }
        if (f2 > 0.0f && this.mDragEdge == DragEdge.Top) {
            paddingTop += this.mDragDistance;
        }
        if (f2 < 0.0f && this.mDragEdge == DragEdge.Bottom) {
            paddingTop -= this.mDragDistance;
        }
        this.mDragHelper.smoothSlideViewTo(getSurfaceView(), paddingLeft, paddingTop);
        invalidate();
    }

    public void open() {
        open(true, true);
    }

    public void open(boolean z) {
        open(z, true);
    }

    public void open(boolean z, boolean z2) {
        ViewGroup surfaceView = getSurfaceView();
        ViewGroup bottomView = getBottomView();
        Rect computeSurfaceLayoutArea = computeSurfaceLayoutArea(true);
        if (z) {
            this.mDragHelper.smoothSlideViewTo(getSurfaceView(), computeSurfaceLayoutArea.left, computeSurfaceLayoutArea.top);
        } else {
            int left = computeSurfaceLayoutArea.left - surfaceView.getLeft();
            int top = computeSurfaceLayoutArea.top - surfaceView.getTop();
            surfaceView.layout(computeSurfaceLayoutArea.left, computeSurfaceLayoutArea.top, computeSurfaceLayoutArea.right, computeSurfaceLayoutArea.bottom);
            if (getShowMode() == ShowMode.PullOut) {
                Rect computeBottomLayoutAreaViaSurface = computeBottomLayoutAreaViaSurface(ShowMode.PullOut, computeSurfaceLayoutArea);
                bottomView.layout(computeBottomLayoutAreaViaSurface.left, computeBottomLayoutAreaViaSurface.top, computeBottomLayoutAreaViaSurface.right, computeBottomLayoutAreaViaSurface.bottom);
            }
            if (z2) {
                dispatchRevealEvent(computeSurfaceLayoutArea.left, computeSurfaceLayoutArea.top, computeSurfaceLayoutArea.right, computeSurfaceLayoutArea.bottom);
                dispatchSwipeEvent(computeSurfaceLayoutArea.left, computeSurfaceLayoutArea.top, left, top);
            } else {
                safeBottomView();
            }
        }
        invalidate();
    }

    public void close() {
        close(true, true);
    }

    public void close(boolean z) {
        close(z, true);
    }

    public void close(boolean z, boolean z2) {
        ViewGroup surfaceView = getSurfaceView();
        if (z) {
            this.mDragHelper.smoothSlideViewTo(getSurfaceView(), getPaddingLeft(), getPaddingTop());
        } else {
            Rect computeSurfaceLayoutArea = computeSurfaceLayoutArea(false);
            int left = computeSurfaceLayoutArea.left - surfaceView.getLeft();
            int top = computeSurfaceLayoutArea.top - surfaceView.getTop();
            surfaceView.layout(computeSurfaceLayoutArea.left, computeSurfaceLayoutArea.top, computeSurfaceLayoutArea.right, computeSurfaceLayoutArea.bottom);
            if (z2) {
                dispatchRevealEvent(computeSurfaceLayoutArea.left, computeSurfaceLayoutArea.top, computeSurfaceLayoutArea.right, computeSurfaceLayoutArea.bottom);
                dispatchSwipeEvent(computeSurfaceLayoutArea.left, computeSurfaceLayoutArea.top, left, top);
            } else {
                safeBottomView();
            }
        }
        invalidate();
    }

    public void toggle() {
        toggle(true);
    }

    public void toggle(boolean z) {
        if (getOpenStatus() == Status.Open) {
            close(z);
        } else if (getOpenStatus() == Status.Close) {
            open(z);
        }
    }

    private Rect computeSurfaceLayoutArea(boolean z) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        if (z) {
            if (this.mDragEdge == DragEdge.Left) {
                paddingLeft = this.mDragDistance + getPaddingLeft();
            } else if (this.mDragEdge == DragEdge.Right) {
                paddingLeft = getPaddingLeft() - this.mDragDistance;
            } else if (this.mDragEdge == DragEdge.Top) {
                paddingTop = this.mDragDistance + getPaddingTop();
            } else {
                paddingTop = getPaddingTop() - this.mDragDistance;
            }
        }
        return new Rect(paddingLeft, paddingTop, getMeasuredWidth() + paddingLeft, getMeasuredHeight() + paddingTop);
    }

    private Rect computeBottomLayoutAreaViaSurface(ShowMode showMode, Rect rect) {
        int i = rect.left;
        int i2 = rect.top;
        int i3 = rect.right;
        int i4 = rect.bottom;
        if (showMode == ShowMode.PullOut) {
            if (this.mDragEdge == DragEdge.Left) {
                i = rect.left - this.mDragDistance;
            } else if (this.mDragEdge == DragEdge.Right) {
                i = rect.right;
            } else {
                i2 = this.mDragEdge == DragEdge.Top ? rect.top - this.mDragDistance : rect.bottom;
            }
            if (this.mDragEdge == DragEdge.Left || this.mDragEdge == DragEdge.Right) {
                i4 = rect.bottom;
                i3 = i + getBottomView().getMeasuredWidth();
            } else {
                i4 = i2 + getBottomView().getMeasuredHeight();
                i3 = rect.right;
            }
        } else if (showMode == ShowMode.LayDown) {
            if (this.mDragEdge == DragEdge.Left) {
                i3 = i + this.mDragDistance;
            } else if (this.mDragEdge == DragEdge.Right) {
                i = i3 - this.mDragDistance;
            } else if (this.mDragEdge == DragEdge.Top) {
                i4 = i2 + this.mDragDistance;
            } else {
                i2 = i4 - this.mDragDistance;
            }
        }
        return new Rect(i, i2, i3, i4);
    }

    /* access modifiers changed from: private */
    public Rect computeBottomLayDown(DragEdge dragEdge) {
        int i;
        int i2;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        if (dragEdge == DragEdge.Right) {
            paddingLeft = getMeasuredWidth() - this.mDragDistance;
        } else if (dragEdge == DragEdge.Bottom) {
            paddingTop = getMeasuredHeight() - this.mDragDistance;
        }
        if (dragEdge == DragEdge.Left || dragEdge == DragEdge.Right) {
            i = this.mDragDistance + paddingLeft;
            i2 = getMeasuredHeight() + paddingTop;
        } else {
            i = getMeasuredWidth() + paddingLeft;
            i2 = this.mDragDistance + paddingTop;
        }
        return new Rect(paddingLeft, paddingTop, i, i2);
    }

    public void setOnDoubleClickListener(DoubleClickListener doubleClickListener) {
        this.mDoubleClickListener = doubleClickListener;
    }

    private int dp2px(float f) {
        return (int) ((f * getContext().getResources().getDisplayMetrics().density) + 0.5f);
    }
}
