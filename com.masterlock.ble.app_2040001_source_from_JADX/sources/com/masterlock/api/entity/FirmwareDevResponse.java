package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;
import com.masterlock.core.Firmware;
import com.masterlock.core.Firmware.Builder;

public class FirmwareDevResponse {
    @SerializedName("Description")
    private String description;
    @SerializedName("ReleaseDate")
    private String releaseDate;
    @SerializedName("ReleaseNotes")
    private String releaseNotes;
    @SerializedName("Version")
    private int version;

    public Firmware getModel() {
        Builder builder = new Builder();
        builder.lockDescription(this.description).releaseDate(this.releaseDate).firmwareVersion(this.version);
        return builder.build();
    }
}
