package com.masterlock.core;

import com.masterlock.core.ProductCode.Builder;
import java.util.ArrayList;
import java.util.List;

public class Lock {
    public static final String BIOMETRIC_PADLOCK_EXTERIORS = "4901";
    public static final String BIOMETRIC_PADLOCK_INTERIORS = "4900";
    public static final String DIAL_SPEED = "1500eD";
    public static final String KEY_SAFE_SHACKLED = "5440";
    public static final String KEY_SAFE_WALL_MOUNT = "5441";
    public static final String MECHANICAL_GENERIC = "generic1";
    public static final String PADLOCK_EXTERIORS = "4401";
    public static final String PADLOCK_INTERIORS = "4400";
    private String Latitude;
    private String Longitude;
    private AccessType accessType;
    private FirmwareDevAllAvailable allAvailableFirmwares;
    private List<AvailableCommand> availableCommands;
    private List<AvailableSetting> availableSettings;
    private CalibrationInfo calibrationInfo;
    private String createdOn;
    private String createdOnTimeZoneAdjusted;
    private boolean favorite;
    private int firmwareCounter = 0;
    private String firmwareFirstCommand;
    private String firmwareLastCommand;
    private String firmwareUpdateCommand;
    private List firmwareUpdateCommands;
    private int firmwareVersion;
    private boolean hasNewUpdate;
    private List<Invitation> invitations;
    private boolean isUpdating;
    private String kmsCreatedOn;
    private KmsDeviceKey kmsDeviceKey;
    private String kmsId;
    private String kmsModifiedOn;
    private String label;
    private long lastLogReferenceId;
    private long lastUnlocked;
    private long lastUnlockedShackle;
    private long lastUpdated;
    private String localizedTimeZone;
    private String lockId;
    private LockMode lockMode;
    private LockStatus lockStatus = LockStatus.UNKNOWN;
    private LockType lockType = LockType.PADLOCK_INTERIORS;
    private boolean lockerMode;
    private List<KmsLogEntry> logs;
    private String macAddress;
    private int measurementCounter = 0;
    private int memoryMapVersion = 0;
    public String modelDescription;
    public String modelId;
    public int modelInterfaceId;
    public String modelName;
    public String modelNumber;
    public String modelSku;
    private String modifiedOn;
    private String modifiedOnTimeZoneAdjusted;
    private String name;
    private boolean needsReturnTouch;
    private String notes;
    private int numberOfCommands;
    private boolean pendingFirmwareUpdateConfirm;
    private boolean pendingRestore;
    private GuestPermissions permissions;
    private String primaryCode;
    private long primaryCodeCounter = 0;
    private List<ProductCode> productCodes;
    private String proximitySwipeModeConfiguration;
    private int publicConfigCounter = 0;
    private int relockTimeInSeconds;
    private int remainingBatteryPercentage;
    private int requestedFirmware;
    private Integer rssiThreshold;
    private String secondaryCode1 = "";
    private String secondaryCode2 = "";
    private String secondaryCode3 = "";
    private String secondaryCode4 = "";
    private String secondaryCode5 = "";
    private long secondaryCodeCounter = 0;
    private int sectionNumber;
    private String serviceCode = "";
    private String serviceCodeExpiresOn = "0001-01-01T00:00:00+00:00";
    private ShackleStatus shackleStatus = ShackleStatus.UNKNOWN;
    private int temperature;
    private String timeZoneOffset;
    private String timezone;
    private int totalNumberOfCommands;
    private String touchModeConfiguration;
    private int txInterval;
    private int txPower;

