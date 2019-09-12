package com.masterlock.core;

import com.google.gson.annotations.SerializedName;

public class ProductCode {
    @SerializedName("DisplayOrder")
    int displayOrder;
    @SerializedName("Id")

    /* renamed from: id */
    String f197id;
    String lockId;
    @SerializedName("Name")
    String name;
    @SerializedName("Value")
    String value;

    public static class Builder {
        int displayOrder;

        /* renamed from: id */
        String f198id;
        String lockId;
        String name;
        String value;

        public Builder setId(String str) {
            this.f198id = str;
            return this;
        }

        public Builder setName(String str) {
            this.name = str;
            return this;
        }

        public Builder setValue(String str) {
            this.value = str;
            return this;
        }

        public Builder setDisplayOrder(int i) {
            this.displayOrder = i;
            return this;
        }

        public Builder setLockId(String str) {
            this.lockId = str;
            return this;
        }

        public ProductCode build() {
            return new ProductCode(this);
        }
    }

    public String getId() {
        return this.f197id;
    }

    public void setId(String str) {
        this.f197id = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public int getDisplayOrder() {
        return this.displayOrder;
    }

    public void setDisplayOrder(int i) {
        this.displayOrder = i;
    }

    public String getLockId() {
        return this.lockId;
    }

    public void setLockId(String str) {
        this.lockId = str;
    }

    public ProductCode(Builder builder) {
        this.f197id = builder.f198id;
        this.name = builder.name;
        this.value = builder.value;
        this.displayOrder = builder.displayOrder;
        this.lockId = builder.lockId;
    }
}
