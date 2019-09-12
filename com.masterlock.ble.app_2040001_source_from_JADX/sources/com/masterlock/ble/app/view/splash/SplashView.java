package com.masterlock.ble.app.view.splash;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.splash.SplashPresenter;

public class SplashView extends LinearLayout {
    SplashPresenter splashPresenter;

    public SplashView(Context context) {
        super(context);
    }

    public SplashView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SplashView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            this.splashPresenter = new SplashPresenter(this);
        }
    }

    public void displayError(ApiError apiError) {
        Toast.makeText(getContext(), apiError.getMessage(), 1).show();
    }

    public void hideProgress() {
        ButterKnife.findById((View) this, (int) C1075R.C1077id.progressBar1).setVisibility(8);
    }

    public void showProgress() {
        ButterKnife.findById((View) this, (int) C1075R.C1077id.progressBar1).setVisibility(0);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.splashPresenter.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.splashPresenter.finish();
    }
}
