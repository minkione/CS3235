package com.crashlytics.android.beta;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build.VERSION;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;
import p008io.fabric.sdk.android.Fabric;
import p008io.fabric.sdk.android.Kit;
import p008io.fabric.sdk.android.services.cache.MemoryValueCache;
import p008io.fabric.sdk.android.services.common.CommonUtils;
import p008io.fabric.sdk.android.services.common.DeliveryMechanism;
import p008io.fabric.sdk.android.services.common.DeviceIdentifierProvider;
import p008io.fabric.sdk.android.services.common.IdManager;
import p008io.fabric.sdk.android.services.common.IdManager.DeviceIdentifierType;
import p008io.fabric.sdk.android.services.common.SystemCurrentTimeProvider;
import p008io.fabric.sdk.android.services.network.DefaultHttpRequestFactory;
import p008io.fabric.sdk.android.services.persistence.PreferenceStoreImpl;
import p008io.fabric.sdk.android.services.settings.BetaSettingsData;
import p008io.fabric.sdk.android.services.settings.Settings;
import p008io.fabric.sdk.android.services.settings.SettingsData;

public class Beta extends Kit<Boolean> implements DeviceIdentifierProvider {
    private static final String CRASHLYTICS_API_ENDPOINT = "com.crashlytics.ApiEndpoint";
    private static final String CRASHLYTICS_BUILD_PROPERTIES = "crashlytics-build.properties";
    static final String NO_DEVICE_TOKEN = "";
    public static final String TAG = "Beta";
    private final MemoryValueCache<String> deviceTokenCache = new MemoryValueCache<>();
    private final DeviceTokenLoader deviceTokenLoader = new DeviceTokenLoader();
    private UpdatesController updatesController;

    public String getIdentifier() {
        return "com.crashlytics.sdk.android:beta";
    }

    public String getVersion() {
        return "1.1.4.92";
    }

    public static Beta getInstance() {
        return (Beta) Fabric.getKit(Beta.class);
    }

    /* access modifiers changed from: protected */
    @TargetApi(14)
    public boolean onPreExecute() {
        this.updatesController = createUpdatesController(VERSION.SDK_INT, (Application) getContext().getApplicationContext());
        return true;
    }

    /* access modifiers changed from: protected */
    public Boolean doInBackground() {
        Fabric.getLogger().mo21793d(TAG, "Beta kit initializing...");
        Context context = getContext();
        IdManager idManager = getIdManager();
        if (TextUtils.isEmpty(getBetaDeviceToken(context, idManager.getInstallerPackageName()))) {
            Fabric.getLogger().mo21793d(TAG, "A Beta device token was not found for this app");
            return Boolean.valueOf(false);
        }
        Fabric.getLogger().mo21793d(TAG, "Beta device token is present, checking for app updates.");
        BetaSettingsData betaSettingsData = getBetaSettingsData();
        BuildProperties loadBuildProperties = loadBuildProperties(context);
        if (canCheckForUpdates(betaSettingsData, loadBuildProperties)) {
            this.updatesController.initialize(context, this, idManager, betaSettingsData, loadBuildProperties, new PreferenceStoreImpl(this), new SystemCurrentTimeProvider(), new DefaultHttpRequestFactory(Fabric.getLogger()));
        }
        return Boolean.valueOf(true);
    }

    /* access modifiers changed from: 0000 */
    @TargetApi(14)
    public UpdatesController createUpdatesController(int i, Application application) {
        if (i >= 14) {
            return new ActivityLifecycleCheckForUpdatesController(getFabric().getActivityLifecycleManager(), getFabric().getExecutorService());
        }
        return new ImmediateCheckForUpdatesController();
    }

