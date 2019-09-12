package com.masterlock.ble.app.view.modal;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class LockerModeDialogPadLock$$ViewInjector {
    public static void inject(Finder finder, LockerModeDialogPadLock lockerModeDialogPadLock, Object obj) {
        lockerModeDialogPadLock.mPrimaryCodeContainer = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.primary_code_container, "field 'mPrimaryCodeContainer'");
        lockerModeDialogPadLock.mTitle = (TextView) finder.findRequiredView(obj, C1075R.C1077id.title, "field 'mTitle'");
        lockerModeDialogPadLock.mBody1 = (TextView) finder.findRequiredView(obj, C1075R.C1077id.body_1, "field 'mBody1'");
        lockerModeDialogPadLock.mBody2 = (TextView) finder.findRequiredView(obj, C1075R.C1077id.body_2, "field 'mBody2'");
        lockerModeDialogPadLock.positiveButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.positive_button, "field 'positiveButton'");
    }

    public static void reset(LockerModeDialogPadLock lockerModeDialogPadLock) {
        lockerModeDialogPadLock.mPrimaryCodeContainer = null;
        lockerModeDialogPadLock.mTitle = null;
        lockerModeDialogPadLock.mBody1 = null;
        lockerModeDialogPadLock.mBody2 = null;
        lockerModeDialogPadLock.positiveButton = null;
    }
}
