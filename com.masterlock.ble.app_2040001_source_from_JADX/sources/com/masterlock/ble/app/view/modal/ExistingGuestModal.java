package com.masterlock.ble.app.view.modal;

import android.content.Context;
import android.support.p003v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;
import com.masterlock.core.Invitation;
import java.util.List;

public class ExistingGuestModal extends CardView {
    @InjectView(2131296317)
    TextView mBody;
    @InjectView(2131296320)
    LinearLayout mBodyContainer;
    /* access modifiers changed from: private */
    public List<Invitation> mInvitationList;
    /* access modifiers changed from: private */
    public OnGuestSelectedListener mOnGuestSelectedListener;
    /* access modifiers changed from: private */
    public int mSelectedInvitation;
    @InjectView(2131296802)
    TextView mTitle;
    @InjectView(2131296631)
    Button negativeButton;
    @InjectView(2131296655)
    Button positiveButton;
    @InjectView(2131296677)
    RadioButton rBGuest1;
    @InjectView(2131296678)
    RadioButton rBGuest2;
    @InjectView(2131296698)
    RadioGroup rGGuests;

    public interface OnGuestSelectedListener {
        void onGuestSelected(Invitation invitation);
    }

    public ExistingGuestModal(Context context) {
        this(context, null);
    }

    public ExistingGuestModal(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSelectedInvitation = 0;
        init();
    }

    private void init() {
        inflate(getContext(), C1075R.layout.existing_guest_modal_view, this);
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

    public LinearLayout getBodyContainer() {
        return this.mBodyContainer;
    }

    public Button getPositiveButton() {
        return this.positiveButton;
    }

    public Button getNegativeButton() {
        return this.negativeButton;
    }

    public void setPositiveButtonClickListener(final OnClickListener onClickListener) {
        this.positiveButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ExistingGuestModal.this.mOnGuestSelectedListener != null) {
                    ExistingGuestModal.this.mOnGuestSelectedListener.onGuestSelected((Invitation) ExistingGuestModal.this.mInvitationList.get(ExistingGuestModal.this.mSelectedInvitation));
                }
                onClickListener.onClick(view);
            }
        });
    }

    public void setNegativeButtonClickListener(OnClickListener onClickListener) {
        this.negativeButton.setOnClickListener(onClickListener);
    }

    public void setInvitationList(List<Invitation> list) {
        this.mInvitationList = list;
        guestsSetup();
    }

    public void setGuestSelectionListener(OnGuestSelectedListener onGuestSelectedListener) {
        this.mOnGuestSelectedListener = onGuestSelectedListener;
        this.rGGuests.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public final void onCheckedChanged(RadioGroup radioGroup, int i) {
                ExistingGuestModal.lambda$setGuestSelectionListener$0(ExistingGuestModal.this, radioGroup, i);
            }
        });
    }

    public static /* synthetic */ void lambda$setGuestSelectionListener$0(ExistingGuestModal existingGuestModal, RadioGroup radioGroup, int i) {
        if (i == C1075R.C1077id.rb_guest_1) {
            existingGuestModal.mSelectedInvitation = 0;
        } else {
            existingGuestModal.mSelectedInvitation = 1;
        }
    }

    private void guestsSetup() {
        List<Invitation> list = this.mInvitationList;
        if (list == null || list.size() != 2) {
            Log.d(getClass().getSimpleName(), "guestsSetup: GuestList is null or has incorrect size");
            return;
        }
        this.rBGuest1.setText(((Invitation) this.mInvitationList.get(0)).getGuest().getDisplayableName());
        this.rBGuest2.setText(((Invitation) this.mInvitationList.get(1)).getGuest().getDisplayableName());
        String simpleName = getClass().getSimpleName();
        StringBuilder sb = new StringBuilder();
        sb.append("guestsSetup: ");
        sb.append(((Invitation) this.mInvitationList.get(0)).getGuest().getDisplayableName());
        Log.d(simpleName, sb.toString());
    }
}
