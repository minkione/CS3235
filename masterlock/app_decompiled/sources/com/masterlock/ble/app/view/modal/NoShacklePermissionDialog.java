package com.masterlock.ble.app.view.modal;

import android.content.Context;
import android.support.p003v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;

public class NoShacklePermissionDialog extends CardView {
    @InjectView(2131296655)
    Button mPositiveButton;

    public NoShacklePermissionDialog(Context context) {
        this(context, null);
    }

    public NoShacklePermissionDialog(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        inflate(getContext(), C1075R.layout.no_shackle_permission_dialog, this);
        ButterKnife.inject((View) this);
    }

    public void setPositiveClickListener(OnClickListener onClickListener) {
        this.mPositiveButton.setOnClickListener(onClickListener);
    }
}
