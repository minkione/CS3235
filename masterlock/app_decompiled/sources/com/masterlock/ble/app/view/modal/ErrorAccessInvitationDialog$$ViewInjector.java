package com.masterlock.ble.app.view.modal;

import android.widget.Button;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class ErrorAccessInvitationDialog$$ViewInjector {
    public static void inject(Finder finder, ErrorAccessInvitationDialog errorAccessInvitationDialog, Object obj) {
        errorAccessInvitationDialog.okButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.ok_button, "field 'okButton'");
    }

    public static void reset(ErrorAccessInvitationDialog errorAccessInvitationDialog) {
        errorAccessInvitationDialog.okButton = null;
    }
}
