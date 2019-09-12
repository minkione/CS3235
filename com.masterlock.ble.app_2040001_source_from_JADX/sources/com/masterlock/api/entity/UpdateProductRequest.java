package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;
import com.masterlock.core.ProductCode;
import java.util.List;

public class UpdateProductRequest {
    @SerializedName("id")
    public String lockId;
    @SerializedName("name")
    public String name;
    @SerializedName("value")
    public String notes;
    @SerializedName("productCodes")
    public List<ProductCode> productCodes;
}