    public enum LockType {
        PADLOCK_INTERIORS(Lock.PADLOCK_INTERIORS),
        PADLOCK_EXTERIORS("4401"),
        KEY_SAFE_WALL_MOUNT(Lock.KEY_SAFE_WALL_MOUNT),
        KEY_SAFE_SHACKLED(Lock.KEY_SAFE_SHACKLED),
        MECHANICAL_GENERIC(Lock.MECHANICAL_GENERIC),
        DIAL_SPEED(Lock.DIAL_SPEED),
        BIOMETRIC_PADLOCK_INTERIORS(Lock.BIOMETRIC_PADLOCK_INTERIORS),
        BIOMETRIC_PADLOCK_EXTERIORS(Lock.BIOMETRIC_PADLOCK_EXTERIORS);
        
        private String lockType;

        private LockType(String str) {
            this.lockType = str;
        }

        public String getLockTypeValue() {
            return this.lockType;
        }

        public static LockType fromKey(String str) {
            LockType[] values;
            for (LockType lockType2 : values()) {
                if (str.contains(lockType2.getLockTypeValue())) {
                    return lockType2;
                }
            }
            return PADLOCK_INTERIORS;
        }
    }

    public String getLatitude() {
        return this.Latitude;
    }

    public void setLatitude(String str) {
        this.Latitude = str;
    }

    public String getLongitude() {
        return this.Longitude;
    }

    public void setLongitude(String str) {
        this.Longitude = str;
    }

    public int getSectionNumber() {
        return this.sectionNumber;
    }

    public void setSectionNumber(int i) {
        this.sectionNumber = i;
    }

    public Lock(MasterLockMfrData masterLockMfrData) {
        this.lockId = String.valueOf(masterLockMfrData.getDeviceId());
    }

    public Lock(String str) {
        this.lockId = str;
    }

