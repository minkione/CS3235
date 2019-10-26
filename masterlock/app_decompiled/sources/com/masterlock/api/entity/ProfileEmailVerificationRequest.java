package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;

public class ProfileEmailVerificationRequest {
    @SerializedName("Email")
    String email;
    @SerializedName("VerificationCode")
    String verificationCode;

    public ProfileEmailVerificationRequest(String str, String str2) {
        this.email = str;
        this.verificationCode = str2;
    }
}
