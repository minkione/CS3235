package com.masterlock.core;

import com.google.gson.annotations.SerializedName;

public class Translation {
    @SerializedName("deu")
    public CountryTranslation deu;
    @SerializedName("fra")
    public CountryTranslation fra;
    @SerializedName("spa")
    public CountryTranslation spa;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Translation{deu=");
        sb.append(this.deu);
        sb.append(", fra=");
        sb.append(this.fra);
        sb.append(", spa=");
        sb.append(this.spa);
        sb.append('}');
        return sb.toString();
    }
}
