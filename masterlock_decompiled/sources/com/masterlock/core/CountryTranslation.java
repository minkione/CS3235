package com.masterlock.core;

import com.google.gson.annotations.SerializedName;

public class CountryTranslation {
    @SerializedName("common")
    public String common;
    @SerializedName("official")
    public String official;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CountryTranslation{official='");
        sb.append(this.official);
        sb.append('\'');
        sb.append(", common='");
        sb.append(this.common);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }
}
