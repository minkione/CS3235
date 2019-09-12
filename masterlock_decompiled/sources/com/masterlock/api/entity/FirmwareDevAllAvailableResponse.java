package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;
import com.masterlock.core.FirmwareDev;
import com.masterlock.core.FirmwareDevAllAvailable;
import com.masterlock.core.FirmwareDevAllAvailable.Builder;
import java.util.List;

public class FirmwareDevAllAvailableResponse {
    @SerializedName("FirmwareVersions")
    private List<FirmwareDev> firmwareVersions;
    @SerializedName("KMSDeviceId")
    private String kmsDeviceId;

    public FirmwareDevAllAvailable getModel() {
        Builder builder = new Builder();
        builder.kmsDeviceId(this.kmsDeviceId).firmwareVersions(this.firmwareVersions);
        return builder.build();
    }
}
