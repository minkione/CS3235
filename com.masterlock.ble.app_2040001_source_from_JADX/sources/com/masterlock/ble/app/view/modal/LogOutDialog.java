package com.masterlock.ble.app.view.modal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import butterknife.ButterKnife;
import com.masterlock.ble.app.C1075R;

public class LogOutDialog extends SimpleDialog {
    public LogOutDialog(Context context) {
        this(context, null);
    }

    public LogOutDialog(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: 0000 */
    public void init() {
        inflate(getContext(), C1075R.layout.log_out_dialog, this);
        ButterKnife.inject((View) this);
    }
}
