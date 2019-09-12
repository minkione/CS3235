package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;
import com.masterlock.core.Firmware;
import com.masterlock.core.Firmware.Builder;

public class FirmwareResponse {
    @SerializedName("NewFirmwareVersion")
    public int firmwareVersion;
    @SerializedName("IsFirmwareUpdateAvailable")
    public boolean isFirmwareUpdateAvailable;
    @SerializedName("KMSDeviceId")
    public String kmsDeviceId;
    @SerializedName("Description")
    public String lockDescription;
    @SerializedName("MinAndroidBuild")
    public int minAndroidBuild;
    @SerializedName("MinIOSBuild")
    public int minIOSBuild;
    @SerializedName("ReleaseDate")
    public String releaseDate;
    @SerializedName("Url")
    public String url;

    public Firmware getModel() {
        Builder builder = new Builder();
        builder.kmsDeviceId(this.kmsDeviceId).isFirmwareUpdateAvailable(Boolean.valueOf(this.isFirmwareUpdateAvailable)).firmwareVersion(this.firmwareVersion).lockDescription(this.lockDescription).minIOSBuild(this.minIOSBuild).minAndroidBuild(this.minAndroidBuild).releaseDate(this.releaseDate).url(this.url);
        return builder.build();
    }
}
