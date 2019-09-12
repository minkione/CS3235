package p008io.fabric.sdk.android.services.settings;

import android.content.Context;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import p008io.fabric.sdk.android.Fabric;
import p008io.fabric.sdk.android.Kit;
import p008io.fabric.sdk.android.services.common.ApiKey;
import p008io.fabric.sdk.android.services.common.CommonUtils;
import p008io.fabric.sdk.android.services.common.DeliveryMechanism;
import p008io.fabric.sdk.android.services.common.IdManager;
import p008io.fabric.sdk.android.services.common.SystemCurrentTimeProvider;
import p008io.fabric.sdk.android.services.network.HttpRequestFactory;

/* renamed from: io.fabric.sdk.android.services.settings.Settings */
public class Settings {
    public static final String SETTINGS_CACHE_FILENAME = "com.crashlytics.settings.json";
    private static final String SETTINGS_URL_FORMAT = "https://settings.crashlytics.com/spi/v2/platforms/android/apps/%s/settings";
    private boolean initialized;
    private SettingsController settingsController;
    private final AtomicReference<SettingsData> settingsData;
    private final CountDownLatch settingsDataLatch;

    /* renamed from: io.fabric.sdk.android.services.settings.Settings$LazyHolder */
    static class LazyHolder {
        /* access modifiers changed from: private */
        public static final Settings INSTANCE = new Settings();

        LazyHolder() {
        }
    }

    /* renamed from: io.fabric.sdk.android.services.settings.Settings$SettingsAccess */
    public interface SettingsAccess<T> {
        T usingSettings(SettingsData settingsData);
    }

    public static Settings getInstance() {
        return LazyHolder.INSTANCE;
    }

    private Settings() {
        this.settingsData = new AtomicReference<>();
        this.settingsDataLatch = new CountDownLatch(1);
        this.initialized = false;
    }

    public synchronized Settings initialize(Kit kit, IdManager idManager, HttpRequestFactory httpRequestFactory, String str, String str2, String str3) {
        Kit kit2 = kit;
        synchronized (this) {
            if (this.initialized) {
                return this;
            }
            if (this.settingsController == null) {
                Context context = kit.getContext();
                String appIdentifier = idManager.getAppIdentifier();
                String value = new ApiKey().getValue(context);
                String installerPackageName = idManager.getInstallerPackageName();
                SystemCurrentTimeProvider systemCurrentTimeProvider = new SystemCurrentTimeProvider();
                DefaultSettingsJsonTransform defaultSettingsJsonTransform = new DefaultSettingsJsonTransform();
                DefaultCachedSettingsIo defaultCachedSettingsIo = new DefaultCachedSettingsIo(kit2);
                String appIconHashOrNull = CommonUtils.getAppIconHashOrNull(context);
                String str4 = str3;
                DefaultSettingsSpiCall defaultSettingsSpiCall = new DefaultSettingsSpiCall(kit2, str4, String.format(Locale.US, SETTINGS_URL_FORMAT, new Object[]{appIdentifier}), httpRequestFactory);
                String modelName = idManager.getModelName();
                String osBuildVersionString = idManager.getOsBuildVersionString();
                String str5 = osBuildVersionString;
                String str6 = str2;
                String str7 = str;
                SettingsRequest settingsRequest = new SettingsRequest(value, modelName, str5, idManager.getOsDisplayVersionString(), idManager.getAdvertisingId(), idManager.getAppInstallIdentifier(), idManager.getAndroidId(), CommonUtils.createInstanceIdFrom(CommonUtils.resolveBuildId(context)), str6, str7, DeliveryMechanism.determineFrom(installerPackageName).getId(), appIconHashOrNull);
                DefaultSettingsController defaultSettingsController = new DefaultSettingsController(kit, settingsRequest, systemCurrentTimeProvider, defaultSettingsJsonTransform, defaultCachedSettingsIo, defaultSettingsSpiCall);
                this.settingsController = defaultSettingsController;
            }
            this.initialized = true;
            return this;
        }
    }

    public void clearSettings() {
        this.settingsData.set(null);
    }

    public void setSettingsController(SettingsController settingsController2) {
        this.settingsController = settingsController2;
    }

    public <T> T withSettings(SettingsAccess<T> settingsAccess, T t) {
        SettingsData settingsData2 = (SettingsData) this.settingsData.get();
        return settingsData2 == null ? t : settingsAccess.usingSettings(settingsData2);
    }

    public SettingsData awaitSettingsData() {
        try {
            this.settingsDataLatch.await();
            return (SettingsData) this.settingsData.get();
        } catch (InterruptedException unused) {
            Fabric.getLogger().mo21795e(Fabric.TAG, "Interrupted while waiting for settings data.");
            return null;
        }
    }

    public synchronized boolean loadSettingsData() {
        SettingsData loadSettingsData;
        loadSettingsData = this.settingsController.loadSettingsData();
        setSettingsData(loadSettingsData);
        return loadSettingsData != null;
    }

    public synchronized boolean loadSettingsSkippingCache() {
        SettingsData loadSettingsData;
        loadSettingsData = this.settingsController.loadSettingsData(SettingsCacheBehavior.SKIP_CACHE_LOOKUP);
        setSettingsData(loadSettingsData);
        if (loadSettingsData == null) {
            Fabric.getLogger().mo21796e(Fabric.TAG, "Failed to force reload of settings from Crashlytics.", null);
        }
        return loadSettingsData != null;
    }

    private void setSettingsData(SettingsData settingsData2) {
        this.settingsData.set(settingsData2);
        this.settingsDataLatch.countDown();
    }
}
