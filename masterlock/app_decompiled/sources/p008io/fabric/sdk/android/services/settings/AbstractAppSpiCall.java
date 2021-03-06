package p008io.fabric.sdk.android.services.settings;

import android.content.res.Resources.NotFoundException;
import java.io.InputStream;
import java.util.Locale;
import p008io.fabric.sdk.android.Fabric;
import p008io.fabric.sdk.android.Kit;
import p008io.fabric.sdk.android.KitInfo;
import p008io.fabric.sdk.android.Logger;
import p008io.fabric.sdk.android.services.common.AbstractSpiCall;
import p008io.fabric.sdk.android.services.common.CommonUtils;
import p008io.fabric.sdk.android.services.common.ResponseParser;
import p008io.fabric.sdk.android.services.network.HttpMethod;
import p008io.fabric.sdk.android.services.network.HttpRequest;
import p008io.fabric.sdk.android.services.network.HttpRequestFactory;

/* renamed from: io.fabric.sdk.android.services.settings.AbstractAppSpiCall */
abstract class AbstractAppSpiCall extends AbstractSpiCall implements AppSpiCall {
    public static final String APP_BUILD_VERSION_PARAM = "app[build_version]";
    public static final String APP_BUILT_SDK_VERSION_PARAM = "app[built_sdk_version]";
    public static final String APP_DISPLAY_VERSION_PARAM = "app[display_version]";
    public static final String APP_ICON_DATA_PARAM = "app[icon][data]";
    public static final String APP_ICON_HASH_PARAM = "app[icon][hash]";
    public static final String APP_ICON_HEIGHT_PARAM = "app[icon][height]";
    public static final String APP_ICON_PRERENDERED_PARAM = "app[icon][prerendered]";
    public static final String APP_ICON_WIDTH_PARAM = "app[icon][width]";
    public static final String APP_IDENTIFIER_PARAM = "app[identifier]";
    public static final String APP_INSTANCE_IDENTIFIER_PARAM = "app[instance_identifier]";
    public static final String APP_MIN_SDK_VERSION_PARAM = "app[minimum_sdk_version]";
    public static final String APP_NAME_PARAM = "app[name]";
    public static final String APP_SDK_MODULES_PARAM_BUILD_TYPE = "app[build][libraries][%s][type]";
    public static final String APP_SDK_MODULES_PARAM_PREFIX = "app[build][libraries][%s]";
    public static final String APP_SDK_MODULES_PARAM_VERSION = "app[build][libraries][%s][version]";
    public static final String APP_SOURCE_PARAM = "app[source]";
    static final String ICON_CONTENT_TYPE = "application/octet-stream";
    static final String ICON_FILE_NAME = "icon.png";

    public AbstractAppSpiCall(Kit kit, String str, String str2, HttpRequestFactory httpRequestFactory, HttpMethod httpMethod) {
        super(kit, str, str2, httpRequestFactory, httpMethod);
    }

    public boolean invoke(AppRequestData appRequestData) {
        HttpRequest applyMultipartDataTo = applyMultipartDataTo(applyHeadersTo(getHttpRequest(), appRequestData), appRequestData);
        Logger logger = Fabric.getLogger();
        String str = Fabric.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Sending app info to ");
        sb.append(getUrl());
        logger.mo21793d(str, sb.toString());
        if (appRequestData.icon != null) {
            Logger logger2 = Fabric.getLogger();
            String str2 = Fabric.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("App icon hash is ");
            sb2.append(appRequestData.icon.hash);
            logger2.mo21793d(str2, sb2.toString());
            Logger logger3 = Fabric.getLogger();
            String str3 = Fabric.TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("App icon size is ");
            sb3.append(appRequestData.icon.width);
            sb3.append("x");
            sb3.append(appRequestData.icon.height);
            logger3.mo21793d(str3, sb3.toString());
        }
        int code = applyMultipartDataTo.code();
        String str4 = HttpRequest.METHOD_POST.equals(applyMultipartDataTo.method()) ? "Create" : "Update";
        Logger logger4 = Fabric.getLogger();
        String str5 = Fabric.TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append(str4);
        sb4.append(" app request ID: ");
        sb4.append(applyMultipartDataTo.header(AbstractSpiCall.HEADER_REQUEST_ID));
        logger4.mo21793d(str5, sb4.toString());
        Logger logger5 = Fabric.getLogger();
        String str6 = Fabric.TAG;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("Result was ");
        sb5.append(code);
        logger5.mo21793d(str6, sb5.toString());
        return ResponseParser.parse(code) == 0;
    }

