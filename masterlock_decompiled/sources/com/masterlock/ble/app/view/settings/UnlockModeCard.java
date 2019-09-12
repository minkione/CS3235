package com.masterlock.ble.app.view.settings;

import android.content.Context;
import android.support.p003v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;

public class UnlockModeCard extends CardView {
    public static final boolean ACTIVE = true;
    public static final boolean INACTIVE = false;
    @InjectView(2131296898)
    ImageView mBetaMode;
    @InjectView(2131296918)
    TextView mCardDesc;
    @InjectView(2131296902)
    TextView mCardTitle;
    @InjectView(2131296398)
    TextView mCurrentUnlockModeText;
    @InjectView(2131296900)
    ImageView mImage;
    @InjectView(2131296749)
    Button mSelectButton;
    @InjectView(2131296919)
    TextView mWhatIsThisTitle;

    public UnlockModeCard(Context context) {
        super(context);
    }

    public UnlockModeCard(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public UnlockModeCard(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject((View) this);
    }

    public void setUpCard(int i, int i2, int i3, int i4, OnClickListener onClickListener) {
        this.mCardTitle.setText(i);
        this.mWhatIsThisTitle.setText(i2);
        this.mCardDesc.setText(i3);
        this.mImage.setImageResource(i4);
        this.mSelectButton.setOnClickListener(onClickListener);
    }

    public void setInBetaMode(boolean z) {
        if (z) {
            this.mBetaMode.setVisibility(0);
        } else {
            this.mBetaMode.setVisibility(4);
        }
    }

    public void setCardAsActive() {
        this.mSelectButton.setEnabled(false);
        this.mSelectButton.setTextColor(getContext().getResources().getColor(C1075R.color.medium_grey));
        this.mSelectButton.setText(C1075R.string.active_button_text);
    }

    public void setCardAsInactive() {
        this.mSelectButton.setEnabled(true);
        this.mSelectButton.setTextColor(getContext().getResources().getColor(C1075R.color.primary));
        this.mSelectButton.setText(C1075R.string.select);
    }

    public void setAsCurrent() {
        this.mCurrentUnlockModeText.setVisibility(0);
    }
}
