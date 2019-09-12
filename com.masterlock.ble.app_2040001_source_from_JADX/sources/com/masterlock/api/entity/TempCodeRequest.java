package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;

public class TempCodeRequest {
    @SerializedName("Email")
    String email;
    @SerializedName("FirstName")
    String firstName;
    @SerializedName("Id")
    String kmsId;
    @SerializedName("LastName")
    String lastName;
    @SerializedName("Message")
    String message;
    @SerializedName("MobileNumber")
    String mobileNumber;

    public static class Builder {
        /* access modifiers changed from: private */
        public String email;
        /* access modifiers changed from: private */
        public String firstName;
        /* access modifiers changed from: private */
        public String kmsId;
        /* access modifiers changed from: private */
        public String lastName;
        /* access modifiers changed from: private */
        public String message;
        /* access modifiers changed from: private */
        public String mobileNumber;

        public Builder kmsId(String str) {
            this.kmsId = str;
            return this;
        }

        public Builder firstName(String str) {
            this.firstName = str;
            return this;
        }

        public Builder lastName(String str) {
            this.lastName = str;
            return this;
        }

        public Builder message(String str) {
            this.message = str;
            return this;
        }

        public Builder email(String str) {
            this.email = str;
            return this;
        }

        public Builder mobileNumber(String str) {
            this.mobileNumber = str;
            return this;
        }

        public Builder fromPrototype(TempCodeRequest tempCodeRequest) {
            this.kmsId = tempCodeRequest.kmsId;
            this.firstName = tempCodeRequest.firstName;
            this.lastName = tempCodeRequest.lastName;
            this.message = tempCodeRequest.message;
            this.email = tempCodeRequest.email;
            this.mobileNumber = tempCodeRequest.mobileNumber;
            return this;
        }

        public TempCodeRequest build() {
            return new TempCodeRequest(this);
        }
    }

    public TempCodeRequest(String str, String str2, String str3, String str4, String str5, String str6) {
        this.kmsId = str;
        this.firstName = str2;
        this.lastName = str3;
        this.message = str4;
        this.email = str5;
        this.mobileNumber = str6;
    }

    private TempCodeRequest(Builder builder) {
        this.kmsId = builder.kmsId;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.message = builder.message;
        this.email = builder.email;
        this.mobileNumber = builder.mobileNumber;
    }

    public void setKmsId(String str) {
        this.kmsId = str;
    }

    public void setFirstName(String str) {
        this.firstName = str;
    }

    public void setLastName(String str) {
        this.lastName = str;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public void setMobileNumber(String str) {
        this.mobileNumber = str;
    }

    public String getKmsId() {
        return this.kmsId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getMessage() {
        return this.message;
    }

    public String getEmail() {
        return this.email;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }
}
