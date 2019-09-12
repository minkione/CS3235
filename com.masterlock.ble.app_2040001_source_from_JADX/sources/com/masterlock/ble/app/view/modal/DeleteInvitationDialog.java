package com.masterlock.ble.app.view.modal;

import android.content.Context;
import android.support.p003v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;

public class DeleteInvitationDialog extends CardView {
    @InjectView(2131296317)
    TextView mBody;
    @InjectView(2131296320)
    LinearLayout mBodyContainer;
    @InjectView(2131296363)
    Button mCancelButton;
    @InjectView(2131296420)
    Button mDeleteGuestButton;
    @InjectView(2131296697)
    Button mRevokeAccessButton;

    public DeleteInvitationDialog(Context context) {
        this(context, null);
    }

    public DeleteInvitationDialog(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        inflate(getContext(), C1075R.layout.delete_invitation_dialog, this);
        ButterKnife.inject((View) this);
    }

    public TextView getBody() {
        return this.mBody;
    }

    public void setBody(TextView textView) {
        this.mBody = textView;
    }

    public LinearLayout getBodyContainer() {
        return this.mBodyContainer;
    }

    public void setBodyContainer(LinearLayout linearLayout) {
        this.mBodyContainer = linearLayout;
    }

    public Button getRevokeAccessButton() {
        return this.mRevokeAccessButton;
    }

    public void setRevokeAccessButton(Button button) {
        this.mRevokeAccessButton = button;
    }

    public Button getDeleteGuestButton() {
        return this.mDeleteGuestButton;
    }

    public void setDeleteGuestButton(Button button) {
        this.mDeleteGuestButton = button;
    }

    public Button getCancelButton() {
        return this.mCancelButton;
    }

    public void setCancelButton(Button button) {
        this.mCancelButton = button;
    }

    public void setRevokeAccessButtonClickListener(OnClickListener onClickListener) {
        this.mRevokeAccessButton.setOnClickListener(onClickListener);
    }

    public void setDeleteGuestButtonClickListener(OnClickListener onClickListener) {
        this.mDeleteGuestButton.setOnClickListener(onClickListener);
    }

    public void setCancelButtonClickListener(OnClickListener onClickListener) {
        this.mCancelButton.setOnClickListener(onClickListener);
    }
}
