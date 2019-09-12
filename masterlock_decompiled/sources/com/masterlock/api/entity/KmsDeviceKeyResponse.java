package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;
import com.masterlock.core.AccessType;
import com.masterlock.core.KmsDeviceKey;
import com.masterlock.core.KmsDeviceKey.Builder;
import com.masterlock.core.ScheduleType;

public class KmsDeviceKeyResponse {
    @SerializedName("Alias")
    public int alias;
    @SerializedName("CreatedOn")
    public String createdOn;
    @SerializedName("DeviceId")
    public String deviceId;
    @SerializedName("ExpiresOn")
    public String expiresOn;
    @SerializedName("Id")

    /* renamed from: id */
    public String f158id;
    @SerializedName("KMSDeviceId")
    public String kmsDeviceId;
    @SerializedName("ModifiedOn")
    public String modifiedOn;
    @SerializedName("ProductInvitationId")
    public String productInvitationId;
    @SerializedName("Profile")
    public String profile;
    @SerializedName("ScheduleType")
    public ScheduleType scheduleType = ScheduleType.UNKNOWN;
    @SerializedName("UserID")
    public String userId;
    @SerializedName("UserType")
    public AccessType userType = AccessType.UNKNOWN;
    @SerializedName("Value")
    public String value;

    public KmsDeviceKey getModel() {
        Builder builder = new Builder();
        builder.mo20008id(this.f158id).kmsDeviceId(this.kmsDeviceId).deviceId(this.deviceId).value(this.value).profile(this.profile).userType(this.userType).scheduleType(this.scheduleType).userId(this.userId).alias(this.alias).productInvitationId(this.productInvitationId).expiresOn(this.expiresOn).createdOn(this.createdOn).modifiedOn(this.modifiedOn);
        return builder.build();
    }
}
