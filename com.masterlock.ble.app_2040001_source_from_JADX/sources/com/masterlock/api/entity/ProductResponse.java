package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;
import com.masterlock.core.AccessType;
import com.masterlock.core.GuestPermissions;
import com.masterlock.core.Invitation;
import com.masterlock.core.Lock;
import com.masterlock.core.Lock.LockType;
import com.masterlock.core.LockStatus;
import com.masterlock.core.ProductCode;
import java.util.ArrayList;
import java.util.List;

public class ProductResponse {
    @SerializedName("CreatedOn")
    public String createdOn;
    @SerializedName("Id")

    /* renamed from: id */
    public String f161id;
    @SerializedName("Invitations")
    public List<Invitation> invitations;
    @SerializedName("KMSDevice")
    public KmsDevice kmsDevice = new KmsDevice();
    @SerializedName("Labels")
    public String labels;
    @SerializedName("Model")
    public Model model = new Model();
    @SerializedName("ModifiedOn")
    public String modifiedOn;
    @SerializedName("Name")
    public String name;
    @SerializedName("Value")
    public String notes;
    @SerializedName("Permissions")
    public GuestPermissions permissions = new GuestPermissions();
    @SerializedName("ProductCodes")
    public List<ProductCode> productCodes;
    @SerializedName("UserType")
    public AccessType userType = AccessType.UNKNOWN;

    public Lock getModel() {
        Lock lock = new Lock(this.f161id);
        lock.setName(this.name);
        lock.setNotes(this.notes);
        lock.setLabel(this.labels);
        lock.setAccessType(this.userType);
        lock.setCreatedOn(this.createdOn);
        lock.setModifiedOn(this.modifiedOn);
        lock.setModelId(this.model.f159id);
        lock.setModelInterfaceId(this.model.interfaceId);
        lock.setModelNumber(this.model.modelNumber);
        lock.setModelSku(this.model.sku);
        lock.setModelName(this.model.name);
        lock.setModelDescription(this.model.description);
        lock.setFirmwareVersion(this.kmsDevice.firmwareVersion);
        lock.setPrimaryCode(this.kmsDevice.primaryCode);
        lock.setPrimaryCodeCounter(this.kmsDevice.primaryCodeCounter);
        lock.setSecondaryCode1(this.kmsDevice.secondaryCode1);
        lock.setSecondaryCode2(this.kmsDevice.secondaryCode2);
        lock.setSecondaryCode3(this.kmsDevice.secondaryCode3);
        lock.setSecondaryCode4(this.kmsDevice.secondaryCode4);
        lock.setSecondaryCode5(this.kmsDevice.secondaryCode5);
        lock.setSecondaryCodeCounter(this.kmsDevice.secondaryCodeCounter);
        lock.setRemainingBatteryPercentage(this.kmsDevice.batteryLevel);
        lock.setTemperature(this.kmsDevice.temperature);
        lock.setTxPower(this.kmsDevice.txPower);
        lock.setTxInterval(this.kmsDevice.txInterval);
        lock.setLockMode(this.kmsDevice.lockMode);
        lock.setRelockTimeInSeconds(this.kmsDevice.relockInterval);
        lock.setLockStatus(this.kmsDevice.isInFirmwareUpdateMode ? LockStatus.UPDATE_MODE : this.kmsDevice.lockState);
        lock.setTimezone(this.kmsDevice.timezone);
        lock.setLastLogReferenceId(this.kmsDevice.lastLogReferenceId);
        lock.setTouchModeConfiguration(this.kmsDevice.touchModeConfiguration);
        lock.setProximitySwipeModeConfiguration(this.kmsDevice.proximitySwipeModeConfiguration);
        lock.setKmsCreatedOn(this.kmsDevice.createdOn);
        lock.setKmsModifiedOn(this.kmsDevice.modifiedOn);
        lock.setInvitations(this.invitations);
        lock.setLogs(this.kmsDevice.logs);
        lock.setCreatedOnTimeZoneAdjusted(this.kmsDevice.createdOnTimeZoneAdjusted);
        lock.setModifiedOnTimeZoneAdjusted(this.kmsDevice.modifiedOnTimeZoneAdjusted);
        lock.setPendingFirmwareUpdateConfirm(this.kmsDevice.isInFirmwareUpdateMode);
        lock.setFirmwareCounter(this.kmsDevice.firmwareCounter);
        lock.setLatitude(this.kmsDevice.location != null ? this.kmsDevice.location.latitude.replace(",", ".") : "");
        lock.setLongitude(this.kmsDevice.location != null ? this.kmsDevice.location.longitude.replace(",", ".") : "");
        lock.setLockType(LockType.fromKey(this.model.modelNumber));
        GuestPermissions guestPermissions = this.permissions;
        if (guestPermissions == null) {
            guestPermissions = new GuestPermissions();
        }
        lock.setPermissions(guestPermissions);
        lock.setKmsId((lock.isDialSpeedLock() || lock.isMechanicalLock()) ? null : this.kmsDevice.f157id);
        if (lock.isDialSpeedLock() || lock.isBiometricPadLock()) {
            List<ProductCode> list = this.productCodes;
            if (list == null) {
                lock.setProductCodes(new ArrayList());
            } else {
                lock.setProductCodes(list);
            }
        }
        if (this.kmsDevice.commands != null) {
            lock.setAvailableCommands(this.kmsDevice.commands.availableCommands);
            lock.setAvailableSettings(this.kmsDevice.commands.availableSettings);
            lock.setMemoryMapVersion(this.kmsDevice.commands.firmwareVersion);
        }
        return lock;
    }
}
