package com.masterlock.ble.app.view.lock;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ViewFlipper;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class ApplyChangesView$$ViewInjector {
    public static void inject(Finder finder, final ApplyChangesView applyChangesView, Object obj) {
        applyChangesView.mContentFlipper = (ViewFlipper) finder.findRequiredView(obj, C1075R.C1077id.content_flipper, "field 'mContentFlipper'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.primary_action_button, "field 'mPrimaryActionButton'");
        applyChangesView.mPrimaryActionButton = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                applyChangesView.onPrimaryClick();
            }
        });
        View findRequiredView2 = finder.findRequiredView(obj, C1075R.C1077id.secondary_action_button, "field 'mSecondaryActionButton'");
        applyChangesView.mSecondaryActionButton = (Button) findRequiredView2;
        findRequiredView2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                applyChangesView.onSecondaryClick();
            }
        });
        applyChangesView.mButtonBar = finder.findRequiredView(obj, C1075R.C1077id.button_bar, "field 'mButtonBar'");
    }

    public static void reset(ApplyChangesView applyChangesView) {
        applyChangesView.mContentFlipper = null;
        applyChangesView.mPrimaryActionButton = null;
        applyChangesView.mSecondaryActionButton = null;
        applyChangesView.mButtonBar = null;
    }
}
