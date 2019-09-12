package com.masterlock.ble.app.view.lock.padlock;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.lock.padlock.PrimaryCodeUpdatePadLockPresenter;
import com.masterlock.ble.app.screens.LockScreens.PrimaryCodeUpdatePadLock;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.ble.app.view.lock.DirectionalPadButton;
import com.masterlock.core.LockCodeDirection;
import com.square.flow.appflow.AppFlow;
import java.util.ArrayList;
import java.util.List;

public class PrimaryCodeUpdatePadLockView extends LinearLayout implements IAuthenticatedView {
    public static final int PRIMARY_CODE_SIZE = 7;
    private int mCurrentCode = 0;
    private List<LockCodeDirection> mEnteredLockCodeList;
    private List<View> mLockCodeViewList;
    private PrimaryCodeUpdatePadLockPresenter mPrimarycodeUpdatePadLockPresenter;

    public PrimaryCodeUpdatePadLockView(Context context) {
        super(context);
    }

    public PrimaryCodeUpdatePadLockView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PrimaryCodeUpdatePadLockView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mPrimarycodeUpdatePadLockPresenter = new PrimaryCodeUpdatePadLockPresenter(((PrimaryCodeUpdatePadLock) AppFlow.getScreen(getContext())).mLock, this);
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
        this.mEnteredLockCodeList = new ArrayList();
        for (int i = 0; i < 7; i++) {
            View inflate = layoutInflater.inflate(C1075R.layout.update_lock_code_item, linearLayout, false);
            inflate.setLayoutParams(layoutParams);
            inflate.setPadding(0, dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset);
            linearLayout.addView(inflate, i);
            this.mLockCodeViewList.add(i, inflate);
        }
        LinearLayout linearLayout2 = (LinearLayout) this.mLockCodeViewList.get(this.mCurrentCode);
        ImageView imageView = (ImageView) ButterKnife.findById((View) linearLayout2, (int) C1075R.C1077id.img_code_direction);
        TextView textView = (TextView) ButterKnife.findById((View) linearLayout2, (int) C1075R.C1077id.txt_code_direction);
        imageView.setImageResource(C1075R.C1076drawable.ic_arrow_up);
        imageView.setAlpha(0.35f);
        textView.setText(C1075R.string.lock_code_up);
        textView.setVisibility(0);
        imageView.setVisibility(0);
        this.mEnteredLockCodeList.add(this.mCurrentCode, LockCodeDirection.UP);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mPrimarycodeUpdatePadLockPresenter.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mPrimarycodeUpdatePadLockPresenter.finish();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296350})
    public void savePrimaryCode() {
        this.mPrimarycodeUpdatePadLockPresenter.savePrimaryCode(this.mEnteredLockCodeList);
    }

    /* access modifiers changed from: 0000 */
    @OnTouch({2131296413})
    public boolean dPadButtonRightClicked(MotionEvent motionEvent) {
        boolean viewTouched = ((DirectionalPadButton) ButterKnife.findById((View) this, (int) C1075R.C1077id.d_pad_button_right)).viewTouched(motionEvent);
        if (viewTouched && motionEvent.getAction() == 0) {
            updateDisplayedLockCode(LockCodeDirection.RIGHT);
        }
        return viewTouched;
    }

    /* access modifiers changed from: 0000 */
    @OnTouch({2131296411})
    public boolean dPadButtonDownClicked(MotionEvent motionEvent) {
        boolean viewTouched = ((DirectionalPadButton) ButterKnife.findById((View) this, (int) C1075R.C1077id.d_pad_button_down)).viewTouched(motionEvent);
        if (viewTouched && motionEvent.getAction() == 0) {
            updateDisplayedLockCode(LockCodeDirection.DOWN);
        }
        return viewTouched;
    }

    /* access modifiers changed from: 0000 */
    @OnTouch({2131296412})
    public boolean dPadButtonLeftClicked(MotionEvent motionEvent) {
        boolean viewTouched = ((DirectionalPadButton) ButterKnife.findById((View) this, (int) C1075R.C1077id.d_pad_button_left)).viewTouched(motionEvent);
        if (viewTouched && motionEvent.getAction() == 0) {
            updateDisplayedLockCode(LockCodeDirection.LEFT);
        }
        return viewTouched;
    }

    /* access modifiers changed from: 0000 */
    @OnTouch({2131296414})
    public boolean dPadButtonUpClicked(MotionEvent motionEvent) {
        boolean viewTouched = ((DirectionalPadButton) ButterKnife.findById((View) this, (int) C1075R.C1077id.d_pad_button_up)).viewTouched(motionEvent);
        if (viewTouched && motionEvent.getAction() == 0) {
            updateDisplayedLockCode(LockCodeDirection.UP);
        }
        return viewTouched;
    }

    private boolean updateDisplayedLockCode(LockCodeDirection lockCodeDirection) {
        int i;
        int i2;
        int i3 = this.mCurrentCode;
        if (i3 + 1 >= 7) {
            return false;
        }
        this.mCurrentCode = i3 + 1;
        View view = (View) this.mLockCodeViewList.get(this.mCurrentCode);
        ImageView imageView = (ImageView) ButterKnife.findById(view, (int) C1075R.C1077id.img_code_direction);
        TextView textView = (TextView) ButterKnife.findById(view, (int) C1075R.C1077id.txt_code_direction);
        switch (lockCodeDirection) {
            case LEFT:
                i = C1075R.C1076drawable.ic_arrow_left;
                i2 = C1075R.string.lock_code_left;
                this.mEnteredLockCodeList.add(this.mCurrentCode, LockCodeDirection.LEFT);
                break;
            case UP:
                i = C1075R.C1076drawable.ic_arrow_up;
                i2 = C1075R.string.lock_code_up;
                this.mEnteredLockCodeList.add(this.mCurrentCode, LockCodeDirection.UP);
                break;
            case RIGHT:
                i = C1075R.C1076drawable.ic_arrow_right;
                i2 = C1075R.string.lock_code_right;
                this.mEnteredLockCodeList.add(this.mCurrentCode, LockCodeDirection.RIGHT);
                break;
            case DOWN:
                i = C1075R.C1076drawable.ic_arrow_down;
                i2 = C1075R.string.lock_code_down;
                this.mEnteredLockCodeList.add(this.mCurrentCode, LockCodeDirection.DOWN);
                break;
            default:
                i = 0;
                i2 = 0;
                break;
        }
        imageView.setImageResource(i);
        textView.setText(i2);
        textView.setVisibility(0);
        imageView.setVisibility(0);
        return true;
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296658})
    public void deleteCodeEntry() {
        int i = this.mCurrentCode;
        if (i > 0) {
            View view = (View) this.mLockCodeViewList.get(i);
            ButterKnife.findById(view, (int) C1075R.C1077id.img_code_direction).setVisibility(4);
            ButterKnife.findById(view, (int) C1075R.C1077id.txt_code_direction).setVisibility(4);
            this.mEnteredLockCodeList.remove(this.mCurrentCode);
            this.mCurrentCode--;
        }
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }
}
