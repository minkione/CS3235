package com.masterlock.ble.app.view.lock;

import android.support.p000v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class LockListView$$ViewInjector {
    public static void inject(Finder finder, final LockListView lockListView, Object obj) {
        lockListView.mLockerModeBannerContainer = finder.findRequiredView(obj, C1075R.C1077id.locker_mode_banner_container, "field 'mLockerModeBannerContainer'");
        lockListView.mLockerModeBanner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_locker_mode_banner, "field 'mLockerModeBanner'");
        lockListView.mEmptyLayout = finder.findRequiredView(obj, C1075R.C1077id.empty_layout, "field 'mEmptyLayout'");
        lockListView.mEmptyView = (TextView) finder.findRequiredView(obj, C1075R.C1077id.empty, "field 'mEmptyView'");
        lockListView.mEmptyImageView = (ImageView) finder.findRequiredView(obj, C1075R.C1077id.empty_image, "field 'mEmptyImageView'");
        lockListView.mEmptyBody = (TextView) finder.findRequiredView(obj, C1075R.C1077id.empty_body, "field 'mEmptyBody'");
        lockListView.mListView = (ListView) finder.findRequiredView(obj, C1075R.C1077id.list, "field 'mListView'");
        lockListView.mSwipeRefreshLayout = (SwipeRefreshLayout) finder.findRequiredView(obj, C1075R.C1077id.swipe_container, "field 'mSwipeRefreshLayout'");
        lockListView.mEmptySwipeRefreshLayout = (SwipeRefreshLayout) finder.findRequiredView(obj, C1075R.C1077id.empty_swipe_container, "field 'mEmptySwipeRefreshLayout'");
        View findOptionalView = finder.findOptionalView(obj, C1075R.C1077id.locker_mode_banner);
        if (findOptionalView != null) {
            findOptionalView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    lockListView.lockerModeBannerClicked();
                }
            });
        }
    }

    public static void reset(LockListView lockListView) {
        lockListView.mLockerModeBannerContainer = null;
        lockListView.mLockerModeBanner = null;
        lockListView.mEmptyLayout = null;
        lockListView.mEmptyView = null;
        lockListView.mEmptyImageView = null;
        lockListView.mEmptyBody = null;
        lockListView.mListView = null;
        lockListView.mSwipeRefreshLayout = null;
        lockListView.mEmptySwipeRefreshLayout = null;
    }
}
