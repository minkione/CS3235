package com.masterlock.ble.app.view.modal;

import android.content.Context;
import android.support.p003v7.widget.CardView;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;

public class BaseModal extends CardView {
    @InjectView(2131296317)
    TextView mBody;
    @InjectView(2131296320)
    LinearLayout mBodyContainer;
    @InjectView(2131296802)
    TextView mTitle;
    @InjectView(2131296631)
    Button negativeButton;
    @InjectView(2131296655)
    Button positiveButton;

    public BaseModal(Context context) {
        this(context, null);
    }

    public BaseModal(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        inflate(getContext(), C1075R.layout.base_modal_view, this);
        ButterKnife.inject((View) this);
    }

    public TextView getTitle() {
        return this.mTitle;
    }

    public void setTitle(String str) {
        this.mTitle.setText(str);
    }

    public TextView getBody() {
        return this.mBody;
    }

    public void setBody(String str) {
        this.mBody.setText(str);
    }

    public void setBody(Spanned spanned) {
        this.mBody.setText(spanned);
    }

    public LinearLayout getBodyContainer() {
        return this.mBodyContainer;
    }

    public Button getPositiveButton() {
        return this.positiveButton;
    }

    public Button getNegativeButton() {
        return this.negativeButton;
    }

    public void setPositiveButtonClickListener(OnClickListener onClickListener) {
        this.positiveButton.setOnClickListener(onClickListener);
    }

    public void setNegativeButtonClickListener(OnClickListener onClickListener) {
        this.negativeButton.setOnClickListener(onClickListener);
    }
}
