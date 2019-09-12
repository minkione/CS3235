package com.masterlock.api.provider;

public interface AuthenticationStore {
    String getAuthToken();

    String getUsername();

    void putAuthToken(String str);

    void putUsername(String str);
}
