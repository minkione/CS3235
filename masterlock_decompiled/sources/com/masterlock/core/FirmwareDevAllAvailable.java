package com.masterlock.core;

import java.util.List;

public class FirmwareDevAllAvailable {
    /* access modifiers changed from: private */
    public List<FirmwareDev> firmwareVersions;
    /* access modifiers changed from: private */
    public String kmsDeviceId;

    public static class Builder {
        public List firmwareVersions;
        public String kmsDeviceId;

        public Builder kmsDeviceId(String str) {
            this.kmsDeviceId = str;
            return this;
        }

        public Builder firmwareVersions(List list) {
            this.firmwareVersions = list;
            return this;
        }

        public Builder fromPrototype(FirmwareDevAllAvailable firmwareDevAllAvailable) {
            this.kmsDeviceId = firmwareDevAllAvailable.kmsDeviceId;
            this.firmwareVersions = firmwareDevAllAvailable.firmwareVersions;
            return this;
        }

        public FirmwareDevAllAvailable build() {
            return new FirmwareDevAllAvailable(this);
        }
    }

    private FirmwareDevAllAvailable(Builder builder) {
        this.kmsDeviceId = builder.kmsDeviceId;
        this.firmwareVersions = builder.firmwareVersions;
    }

    public String getKmsDeviceId() {
        return this.kmsDeviceId;
    }

    public List<FirmwareDev> getFirmwareVersions() {
        return this.firmwareVersions;
    }
}
