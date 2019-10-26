package com.masterlock.ble.app.view.modal;

import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class InvalidInvitationCodeDialog$$ViewInjector {
    public static void inject(Finder finder, InvalidInvitationCodeDialog invalidInvitationCodeDialog, Object obj) {
        invalidInvitationCodeDialog.positiveButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.positive_button, "field 'positiveButton'");
        invalidInvitationCodeDialog.tVTitle = (TextView) finder.findRequiredView(obj, C1075R.C1077id.title, "field 'tVTitle'");
        invalidInvitationCodeDialog.tVBody = (TextView) finder.findRequiredView(obj, C1075R.C1077id.body, "field 'tVBody'");
    }

    public static void reset(InvalidInvitationCodeDialog invalidInvitationCodeDialog) {
        invalidInvitationCodeDialog.positiveButton = null;
        invalidInvitationCodeDialog.tVTitle = null;
        invalidInvitationCodeDialog.tVBody = null;
    }
}
