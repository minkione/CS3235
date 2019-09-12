package com.masterlock.ble.app.view.lock.keysafe;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.lock.keysafe.UnlockShacklePresenter;
import com.masterlock.ble.app.screens.LockScreens.UnlockShackle;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.ble.app.view.lock.keysafe.shackle.ShackleFoundUnlockingView;
import com.masterlock.ble.app.view.lock.keysafe.shackle.ShackleRemovedView;
import com.masterlock.ble.app.view.lock.keysafe.shackle.ShackleSecuredView;
import com.masterlock.ble.app.view.lock.keysafe.shackle.ShackleUnlockedView;
import com.masterlock.ble.app.view.lock.keysafe.shackle.ShackleWakeLockView;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;
import com.square.flow.screenswitcher.HandlesBack;
import com.square.flow.screenswitcher.HandlesUp;
import java.util.concurrent.TimeUnit;
import p009rx.Observable;
import p009rx.Subscription;
import p009rx.android.schedulers.AndroidSchedulers;
import p009rx.functions.Action0;
import p009rx.functions.Action1;
import p009rx.schedulers.Schedulers;
import p009rx.subscriptions.Subscriptions;

public class UnlockShackleView extends LinearLayout implements IAuthenticatedView, HandlesUp, HandlesBack {
    private static final String TAG = "UnlockShackleView";
    @InjectView(2131296361)
    View mButtonBar;
    @InjectView(2131296392)
    ViewFlipper mContentFlipper;
    /* access modifiers changed from: private */
    public boolean mCountDownRunning;
    private Subscription mCountDownSubscription;
    private CountDownTimer mCountDownTimer;
    @InjectView(2131296422)
    TextView mDeviceIdBanner;
    private Lock mLock;
    @InjectView(2131296597)
    TextView mLockNameBanner;
    UnlockShacklePresenter mPresenter;
    @InjectView(2131296656)
    Button mPrimaryActionButton;
    private boolean mRetry;
    private ShackleFoundUnlockingView mShackleFoundUnlockingView;
    private ShackleRemovedView mShackleRemovedView;
    private ShackleSecuredView mShackleSecuredView;
    private ShackleUnlockedView mShackleUnlockedView;
    private ShackleWakeLockView mShackleWakeLockView;
    private UnlockShackleViewState mViewState;

    private enum UnlockShackleViewState {
        WAKE_LOCK(0),
        LOCK_FOUND_UNLOCKING(1),
        SHACKLE_UNLOCKED(2),
        SHACKLE_REMOVED(3),
        SHACKLE_SECURED(4);
        
        private final int value;

        private UnlockShackleViewState(int i) {
            this.value = i;
        }

        public static UnlockShackleViewState fromKey(int i) {
            UnlockShackleViewState[] values;
            for (UnlockShackleViewState unlockShackleViewState : values()) {
                if (unlockShackleViewState.getValue() == i) {
                    return unlockShackleViewState;
                }
            }
            return WAKE_LOCK;
        }

        public int getValue() {
            return this.value;
        }
    }

    public UnlockShackleView(Context context) {
        this(context, null);
    }

