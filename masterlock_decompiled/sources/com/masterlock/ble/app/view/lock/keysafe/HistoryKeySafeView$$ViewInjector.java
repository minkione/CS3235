package com.masterlock.ble.app.view.lock.keysafe;

import android.support.p000v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class HistoryKeySafeView$$ViewInjector {
    public static void inject(Finder finder, HistoryKeySafeView historyKeySafeView, Object obj) {
        historyKeySafeView.mLockName = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_lock_name, "field 'mLockName'");
        historyKeySafeView.deviceId = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id_banner, "field 'deviceId'");
        historyKeySafeView.mEmptyView = (TextView) finder.findRequiredView(obj, C1075R.C1077id.empty, "field 'mEmptyView'");
        historyKeySafeView.mListView = (ListView) finder.findRequiredView(obj, C1075R.C1077id.list, "field 'mListView'");
        historyKeySafeView.mSwipeRefreshLayout = (SwipeRefreshLayout) finder.findRequiredView(obj, C1075R.C1077id.swipe_container, "field 'mSwipeRefreshLayout'");
        historyKeySafeView.mEmptySwipeRefreshLayout = (SwipeRefreshLayout) finder.findRequiredView(obj, C1075R.C1077id.empty_swipe_container, "field 'mEmptySwipeRefreshLayout'");
    }

    public static void reset(HistoryKeySafeView historyKeySafeView) {
        historyKeySafeView.mLockName = null;
        historyKeySafeView.deviceId = null;
        historyKeySafeView.mEmptyView = null;
        historyKeySafeView.mListView = null;
        historyKeySafeView.mSwipeRefreshLayout = null;
        historyKeySafeView.mEmptySwipeRefreshLayout = null;
    }
}
