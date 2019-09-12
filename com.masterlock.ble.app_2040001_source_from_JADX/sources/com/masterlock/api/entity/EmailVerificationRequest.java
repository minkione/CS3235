package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class EmailVerificationRequest {
    @SerializedName("country")
    public String countryCode;
    @SerializedName("dateOfBirth")
    public Date dob;
    @SerializedName("email")
    public String email;
    @SerializedName("RedirectUrl")
    public String redirectUrl;
    @SerializedName("Source")
    public String source;
    @SerializedName("tosVersion")
    public int tosVersion;
}
