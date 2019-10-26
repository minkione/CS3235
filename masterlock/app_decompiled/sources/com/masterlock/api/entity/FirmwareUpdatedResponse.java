package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;
import com.masterlock.core.FirmwareUpdated;
import com.masterlock.core.FirmwareUpdated.Builder;

public class FirmwareUpdatedResponse {
    @SerializedName("Alias")
    public int alias;
    @SerializedName("CreatedOn")
    public String createdOn;
    @SerializedName("DeviceId")
    public String deviceId;
    @SerializedName("DeviceName")
    public String deviceName;
    @SerializedName("ExpiresOn")
    public String expiresOn;
    @SerializedName("KMSDeviceId")
    public String kmsDeviceId;
    @SerializedName("ModifiedOn")
    public String modifiedOn;
    @SerializedName("ProductInvitationId")
    public String productInvitationId;
    @SerializedName("Profile")
    public String profile;
    @SerializedName("Id")
    public String responseId;
    @SerializedName("ScheduleType")
    public int scheduleType;
    @SerializedName("UserID")
    public String userID;
    @SerializedName("UserType")
    public int userType;
    @SerializedName("Value")
    public String value;

    public FirmwareUpdated getModel() {
        Builder builder = new Builder();
        builder.responseId(this.responseId).kmsDeviceId(this.kmsDeviceId).deviceId(this.deviceId).profile(this.profile).value(this.value).userType(this.userType).scheduleType(this.scheduleType).userID(this.userID).alias(this.alias).productInvitationId(this.productInvitationId).deviceName(this.deviceName).expiresOn(this.expiresOn).createdOn(this.createdOn).modifiedOn(this.modifiedOn);
        return builder.build();
    }
}
