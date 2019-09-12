package com.squareup.okhttp.internal.huc;

import com.google.common.net.HttpHeaders;
import com.squareup.okhttp.Connection;
import com.squareup.okhttp.Handshake;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Headers.Builder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Route;
import com.squareup.okhttp.internal.Internal;
import com.squareup.okhttp.internal.Platform;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.http.HttpDate;
import com.squareup.okhttp.internal.http.HttpEngine;
import com.squareup.okhttp.internal.http.HttpMethod;
import com.squareup.okhttp.internal.http.OkHeaders;
import com.squareup.okhttp.internal.http.RetryableSink;
import com.squareup.okhttp.internal.http.StatusLine;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.SocketPermission;
import java.net.URL;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import okio.BufferedSink;
import okio.Sink;
import p008io.fabric.sdk.android.services.network.HttpRequest;

public class HttpURLConnectionImpl extends HttpURLConnection {
    private static final Set<String> METHODS = new LinkedHashSet(Arrays.asList(new String[]{HttpRequest.METHOD_OPTIONS, HttpRequest.METHOD_GET, HttpRequest.METHOD_HEAD, HttpRequest.METHOD_POST, HttpRequest.METHOD_PUT, HttpRequest.METHOD_DELETE, HttpRequest.METHOD_TRACE, "PATCH"}));
    final OkHttpClient client;
    private long fixedContentLength = -1;
    private int followUpCount;
    Handshake handshake;
    protected HttpEngine httpEngine;
    protected IOException httpEngineFailure;
    private Builder requestHeaders = new Builder();
    private Headers responseHeaders;
    private Route route;

    public HttpURLConnectionImpl(URL url, OkHttpClient okHttpClient) {
        super(url);
        this.client = okHttpClient;
    }

    public final void connect() throws IOException {
        initHttpEngine();
        do {
        } while (!execute(false));
    }

    public final void disconnect() {
        HttpEngine httpEngine2 = this.httpEngine;
        if (httpEngine2 != null) {
            httpEngine2.disconnect();
        }
    }

    public final InputStream getErrorStream() {
        try {
            HttpEngine response = getResponse();
            if (!HttpEngine.hasBody(response.getResponse()) || response.getResponse().code() < 400) {
                return null;
            }
            return response.getResponse().body().byteStream();
        } catch (IOException unused) {
            return null;
        }
    }

    private Headers getHeaders() throws IOException {
        if (this.responseHeaders == null) {
            Response response = getResponse().getResponse();
            Builder newBuilder = response.headers().newBuilder();
            StringBuilder sb = new StringBuilder();
            sb.append(Platform.get().getPrefix());
            sb.append("-Response-Source");
            this.responseHeaders = newBuilder.add(sb.toString(), responseSourceHeader(response)).build();
        }
        return this.responseHeaders;
    }

    private static String responseSourceHeader(Response response) {
        if (response.networkResponse() == null) {
            if (response.cacheResponse() == null) {
                return "NONE";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("CACHE ");
            sb.append(response.code());
            return sb.toString();
        } else if (response.cacheResponse() == null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("NETWORK ");
            sb2.append(response.code());
            return sb2.toString();
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("CONDITIONAL_CACHE ");
            sb3.append(response.networkResponse().code());
            return sb3.toString();
        }
    }

    public final String getHeaderField(int i) {
        try {
            return getHeaders().value(i);
        } catch (IOException unused) {
            return null;
        }
    }

    public final String getHeaderField(String str) {
        String str2;
        if (str == null) {
            try {
                str2 = StatusLine.get(getResponse().getResponse()).toString();
            } catch (IOException unused) {
                return null;
            }
        } else {
            str2 = getHeaders().get(str);
        }
        return str2;
    }

    public final String getHeaderFieldKey(int i) {
        try {
            return getHeaders().name(i);
        } catch (IOException unused) {
            return null;
        }
    }

    public final Map<String, List<String>> getHeaderFields() {
        try {
            return OkHeaders.toMultimap(getHeaders(), StatusLine.get(getResponse().getResponse()).toString());
        } catch (IOException unused) {
            return Collections.emptyMap();
        }
    }

    public final Map<String, List<String>> getRequestProperties() {
        if (!this.connected) {
            return OkHeaders.toMultimap(this.requestHeaders.build(), null);
        }
        throw new IllegalStateException("Cannot access request header fields after connection is set");
    }

    public final InputStream getInputStream() throws IOException {
        if (this.doInput) {
            HttpEngine response = getResponse();
            if (getResponseCode() < 400) {
                return response.getResponse().body().byteStream();
            }
            throw new FileNotFoundException(this.url.toString());
        }
        throw new ProtocolException("This protocol does not support input");
    }

    public final OutputStream getOutputStream() throws IOException {
        connect();
        BufferedSink bufferedRequestBody = this.httpEngine.getBufferedRequestBody();
        if (bufferedRequestBody == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("method does not support a request body: ");
            sb.append(this.method);
            throw new ProtocolException(sb.toString());
        } else if (!this.httpEngine.hasResponse()) {
            return bufferedRequestBody.outputStream();
        } else {
            throw new ProtocolException("cannot write request body after response has been read");
        }
    }

    public final Permission getPermission() throws IOException {
        String host = getURL().getHost();
        int effectivePort = Util.getEffectivePort(getURL());
        if (usingProxy()) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) this.client.getProxy().address();
            String hostName = inetSocketAddress.getHostName();
            effectivePort = inetSocketAddress.getPort();
            host = hostName;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(host);
        sb.append(":");
        sb.append(effectivePort);
        return new SocketPermission(sb.toString(), "connect, resolve");
    }

