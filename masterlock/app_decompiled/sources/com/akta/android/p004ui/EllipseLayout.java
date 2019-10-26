package com.akta.android.p004ui;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.akta.android.util.MathUtils;

/* renamed from: com.akta.android.ui.EllipseLayout */
public class EllipseLayout extends ViewGroup {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    protected double angleDegDelta;
    protected double angleDegOffset;

    /* renamed from: com.akta.android.ui.EllipseLayout$MenuItemTag */
    public class MenuItemTag {
        /* access modifiers changed from: private */
        public double angleDeg;
        private int idx;

        public MenuItemTag() {
        }

        public int getIdx() {
            return this.idx;
        }

        public void setIdx(int i) {
            this.idx = i;
        }

        public double getAngleDeg() {
            return this.angleDeg;
        }

        public void setAngleDeg(double d) {
            this.angleDeg = d;
        }
    }

    public EllipseLayout(Context context) {
        this(context, null);
    }

    public EllipseLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        this.angleDegOffset = 0.0d;
        this.angleDegDelta = 0.0d;
    }

    /* access modifiers changed from: protected */
    public int left() {
        return getPaddingLeft();
    }

    /* access modifiers changed from: protected */
    public int top() {
        return getPaddingTop();
    }

    /* access modifiers changed from: protected */
    public int right() {
        return (getWidth() - getPaddingRight()) - left();
    }

    /* access modifiers changed from: protected */
    public int bottom() {
        return (getHeight() - getPaddingBottom()) - top();
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int childCount = getChildCount();
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            if (childAt.getVisibility() != 8) {
                Object tag = childAt.getTag();
                MenuItemTag initialMenuItemTag = getInitialMenuItemTag(i5);
                if (tag != null && (tag instanceof MenuItemTag)) {
                    initialMenuItemTag.angleDeg = ((MenuItemTag) tag).getAngleDeg() + this.angleDegDelta;
                }
                initialMenuItemTag.angleDeg = MathUtils.normalizeAngle(initialMenuItemTag.getAngleDeg());
                childAt.setTag(initialMenuItemTag);
                LayoutParams layoutParams = childAt.getLayoutParams();
                Point pointOnEdge = pointOnEdge(initialMenuItemTag.angleDeg);
                int left = pointOnEdge.x + left();
                int pVar = pointOnEdge.y + top();
                childAt.layout(left, pVar, layoutParams.width + left, layoutParams.height + pVar);
            }
        }
    }

    /* access modifiers changed from: protected */
    public double nextAngleInterval(double d) {
        return angleForIndex((int) Math.round(MathUtils.normalizeAngle(d) / angleForIndex(1)));
    }

    public Point pointOnEdge(double d) {
        LayoutParams layoutParams = getChildAt(0).getLayoutParams();
        return MathUtils.pointOnEllipse(d, right() - layoutParams.width, bottom() - layoutParams.height);
    }

    /* access modifiers changed from: protected */
    public double angleForIndex(int i) {
        double childCount = (double) getChildCount();
        Double.isNaN(childCount);
        double d = 360.0d / childCount;
        double d2 = (double) i;
        Double.isNaN(d2);
        return d * d2;
    }

    /* access modifiers changed from: protected */
    public MenuItemTag getInitialMenuItemTag(int i) {
        MenuItemTag menuItemTag = new MenuItemTag();
        menuItemTag.setIdx(i);
        menuItemTag.setAngleDeg(angleForIndex(i));
        return menuItemTag;
    }
}
