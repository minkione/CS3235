package com.masterlock.api.util;

import com.masterlock.api.provider.AuthenticationStore;
import java.util.HashMap;
import java.util.Map;

public class AuthenticatedRequestBuilder {
    protected AuthenticationStore store;

    public AuthenticatedRequestBuilder(AuthenticationStore authenticationStore) {
        this.store = authenticationStore;
    }

    public AuthenticatedRequestBuilder setAuthenticationStore(AuthenticationStore authenticationStore) {
        this.store = authenticationStore;
        return this;
    }

    public Map<String, String> build() {
        return build(null);
    }

    public Map<String, String> build(Map<String, String> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        AuthenticationStore authenticationStore = this.store;
        if (authenticationStore != null) {
            map.put(ApiConstants.REQUEST_API_KEY_PARAM_NAME, authenticationStore.getAuthToken());
            map.put(ApiConstants.REQUEST_USERNAME_PARAM_NAME, this.store.getUsername());
        }
        return map;
    }
}
