package com.masterlock.ble.app.view.settings.keysafe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.settings.keysafe.UnlockModeKeySafePresenter;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.ble.app.view.settings.UnlockModeCard;
import com.masterlock.core.LockMode;

public class UnlockModeKeySafeListView extends RelativeLayout implements IAuthenticatedView {
    public static final boolean DISABLED = false;
    public static final boolean ENABLED = true;
    private LockMode mInitialMode;
    @InjectView(2131296597)
    TextView mLockNameBanner;
    @InjectView(2131296667)
    UnlockModeCard mProximitySwipeModeCard;
    private OnClickListener mProximitySwipeSelectListener;
    @InjectView(2131296354)
    Button mSaveButton;
    @InjectView(2131296817)
    UnlockModeCard mTouchModeCard;
    private OnClickListener mTouchUnlockSelectListener;
    private UnlockModeKeySafePresenter mUnlockModeKeySafePresenter;
    @InjectView(2131296422)
    TextView txtDeviceId;

    public UnlockModeKeySafeListView(Context context) {
        super(context);
    }

    public UnlockModeKeySafeListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public UnlockModeKeySafeListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mUnlockModeKeySafePresenter.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mUnlockModeKeySafePresenter.finish();
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mUnlockModeKeySafePresenter = new UnlockModeKeySafePresenter(this);
            this.mInitialMode = this.mUnlockModeKeySafePresenter.getLock().getLockMode();
            setUpClickListeners();
            setUpUnlockModeCards();
            setInitialMode();
        }
    }

    private void setUpClickListeners() {
        this.mTouchUnlockSelectListener = new OnClickListener() {
            public final void onClick(View view) {
                UnlockModeKeySafeListView.this.setActiveCardForMode(LockMode.TOUCH);
            }
        };
        this.mProximitySwipeSelectListener = new OnClickListener() {
            public final void onClick(View view) {
                UnlockModeKeySafeListView.this.setActiveCardForMode(LockMode.PROXIMITYSWIPE);
            }
        };
    }

    private void setUpUnlockModeCards() {
        this.mTouchModeCard.setUpCard(C1075R.string.touch_unlock_card_title, C1075R.string.touch_unlock_card_body, C1075R.string.touch_unlock_card_desc, C1075R.C1076drawable.graphic_touch_unlock, this.mTouchUnlockSelectListener);
        this.mTouchModeCard.setInBetaMode(false);
        this.mProximitySwipeModeCard.setUpCard(C1075R.string.proximity_swipe_card_title, C1075R.string.proximity_swipe_card_body, C1075R.string.proximity_swipe_card_desc, C1075R.C1076drawable.graphic_proximity_swipe, this.mProximitySwipeSelectListener);
        this.mProximitySwipeModeCard.setInBetaMode(true);
        setActiveCardForMode(this.mInitialMode);
    }

    private void setInitialMode() {
        switch (this.mInitialMode) {
            case PROXIMITYSWIPE:
                this.mProximitySwipeModeCard.setAsCurrent();
                return;
            case TOUCH:
                this.mTouchModeCard.setAsCurrent();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void setActiveCardForMode(LockMode lockMode) {
        if (lockMode != null && !lockMode.equals(LockMode.UNKNOWN)) {
            switch (lockMode) {
                case PROXIMITYSWIPE:
                    this.mProximitySwipeModeCard.setCardAsActive();
                    this.mTouchModeCard.setCardAsInactive();
                    this.mUnlockModeKeySafePresenter.setLockMode(LockMode.PROXIMITYSWIPE);
                    enableSaveOnNewSelection(LockMode.PROXIMITYSWIPE);
                    break;
                case TOUCH:
                    this.mTouchModeCard.setCardAsActive();
                    this.mProximitySwipeModeCard.setCardAsInactive();
                    this.mUnlockModeKeySafePresenter.setLockMode(LockMode.TOUCH);
                    enableSaveOnNewSelection(LockMode.TOUCH);
                    break;
                case PROXIMITY:
                    this.mProximitySwipeModeCard.setCardAsInactive();
                    this.mTouchModeCard.setCardAsInactive();
                    this.mUnlockModeKeySafePresenter.setLockMode(LockMode.PROXIMITY);
                    enableSaveOnNewSelection(LockMode.PROXIMITY);
                    break;
            }
        }
    }

    private void enableSaveOnNewSelection(LockMode lockMode) {
        if (!lockMode.equals(this.mInitialMode)) {
            this.mSaveButton.setEnabled(true);
            this.mSaveButton.setTextColor(getResources().getColor(C1075R.color.primary));
            return;
        }
        this.mSaveButton.setEnabled(false);
        this.mSaveButton.setTextColor(getResources().getColor(C1075R.color.medium_grey));
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296354})
    public void onSave() {
        this.mUnlockModeKeySafePresenter.saveNewUnlockMode();
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    public void setLockName(String str) {
        this.mLockNameBanner.setText(getResources().getString(C1075R.string.about_lock_name, new Object[]{str}));
    }

    public void setDeviceId(String str) {
        this.txtDeviceId.setText(str);
    }
}
