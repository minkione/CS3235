package com.masterlock.ble.app;

import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.google.security.PRNGFixes;
import com.masterlock.api.util.IResourceWrapper;
import com.masterlock.ble.app.analytics.Analytics;
import com.masterlock.ble.app.module.Modules;
import dagger.ObjectGraph;
import java.util.Locale;
import p008io.fabric.sdk.android.Fabric;

public class MasterLockApp extends MultiDexApplication implements IResourceWrapper {
    private static final long FIVE_MINUTES_IN_NANOSECONDS = 300000000000L;
    private static MasterLockApp mAppContext;
    private long initialInvalidAttemptTimeInNanoseconds;
    private int invalidAttemptCount;
    private Analytics mAnalytics;
    private String mDefSystemLanguage;
    private boolean mIsInBackground;
    private ObjectGraph mObjectGraph;

    public Locale getLocale() {
        return null;
    }

    public String stringFromId(int i) {
        return null;
    }

    public String stringFromName(String str) {
        return null;
    }

    public String stringFromName(String str, Object... objArr) {
        return null;
    }

    public String stringFromPlural(String str, int i, Object... objArr) {
        return null;
    }

    public static MasterLockApp get() {
        return mAppContext;
    }

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    public void onCreate() {
        super.onCreate();
        this.mDefSystemLanguage = Locale.getDefault().getLanguage();
        Fabric.with(this, new Crashlytics());
        initializeInvalidAttemptTracking();
        PRNGFixes.apply();
        this.mAnalytics = new Analytics(this, BuildConfig.ANALYTICS_TRACKING_ID);
        mAppContext = (MasterLockApp) getApplicationContext();
        mAppContext.registerActivityLifecycleCallbacks(new BaseLifeCycleCallbacks());
        setupObjectGraph();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mDefSystemLanguage = configuration.locale.getLanguage();
    }

    private void initializeInvalidAttemptTracking() {
        this.initialInvalidAttemptTimeInNanoseconds = -1;
        this.invalidAttemptCount = 0;
    }

    private void setupObjectGraph() {
        this.mObjectGraph = ObjectGraph.create(Modules.modulesForApp(this));
    }

    public Analytics getAnalytics() {
        return this.mAnalytics;
    }

