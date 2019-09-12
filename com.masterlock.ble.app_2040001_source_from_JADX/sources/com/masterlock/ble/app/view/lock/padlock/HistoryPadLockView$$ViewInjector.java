package com.masterlock.ble.app.view.lock.padlock;

import android.support.p000v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class HistoryPadLockView$$ViewInjector {
    public static void inject(Finder finder, HistoryPadLockView historyPadLockView, Object obj) {
        historyPadLockView.mLockName = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_lock_name, "field 'mLockName'");
        historyPadLockView.deviceId = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id_banner, "field 'deviceId'");
        historyPadLockView.mEmptyView = (TextView) finder.findRequiredView(obj, C1075R.C1077id.empty, "field 'mEmptyView'");
        historyPadLockView.mListView = (ListView) finder.findRequiredView(obj, C1075R.C1077id.list, "field 'mListView'");
        historyPadLockView.mSwipeRefreshLayout = (SwipeRefreshLayout) finder.findRequiredView(obj, C1075R.C1077id.swipe_container, "field 'mSwipeRefreshLayout'");
        historyPadLockView.mEmptySwipeRefreshLayout = (SwipeRefreshLayout) finder.findRequiredView(obj, C1075R.C1077id.empty_swipe_container, "field 'mEmptySwipeRefreshLayout'");
    }

    public static void reset(HistoryPadLockView historyPadLockView) {
        historyPadLockView.mLockName = null;
        historyPadLockView.deviceId = null;
        historyPadLockView.mEmptyView = null;
        historyPadLockView.mListView = null;
        historyPadLockView.mSwipeRefreshLayout = null;
        historyPadLockView.mEmptySwipeRefreshLayout = null;
    }
}
