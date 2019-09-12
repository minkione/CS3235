package com.masterlock.core;

public class FirmwareUpdated {
    public int alias;
    public String createdOn;
    public String deviceId;
    public String deviceName;
    public String expiresOn;
    public String kmsDeviceId;
    public String modifiedOn;
    public String productInvitationId;
    public String profile;
    public String responseId;
    public int scheduleType;
    public String userID;
    public int userType;
    public String value;

    public static class Builder {
        public int alias;
        public String createdOn;
        public String deviceId;
        public String deviceName;
        public String expiresOn;
        public String kmsDeviceId;
        public String modifiedOn;
        public String productInvitationId;
        public String profile;
        public String responseId;
        public int scheduleType;
        public String userID;
        public int userType;
        public String value;

        public Builder responseId(String str) {
            this.responseId = str;
            return this;
        }

        public Builder kmsDeviceId(String str) {
            this.kmsDeviceId = str;
            return this;
        }

        public Builder deviceId(String str) {
            this.deviceId = str;
            return this;
        }

        public Builder profile(String str) {
            this.profile = str;
            return this;
        }

        public Builder value(String str) {
            this.value = str;
            return this;
        }

        public Builder userType(int i) {
            this.userType = i;
            return this;
        }

        public Builder scheduleType(int i) {
            this.scheduleType = i;
            return this;
        }

        public Builder userID(String str) {
            this.userID = str;
            return this;
        }

        public Builder alias(int i) {
            this.alias = i;
            return this;
        }

        public Builder productInvitationId(String str) {
            this.productInvitationId = str;
            return this;
        }

        public Builder deviceName(String str) {
            this.deviceName = str;
            return this;
        }

        public Builder expiresOn(String str) {
            this.expiresOn = str;
            return this;
        }

        public Builder createdOn(String str) {
            this.createdOn = str;
            return this;
        }

        public Builder modifiedOn(String str) {
            this.modifiedOn = str;
            return this;
        }

        public Builder fromPrototype(FirmwareUpdated firmwareUpdated) {
            this.responseId = firmwareUpdated.responseId;
            this.kmsDeviceId = firmwareUpdated.kmsDeviceId;
            this.deviceId = firmwareUpdated.deviceId;
            this.profile = firmwareUpdated.profile;
            this.value = firmwareUpdated.value;
            this.userType = firmwareUpdated.userType;
            this.scheduleType = firmwareUpdated.scheduleType;
            this.userID = firmwareUpdated.userID;
            this.alias = firmwareUpdated.alias;
            this.productInvitationId = firmwareUpdated.productInvitationId;
            this.deviceName = firmwareUpdated.deviceName;
            this.expiresOn = firmwareUpdated.expiresOn;
            this.createdOn = firmwareUpdated.createdOn;
            this.modifiedOn = firmwareUpdated.modifiedOn;
            return this;
        }

        public FirmwareUpdated build() {
            return new FirmwareUpdated(this);
        }
    }

    private FirmwareUpdated(Builder builder) {
        this.responseId = builder.responseId;
        this.kmsDeviceId = builder.kmsDeviceId;
        this.deviceId = builder.deviceId;
        this.profile = builder.profile;
        this.value = builder.value;
        this.userType = builder.userType;
        this.scheduleType = builder.scheduleType;
        this.userID = builder.userID;
        this.alias = builder.alias;
        this.productInvitationId = builder.productInvitationId;
        this.deviceName = builder.deviceName;
        this.expiresOn = builder.expiresOn;
        this.createdOn = builder.createdOn;
        this.modifiedOn = builder.modifiedOn;
    }
}
