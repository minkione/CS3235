package com.squareup.okhttp;

import com.google.common.net.HttpHeaders;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.internal.Platform;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.http.HttpConnection;
import com.squareup.okhttp.internal.http.HttpEngine;
import com.squareup.okhttp.internal.http.HttpTransport;
import com.squareup.okhttp.internal.http.OkHeaders;
import com.squareup.okhttp.internal.http.SpdyTransport;
import com.squareup.okhttp.internal.http.Transport;
import com.squareup.okhttp.internal.spdy.SpdyConnection;
import com.squareup.okhttp.internal.tls.OkHostnameVerifier;
import java.io.IOException;
import java.net.Proxy.Type;
import java.net.Socket;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import okio.Source;

public final class Connection {
    private boolean connected = false;
    private Handshake handshake;
    private HttpConnection httpConnection;
    private long idleStartTimeNs;
    private Object owner;
    private final ConnectionPool pool;
    private Protocol protocol = Protocol.HTTP_1_1;
    private int recycleCount;
    private final Route route;
    private Socket socket;
    private SpdyConnection spdyConnection;

    public Connection(ConnectionPool connectionPool, Route route2) {
        this.pool = connectionPool;
        this.route = route2;
    }

    /* access modifiers changed from: 0000 */
    public Object getOwner() {
        Object obj;
        synchronized (this.pool) {
            obj = this.owner;
        }
        return obj;
    }

