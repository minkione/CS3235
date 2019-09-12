package com.masterlock.ble.app.view.lock;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import butterknife.ButterKnife.Finder;
import com.akta.android.p004ui.floatinglabeledittext.FloatingLabelEditText;
import com.masterlock.ble.app.C1075R;

public class AddLockView$$ViewInjector {
    public static void inject(Finder finder, final AddLockView addLockView, Object obj) {
        addLockView.activationCode = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.floating_label_activation_code, "field 'activationCode'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.btn_submit, "field 'submitButton' and method 'submit'");
        addLockView.submitButton = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                addLockView.submit();
            }
        });
        finder.findRequiredView(obj, C1075R.C1077id.btn_no_activation, "method 'goToAddMechanicalLocks'").setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                addLockView.goToAddMechanicalLocks();
            }
        });
    }

    public static void reset(AddLockView addLockView) {
        addLockView.activationCode = null;
        addLockView.submitButton = null;
    }
}