    public Map<DeviceIdentifierType, String> getDeviceIdentifiers() {
        String betaDeviceToken = getBetaDeviceToken(getContext(), getIdManager().getInstallerPackageName());
        HashMap hashMap = new HashMap();
        if (!TextUtils.isEmpty(betaDeviceToken)) {
            hashMap.put(DeviceIdentifierType.FONT_TOKEN, betaDeviceToken);
        }
        return hashMap;
    }

    /* access modifiers changed from: 0000 */
    @TargetApi(11)
    public boolean isAppPossiblyInstalledByBeta(String str, int i) {
        if (i >= 11) {
            return DeliveryMechanism.BETA_APP_PACKAGE_NAME.equals(str);
        }
        return str == null;
    }

    /* access modifiers changed from: 0000 */
    public boolean canCheckForUpdates(BetaSettingsData betaSettingsData, BuildProperties buildProperties) {
        return (betaSettingsData == null || TextUtils.isEmpty(betaSettingsData.updateUrl) || buildProperties == null) ? false : true;
    }

    private String getBetaDeviceToken(Context context, String str) {
        if (isAppPossiblyInstalledByBeta(str, VERSION.SDK_INT)) {
            Fabric.getLogger().mo21793d(TAG, "App was possibly installed by Beta. Getting device token");
            try {
                String str2 = (String) this.deviceTokenCache.get(context, this.deviceTokenLoader);
                if ("".equals(str2)) {
                    return null;
                }
                return str2;
            } catch (Exception e) {
                Fabric.getLogger().mo21796e(TAG, "Failed to load the Beta device token", e);
                return null;
            }
        } else {
            Fabric.getLogger().mo21793d(TAG, "App was not installed by Beta. Skipping device token");
            return null;
        }
    }

    private BetaSettingsData getBetaSettingsData() {
        SettingsData awaitSettingsData = Settings.getInstance().awaitSettingsData();
        if (awaitSettingsData != null) {
            return awaitSettingsData.betaSettingsData;
        }
        return null;
    }

