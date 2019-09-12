package com.daimajia.swipe.implments;

import android.support.p003v7.widget.RecyclerView.Adapter;
import android.view.View;
import com.daimajia.swipe.SwipeLayout;

public class SwipeItemRecyclerMangerImpl extends SwipeItemMangerImpl {
    protected Adapter mAdapter;

    public void initialize(View view, int i) {
    }

    public void updateConvertView(View view, int i) {
    }

    public SwipeItemRecyclerMangerImpl(Adapter adapter) {
        super(adapter);
        this.mAdapter = adapter;
    }

    public void bindView(View view, int i) {
        int swipeLayoutId = getSwipeLayoutId(i);
        OnLayoutListener onLayoutListener = new OnLayoutListener(i);
        SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(swipeLayoutId);
        if (swipeLayout == null) {
            throw new IllegalStateException("can not find SwipeLayout in target view");
        } else if (swipeLayout.getTag(swipeLayoutId) == null) {
            SwipeMemory swipeMemory = new SwipeMemory(i);
            swipeLayout.addSwipeListener(swipeMemory);
            swipeLayout.addOnLayoutListener(onLayoutListener);
            swipeLayout.setTag(swipeLayoutId, new ValueBox(i, swipeMemory, onLayoutListener));
            this.mShownLayouts.add(swipeLayout);
        } else {
            ValueBox valueBox = (ValueBox) swipeLayout.getTag(swipeLayoutId);
            valueBox.swipeMemory.setPosition(i);
            valueBox.onLayoutListener.setPosition(i);
            valueBox.position = i;
        }
    }
}
