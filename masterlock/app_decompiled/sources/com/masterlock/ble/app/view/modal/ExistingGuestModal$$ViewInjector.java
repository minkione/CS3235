package com.masterlock.ble.app.view.modal;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class ExistingGuestModal$$ViewInjector {
    public static void inject(Finder finder, ExistingGuestModal existingGuestModal, Object obj) {
        existingGuestModal.mTitle = (TextView) finder.findRequiredView(obj, C1075R.C1077id.title, "field 'mTitle'");
        existingGuestModal.mBody = (TextView) finder.findRequiredView(obj, C1075R.C1077id.body, "field 'mBody'");
        existingGuestModal.mBodyContainer = (LinearLayout) finder.findRequiredView(obj, C1075R.C1077id.body_container, "field 'mBodyContainer'");
        existingGuestModal.positiveButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.positive_button, "field 'positiveButton'");
        existingGuestModal.negativeButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.negative_button, "field 'negativeButton'");
        existingGuestModal.rBGuest1 = (RadioButton) finder.findRequiredView(obj, C1075R.C1077id.rb_guest_1, "field 'rBGuest1'");
        existingGuestModal.rBGuest2 = (RadioButton) finder.findRequiredView(obj, C1075R.C1077id.rb_guest_2, "field 'rBGuest2'");
        existingGuestModal.rGGuests = (RadioGroup) finder.findRequiredView(obj, C1075R.C1077id.rg_guests, "field 'rGGuests'");
    }

    public static void reset(ExistingGuestModal existingGuestModal) {
        existingGuestModal.mTitle = null;
        existingGuestModal.mBody = null;
        existingGuestModal.mBodyContainer = null;
        existingGuestModal.positiveButton = null;
        existingGuestModal.negativeButton = null;
        existingGuestModal.rBGuest1 = null;
        existingGuestModal.rBGuest2 = null;
        existingGuestModal.rGGuests = null;
    }
}
