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

public class BleLocationPermissionModal extends CardView {
    @InjectView(2131296631)
    Button mCancel;
    @InjectView(2131296655)
    Button mEnableBleLocation;

    public BleLocationPermissionModal(Context context) {
        this(context, null);
    }

    public BleLocationPermissionModal(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        inflate(getContext(), C1075R.layout.ble_location_permission_dialog, this);
        ButterKnife.inject((View) this);
    }

    public void setEnableBleLocation(OnClickListener onClickListener) {
        this.mEnableBleLocation.setOnClickListener(onClickListener);
    }

    public void setCancelClickListener(OnClickListener onClickListener) {
        this.mCancel.setOnClickListener(onClickListener);
    }
}