    private HttpRequest applyHeadersTo(HttpRequest httpRequest, AppRequestData appRequestData) {
        return httpRequest.header(AbstractSpiCall.HEADER_API_KEY, appRequestData.apiKey).header(AbstractSpiCall.HEADER_CLIENT_TYPE, AbstractSpiCall.ANDROID_CLIENT_TYPE).header(AbstractSpiCall.HEADER_CLIENT_VERSION, this.kit.getVersion());
    }

    private HttpRequest applyMultipartDataTo(HttpRequest httpRequest, AppRequestData appRequestData) {
        HttpRequest part = httpRequest.part(APP_IDENTIFIER_PARAM, appRequestData.appId).part(APP_NAME_PARAM, appRequestData.name).part(APP_DISPLAY_VERSION_PARAM, appRequestData.displayVersion).part(APP_BUILD_VERSION_PARAM, appRequestData.buildVersion).part(APP_SOURCE_PARAM, (Number) Integer.valueOf(appRequestData.source)).part(APP_MIN_SDK_VERSION_PARAM, appRequestData.minSdkVersion).part(APP_BUILT_SDK_VERSION_PARAM, appRequestData.builtSdkVersion);
        if (!CommonUtils.isNullOrEmpty(appRequestData.instanceIdentifier)) {
            part.part(APP_INSTANCE_IDENTIFIER_PARAM, appRequestData.instanceIdentifier);
        }
        if (appRequestData.icon != null) {
            InputStream inputStream = null;
            try {
                inputStream = this.kit.getContext().getResources().openRawResource(appRequestData.icon.iconResourceId);
                part.part(APP_ICON_HASH_PARAM, appRequestData.icon.hash).part(APP_ICON_DATA_PARAM, ICON_FILE_NAME, ICON_CONTENT_TYPE, inputStream).part(APP_ICON_WIDTH_PARAM, (Number) Integer.valueOf(appRequestData.icon.width)).part(APP_ICON_HEIGHT_PARAM, (Number) Integer.valueOf(appRequestData.icon.height));
            } catch (NotFoundException e) {
                Logger logger = Fabric.getLogger();
                String str = Fabric.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Failed to find app icon with resource ID: ");
                sb.append(appRequestData.icon.iconResourceId);
                logger.mo21796e(str, sb.toString(), e);
            } catch (Throwable th) {
                CommonUtils.closeOrLog(inputStream, "Failed to close app icon InputStream.");
                throw th;
            }
            CommonUtils.closeOrLog(inputStream, "Failed to close app icon InputStream.");
        }
        if (appRequestData.sdkKits != null) {
            for (KitInfo kitInfo : appRequestData.sdkKits) {
                part.part(getKitVersionKey(kitInfo), kitInfo.getVersion());
                part.part(getKitBuildTypeKey(kitInfo), kitInfo.getBuildType());
            }
        }
        return part;
    }

    /* access modifiers changed from: 0000 */
    public String getKitVersionKey(KitInfo kitInfo) {
        return String.format(Locale.US, APP_SDK_MODULES_PARAM_VERSION, new Object[]{kitInfo.getIdentifier()});
    }

    /* access modifiers changed from: 0000 */
    public String getKitBuildTypeKey(KitInfo kitInfo) {
        return String.format(Locale.US, APP_SDK_MODULES_PARAM_BUILD_TYPE, new Object[]{kitInfo.getIdentifier()});
    }
}
