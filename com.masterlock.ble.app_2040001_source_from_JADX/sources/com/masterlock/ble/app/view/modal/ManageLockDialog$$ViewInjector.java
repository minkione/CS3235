package com.masterlock.ble.app.view.modal;

import android.widget.Button;
import butterknife.ButterKnife.Finder;
import com.akta.android.p004ui.floatinglabeledittext.FloatingLabelEditText;
import com.masterlock.ble.app.C1075R;
import p007fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class ManageLockDialog$$ViewInjector {
    public static void inject(Finder finder, ManageLockDialog manageLockDialog, Object obj) {
        manageLockDialog.loadingProgressBar = (SmoothProgressBar) finder.findRequiredView(obj, C1075R.C1077id.loading, "field 'loadingProgressBar'");
        manageLockDialog.passcodeFloatingLabelEditText = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.passcode, "field 'passcodeFloatingLabelEditText'");
        manageLockDialog.positiveButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.positive_button, "field 'positiveButton'");
        manageLockDialog.negativeButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.negative_button, "field 'negativeButton'");
        manageLockDialog.forgotPasscodeButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.forgot_passcode_button, "field 'forgotPasscodeButton'");
    }

    public static void reset(ManageLockDialog manageLockDialog) {
        manageLockDialog.loadingProgressBar = null;
        manageLockDialog.passcodeFloatingLabelEditText = null;
        manageLockDialog.positiveButton = null;
        manageLockDialog.negativeButton = null;
        manageLockDialog.forgotPasscodeButton = null;
    }
}
