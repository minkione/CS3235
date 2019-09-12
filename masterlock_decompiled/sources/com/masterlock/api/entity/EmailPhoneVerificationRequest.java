package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;

public class EmailPhoneVerificationRequest {
    @SerializedName("AlphaCountryCode")
    public String alphaCountyCode;
    @SerializedName("Email")
    public String email;
    @SerializedName("PhoneNumber")
    public String phone;
    @SerializedName("PhoneCountryCode")
    public String phoneCountryCode;
    @SerializedName("RedirectUrl")
    public String redirectUrl;
    @SerializedName("Source")
    public String source;
    @SerializedName("Id")
    public String verificationId;
}
