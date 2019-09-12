package com.daimajia.swipe.implments;

import android.support.p003v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.widget.BaseAdapter;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.SwipeLayout.OnLayout;
import com.daimajia.swipe.interfaces.SwipeAdapterInterface;
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface;
import com.daimajia.swipe.util.Attributes.Mode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class SwipeItemMangerImpl implements SwipeItemMangerInterface {
    public final int INVALID_POSITION = -1;
    protected BaseAdapter mBaseAdapter;
    protected int mOpenPosition = -1;
    protected Set<Integer> mOpenPositions = new HashSet();
    protected Adapter mRecyclerAdapter;
    protected Set<SwipeLayout> mShownLayouts = new HashSet();
    /* access modifiers changed from: private */
    public Mode mode = Mode.Single;

    class OnLayoutListener implements OnLayout {
        private int position;

        OnLayoutListener(int i) {
            this.position = i;
        }

        public void setPosition(int i) {
            this.position = i;
        }

        public void onLayout(SwipeLayout swipeLayout) {
            if (SwipeItemMangerImpl.this.isOpen(this.position)) {
                swipeLayout.open(false, false);
            } else {
                swipeLayout.close(false, false);
            }
        }
    }

    class SwipeMemory extends SimpleSwipeListener {
        private int position;

        SwipeMemory(int i) {
            this.position = i;
        }

        public void onClose(SwipeLayout swipeLayout) {
            if (SwipeItemMangerImpl.this.mode == Mode.Multiple) {
                SwipeItemMangerImpl.this.mOpenPositions.remove(Integer.valueOf(this.position));
            } else {
                SwipeItemMangerImpl.this.mOpenPosition = -1;
            }
        }

        public void onStartOpen(SwipeLayout swipeLayout) {
            if (SwipeItemMangerImpl.this.mode == Mode.Single) {
                SwipeItemMangerImpl.this.closeAllExcept(swipeLayout);
            }
        }

        public void onOpen(SwipeLayout swipeLayout) {
            if (SwipeItemMangerImpl.this.mode == Mode.Multiple) {
                SwipeItemMangerImpl.this.mOpenPositions.add(Integer.valueOf(this.position));
                return;
            }
            SwipeItemMangerImpl.this.closeAllExcept(swipeLayout);
            SwipeItemMangerImpl.this.mOpenPosition = this.position;
        }

        public void setPosition(int i) {
            this.position = i;
        }
    }

    class ValueBox {
        OnLayoutListener onLayoutListener;
        int position;
        SwipeMemory swipeMemory;

        ValueBox(int i, SwipeMemory swipeMemory2, OnLayoutListener onLayoutListener2) {
            this.swipeMemory = swipeMemory2;
            this.onLayoutListener = onLayoutListener2;
            this.position = i;
        }
    }

    public abstract void bindView(View view, int i);

    public abstract void initialize(View view, int i);

    public abstract void updateConvertView(View view, int i);

    public SwipeItemMangerImpl(BaseAdapter baseAdapter) {
        if (baseAdapter == null) {
            throw new IllegalArgumentException("Adapter can not be null");
        } else if (baseAdapter instanceof SwipeItemMangerInterface) {
            this.mBaseAdapter = baseAdapter;
        } else {
            throw new IllegalArgumentException("adapter should implement the SwipeAdapterInterface");
        }
    }

    public SwipeItemMangerImpl(Adapter adapter) {
        if (adapter == null) {
            throw new IllegalArgumentException("Adapter can not be null");
        } else if (adapter instanceof SwipeItemMangerInterface) {
            this.mRecyclerAdapter = adapter;
        } else {
            throw new IllegalArgumentException("adapter should implement the SwipeAdapterInterface");
        }
    }

    public Mode getMode() {
        return this.mode;
    }

    public void setMode(Mode mode2) {
        this.mode = mode2;
        this.mOpenPositions.clear();
        this.mShownLayouts.clear();
        this.mOpenPosition = -1;
    }

    public int getSwipeLayoutId(int i) {
        BaseAdapter baseAdapter = this.mBaseAdapter;
        if (baseAdapter != null) {
            return ((SwipeAdapterInterface) baseAdapter).getSwipeLayoutResourceId(i);
        }
        Adapter adapter = this.mRecyclerAdapter;
        if (adapter != null) {
            return ((SwipeAdapterInterface) adapter).getSwipeLayoutResourceId(i);
        }
        return -1;
    }

    public void openItem(int i) {
        if (this.mode != Mode.Multiple) {
            this.mOpenPosition = i;
        } else if (!this.mOpenPositions.contains(Integer.valueOf(i))) {
            this.mOpenPositions.add(Integer.valueOf(i));
        }
        BaseAdapter baseAdapter = this.mBaseAdapter;
        if (baseAdapter != null) {
            baseAdapter.notifyDataSetChanged();
            return;
        }
        Adapter adapter = this.mRecyclerAdapter;
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void closeItem(int i) {
        if (this.mode == Mode.Multiple) {
            this.mOpenPositions.remove(Integer.valueOf(i));
        } else if (this.mOpenPosition == i) {
            this.mOpenPosition = -1;
        }
        BaseAdapter baseAdapter = this.mBaseAdapter;
        if (baseAdapter != null) {
            baseAdapter.notifyDataSetChanged();
            return;
        }
        Adapter adapter = this.mRecyclerAdapter;
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void closeAllExcept(SwipeLayout swipeLayout) {
        for (SwipeLayout swipeLayout2 : this.mShownLayouts) {
            if (swipeLayout2 != swipeLayout) {
                swipeLayout2.close();
            }
        }
    }

    public void closeAllItems() {
        if (this.mode == Mode.Multiple) {
            this.mOpenPositions.clear();
        } else {
            this.mOpenPosition = -1;
        }
        for (SwipeLayout close : this.mShownLayouts) {
            close.close();
        }
    }

    public void removeShownLayouts(SwipeLayout swipeLayout) {
        this.mShownLayouts.remove(swipeLayout);
    }

    public List<Integer> getOpenItems() {
        if (this.mode == Mode.Multiple) {
            return new ArrayList(this.mOpenPositions);
        }
        return Arrays.asList(new Integer[]{Integer.valueOf(this.mOpenPosition)});
    }

    public List<SwipeLayout> getOpenLayouts() {
        return new ArrayList(this.mShownLayouts);
    }

    public boolean isOpen(int i) {
        if (this.mode == Mode.Multiple) {
            return this.mOpenPositions.contains(Integer.valueOf(i));
        }
        return this.mOpenPosition == i;
    }
}
