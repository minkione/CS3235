package com.masterlock.ble.app.view.lock.generic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.lock.generic.GenericLockDetailsPresenter;
import com.masterlock.ble.app.screens.LockScreens.GenericLock;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;

public class GenericLockDetailsView extends LinearLayout implements IAuthenticatedView {
    @InjectView(2131296497)
    TextView mCombinationTV;
    private GenericLockDetailsPresenter mGenericLockDetailsPresenter;
    @InjectView(2131296871)
    TextView mLockNameTV;

    public GenericLockDetailsView(Context context) {
        super(context);
    }

    public GenericLockDetailsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public GenericLockDetailsView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.inject((View) this);
        this.mGenericLockDetailsPresenter = new GenericLockDetailsPresenter(this, ((GenericLock) AppFlow.getScreen(getContext())).mLock);
        this.mGenericLockDetailsPresenter.start();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mGenericLockDetailsPresenter.finish();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296338})
    public void goToEdit() {
        this.mGenericLockDetailsPresenter.goToEditGenericLock();
    }

    public void displayError(Throwable th) {
        Toast.makeText(getContext(), th.getMessage() != null ? th.getMessage() : th.getClass().getSimpleName(), 0).show();
    }

    public void updateView(Lock lock) {
        this.mLockNameTV.setText(lock.getName());
        this.mCombinationTV.setText(lock.getNotes());
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }
}