    /* JADX WARNING: type inference failed for: r7v1, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r0v2, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r5v0 */
    /* JADX WARNING: type inference failed for: r7v5 */
    /* JADX WARNING: type inference failed for: r0v5 */
    /* JADX WARNING: type inference failed for: r0v6 */
    /* JADX WARNING: type inference failed for: r5v1 */
    /* JADX WARNING: type inference failed for: r0v7 */
    /* JADX WARNING: type inference failed for: r0v10 */
    /* JADX WARNING: type inference failed for: r7v12 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x008e A[SYNTHETIC, Splitter:B:27:0x008e] */
    /* JADX WARNING: Unknown variable types count: 4 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.crashlytics.android.beta.BuildProperties loadBuildProperties(android.content.Context r7) {
        /*
            r6 = this;
            r0 = 0
            android.content.res.AssetManager r7 = r7.getAssets()     // Catch:{ Exception -> 0x006c }
            java.lang.String r1 = "crashlytics-build.properties"
            java.io.InputStream r7 = r7.open(r1)     // Catch:{ Exception -> 0x006c }
            if (r7 == 0) goto L_0x0053
            com.crashlytics.android.beta.BuildProperties r0 = com.crashlytics.android.beta.BuildProperties.fromPropertiesStream(r7)     // Catch:{ Exception -> 0x004e, all -> 0x004c }
            io.fabric.sdk.android.Logger r1 = p008io.fabric.sdk.android.Fabric.getLogger()     // Catch:{ Exception -> 0x004e, all -> 0x004c }
            java.lang.String r2 = "Beta"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x004e, all -> 0x004c }
            r3.<init>()     // Catch:{ Exception -> 0x004e, all -> 0x004c }
            java.lang.String r4 = r0.packageName     // Catch:{ Exception -> 0x004e, all -> 0x004c }
            r3.append(r4)     // Catch:{ Exception -> 0x004e, all -> 0x004c }
            java.lang.String r4 = " build properties: "
            r3.append(r4)     // Catch:{ Exception -> 0x004e, all -> 0x004c }
            java.lang.String r4 = r0.versionName     // Catch:{ Exception -> 0x004e, all -> 0x004c }
            r3.append(r4)     // Catch:{ Exception -> 0x004e, all -> 0x004c }
            java.lang.String r4 = " ("
            r3.append(r4)     // Catch:{ Exception -> 0x004e, all -> 0x004c }
            java.lang.String r4 = r0.versionCode     // Catch:{ Exception -> 0x004e, all -> 0x004c }
            r3.append(r4)     // Catch:{ Exception -> 0x004e, all -> 0x004c }
            java.lang.String r4 = ")"
            r3.append(r4)     // Catch:{ Exception -> 0x004e, all -> 0x004c }
            java.lang.String r4 = " - "
            r3.append(r4)     // Catch:{ Exception -> 0x004e, all -> 0x004c }
            java.lang.String r4 = r0.buildId     // Catch:{ Exception -> 0x004e, all -> 0x004c }
            r3.append(r4)     // Catch:{ Exception -> 0x004e, all -> 0x004c }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x004e, all -> 0x004c }
            r1.mo21793d(r2, r3)     // Catch:{ Exception -> 0x004e, all -> 0x004c }
            goto L_0x0053
        L_0x004c:
            r0 = move-exception
            goto L_0x008c
        L_0x004e:
            r1 = move-exception
            r5 = r0
            r0 = r7
            r7 = r5
            goto L_0x006e
        L_0x0053:
            if (r7 == 0) goto L_0x0065
            r7.close()     // Catch:{ IOException -> 0x0059 }
            goto L_0x0065
        L_0x0059:
            r7 = move-exception
            io.fabric.sdk.android.Logger r1 = p008io.fabric.sdk.android.Fabric.getLogger()
            java.lang.String r2 = "Beta"
            java.lang.String r3 = "Error closing Beta build properties asset"
            r1.mo21796e(r2, r3, r7)
        L_0x0065:
            r7 = r0
            goto L_0x008b
        L_0x0067:
            r7 = move-exception
            r5 = r0
            r0 = r7
            r7 = r5
            goto L_0x008c
        L_0x006c:
            r1 = move-exception
            r7 = r0
        L_0x006e:
            io.fabric.sdk.android.Logger r2 = p008io.fabric.sdk.android.Fabric.getLogger()     // Catch:{ all -> 0x0067 }
            java.lang.String r3 = "Beta"
            java.lang.String r4 = "Error reading Beta build properties"
            r2.mo21796e(r3, r4, r1)     // Catch:{ all -> 0x0067 }
            if (r0 == 0) goto L_0x008b
            r0.close()     // Catch:{ IOException -> 0x007f }
            goto L_0x008b
        L_0x007f:
            r0 = move-exception
            io.fabric.sdk.android.Logger r1 = p008io.fabric.sdk.android.Fabric.getLogger()
            java.lang.String r2 = "Beta"
            java.lang.String r3 = "Error closing Beta build properties asset"
            r1.mo21796e(r2, r3, r0)
        L_0x008b:
            return r7
        L_0x008c:
            if (r7 == 0) goto L_0x009e
            r7.close()     // Catch:{ IOException -> 0x0092 }
            goto L_0x009e
        L_0x0092:
            r7 = move-exception
            io.fabric.sdk.android.Logger r1 = p008io.fabric.sdk.android.Fabric.getLogger()
            java.lang.String r2 = "Beta"
            java.lang.String r3 = "Error closing Beta build properties asset"
            r1.mo21796e(r2, r3, r7)
        L_0x009e:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.crashlytics.android.beta.Beta.loadBuildProperties(android.content.Context):com.crashlytics.android.beta.BuildProperties");
    }

    /* access modifiers changed from: 0000 */
    public String getOverridenSpiEndpoint() {
        return CommonUtils.getStringsFileValue(getContext(), CRASHLYTICS_API_ENDPOINT);
    }
}
