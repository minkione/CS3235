package com.masterlock.ble.app.view.lock.padlock;

import android.content.Context;
import android.support.p000v4.widget.SwipeRefreshLayout;
import android.support.p000v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.adapters.HistoryAdapter;
import com.masterlock.ble.app.presenter.lock.padlock.HistoryPadLockPresenter;
import com.masterlock.ble.app.screens.LockScreens.HistoryPadLock;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;

public class HistoryPadLockView extends LinearLayout implements IAuthenticatedView {
    @InjectView(2131296422)
    public TextView deviceId;
    private HistoryAdapter mAdapter;
    @InjectView(2131296457)
    SwipeRefreshLayout mEmptySwipeRefreshLayout;
    @InjectView(2131296453)
    public TextView mEmptyView;
    private HistoryPadLockPresenter mHistoryPadLockPresenter;
    @InjectView(2131296572)
    ListView mListView;
    @InjectView(2131296871)
    public TextView mLockName;
    @InjectView(2131296784)
    SwipeRefreshLayout mSwipeRefreshLayout;

    public HistoryPadLockView(Context context) {
        super(context);
    }

    public HistoryPadLockView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public HistoryPadLockView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void updateView(Lock lock) {
        this.mSwipeRefreshLayout.setRefreshing(false);
        this.mEmptySwipeRefreshLayout.setRefreshing(false);
        this.mAdapter.setNotifyOnChange(false);
        this.mAdapter.clear();
        if (lock == null || lock.getLogs() == null || lock.getLogs().size() <= 0) {
            this.mEmptyView.setText(C1075R.string.no_history);
        } else {
            this.mLockName.setText(lock.getName());
            this.deviceId.setText(lock.getKmsDeviceKey().getDeviceId());
            this.mAdapter.addAll(lock.getLogs());
        }
        this.mAdapter.notifyDataSetChanged();
        this.mAdapter.setNotifyOnChange(false);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            HistoryPadLock historyPadLock = (HistoryPadLock) AppFlow.getScreen(getContext());
            this.mHistoryPadLockPresenter = new HistoryPadLockPresenter(historyPadLock.mLock, this);
            this.mAdapter = new HistoryAdapter(getContext(), this.mHistoryPadLockPresenter.getModel());
            this.mListView.setAdapter(this.mAdapter);
            this.mListView.setEmptyView(this.mEmptySwipeRefreshLayout);
            this.mLockName.setText(historyPadLock.mLock.getName());
            this.deviceId.setText(historyPadLock.mLock.getKmsDeviceKey().getDeviceId());
            setupSwipeRefreshLayouts(this.mSwipeRefreshLayout, this.mEmptySwipeRefreshLayout);
            MasterLockApp.get().inject(this.mHistoryPadLockPresenter);
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
            this.mHistoryPadLockPresenter.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mHistoryPadLockPresenter.finish();
    }

    public void setupSwipeRefreshLayouts(SwipeRefreshLayout... swipeRefreshLayoutArr) {
        for (SwipeRefreshLayout swipeRefreshLayout : swipeRefreshLayoutArr) {
            swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                public final void onRefresh() {
                    HistoryPadLockView.this.mHistoryPadLockPresenter.refresh();
                }
            });
            swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(C1075R.array.progress_bar_colors));
        }
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }
}
