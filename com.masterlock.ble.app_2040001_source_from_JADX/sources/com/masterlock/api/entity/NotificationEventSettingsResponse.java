package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;
import java.util.LinkedList;

public class NotificationEventSettingsResponse {
    @SerializedName("HasMobilePhoneNumber")
    private boolean hasPhoneNumber;
    @SerializedName("MobileNumberIsVerified")
    private boolean isPhoneVerified;
    @SerializedName("Settings")
    private LinkedList<NotificationEventSettings> notificationEventSettingsList = new LinkedList<>();

    public Boolean getPhoneVerified() {
        return Boolean.valueOf(this.isPhoneVerified);
    }

    public boolean hasPhoneNumber() {
        return this.hasPhoneNumber;
    }

    public LinkedList<NotificationEventSettings> getNotificationEventSettingsList() {
        return this.notificationEventSettingsList;
    }

    public NotificationEventSettingsResponse() {
    }

    public NotificationEventSettingsResponse(boolean z, boolean z2, LinkedList<NotificationEventSettings> linkedList) {
        this.isPhoneVerified = z;
        this.hasPhoneNumber = z2;
        this.notificationEventSettingsList = linkedList;
    }

    public void copyConstructor(NotificationEventSettingsResponse notificationEventSettingsResponse) {
        this.hasPhoneNumber = notificationEventSettingsResponse.hasPhoneNumber;
        this.isPhoneVerified = notificationEventSettingsResponse.isPhoneVerified;
        this.notificationEventSettingsList.clear();
        this.notificationEventSettingsList.addAll(notificationEventSettingsResponse.getNotificationEventSettingsList());
    }
}
