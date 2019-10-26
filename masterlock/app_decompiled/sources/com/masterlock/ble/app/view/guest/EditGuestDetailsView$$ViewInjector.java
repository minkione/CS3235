package com.masterlock.ble.app.view.guest;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.akta.android.p004ui.floatinglabeledittext.FloatingLabelEditText;
import com.masterlock.ble.app.C1075R;

public class EditGuestDetailsView$$ViewInjector {
    public static void inject(Finder finder, final EditGuestDetailsView editGuestDetailsView, Object obj) {
        editGuestDetailsView.guestFirstName = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.guest_first_name, "field 'guestFirstName'");
        editGuestDetailsView.guestLastName = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.guest_last_name, "field 'guestLastName'");
        editGuestDetailsView.guestEmail = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.email, "field 'guestEmail'");
        editGuestDetailsView.guestOrganization = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.organization, "field 'guestOrganization'");
        editGuestDetailsView.phoneNumberInput = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.phone, "field 'phoneNumberInput'");
        editGuestDetailsView.countryCodeInput = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.code, "field 'countryCodeInput'");
        editGuestDetailsView.mLockNameBanner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.lock_name_banner, "field 'mLockNameBanner'");
        editGuestDetailsView.mDeviceIdBanner = (TextView) finder.findRequiredView(obj, C1075R.C1077id.device_id_banner, "field 'mDeviceIdBanner'");
        editGuestDetailsView.mInstructions = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_guest_details_instructions, "field 'mInstructions'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.btn_add_guest, "field 'mButtonAddGuest' and method 'addGuest'");
        editGuestDetailsView.mButtonAddGuest = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                editGuestDetailsView.addGuest();
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.change_country_code, "method 'openModal'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                editGuestDetailsView.openModal();
            }
        });
    }

    public static void reset(EditGuestDetailsView editGuestDetailsView) {
        editGuestDetailsView.guestFirstName = null;
        editGuestDetailsView.guestLastName = null;
        editGuestDetailsView.guestEmail = null;
        editGuestDetailsView.guestOrganization = null;
        editGuestDetailsView.phoneNumberInput = null;
        editGuestDetailsView.countryCodeInput = null;
        editGuestDetailsView.mLockNameBanner = null;
        editGuestDetailsView.mDeviceIdBanner = null;
        editGuestDetailsView.mInstructions = null;
        editGuestDetailsView.mButtonAddGuest = null;
    }
}
