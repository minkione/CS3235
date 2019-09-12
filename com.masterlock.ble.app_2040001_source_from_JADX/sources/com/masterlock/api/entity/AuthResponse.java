package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("AlphaCountryCode")
    public String alphaCountyCode;
    @SerializedName("Email")
    public String email;
    @SerializedName("IsTermsOfServiceCurrent")
    public boolean isTermsOfServiceCurrent;
    @SerializedName("MobilePhoneNumber")
    public String mobilePhoneNumber;
    @SerializedName("MobilePhoneNumberIsVerified")
    public boolean mobilePhoneNumberIsVerified;
    @SerializedName("PhoneCountryCode")
    public String phoneCountryCode;
    @SerializedName("TimeZone")
    public String timeZone;
    @SerializedName("Token")
    public String token;
    @SerializedName("UserFirstName")
    public String userFirstName;
    @SerializedName("UserLastName")
    public String userLastName;
    @SerializedName("Username")
    public String userName;
}
