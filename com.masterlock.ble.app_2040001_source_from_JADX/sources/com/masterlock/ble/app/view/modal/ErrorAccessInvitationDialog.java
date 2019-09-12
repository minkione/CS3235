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

public class ErrorAccessInvitationDialog extends CardView {
    @InjectView(2131296642)
    Button okButton;

    public ErrorAccessInvitationDialog(Context context) {
        super(context);
    }

    public ErrorAccessInvitationDialog(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void init() {
        inflate(getContext(), C1075R.layout.error_access_invitation_dialog, this);
        ButterKnife.inject((View) this);
    }

    public void setOkButtonClickListener(OnClickListener onClickListener) {
        this.okButton.setOnClickListener(onClickListener);
    }
}
