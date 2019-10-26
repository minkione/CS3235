package com.masterlock.ble.app.view.modal;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class AddGuestModal$$ViewInjector {
    public static void inject(Finder finder, AddGuestModal addGuestModal, Object obj) {
        addGuestModal.mBody = (TextView) finder.findRequiredView(obj, C1075R.C1077id.body, "field 'mBody'");
        addGuestModal.mBodyContainer = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.body_container, "field 'mBodyContainer'");
        addGuestModal.existingGuestButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.btn_existing_guest, "field 'existingGuestButton'");
        addGuestModal.addFromContactsButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.btn_add_from_contacts, "field 'addFromContactsButton'");
        addGuestModal.enterManuallyButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.btn_enter_manually, "field 'enterManuallyButton'");
    }

    public static void reset(AddGuestModal addGuestModal) {
        addGuestModal.mBody = null;
        addGuestModal.mBodyContainer = null;
        addGuestModal.existingGuestButton = null;
        addGuestModal.addFromContactsButton = null;
        addGuestModal.enterManuallyButton = null;
    }
}
