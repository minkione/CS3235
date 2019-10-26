package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;
import com.masterlock.core.AccessType;
import com.masterlock.core.ScheduleType;

public class ProductInvitationRequest {
    @SerializedName("UserType")
    AccessType accessType;
    @SerializedName("GuestId")
    String guestId;
    @SerializedName("Id")

    /* renamed from: id */
    String f160id;
    @SerializedName("Message")
    String message;
    @SerializedName("ProductId")
    String productId;
    @SerializedName("ScheduleType")
    ScheduleType scheduleType;
    @SerializedName("SendViaEmail")
    boolean shouldSendEmail;
    @SerializedName("SendViaSMS")
    boolean shouldSendSMS;

    public ProductInvitationRequest(String str, String str2, AccessType accessType2, ScheduleType scheduleType2, boolean z, boolean z2, String str3) {
        this.productId = str;
        this.guestId = str2;
        this.accessType = accessType2;
        this.scheduleType = scheduleType2;
        this.shouldSendEmail = z;
        this.shouldSendSMS = z2;
        this.message = str3;
    }

    public String getId() {
        return this.f160id;
    }

    public void setId(String str) {
        this.f160id = str;
    }

    public String getProductId() {
        return this.productId;
    }

    public void setProductId(String str) {
        this.productId = str;
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

    public AccessType getAccessType() {
        return this.accessType;
    }

    public void setAccessType(AccessType accessType2) {
        this.accessType = accessType2;
    }

    public ScheduleType getScheduleType() {
        return this.scheduleType;
    }

    public void setScheduleType(ScheduleType scheduleType2) {
        this.scheduleType = scheduleType2;
    }
}
