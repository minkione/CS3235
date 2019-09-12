package com.masterlock.ble.app.activity;

import android.content.Intent;
import android.os.Bundle;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.bus.FinishActivityEvent;
import com.masterlock.ble.app.provider.MasterlockDatabase;
import com.masterlock.ble.app.screens.SplashScreens.SplashScreen;
import com.masterlock.ble.app.service.LocationService;
import com.masterlock.ble.app.service.SignUpService;
import com.masterlock.ble.app.service.scan.BackgroundScanService;
import com.masterlock.ble.app.service.scan.FirmwareUpdateService;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.android.schedulers.AndroidSchedulers;
import p009rx.schedulers.Schedulers;
import p009rx.subscriptions.Subscriptions;

public class SplashActivity extends FlowActivity {
    @Inject
    Bus mEventBus;
    private Subscription mInitDataBaseSubscription = Subscriptions.empty();
    @Inject
    SignUpService signUpService;

    public int getContentView() {
        return C1075R.layout.no_action_bar_activity;
    }

    public void showNoConnectionDialog() {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        MasterLockApp.get().inject(this);
        if (MasterLockApp.get().isSignedIn()) {
            startService(new Intent(this, FirmwareUpdateService.class));
            startService(new Intent(this, BackgroundScanService.class));
            startService(new Intent(this, LocationService.class));
            return;
        }
        initDataBase();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        MasterLockSharedPreferences.getInstance().putCanManageLock(false);
        this.mEventBus.register(this);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mEventBus.unregister(this);
    }

    public Object defaultScreen() {
        return new SplashScreen();
    }

    @Subscribe
    public void onFinishActivityEvent(FinishActivityEvent finishActivityEvent) {
        finish();
    }

    /* access modifiers changed from: private */
    public void initDataBase() {
        this.mInitDataBaseSubscription.unsubscribe();
        this.mInitDataBaseSubscription = this.signUpService.containsDictionaryWords("").subscribeOn(Schedulers.m220io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
            public void onCompleted() {
            }

            public void onNext(Boolean bool) {
            }

            public void onError(Throwable th) {
                SplashActivity.this.getApplicationContext().deleteDatabase(MasterlockDatabase.DATABASE_NAME);
                SplashActivity.this.initDataBase();
            }
        });
    }
}
