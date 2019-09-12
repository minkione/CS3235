package com.masterlock.ble.app.view.welcome;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.masterlock.ble.app.presenter.welcome.WelcomePresenter;

public class WelcomeView extends LinearLayout {
    WelcomePresenter welcomePresenter;

    public WelcomeView(Context context) {
        this(context, null);
    }

    public WelcomeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.welcomePresenter = new WelcomePresenter(this);
        this.welcomePresenter.start();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.welcomePresenter.finish();
    }
}
