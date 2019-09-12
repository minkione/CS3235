package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;

public class NotificationEventSettings {
    @SerializedName("DisplayOrder")
    private int displayOrder;
    @SerializedName("SendEmail")
    private Boolean emailNotificationState;
    @SerializedName("Editable")
    private Boolean enabled;
    @SerializedName("Name")
    private String eventName;
    @SerializedName("NotificationId")
    private String notificationId;
    @SerializedName("NotificationUserId")
    private String notificationUserId;
    @SerializedName("SendSms")
    private Boolean smsNotificationState;

    public enum NotificationItemState {
        SELECTED,
        UNSELECTED,
        DISABLED
    }

    public NotificationEventSettings(Boolean bool, Boolean bool2, Boolean bool3) {
        this.emailNotificationState = bool;
        this.smsNotificationState = bool2;
        this.enabled = bool3;
    }

    public NotificationEventSettings(String str, Boolean bool, Boolean bool2, Boolean bool3) {
        this(bool, bool2, bool3);
        this.eventName = str;
    }

    public Boolean getSmsNotificationState() {
        return this.smsNotificationState;
    }

    public void setSmsNotificationState(Boolean bool) {
        this.smsNotificationState = bool;
    }

    public String getEventName() {
        return this.eventName;
    }

    public void setEventName(String str) {
        this.eventName = str;
    }

    public Boolean getEmailNotificationState() {
        return this.emailNotificationState;
    }

    public void setEmailNotificationState(Boolean bool) {
        this.emailNotificationState = bool;
    }

    public Boolean isEnabled() {
        return this.enabled;
    }

    public int getDisplayOrder() {
        return this.displayOrder;
    }

    public String getNotificationId() {
        return this.notificationId;
    }

    public String getNotificationUserId() {
        return this.notificationUserId;
    }

    public NotificationItemState computeEmailNotificationState() {
        if (!this.enabled.booleanValue()) {
            return NotificationItemState.DISABLED;
        }
        return this.emailNotificationState.booleanValue() ? NotificationItemState.SELECTED : NotificationItemState.UNSELECTED;
    }

    public NotificationItemState computeSmsNotificationState(boolean z) {
        if (!z) {
            return NotificationItemState.DISABLED;
        }
        return this.smsNotificationState.booleanValue() ? NotificationItemState.SELECTED : NotificationItemState.UNSELECTED;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EventName: ");
        sb.append(this.eventName);
        sb.append(", emailNotificationState: ");
        sb.append(this.emailNotificationState);
        sb.append(", smsNotificationState: ");
        sb.append(this.smsNotificationState);
        sb.append(", isEnabled: ");
        sb.append(this.enabled);
        return sb.toString();
    }
}
