package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;
import com.masterlock.core.KmsLogEntry;
import com.masterlock.core.Location;
import com.masterlock.core.LockMode;
import com.masterlock.core.LockStatus;
import java.util.List;

public class KmsDevice {
    @SerializedName("BatteryLevel")
    public int batteryLevel;
    @SerializedName("IIP")
    public BlePermissionsResponse commands;
    @SerializedName("CreatedOn")
    public String createdOn;
    @SerializedName("CreatedOnTimeZoneAdjusted")
    public String createdOnTimeZoneAdjusted;
    @SerializedName("FirmwareCounter")
    public int firmwareCounter;
    @SerializedName("FirmwareVersion")
    public int firmwareVersion;
    @SerializedName("Id")

    /* renamed from: id */
    public String f157id;
    @SerializedName("IsInFirmwareUpdateMode")
    public boolean isInFirmwareUpdateMode;
    @SerializedName("LastLogReferenceId")
    public long lastLogReferenceId;
    @SerializedName("Location")
    public Location location;
    @SerializedName("LockMode")
    public LockMode lockMode = LockMode.UNKNOWN;
    @SerializedName("LockState")
    public LockStatus lockState = LockStatus.UNKNOWN;
    @SerializedName("Logs")
    public List<KmsLogEntry> logs;
    @SerializedName("ModifiedOn")
    public String modifiedOn;
    @SerializedName("ModifiedOnTimeZoneAdjusted")
    public String modifiedOnTimeZoneAdjusted;
    @SerializedName("PrimaryCode")
    public String primaryCode;
    @SerializedName("PrimaryCodeCounter")
    public long primaryCodeCounter;
    @SerializedName("ProximitySwipeModeConfiguration")
    public String proximitySwipeModeConfiguration;
    @SerializedName("RelockInterval")
    public int relockInterval;
    @SerializedName("SecondaryCode1")
    public String secondaryCode1;
    @SerializedName("SecondaryCode2")
    public String secondaryCode2;
    @SerializedName("SecondaryCode3")
    public String secondaryCode3;
    @SerializedName("SecondaryCode4")
    public String secondaryCode4;
    @SerializedName("SecondaryCode5")
    public String secondaryCode5;
    @SerializedName("SecondaryCodeCounter")
    public long secondaryCodeCounter;
    @SerializedName("ServiceCode")
    public String serviceCode;
    @SerializedName("ServiceCodeExpiresOn")
    public String serviceCodeExpiresOn;
    @SerializedName("Tempature")
    public int temperature;
    @SerializedName("TimeZone")
    public String timezone;
    @SerializedName("TouchModeConfiguration")
    public String touchModeConfiguration;
    @SerializedName("TxInterval")
    public int txInterval;
    @SerializedName("TxPower")
    public int txPower;
}
