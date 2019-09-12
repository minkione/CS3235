package com.masterlock.api.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import p008io.fabric.sdk.android.services.events.EventsFilesManager;
import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedInput;

public class LocalApiClient implements Client {
    private String scenario = "";

    private static class TypedInputStream implements TypedInput {
        private final long length;
        private final String mimeType;
        private final InputStream stream;

        private TypedInputStream(String str, long j, InputStream inputStream) {
            this.mimeType = str;
            this.length = j;
            this.stream = inputStream;
        }

        public String mimeType() {
            return this.mimeType;
        }

        public long length() {
            return this.length;
        }

        /* renamed from: in */
        public InputStream mo16704in() throws IOException {
            return this.stream;
        }
    }

    public void setScenario(String str) {
        this.scenario = str;
    }

    public Response execute(Request request) throws IOException {
        String str = this.scenario;
        if (str == null || "".equals(str)) {
            String path = new URL(request.getUrl()).getPath();
            String lowerCase = request.getMethod().toLowerCase();
            StringBuilder sb = new StringBuilder();
            sb.append(this.scenario);
            sb.append(lowerCase);
            sb.append(path.replace("/", EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR));
            sb.append(".json");
            this.scenario = sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("/");
        sb2.append(this.scenario);
        InputStream resourceAsStream = LocalApiClient.class.getResourceAsStream(sb2.toString());
        if (resourceAsStream != null) {
            String guessContentTypeFromStream = URLConnection.guessContentTypeFromStream(resourceAsStream);
            TypedInputStream typedInputStream = new TypedInputStream(guessContentTypeFromStream == null ? "application/json" : guessContentTypeFromStream, (long) resourceAsStream.available(), resourceAsStream);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("local-resources/resources/");
            sb3.append(this.scenario);
            new ArrayList().add(new Header("Content from", sb3.toString()));
            this.scenario = "";
            String url = request.getUrl();
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Content from res/raw/");
            sb4.append(this.scenario);
            Response response = new Response(url, 200, sb4.toString(), new ArrayList(), typedInputStream);
            return response;
        }
        StringBuilder sb5 = new StringBuilder();
        sb5.append("Unable to find resource named: ");
        sb5.append(this.scenario);
        throw new NullPointerException(sb5.toString());
    }
}
