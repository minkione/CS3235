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
import butterknife.Optional;
import com.masterlock.ble.app.C1075R;

public class SimpleDialog extends CardView {
    @InjectView(2131296875)
    @Optional
    TextView message;
    @InjectView(2131296631)
    Button negativeButton;
    @InjectView(2131296655)
    Button positiveButton;

    public SimpleDialog(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    /* access modifiers changed from: 0000 */
    public void init() {
        inflate(getContext(), C1075R.layout.simple_dialog, this);
        ButterKnife.inject((View) this);
    }

    public void setPositiveButtonClickListener(OnClickListener onClickListener) {
        this.positiveButton.setOnClickListener(onClickListener);
    }

    public void setNegativeButtonClickListener(OnClickListener onClickListener) {
        this.negativeButton.setOnClickListener(onClickListener);
        this.negativeButton.setVisibility(0);
    }

    public void setPositiveButton(CharSequence charSequence) {
        this.positiveButton.setText(charSequence);
    }

    public void setPositiveButton(int i) {
        this.positiveButton.setText(getResources().getText(i));
    }

    public void setNegativeButton(CharSequence charSequence) {
        this.negativeButton.setText(charSequence);
        this.negativeButton.setVisibility(0);
    }

    public void setNegativeButton(int i) {
        this.negativeButton.setText(getResources().getText(i));
        this.negativeButton.setVisibility(0);
    }

    public void setMessage(CharSequence charSequence) {
        this.message.setText(charSequence);
    }

    public void setMessage(int i) {
        this.message.setText(getResources().getText(i));
    }
}
