package com.masterlock.ble.app.presenter;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.p000v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.analytics.Analytics;
import com.masterlock.ble.app.view.NavigationDrawerView;
import com.masterlock.ble.app.view.modal.LogOutDialog;
import com.masterlock.ble.app.view.modal.ProfileDialog;
import com.square.flow.appflow.AppFlow;
import flow.Screen;

public class NavigationDrawerPresenter extends Presenter<Void, NavigationDrawerView> {
    Dialog mDialog;
    DrawerLayout mDrawerLayout;

    private NavigationDrawerPresenter(NavigationDrawerView navigationDrawerView) {
        super(navigationDrawerView);
    }

    public NavigationDrawerPresenter(NavigationDrawerView navigationDrawerView, DrawerLayout drawerLayout) {
        this(navigationDrawerView);
        this.mDrawerLayout = drawerLayout;
    }

    public void start() {
        displayVersion();
    }

    public void finish() {
        super.finish();
        this.mDrawerLayout = null;
        Dialog dialog = this.mDialog;
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void displayVersion() {
        try {
            ((NavigationDrawerView) this.view).updateAppVersion(((NavigationDrawerView) this.view).getContext().getPackageManager().getPackageInfo(((NavigationDrawerView) this.view).getContext().getPackageName(), 0).versionName);
        } catch (Exception unused) {
        }
    }

    public void onProfileClick() {
        ProfileDialog profileDialog = new ProfileDialog(((NavigationDrawerView) this.view).getContext(), null);
        profileDialog.setPositiveButtonClickListener(new OnClickListener() {
            public final void onClick(View view) {
                NavigationDrawerPresenter.lambda$onProfileClick$0(NavigationDrawerPresenter.this, view);
            }
        });
        profileDialog.setNegativeButtonClickListener(new OnClickListener() {
            public final void onClick(View view) {
                NavigationDrawerPresenter.this.mDialog.dismiss();
            }
        });
        this.mDialog = new Dialog(((NavigationDrawerView) this.view).getContext());
        this.mDialog.requestWindowFeature(1);
        this.mDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mDialog.setContentView(profileDialog);
        this.mDialog.show();
    }

    public static /* synthetic */ void lambda$onProfileClick$0(NavigationDrawerPresenter navigationDrawerPresenter, View view) {
        navigationDrawerPresenter.mDialog.dismiss();
        navigationDrawerPresenter.mDrawerLayout.closeDrawer((View) navigationDrawerPresenter.view);
        String string = ((NavigationDrawerView) navigationDrawerPresenter.view).getContext().getString(C1075R.string.profile_webview_url);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(string));
        ((NavigationDrawerView) navigationDrawerPresenter.view).getContext().startActivity(intent);
    }

    public void goTo(Screen screen) {
        AppFlow.get(((NavigationDrawerView) this.view).getContext()).goTo(screen);
        MasterLockApp.get().getAnalytics().logEvent(Analytics.CATEGORY_MASTERLOCK_EVENT, Analytics.ACTION_EXTERNAL_LINK, Analytics.ACTION_EXTERNAL_LINK, 1);
    }

    public void confirmLogout() {
        LogOutDialog logOutDialog = new LogOutDialog(((NavigationDrawerView) this.view).getContext(), null);
        Dialog dialog = new Dialog(((NavigationDrawerView) this.view).getContext());
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.setContentView(logOutDialog);
        logOutDialog.setPositiveButtonClickListener(new OnClickListener(dialog) {
            private final /* synthetic */ Dialog f$0;

            {
                this.f$0 = r1;
            }

            public final void onClick(View view) {
                NavigationDrawerPresenter.lambda$confirmLogout$2(this.f$0, view);
            }
        });
        logOutDialog.setNegativeButtonClickListener(new OnClickListener(dialog) {
            private final /* synthetic */ Dialog f$0;

            {
                this.f$0 = r1;
            }

            public final void onClick(View view) {
                this.f$0.dismiss();
            }
        });
        dialog.show();
    }

    static /* synthetic */ void lambda$confirmLogout$2(Dialog dialog, View view) {
        dialog.dismiss();
        MasterLockApp.get().logOut(true);
    }
}
