package com.masterlock.ble.app.view.lock.dialspeed;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.adapters.DialSpeedCodesAdapter;
import com.masterlock.ble.app.presenter.lock.dialspeed.DialSpeedDetailsPresenter;
import com.masterlock.ble.app.screens.LockScreens.DialSpeedDetails;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;
import java.util.ArrayList;

public class DialSpeedDetailsView extends LinearLayout implements IAuthenticatedView {
    DialSpeedCodesAdapter mAdapter;
    @InjectView(2131296425)
    RecyclerView mCodesRV;
    DialSpeedDetailsPresenter mDialSpeedPresenter;
    @InjectView(2131296423)
    Button mEditCodesBtn;
    @InjectView(2131296871)
    TextView mLockNameTV;

    public DialSpeedDetailsView(Context context) {
        super(context);
    }

    public DialSpeedDetailsView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DialSpeedDetailsView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.inject((View) this);
        Lock lock = ((DialSpeedDetails) AppFlow.getScreen(getContext())).getLock();
        this.mCodesRV.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mCodesRV.setHasFixedSize(true);
        this.mDialSpeedPresenter = new DialSpeedDetailsPresenter(this, lock);
        this.mDialSpeedPresenter.start();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mDialSpeedPresenter.finish();
    }

    public void displayError(Throwable th) {
        Toast.makeText(getContext(), th.getMessage() != null ? th.getMessage() : th.getClass().getSimpleName(), 0).show();
    }

    public void setUpViewWithLock(Lock lock) {
        this.mLockNameTV.setText(lock.getName());
        if (lock.isBiometricPadLock()) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(lock.generateProductCode(1, getResources().getString(C1075R.string.title_primary_code), lock.getPrimaryCode()));
            this.mAdapter = new DialSpeedCodesAdapter(arrayList, getContext());
        } else {
            this.mAdapter = new DialSpeedCodesAdapter(lock.getProductCodes(), getContext());
        }
        this.mCodesRV.setAdapter(this.mAdapter);
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296423})
    public void goToEditCodes() {
        this.mDialSpeedPresenter.transitionToEditCodes();
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }
}