    public UnlockShackleView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mCountDownSubscription = Subscriptions.empty();
        this.mRetry = false;
        this.mCountDownRunning = false;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mLock = ((UnlockShackle) AppFlow.getScreen(getContext())).mLock;
            this.mPresenter = new UnlockShacklePresenter(this);
            initContentFlipper();
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mPresenter.start();
            this.mLockNameBanner.setText(this.mLock.getName());
            this.mDeviceIdBanner.setText(this.mLock.getKmsDeviceKey().getDeviceId());
            updateView(this.mLock);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelTimer();
        this.mPresenter.finish();
        this.mPresenter = null;
    }

    private void initContentFlipper() {
        LayoutInflater from = LayoutInflater.from(getContext());
        this.mShackleWakeLockView = (ShackleWakeLockView) from.inflate(C1075R.layout.shackle_wake_lock_view, this.mContentFlipper, false);
        this.mContentFlipper.addView(this.mShackleWakeLockView, UnlockShackleViewState.WAKE_LOCK.getValue());
        this.mShackleFoundUnlockingView = (ShackleFoundUnlockingView) from.inflate(C1075R.layout.shackle_found_unlocking_view, this.mContentFlipper, false);
        this.mContentFlipper.addView(this.mShackleFoundUnlockingView, UnlockShackleViewState.LOCK_FOUND_UNLOCKING.getValue());
        this.mShackleUnlockedView = (ShackleUnlockedView) from.inflate(C1075R.layout.shackle_unlocked_view, this.mContentFlipper, false);
        this.mContentFlipper.addView(this.mShackleUnlockedView, UnlockShackleViewState.SHACKLE_UNLOCKED.getValue());
        this.mShackleRemovedView = (ShackleRemovedView) from.inflate(C1075R.layout.shackle_removed_view, this.mContentFlipper, false);
        this.mContentFlipper.addView(this.mShackleRemovedView, UnlockShackleViewState.SHACKLE_REMOVED.getValue());
        this.mShackleSecuredView = (ShackleSecuredView) from.inflate(C1075R.layout.shackle_secured_view, this.mContentFlipper, false);
        this.mContentFlipper.addView(this.mShackleSecuredView, UnlockShackleViewState.SHACKLE_SECURED.getValue());
    }

    public void updateView(Lock lock) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("updateView: ");
        sb.append(lock.getShackleStatus());
        Log.i(str, sb.toString());
        switch (lock.getShackleStatus()) {
            case UNKNOWN:
            case UNREACHABLE:
            case NO_COMMUNICATION:
                displayWakeLock();
                break;
            case LOCK_FOUND:
            case UNLOCKING:
            case LOCKED:
                if (!this.mRetry) {
                    if (!this.mPresenter.hasUnlocked()) {
                        displayLockFound();
                        break;
                    } else {
                        displayShackleSecured();
                        break;
                    }
                }
                break;
            case UNLOCKED:
                displayShackleUnlocked();
                break;
            case OPENED:
                displayShackleRemoved();
                break;
        }
        updateButtonBar();
    }

    public void displayWakeLock() {
        this.mViewState = UnlockShackleViewState.WAKE_LOCK;
        this.mContentFlipper.setDisplayedChild(UnlockShackleViewState.WAKE_LOCK.getValue());
    }

    public void displayLockFound() {
        this.mViewState = UnlockShackleViewState.LOCK_FOUND_UNLOCKING;
        this.mContentFlipper.setDisplayedChild(UnlockShackleViewState.LOCK_FOUND_UNLOCKING.getValue());
    }

    public void displayShackleUnlocked() {
        this.mViewState = UnlockShackleViewState.SHACKLE_UNLOCKED;
        this.mContentFlipper.setDisplayedChild(UnlockShackleViewState.SHACKLE_UNLOCKED.getValue());
        initializeTimer();
        this.mRetry = false;
    }

    public void displayShackleRemoved() {
        this.mViewState = UnlockShackleViewState.SHACKLE_REMOVED;
        this.mContentFlipper.setDisplayedChild(UnlockShackleViewState.SHACKLE_REMOVED.getValue());
    }

    public void displayShackleSecured() {
        this.mViewState = UnlockShackleViewState.SHACKLE_SECURED;
        this.mContentFlipper.setDisplayedChild(UnlockShackleViewState.SHACKLE_SECURED.getValue());
    }

    public void updateButtonBar() {
        switch (this.mViewState) {
            case WAKE_LOCK:
                this.mButtonBar.setVisibility(0);
                this.mPrimaryActionButton.setText(getResources().getString(C1075R.string.primary_try_later));
                return;
            case LOCK_FOUND_UNLOCKING:
            case SHACKLE_UNLOCKED:
            case SHACKLE_REMOVED:
                this.mButtonBar.setVisibility(4);
                return;
            case SHACKLE_SECURED:
                this.mButtonBar.setVisibility(0);
                this.mPrimaryActionButton.setText(getResources().getString(C1075R.string.unlock_shackle));
                return;
            default:
                return;
        }
    }

    @OnClick({2131296656})
    @Optional
    public void onPrimaryClicked() {
        switch (this.mViewState) {
            case WAKE_LOCK:
                this.mPresenter.tryLater();
                return;
            case LOCK_FOUND_UNLOCKING:
            case SHACKLE_UNLOCKED:
            case SHACKLE_REMOVED:
                return;
            case SHACKLE_SECURED:
                this.mViewState = UnlockShackleViewState.WAKE_LOCK;
                updateView(this.mLock);
                this.mPresenter.unlockShackle();
                this.mRetry = true;
                return;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append(this.mViewState);
                sb.append(" not supported");
                throw new UnsupportedOperationException(sb.toString());
        }
    }

    public boolean onUpPressed() {
        this.mPresenter.onBackPressed();
        return false;
    }

    public boolean onBackPressed() {
        this.mPresenter.onBackPressed();
        return false;
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    public void displayError(Throwable th) {
        Toast.makeText(getContext(), th.getMessage() != null ? th.getMessage() : th.getClass().getSimpleName(), 0).show();
    }

    public void initializeTimer() {
        int relockTimeInSeconds = this.mLock.getRelockTimeInSeconds();
        if (!this.mCountDownRunning) {
            this.mCountDownSubscription.unsubscribe();
            this.mCountDownSubscription = Observable.timer(0, 1, TimeUnit.SECONDS).take(relockTimeInSeconds + 1).subscribeOn(Schedulers.m220io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1(relockTimeInSeconds) {
                private final /* synthetic */ int f$1;

                {
                    this.f$1 = r2;
                }

                public final void call(Object obj) {
                    UnlockShackleView.this.mShackleUnlockedView.setRemainingRelockTime(Long.valueOf(((long) this.f$1) - ((Long) obj).longValue()));
                }
            }, $$Lambda$UnlockShackleView$VOaTNVbdCWjvoe0UNwrxmJXSeY.INSTANCE, new Action0() {
                public final void call() {
                    UnlockShackleView.this.mCountDownRunning = false;
                }
            });
            this.mCountDownRunning = true;
        }
    }

    private void cancelTimer() {
        this.mCountDownSubscription.unsubscribe();
    }
}
