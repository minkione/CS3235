package p008io.fabric.sdk.android.services.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;
import p008io.fabric.sdk.android.Fabric;
import p008io.fabric.sdk.android.Kit;
import p008io.fabric.sdk.android.Logger;

/* renamed from: io.fabric.sdk.android.services.common.IdManager */
public class IdManager {
    private static final String BAD_ANDROID_ID = "9774d56d682e549c";
    public static final String COLLECT_DEVICE_IDENTIFIERS = "com.crashlytics.CollectDeviceIdentifiers";
    public static final String COLLECT_USER_IDENTIFIERS = "com.crashlytics.CollectUserIdentifiers";
    public static final String DEFAULT_VERSION_NAME = "0.0";
    private static final String FORWARD_SLASH_REGEX = Pattern.quote("/");
    private static final Pattern ID_PATTERN = Pattern.compile("[^\\p{Alnum}]");
    private static final String PREFKEY_INSTALLATION_UUID = "crashlytics.installation.id";
    AdvertisingInfo advertisingInfo;
    AdvertisingInfoProvider advertisingInfoProvider;
    private final Context appContext;
    private final String appIdentifier;
    private final String appInstallIdentifier;
    private final boolean collectHardwareIds;
    private final boolean collectUserIds;
    boolean fetchedAdvertisingInfo;
    private final ReentrantLock installationIdLock = new ReentrantLock();
    private final InstallerPackageNameProvider installerPackageNameProvider;
    private final Collection<Kit> kits;

    /* renamed from: io.fabric.sdk.android.services.common.IdManager$DeviceIdentifierType */
    public enum DeviceIdentifierType {
        WIFI_MAC_ADDRESS(1),
        BLUETOOTH_MAC_ADDRESS(2),
        FONT_TOKEN(53),
        ANDROID_ID(100),
        ANDROID_DEVICE_ID(101),
        ANDROID_SERIAL(102),
        ANDROID_ADVERTISING_ID(103);
        
        public final int protobufIndex;

        private DeviceIdentifierType(int i) {
            this.protobufIndex = i;
        }
    }

    @Deprecated
    public String createIdHeaderValue(String str, String str2) {
        return "";
    }

    @Deprecated
    public String getBluetoothMacAddress() {
        return null;
    }

    @Deprecated
    public String getSerialNumber() {
        return null;
    }

    @Deprecated
    public String getTelephonyId() {
        return null;
    }

    @Deprecated
    public String getWifiMacAddress() {
        return null;
    }

    public IdManager(Context context, String str, String str2, Collection<Kit> collection) {
        if (context == null) {
            throw new IllegalArgumentException("appContext must not be null");
        } else if (str == null) {
            throw new IllegalArgumentException("appIdentifier must not be null");
        } else if (collection != null) {
            this.appContext = context;
            this.appIdentifier = str;
            this.appInstallIdentifier = str2;
            this.kits = collection;
            this.installerPackageNameProvider = new InstallerPackageNameProvider();
            this.advertisingInfoProvider = new AdvertisingInfoProvider(context);
            this.collectHardwareIds = CommonUtils.getBooleanResourceValue(context, COLLECT_DEVICE_IDENTIFIERS, true);
            if (!this.collectHardwareIds) {
                Logger logger = Fabric.getLogger();
                String str3 = Fabric.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Device ID collection disabled for ");
                sb.append(context.getPackageName());
                logger.mo21793d(str3, sb.toString());
            }
            this.collectUserIds = CommonUtils.getBooleanResourceValue(context, COLLECT_USER_IDENTIFIERS, true);
            if (!this.collectUserIds) {
                Logger logger2 = Fabric.getLogger();
                String str4 = Fabric.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("User information collection disabled for ");
                sb2.append(context.getPackageName());
                logger2.mo21793d(str4, sb2.toString());
            }
        } else {
            throw new IllegalArgumentException("kits must not be null");
        }
    }

    public boolean canCollectUserIds() {
        return this.collectUserIds;
    }

    private String formatId(String str) {
        if (str == null) {
            return null;
        }
        return ID_PATTERN.matcher(str).replaceAll("").toLowerCase(Locale.US);
    }

    public String getAppInstallIdentifier() {
        String str = this.appInstallIdentifier;
        if (str != null) {
            return str;
        }
        SharedPreferences sharedPrefs = CommonUtils.getSharedPrefs(this.appContext);
        String string = sharedPrefs.getString(PREFKEY_INSTALLATION_UUID, null);
        return string == null ? createInstallationUUID(sharedPrefs) : string;
    }

