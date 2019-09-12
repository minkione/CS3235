package com.masterlock.ble.app.view.lock.padlock;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.akta.android.p004ui.floatinglabeledittext.FloatingLabelEditText;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.lock.padlock.LastLocationInfoPadLockPresenter;
import com.masterlock.ble.app.screens.LockScreens.LastLocationInfoPadLock;
import com.masterlock.ble.app.util.ViewUtil;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.core.AccessType;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;

public class LastLocationInfoPadLockView extends LinearLayout implements IAuthenticatedView {
    @InjectView(2131296556)
    TextView mAddress;
    @InjectView(2131296557)
    TextView mClear;
    @InjectView(2131296558)
    TextView mCoordinates;
    private Lock mLock;
    @InjectView(2131296561)
    FloatingLabelEditText mLockNotes;
    @InjectView(2131296559)
    FrameLayout mMapView;
    private LastLocationInfoPadLockPresenter mPresenter;

    public LastLocationInfoPadLockView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mLock = ((LastLocationInfoPadLock) AppFlow.getScreen(getContext())).mLock;
            this.mPresenter = new LastLocationInfoPadLockPresenter(this.mLock, this);
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mPresenter.start();
            this.mLockNotes.getEditText().addTextChangedListener(ViewUtil.createHideErrorTextWatcher(this.mLockNotes));
            enableClearLocationButton(true);
            if (this.mLock.getAccessType() == AccessType.GUEST) {
                this.mClear.setVisibility(8);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mPresenter.finish();
        ViewUtil.hideKeyboard(getContext(), this.mLockNotes.getWindowToken());
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    public void displayError(ApiError apiError) {
        Toast.makeText(getContext(), apiError.getMessage(), 1).show();
    }

    public void displaySuccess() {
        Toast.makeText(getContext(), getResources().getString(C1075R.string.lock_notes_successfully_changed), 1).show();
    }

    public void updateNotes(String str) {
        FloatingLabelEditText floatingLabelEditText = this.mLockNotes;
        if (str == null) {
            str = "";
        }
        floatingLabelEditText.setText(str);
    }

    @OnClick({2131296557})
    public void clearLocation() {
        this.mPresenter.showClearLastLocationDialog();
    }

    @OnClick({2131296562})
    public void saveLocationNotes() {
        this.mPresenter.saveLocationNotes(this.mLockNotes.getText().toString());
        ViewUtil.hideKeyboard(getContext(), this.mLockNotes.getWindowToken());
    }

    public void showMap(boolean z) {
        FrameLayout frameLayout = this.mMapView;
        if (frameLayout != null) {
            frameLayout.setVisibility(z ? 0 : 8);
        }
    }

    public void showAddress(String str, String str2) {
        if (str == null || str.isEmpty() || str2 == null || str2.isEmpty()) {
            this.mAddress.setText("N/A");
            this.mCoordinates.setText("N/A");
            showMap(false);
            return;
        }
        showMap(true);
        this.mCoordinates.setText(String.format("%s, %s", new Object[]{str, str2}));
        this.mPresenter.showAddress(str, str2, this.mMapView.getContext());
    }

    public void updateAddress(String str) {
        this.mAddress.setText(str);
    }

    public void enableClearLocationButton(boolean z) {
        this.mClear.setEnabled(z);
    }
}
