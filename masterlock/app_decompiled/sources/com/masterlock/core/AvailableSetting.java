package com.masterlock.core;

import com.google.gson.annotations.SerializedName;

public class AvailableSetting {
    @SerializedName("Address")
    int address;
    @SerializedName("ID")

    /* renamed from: id */
    public AvailableSettingType f183id;
    transient String kmsDeviceId;
    @SerializedName("Size")
    int size;
    transient String uuid;

    public static class Builder {
        public int address;

        /* renamed from: id */
        public AvailableSettingType f184id;
        public String kmsDeviceId;
        public int size;

        public Builder setId(AvailableSettingType availableSettingType) {
            this.f184id = availableSettingType;
            return this;
        }

        public Builder setAddress(int i) {
            this.address = i;
            return this;
        }

        public Builder setSize(int i) {
            this.size = i;
            return this;
        }

        public Builder setKmsDeviceId(String str) {
            this.kmsDeviceId = str;
            return this;
        }

        public AvailableSetting build() {
            return new AvailableSetting(this);
        }
    }

    public AvailableSetting(Builder builder) {
        this.f183id = builder.f184id;
        this.address = builder.address;
        this.size = builder.size;
        this.kmsDeviceId = builder.kmsDeviceId;
        StringBuilder sb = new StringBuilder();
        sb.append(builder.kmsDeviceId);
        sb.append("-");
        sb.append(builder.f184id.getValue());
        this.uuid = sb.toString();
    }

    public AvailableSettingType getId() {
        return this.f183id;
    }

    public void setId(AvailableSettingType availableSettingType) {
        this.f183id = availableSettingType;
    }

    public int getAddress() {
        return this.address;
    }

    public void setAddress(int i) {
        this.address = i;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int i) {
        this.size = i;
    }

    public String getKmsDeviceId() {
        return this.kmsDeviceId;
    }

    public void setKmsDeviceId(String str) {
        this.kmsDeviceId = str;
        createUuid();
    }

    public String getUuid() {
        return this.uuid;
    }

    public void createUuid() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.kmsDeviceId);
        sb.append("-");
        sb.append(this.f183id.getValue());
        this.uuid = sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AvailableSetting{id=");
        sb.append(this.f183id);
        sb.append(", address=");
        sb.append(this.address);
        sb.append(", size=");
        sb.append(this.size);
        sb.append(", kmsDeviceId='");
        sb.append(this.kmsDeviceId);
        sb.append('\'');
        sb.append(", uuid='");
        sb.append(this.uuid);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }
}
