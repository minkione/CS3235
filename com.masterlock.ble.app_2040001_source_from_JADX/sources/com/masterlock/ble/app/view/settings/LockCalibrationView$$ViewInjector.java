package com.masterlock.ble.app.view.settings;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class LockCalibrationView$$ViewInjector {
    public static void inject(Finder finder, final LockCalibrationView lockCalibrationView, Object obj) {
        lockCalibrationView.mLockNameBanner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_name_banner, "field 'mLockNameBanner'");
        lockCalibrationView.deviceIdTxt = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id_banner, "field 'deviceIdTxt'");
        lockCalibrationView.mContentFlipper = (ViewFlipper) finder.findRequiredView(obj, C1075R.C1077id.content_flipper, "field 'mContentFlipper'");
        lockCalibrationView.mTopButtonBar = finder.findRequiredView(obj, C1075R.C1077id.top_button_bar, "field 'mTopButtonBar'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.top_action_button, "field 'mTopActionButton' and method 'onActionClicked'");
        lockCalibrationView.mTopActionButton = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                lockCalibrationView.onActionClicked(view);
            }
        });
        View findRequiredView2 = finder.findRequiredView(obj, C1075R.C1077id.primary_action_button, "field 'mPrimaryActionButton' and method 'onActionClicked'");
        lockCalibrationView.mPrimaryActionButton = (Button) findRequiredView2;
        findRequiredView2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                lockCalibrationView.onActionClicked(view);
            }
        });
        View findRequiredView3 = finder.findRequiredView(obj, C1075R.C1077id.secondary_action_button, "field 'mSecondaryActionButton' and method 'onActionClicked'");
        lockCalibrationView.mSecondaryActionButton = (Button) findRequiredView3;
        findRequiredView3.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                lockCalibrationView.onActionClicked(view);
            }
        });
        lockCalibrationView.mButtonBar = finder.findRequiredView(obj, C1075R.C1077id.button_bar, "field 'mButtonBar'");
    }

    public static void reset(LockCalibrationView lockCalibrationView) {
        lockCalibrationView.mLockNameBanner = null;
        lockCalibrationView.deviceIdTxt = null;
        lockCalibrationView.mContentFlipper = null;
        lockCalibrationView.mTopButtonBar = null;
        lockCalibrationView.mTopActionButton = null;
        lockCalibrationView.mPrimaryActionButton = null;
        lockCalibrationView.mSecondaryActionButton = null;
        lockCalibrationView.mButtonBar = null;
    }
}
