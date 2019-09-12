package com.masterlock.ble.app.view.lock;

import android.content.Context;
import android.support.p000v4.widget.SwipeRefreshLayout;
import android.support.p000v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.adapters.LockAdapter;
import com.masterlock.ble.app.presenter.lock.LockListPresenter;
import com.masterlock.ble.app.screens.LockScreens.LockList;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;
import java.util.Collection;
import java.util.List;

public class LockListView extends LinearLayout {
    String mAction;
    @InjectView(2131296454)
    public TextView mEmptyBody;
    @InjectView(2131296455)
    public ImageView mEmptyImageView;
    @InjectView(2131296456)
    public View mEmptyLayout;
    @InjectView(2131296457)
    SwipeRefreshLayout mEmptySwipeRefreshLayout;
    @InjectView(2131296453)
    public TextView mEmptyView;
    @InjectView(2131296572)
    ListView mListView;
    private LockAdapter mLockAdapter;
    String mLockId;
    private LockListPresenter mLockListPresenter;
    @InjectView(2131296873)
    public TextView mLockerModeBanner;
    @InjectView(2131296607)
    public View mLockerModeBannerContainer;
    @InjectView(2131296784)
    SwipeRefreshLayout mSwipeRefreshLayout;

    public LockListView(Context context) {
        super(context);
    }

    public LockListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public LockListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void updateItems(List<Lock> list) {
        this.mSwipeRefreshLayout.setRefreshing(false);
        this.mSwipeRefreshLayout.setEnabled(true);
        this.mEmptySwipeRefreshLayout.setRefreshing(false);
        this.mEmptySwipeRefreshLayout.setEnabled(true);
        this.mLockAdapter.setNotifyOnChange(false);
        this.mLockAdapter.clear();
        this.mLockAdapter.clearItems();
        if (list == null || list.size() <= 0) {
            this.mEmptyLayout.setVisibility(0);
        } else {
            this.mEmptyLayout.setVisibility(8);
            this.mLockAdapter.addAll((Collection<? extends Lock>) list);
        }
        setupLockerModeBanner(list);
        this.mLockAdapter.notifyDataSetChanged();
        this.mLockAdapter.setNotifyOnChange(true);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            LockList lockList = (LockList) AppFlow.getScreen(getContext());
            this.mAction = lockList.mAction;
            this.mLockId = lockList.mLockId;
            lockList.mAction = null;
            lockList.mLockId = null;
            this.mLockAdapter = new LockAdapter(getContext());
            this.mListView.setAdapter(this.mLockAdapter);
            this.mListView.setEmptyView(this.mEmptySwipeRefreshLayout);
            this.mLockListPresenter = new LockListPresenter(this);
            setupSwipeRefreshLayouts(this.mSwipeRefreshLayout, this.mEmptySwipeRefreshLayout);
            MasterLockApp.get().inject(this.mLockListPresenter);
            this.mLockListPresenter.refresh();
            this.mLockListPresenter.forceRescan();
        }
    }

    public void displayError(Throwable th) {
        this.mSwipeRefreshLayout.setRefreshing(false);
        this.mEmptySwipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getContext(), th.getMessage() != null ? th.getMessage() : th.getClass().getSimpleName(), 0).show();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mLockListPresenter.start();
            this.mLockListPresenter.checkAction(this.mAction, this.mLockId);
            MasterLockSharedPreferences.getInstance().putLockLandingId("");
            MasterLockSharedPreferences.getInstance().putLockListVisible(true);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        MasterLockSharedPreferences.getInstance().putLockListVisible(false);
        this.mLockListPresenter.finish();
        this.mLockAdapter.cancelAll();
    }

    public void setupSwipeRefreshLayouts(SwipeRefreshLayout... swipeRefreshLayoutArr) {
        for (SwipeRefreshLayout swipeRefreshLayout : swipeRefreshLayoutArr) {
            swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener(swipeRefreshLayout) {
                private final /* synthetic */ SwipeRefreshLayout f$1;

                {
                    this.f$1 = r2;
                }

                public final void onRefresh() {
                    LockListView.lambda$setupSwipeRefreshLayouts$0(LockListView.this, this.f$1);
                }
            });
            swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(C1075R.array.progress_bar_colors));
        }
    }

    public static /* synthetic */ void lambda$setupSwipeRefreshLayouts$0(LockListView lockListView, SwipeRefreshLayout swipeRefreshLayout) {
        lockListView.mLockListPresenter.reload();
        swipeRefreshLayout.setEnabled(false);
    }

    public void setupLockerModeBanner(List<Lock> list) {
        int i = 0;
        for (Lock isLockerMode : list) {
            if (isLockerMode.isLockerMode()) {
                i++;
            }
        }
        if (i > 0) {
            this.mLockerModeBanner.setText(getResources().getString(C1075R.string.locker_mode_on));
            this.mLockerModeBannerContainer.setVisibility(0);
            return;
        }
        this.mLockerModeBannerContainer.setVisibility(8);
    }

    @OnClick({2131296606})
    @Optional
    public void lockerModeBannerClicked() {
        this.mLockListPresenter.removeLockerMode();
    }

    public String getLockId() {
        return this.mLockId;
    }
}
