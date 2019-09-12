package com.masterlock.ble.app.view.modal;

import android.content.Context;
import android.support.p003v7.widget.CardView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;

public class InvalidInvitationCodeDialog extends CardView {
    @InjectView(2131296655)
    Button positiveButton;
    @InjectView(2131296317)
    TextView tVBody;
    @InjectView(2131296802)
    TextView tVTitle;

    public InvalidInvitationCodeDialog(Context context) {
        super(context, null);
        init();
    }

    private void init() {
        inflate(getContext(), C1075R.layout.base_modal_view, this);
        ButterKnife.inject((View) this);
        ButterKnife.findById((View) this, (int) C1075R.C1077id.negative_button).setVisibility(8);
        this.tVTitle.setText(C1075R.string.title_invalid_invitation_code);
        this.tVBody.setText(C1075R.string.invalid_invitation_body_message);
        this.positiveButton.setText(C1075R.string.accept_button);
    }

    public void setPositiveButtonOnClickListener(OnClickListener onClickListener) {
        this.positiveButton.setOnClickListener(onClickListener);
    }
}
