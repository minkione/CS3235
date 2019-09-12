package com.masterlock.ble.app.view.modal;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class LockerModeDialogKeySafe$$ViewInjector {
    public static void inject(Finder finder, LockerModeDialogKeySafe lockerModeDialogKeySafe, Object obj) {
        lockerModeDialogKeySafe.mPrimaryCodeContainer = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.primary_code_container, "field 'mPrimaryCodeContainer'");
        lockerModeDialogKeySafe.mTitle = (TextView) finder.findRequiredView(obj, C1075R.C1077id.title, "field 'mTitle'");
        lockerModeDialogKeySafe.mBody1 = (TextView) finder.findRequiredView(obj, C1075R.C1077id.body_1, "field 'mBody1'");
        lockerModeDialogKeySafe.mBody2 = (TextView) finder.findRequiredView(obj, C1075R.C1077id.body_2, "field 'mBody2'");
        lockerModeDialogKeySafe.positiveButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.positive_button, "field 'positiveButton'");
    }

    public static void reset(LockerModeDialogKeySafe lockerModeDialogKeySafe) {
        lockerModeDialogKeySafe.mPrimaryCodeContainer = null;
        lockerModeDialogKeySafe.mTitle = null;
        lockerModeDialogKeySafe.mBody1 = null;
        lockerModeDialogKeySafe.mBody2 = null;
        lockerModeDialogKeySafe.positiveButton = null;
    }
}
