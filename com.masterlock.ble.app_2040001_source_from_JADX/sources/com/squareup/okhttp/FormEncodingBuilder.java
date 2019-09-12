package com.squareup.okhttp;

import com.squareup.okhttp.internal.Util;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import p008io.fabric.sdk.android.services.network.HttpRequest;

public final class FormEncodingBuilder {
    private static final MediaType CONTENT_TYPE = MediaType.parse(HttpRequest.CONTENT_TYPE_FORM);
    private final StringBuilder content = new StringBuilder();

    public FormEncodingBuilder add(String str, String str2) {
        if (this.content.length() > 0) {
            this.content.append('&');
        }
        try {
            StringBuilder sb = this.content;
            sb.append(URLEncoder.encode(str, HttpRequest.CHARSET_UTF8));
            sb.append('=');
            sb.append(URLEncoder.encode(str2, HttpRequest.CHARSET_UTF8));
            return this;
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public RequestBody build() {
        if (this.content.length() != 0) {
            return RequestBody.create(CONTENT_TYPE, this.content.toString().getBytes(Util.UTF_8));
        }
        throw new IllegalStateException("Form encoded body must have at least one part.");
    }
}
