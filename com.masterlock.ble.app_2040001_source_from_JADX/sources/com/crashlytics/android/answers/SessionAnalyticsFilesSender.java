package com.crashlytics.android.answers;

import java.io.File;
import java.util.List;
import p008io.fabric.sdk.android.Fabric;
import p008io.fabric.sdk.android.Kit;
import p008io.fabric.sdk.android.Logger;
import p008io.fabric.sdk.android.services.common.AbstractSpiCall;
import p008io.fabric.sdk.android.services.common.ResponseParser;
import p008io.fabric.sdk.android.services.events.FilesSender;
import p008io.fabric.sdk.android.services.network.HttpMethod;
import p008io.fabric.sdk.android.services.network.HttpRequest;
import p008io.fabric.sdk.android.services.network.HttpRequestFactory;

class SessionAnalyticsFilesSender extends AbstractSpiCall implements FilesSender {
    static final String FILE_CONTENT_TYPE = "application/vnd.crashlytics.android.events";
    static final String FILE_PARAM_NAME = "session_analytics_file_";
    private final String apiKey;

    public SessionAnalyticsFilesSender(Kit kit, String str, String str2, HttpRequestFactory httpRequestFactory, String str3) {
        super(kit, str, str2, httpRequestFactory, HttpMethod.POST);
        this.apiKey = str3;
    }

    public boolean send(List<File> list) {
        HttpRequest header = getHttpRequest().header(AbstractSpiCall.HEADER_CLIENT_TYPE, AbstractSpiCall.ANDROID_CLIENT_TYPE).header(AbstractSpiCall.HEADER_CLIENT_VERSION, this.kit.getVersion()).header(AbstractSpiCall.HEADER_API_KEY, this.apiKey);
        int i = 0;
        for (File file : list) {
            StringBuilder sb = new StringBuilder();
            sb.append(FILE_PARAM_NAME);
            sb.append(i);
            header.part(sb.toString(), file.getName(), FILE_CONTENT_TYPE, file);
            i++;
        }
        Logger logger = Fabric.getLogger();
        String str = Answers.TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Sending ");
        sb2.append(list.size());
        sb2.append(" analytics files to ");
        sb2.append(getUrl());
        logger.mo21793d(str, sb2.toString());
        int code = header.code();
        Logger logger2 = Fabric.getLogger();
        String str2 = Answers.TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Response code for analytics file send is ");
        sb3.append(code);
        logger2.mo21793d(str2, sb3.toString());
        if (ResponseParser.parse(code) == 0) {
            return true;
        }
        return false;
    }
}
