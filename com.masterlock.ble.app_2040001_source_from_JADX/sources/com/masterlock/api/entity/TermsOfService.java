package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;

public class TermsOfService {
    @SerializedName("Value")
    private String value;
    @SerializedName("Version")
    private int version;

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int i) {
        this.version = i;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }
}
