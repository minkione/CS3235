package com.masterlock.ble.app.view.locklanding;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.util.LandingScrollView;
import p007fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class LockLandingView$$ViewInjector {
    public static void inject(Finder finder, final LockLandingView lockLandingView, Object obj) {
        lockLandingView.loadingProgressBar = (SmoothProgressBar) finder.findRequiredView(obj, C1075R.C1077id.loading, "field 'loadingProgressBar'");
        lockLandingView.footerStub = (ViewFlipper) finder.findRequiredView(obj, C1075R.C1077id.footer_stub, "field 'footerStub'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.locker_mode_toggle, "field 'lockerModeToggle' and method 'toggleLockerMode'");
        lockLandingView.lockerModeToggle = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                lockLandingView.toggleLockerMode();
            }
        });
        lockLandingView.lockerModeApplied = (TextView) finder.findRequiredView(obj, C1075R.C1077id.locker_mode_applied, "field 'lockerModeApplied'");
        lockLandingView.buttonBar = finder.findRequiredView(obj, C1075R.C1077id.button_bar, "field 'buttonBar'");
        lockLandingView.stateTitle = (TextView) finder.findRequiredView(obj, C1075R.C1077id.state_title, "field 'stateTitle'");
        lockLandingView.stateBody = (TextView) finder.findRequiredView(obj, C1075R.C1077id.state_body, "field 'stateBody'");
        lockLandingView.lockIcon = (ImageSwitcher) finder.findRequiredView(obj, C1075R.C1077id.lock_icon, "field 'lockIcon'");
        lockLandingView.lockArrowLarge = (ImageView) finder.findRequiredView(obj, C1075R.C1077id.lock_arrow_large, "field 'lockArrowLarge'");
        lockLandingView.lockArrowMedium = (ImageView) finder.findRequiredView(obj, C1075R.C1077id.lock_arrow_medium, "field 'lockArrowMedium'");
        lockLandingView.lockArrowSmall = (ImageView) finder.findRequiredView(obj, C1075R.C1077id.lock_arrow_small, "field 'lockArrowSmall'");
        lockLandingView.lockName = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_name, "field 'lockName'");
        lockLandingView.auditDetail = (TextView) finder.findRequiredView(obj, C1075R.C1077id.audit_detail, "field 'auditDetail'");
        lockLandingView.batteryPercentage = (TextView) finder.findRequiredView(obj, C1075R.C1077id.battery_percentage, "field 'batteryPercentage'");
        lockLandingView.batteryIndicator = (ImageView) finder.findRequiredView(obj, C1075R.C1077id.battery_indicator, "field 'batteryIndicator'");
        lockLandingView.relockTime = (TextView) finder.findRequiredView(obj, C1075R.C1077id.relock_time, "field 'relockTime'");
        lockLandingView.landingScroll = (LandingScrollView) finder.findRequiredView(obj, C1075R.C1077id.landing_scroll, "field 'landingScroll'");
        lockLandingView.deviceId = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id, "field 'deviceId'");
    }

    public static void reset(LockLandingView lockLandingView) {
        lockLandingView.loadingProgressBar = null;
        lockLandingView.footerStub = null;
        lockLandingView.lockerModeToggle = null;
        lockLandingView.lockerModeApplied = null;
        lockLandingView.buttonBar = null;
        lockLandingView.stateTitle = null;
        lockLandingView.stateBody = null;
        lockLandingView.lockIcon = null;
        lockLandingView.lockArrowLarge = null;
        lockLandingView.lockArrowMedium = null;
        lockLandingView.lockArrowSmall = null;
        lockLandingView.lockName = null;
        lockLandingView.auditDetail = null;
        lockLandingView.batteryPercentage = null;
        lockLandingView.batteryIndicator = null;
        lockLandingView.relockTime = null;
        lockLandingView.landingScroll = null;
        lockLandingView.deviceId = null;
    }
}
