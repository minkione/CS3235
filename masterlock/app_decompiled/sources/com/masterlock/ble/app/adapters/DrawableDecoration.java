package com.masterlock.ble.app.adapters;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.RecyclerView.ItemDecoration;
import android.support.p003v7.widget.RecyclerView.LayoutManager;
import android.support.p003v7.widget.RecyclerView.LayoutParams;
import android.support.p003v7.widget.RecyclerView.State;
import android.view.View;
import java.util.EnumSet;

public class DrawableDecoration extends ItemDecoration {
    private Drawable mDivider;
    private int mLeft;
    private EnumSet<Position> mPositions;
    private int mRight = 0;

    public enum Position {
        NONE,
        START,
        MIDDLE,
        END
    }

    public DrawableDecoration(Drawable drawable, EnumSet<Position> enumSet) {
        this.mDivider = drawable;
        this.mPositions = enumSet;
        if (this.mPositions == null) {
            this.mPositions = EnumSet.of(Position.NONE);
        }
    }

    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, State state) {
        super.getItemOffsets(rect, view, recyclerView, state);
        if (this.mDivider != null) {
            if (getOrientation(recyclerView) == 1) {
                int childPosition = recyclerView.getChildPosition(view);
                if (this.mPositions.contains(Position.START) && childPosition == 0) {
                    rect.top = this.mDivider.getIntrinsicHeight();
                }
                if (this.mPositions.contains(Position.END) && childPosition == state.getItemCount() - 1) {
                    rect.bottom = this.mDivider.getIntrinsicHeight();
                }
                if (childPosition > 0 && this.mPositions.contains(Position.MIDDLE)) {
                    rect.top = this.mDivider.getIntrinsicHeight();
                }
            } else {
                rect.left = this.mDivider.getIntrinsicWidth();
            }
        }
    }

    private int getLastItemCount(RecyclerView recyclerView, State state) {
        LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (!(layoutManager instanceof LinearLayoutManager)) {
            throw new IllegalStateException("DrawableDecoration can only be used with a LinearLayoutManager.");
        } else if (((LinearLayoutManager) layoutManager).getReverseLayout()) {
            return 0;
        } else {
            return state.getItemCount() - 1;
        }
    }

    private int getFirstItemCount(RecyclerView recyclerView, State state) {
        LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (!(layoutManager instanceof LinearLayoutManager)) {
            throw new IllegalStateException("DrawableDecoration can only be used with a LinearLayoutManager.");
        } else if (((LinearLayoutManager) layoutManager).getReverseLayout()) {
            return state.getItemCount() - 1;
        } else {
            return 0;
        }
    }

    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, State state) {
        super.onDrawOver(canvas, recyclerView, state);
        if (this.mDivider == null) {
            super.onDrawOver(canvas, recyclerView, state);
            return;
        }
        int i = 0;
        if (getOrientation(recyclerView) == 1) {
            int paddingLeft = recyclerView.getPaddingLeft() + this.mLeft;
            int width = (recyclerView.getWidth() - recyclerView.getPaddingRight()) - this.mRight;
            int childCount = recyclerView.getChildCount();
            while (i < childCount) {
                View childAt = recyclerView.getChildAt(i);
                int childPosition = recyclerView.getChildPosition(childAt);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                int intrinsicHeight = this.mDivider.getIntrinsicHeight();
                if (this.mPositions.contains(Position.START) && childPosition == 0) {
                    this.mDivider.setBounds(paddingLeft, childAt.getTop() - intrinsicHeight, width, childAt.getTop());
                    this.mDivider.draw(canvas);
                }
                if (this.mPositions.contains(Position.END) && childPosition == state.getItemCount() - 1) {
                    this.mDivider.setBounds(paddingLeft, childAt.getBottom(), width, childAt.getBottom() + intrinsicHeight);
                    this.mDivider.draw(canvas);
                }
                if (i > 0 && this.mPositions.contains(Position.MIDDLE)) {
                    int top = (childAt.getTop() - layoutParams.topMargin) - intrinsicHeight;
                    this.mDivider.setBounds(paddingLeft, top, width, intrinsicHeight + top);
                    this.mDivider.draw(canvas);
                }
                i++;
            }
        } else {
            int paddingTop = recyclerView.getPaddingTop();
            int height = recyclerView.getHeight() - recyclerView.getPaddingBottom();
            int childCount2 = recyclerView.getChildCount();
            while (i < childCount2) {
                View childAt2 = recyclerView.getChildAt(i);
                LayoutParams layoutParams2 = (LayoutParams) childAt2.getLayoutParams();
                int left = childAt2.getLeft() - layoutParams2.leftMargin;
                this.mDivider.setBounds(left, paddingTop, this.mDivider.getIntrinsicWidth() + left, height);
                this.mDivider.draw(canvas);
                i++;
            }
        }
    }

    public void setLeft(int i) {
        this.mLeft = i;
    }

    public void setRight(int i) {
        this.mRight = i;
    }

    private int getOrientation(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation();
        }
        throw new IllegalStateException("DividerItemDecoration can only be used with a LinearLayoutManager.");
    }
}
