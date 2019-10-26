package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;
import com.masterlock.core.AvailableCommand;
import com.masterlock.core.AvailableSetting;
import java.util.List;

public class BlePermissionsResponse {
    @SerializedName("AvailableCommands")
    public List<AvailableCommand> availableCommands;
    @SerializedName("Settings")
    public List<AvailableSetting> availableSettings;
    @SerializedName("DeviceId")
    public String deviceId;
    @SerializedName("FirmwareVersion")
    public int firmwareVersion;
}
