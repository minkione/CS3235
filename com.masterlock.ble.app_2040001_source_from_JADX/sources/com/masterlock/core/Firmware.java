package com.masterlock.core;

public class Firmware {
    public int firmwareVersion;
    public boolean isFirmwareUpdateAvailable;
    public String kmsDeviceId;
    public String lockDescription;
    public int minAndroidBuild;
    public int minIOSBuild;
    public String releaseDate;
    public String url;

    public static class Builder {
        public int firmwareVersion;
        public boolean isFirmwareUpdateAvailable;
        public String kmsDeviceId;
        public String lockDescription;
        public int minAndroidBuild;
        public int minIOSBuild;
        public String releaseDate;
        public String url;

        public Builder kmsDeviceId(String str) {
            this.kmsDeviceId = str;
            return this;
        }

        public Builder isFirmwareUpdateAvailable(Boolean bool) {
            this.isFirmwareUpdateAvailable = bool.booleanValue();
            return this;
        }

        public Builder firmwareVersion(int i) {
            this.firmwareVersion = i;
            return this;
        }

        public Builder lockDescription(String str) {
            this.lockDescription = str;
            return this;
        }

        public Builder minIOSBuild(int i) {
            this.minIOSBuild = i;
            return this;
        }

        public Builder minAndroidBuild(int i) {
            this.minAndroidBuild = i;
            return this;
        }

        public Builder releaseDate(String str) {
            this.releaseDate = str;
            return this;
        }

        public Builder url(String str) {
            this.url = str;
            return this;
        }

        public Builder fromPrototype(Firmware firmware) {
            this.kmsDeviceId = firmware.kmsDeviceId;
            this.isFirmwareUpdateAvailable = firmware.isFirmwareUpdateAvailable;
            this.firmwareVersion = firmware.firmwareVersion;
            this.lockDescription = firmware.lockDescription;
            this.minIOSBuild = firmware.minIOSBuild;
            this.minAndroidBuild = firmware.minAndroidBuild;
            this.releaseDate = firmware.releaseDate;
            this.url = firmware.url;
            return this;
        }

        public Firmware build() {
            return new Firmware(this);
        }
    }

    private Firmware(Builder builder) {
        this.kmsDeviceId = builder.kmsDeviceId;
        this.isFirmwareUpdateAvailable = builder.isFirmwareUpdateAvailable;
        this.firmwareVersion = builder.firmwareVersion;
        this.lockDescription = builder.lockDescription;
        this.minIOSBuild = builder.minIOSBuild;
        this.minAndroidBuild = builder.minAndroidBuild;
        this.releaseDate = builder.releaseDate;
        this.url = builder.url;
    }
}
