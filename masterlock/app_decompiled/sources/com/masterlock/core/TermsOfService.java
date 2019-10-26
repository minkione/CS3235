package com.masterlock.core;

public class TermsOfService {
    private String terms;
    private int version;

    public TermsOfService(int i, String str) {
        this.version = i;
        this.terms = str;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int i) {
        this.version = i;
    }

    public String getTerms() {
        return this.terms;
    }

    public void setTerms(String str) {
        this.terms = str;
    }
}
