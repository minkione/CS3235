package com.masterlock.api.module;

import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.masterlock.api.client.AccountClient;
import com.masterlock.api.client.GuestClient;
import com.masterlock.api.client.KMSDeviceClient;
import com.masterlock.api.client.KMSDeviceLogClient;
import com.masterlock.api.client.ProductClient;
import com.masterlock.api.client.ProductInvitationClient;
import com.masterlock.api.client.TermsOfServiceClient;
import com.masterlock.api.client.TimezoneClient;
import com.masterlock.api.util.ApiErrorHandler;
import com.masterlock.api.util.DateDeserializer;
import com.masterlock.api.util.IResourceWrapper;
import com.squareup.okhttp.CertificatePinner;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import retrofit.RequestInterceptor;
import retrofit.RequestInterceptor.RequestFacade;
import retrofit.RestAdapter;
import retrofit.RestAdapter.Builder;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module(complete = false, injects = {}, library = true)
public class ApiModule {
    private final String apiKey;
    private final String appVersion;
    private final String endPoint;
    private final String hostName;
    private final String[] pinCertificates;

    public ApiModule(String str, String str2, String str3, String[] strArr, String str4) {
        this.endPoint = str;
        this.apiKey = str2;
        this.appVersion = str4;
        this.hostName = str3;
        this.pinCertificates = strArr;
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public Gson providesGson() {
        return new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Date.class, new DateDeserializer()).create();
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public RequestInterceptor providesRequestInterceptor() {
        return getDeviceLocale(this.appVersion);
    }

    public static RequestInterceptor getDeviceLocale(final String str) {
        return new RequestInterceptor() {
            public void intercept(RequestFacade requestFacade) {
                requestFacade.addHeader(HttpHeaders.ACCEPT_LANGUAGE, Locale.getDefault().getLanguage());
                requestFacade.addHeader("User-Agent", "com.masterlock.android");
                requestFacade.addHeader("AppVersion", str);
            }
        };
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public RestAdapter providesAdapter(Gson gson, RequestInterceptor requestInterceptor, ApiErrorHandler apiErrorHandler) {
        OkHttpClient okHttpClient = new OkHttpClient();
        CertificatePinner createCertificatePinner = createCertificatePinner();
        if (createCertificatePinner != null) {
            okHttpClient.setCertificatePinner(createCertificatePinner);
        }
        okHttpClient.setConnectTimeout(45000, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(45000, TimeUnit.MILLISECONDS);
        return new Builder().setEndpoint(this.endPoint).setClient((Client) new OkClient(okHttpClient)).setLogLevel(LogLevel.FULL).setRequestInterceptor(requestInterceptor).setErrorHandler(apiErrorHandler).setConverter(new GsonConverter(gson)).build();
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public AccountClient provideAccountClient(RestAdapter restAdapter) {
        return (AccountClient) restAdapter.create(AccountClient.class);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public ProductClient provideProductClient(RestAdapter restAdapter) {
        return (ProductClient) restAdapter.create(ProductClient.class);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public TermsOfServiceClient provideTermsOfServiceClient(RestAdapter restAdapter) {
        return (TermsOfServiceClient) restAdapter.create(TermsOfServiceClient.class);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public KMSDeviceClient provideKMSDeviceClient(RestAdapter restAdapter) {
        return (KMSDeviceClient) restAdapter.create(KMSDeviceClient.class);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public KMSDeviceLogClient provideKMSDeviceLogClient(RestAdapter restAdapter) {
        return (KMSDeviceLogClient) restAdapter.create(KMSDeviceLogClient.class);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public GuestClient providesGuestClient(RestAdapter restAdapter) {
        return (GuestClient) restAdapter.create(GuestClient.class);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public ProductInvitationClient providesProductInvitationClient(RestAdapter restAdapter) {
        return (ProductInvitationClient) restAdapter.create(ProductInvitationClient.class);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public ApiErrorHandler providesApiErrorHandler(IResourceWrapper iResourceWrapper) {
        return new ApiErrorHandler(iResourceWrapper);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public TimezoneClient provideTimezoneClient(RestAdapter restAdapter) {
        return (TimezoneClient) restAdapter.create(TimezoneClient.class);
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x002e  */
    /* JADX WARNING: Removed duplicated region for block: B:14:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.squareup.okhttp.CertificatePinner createCertificatePinner() {
        /*
            r9 = this;
            java.lang.String r0 = r9.hostName
            boolean r0 = com.google.common.base.Strings.isNullOrEmpty(r0)
            r1 = 0
            if (r0 != 0) goto L_0x002b
            java.lang.String[] r0 = r9.pinCertificates
            if (r0 == 0) goto L_0x002b
            int r0 = r0.length
            if (r0 <= 0) goto L_0x002b
            com.squareup.okhttp.CertificatePinner$Builder r0 = new com.squareup.okhttp.CertificatePinner$Builder
            r0.<init>()
            java.lang.String[] r2 = r9.pinCertificates
            int r3 = r2.length
            r4 = 0
            r5 = 0
        L_0x001a:
            if (r5 >= r3) goto L_0x002c
            r6 = r2[r5]
            java.lang.String r7 = r9.hostName
            r8 = 1
            java.lang.String[] r8 = new java.lang.String[r8]
            r8[r4] = r6
            r0.add(r7, r8)
            int r5 = r5 + 1
            goto L_0x001a
        L_0x002b:
            r0 = r1
        L_0x002c:
            if (r0 == 0) goto L_0x0032
            com.squareup.okhttp.CertificatePinner r1 = r0.build()
        L_0x0032:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.masterlock.api.module.ApiModule.createCertificatePinner():com.squareup.okhttp.CertificatePinner");
    }
}