    public String getAppIdentifier() {
        return this.appIdentifier;
    }

    public String getOsVersionString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getOsDisplayVersionString());
        sb.append("/");
        sb.append(getOsBuildVersionString());
        return sb.toString();
    }

    public String getOsDisplayVersionString() {
        return removeForwardSlashesIn(VERSION.RELEASE);
    }

    public String getOsBuildVersionString() {
        return removeForwardSlashesIn(VERSION.INCREMENTAL);
    }

    public String getModelName() {
        return String.format(Locale.US, "%s/%s", new Object[]{removeForwardSlashesIn(Build.MANUFACTURER), removeForwardSlashesIn(Build.MODEL)});
    }

    private String removeForwardSlashesIn(String str) {
        return str.replaceAll(FORWARD_SLASH_REGEX, "");
    }

    public String getDeviceUUID() {
        String str = "";
        if (!this.collectHardwareIds) {
            return str;
        }
        String androidId = getAndroidId();
        if (androidId != null) {
            return androidId;
        }
        SharedPreferences sharedPrefs = CommonUtils.getSharedPrefs(this.appContext);
        String string = sharedPrefs.getString(PREFKEY_INSTALLATION_UUID, null);
        return string == null ? createInstallationUUID(sharedPrefs) : string;
    }

    private String createInstallationUUID(SharedPreferences sharedPreferences) {
        this.installationIdLock.lock();
        try {
            String string = sharedPreferences.getString(PREFKEY_INSTALLATION_UUID, null);
            if (string == null) {
                string = formatId(UUID.randomUUID().toString());
                sharedPreferences.edit().putString(PREFKEY_INSTALLATION_UUID, string).commit();
            }
            return string;
        } finally {
            this.installationIdLock.unlock();
        }
    }

    public Map<DeviceIdentifierType, String> getDeviceIdentifiers() {
        HashMap hashMap = new HashMap();
        for (Kit kit : this.kits) {
            if (kit instanceof DeviceIdentifierProvider) {
                for (Entry entry : ((DeviceIdentifierProvider) kit).getDeviceIdentifiers().entrySet()) {
                    putNonNullIdInto(hashMap, (DeviceIdentifierType) entry.getKey(), (String) entry.getValue());
                }
            }
        }
        putNonNullIdInto(hashMap, DeviceIdentifierType.ANDROID_ID, getAndroidId());
        putNonNullIdInto(hashMap, DeviceIdentifierType.ANDROID_ADVERTISING_ID, getAdvertisingId());
        return Collections.unmodifiableMap(hashMap);
    }

    public String getInstallerPackageName() {
        return this.installerPackageNameProvider.getInstallerPackageName(this.appContext);
    }

    /* access modifiers changed from: 0000 */
    public synchronized AdvertisingInfo getAdvertisingInfo() {
        if (!this.fetchedAdvertisingInfo) {
            this.advertisingInfo = this.advertisingInfoProvider.getAdvertisingInfo();
            this.fetchedAdvertisingInfo = true;
        }
        return this.advertisingInfo;
    }

    public Boolean isLimitAdTrackingEnabled() {
        if (this.collectHardwareIds) {
            AdvertisingInfo advertisingInfo2 = getAdvertisingInfo();
            if (advertisingInfo2 != null) {
                return Boolean.valueOf(advertisingInfo2.limitAdTrackingEnabled);
            }
        }
        return null;
    }

    public String getAdvertisingId() {
        if (this.collectHardwareIds) {
            AdvertisingInfo advertisingInfo2 = getAdvertisingInfo();
            if (advertisingInfo2 != null) {
                return advertisingInfo2.advertisingId;
            }
        }
        return null;
    }

    private void putNonNullIdInto(Map<DeviceIdentifierType, String> map, DeviceIdentifierType deviceIdentifierType, String str) {
        if (str != null) {
            map.put(deviceIdentifierType, str);
        }
    }

    public String getAndroidId() {
        if (this.collectHardwareIds) {
            String string = Secure.getString(this.appContext.getContentResolver(), "android_id");
            if (!BAD_ANDROID_ID.equals(string)) {
                return formatId(string);
            }
        }
        return null;
    }
}