    public String getLockId() {
        return this.lockId;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public Integer getRssiThreshold() {
        return this.rssiThreshold;
    }

    public String getName() {
        return this.name;
    }

    public String getNotes() {
        return this.notes;
    }

    public String getLabel() {
        return this.label;
    }

    public boolean isFavorite() {
        return this.favorite;
    }

    public AccessType getAccessType() {
        return this.accessType;
    }

    public String getCreatedOn() {
        return this.createdOn;
    }

    public String getModifiedOn() {
        return this.modifiedOn;
    }

    public String getModelId() {
        return this.modelId;
    }

    public int getModelInterfaceId() {
        return this.modelInterfaceId;
    }

    public String getModelNumber() {
        return this.modelNumber;
    }

    public String getModelSku() {
        return this.modelSku;
    }

    public String getModelName() {
        return this.modelName;
    }

    public String getModelDescription() {
        return this.modelDescription;
    }

    public String getKmsId() {
        return this.kmsId;
    }

    public int getFirmwareVersion() {
        return this.firmwareVersion;
    }

    public String getPrimaryCode() {
        return this.primaryCode;
    }

    public String getSecondaryCode1() {
        return this.secondaryCode1;
    }

    public String getSecondaryCode2() {
        return this.secondaryCode2;
    }

    public String getSecondaryCode3() {
        return this.secondaryCode3;
    }

    public String getSecondaryCode4() {
        return this.secondaryCode4;
    }

    public String getSecondaryCode5() {
        return this.secondaryCode5;
    }

    public int getRemainingBatteryPercentage() {
        return this.remainingBatteryPercentage;
    }

    public int getTemperature() {
        return this.temperature;
    }

    public int getTxPower() {
        return this.txPower;
    }

    public int getTxInterval() {
        return this.txInterval;
    }

    public LockMode getLockMode() {
        return this.lockMode;
    }

    public int getRelockTimeInSeconds() {
        return this.relockTimeInSeconds;
    }

    public LockStatus getLockStatus() {
        return this.lockStatus;
    }

    public ShackleStatus getShackleStatus() {
        return this.shackleStatus;
    }

    public boolean isLockerMode() {
        return this.lockerMode;
    }

    public String getServiceCode() {
        return this.serviceCode;
    }

    public String getServiceCodeExpiresOn() {
        return this.serviceCodeExpiresOn;
    }

    public long getLastLogReferenceId() {
        return this.lastLogReferenceId;
    }

    public String getKmsCreatedOn() {
        return this.kmsCreatedOn;
    }

    public String getKmsModifiedOn() {
        return this.kmsModifiedOn;
    }

    public KmsDeviceKey getKmsDeviceKey() {
        return this.kmsDeviceKey;
    }

    public List<Invitation> getInvitations() {
        return this.invitations;
    }

    public List<KmsLogEntry> getLogs() {
        return this.logs;
    }

    public String getFirmwareUpdateCommand() {
        return this.firmwareUpdateCommand;
    }

    public List getFirmwareUpdateCommands() {
        return this.firmwareUpdateCommands;
    }

    public int getTotalNumberOfCommands() {
        return this.totalNumberOfCommands;
    }

    public void setTotalNumberOfCommands(int i) {
        this.totalNumberOfCommands = i;
    }

    public int getNumberOfCommands() {
        return this.numberOfCommands;
    }

    public int getMemoryMapVersion() {
        return this.memoryMapVersion;
    }

    public void setNumberOfCommands(int i) {
        this.numberOfCommands = i;
    }

    public void setFirmwareUpdateCommand(String str) {
        this.firmwareUpdateCommand = str;
    }

    public void setFirmwareUpdateCommands(List list) {
        this.firmwareUpdateCommands = list;
    }

    public void setLockId(String str) {
        this.lockId = str;
    }

    public void setMacAddress(String str) {
        this.macAddress = str;
    }

    public void setRssiThreshold(Integer num) {
        this.rssiThreshold = num;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setNotes(String str) {
        this.notes = str;
    }

    public void setLabel(String str) {
        this.label = str;
    }

    public void setFavorite(boolean z) {
        this.favorite = z;
    }

    public void setAccessType(AccessType accessType2) {
        this.accessType = accessType2;
    }

    public void setCreatedOn(String str) {
        this.createdOn = str;
    }

    public void setModifiedOn(String str) {
        this.modifiedOn = str;
    }

    public void setModelId(String str) {
        this.modelId = str;
    }

    public void setModelInterfaceId(int i) {
        this.modelInterfaceId = i;
    }

    public void setModelNumber(String str) {
        this.modelNumber = str;
    }

    public void setModelSku(String str) {
        this.modelSku = str;
    }

    public void setModelName(String str) {
        this.modelName = str;
    }

    public void setModelDescription(String str) {
        this.modelDescription = str;
    }

    public void setKmsId(String str) {
        this.kmsId = str;
    }

    public void setFirmwareVersion(int i) {
        this.firmwareVersion = i;
    }

    public void setPrimaryCode(String str) {
        this.primaryCode = str;
    }

    public void setSecondaryCode1(String str) {
        this.secondaryCode1 = str;
    }

    public void setSecondaryCode2(String str) {
        this.secondaryCode2 = str;
    }

    public void setSecondaryCode3(String str) {
        this.secondaryCode3 = str;
    }

    public void setSecondaryCode4(String str) {
        this.secondaryCode4 = str;
    }

    public void setSecondaryCode5(String str) {
        this.secondaryCode5 = str;
    }

    public void setRemainingBatteryPercentage(int i) {
        this.remainingBatteryPercentage = i;
    }

    public void setTemperature(int i) {
        this.temperature = i;
    }

    public void setTxPower(int i) {
        this.txPower = i;
    }

    public void setTxInterval(int i) {
        this.txInterval = i;
    }

    public void setLockMode(LockMode lockMode2) {
        this.lockMode = lockMode2;
    }

    public String getTimezone() {
        return this.timezone;
    }

    public void setTimezone(String str) {
        this.timezone = str;
    }

    public String getLocalizedTimeZone() {
        return this.localizedTimeZone;
    }

    public void setLocalizedTimeZone(String str) {
        this.localizedTimeZone = str;
    }

    public String getTimeZoneOffset() {
        return this.timeZoneOffset;
    }

    public void setTimeZoneOffset(String str) {
        this.timeZoneOffset = str;
    }

    public void setRelockTimeInSeconds(int i) {
        this.relockTimeInSeconds = i;
    }

    public void setLockStatus(LockStatus lockStatus2) {
        this.lockStatus = lockStatus2;
    }

    public void setShackleStatus(ShackleStatus shackleStatus2) {
        this.shackleStatus = shackleStatus2;
    }

    public void setLockerMode(boolean z) {
        this.lockerMode = z;
    }

    public void setServiceCode(String str) {
        this.serviceCode = str;
    }

    public void setServiceCodeExpiresOn(String str) {
        this.serviceCodeExpiresOn = str;
    }

    public void setLastLogReferenceId(long j) {
        this.lastLogReferenceId = j;
    }

    public void setKmsCreatedOn(String str) {
        this.kmsCreatedOn = str;
    }

    public void setKmsModifiedOn(String str) {
        this.kmsModifiedOn = str;
    }

    public void setKmsDeviceKey(KmsDeviceKey kmsDeviceKey2) {
        this.kmsDeviceKey = kmsDeviceKey2;
    }

    public void setInvitations(List<Invitation> list) {
        this.invitations = list;
    }

    public void setLogs(List<KmsLogEntry> list) {
        this.logs = list;
    }

    public void setMemoryMapVersion(int i) {
        this.memoryMapVersion = i;
    }

    public String getTouchModeConfiguration() {
        return this.touchModeConfiguration;
    }

    public void setTouchModeConfiguration(String str) {
        this.touchModeConfiguration = str;
    }

    public String getProximitySwipeModeConfiguration() {
        return this.proximitySwipeModeConfiguration;
    }

    public void setProximitySwipeModeConfiguration(String str) {
        this.proximitySwipeModeConfiguration = str;
    }

    public List<LockCodeDirection> generateLockPrimaryCodeList() {
        return LockCodeDirection.generateLockDirectionListFromStringCode(this.primaryCode);
    }

    public int getPublicConfigCounter() {
        return this.publicConfigCounter;
    }

    public void setPublicConfigCounter(int i) {
        this.publicConfigCounter = i;
    }

    public long getPrimaryCodeCounter() {
        return this.primaryCodeCounter;
    }

    public void setPrimaryCodeCounter(long j) {
        this.primaryCodeCounter = j;
    }

    public long getSecondaryCodeCounter() {
        return this.secondaryCodeCounter;
    }

    public void setSecondaryCodeCounter(long j) {
        this.secondaryCodeCounter = j;
    }

    public int getMeasurementCounter() {
        return this.measurementCounter;
    }

    public void setMeasurementCounter(int i) {
        this.measurementCounter = i;
    }

    public ProductCode generateProductCode(int i, String str, String str2) {
        return new Builder().setId("00000000-0000-0000-0000-000000000000").setDisplayOrder(i).setValue(str2).setName(str).setLockId(this.lockId).build();
    }

    public List<ProductCode> getProductCodes() {
        return this.productCodes;
    }

    public void setProductCodes(List<ProductCode> list) {
        this.productCodes = list;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Lock lock = (Lock) obj;
        String str = this.lockId;
        return str == null ? lock.lockId == null : str.equals(lock.lockId);
    }

    public int hashCode() {
        String str = this.lockId;
        if (str != null) {
            return str.hashCode();
        }
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Lock{lockId='");
        sb.append(this.lockId);
        sb.append('\'');
        sb.append(", macAddress='");
        sb.append(this.macAddress);
        sb.append('\'');
        sb.append(", rssiThreshold=");
        sb.append(this.rssiThreshold);
        sb.append(", name='");
        sb.append(this.name);
        sb.append('\'');
        sb.append(", notes='");
        sb.append(this.notes);
        sb.append('\'');
        sb.append(", label='");
        sb.append(this.label);
        sb.append('\'');
        sb.append(", favorite=");
        sb.append(this.favorite);
        sb.append(", accessType=");
        sb.append(this.accessType);
        sb.append(", createdOn='");
        sb.append(this.createdOn);
        sb.append('\'');
        sb.append(", modifiedOn='");
        sb.append(this.modifiedOn);
        sb.append('\'');
        sb.append(", lastUpdated=");
        sb.append(this.lastUpdated);
        sb.append(", lastUnlocked=");
        sb.append(this.lastUnlocked);
        sb.append(", lastUnlockedShackle=");
        sb.append(this.lastUnlockedShackle);
        sb.append(", firmwareUpdateCommand='");
        sb.append(this.firmwareUpdateCommand);
        sb.append('\'');
        sb.append(", firmwareFirstCommand='");
        sb.append(this.firmwareFirstCommand);
        sb.append('\'');
        sb.append(", firmwareLastCommand='");
        sb.append(this.firmwareLastCommand);
        sb.append('\'');
        sb.append(", firmwareUpdateCommands=");
        sb.append(this.firmwareUpdateCommands);
        sb.append(", totalNumberOfCommands=");
        sb.append(this.totalNumberOfCommands);
        sb.append(", numberOfCommands=");
        sb.append(this.numberOfCommands);
        sb.append(", isUpdating=");
        sb.append(this.isUpdating);
        sb.append(", hasNewUpdate=");
        sb.append(this.hasNewUpdate);
        sb.append(", requestedFirmware=");
        sb.append(this.requestedFirmware);
        sb.append(", allAvailableFirmwares=");
        sb.append(this.allAvailableFirmwares);
        sb.append(", modelId='");
        sb.append(this.modelId);
        sb.append('\'');
        sb.append(", modelInterfaceId=");
        sb.append(this.modelInterfaceId);
        sb.append(", modelNumber='");
        sb.append(this.modelNumber);
        sb.append('\'');
        sb.append(", modelSku='");
        sb.append(this.modelSku);
        sb.append('\'');
        sb.append(", modelName='");
        sb.append(this.modelName);
        sb.append('\'');
        sb.append(", modelDescription='");
        sb.append(this.modelDescription);
        sb.append('\'');
        sb.append(", kmsId='");
        sb.append(this.kmsId);
        sb.append('\'');
        sb.append(", firmwareVersion=");
        sb.append(this.firmwareVersion);
        sb.append(", firmwareCounter=");
        sb.append(this.firmwareCounter);
        sb.append(", publicConfigCounter=");
        sb.append(this.publicConfigCounter);
        sb.append(", primaryCodeCounter=");
        sb.append(this.primaryCodeCounter);
        sb.append(", measurementCounter=");
        sb.append(this.measurementCounter);
        sb.append(", primaryCode='");
        sb.append(this.primaryCode);
        sb.append('\'');
        sb.append(", remainingBatteryPercentage=");
        sb.append(this.remainingBatteryPercentage);
        sb.append(", temperature=");
        sb.append(this.temperature);
        sb.append(", txPower=");
        sb.append(this.txPower);
        sb.append(", txInterval=");
        sb.append(this.txInterval);
        sb.append(", lockMode=");
        sb.append(this.lockMode);
        sb.append(", relockTimeInSeconds=");
        sb.append(this.relockTimeInSeconds);
        sb.append(", lockStatus=");
        sb.append(this.lockStatus);
        sb.append(", shackleStatus=");
        sb.append(this.shackleStatus);
        sb.append(", lockerMode=");
        sb.append(this.lockerMode);
        sb.append(", timezone='");
        sb.append(this.timezone);
        sb.append('\'');
        sb.append(", serviceCode='");
        sb.append(this.serviceCode);
        sb.append('\'');
        sb.append(", serviceCodeExpiresOn='");
        sb.append(this.serviceCodeExpiresOn);
        sb.append('\'');
        sb.append(", lastLogReferenceId=");
        sb.append(this.lastLogReferenceId);
        sb.append(", kmsCreatedOn='");
        sb.append(this.kmsCreatedOn);
        sb.append('\'');
        sb.append(", kmsModifiedOn='");
        sb.append(this.kmsModifiedOn);
        sb.append('\'');
        sb.append(", touchModeConfiguration='");
        sb.append(this.touchModeConfiguration);
        sb.append('\'');
        sb.append(", proximitySwipeModeConfiguration='");
        sb.append(this.proximitySwipeModeConfiguration);
        sb.append('\'');
        sb.append(", createdOnTimeZoneAdjusted='");
        sb.append(this.createdOnTimeZoneAdjusted);
        sb.append('\'');
        sb.append(", modifiedOnTimeZoneAdjusted='");
        sb.append(this.modifiedOnTimeZoneAdjusted);
        sb.append('\'');
        sb.append(", kmsDeviceKey=");
        sb.append(this.kmsDeviceKey);
        sb.append(", invitations=");
        sb.append(this.invitations);
        sb.append(", logs=");
        sb.append(this.logs);
        sb.append('}');
        return sb.toString();
    }

    public String toJSONString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{lockId:'");
        sb.append(this.lockId);
        sb.append('\'');
        sb.append(", macAddress:'");
        sb.append(this.macAddress);
        sb.append('\'');
        sb.append(", rssiThreshold:'");
        sb.append(this.rssiThreshold);
        sb.append('\'');
        sb.append(", name:'");
        sb.append(this.name);
        sb.append('\'');
        sb.append(", notes:'");
        sb.append(this.notes);
        sb.append('\'');
        sb.append(", label:'");
        sb.append(this.label);
        sb.append('\'');
        sb.append(", favorite:");
        sb.append(this.favorite);
        sb.append(", accessType:");
        sb.append(this.accessType);
        sb.append(", createdOn:'");
        sb.append(this.createdOn);
        sb.append('\'');
        sb.append(", modifiedOn:'");
        sb.append(this.modifiedOn);
        sb.append('\'');
        sb.append(", modelId:'");
        sb.append(this.modelId);
        sb.append('\'');
        sb.append(", modelInterfaceId:");
        sb.append(this.modelInterfaceId);
        sb.append(", modelNumber:'");
        sb.append(this.modelNumber);
        sb.append('\'');
        sb.append(", modelSku:'");
        sb.append(this.modelSku);
        sb.append('\'');
        sb.append(", modelName:'");
        sb.append(this.modelName);
        sb.append('\'');
        sb.append(", modelDescription:'");
        sb.append(this.modelDescription);
        sb.append('\'');
        sb.append(", kmsId:'");
        sb.append(this.kmsId);
        sb.append('\'');
        sb.append(", firmwareVersion:");
        sb.append(this.firmwareVersion);
        sb.append(", primaryCode:'");
        sb.append(this.primaryCode);
        sb.append('\'');
        sb.append(", remainingBatteryPercentage:");
        sb.append(this.remainingBatteryPercentage);
        sb.append(", temperature:");
        sb.append(this.temperature);
        sb.append(", txPower:");
        sb.append(this.txPower);
        sb.append(", txInterval:");
        sb.append(this.txInterval);
        sb.append(", lockMode:");
        sb.append(this.lockMode);
        sb.append(", relockTimeInSeconds:");
        sb.append(this.relockTimeInSeconds);
        sb.append(", lockStatus:");
        sb.append(this.lockStatus);
        sb.append(", shackleStatus:");
        sb.append(this.shackleStatus);
        sb.append(", timezone:'");
        sb.append(this.timezone);
        sb.append('\'');
        sb.append(", serviceCode='");
        sb.append(this.serviceCode);
        sb.append('\'');
        sb.append(", serviceCodeExpiresOn:'");
        sb.append(this.serviceCodeExpiresOn);
        sb.append('\'');
        sb.append(", lastLogReferenceId:");
        sb.append(this.lastLogReferenceId);
        sb.append(", kmsCreatedOn:'");
        sb.append(this.kmsCreatedOn);
        sb.append('\'');
        sb.append(", kmsModifiedOn:'");
        sb.append(this.kmsModifiedOn);
        sb.append('\'');
        sb.append(", kmsDeviceKey:");
        sb.append(this.kmsDeviceKey);
        sb.append(", invitations:");
        sb.append(this.invitations);
        sb.append(", logs:");
        sb.append(this.logs);
        sb.append(", publicConfigCounter:");
        sb.append(String.valueOf(this.publicConfigCounter));
        sb.append(", primaryPasscodeCounter:");
        sb.append(String.valueOf(this.primaryCodeCounter));
        sb.append(", firmwareVersion:");
        sb.append(String.valueOf(this.firmwareVersion));
        sb.append(", firmwareCounter:");
        sb.append(String.valueOf(this.firmwareCounter));
        sb.append(", measurementCounter:");
        sb.append(String.valueOf(this.measurementCounter));
        sb.append('}');
        return sb.toString();
    }

    public void setFirmwareCounter(int i) {
        this.firmwareCounter = i;
    }

    public int getFirmwareCounter() {
        return this.firmwareCounter;
    }

    public long getLastUpdated() {
        return this.lastUpdated;
    }

    public void setLastUpdated(long j) {
        this.lastUpdated = j;
    }

    public long getLastUnlocked() {
        return this.lastUnlocked;
    }

    public long getLastUnlockedShackle() {
        return this.lastUnlockedShackle;
    }

    public void setLastUnlocked(long j) {
        this.lastUnlocked = j;
    }

    public void setLastUnlockedShackle(long j) {
        this.lastUnlockedShackle = j;
    }

    public String getFirmwareLastCommand() {
        return this.firmwareLastCommand;
    }

    public void setFirmwareLastCommand(String str) {
        this.firmwareLastCommand = str;
    }

    public String getFirmwareFirstCommand() {
        return this.firmwareFirstCommand;
    }

    public void setFirmwareFirstCommand(String str) {
        this.firmwareFirstCommand = str;
    }

    public boolean getHasNewUpdate() {
        return this.hasNewUpdate;
    }

    public void setHasNewUpdate(boolean z) {
        this.hasNewUpdate = z;
    }

    public boolean getIsUpdating() {
        return this.isUpdating;
    }

    public void setIsUpdating(boolean z) {
        this.isUpdating = z;
    }

    public int getRequestedFirmware() {
        return this.requestedFirmware;
    }

    public void setRequestedFirmware(int i) {
        this.requestedFirmware = i;
    }

    public FirmwareDevAllAvailable getAllAvailableFirmwares() {
        return this.allAvailableFirmwares;
    }

    public void setAllAvailableFirmwares(FirmwareDevAllAvailable firmwareDevAllAvailable) {
        this.allAvailableFirmwares = firmwareDevAllAvailable;
    }

    public String getCreatedOnTimeZoneAdjusted() {
        return this.createdOnTimeZoneAdjusted;
    }

    public void setCreatedOnTimeZoneAdjusted(String str) {
        this.createdOnTimeZoneAdjusted = str;
    }

    public String getModifiedOnTimeZoneAdjusted() {
        return this.modifiedOnTimeZoneAdjusted;
    }

    public void setModifiedOnTimeZoneAdjusted(String str) {
        this.modifiedOnTimeZoneAdjusted = str;
    }

    public boolean isPendingRestore() {
        return this.pendingRestore;
    }

    public void setPendingRestore(boolean z) {
        this.pendingRestore = z;
    }

    public boolean isPendingFirmwareUpdateConfirm() {
        return this.pendingFirmwareUpdateConfirm;
    }

    public void setPendingFirmwareUpdateConfirm(boolean z) {
        this.pendingFirmwareUpdateConfirm = z;
    }

    public LockType getLockType() {
        return this.lockType;
    }

    public void setLockType(LockType lockType2) {
        this.lockType = lockType2;
    }

    public boolean isPadLock() {
        return this.lockType == LockType.PADLOCK_INTERIORS || this.lockType == LockType.PADLOCK_EXTERIORS;
    }

    public boolean isMechanicalLock() {
        return this.lockType == LockType.MECHANICAL_GENERIC;
    }

    public boolean isDialSpeedLock() {
        return this.lockType == LockType.DIAL_SPEED;
    }

    public boolean isBiometricPadLock() {
        return this.lockType == LockType.BIOMETRIC_PADLOCK_INTERIORS || this.lockType == LockType.BIOMETRIC_PADLOCK_EXTERIORS;
    }

    public boolean isShackledKeySafe() {
        return this.lockType == LockType.KEY_SAFE_SHACKLED;
    }

    public GuestPermissions getPermissions() {
        return this.permissions;
    }

    public void setPermissions(GuestPermissions guestPermissions) {
        this.permissions = guestPermissions;
    }

    public List<AvailableCommand> getAvailableCommands() {
        return this.availableCommands;
    }

    public void setAvailableCommands(List<AvailableCommand> list) {
        this.availableCommands = list;
    }

    public List<AvailableSetting> getAvailableSettings() {
        return this.availableSettings;
    }

    public void setAvailableSettings(List<AvailableSetting> list) {
        this.availableSettings = list;
    }

    public boolean canSendCommand(AvailableCommandType availableCommandType) {
        boolean z = false;
        try {
            if (this.availableCommands.get(availableCommandType.getValue()) != null) {
                z = true;
            }
            return z;
        } catch (IndexOutOfBoundsException unused) {
            return false;
        }
    }

    public boolean needsReturnTouch() {
        return this.needsReturnTouch;
    }

    public void setNeedsReturnTouch(boolean z) {
        this.needsReturnTouch = z;
    }

    public String getSecondaryCodeAt(SecondaryCodeIndex secondaryCodeIndex) {
        switch (secondaryCodeIndex) {
            case SECONDARY_PASSCODE_2:
                return this.secondaryCode2;
            case SECONDARY_PASSCODE_3:
                return this.secondaryCode3;
            case SECONDARY_PASSCODE_4:
                return this.secondaryCode4;
            case SECONDARY_PASSCODE_5:
                return this.secondaryCode5;
            default:
                return this.secondaryCode1;
        }
    }

    public List<String> getAllSecondaryCodes() {
        ArrayList arrayList = new ArrayList(5);
        arrayList.add(this.secondaryCode1);
        arrayList.add(this.secondaryCode2);
        arrayList.add(this.secondaryCode3);
        arrayList.add(this.secondaryCode4);
        arrayList.add(this.secondaryCode5);
        return arrayList;
    }

    public void setSecondaryCodeAt(SecondaryCodeIndex secondaryCodeIndex, String str) {
        switch (secondaryCodeIndex) {
            case SECONDARY_PASSCODE_2:
                this.secondaryCode2 = str;
                return;
            case SECONDARY_PASSCODE_3:
                this.secondaryCode3 = str;
                return;
            case SECONDARY_PASSCODE_4:
                this.secondaryCode4 = str;
                return;
            case SECONDARY_PASSCODE_5:
                this.secondaryCode5 = str;
                return;
            default:
                this.secondaryCode1 = str;
                return;
        }
    }

    public CalibrationInfo getCalibrationInfo() {
        return this.calibrationInfo;
    }

    public void setCalibrationInfo(CalibrationInfo calibrationInfo2) {
        this.calibrationInfo = calibrationInfo2;
    }

    public boolean hasSkippedCalibration() {
        return this.calibrationInfo.hasSkipped();
    }

    public void setSkippedCalibration(boolean z) {
        this.calibrationInfo.setHasSkipped(z);
    }
}
