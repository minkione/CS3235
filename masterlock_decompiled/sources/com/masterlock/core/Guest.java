package com.masterlock.core;

import com.google.gson.annotations.SerializedName;

public class Guest {
    @SerializedName("Email")
    String email;
    @SerializedName("FirstName")
    String firstName;
    @SerializedName("Id")

    /* renamed from: id */
    String f188id;
    @SerializedName("LastName")
    String lastName;
    @SerializedName("MobileNumber")
    String mobileNumberE164;
    @SerializedName("Organization")
    String organization;
    @SerializedName("AlphaCountryCode")
    String phoneAlphaCountryCode;
    @SerializedName("PhoneCountryCode")
    String phoneCountryCode;

    public Guest() {
    }

    public Guest(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        this.firstName = str;
        this.lastName = str2;
        this.mobileNumberE164 = str3;
        this.phoneCountryCode = str4;
        this.phoneAlphaCountryCode = str5;
        this.email = str6;
        this.organization = str7;
    }

    public String getId() {
        return this.f188id;
    }

    public void setId(String str) {
        this.f188id = str;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String str) {
        this.firstName = str;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String str) {
        this.lastName = str;
    }

    public String getOrganization() {
        return this.organization;
    }

    public void setOrganization(String str) {
        this.organization = str;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public String getMobileNumberE164() {
        return this.mobileNumberE164;
    }

    public void setMobileNumberE164(String str) {
        this.mobileNumberE164 = str;
    }

    public String getAlphaCountryCode() {
        return this.phoneAlphaCountryCode;
    }

    public void setAlphaCountryCode(String str) {
        this.phoneAlphaCountryCode = str;
    }

    public String getPhoneCountryCode() {
        return this.phoneCountryCode;
    }

    public void setPhoneCountryCode(String str) {
        this.phoneCountryCode = str;
    }

    public String getDisplayableName() {
        StringBuilder sb = new StringBuilder();
        String str = this.firstName;
        if (str != null && !"".equals(str)) {
            sb.append(this.firstName);
            sb.append(" ");
        }
        String str2 = this.lastName;
        if (str2 != null && !"".equals(str2)) {
            sb.append(this.lastName);
        }
        return "".equals(sb.toString()) ? this.email : sb.toString().trim();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FirstName = ");
        sb.append(this.firstName);
        sb.append("\nLastName = ");
        sb.append(this.lastName);
        sb.append("\nOrganization = ");
        sb.append(this.organization);
        sb.append("\nMobileNumber = ");
        sb.append(this.mobileNumberE164);
        sb.append("\nCountryCode = ");
        sb.append(this.phoneCountryCode);
        sb.append("\nAlphaCountryCode = ");
        sb.append(this.phoneAlphaCountryCode);
        sb.append("\n");
        return sb.toString();
    }
}
