package p008io.fabric.sdk.android.services.settings;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import p008io.fabric.sdk.android.Fabric;
import p008io.fabric.sdk.android.Kit;
import p008io.fabric.sdk.android.Logger;
import p008io.fabric.sdk.android.services.common.AbstractSpiCall;
import p008io.fabric.sdk.android.services.common.CommonUtils;
import p008io.fabric.sdk.android.services.network.HttpMethod;
import p008io.fabric.sdk.android.services.network.HttpRequest;
import p008io.fabric.sdk.android.services.network.HttpRequestFactory;

/* renamed from: io.fabric.sdk.android.services.settings.DefaultSettingsSpiCall */
class DefaultSettingsSpiCall extends AbstractSpiCall implements SettingsSpiCall {
    static final String BUILD_VERSION_PARAM = "build_version";
    static final String DISPLAY_VERSION_PARAM = "display_version";
    static final String HEADER_ADVERTISING_TOKEN = "X-CRASHLYTICS-ADVERTISING-TOKEN";
    static final String HEADER_ANDROID_ID = "X-CRASHLYTICS-ANDROID-ID";
    static final String HEADER_DEVICE_MODEL = "X-CRASHLYTICS-DEVICE-MODEL";
    static final String HEADER_INSTALLATION_ID = "X-CRASHLYTICS-INSTALLATION-ID";
    static final String HEADER_OS_BUILD_VERSION = "X-CRASHLYTICS-OS-BUILD-VERSION";
    static final String HEADER_OS_DISPLAY_VERSION = "X-CRASHLYTICS-OS-DISPLAY-VERSION";
    static final String ICON_HASH = "icon_hash";
    static final String INSTANCE_PARAM = "instance";
    static final String SOURCE_PARAM = "source";

    /* access modifiers changed from: 0000 */
    public boolean requestWasSuccessful(int i) {
        return i == 200 || i == 201 || i == 202 || i == 203;
    }

    public DefaultSettingsSpiCall(Kit kit, String str, String str2, HttpRequestFactory httpRequestFactory) {
        this(kit, str, str2, httpRequestFactory, HttpMethod.GET);
    }

    DefaultSettingsSpiCall(Kit kit, String str, String str2, HttpRequestFactory httpRequestFactory, HttpMethod httpMethod) {
        super(kit, str, str2, httpRequestFactory, httpMethod);
    }

    public JSONObject invoke(SettingsRequest settingsRequest) {
        HttpRequest httpRequest = null;
        try {
            Map queryParamsFor = getQueryParamsFor(settingsRequest);
            HttpRequest applyHeadersTo = applyHeadersTo(getHttpRequest(queryParamsFor), settingsRequest);
            Logger logger = Fabric.getLogger();
            String str = Fabric.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Requesting settings from ");
            sb.append(getUrl());
            logger.mo21793d(str, sb.toString());
            Logger logger2 = Fabric.getLogger();
            String str2 = Fabric.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Settings query params were: ");
            sb2.append(queryParamsFor);
            logger2.mo21793d(str2, sb2.toString());
            JSONObject handleResponse = handleResponse(applyHeadersTo);
            if (applyHeadersTo != null) {
                Logger logger3 = Fabric.getLogger();
                String str3 = Fabric.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Settings request ID: ");
                sb3.append(applyHeadersTo.header(AbstractSpiCall.HEADER_REQUEST_ID));
                logger3.mo21793d(str3, sb3.toString());
            }
            return handleResponse;
        } catch (Throwable th) {
            if (httpRequest != null) {
                Logger logger4 = Fabric.getLogger();
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Settings request ID: ");
                sb4.append(httpRequest.header(AbstractSpiCall.HEADER_REQUEST_ID));
                logger4.mo21793d(Fabric.TAG, sb4.toString());
            }
            throw th;
        }
    }

    /* access modifiers changed from: 0000 */
    public JSONObject handleResponse(HttpRequest httpRequest) {
        int code = httpRequest.code();
        Logger logger = Fabric.getLogger();
        String str = Fabric.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Settings result was: ");
        sb.append(code);
        logger.mo21793d(str, sb.toString());
        if (requestWasSuccessful(code)) {
            return getJsonObjectFrom(httpRequest.body());
        }
        Logger logger2 = Fabric.getLogger();
        String str2 = Fabric.TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Failed to retrieve settings from ");
        sb2.append(getUrl());
        logger2.mo21795e(str2, sb2.toString());
        return null;
    }

    private JSONObject getJsonObjectFrom(String str) {
        try {
            return new JSONObject(str);
        } catch (Exception e) {
            Logger logger = Fabric.getLogger();
            String str2 = Fabric.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to parse settings JSON from ");
            sb.append(getUrl());
            logger.mo21794d(str2, sb.toString(), e);
            Logger logger2 = Fabric.getLogger();
            String str3 = Fabric.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Settings response ");
            sb2.append(str);
            logger2.mo21793d(str3, sb2.toString());
            return null;
        }
    }

    private Map<String, String> getQueryParamsFor(SettingsRequest settingsRequest) {
        HashMap hashMap = new HashMap();
        hashMap.put(BUILD_VERSION_PARAM, settingsRequest.buildVersion);
        hashMap.put(DISPLAY_VERSION_PARAM, settingsRequest.displayVersion);
        hashMap.put(SOURCE_PARAM, Integer.toString(settingsRequest.source));
        if (settingsRequest.iconHash != null) {
            hashMap.put(ICON_HASH, settingsRequest.iconHash);
        }
        String str = settingsRequest.instanceId;
        if (!CommonUtils.isNullOrEmpty(str)) {
            hashMap.put(INSTANCE_PARAM, str);
        }
        return hashMap;
    }

    private HttpRequest applyHeadersTo(HttpRequest httpRequest, SettingsRequest settingsRequest) {
        applyNonNullHeader(httpRequest, AbstractSpiCall.HEADER_API_KEY, settingsRequest.apiKey);
        applyNonNullHeader(httpRequest, AbstractSpiCall.HEADER_CLIENT_TYPE, AbstractSpiCall.ANDROID_CLIENT_TYPE);
        applyNonNullHeader(httpRequest, AbstractSpiCall.HEADER_CLIENT_VERSION, this.kit.getVersion());
        applyNonNullHeader(httpRequest, "Accept", "application/json");
        applyNonNullHeader(httpRequest, HEADER_DEVICE_MODEL, settingsRequest.deviceModel);
        applyNonNullHeader(httpRequest, HEADER_OS_BUILD_VERSION, settingsRequest.osBuildVersion);
        applyNonNullHeader(httpRequest, HEADER_OS_DISPLAY_VERSION, settingsRequest.osDisplayVersion);
        applyNonNullHeader(httpRequest, HEADER_ADVERTISING_TOKEN, settingsRequest.advertisingId);
        applyNonNullHeader(httpRequest, HEADER_INSTALLATION_ID, settingsRequest.installationId);
        applyNonNullHeader(httpRequest, HEADER_ANDROID_ID, settingsRequest.androidId);
        return httpRequest;
    }

    private void applyNonNullHeader(HttpRequest httpRequest, String str, String str2) {
        if (str2 != null) {
            httpRequest.header(str, str2);
        }
    }
}
