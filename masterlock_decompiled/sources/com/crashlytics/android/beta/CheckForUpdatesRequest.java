package com.crashlytics.android.beta;

import java.util.HashMap;
import java.util.Map;
import p008io.fabric.sdk.android.Kit;
import p008io.fabric.sdk.android.services.common.AbstractSpiCall;
import p008io.fabric.sdk.android.services.network.HttpMethod;
import p008io.fabric.sdk.android.services.network.HttpRequest;
import p008io.fabric.sdk.android.services.network.HttpRequestFactory;

class CheckForUpdatesRequest extends AbstractSpiCall {
    static final String BETA_SOURCE = "3";
    static final String BUILD_VERSION = "build_version";
    static final String DISPLAY_VERSION = "display_version";
    static final String HEADER_BETA_TOKEN = "X-CRASHLYTICS-BETA-TOKEN";
    static final String INSTANCE = "instance";
    static final String SDK_ANDROID_DIR_TOKEN_TYPE = "3";
    static final String SOURCE = "source";
    private final CheckForUpdatesResponseTransform responseTransform;

    static String createBetaTokenHeaderValueFor(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("3:");
        sb.append(str);
        return sb.toString();
    }

    public CheckForUpdatesRequest(Kit kit, String str, String str2, HttpRequestFactory httpRequestFactory, CheckForUpdatesResponseTransform checkForUpdatesResponseTransform) {
        super(kit, str, str2, httpRequestFactory, HttpMethod.GET);
        this.responseTransform = checkForUpdatesResponseTransform;
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x00e7  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x010b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.crashlytics.android.beta.CheckForUpdatesResponse invoke(java.lang.String r5, java.lang.String r6, com.crashlytics.android.beta.BuildProperties r7) {
        /*
            r4 = this;
            r0 = 0
            java.util.Map r7 = r4.getQueryParamsFor(r7)     // Catch:{ Exception -> 0x00c5, all -> 0x00c2 }
            io.fabric.sdk.android.services.network.HttpRequest r1 = r4.getHttpRequest(r7)     // Catch:{ Exception -> 0x00c5, all -> 0x00c2 }
            io.fabric.sdk.android.services.network.HttpRequest r5 = r4.applyHeadersTo(r1, r5, r6)     // Catch:{ Exception -> 0x00bf, all -> 0x00bc }
            io.fabric.sdk.android.Logger r6 = p008io.fabric.sdk.android.Fabric.getLogger()     // Catch:{ Exception -> 0x00ba }
            java.lang.String r1 = "Beta"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00ba }
            r2.<init>()     // Catch:{ Exception -> 0x00ba }
            java.lang.String r3 = "Checking for updates from "
            r2.append(r3)     // Catch:{ Exception -> 0x00ba }
            java.lang.String r3 = r4.getUrl()     // Catch:{ Exception -> 0x00ba }
            r2.append(r3)     // Catch:{ Exception -> 0x00ba }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x00ba }
            r6.mo21793d(r1, r2)     // Catch:{ Exception -> 0x00ba }
            io.fabric.sdk.android.Logger r6 = p008io.fabric.sdk.android.Fabric.getLogger()     // Catch:{ Exception -> 0x00ba }
            java.lang.String r1 = "Beta"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00ba }
            r2.<init>()     // Catch:{ Exception -> 0x00ba }
            java.lang.String r3 = "Checking for updates query params are: "
            r2.append(r3)     // Catch:{ Exception -> 0x00ba }
            r2.append(r7)     // Catch:{ Exception -> 0x00ba }
            java.lang.String r7 = r2.toString()     // Catch:{ Exception -> 0x00ba }
            r6.mo21793d(r1, r7)     // Catch:{ Exception -> 0x00ba }
            boolean r6 = r5.mo23102ok()     // Catch:{ Exception -> 0x00ba }
            if (r6 == 0) goto L_0x0088
            io.fabric.sdk.android.Logger r6 = p008io.fabric.sdk.android.Fabric.getLogger()     // Catch:{ Exception -> 0x00ba }
            java.lang.String r7 = "Beta"
            java.lang.String r1 = "Checking for updates was successful"
            r6.mo21793d(r7, r1)     // Catch:{ Exception -> 0x00ba }
            org.json.JSONObject r6 = new org.json.JSONObject     // Catch:{ Exception -> 0x00ba }
            java.lang.String r7 = r5.body()     // Catch:{ Exception -> 0x00ba }
            r6.<init>(r7)     // Catch:{ Exception -> 0x00ba }
            com.crashlytics.android.beta.CheckForUpdatesResponseTransform r7 = r4.responseTransform     // Catch:{ Exception -> 0x00ba }
            com.crashlytics.android.beta.CheckForUpdatesResponse r6 = r7.fromJson(r6)     // Catch:{ Exception -> 0x00ba }
            if (r5 == 0) goto L_0x0087
            java.lang.String r7 = "X-REQUEST-ID"
            java.lang.String r5 = r5.header(r7)
            io.fabric.sdk.android.Logger r7 = p008io.fabric.sdk.android.Fabric.getLogger()
            java.lang.String r0 = "Fabric"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Checking for updates request ID: "
            r1.append(r2)
            r1.append(r5)
            java.lang.String r5 = r1.toString()
            r7.mo21793d(r0, r5)
        L_0x0087:
            return r6
        L_0x0088:
            io.fabric.sdk.android.Logger r6 = p008io.fabric.sdk.android.Fabric.getLogger()     // Catch:{ Exception -> 0x00ba }
            java.lang.String r7 = "Beta"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00ba }
            r1.<init>()     // Catch:{ Exception -> 0x00ba }
            java.lang.String r2 = "Checking for updates failed. Response code: "
            r1.append(r2)     // Catch:{ Exception -> 0x00ba }
            int r2 = r5.code()     // Catch:{ Exception -> 0x00ba }
            r1.append(r2)     // Catch:{ Exception -> 0x00ba }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x00ba }
            r6.mo21795e(r7, r1)     // Catch:{ Exception -> 0x00ba }
            if (r5 == 0) goto L_0x0107
            java.lang.String r6 = "X-REQUEST-ID"
            java.lang.String r5 = r5.header(r6)
            io.fabric.sdk.android.Logger r6 = p008io.fabric.sdk.android.Fabric.getLogger()
            java.lang.String r7 = "Fabric"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            goto L_0x00f8
        L_0x00ba:
            r6 = move-exception
            goto L_0x00c7
        L_0x00bc:
            r6 = move-exception
            r5 = r1
            goto L_0x0109
        L_0x00bf:
            r6 = move-exception
            r5 = r1
            goto L_0x00c7
        L_0x00c2:
            r6 = move-exception
            r5 = r0
            goto L_0x0109
        L_0x00c5:
            r6 = move-exception
            r5 = r0
        L_0x00c7:
            io.fabric.sdk.android.Logger r7 = p008io.fabric.sdk.android.Fabric.getLogger()     // Catch:{ all -> 0x0108 }
            java.lang.String r1 = "Beta"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0108 }
            r2.<init>()     // Catch:{ all -> 0x0108 }
            java.lang.String r3 = "Error while checking for updates from "
            r2.append(r3)     // Catch:{ all -> 0x0108 }
            java.lang.String r3 = r4.getUrl()     // Catch:{ all -> 0x0108 }
            r2.append(r3)     // Catch:{ all -> 0x0108 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0108 }
            r7.mo21796e(r1, r2, r6)     // Catch:{ all -> 0x0108 }
            if (r5 == 0) goto L_0x0107
            java.lang.String r6 = "X-REQUEST-ID"
            java.lang.String r5 = r5.header(r6)
            io.fabric.sdk.android.Logger r6 = p008io.fabric.sdk.android.Fabric.getLogger()
            java.lang.String r7 = "Fabric"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
        L_0x00f8:
            java.lang.String r2 = "Checking for updates request ID: "
            r1.append(r2)
            r1.append(r5)
            java.lang.String r5 = r1.toString()
            r6.mo21793d(r7, r5)
        L_0x0107:
            return r0
        L_0x0108:
            r6 = move-exception
        L_0x0109:
            if (r5 == 0) goto L_0x012b
            java.lang.String r7 = "X-REQUEST-ID"
            java.lang.String r5 = r5.header(r7)
            io.fabric.sdk.android.Logger r7 = p008io.fabric.sdk.android.Fabric.getLogger()
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Checking for updates request ID: "
            r0.append(r1)
            r0.append(r5)
            java.lang.String r5 = r0.toString()
            java.lang.String r0 = "Fabric"
            r7.mo21793d(r0, r5)
        L_0x012b:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.crashlytics.android.beta.CheckForUpdatesRequest.invoke(java.lang.String, java.lang.String, com.crashlytics.android.beta.BuildProperties):com.crashlytics.android.beta.CheckForUpdatesResponse");
    }

    private HttpRequest applyHeadersTo(HttpRequest httpRequest, String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(AbstractSpiCall.CRASHLYTICS_USER_AGENT);
        sb.append(this.kit.getVersion());
        return httpRequest.header("Accept", "application/json").header("User-Agent", sb.toString()).header(AbstractSpiCall.HEADER_DEVELOPER_TOKEN, "470fa2b4ae81cd56ecbcda9735803434cec591fa").header(AbstractSpiCall.HEADER_CLIENT_TYPE, AbstractSpiCall.ANDROID_CLIENT_TYPE).header(AbstractSpiCall.HEADER_CLIENT_VERSION, this.kit.getVersion()).header(AbstractSpiCall.HEADER_API_KEY, str).header(HEADER_BETA_TOKEN, createBetaTokenHeaderValueFor(str2));
    }

    private Map<String, String> getQueryParamsFor(BuildProperties buildProperties) {
        HashMap hashMap = new HashMap();
        hashMap.put(BUILD_VERSION, buildProperties.versionCode);
        hashMap.put(DISPLAY_VERSION, buildProperties.versionName);
        hashMap.put(INSTANCE, buildProperties.buildId);
        hashMap.put(SOURCE, "3");
        return hashMap;
    }
}
