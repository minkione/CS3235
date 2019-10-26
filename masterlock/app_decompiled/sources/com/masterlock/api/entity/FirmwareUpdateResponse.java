package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;
import com.masterlock.core.FirmwareUpdate;
import com.masterlock.core.FirmwareUpdate.Builder;
import java.util.ArrayList;

public class FirmwareUpdateResponse {
    @SerializedName("Commands")
    public ArrayList commands;
    @SerializedName("KMSReferenceHandler")
    public String kMSReferenceHandler;
    @SerializedName("KmsDeviceId")
    public String kmsDeviceId;

    public FirmwareUpdate getModel() {
        Builder builder = new Builder();
        builder.kmsDeviceId(this.kmsDeviceId).commands(this.commands).kMSReferenceHandler(this.kMSReferenceHandler);
        return builder.build();
    }
}
