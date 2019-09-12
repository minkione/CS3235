package com.masterlock.ble.app.view.modal;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class DeleteInvitationDialog$$ViewInjector {
    public static void inject(Finder finder, DeleteInvitationDialog deleteInvitationDialog, Object obj) {
        deleteInvitationDialog.mBody = (TextView) finder.findRequiredView(obj, C1075R.C1077id.body, "field 'mBody'");
        deleteInvitationDialog.mBodyContainer = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.body_container, "field 'mBodyContainer'");
        deleteInvitationDialog.mRevokeAccessButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.revoke_access, "field 'mRevokeAccessButton'");
        deleteInvitationDialog.mDeleteGuestButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.delete_guest, "field 'mDeleteGuestButton'");
        deleteInvitationDialog.mCancelButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.cancel, "field 'mCancelButton'");
    }

    public static void reset(DeleteInvitationDialog deleteInvitationDialog) {
        deleteInvitationDialog.mBody = null;
        deleteInvitationDialog.mBodyContainer = null;
        deleteInvitationDialog.mRevokeAccessButton = null;
        deleteInvitationDialog.mDeleteGuestButton = null;
        deleteInvitationDialog.mCancelButton = null;
    }
}
