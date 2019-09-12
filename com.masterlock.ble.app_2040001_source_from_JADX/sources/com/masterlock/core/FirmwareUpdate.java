package com.masterlock.core;

import java.util.List;

public class FirmwareUpdate {
    public List commands;
    public String kMSReferenceHandler;
    public String kmsDeviceId;

    public static class Builder {
        public List commands;
        public String kMSReferenceHandler;
        public String kmsDeviceId;

        public Builder kmsDeviceId(String str) {
            this.kmsDeviceId = str;
            return this;
        }

        public Builder commands(List list) {
            this.commands = list;
            return this;
        }

        public Builder kMSReferenceHandler(String str) {
            this.kMSReferenceHandler = str;
            return this;
        }

        public Builder fromPrototype(FirmwareUpdate firmwareUpdate) {
            this.kmsDeviceId = firmwareUpdate.kmsDeviceId;
            this.commands = firmwareUpdate.commands;
            this.kMSReferenceHandler = firmwareUpdate.kMSReferenceHandler;
            return this;
        }

        public FirmwareUpdate build() {
            return new FirmwareUpdate(this);
        }
    }

    private FirmwareUpdate(Builder builder) {
        this.kmsDeviceId = builder.kmsDeviceId;
        this.commands = builder.commands;
        this.kMSReferenceHandler = builder.kMSReferenceHandler;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FirmwareUpdate{kmsDeviceId='");
        sb.append(this.kmsDeviceId);
        sb.append('\'');
        sb.append(", commands=");
        sb.append(this.commands);
        sb.append(", kMSReferenceHandler='");
        sb.append(this.kMSReferenceHandler);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }
}
