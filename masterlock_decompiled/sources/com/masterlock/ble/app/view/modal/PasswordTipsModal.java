package com.masterlock.ble.app.view.modal;

import android.content.Context;
import android.support.p000v4.content.ContextCompat;
import android.support.p003v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.util.SignUpHelper.PasswordCriteria;
import java.util.Map;
import java.util.Map.Entry;

public class PasswordTipsModal extends CardView {
    private final String TAG;
    private Map<PasswordCriteria, Boolean> mPasswordCriteriaStatusMap;
    @InjectView(2131296583)
    LinearLayout passwordTipsContainer;
    @InjectView(2131296655)
    Button positiveButton;

    public PasswordTipsModal(Context context) {
        this(context, null);
    }

    public PasswordTipsModal(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.TAG = getClass().getSimpleName();
        this.mPasswordCriteriaStatusMap = null;
        setup();
    }

    private void setup() {
        inflate(getContext(), C1075R.layout.passoword_tips_modal, this);
        ButterKnife.inject((View) this);
    }

    public void initialize(Map<PasswordCriteria, Boolean> map) {
        this.mPasswordCriteriaStatusMap = map;
        inflatePasswordTipItems();
    }

    private void inflatePasswordTipItems() {
        LayoutInflater from = LayoutInflater.from(getContext());
        for (Entry entry : this.mPasswordCriteriaStatusMap.entrySet()) {
            Boolean bool = (Boolean) entry.getValue();
            PasswordCriteria passwordCriteria = (PasswordCriteria) entry.getKey();
            View inflate = from.inflate(C1075R.layout.password_tip, this.passwordTipsContainer, false);
            ((TextView) inflate.findViewById(C1075R.C1077id.tv_password_tip)).setText(getResources().getString(passwordCriteria.getStringResourceId()));
            ((ImageView) inflate.findViewById(C1075R.C1077id.iv_password_tip_icon)).setImageDrawable(ContextCompat.getDrawable(getContext(), bool.booleanValue() ? C1075R.C1076drawable.custom_ic_done_test : C1075R.C1076drawable.custom_ic_error_outline_test));
            this.passwordTipsContainer.addView(inflate);
        }
    }

    public void setPositiveButtonClickListener(OnClickListener onClickListener) {
        this.positiveButton.setOnClickListener(onClickListener);
    }
}
