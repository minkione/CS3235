package com.masterlock.ble.app.view.nav.settings;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.ButterKnife.Finder;
import com.masterlock.ble.app.C1075R;

public class ChangePasswordView$$ViewInjector {
    public static void inject(Finder finder, final ChangePasswordView changePasswordView, Object obj) {
        changePasswordView.scrollView = (ScrollView) finder.findRequiredView(obj, C1075R.C1077id.scrollview, "field 'scrollView'");
        changePasswordView.eTCurrentPassword = (EditText) finder.findRequiredView(obj, C1075R.C1077id.et_current_password, "field 'eTCurrentPassword'");
        changePasswordView.etConfirmPassword = (EditText) finder.findRequiredView(obj, C1075R.C1077id.et_confirm_password, "field 'etConfirmPassword'");
        changePasswordView.etNewPassword = (EditText) finder.findRequiredView(obj, C1075R.C1077id.et_new_password, "field 'etNewPassword'");
        changePasswordView.passwordStrengthContainer = finder.findRequiredView(obj, C1075R.C1077id.rl_password_strength_container, "field 'passwordStrengthContainer'");
        changePasswordView.tVPasswordStrength = (TextView) finder.findRequiredView(obj, C1075R.C1077id.txt_password_strength, "field 'tVPasswordStrength'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.bt_save, "field 'bTSave' and method 'onContinueClick'");
        changePasswordView.bTSave = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                changePasswordView.onContinueClick(view);
            }
        });
        ((CompoundButton) finder.findRequiredView(obj, C1075R.C1077id.cb_password, "method 'onCheckedChange'")).setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                changePasswordView.onCheckedChange(z);
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.iv_password_tips_info, "method 'onClick'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                changePasswordView.onClick();
            }
        });
    }

    public static void reset(ChangePasswordView changePasswordView) {
        changePasswordView.scrollView = null;
        changePasswordView.eTCurrentPassword = null;
        changePasswordView.etConfirmPassword = null;
        changePasswordView.etNewPassword = null;
        changePasswordView.passwordStrengthContainer = null;
        changePasswordView.tVPasswordStrength = null;
        changePasswordView.bTSave = null;
    }
}
