package com.masterlock.core;

import com.google.gson.annotations.SerializedName;

public class AvailableCommand {
    @SerializedName("ID")

    /* renamed from: id */
    public AvailableCommandType f181id;
    transient String kmsDeviceId;
    transient String uuid;

    public static class Builder {

        /* renamed from: id */
        public AvailableCommandType f182id;
        public String kmsDeviceId;

        public Builder setId(AvailableCommandType availableCommandType) {
            this.f182id = availableCommandType;
            return this;
        }

        public Builder setKmsDeviceid(String str) {
            this.kmsDeviceId = str;
            return this;
        }

        public AvailableCommand build() {
            return new AvailableCommand(this);
        }
    }

    public AvailableCommand(Builder builder) {
        this.f181id = builder.f182id;
        this.kmsDeviceId = builder.kmsDeviceId;
        StringBuilder sb = new StringBuilder();
        sb.append(builder.kmsDeviceId);
        sb.append("-");
        sb.append(builder.f182id.getValue());
        this.uuid = sb.toString();
    }

    public AvailableCommandType getId() {
        return this.f181id;
    }

    public void setId(AvailableCommandType availableCommandType) {
        this.f181id = availableCommandType;
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
        sb.append(this.f181id.getValue());
        this.uuid = sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AvailableCommand{id=");
        sb.append(this.f181id);
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
