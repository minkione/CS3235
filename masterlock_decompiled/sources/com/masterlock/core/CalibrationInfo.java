package com.masterlock.core;

public class CalibrationInfo {
    private boolean hasSkipped;

    /* renamed from: id */
    private String f185id;
    private String kmsDeviceId;
    private int value;

    public static class Builder {
        boolean hasSkipped;

        /* renamed from: id */
        String f186id;
        String kmsDeviceId;
        int value;

        public Builder setId(String str) {
            this.f186id = str;
            return this;
        }

        public Builder setKmsDeviceId(String str) {
            this.kmsDeviceId = str;
            return this;
        }

        public Builder setHasSkipped(boolean z) {
            this.hasSkipped = z;
            return this;
        }

        public Builder setValue(int i) {
            this.value = i;
            return this;
        }

        public CalibrationInfo build() {
            return new CalibrationInfo(this);
        }
    }

    public CalibrationInfo(Builder builder) {
        this.f185id = builder.f186id;
        this.kmsDeviceId = builder.kmsDeviceId;
        this.hasSkipped = builder.hasSkipped;
        this.value = builder.value;
    }

    public String getId() {
        return this.f185id;
    }

    public String getKmsDeviceId() {
        return this.kmsDeviceId;
    }

    public boolean hasSkipped() {
        return this.hasSkipped;
    }

    public int getValue() {
        return this.value;
    }

    public void setId(String str) {
        this.f185id = str;
    }

    public void setKmsDeviceId(String str) {
        this.kmsDeviceId = str;
    }

    public void setHasSkipped(boolean z) {
        this.hasSkipped = z;
    }

    public void setValue(int i) {
        this.value = i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CalibrationInfo{id='");
        sb.append(this.f185id);
        sb.append('\'');
        sb.append(", kmsDeviceId='");
        sb.append(this.kmsDeviceId);
        sb.append('\'');
        sb.append(", hasSkipped=");
        sb.append(this.hasSkipped);
        sb.append(", value=");
        sb.append(this.value);
        sb.append('}');
        return sb.toString();
    }
}
