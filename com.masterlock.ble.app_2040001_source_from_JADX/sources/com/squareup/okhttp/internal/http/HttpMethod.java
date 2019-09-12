package com.squareup.okhttp.internal.http;

import p008io.fabric.sdk.android.services.network.HttpRequest;

public final class HttpMethod {
    public static boolean invalidatesCache(String str) {
        return str.equals(HttpRequest.METHOD_POST) || str.equals("PATCH") || str.equals(HttpRequest.METHOD_PUT) || str.equals(HttpRequest.METHOD_DELETE);
    }

    public static boolean requiresRequestBody(String str) {
        return str.equals(HttpRequest.METHOD_POST) || str.equals(HttpRequest.METHOD_PUT) || str.equals("PATCH");
    }

    public static boolean permitsRequestBody(String str) {
        return requiresRequestBody(str) || str.equals(HttpRequest.METHOD_DELETE);
    }

    private HttpMethod() {
    }
}
