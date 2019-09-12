package com.crashlytics.android.core;

import java.util.Map.Entry;
import p008io.fabric.sdk.android.Fabric;
import p008io.fabric.sdk.android.Kit;
import p008io.fabric.sdk.android.Logger;
import p008io.fabric.sdk.android.services.common.AbstractSpiCall;
import p008io.fabric.sdk.android.services.common.ResponseParser;
import p008io.fabric.sdk.android.services.network.HttpMethod;
import p008io.fabric.sdk.android.services.network.HttpRequest;
import p008io.fabric.sdk.android.services.network.HttpRequestFactory;

class DefaultCreateReportSpiCall extends AbstractSpiCall implements CreateReportSpiCall {
    static final String FILE_CONTENT_TYPE = "application/octet-stream";
    static final String FILE_PARAM = "report[file]";
    static final String IDENTIFIER_PARAM = "report[identifier]";

    public DefaultCreateReportSpiCall(Kit kit, String str, String str2, HttpRequestFactory httpRequestFactory) {
        super(kit, str, str2, httpRequestFactory, HttpMethod.POST);
    }

    DefaultCreateReportSpiCall(Kit kit, String str, String str2, HttpRequestFactory httpRequestFactory, HttpMethod httpMethod) {
        super(kit, str, str2, httpRequestFactory, httpMethod);
    }

    public boolean invoke(CreateReportRequest createReportRequest) {
        HttpRequest applyMultipartDataTo = applyMultipartDataTo(applyHeadersTo(getHttpRequest(), createReportRequest), createReportRequest);
        Logger logger = Fabric.getLogger();
        String str = CrashlyticsCore.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Sending report to: ");
        sb.append(getUrl());
        logger.mo21793d(str, sb.toString());
        int code = applyMultipartDataTo.code();
        Logger logger2 = Fabric.getLogger();
        String str2 = CrashlyticsCore.TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Create report request ID: ");
        sb2.append(applyMultipartDataTo.header(AbstractSpiCall.HEADER_REQUEST_ID));
        logger2.mo21793d(str2, sb2.toString());
        Logger logger3 = Fabric.getLogger();
        String str3 = CrashlyticsCore.TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Result was: ");
        sb3.append(code);
        logger3.mo21793d(str3, sb3.toString());
        return ResponseParser.parse(code) == 0;
    }

    private HttpRequest applyHeadersTo(HttpRequest httpRequest, CreateReportRequest createReportRequest) {
        HttpRequest header = httpRequest.header(AbstractSpiCall.HEADER_API_KEY, createReportRequest.apiKey).header(AbstractSpiCall.HEADER_CLIENT_TYPE, AbstractSpiCall.ANDROID_CLIENT_TYPE).header(AbstractSpiCall.HEADER_CLIENT_VERSION, CrashlyticsCore.getInstance().getVersion());
        for (Entry header2 : createReportRequest.report.getCustomHeaders().entrySet()) {
            header = header.header(header2);
        }
        return header;
    }

    private HttpRequest applyMultipartDataTo(HttpRequest httpRequest, CreateReportRequest createReportRequest) {
        Report report = createReportRequest.report;
        return httpRequest.part(FILE_PARAM, report.getFileName(), FILE_CONTENT_TYPE, report.getFile()).part(IDENTIFIER_PARAM, report.getIdentifier());
    }
}
