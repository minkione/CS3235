package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;

public class ProfileMobilePhoneVerificationRequest {
    @SerializedName("MobilePhoneNumber")
    String mobilePhone;
    @SerializedName("VerificationCode")
    String verificationCode;

    public ProfileMobilePhoneVerificationRequest(String str, String str2) {
        this.mobilePhone = str;
        this.verificationCode = str2;
    }
}
