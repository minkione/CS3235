package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;

public class GuestResponse {
    @SerializedName("Email")
    String email;
    @SerializedName("FirstName")
    String firstName;
    @SerializedName("Id")

    /* renamed from: id */
    String f156id;
    @SerializedName("LastName")
    String lastName;
    @SerializedName("MobileNumber")
    String mobileNumberE164;
    @SerializedName("Organization")
    String organization;
    @SerializedName("ServiceResult")
    public int serviceResult;
}
