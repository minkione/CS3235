package com.masterlock.ble.app.view;

import android.content.Context;
import android.support.p000v4.widget.DrawerLayout;
import android.support.p000v4.widget.DrawerLayout.SimpleDrawerListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.presenter.NavigationDrawerPresenter;
import com.masterlock.ble.app.screens.NavScreens.AccountSettings;
import com.masterlock.ble.app.screens.NavScreens.ContactMasterlock;
import com.masterlock.ble.app.screens.NavScreens.Help;
import com.masterlock.ble.app.screens.NavScreens.PrivacyPolicy;
import com.masterlock.ble.app.screens.SignUpScreens.TermsOfService;

public class NavigationDrawerView extends LinearLayout {
    @InjectView(2131296302)
    TextView mAppVersion;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerPresenter mNavigationDrawerPresenter;

    static /* synthetic */ boolean lambda$onFinishInflate$0(View view, MotionEvent motionEvent) {
        return true;
    }

    public NavigationDrawerView(Context context) {
        this(context, null);
    }

    public NavigationDrawerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        inflate(context, C1075R.layout.navigation_drawer, this);
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.mDrawerLayout = drawerLayout;
        this.mDrawerLayout.setDrawerListener(new SimpleDrawerListener() {
            public void onDrawerStateChanged(int i) {
                super.onDrawerStateChanged(i);
                ((InputMethodManager) NavigationDrawerView.this.getContext().getSystemService("input_method")).hideSoftInputFromWindow(NavigationDrawerView.this.getWindowToken(), 0);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject((View) this);
        MasterLockApp.get().inject(this);
        setOnTouchListener($$Lambda$NavigationDrawerView$Yhu8WzYvEJmti9Ml8sHRgqBU8.INSTANCE);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mNavigationDrawerPresenter = new NavigationDrawerPresenter(this, this.mDrawerLayout);
        this.mNavigationDrawerPresenter.start();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mDrawerLayout = null;
    }

    public void updateAppVersion(String str) {
        this.mAppVersion.setText(getResources().getString(C1075R.string.app_version, new Object[]{str}));
    }

    @OnClick({2131296885})
    public void goToSettings() {
        this.mNavigationDrawerPresenter.goTo(new AccountSettings());
    }

    @OnClick({2131296874})
    public void confirmLogOut() {
        this.mNavigationDrawerPresenter.confirmLogout();
    }

    @OnClick({2131296862})
    public void goToHelp() {
        this.mNavigationDrawerPresenter.goTo(new Help());
    }

    @OnClick({2131296888})
    public void goToTermsOfService() {
        this.mNavigationDrawerPresenter.goTo(new TermsOfService(false));
    }

    @OnClick({2131296879})
    public void goToPrivacyPolicy() {
        this.mNavigationDrawerPresenter.goTo(new PrivacyPolicy());
    }

    @OnClick({2131296853})
    public void goToContactMasterlock() {
        this.mNavigationDrawerPresenter.goTo(new ContactMasterlock());
    }
}