    public final String getRequestProperty(String str) {
        if (str == null) {
            return null;
        }
        return this.requestHeaders.get(str);
    }

    public void setConnectTimeout(int i) {
        this.client.setConnectTimeout((long) i, TimeUnit.MILLISECONDS);
    }

    public void setInstanceFollowRedirects(boolean z) {
        this.client.setFollowRedirects(z);
    }

    public int getConnectTimeout() {
        return this.client.getConnectTimeout();
    }

    public void setReadTimeout(int i) {
        this.client.setReadTimeout((long) i, TimeUnit.MILLISECONDS);
    }

    public int getReadTimeout() {
        return this.client.getReadTimeout();
    }

    private void initHttpEngine() throws IOException {
        IOException iOException = this.httpEngineFailure;
        if (iOException != null) {
            throw iOException;
        } else if (this.httpEngine == null) {
            this.connected = true;
            try {
                if (this.doOutput) {
                    if (this.method.equals(HttpRequest.METHOD_GET)) {
                        this.method = HttpRequest.METHOD_POST;
                    } else if (!HttpMethod.permitsRequestBody(this.method)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(this.method);
                        sb.append(" does not support writing");
                        throw new ProtocolException(sb.toString());
                    }
                }
                this.httpEngine = newHttpEngine(this.method, null, null, null);
            } catch (IOException e) {
                this.httpEngineFailure = e;
                throw e;
            }
        }
    }

    private HttpEngine newHttpEngine(String str, Connection connection, RetryableSink retryableSink, Response response) {
        boolean z;
        Request.Builder method = new Request.Builder().url(getURL()).method(str, null);
        Headers build = this.requestHeaders.build();
        int size = build.size();
        boolean z2 = false;
        for (int i = 0; i < size; i++) {
            method.addHeader(build.name(i), build.value(i));
        }
        if (HttpMethod.permitsRequestBody(str)) {
            long j = this.fixedContentLength;
            if (j != -1) {
                method.header("Content-Length", Long.toString(j));
            } else if (this.chunkLength > 0) {
                method.header(HttpHeaders.TRANSFER_ENCODING, "chunked");
            } else {
                z2 = true;
            }
            if (build.get("Content-Type") == null) {
                method.header("Content-Type", HttpRequest.CONTENT_TYPE_FORM);
            }
            z = z2;
        } else {
            z = false;
        }
        if (build.get("User-Agent") == null) {
            method.header("User-Agent", defaultUserAgent());
        }
        Request build2 = method.build();
        OkHttpClient okHttpClient = this.client;
        HttpEngine httpEngine2 = new HttpEngine((Internal.instance.internalCache(okHttpClient) == null || getUseCaches()) ? okHttpClient : this.client.clone().setCache(null), build2, z, true, false, connection, null, retryableSink, response);
        return httpEngine2;
    }

