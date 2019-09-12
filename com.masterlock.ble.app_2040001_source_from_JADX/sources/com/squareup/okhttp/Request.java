package com.squareup.okhttp;

import com.squareup.okhttp.internal.Platform;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.http.HttpMethod;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import p008io.fabric.sdk.android.services.network.HttpRequest;

public final class Request {
    /* access modifiers changed from: private */
    public final RequestBody body;
    private volatile CacheControl cacheControl;
    /* access modifiers changed from: private */
    public final Headers headers;
    /* access modifiers changed from: private */
    public final String method;
    /* access modifiers changed from: private */
    public final Object tag;
    private volatile URI uri;
    /* access modifiers changed from: private */
    public volatile URL url;
    /* access modifiers changed from: private */
    public final String urlString;

    public static class Builder {
        /* access modifiers changed from: private */
        public RequestBody body;
        /* access modifiers changed from: private */
        public com.squareup.okhttp.Headers.Builder headers;
        /* access modifiers changed from: private */
        public String method;
        /* access modifiers changed from: private */
        public Object tag;
        /* access modifiers changed from: private */
        public URL url;
        /* access modifiers changed from: private */
        public String urlString;

        public Builder() {
            this.method = HttpRequest.METHOD_GET;
            this.headers = new com.squareup.okhttp.Headers.Builder();
        }

        private Builder(Request request) {
            this.urlString = request.urlString;
            this.url = request.url;
            this.method = request.method;
            this.body = request.body;
            this.tag = request.tag;
            this.headers = request.headers.newBuilder();
        }

        public Builder url(String str) {
            if (str != null) {
                this.urlString = str;
                this.url = null;
                return this;
            }
            throw new IllegalArgumentException("url == null");
        }

        public Builder url(URL url2) {
            if (url2 != null) {
                this.url = url2;
                this.urlString = url2.toString();
                return this;
            }
            throw new IllegalArgumentException("url == null");
        }

        public Builder header(String str, String str2) {
            this.headers.set(str, str2);
            return this;
        }

        public Builder addHeader(String str, String str2) {
            this.headers.add(str, str2);
            return this;
        }

        public Builder removeHeader(String str) {
            this.headers.removeAll(str);
            return this;
        }

        public Builder headers(Headers headers2) {
            this.headers = headers2.newBuilder();
            return this;
        }

        public Builder cacheControl(CacheControl cacheControl) {
            String cacheControl2 = cacheControl.toString();
            if (cacheControl2.isEmpty()) {
                return removeHeader("Cache-Control");
            }
            return header("Cache-Control", cacheControl2);
        }

        public Builder get() {
            return method(HttpRequest.METHOD_GET, null);
        }

        public Builder head() {
            return method(HttpRequest.METHOD_HEAD, null);
        }

        public Builder post(RequestBody requestBody) {
            return method(HttpRequest.METHOD_POST, requestBody);
        }

        public Builder delete(RequestBody requestBody) {
            return method(HttpRequest.METHOD_DELETE, requestBody);
        }

        public Builder delete() {
            return method(HttpRequest.METHOD_DELETE, null);
        }

        public Builder put(RequestBody requestBody) {
            return method(HttpRequest.METHOD_PUT, requestBody);
        }

        public Builder patch(RequestBody requestBody) {
            return method("PATCH", requestBody);
        }

        public Builder method(String str, RequestBody requestBody) {
            if (str == null || str.length() == 0) {
                throw new IllegalArgumentException("method == null || method.length() == 0");
            } else if (requestBody == null || HttpMethod.permitsRequestBody(str)) {
                if (requestBody == null && HttpMethod.permitsRequestBody(str)) {
                    requestBody = RequestBody.create((MediaType) null, Util.EMPTY_BYTE_ARRAY);
                }
                this.method = str;
                this.body = requestBody;
                return this;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("method ");
                sb.append(str);
                sb.append(" must not have a request body.");
                throw new IllegalArgumentException(sb.toString());
            }
        }

        public Builder tag(Object obj) {
            this.tag = obj;
            return this;
        }

        public Request build() {
            if (this.urlString != null) {
                return new Request(this);
            }
            throw new IllegalStateException("url == null");
        }
    }

    private Request(Builder builder) {
        this.urlString = builder.urlString;
        this.method = builder.method;
        this.headers = builder.headers.build();
        this.body = builder.body;
        this.tag = builder.tag != null ? builder.tag : this;
        this.url = builder.url;
    }

    public URL url() {
        try {
            URL url2 = this.url;
            if (url2 != null) {
                return url2;
            }
            URL url3 = new URL(this.urlString);
            this.url = url3;
            return url3;
        } catch (MalformedURLException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Malformed URL: ");
            sb.append(this.urlString);
            throw new RuntimeException(sb.toString(), e);
        }
    }

    public URI uri() throws IOException {
        try {
            URI uri2 = this.uri;
            if (uri2 != null) {
                return uri2;
            }
            URI uriLenient = Platform.get().toUriLenient(url());
            this.uri = uriLenient;
            return uriLenient;
        } catch (URISyntaxException e) {
            throw new IOException(e.getMessage());
        }
    }

    public String urlString() {
        return this.urlString;
    }

    public String method() {
        return this.method;
    }

    public Headers headers() {
        return this.headers;
    }

    public String header(String str) {
        return this.headers.get(str);
    }

    public List<String> headers(String str) {
        return this.headers.values(str);
    }

    public RequestBody body() {
        return this.body;
    }

    public Object tag() {
        return this.tag;
    }

    public Builder newBuilder() {
        return new Builder();
    }

    public CacheControl cacheControl() {
        CacheControl cacheControl2 = this.cacheControl;
        if (cacheControl2 != null) {
            return cacheControl2;
        }
        CacheControl parse = CacheControl.parse(this.headers);
        this.cacheControl = parse;
        return parse;
    }

    public boolean isHttps() {
        return url().getProtocol().equals("https");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Request{method=");
        sb.append(this.method);
        sb.append(", url=");
        sb.append(this.urlString);
        sb.append(", tag=");
        Object obj = this.tag;
        if (obj == this) {
            obj = null;
        }
        sb.append(obj);
        sb.append('}');
        return sb.toString();
    }
}
