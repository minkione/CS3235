package com.masterlock.ble.app.activity;

import android.support.p000v4.widget.DrawerLayout;
import android.support.p003v7.widget.Toolbar;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.view.NavigationDrawerView;
import com.square.flow.screenswitcher.FrameScreenSwitcherView;
import p007fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class FlowActivity$$ViewInjector {
    public static void inject(Finder finder, FlowActivity flowActivity, Object obj) {
        flowActivity.container = (FrameScreenSwitcherView) finder.findRequiredView(obj, C1075R.C1077id.container, "field 'container'");
        flowActivity.mToolbar = (Toolbar) finder.findOptionalView(obj, C1075R.C1077id.toolbar);
        flowActivity.toolbarContainer = finder.findOptionalView(obj, C1075R.C1077id.toolbar_container);
        flowActivity.progressBar = (SmoothProgressBar) finder.findRequiredView(obj, C1075R.C1077id.progressBar, "field 'progressBar'");
        flowActivity.mDrawerLayout = (DrawerLayout) finder.findOptionalView(obj, C1075R.C1077id.drawer_layout);
        flowActivity.mDrawerView = (NavigationDrawerView) finder.findOptionalView(obj, C1075R.C1077id.drawer);
    }

    public static void reset(FlowActivity flowActivity) {
        flowActivity.container = null;
        flowActivity.mToolbar = null;
        flowActivity.toolbarContainer = null;
        flowActivity.progressBar = null;
        flowActivity.mDrawerLayout = null;
        flowActivity.mDrawerView = null;
    }
}
