package com.masterlock.ble.app.view.modal;

import android.content.Context;
import android.support.p003v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;

public class CalibrateModal extends CardView {
    @InjectView(2131296325)
    Button mCalibrateLock;
    @InjectView(2131296833)
    TextView mSkipForNow;

    public CalibrateModal(Context context) {
        super(context);
    }

    public CalibrateModal(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        inflate(getContext(), C1075R.layout.calibrate_modal_view, this);
        ButterKnife.inject((View) this);
    }

    public void setOnCalibrationClickedListener(OnClickListener onClickListener) {
        this.mCalibrateLock.setOnClickListener(onClickListener);
    }

    public void setOnSkipClickedListener(OnClickListener onClickListener) {
        this.mSkipForNow.setOnClickListener(onClickListener);
    }
}
