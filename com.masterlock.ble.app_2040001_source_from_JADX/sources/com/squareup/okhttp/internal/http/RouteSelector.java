package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Address;
import com.squareup.okhttp.ConnectionSpec;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Route;
import com.squareup.okhttp.internal.Internal;
import com.squareup.okhttp.internal.Network;
import com.squareup.okhttp.internal.RouteDatabase;
import com.squareup.okhttp.internal.Util;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLProtocolException;

public final class RouteSelector {
    private final Address address;
    private final OkHttpClient client;
    private List<ConnectionSpec> connectionSpecs = Collections.emptyList();
    private List<InetSocketAddress> inetSocketAddresses = Collections.emptyList();
    private InetSocketAddress lastInetSocketAddress;
    private Proxy lastProxy;
    private ConnectionSpec lastSpec;
    private final Network network;
    private int nextInetSocketAddressIndex;
    private int nextProxyIndex;
    private int nextSpecIndex;
    private final List<Route> postponedRoutes = new ArrayList();
    private List<Proxy> proxies = Collections.emptyList();
    private final Request request;
    private final RouteDatabase routeDatabase;
    private final URI uri;

    private RouteSelector(Address address2, URI uri2, OkHttpClient okHttpClient, Request request2) {
        this.address = address2;
        this.uri = uri2;
        this.client = okHttpClient;
        this.routeDatabase = Internal.instance.routeDatabase(okHttpClient);
        this.network = Internal.instance.network(okHttpClient);
        this.request = request2;
        resetNextProxy(uri2, address2.getProxy());
    }

    public static RouteSelector get(Address address2, Request request2, OkHttpClient okHttpClient) throws IOException {
        return new RouteSelector(address2, request2.uri(), okHttpClient, request2);
    }

    public boolean hasNext() {
        return hasNextConnectionSpec() || hasNextInetSocketAddress() || hasNextProxy() || hasNextPostponed();
    }

    public Route next() throws IOException {
        if (!hasNextConnectionSpec()) {
            if (!hasNextInetSocketAddress()) {
                if (hasNextProxy()) {
                    this.lastProxy = nextProxy();
                } else if (hasNextPostponed()) {
                    return nextPostponed();
                } else {
                    throw new NoSuchElementException();
                }
            }
            this.lastInetSocketAddress = nextInetSocketAddress();
        }
        this.lastSpec = nextConnectionSpec();
        Route route = new Route(this.address, this.lastProxy, this.lastInetSocketAddress, this.lastSpec, shouldSendTlsFallbackIndicator(this.lastSpec));
        if (!this.routeDatabase.shouldPostpone(route)) {
            return route;
        }
        this.postponedRoutes.add(route);
        return next();
    }

    private boolean shouldSendTlsFallbackIndicator(ConnectionSpec connectionSpec) {
        if (connectionSpec == this.connectionSpecs.get(0) || !connectionSpec.isTls()) {
            return false;
        }
        return true;
    }

    public void connectFailed(Route route, IOException iOException) {
        if (!(route.getProxy().type() == Type.DIRECT || this.address.getProxySelector() == null)) {
            this.address.getProxySelector().connectFailed(this.uri, route.getProxy().address(), iOException);
        }
        this.routeDatabase.failed(route);
        if (!(iOException instanceof SSLHandshakeException) && !(iOException instanceof SSLProtocolException)) {
            while (this.nextSpecIndex < this.connectionSpecs.size()) {
                List<ConnectionSpec> list = this.connectionSpecs;
                int i = this.nextSpecIndex;
                this.nextSpecIndex = i + 1;
                ConnectionSpec connectionSpec = (ConnectionSpec) list.get(i);
                Route route2 = new Route(this.address, this.lastProxy, this.lastInetSocketAddress, connectionSpec, shouldSendTlsFallbackIndicator(connectionSpec));
                this.routeDatabase.failed(route2);
            }
        }
    }

    private void resetNextProxy(URI uri2, Proxy proxy) {
        if (proxy != null) {
            this.proxies = Collections.singletonList(proxy);
        } else {
            this.proxies = new ArrayList();
            List select = this.client.getProxySelector().select(uri2);
            if (select != null) {
                this.proxies.addAll(select);
            }
            this.proxies.removeAll(Collections.singleton(Proxy.NO_PROXY));
            this.proxies.add(Proxy.NO_PROXY);
        }
        this.nextProxyIndex = 0;
    }

    private boolean hasNextProxy() {
        return this.nextProxyIndex < this.proxies.size();
    }

