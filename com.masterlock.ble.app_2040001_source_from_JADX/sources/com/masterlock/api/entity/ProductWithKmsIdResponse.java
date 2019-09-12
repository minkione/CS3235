package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;
import com.masterlock.core.Lock;

public class ProductWithKmsIdResponse extends ProductResponse {
    @SerializedName("KMSDeviceId")
    public String kmsDeviceId;

    public Lock getModel() {
        Lock model = super.getModel();
        model.setKmsId(this.kmsDeviceId);
        return model;
    }
}
