package com.masterlock.core;

public class DeviceInformation {
    private String lockId;
    private String macAddress;
    private Integer rssiThreshold;

    public String getLockId() {
        return this.lockId;
    }

    public void setLockId(String str) {
        this.lockId = str;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public void setMacAddress(String str) {
        this.macAddress = str;
    }

    public Integer getRssiThreshold() {
        return this.rssiThreshold;
    }

    public void setRssiThreshold(Integer num) {
        this.rssiThreshold = num;
    }
}
