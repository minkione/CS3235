package com.masterlock.ble.app.view.nav.settings;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class ChangePhoneNumberView$$ViewInjector {
    public static void inject(Finder finder, final ChangePhoneNumberView changePhoneNumberView, Object obj) {
        changePhoneNumberView.mCurrentPhoneNumber = (TextView) finder.findRequiredView(obj, C1075R.C1077id.current_phone_number, "field 'mCurrentPhoneNumber'");
        changePhoneNumberView.mCurrentPhoneContainer = (RelativeLayout) finder.findRequiredView(obj, C1075R.C1077id.ll_current_phone_container, "field 'mCurrentPhoneContainer'");
        changePhoneNumberView.mCountryCode = (TextView) finder.findRequiredView(obj, C1075R.C1077id.country_code, "field 'mCountryCode'");
        changePhoneNumberView.mPhoneNumber = (EditText) finder.findRequiredView(obj, C1075R.C1077id.phone_number, "field 'mPhoneNumber'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.change_country_code, "field 'mChangeCountryCode' and method 'openModal'");
        changePhoneNumberView.mChangeCountryCode = (TextView) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                changePhoneNumberView.openModal();
            }
        });
        View findRequiredView2 = finder.findRequiredView(obj, C1075R.C1077id.continue_button, "field 'mContinueBtn' and method 'onContinueClicked'");
        changePhoneNumberView.mContinueBtn = (Button) findRequiredView2;
        findRequiredView2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                changePhoneNumberView.onContinueClicked();
            }
        });
        View findRequiredView3 = finder.findRequiredView(obj, C1075R.C1077id.remove_phone_number, "field 'mRemovePhoneNumber' and method 'onRemovePhoneNumberClicked'");
        changePhoneNumberView.mRemovePhoneNumber = (TextView) findRequiredView3;
        findRequiredView3.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                changePhoneNumberView.onRemovePhoneNumberClicked();
            }
        });
    }

    public static void reset(ChangePhoneNumberView changePhoneNumberView) {
        changePhoneNumberView.mCurrentPhoneNumber = null;
        changePhoneNumberView.mCurrentPhoneContainer = null;
        changePhoneNumberView.mCountryCode = null;
        changePhoneNumberView.mPhoneNumber = null;
        changePhoneNumberView.mChangeCountryCode = null;
        changePhoneNumberView.mContinueBtn = null;
        changePhoneNumberView.mRemovePhoneNumber = null;
    }
}
