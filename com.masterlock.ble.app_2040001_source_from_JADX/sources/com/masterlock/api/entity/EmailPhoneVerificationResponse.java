package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;

public class EmailPhoneVerificationResponse {
    @SerializedName("EmailShortCode")
    public String emailVerificationCode;
    @SerializedName("Error")
    public String error;
    @SerializedName("SmsShortCode")
    public String phoneVerificationCode;
    @SerializedName("Id")
    public String verificationId;
}
