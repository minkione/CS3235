package com.masterlock.ble.app.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.p000v4.view.GravityCompat;
import android.support.p000v4.widget.DrawerLayout;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import com.google.gson.Gson;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.gamma.LocaleHelper;
import com.masterlock.ble.app.screens.AppScreen;
import com.masterlock.ble.app.screens.HideToolbar;
import com.masterlock.ble.app.screens.MultiParent;
import com.masterlock.ble.app.util.AnimatingDrawerToggle;
import com.masterlock.ble.app.util.AnimatingDrawerToggle.State;
import com.masterlock.ble.app.view.NavigationDrawerView;
import com.masterlock.ble.app.view.modal.SimpleDialog;
import com.square.flow.GsonParcer;
import com.square.flow.appflow.AppFlow;
import com.square.flow.appflow.FlowBundler;
import com.square.flow.screenswitcher.FrameScreenSwitcherView;
import com.square.flow.screenswitcher.FrameScreenSwitcherView.OnScreenSwitchedListener;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import flow.Backstack;
import flow.Flow.Callback;
import flow.Flow.Direction;
import flow.Flow.Listener;
import flow.HasParent;
import flow.Screen;
import javax.inject.Inject;
import p007fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import p007fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;
import p007fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable.Callbacks;

public abstract class FlowActivity extends AppCompatActivity implements Listener, OnScreenSwitchedListener {
    private static final int MIN_DELAY = 0;
    private static final int MIN_SHOW_TIME = 800;
    protected AppFlow appFlow;
    @InjectView(2131296389)
    FrameScreenSwitcherView container;
    AnimatingDrawerToggle drawerToggle;
    protected FlowBundler flowBundler;
    private final Runnable mDelayedHide = new Runnable() {
        public void run() {
            FlowActivity.this.mPostedHide = false;
            FlowActivity.this.mStartTime = System.currentTimeMillis();
            FlowActivity.this.progressBar.progressiveStop();
        }
    };
    private final Runnable mDelayedShow = new Runnable() {
        public void run() {
            FlowActivity.this.mPostedShow = false;
            if (!FlowActivity.this.mDismissed) {
                FlowActivity.this.mStartTime = System.currentTimeMillis();
                FlowActivity.this.progressBar.setVisibility(0);
                FlowActivity.this.startProgressIfNecessary();
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mDismissed = false;
    @InjectView(2131296441)
    @Optional
    DrawerLayout mDrawerLayout;
    @InjectView(2131296440)
    @Optional
    NavigationDrawerView mDrawerView;
    @Inject
    Bus mEventBus;
    private boolean mIsBackArrow = false;
    private Dialog mNoConnectionDialog;
    /* access modifiers changed from: private */
    public boolean mPostedHide = false;
    /* access modifiers changed from: private */
    public boolean mPostedShow = false;
    private Object mProgressBarListener = new Object() {
        @Subscribe
        public void showProgressBar(ToggleProgressBarEvent toggleProgressBarEvent) {
            if (FlowActivity.this.progressBar == null) {
                return;
            }
            if (toggleProgressBarEvent.shouldShow()) {
                FlowActivity.this.showProgress();
            } else {
                FlowActivity.this.hideProgress();
            }
        }
    };
    /* access modifiers changed from: private */
    public long mStartTime = -1;
    @InjectView(2131296806)
    @Optional
    Toolbar mToolbar;
    @InjectView(2131296663)
    SmoothProgressBar progressBar;
    @InjectView(2131296807)
    @Optional
    View toolbarContainer;

    public abstract Object defaultScreen();

    public int getContentView() {
        return C1075R.layout.action_bar_activity;
    }

    public Listener listener() {
        return this;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        MasterLockApp.get().inject(this);
        this.flowBundler = new FlowBundler(defaultScreen(), listener(), new GsonParcer(new Gson()));
        this.appFlow = this.flowBundler.onCreate(bundle);
        disableAutoFill();
        setContentView(getContentView());
        ButterKnife.inject((Activity) this);
        this.container.setOnScreenSwitchedListener(this);
        Toolbar toolbar = this.mToolbar;
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        if (this.mDrawerView != null) {
            AnimatingDrawerToggle animatingDrawerToggle = new AnimatingDrawerToggle(this, this.mDrawerLayout, this.mToolbar, C1075R.string.open_drawer_content_desc, C1075R.string.close_drawer_content_desc);
            this.drawerToggle = animatingDrawerToggle;
            this.mToolbar.setNavigationOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (FlowActivity.this.drawerToggle.getCurrentState() == State.HOME) {
                        FlowActivity.this.mDrawerLayout.openDrawer((int) GravityCompat.START);
                    } else {
                        FlowActivity.this.container.onUpPressed();
                    }
                }
            });
            this.mDrawerLayout.setDrawerListener(this.drawerToggle);
            this.mDrawerView.setDrawerLayout(this.mDrawerLayout);
            this.mDrawerLayout.post(new Runnable() {
                public void run() {
                    FlowActivity.this.drawerToggle.syncState();
                }
            });
        }
        this.progressBar.setSmoothProgressDrawableCallbacks(new Callbacks() {
            public void onStart() {
            }

            public void onStop() {
                FlowActivity.this.progressBar.setVisibility(8);
            }
        });
        AppFlow.loadInitialScreen(this);
    }

