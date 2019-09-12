package com.daimajia.swipe.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.implments.SwipeItemAdapterMangerImpl;
import com.daimajia.swipe.interfaces.SwipeAdapterInterface;
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface;
import com.daimajia.swipe.util.Attributes.Mode;
import java.util.List;

public abstract class ArraySwipeAdapter<T> extends ArrayAdapter implements SwipeItemMangerInterface, SwipeAdapterInterface {
    private SwipeItemAdapterMangerImpl mItemManger = new SwipeItemAdapterMangerImpl(this);

    public ArraySwipeAdapter(Context context, int i) {
        super(context, i);
    }

    public ArraySwipeAdapter(Context context, int i, int i2) {
        super(context, i, i2);
    }

    public ArraySwipeAdapter(Context context, int i, T[] tArr) {
        super(context, i, tArr);
    }

    public ArraySwipeAdapter(Context context, int i, int i2, T[] tArr) {
        super(context, i, i2, tArr);
    }

    public ArraySwipeAdapter(Context context, int i, List<T> list) {
        super(context, i, list);
    }

    public ArraySwipeAdapter(Context context, int i, int i2, List<T> list) {
        super(context, i, i2, list);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        boolean z = view == null;
        View view2 = super.getView(i, view, viewGroup);
        if (z) {
            this.mItemManger.initialize(view2, i);
        } else {
            this.mItemManger.updateConvertView(view2, i);
        }
        return view2;
    }

    public void openItem(int i) {
        this.mItemManger.openItem(i);
    }

    public void closeItem(int i) {
        this.mItemManger.closeItem(i);
    }

    public void closeAllExcept(SwipeLayout swipeLayout) {
        this.mItemManger.closeAllExcept(swipeLayout);
    }

    public void closeAllItems() {
        this.mItemManger.closeAllItems();
    }

    public List<Integer> getOpenItems() {
        return this.mItemManger.getOpenItems();
    }

    public List<SwipeLayout> getOpenLayouts() {
        return this.mItemManger.getOpenLayouts();
    }

    public void removeShownLayouts(SwipeLayout swipeLayout) {
        this.mItemManger.removeShownLayouts(swipeLayout);
    }

    public boolean isOpen(int i) {
        return this.mItemManger.isOpen(i);
    }

    public Mode getMode() {
        return this.mItemManger.getMode();
    }

    public void setMode(Mode mode) {
        this.mItemManger.setMode(mode);
    }
}
