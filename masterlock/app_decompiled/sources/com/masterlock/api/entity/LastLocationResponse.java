package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;

public class LastLocationResponse {
    @SerializedName("Latitude")
    public String latitude;
    @SerializedName("Longitude")
    public String longitude;
    @SerializedName("Notes")
    public String notes;
}
