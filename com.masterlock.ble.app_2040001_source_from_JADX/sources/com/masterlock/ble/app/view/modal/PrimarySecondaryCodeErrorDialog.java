package com.masterlock.ble.app.view.modal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import butterknife.ButterKnife;
import com.masterlock.ble.app.C1075R;

public class PrimarySecondaryCodeErrorDialog extends SimpleDialog {
    public PrimarySecondaryCodeErrorDialog(Context context) {
        this(context, null);
    }

    public PrimarySecondaryCodeErrorDialog(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: 0000 */
    public void init() {
        inflate(getContext(), C1075R.layout.primary_secondary_code_error_dialog, this);
        ButterKnife.inject((View) this);
    }
}
