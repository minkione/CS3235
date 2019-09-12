package com.masterlock.ble.app.view.signup;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class AccountDetailsView$$ViewInjector {
    public static void inject(Finder finder, final AccountDetailsView accountDetailsView, Object obj) {
        accountDetailsView.scrollView = (ScrollView) finder.findRequiredView(obj, C1075R.C1077id.scrollview, "field 'scrollView'");
        accountDetailsView.firstNameEditText = (EditText) finder.findRequiredView(obj, C1075R.C1077id.first_name_edit_text, "field 'firstNameEditText'");
        accountDetailsView.userNameEditText = (EditText) finder.findRequiredView(obj, C1075R.C1077id.username_edit_text, "field 'userNameEditText'");
        accountDetailsView.lastNameEditText = (EditText) finder.findRequiredView(obj, C1075R.C1077id.last_name_edit_text, "field 'lastNameEditText'");
        accountDetailsView.passwordEditText = (EditText) finder.findRequiredView(obj, C1075R.C1077id.password_edit_text, "field 'passwordEditText'");
        accountDetailsView.passwordStrengthContainer = finder.findRequiredView(obj, C1075R.C1077id.rl_password_strength_container, "field 'passwordStrengthContainer'");
        accountDetailsView.tVPasswordStrength = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_password_strength, "field 'tVPasswordStrength'");
        accountDetailsView.continueButton = (Button) finder.findRequiredView(obj, C1075R.C1077id.continue_button, "field 'continueButton'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.s_password, "field 'passwordSwitch' and method 'onCheckedChange'");
        accountDetailsView.passwordSwitch = (CheckBox) findRequiredView;
        ((CompoundButton) findRequiredView).setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                accountDetailsView.onCheckedChange(z);
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.iv_password_tips_info, "method 'onClick'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                accountDetailsView.onClick();
            }
        });
    }

    public static void reset(AccountDetailsView accountDetailsView) {
        accountDetailsView.scrollView = null;
        accountDetailsView.firstNameEditText = null;
        accountDetailsView.userNameEditText = null;
        accountDetailsView.lastNameEditText = null;
        accountDetailsView.passwordEditText = null;
        accountDetailsView.passwordStrengthContainer = null;
        accountDetailsView.tVPasswordStrength = null;
        accountDetailsView.continueButton = null;
        accountDetailsView.passwordSwitch = null;
    }
}
