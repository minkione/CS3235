package com.masterlock.ble.app.view.modal;

import android.content.Context;
import android.support.p003v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.core.AccessType;
import com.masterlock.core.Lock;

public class LockerModeDialogKeySafe extends CardView {
    @InjectView(2131296318)
    TextView mBody1;
    @InjectView(2131296319)
    TextView mBody2;
    @InjectView(2131296657)
    LinearLayout mPrimaryCodeContainer;
    @InjectView(2131296802)
    TextView mTitle;
    @InjectView(2131296655)
    Button positiveButton;

    public LockerModeDialogKeySafe(Context context) {
        this(context, null);
    }

    public LockerModeDialogKeySafe(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    /* access modifiers changed from: 0000 */
    public void init() {
        inflate(getContext(), C1075R.layout.locker_mode_dialog, this);
        ButterKnife.inject((View) this);
    }

    public void displayState(boolean z, Lock lock) {
        int i = 8;
        if (z) {
            this.mTitle.setText(getResources().getString(C1075R.string.lock_communications_disabled));
            this.mBody1.setText(getResources().getString(C1075R.string.lock_communications_disabled_description));
            this.mBody2.setText(getResources().getString(C1075R.string.locker_mode_enabled_reminder));
            this.mBody2.setVisibility(0);
            boolean canManageLock = MasterLockSharedPreferences.getInstance().canManageLock();
            LinearLayout linearLayout = this.mPrimaryCodeContainer;
            if (canManageLock) {
                i = 0;
            }
            linearLayout.setVisibility(i);
            fillCode(lock);
            return;
        }
        this.mTitle.setText(getResources().getString(C1075R.string.lock_communications_enabled));
        this.mBody1.setText(getResources().getString(C1075R.string.locker_mode_disabled_description));
        this.mBody2.setVisibility(8);
        this.mPrimaryCodeContainer.setVisibility(8);
    }

    public void displayStateFirmwareUpdate(Lock lock) {
        this.mTitle.setText(getResources().getString(C1075R.string.locker_mode_firmware_update_enabled_alert));
        this.mBody1.setText(getResources().getString(C1075R.string.locker_mode_firmware_update_enabled_description));
        this.mBody2.setVisibility(8);
        this.mPrimaryCodeContainer.setVisibility(8);
    }

    public void fillCode(Lock lock) {
        String serviceCode = lock.getAccessType() == AccessType.GUEST ? lock.getServiceCode() : lock.getPrimaryCode();
        this.mPrimaryCodeContainer.removeAllViews();
        char[] charArray = serviceCode.toCharArray();
        boolean z = true;
        boolean z2 = lock.getAccessType() != AccessType.GUEST;
        if (lock.getAccessType() != AccessType.GUEST || !lock.getPermissions().isViewTemporaryCodePermission()) {
            z = false;
        }
        if (z2 || z) {
            for (char c : charArray) {
                View inflate = ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(C1075R.layout.lock_code_item_key_safe, this.mPrimaryCodeContainer, false);
                ((TextView) ButterKnife.findById(inflate, (int) C1075R.C1077id.txt_code_direction)).setText(String.valueOf(c));
                this.mPrimaryCodeContainer.addView(inflate);
            }
        }
    }

    public void setPositiveButtonClickListener(OnClickListener onClickListener) {
        this.positiveButton.setOnClickListener(onClickListener);
    }

    public void setPositiveButton(CharSequence charSequence) {
        this.positiveButton.setText(charSequence);
    }

    public void setPositiveButton(int i) {
        this.positiveButton.setText(getResources().getText(i));
    }
}
