package com.masterlock.ble.app.view.modal;

import butterknife.ButterKnife.Finder;
import com.akta.android.p004ui.floatinglabeledittext.FloatingLabelEditText;
import com.masterlock.ble.app.C1075R;
import p007fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class ForgotPasscodeDialog$$ViewInjector {
    public static void inject(Finder finder, ForgotPasscodeDialog forgotPasscodeDialog, Object obj) {
        SimpleDialog$$ViewInjector.inject(finder, forgotPasscodeDialog, obj);
        forgotPasscodeDialog.loadingProgressBar = (SmoothProgressBar) finder.findRequiredView(obj, C1075R.C1077id.loading, "field 'loadingProgressBar'");
        forgotPasscodeDialog.forgotPasscodeEditText = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.passcode_flet, "field 'forgotPasscodeEditText'");
    }

    public static void reset(ForgotPasscodeDialog forgotPasscodeDialog) {
        SimpleDialog$$ViewInjector.reset(forgotPasscodeDialog);
        forgotPasscodeDialog.loadingProgressBar = null;
        forgotPasscodeDialog.forgotPasscodeEditText = null;
    }
}
