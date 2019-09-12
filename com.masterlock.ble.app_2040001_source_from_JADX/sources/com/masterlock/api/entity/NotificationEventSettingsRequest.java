package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;

public class NotificationEventSettingsRequest {
    @SerializedName("SendEmail")
    private Boolean emailNotificationState;
    @SerializedName("NotificationId")
    private String notificationId;
    @SerializedName("NotificationUserId")
    private String notificationUserId;
    @SerializedName("SendSms")
    private Boolean smsNotificationState;

    public NotificationEventSettingsRequest(String str, String str2, Boolean bool, Boolean bool2) {
        this.notificationUserId = str;
        this.notificationId = str2;
        this.emailNotificationState = bool;
        this.smsNotificationState = bool2;
    }
}
