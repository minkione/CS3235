package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;

public class CreateAccountRequest {
    public transient String email;
    @SerializedName("emailOptIn")
    public boolean emailOptIn;
    @SerializedName("emailVerificationId")
    public String emailVerificationId;
    @SerializedName("firstName")
    public String firstName;
    @SerializedName("lastName")
    public String lastName;
    @SerializedName("MobilePhoneNumberVerified")
    public boolean mobilePhoneNumberVerified;
    @SerializedName("password")
    public String passcode;
    @SerializedName("securityAnswer")
    public String securityAnswer;
    @SerializedName("securityQuestion")
    public String securityQuestion;
    @SerializedName("timeZone")
    public String timezone;
    @SerializedName("username")
    public String username;
}