    private Proxy nextProxy() throws IOException {
        if (hasNextProxy()) {
            List<Proxy> list = this.proxies;
            int i = this.nextProxyIndex;
            this.nextProxyIndex = i + 1;
            Proxy proxy = (Proxy) list.get(i);
            resetNextInetSocketAddress(proxy);
            return proxy;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("No route to ");
        sb.append(this.address.getUriHost());
        sb.append("; exhausted proxy configurations: ");
        sb.append(this.proxies);
        throw new SocketException(sb.toString());
    }

    private void resetNextInetSocketAddress(Proxy proxy) throws IOException {
        int i;
        String str;
        this.inetSocketAddresses = new ArrayList();
        if (proxy.type() == Type.DIRECT || proxy.type() == Type.SOCKS) {
            str = this.address.getUriHost();
            i = Util.getEffectivePort(this.uri);
        } else {
            SocketAddress address2 = proxy.address();
            if (address2 instanceof InetSocketAddress) {
                InetSocketAddress inetSocketAddress = (InetSocketAddress) address2;
                str = getHostString(inetSocketAddress);
                i = inetSocketAddress.getPort();
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Proxy.address() is not an InetSocketAddress: ");
                sb.append(address2.getClass());
                throw new IllegalArgumentException(sb.toString());
            }
        }
        if (i < 1 || i > 65535) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("No route to ");
            sb2.append(str);
            sb2.append(":");
            sb2.append(i);
            sb2.append("; port is out of range");
            throw new SocketException(sb2.toString());
        }
        for (InetAddress inetSocketAddress2 : this.network.resolveInetAddresses(str)) {
            this.inetSocketAddresses.add(new InetSocketAddress(inetSocketAddress2, i));
        }
        this.nextInetSocketAddressIndex = 0;
    }

    static String getHostString(InetSocketAddress inetSocketAddress) {
        InetAddress address2 = inetSocketAddress.getAddress();
        if (address2 == null) {
            return inetSocketAddress.getHostName();
        }
        return address2.getHostAddress();
    }

    private boolean hasNextInetSocketAddress() {
        return this.nextInetSocketAddressIndex < this.inetSocketAddresses.size();
    }

    private InetSocketAddress nextInetSocketAddress() throws IOException {
        if (hasNextInetSocketAddress()) {
            List<InetSocketAddress> list = this.inetSocketAddresses;
            int i = this.nextInetSocketAddressIndex;
            this.nextInetSocketAddressIndex = i + 1;
            InetSocketAddress inetSocketAddress = (InetSocketAddress) list.get(i);
            resetConnectionSpecs();
            return inetSocketAddress;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("No route to ");
        sb.append(this.address.getUriHost());
        sb.append("; exhausted inet socket addresses: ");
        sb.append(this.inetSocketAddresses);
        throw new SocketException(sb.toString());
    }

    private void resetConnectionSpecs() {
        this.connectionSpecs = new ArrayList();
        List connectionSpecs2 = this.address.getConnectionSpecs();
        int size = connectionSpecs2.size();
        for (int i = 0; i < size; i++) {
            ConnectionSpec connectionSpec = (ConnectionSpec) connectionSpecs2.get(i);
            if (this.request.isHttps() == connectionSpec.isTls()) {
                this.connectionSpecs.add(connectionSpec);
            }
        }
        this.nextSpecIndex = 0;
    }

    private boolean hasNextConnectionSpec() {
        return this.nextSpecIndex < this.connectionSpecs.size();
    }

    private ConnectionSpec nextConnectionSpec() throws IOException {
        String str;
        String str2;
        if (this.connectionSpecs.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("No route to ");
            if (this.uri.getScheme() != null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(this.uri.getScheme());
                sb2.append("://");
                str2 = sb2.toString();
            } else {
                str2 = "//";
            }
            sb.append(str2);
            sb.append(this.address.getUriHost());
            sb.append("; no connection specs");
            throw new UnknownServiceException(sb.toString());
        } else if (!hasNextConnectionSpec()) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("No route to ");
            if (this.uri.getScheme() != null) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(this.uri.getScheme());
                sb4.append("://");
                str = sb4.toString();
            } else {
                str = "//";
            }
            sb3.append(str);
            sb3.append(this.address.getUriHost());
            sb3.append("; exhausted connection specs: ");
            sb3.append(this.connectionSpecs);
            throw new SocketException(sb3.toString());
        } else {
            List<ConnectionSpec> list = this.connectionSpecs;
            int i = this.nextSpecIndex;
            this.nextSpecIndex = i + 1;
            return (ConnectionSpec) list.get(i);
        }
    }

    private boolean hasNextPostponed() {
        return !this.postponedRoutes.isEmpty();
    }

    private Route nextPostponed() {
        return (Route) this.postponedRoutes.remove(0);
    }
}
