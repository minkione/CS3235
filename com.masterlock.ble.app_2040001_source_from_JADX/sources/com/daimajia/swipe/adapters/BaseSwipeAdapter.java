package com.daimajia.swipe.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.implments.SwipeItemAdapterMangerImpl;
import com.daimajia.swipe.interfaces.SwipeAdapterInterface;
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface;
import com.daimajia.swipe.util.Attributes.Mode;
import java.util.List;

public abstract class BaseSwipeAdapter extends BaseAdapter implements SwipeItemMangerInterface, SwipeAdapterInterface {
    protected SwipeItemAdapterMangerImpl mItemManger = new SwipeItemAdapterMangerImpl(this);

    public abstract void fillValues(int i, View view);

    public abstract View generateView(int i, ViewGroup viewGroup);

    public abstract int getSwipeLayoutResourceId(int i);

    public final View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = generateView(i, viewGroup);
            this.mItemManger.initialize(view, i);
        } else {
            this.mItemManger.updateConvertView(view, i);
        }
        fillValues(i, view);
        return view;
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
