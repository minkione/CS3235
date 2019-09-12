package com.squareup.okhttp;

import android.support.p000v4.app.NotificationCompat;
import com.google.common.net.HttpHeaders;
import com.squareup.okhttp.Interceptor.Chain;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.internal.NamedRunnable;
import com.squareup.okhttp.internal.http.HttpEngine;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Call {
    volatile boolean canceled;
    /* access modifiers changed from: private */
    public final OkHttpClient client;
    HttpEngine engine;
    private boolean executed;
    Request originalRequest;

    class ApplicationInterceptorChain implements Chain {
        private final boolean forWebSocket;
        private final int index;
        private final Request request;

        public Connection connection() {
            return null;
        }

        ApplicationInterceptorChain(int i, Request request2, boolean z) {
            this.index = i;
            this.request = request2;
            this.forWebSocket = z;
        }

        public Request request() {
            return this.request;
        }

        public Response proceed(Request request2) throws IOException {
            if (this.index >= Call.this.client.interceptors().size()) {
                return Call.this.getResponse(request2, this.forWebSocket);
            }
            return ((Interceptor) Call.this.client.interceptors().get(this.index)).intercept(new ApplicationInterceptorChain(this.index + 1, request2, this.forWebSocket));
        }
    }

    final class AsyncCall extends NamedRunnable {
        private final boolean forWebSocket;
        private final Callback responseCallback;

        private AsyncCall(Callback callback, boolean z) {
            super("OkHttp %s", Call.this.originalRequest.urlString());
            this.responseCallback = callback;
            this.forWebSocket = z;
        }

        /* access modifiers changed from: 0000 */
        public String host() {
            return Call.this.originalRequest.url().getHost();
        }

        /* access modifiers changed from: 0000 */
        public Request request() {
            return Call.this.originalRequest;
        }

        /* access modifiers changed from: 0000 */
        public Object tag() {
            return Call.this.originalRequest.tag();
        }

        /* access modifiers changed from: 0000 */
        public void cancel() {
            Call.this.cancel();
        }

        /* access modifiers changed from: 0000 */
        public Call get() {
            return Call.this;
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Removed duplicated region for block: B:12:0x0030 A[SYNTHETIC, Splitter:B:12:0x0030] */
        /* JADX WARNING: Removed duplicated region for block: B:14:0x004f A[Catch:{ all -> 0x0029 }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void execute() {
            /*
                r5 = this;
                r0 = 1
                r1 = 0
                com.squareup.okhttp.Call r2 = com.squareup.okhttp.Call.this     // Catch:{ IOException -> 0x002b }
                boolean r3 = r5.forWebSocket     // Catch:{ IOException -> 0x002b }
                com.squareup.okhttp.Response r2 = r2.getResponseWithInterceptorChain(r3)     // Catch:{ IOException -> 0x002b }
                com.squareup.okhttp.Call r3 = com.squareup.okhttp.Call.this     // Catch:{ IOException -> 0x002b }
                boolean r1 = r3.canceled     // Catch:{ IOException -> 0x002b }
                if (r1 == 0) goto L_0x0021
                com.squareup.okhttp.Callback r1 = r5.responseCallback     // Catch:{ IOException -> 0x0027 }
                com.squareup.okhttp.Call r2 = com.squareup.okhttp.Call.this     // Catch:{ IOException -> 0x0027 }
                com.squareup.okhttp.Request r2 = r2.originalRequest     // Catch:{ IOException -> 0x0027 }
                java.io.IOException r3 = new java.io.IOException     // Catch:{ IOException -> 0x0027 }
                java.lang.String r4 = "Canceled"
                r3.<init>(r4)     // Catch:{ IOException -> 0x0027 }
                r1.onFailure(r2, r3)     // Catch:{ IOException -> 0x0027 }
                goto L_0x005c
            L_0x0021:
                com.squareup.okhttp.Callback r1 = r5.responseCallback     // Catch:{ IOException -> 0x0027 }
                r1.onResponse(r2)     // Catch:{ IOException -> 0x0027 }
                goto L_0x005c
            L_0x0027:
                r1 = move-exception
                goto L_0x002e
            L_0x0029:
                r0 = move-exception
                goto L_0x006a
            L_0x002b:
                r0 = move-exception
                r1 = r0
                r0 = 0
            L_0x002e:
                if (r0 == 0) goto L_0x004f
                java.util.logging.Logger r0 = com.squareup.okhttp.internal.Internal.logger     // Catch:{ all -> 0x0029 }
                java.util.logging.Level r2 = java.util.logging.Level.INFO     // Catch:{ all -> 0x0029 }
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0029 }
                r3.<init>()     // Catch:{ all -> 0x0029 }
                java.lang.String r4 = "Callback failure for "
                r3.append(r4)     // Catch:{ all -> 0x0029 }
                com.squareup.okhttp.Call r4 = com.squareup.okhttp.Call.this     // Catch:{ all -> 0x0029 }
                java.lang.String r4 = r4.toLoggableString()     // Catch:{ all -> 0x0029 }
                r3.append(r4)     // Catch:{ all -> 0x0029 }
                java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x0029 }
                r0.log(r2, r3, r1)     // Catch:{ all -> 0x0029 }
                goto L_0x005c
            L_0x004f:
                com.squareup.okhttp.Callback r0 = r5.responseCallback     // Catch:{ all -> 0x0029 }
                com.squareup.okhttp.Call r2 = com.squareup.okhttp.Call.this     // Catch:{ all -> 0x0029 }
                com.squareup.okhttp.internal.http.HttpEngine r2 = r2.engine     // Catch:{ all -> 0x0029 }
                com.squareup.okhttp.Request r2 = r2.getRequest()     // Catch:{ all -> 0x0029 }
                r0.onFailure(r2, r1)     // Catch:{ all -> 0x0029 }
            L_0x005c:
                com.squareup.okhttp.Call r0 = com.squareup.okhttp.Call.this
                com.squareup.okhttp.OkHttpClient r0 = r0.client
                com.squareup.okhttp.Dispatcher r0 = r0.getDispatcher()
                r0.finished(r5)
                return
            L_0x006a:
                com.squareup.okhttp.Call r1 = com.squareup.okhttp.Call.this
                com.squareup.okhttp.OkHttpClient r1 = r1.client
                com.squareup.okhttp.Dispatcher r1 = r1.getDispatcher()
                r1.finished(r5)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.squareup.okhttp.Call.AsyncCall.execute():void");
        }
    }

    protected Call(OkHttpClient okHttpClient, Request request) {
        this.client = okHttpClient.copyWithDefaults();
        this.originalRequest = request;
    }

    public Response execute() throws IOException {
        synchronized (this) {
            if (!this.executed) {
                this.executed = true;
            } else {
                throw new IllegalStateException("Already Executed");
            }
        }
        try {
            this.client.getDispatcher().executed(this);
            Response responseWithInterceptorChain = getResponseWithInterceptorChain(false);
            if (responseWithInterceptorChain != null) {
                return responseWithInterceptorChain;
            }
            throw new IOException("Canceled");
        } finally {
            this.client.getDispatcher().finished(this);
        }
    }

    /* access modifiers changed from: 0000 */
    public Object tag() {
        return this.originalRequest.tag();
    }

    public void enqueue(Callback callback) {
        enqueue(callback, false);
    }

    /* access modifiers changed from: 0000 */
    public void enqueue(Callback callback, boolean z) {
        synchronized (this) {
            if (!this.executed) {
                this.executed = true;
            } else {
                throw new IllegalStateException("Already Executed");
            }
        }
        this.client.getDispatcher().enqueue(new AsyncCall(callback, z));
    }

    public void cancel() {
        this.canceled = true;
        HttpEngine httpEngine = this.engine;
        if (httpEngine != null) {
            httpEngine.disconnect();
        }
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    /* access modifiers changed from: private */
    public String toLoggableString() {
        String str = this.canceled ? "canceled call" : NotificationCompat.CATEGORY_CALL;
        try {
            String url = new URL(this.originalRequest.url(), "/...").toString();
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(" to ");
            sb.append(url);
            return sb.toString();
        } catch (MalformedURLException unused) {
            return str;
        }
    }

    /* access modifiers changed from: private */
    public Response getResponseWithInterceptorChain(boolean z) throws IOException {
        return new ApplicationInterceptorChain(0, this.originalRequest, z).proceed(this.originalRequest);
    }

    /* access modifiers changed from: 0000 */
    public Response getResponse(Request request, boolean z) throws IOException {
        Request request2;
        RequestBody body = request.body();
        if (body != null) {
            Builder newBuilder = request.newBuilder();
            MediaType contentType = body.contentType();
            if (contentType != null) {
                newBuilder.header("Content-Type", contentType.toString());
            }
            long contentLength = body.contentLength();
            if (contentLength != -1) {
                newBuilder.header("Content-Length", Long.toString(contentLength));
                newBuilder.removeHeader(HttpHeaders.TRANSFER_ENCODING);
            } else {
                newBuilder.header(HttpHeaders.TRANSFER_ENCODING, "chunked");
                newBuilder.removeHeader("Content-Length");
            }
            request2 = newBuilder.build();
        } else {
            request2 = request;
        }
        HttpEngine httpEngine = new HttpEngine(this.client, request2, false, false, z, null, null, null, null);
        this.engine = httpEngine;
        int i = 0;
        while (!this.canceled) {
            try {
                this.engine.sendRequest();
                this.engine.readResponse();
                Response response = this.engine.getResponse();
                Request followUpRequest = this.engine.followUpRequest();
                if (followUpRequest == null) {
                    if (!z) {
                        this.engine.releaseConnection();
                    }
                    return response;
                }
                i++;
                if (i <= 20) {
                    if (!this.engine.sameConnection(followUpRequest.url())) {
                        this.engine.releaseConnection();
                    }
                    HttpEngine httpEngine2 = new HttpEngine(this.client, followUpRequest, false, false, z, this.engine.close(), null, null, response);
                    this.engine = httpEngine2;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Too many follow-up requests: ");
                    sb.append(i);
                    throw new ProtocolException(sb.toString());
                }
            } catch (IOException e) {
                HttpEngine recover = this.engine.recover(e, null);
                if (recover != null) {
                    this.engine = recover;
                } else {
                    throw e;
                }
            }
        }
        this.engine.releaseConnection();
        return null;
    }
}
