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

public class DeleteLastLocationDialog extends CardView {
    @InjectView(2131296631)
    Button mCancelButton;
    @InjectView(2131296655)
    Button mClearLastLocation;

    public DeleteLastLocationDialog(Context context) {
        this(context, null);
    }

    public DeleteLastLocationDialog(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        inflate(getContext(), C1075R.layout.delete_last_location_dialog, this);
        ButterKnife.inject((View) this);
    }

    public void setClearLastLocationButtonClickListener(OnClickListener onClickListener) {
        this.mClearLastLocation.setOnClickListener(onClickListener);
    }

    public void setCancelButtonClickListener(OnClickListener onClickListener) {
        this.mCancelButton.setOnClickListener(onClickListener);
    }
}