    /* access modifiers changed from: 0000 */
    public void setOwner(Object obj) {
        if (!isSpdy()) {
            synchronized (this.pool) {
                if (this.owner == null) {
                    this.owner = obj;
                } else {
                    throw new IllegalStateException("Connection already has an owner!");
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean clearOwner() {
        synchronized (this.pool) {
            if (this.owner == null) {
                return false;
            }
            this.owner = null;
            return true;
        }
    }

    /* access modifiers changed from: 0000 */
    public void closeIfOwnedBy(Object obj) throws IOException {
        if (!isSpdy()) {
            synchronized (this.pool) {
                if (this.owner == obj) {
                    this.owner = null;
                    this.socket.close();
                    return;
                }
                return;
            }
        }
        throw new IllegalStateException();
    }

    /* access modifiers changed from: 0000 */
    public void connect(int i, int i2, int i3, Request request) throws IOException {
        if (!this.connected) {
            if (this.route.proxy.type() == Type.DIRECT || this.route.proxy.type() == Type.HTTP) {
                this.socket = this.route.address.socketFactory.createSocket();
            } else {
                this.socket = new Socket(this.route.proxy);
            }
            this.socket.setSoTimeout(i2);
            Platform.get().connectSocket(this.socket, this.route.inetSocketAddress, i);
            if (this.route.address.sslSocketFactory != null) {
                upgradeToTls(request, i2, i3);
            } else {
                this.httpConnection = new HttpConnection(this.pool, this, this.socket);
            }
            this.connected = true;
            return;
        }
        throw new IllegalStateException("already connected");
    }

    /* access modifiers changed from: 0000 */
    public void connectAndSetOwner(OkHttpClient okHttpClient, Object obj, Request request) throws IOException {
        setOwner(obj);
        if (!isConnected()) {
            connect(okHttpClient.getConnectTimeout(), okHttpClient.getReadTimeout(), okHttpClient.getWriteTimeout(), tunnelRequest(request));
            if (isSpdy()) {
                okHttpClient.getConnectionPool().share(this);
            }
            okHttpClient.routeDatabase().connected(getRoute());
        }
        setTimeouts(okHttpClient.getReadTimeout(), okHttpClient.getWriteTimeout());
    }

    private Request tunnelRequest(Request request) throws IOException {
        String str;
        if (!this.route.requiresTunnel()) {
            return null;
        }
        String host = request.url().getHost();
        int effectivePort = Util.getEffectivePort(request.url());
        if (effectivePort == Util.getDefaultPort("https")) {
            str = host;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(host);
            sb.append(":");
            sb.append(effectivePort);
            str = sb.toString();
        }
        Builder header = new Builder().url(new URL("https", host, effectivePort, "/")).header(HttpHeaders.HOST, str).header("Proxy-Connection", "Keep-Alive");
        String header2 = request.header("User-Agent");
        if (header2 != null) {
            header.header("User-Agent", header2);
        }
        String header3 = request.header("Proxy-Authorization");
        if (header3 != null) {
            header.header("Proxy-Authorization", header3);
        }
        return header.build();
    }

    /* JADX INFO: finally extract failed */
    private void upgradeToTls(Request request, int i, int i2) throws IOException {
        Platform platform = Platform.get();
        if (request != null) {
            makeTunnel(request, i, i2);
        }
        this.socket = this.route.address.sslSocketFactory.createSocket(this.socket, this.route.address.uriHost, this.route.address.uriPort, true);
        SSLSocket sSLSocket = (SSLSocket) this.socket;
        this.route.connectionSpec.apply(sSLSocket, this.route);
        try {
            sSLSocket.startHandshake();
            if (this.route.connectionSpec.supportsTlsExtensions()) {
                String selectedProtocol = platform.getSelectedProtocol(sSLSocket);
                if (selectedProtocol != null) {
                    this.protocol = Protocol.get(selectedProtocol);
                }
            }
            platform.afterHandshake(sSLSocket);
            this.handshake = Handshake.get(sSLSocket.getSession());
            if (this.route.address.hostnameVerifier.verify(this.route.address.uriHost, sSLSocket.getSession())) {
                this.route.address.certificatePinner.check(this.route.address.uriHost, this.handshake.peerCertificates());
                if (this.protocol == Protocol.SPDY_3 || this.protocol == Protocol.HTTP_2) {
                    sSLSocket.setSoTimeout(0);
                    this.spdyConnection = new SpdyConnection.Builder(this.route.address.getUriHost(), true, this.socket).protocol(this.protocol).build();
                    this.spdyConnection.sendConnectionPreface();
                    return;
                }
                this.httpConnection = new HttpConnection(this.pool, this, this.socket);
                return;
            }
            X509Certificate x509Certificate = (X509Certificate) sSLSocket.getSession().getPeerCertificates()[0];
            StringBuilder sb = new StringBuilder();
            sb.append("Hostname ");
            sb.append(this.route.address.uriHost);
            sb.append(" not verified:");
            sb.append("\n    certificate: ");
            sb.append(CertificatePinner.pin(x509Certificate));
            sb.append("\n    DN: ");
            sb.append(x509Certificate.getSubjectDN().getName());
            sb.append("\n    subjectAltNames: ");
            sb.append(OkHostnameVerifier.allSubjectAltNames(x509Certificate));
            throw new SSLPeerUnverifiedException(sb.toString());
        } catch (Throwable th) {
            platform.afterHandshake(sSLSocket);
            throw th;
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean isConnected() {
        return this.connected;
    }

    public Route getRoute() {
        return this.route;
    }

    public Socket getSocket() {
        return this.socket;
    }

    /* access modifiers changed from: 0000 */
    public boolean isAlive() {
        return !this.socket.isClosed() && !this.socket.isInputShutdown() && !this.socket.isOutputShutdown();
    }

    /* access modifiers changed from: 0000 */
    public boolean isReadable() {
        HttpConnection httpConnection2 = this.httpConnection;
        if (httpConnection2 != null) {
            return httpConnection2.isReadable();
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void resetIdleStartTime() {
        if (this.spdyConnection == null) {
            this.idleStartTimeNs = System.nanoTime();
            return;
        }
        throw new IllegalStateException("spdyConnection != null");
    }

    /* access modifiers changed from: 0000 */
    public boolean isIdle() {
        SpdyConnection spdyConnection2 = this.spdyConnection;
        return spdyConnection2 == null || spdyConnection2.isIdle();
    }

    /* access modifiers changed from: 0000 */
    public long getIdleStartTimeNs() {
        SpdyConnection spdyConnection2 = this.spdyConnection;
        return spdyConnection2 == null ? this.idleStartTimeNs : spdyConnection2.getIdleStartTimeNs();
    }

    public Handshake getHandshake() {
        return this.handshake;
    }

    /* access modifiers changed from: 0000 */
    public Transport newTransport(HttpEngine httpEngine) throws IOException {
        SpdyConnection spdyConnection2 = this.spdyConnection;
        return spdyConnection2 != null ? new SpdyTransport(httpEngine, spdyConnection2) : new HttpTransport(httpEngine, this.httpConnection);
    }

    /* access modifiers changed from: 0000 */
    public boolean isSpdy() {
        return this.spdyConnection != null;
    }

    public Protocol getProtocol() {
        return this.protocol;
    }

    /* access modifiers changed from: 0000 */
    public void setProtocol(Protocol protocol2) {
        if (protocol2 != null) {
            this.protocol = protocol2;
            return;
        }
        throw new IllegalArgumentException("protocol == null");
    }

    /* access modifiers changed from: 0000 */
    public void setTimeouts(int i, int i2) throws IOException {
        if (!this.connected) {
            throw new IllegalStateException("setTimeouts - not connected");
        } else if (this.httpConnection != null) {
            this.socket.setSoTimeout(i);
            this.httpConnection.setTimeouts(i, i2);
        }
    }

    /* access modifiers changed from: 0000 */
    public void incrementRecycleCount() {
        this.recycleCount++;
    }

    /* access modifiers changed from: 0000 */
    public int recycleCount() {
        return this.recycleCount;
    }

    private void makeTunnel(Request request, int i, int i2) throws IOException {
        HttpConnection httpConnection2 = new HttpConnection(this.pool, this, this.socket);
        httpConnection2.setTimeouts(i, i2);
        URL url = request.url();
        StringBuilder sb = new StringBuilder();
        sb.append("CONNECT ");
        sb.append(url.getHost());
        sb.append(":");
        sb.append(url.getPort());
        sb.append(" HTTP/1.1");
        String sb2 = sb.toString();
        do {
            httpConnection2.writeRequest(request.headers(), sb2);
            httpConnection2.flush();
            Response build = httpConnection2.readResponse().request(request).build();
            long contentLength = OkHeaders.contentLength(build);
            if (contentLength == -1) {
                contentLength = 0;
            }
            Source newFixedLengthSource = httpConnection2.newFixedLengthSource(contentLength);
            Util.skipAll(newFixedLengthSource, Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
            newFixedLengthSource.close();
            int code = build.code();
            if (code != 200) {
                if (code == 407) {
                    request = OkHeaders.processAuthHeader(this.route.address.authenticator, build, this.route.proxy);
                } else {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Unexpected response code for CONNECT: ");
                    sb3.append(build.code());
                    throw new IOException(sb3.toString());
                }
            } else if (httpConnection2.bufferSize() > 0) {
                throw new IOException("TLS tunnel buffered too many bytes!");
            } else {
                return;
            }
        } while (request != null);
        throw new IOException("Failed to authenticate with proxy");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Connection{");
        sb.append(this.route.address.uriHost);
        sb.append(":");
        sb.append(this.route.address.uriPort);
        sb.append(", proxy=");
        sb.append(this.route.proxy);
        sb.append(" hostAddress=");
        sb.append(this.route.inetSocketAddress.getAddress().getHostAddress());
        sb.append(" cipherSuite=");
        Handshake handshake2 = this.handshake;
        sb.append(handshake2 != null ? handshake2.cipherSuite() : "none");
        sb.append(" protocol=");
        sb.append(this.protocol);
        sb.append('}');
        return sb.toString();
    }
}
