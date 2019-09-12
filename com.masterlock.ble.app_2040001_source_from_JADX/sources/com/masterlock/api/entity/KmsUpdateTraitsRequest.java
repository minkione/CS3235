package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;
import com.masterlock.core.Lock;
import java.util.ArrayList;
import java.util.List;

public class KmsUpdateTraitsRequest {
    @SerializedName("Traits")
    List<KmsDeviceTrait> mDeviceTraits;
    @SerializedName("Id")
    String mId;

    public KmsUpdateTraitsRequest(Lock lock, KmsDeviceTrait kmsDeviceTrait) {
        this.mId = lock.getKmsId();
        this.mDeviceTraits = new ArrayList();
        this.mDeviceTraits.add(kmsDeviceTrait);
    }

    public KmsUpdateTraitsRequest(Lock lock, List<KmsDeviceTrait> list) {
        this.mId = lock.getKmsId();
        this.mDeviceTraits = list;
    }

    public String getId() {
        return this.mId;
    }

    public void setId(String str) {
        this.mId = str;
    }

    public List<KmsDeviceTrait> getDeviceTraits() {
        return this.mDeviceTraits;
    }

    public void setDeviceTraits(List<KmsDeviceTrait> list) {
        this.mDeviceTraits = list;
    }
}