    @TargetApi(26)
    private void disableAutoFill() {
        if (VERSION.SDK_INT >= 26) {
            getWindow().getDecorView().setImportantForAutofill(8);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mEventBus.register(this.mProgressBarListener);
        removeProgressCallbacks();
        checkForInternet();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mEventBus.unregister(this.mProgressBarListener);
        removeProgressCallbacks();
        this.mStartTime = -1;
        this.mPostedHide = false;
        this.mPostedShow = false;
        this.mDismissed = true;
        SmoothProgressBar smoothProgressBar = this.progressBar;
        if (smoothProgressBar != null) {
            smoothProgressBar.setVisibility(8);
        }
    }

    public Object getSystemService(String str) {
        if (AppFlow.isAppFlowSystemService(str)) {
            return this.appFlow;
        }
        return super.getSystemService(str);
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        this.flowBundler.onSaveInstanceState(bundle);
    }

    public void onBackPressed() {
        DrawerLayout drawerLayout = this.mDrawerLayout;
        if (drawerLayout != null && drawerLayout.isDrawerOpen((int) GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer((int) GravityCompat.START);
        } else if (!this.container.onBackPressed()) {
            super.onBackPressed();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        Dialog dialog = this.mNoConnectionDialog;
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        Screen screen = (Screen) AppFlow.get(this).getBackstack().current().getScreen();
        int menuResourceId = screen instanceof AppScreen ? ((AppScreen) screen).getMenuResourceId() : 0;
        if (menuResourceId != 0) {
            getMenuInflater().inflate(menuResourceId, menu);
        } else {
            menu.clear();
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        return super.onOptionsItemSelected(menuItem);
    }

    public void onComplete() {
        invalidateOptionsMenu();
    }

    /* renamed from: go */
    public void mo17059go(Backstack backstack, Direction direction, Callback callback) {
        Screen screen = (Screen) backstack.current().getScreen();
        boolean z = (screen instanceof HasParent) || (screen instanceof MultiParent);
        boolean z2 = screen instanceof HideToolbar;
        if (this.progressBar != null) {
            removeProgressCallbacks();
            this.mStartTime = System.currentTimeMillis();
            this.mPostedHide = false;
            this.mPostedShow = false;
            hideProgress();
        }
        this.container.showScreen(screen, direction, callback);
        DrawerLayout drawerLayout = this.mDrawerLayout;
        if (drawerLayout != null && drawerLayout.isDrawerOpen((View) this.mDrawerView)) {
            this.mDrawerLayout.closeDrawer((View) this.mDrawerView);
        }
        if (this.mToolbar == null) {
            return;
        }
        if (z2) {
            hideToolbar();
            return;
        }
        if (screen instanceof AppScreen) {
            int titleResourceId = ((AppScreen) screen).getTitleResourceId();
            if (titleResourceId != 0) {
                setTitle(titleResourceId);
            }
        }
        showToolbar();
        setBackArrow(z);
    }

    /* access modifiers changed from: protected */
    public void setBackArrow(boolean z) {
        this.mIsBackArrow = z;
        if (this.mDrawerLayout == null) {
            return;
        }
        if (z) {
            this.drawerToggle.animateToUp();
            this.mDrawerLayout.setDrawerLockMode(1);
            return;
        }
        this.drawerToggle.animateToHome();
        this.mDrawerLayout.setDrawerLockMode(0);
    }

    private void hideToolbar() {
        this.mToolbar.setVisibility(8);
    }

    private void showToolbar() {
        this.mToolbar.setVisibility(0);
    }

    private void removeProgressCallbacks() {
        SmoothProgressBar smoothProgressBar = this.progressBar;
        if (smoothProgressBar != null) {
            smoothProgressBar.removeCallbacks(this.mDelayedHide);
            this.progressBar.removeCallbacks(this.mDelayedShow);
        }
    }

    /* access modifiers changed from: private */
    public void hideProgress() {
        this.mDismissed = true;
        this.progressBar.removeCallbacks(this.mDelayedShow);
        this.mPostedShow = false;
        long currentTimeMillis = System.currentTimeMillis();
        long j = this.mStartTime;
        long j2 = currentTimeMillis - j;
        if (j2 >= 800 || j == -1) {
            this.progressBar.progressiveStop();
        } else if (!this.mPostedHide) {
            this.progressBar.postDelayed(this.mDelayedHide, 800 - j2);
            this.mPostedHide = true;
        }
    }

    /* access modifiers changed from: private */
    public void showProgress() {
        this.mStartTime = System.currentTimeMillis();
        this.mDismissed = false;
        this.progressBar.removeCallbacks(this.mDelayedHide);
        this.mPostedHide = false;
        if (!this.mPostedShow) {
            this.mPostedShow = false;
            this.progressBar.setVisibility(0);
            startProgressIfNecessary();
        }
    }

    /* access modifiers changed from: private */
    public void startProgressIfNecessary() {
        if (this.progressBar.getIndeterminateDrawable() != null && (this.progressBar.getIndeterminateDrawable() instanceof SmoothProgressDrawable)) {
            SmoothProgressDrawable smoothProgressDrawable = (SmoothProgressDrawable) this.progressBar.getIndeterminateDrawable();
            if (!smoothProgressDrawable.isStarting() && !smoothProgressDrawable.isRunning()) {
                this.progressBar.progressiveStart();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        AnimatingDrawerToggle animatingDrawerToggle = this.drawerToggle;
        if (animatingDrawerToggle != null) {
            animatingDrawerToggle.syncState();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        AnimatingDrawerToggle animatingDrawerToggle = this.drawerToggle;
        if (animatingDrawerToggle != null) {
            animatingDrawerToggle.onConfigurationChanged(configuration);
        }
    }

    private void checkForInternet() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        if (!(activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting())) {
            showNoConnectionDialog();
        } else {
            Dialog dialog = this.mNoConnectionDialog;
            if (dialog != null) {
                dialog.cancel();
            }
        }
    }

    public void showNoConnectionDialog() {
        Dialog dialog = this.mNoConnectionDialog;
        if (dialog != null) {
            dialog.show();
            return;
        }
        SimpleDialog simpleDialog = new SimpleDialog(this, null);
        final Dialog dialog2 = new Dialog(this);
        dialog2.requestWindowFeature(1);
        dialog2.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog2.setContentView(simpleDialog);
        simpleDialog.setMessage((int) C1075R.string.enable_connection);
        simpleDialog.setPositiveButton((int) C1075R.string.action_settings);
        simpleDialog.setPositiveButtonClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog2.dismiss();
                FlowActivity.this.startActivity(new Intent("android.settings.SETTINGS"));
            }
        });
        simpleDialog.setNegativeButtonClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog2.dismiss();
            }
        });
        dialog2.show();
    }

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context context) {
        super.attachBaseContext(LocaleHelper.onAttach());
    }
}
