package com.masterlock.api.entity;

public class AuthenticationWithTOSResponseWrapper {
    private AuthResponse mAuthResponse;
    private TermsOfService mTermsOfService;

    public AuthResponse getAuthResponse() {
        return this.mAuthResponse;
    }

    public void setAuthResponse(AuthResponse authResponse) {
        this.mAuthResponse = authResponse;
    }

    public TermsOfService getTermsOfService() {
        return this.mTermsOfService;
    }

    public void setTermsOfService(TermsOfService termsOfService) {
        this.mTermsOfService = termsOfService;
    }
}
