package com.masterlock.ble.app.view.lock;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import butterknife.ButterKnife.Finder;
import com.akta.android.p004ui.floatinglabeledittext.FloatingLabelEditText;
import com.masterlock.ble.app.C1075R;

public class AddMechanicalLockView$$ViewInjector {
    public static void inject(Finder finder, final AddMechanicalLockView addMechanicalLockView, Object obj) {
        addMechanicalLockView.mLockNameET = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.floating_label_lock_name, "field 'mLockNameET'");
        addMechanicalLockView.mLockCombinationET = (FloatingLabelEditText) finder.findRequiredView(obj, C1075R.C1077id.floating_label_lock_combination, "field 'mLockCombinationET'");
        View findRequiredView = finder.findRequiredView(obj, C1075R.C1077id.btn_submit, "field 'mSubmitButton' and method 'submit'");
        addMechanicalLockView.mSubmitButton = (Button) findRequiredView;
        findRequiredView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                addMechanicalLockView.submit();
            }
        });
    }

    public static void reset(AddMechanicalLockView addMechanicalLockView) {
        addMechanicalLockView.mLockNameET = null;
        addMechanicalLockView.mLockCombinationET = null;
        addMechanicalLockView.mSubmitButton = null;
    }
}
