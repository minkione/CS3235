package com.masterlock.ble.app.view.lock.keysafe;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.lock.keysafe.SecondaryCodeUpdateKeySafePresenter;
import com.masterlock.ble.app.screens.LockScreens.SecondaryCodeUpdateKeySafe;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.core.SecondaryCodeIndex;
import com.square.flow.appflow.AppFlow;
import java.util.ArrayList;
import java.util.List;

public class SecondaryCodeUpdateKeySafeView extends LinearLayout implements IAuthenticatedView {
    public static final int SECONDARY_CODE_MIN_SIZE = 4;
    public static final int SECONDARY_CODE_SIZE = 8;
    private int mCurrentCode = 0;
    private StringBuilder mEnteredLockCodeSB;
    private List<View> mLockCodeViewList;
    private SecondaryCodeIndex mSecondaryCodeIndex;
    private SecondaryCodeUpdateKeySafePresenter mSecondaryCodeUpdateKeySafePresenter;

    public SecondaryCodeUpdateKeySafeView(Context context) {
        super(context);
    }

    public SecondaryCodeUpdateKeySafeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SecondaryCodeUpdateKeySafeView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            SecondaryCodeUpdateKeySafe secondaryCodeUpdateKeySafe = (SecondaryCodeUpdateKeySafe) AppFlow.getScreen(getContext());
            this.mSecondaryCodeUpdateKeySafePresenter = new SecondaryCodeUpdateKeySafePresenter(secondaryCodeUpdateKeySafe.mLock, this);
            this.mSecondaryCodeIndex = secondaryCodeUpdateKeySafe.mIndex;
            buildLockCodeContainer();
        }
    }

    private void buildLockCodeContainer() {
        LinearLayout linearLayout = (LinearLayout) ButterKnife.findById((View) this, (int) C1075R.C1077id.lock_code_container);
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.gravity = 80;
        int dimensionPixelOffset = getResources().getDimensionPixelOffset(C1075R.dimen.space_m0);
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
        this.mLockCodeViewList = new ArrayList();
        this.mEnteredLockCodeSB = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            View inflate = layoutInflater.inflate(C1075R.layout.secondary_code_digit_with_underscore, linearLayout, false);
            inflate.setLayoutParams(layoutParams);
            inflate.setPadding(0, dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset);
            linearLayout.addView(inflate, i);
            this.mLockCodeViewList.add(i, inflate);
        }
        enableDigit0(false);
        enableSave(false);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mSecondaryCodeUpdateKeySafePresenter.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mSecondaryCodeUpdateKeySafePresenter.finish();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296350})
    public void saveSecondaryCode() {
        this.mSecondaryCodeUpdateKeySafePresenter.validateCode(this.mEnteredLockCodeSB.toString());
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296402, 2131296403, 2131296404, 2131296405, 2131296406, 2131296407, 2131296408, 2131296409, 2131296410, 2131296401})
    public void onDPadButtonClicked(View view) {
        switch (view.getId()) {
            case C1075R.C1077id.d_pad_button_0 /*2131296401*/:
                updateDisplayedLockCode(0);
                return;
            case C1075R.C1077id.d_pad_button_1 /*2131296402*/:
                updateDisplayedLockCode(1);
                return;
            case C1075R.C1077id.d_pad_button_2 /*2131296403*/:
                updateDisplayedLockCode(2);
                return;
            case C1075R.C1077id.d_pad_button_3 /*2131296404*/:
                updateDisplayedLockCode(3);
                return;
            case C1075R.C1077id.d_pad_button_4 /*2131296405*/:
                updateDisplayedLockCode(4);
                return;
            case C1075R.C1077id.d_pad_button_5 /*2131296406*/:
                updateDisplayedLockCode(5);
                return;
            case C1075R.C1077id.d_pad_button_6 /*2131296407*/:
                updateDisplayedLockCode(6);
                return;
            case C1075R.C1077id.d_pad_button_7 /*2131296408*/:
                updateDisplayedLockCode(7);
                return;
            case C1075R.C1077id.d_pad_button_8 /*2131296409*/:
                updateDisplayedLockCode(8);
                return;
            case C1075R.C1077id.d_pad_button_9 /*2131296410*/:
                updateDisplayedLockCode(9);
                return;
            default:
                return;
        }
    }

    private boolean updateDisplayedLockCode(int i) {
        int i2 = this.mCurrentCode;
        if (i2 >= 8) {
            return false;
        }
        this.mCurrentCode = i2 + 1;
        if (this.mCurrentCode > 0) {
            enableDigit0(true);
        }
        if (this.mCurrentCode >= 4) {
            enableSave(true);
        }
        TextView textView = (TextView) ButterKnife.findById((View) this.mLockCodeViewList.get(this.mCurrentCode - 1), (int) C1075R.C1077id.txt_code_digit);
        this.mEnteredLockCodeSB.append(i);
        textView.setText(String.valueOf(i));
        textView.setVisibility(0);
        return true;
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296742})
    public void deleteCodeEntry() {
        int i = this.mCurrentCode;
        if (i > 0) {
            this.mCurrentCode = i - 1;
            this.mEnteredLockCodeSB.deleteCharAt(this.mCurrentCode);
            if (this.mCurrentCode < 4) {
                enableSave(false);
            }
        }
        ButterKnife.findById((View) this.mLockCodeViewList.get(this.mCurrentCode), (int) C1075R.C1077id.txt_code_digit).setVisibility(4);
        if (this.mCurrentCode < 1) {
            enableDigit0(false);
        }
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    private void enableDigit0(boolean z) {
        ButterKnife.findById((View) this, (int) C1075R.C1077id.d_pad_button_0).setEnabled(z);
    }

    private void enableSave(boolean z) {
        Resources resources;
        int i;
        View findById = ButterKnife.findById((View) this, (int) C1075R.C1077id.btn_save_code);
        findById.setEnabled(z);
        Button button = (Button) findById;
        if (z) {
            resources = getResources();
            i = C1075R.color.primary;
        } else {
            resources = getResources();
            i = C1075R.color.dark_grey;
        }
        button.setTextColor(resources.getColor(i));
    }
}
