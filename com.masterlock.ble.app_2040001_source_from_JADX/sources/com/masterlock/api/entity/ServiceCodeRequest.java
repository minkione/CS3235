package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;

public class ServiceCodeRequest {
    @SerializedName("GuestId")
    String guestId;
    @SerializedName("Id")
    String kmsId;
    @SerializedName("Message")
    String message;
    @SerializedName("SendViaEmail")
    boolean shouldSendEmail;
    @SerializedName("SendViaSMS")
    boolean shouldSendSMS;

    public ServiceCodeRequest(String str, String str2, boolean z, boolean z2, String str3) {
        this.kmsId = str;
        this.guestId = str2;
        this.shouldSendEmail = z;
        this.shouldSendSMS = z2;
        this.message = str3;
    }

    public String getKmsID() {
        return this.kmsId;
    }

    public void setKmsId(String str) {
        this.kmsId = str;
    }

    public String getGuestId() {
        return this.guestId;
    }

    public void setGuestId(String str) {
        this.guestId = str;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public boolean isShouldSendEmail() {
        return this.shouldSendEmail;
    }

    public void setShouldSendEmail(boolean z) {
        this.shouldSendEmail = z;
    }

    public boolean isShouldSendSMS() {
        return this.shouldSendSMS;
    }

    public void setShouldSendSMS(boolean z) {
        this.shouldSendSMS = z;
    }
}