    public void inject(Object obj) {
        this.mObjectGraph.inject(obj);
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0032 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void logOut(boolean r4) {
        /*
            r3 = this;
            r3.initializeInvalidAttemptTracking()
            android.content.ContentResolver r0 = r3.getContentResolver()     // Catch:{ Exception -> 0x0032 }
            android.net.Uri r1 = com.masterlock.ble.app.provider.MasterlockContract.Locks.CONTENT_URI     // Catch:{ Exception -> 0x0032 }
            r2 = 0
            r0.delete(r1, r2, r2)     // Catch:{ Exception -> 0x0032 }
            android.content.ContentResolver r0 = r3.getContentResolver()     // Catch:{ Exception -> 0x0032 }
            android.net.Uri r1 = com.masterlock.ble.app.provider.MasterlockContract.Guests.CONTENT_URI     // Catch:{ Exception -> 0x0032 }
            r0.delete(r1, r2, r2)     // Catch:{ Exception -> 0x0032 }
            android.content.ContentResolver r0 = r3.getContentResolver()     // Catch:{ Exception -> 0x0032 }
            android.net.Uri r1 = com.masterlock.ble.app.provider.MasterlockContract.DeviceInfo.CONTENT_URI     // Catch:{ Exception -> 0x0032 }
            r0.delete(r1, r2, r2)     // Catch:{ Exception -> 0x0032 }
            android.content.ContentResolver r0 = r3.getContentResolver()     // Catch:{ Exception -> 0x0032 }
            android.net.Uri r1 = com.masterlock.ble.app.provider.MasterlockContract.AvailableCommands.CONTENT_URI     // Catch:{ Exception -> 0x0032 }
            r0.delete(r1, r2, r2)     // Catch:{ Exception -> 0x0032 }
            android.content.ContentResolver r0 = r3.getContentResolver()     // Catch:{ Exception -> 0x0032 }
            android.net.Uri r1 = com.masterlock.ble.app.provider.MasterlockContract.AvailableSettings.CONTENT_URI     // Catch:{ Exception -> 0x0032 }
            r0.delete(r1, r2, r2)     // Catch:{ Exception -> 0x0032 }
            goto L_0x0046
        L_0x0032:
            android.content.Context r0 = r3.getApplicationContext()     // Catch:{ Exception -> 0x0045 }
            java.lang.String r1 = "masterlock23.db"
            r0.deleteDatabase(r1)     // Catch:{ Exception -> 0x0045 }
            android.content.Context r0 = r3.getApplicationContext()     // Catch:{ Exception -> 0x0045 }
            java.lang.String r1 = "masterlock.db"
            r0.deleteDatabase(r1)     // Catch:{ Exception -> 0x0045 }
            goto L_0x0046
        L_0x0045:
        L_0x0046:
            com.masterlock.ble.app.MasterLockSharedPreferences r0 = com.masterlock.ble.app.MasterLockSharedPreferences.getInstance()
            com.masterlock.ble.app.util.PermissionUtil$PermissionType r1 = com.masterlock.ble.app.util.PermissionUtil.PermissionType.CONTACTS
            java.lang.String r1 = r1.name()
            boolean r0 = r0.getFirstPermissionInteraction(r1)
            com.masterlock.ble.app.MasterLockSharedPreferences r1 = com.masterlock.ble.app.MasterLockSharedPreferences.getInstance()
            com.masterlock.ble.app.util.PermissionUtil$PermissionType r2 = com.masterlock.ble.app.util.PermissionUtil.PermissionType.LOCATION
            java.lang.String r2 = r2.name()
            boolean r1 = r1.getFirstPermissionInteraction(r2)
            com.masterlock.ble.app.MasterLockSharedPreferences r2 = com.masterlock.ble.app.MasterLockSharedPreferences.getInstance()
            r2.clear()
            if (r0 == 0) goto L_0x0078
            com.masterlock.ble.app.MasterLockSharedPreferences r0 = com.masterlock.ble.app.MasterLockSharedPreferences.getInstance()
            com.masterlock.ble.app.util.PermissionUtil$PermissionType r2 = com.masterlock.ble.app.util.PermissionUtil.PermissionType.CONTACTS
            java.lang.String r2 = r2.name()
            r0.putFirstPermissionInteraction(r2)
        L_0x0078:
            if (r1 == 0) goto L_0x0087
            com.masterlock.ble.app.MasterLockSharedPreferences r0 = com.masterlock.ble.app.MasterLockSharedPreferences.getInstance()
            com.masterlock.ble.app.util.PermissionUtil$PermissionType r1 = com.masterlock.ble.app.util.PermissionUtil.PermissionType.LOCATION
            java.lang.String r1 = r1.name()
            r0.putFirstPermissionInteraction(r1)
        L_0x0087:
            com.masterlock.ble.app.activity.LockActivity.cancelPasscodeTimeout()
            android.content.Intent r0 = new android.content.Intent
            java.lang.Class<com.masterlock.ble.app.service.scan.BackgroundScanService> r1 = com.masterlock.ble.app.service.scan.BackgroundScanService.class
            r0.<init>(r3, r1)
            r3.stopService(r0)
            android.content.Intent r0 = new android.content.Intent
            java.lang.Class<com.masterlock.ble.app.service.scan.FirmwareUpdateService> r1 = com.masterlock.ble.app.service.scan.FirmwareUpdateService.class
            r0.<init>(r3, r1)
            r3.stopService(r0)
            android.content.Intent r0 = new android.content.Intent
            java.lang.Class<com.masterlock.ble.app.service.LocationService> r1 = com.masterlock.ble.app.service.LocationService.class
            r0.<init>(r3, r1)
            r3.stopService(r0)
            if (r4 == 0) goto L_0x00be
            boolean r4 = r3.mIsInBackground
            if (r4 != 0) goto L_0x00be
            android.content.Intent r4 = new android.content.Intent
            java.lang.Class<com.masterlock.ble.app.activity.SignInActivity> r0 = com.masterlock.ble.app.activity.SignInActivity.class
            r4.<init>(r3, r0)
            r0 = 268468224(0x10008000, float:2.5342157E-29)
            r4.setFlags(r0)
            r3.startActivity(r4)
        L_0x00be:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.masterlock.ble.app.MasterLockApp.logOut(boolean):void");
    }

    public boolean isSignedIn() {
        return !TextUtils.isEmpty(MasterLockSharedPreferences.getInstance().getAuthToken());
    }

    /* access modifiers changed from: 0000 */
    public void setIsInBackground(boolean z) {
        this.mIsInBackground = z;
    }

    public boolean isInBackground() {
        return this.mIsInBackground;
    }

    public void successfullyVerified() {
        initializeInvalidAttemptTracking();
    }

    public void invalidVerification() {
        this.invalidAttemptCount++;
        if (this.invalidAttemptCount == 1) {
            this.initialInvalidAttemptTimeInNanoseconds = System.nanoTime();
        } else if (System.nanoTime() - this.initialInvalidAttemptTimeInNanoseconds > FIVE_MINUTES_IN_NANOSECONDS) {
            this.invalidAttemptCount = 1;
            this.initialInvalidAttemptTimeInNanoseconds = System.nanoTime();
        } else if (this.invalidAttemptCount == 5) {
            Toast.makeText(mAppContext, C1075R.string.invalid_password_attempts_alert_message, 0).show();
            logOut(true);
        }
    }

    public void logOut() {
        Log.i("MasterLockApp", "Log out forced");
        logOut(true);
    }

    public String getDefSystemLanguage() {
        return this.mDefSystemLanguage;
    }
}
