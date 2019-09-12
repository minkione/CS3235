package com.daimajia.swipe.implments;

import android.view.View;
import android.widget.BaseAdapter;
import com.daimajia.swipe.SwipeLayout;

public class SwipeItemAdapterMangerImpl extends SwipeItemMangerImpl {
    protected BaseAdapter mAdapter;

    public void bindView(View view, int i) {
    }

    public SwipeItemAdapterMangerImpl(BaseAdapter baseAdapter) {
        super(baseAdapter);
        this.mAdapter = baseAdapter;
    }

    public void initialize(View view, int i) {
        int swipeLayoutId = getSwipeLayoutId(i);
        OnLayoutListener onLayoutListener = new OnLayoutListener(i);
        SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(swipeLayoutId);
        if (swipeLayout != null) {
            SwipeMemory swipeMemory = new SwipeMemory(i);
            swipeLayout.addSwipeListener(swipeMemory);
            swipeLayout.addOnLayoutListener(onLayoutListener);
            swipeLayout.setTag(swipeLayoutId, new ValueBox(i, swipeMemory, onLayoutListener));
            this.mShownLayouts.add(swipeLayout);
            return;
        }
        throw new IllegalStateException("can not find SwipeLayout in target view");
    }

    public void updateConvertView(View view, int i) {
        int swipeLayoutId = getSwipeLayoutId(i);
        SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(swipeLayoutId);
        if (swipeLayout != null) {
            ValueBox valueBox = (ValueBox) swipeLayout.getTag(swipeLayoutId);
            valueBox.swipeMemory.setPosition(i);
            valueBox.onLayoutListener.setPosition(i);
            valueBox.position = i;
            return;
        }
        throw new IllegalStateException("can not find SwipeLayout in target view");
    }
}
