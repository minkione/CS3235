package com.squareup.okhttp;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;

public final class Route {
    final Address address;
    final ConnectionSpec connectionSpec;
    final InetSocketAddress inetSocketAddress;
    final Proxy proxy;
    final boolean shouldSendTlsFallbackIndicator;

    public Route(Address address2, Proxy proxy2, InetSocketAddress inetSocketAddress2, ConnectionSpec connectionSpec2) {
        this(address2, proxy2, inetSocketAddress2, connectionSpec2, false);
    }

    public Route(Address address2, Proxy proxy2, InetSocketAddress inetSocketAddress2, ConnectionSpec connectionSpec2, boolean z) {
        if (address2 == null) {
            throw new NullPointerException("address == null");
        } else if (proxy2 == null) {
            throw new NullPointerException("proxy == null");
        } else if (inetSocketAddress2 == null) {
            throw new NullPointerException("inetSocketAddress == null");
        } else if (connectionSpec2 != null) {
            this.address = address2;
            this.proxy = proxy2;
            this.inetSocketAddress = inetSocketAddress2;
            this.connectionSpec = connectionSpec2;
            this.shouldSendTlsFallbackIndicator = z;
        } else {
            throw new NullPointerException("connectionConfiguration == null");
        }
    }

    public Address getAddress() {
        return this.address;
    }

    public Proxy getProxy() {
        return this.proxy;
    }

    public InetSocketAddress getSocketAddress() {
        return this.inetSocketAddress;
    }

    public ConnectionSpec getConnectionSpec() {
        return this.connectionSpec;
    }

    public boolean getShouldSendTlsFallbackIndicator() {
        return this.shouldSendTlsFallbackIndicator;
    }

    public boolean requiresTunnel() {
        return this.address.sslSocketFactory != null && this.proxy.type() == Type.HTTP;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof Route)) {
            return false;
        }
        Route route = (Route) obj;
        if (this.address.equals(route.address) && this.proxy.equals(route.proxy) && this.inetSocketAddress.equals(route.inetSocketAddress) && this.connectionSpec.equals(route.connectionSpec) && this.shouldSendTlsFallbackIndicator == route.shouldSendTlsFallbackIndicator) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return ((((((((527 + this.address.hashCode()) * 31) + this.proxy.hashCode()) * 31) + this.inetSocketAddress.hashCode()) * 31) + this.connectionSpec.hashCode()) * 31) + (this.shouldSendTlsFallbackIndicator ? 1 : 0);
    }
}