    private String defaultUserAgent() {
        String property = System.getProperty("http.agent");
        if (property != null) {
            return property;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Java");
        sb.append(System.getProperty("java.version"));
        return sb.toString();
    }

    private HttpEngine getResponse() throws IOException {
        initHttpEngine();
        if (this.httpEngine.hasResponse()) {
            return this.httpEngine;
        }
        while (true) {
            if (execute(true)) {
                Response response = this.httpEngine.getResponse();
                Request followUpRequest = this.httpEngine.followUpRequest();
                if (followUpRequest == null) {
                    this.httpEngine.releaseConnection();
                    return this.httpEngine;
                }
                int i = this.followUpCount + 1;
                this.followUpCount = i;
                if (i <= 20) {
                    this.url = followUpRequest.url();
                    this.requestHeaders = followUpRequest.headers().newBuilder();
                    Sink requestBody = this.httpEngine.getRequestBody();
                    if (!followUpRequest.method().equals(this.method)) {
                        requestBody = null;
                    }
                    if (requestBody == null || (requestBody instanceof RetryableSink)) {
                        if (!this.httpEngine.sameConnection(followUpRequest.url())) {
                            this.httpEngine.releaseConnection();
                        }
                        this.httpEngine = newHttpEngine(followUpRequest.method(), this.httpEngine.close(), (RetryableSink) requestBody, response);
                    } else {
                        throw new HttpRetryException("Cannot retry streamed HTTP body", this.responseCode);
                    }
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Too many follow-up requests: ");
                    sb.append(this.followUpCount);
                    throw new ProtocolException(sb.toString());
                }
            }
        }
    }

    private boolean execute(boolean z) throws IOException {
        try {
            this.httpEngine.sendRequest();
            this.route = this.httpEngine.getRoute();
            this.handshake = this.httpEngine.getConnection() != null ? this.httpEngine.getConnection().getHandshake() : null;
            if (z) {
                this.httpEngine.readResponse();
            }
            return true;
        } catch (IOException e) {
            HttpEngine recover = this.httpEngine.recover(e);
            if (recover != null) {
                this.httpEngine = recover;
                return false;
            }
            this.httpEngineFailure = e;
            throw e;
        }
    }

    public final boolean usingProxy() {
        Proxy proxy;
        Route route2 = this.route;
        if (route2 != null) {
            proxy = route2.getProxy();
        } else {
            proxy = this.client.getProxy();
        }
        return (proxy == null || proxy.type() == Type.DIRECT) ? false : true;
    }

    public String getResponseMessage() throws IOException {
        return getResponse().getResponse().message();
    }

    public final int getResponseCode() throws IOException {
        return getResponse().getResponse().code();
    }

    public final void setRequestProperty(String str, String str2) {
        if (this.connected) {
            throw new IllegalStateException("Cannot set request property after connection is made");
        } else if (str == null) {
            throw new NullPointerException("field == null");
        } else if (str2 == null) {
            Platform platform = Platform.get();
            StringBuilder sb = new StringBuilder();
            sb.append("Ignoring header ");
            sb.append(str);
            sb.append(" because its value was null.");
            platform.logW(sb.toString());
        } else {
            if ("X-Android-Transports".equals(str) || "X-Android-Protocols".equals(str)) {
                setProtocols(str2, false);
            } else {
                this.requestHeaders.set(str, str2);
            }
        }
    }

    public void setIfModifiedSince(long j) {
        super.setIfModifiedSince(j);
        if (this.ifModifiedSince != 0) {
            this.requestHeaders.set(HttpHeaders.IF_MODIFIED_SINCE, HttpDate.format(new Date(this.ifModifiedSince)));
        } else {
            this.requestHeaders.removeAll(HttpHeaders.IF_MODIFIED_SINCE);
        }
    }

    public final void addRequestProperty(String str, String str2) {
        if (this.connected) {
            throw new IllegalStateException("Cannot add request property after connection is made");
        } else if (str == null) {
            throw new NullPointerException("field == null");
        } else if (str2 == null) {
            Platform platform = Platform.get();
            StringBuilder sb = new StringBuilder();
            sb.append("Ignoring header ");
            sb.append(str);
            sb.append(" because its value was null.");
            platform.logW(sb.toString());
        } else {
            if ("X-Android-Transports".equals(str) || "X-Android-Protocols".equals(str)) {
                setProtocols(str2, true);
            } else {
                this.requestHeaders.add(str, str2);
            }
        }
    }

    private void setProtocols(String str, boolean z) {
        ArrayList arrayList = new ArrayList();
        if (z) {
            arrayList.addAll(this.client.getProtocols());
        }
        String[] split = str.split(",", -1);
        int length = split.length;
        int i = 0;
        while (i < length) {
            try {
                arrayList.add(Protocol.get(split[i]));
                i++;
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        this.client.setProtocols(arrayList);
    }

    public void setRequestMethod(String str) throws ProtocolException {
        if (METHODS.contains(str)) {
            this.method = str;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Expected one of ");
        sb.append(METHODS);
        sb.append(" but was ");
        sb.append(str);
        throw new ProtocolException(sb.toString());
    }

    public void setFixedLengthStreamingMode(int i) {
        setFixedLengthStreamingMode((long) i);
    }

    public void setFixedLengthStreamingMode(long j) {
        if (this.connected) {
            throw new IllegalStateException("Already connected");
        } else if (this.chunkLength > 0) {
            throw new IllegalStateException("Already in chunked mode");
        } else if (j >= 0) {
            this.fixedContentLength = j;
            this.fixedContentLength = (int) Math.min(j, 2147483647L);
        } else {
            throw new IllegalArgumentException("contentLength < 0");
        }
    }
}
